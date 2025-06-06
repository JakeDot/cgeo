package cgeo.geocaching;

import cgeo.geocaching.activity.AbstractActivity;
import cgeo.geocaching.activity.AbstractNavigationBarMapActivity;
import cgeo.geocaching.activity.ActivityMixin;
import cgeo.geocaching.activity.INavigationSource;
import cgeo.geocaching.enumerations.LoadFlags;
import cgeo.geocaching.location.Geopoint;
import cgeo.geocaching.location.Units;
import cgeo.geocaching.log.LoggingUI;
import cgeo.geocaching.models.Geocache;
import cgeo.geocaching.sensors.GeoData;
import cgeo.geocaching.sensors.GeoDirHandler;
import cgeo.geocaching.storage.DataStore;
import cgeo.geocaching.ui.CacheDetailsCreator;
import cgeo.geocaching.ui.ViewUtils;
import cgeo.geocaching.utils.Log;

import android.app.Activity;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import io.reactivex.rxjava3.disposables.CompositeDisposable;

public abstract class AbstractDialogFragment extends Fragment implements CacheMenuHandler.ActivityInterface, INavigationSource {
    public static final int RESULT_CODE_SET_TARGET = Activity.RESULT_FIRST_USER;
    public static final int REQUEST_CODE_TARGET_INFO = 1;
    protected static final String GEOCODE_ARG = "GEOCODE";
    protected static final String WAYPOINT_ARG = "WAYPOINT";
    private final CompositeDisposable resumeDisposables = new CompositeDisposable();
    protected Resources res = null;
    protected String geocode;
    protected CacheDetailsCreator details;
    protected Geocache cache;
    private TextView cacheDistance = null;
    private final GeoDirHandler geoUpdate = new GeoDirHandler() {

        @Override
        public void updateGeoData(final GeoData geo) {
            try {
                if (cacheDistance != null && cache != null && cache.getCoords() != null) {
                    cacheDistance.setText(Units.getDistanceFromKilometers(geo.getCoords().distanceTo(cache.getCoords())));
                    cacheDistance.bringToFront();
                }
                onUpdateGeoData(geo);
            } catch (final RuntimeException e) {
                Log.w("Failed to update location", e);
            }
        }
    };

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        res = getResources();
        setHasOptionsMenu(true);
    }

    @Override
    public void onStart() {
        super.onStart();
        geocode = getArguments().getString(GEOCODE_ARG);
    }

    protected void init() {
        cache = DataStore.loadCache(geocode, LoadFlags.LOAD_CACHE_OR_DB);

        if (cache == null) {
            ((AbstractActivity) requireActivity()).showToast(res.getString(R.string.err_detail_cache_find));

            ((AbstractNavigationBarMapActivity) requireActivity()).sheetRemoveFragment();
            return;
        }

        geocode = cache.getGeocode();
    }

    @Override
    public void onResume() {
        super.onResume();
        // resume location access
        resumeDisposables.add(geoUpdate.start(GeoDirHandler.UPDATE_GEODATA));
        init();
    }


    @Override
    public void onPause() {
        resumeDisposables.clear();
        super.onPause();
    }

    protected final void addCacheDetails(final boolean showGeocode) {
        assert cache != null;

        // cache type
        final String cacheType = cache.getType().getL10n();
        final String cacheSize = cache.showSize() ? " (" + cache.getSize().getL10n() + ")" : "";
        details.add(R.string.cache_type, cacheType + cacheSize);

        if (showGeocode) {
            details.add(R.string.cache_geocode, cache.getShortGeocode());
        }
        details.addCacheState(cache);

        cacheDistance = details.addDistance(cache, cacheDistance);

        details.addDifficultyTerrain(cache);
        details.addEventDate(cache);

        // rating
        if (cache.getRating() > 0) {
            details.addRating(cache);
        }

        // favorite count
        final int favCount = cache.getFavoritePoints();
        if (favCount >= 0) {
            final int findsCount = cache.getFindsCount();
            if (findsCount > 0) {
                details.add(R.string.cache_favorite, res.getString(R.string.favorite_count_percent, favCount, (float) (favCount * 100) / findsCount));
            } else if (!cache.isEventCache()) {
                details.add(R.string.cache_favorite, res.getString(R.string.favorite_count, favCount));
            }
        }

        details.addBetterCacher(cache);
        details.addCoordinates(cache.getCoords());

        // Latest logs
        details.addLatestLogs(cache);

        // more details
        final View view = getView();
        assert view != null;
        final Button buttonMore = view.findViewById(R.id.more_details);

        buttonMore.setOnClickListener(arg0 -> {
            CacheDetailActivity.startActivity(getActivity(), geocode);
            ((AbstractNavigationBarMapActivity) requireActivity()).sheetRemoveFragment();
        });

        /* Only working combination as it seems */
        registerForContextMenu(buttonMore);
    }

    public final void showToast(final String text) {
        ActivityMixin.showToast(getActivity(), text);
    }

    /**
     * @param geo location
     */
    protected void onUpdateGeoData(final GeoData geo) {
        // do nothing by default
    }

    /**
     * Set the current popup coordinates as new navigation target on map
     */
    private void setAsTarget() {
        final TargetUpdateReceiver activity = (TargetUpdateReceiver) requireActivity();
        activity.onReceiveTargetUpdate(getTargetInfo());
        ((AbstractNavigationBarMapActivity) requireActivity()).sheetRemoveFragment();
    }

    public static void onCreatePopupOptionsMenu(final Toolbar toolbar, final INavigationSource navigationSource, final Geocache geocache) {
        final Menu menu = toolbar.getMenu();
        menu.clear();
        toolbar.inflateMenu(R.menu.cache_options);
        CacheMenuHandler.onPrepareOptionsMenu(menu, geocache, true);
        CacheMenuHandler.initDefaultNavigationMenuItem(menu, navigationSource);
        ViewUtils.extendMenuActionBarDisplayItemCount(toolbar.getContext(), menu);
        menu.findItem(R.id.menu_target).setVisible(true);
        LoggingUI.onPrepareOptionsMenu(menu, geocache);
    }

    public boolean onPopupOptionsItemSelected(@NonNull final MenuItem item) {
        if (item.getItemId() == R.id.menu_target) {
            setAsTarget();
            return true;
        }

        if (CacheMenuHandler.onMenuItemSelected(item, this, cache, this::init, true)) {
            return true;
        }

        return LoggingUI.onMenuItemSelected(item, getActivity(), cache, dialog -> init());
    }

    protected abstract TargetInfo getTargetInfo();

    @Override
    public void navigateTo() {
        startDefaultNavigation();
    }

    @Override
    public void cachesAround() {
        final TargetInfo targetInfo = getTargetInfo();
        if (targetInfo == null || targetInfo.coords == null) {
            showToast(res.getString(R.string.err_location_unknown));
            return;
        }
        CacheListActivity.startActivityCoordinates((AbstractActivity) getActivity(), targetInfo.coords, cache != null ? cache.getName() : null);
    }

    public interface TargetUpdateReceiver {
        void onReceiveTargetUpdate(TargetInfo targetInfo);
    }

    public static class TargetInfo implements Parcelable {

        public static final Parcelable.Creator<TargetInfo> CREATOR = new Parcelable.Creator<TargetInfo>() {
            @Override
            public TargetInfo createFromParcel(final Parcel in) {
                return new TargetInfo(in);
            }

            @Override
            public TargetInfo[] newArray(final int size) {
                return new TargetInfo[size];
            }
        };
        public final Geopoint coords;
        public final String geocode;

        public TargetInfo(final Geopoint coords, final String geocode) {
            this.coords = coords;
            this.geocode = geocode;
        }

        public TargetInfo(final Parcel in) {
            this.coords = in.readParcelable(Geopoint.class.getClassLoader());
            this.geocode = in.readString();
        }

        @Override
        public void writeToParcel(final Parcel dest, final int flags) {
            dest.writeParcelable(coords, PARCELABLE_WRITE_RETURN_VALUE);
            dest.writeString(geocode);
        }

        @Override
        public int describeContents() {
            return 0;
        }
    }
}
