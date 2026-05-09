package cgeo.geocaching.unifiedmap.tileproviders;

import cgeo.geocaching.R;
import cgeo.geocaching.settings.Settings;
import cgeo.geocaching.utils.LocalizationUtils;
import cgeo.geocaching.utils.Log;
import cgeo.geocaching.utils.ProcessUtils;
import cgeo.geocaching.utils.TextUtils;

import android.app.Activity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.PopupMenu;
import androidx.core.view.MenuCompat;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Strings;

public class TileProviderFactory {
    public static final int MAP_LANGUAGE_DEFAULT_ID = 432198765;

    private static final HashMap<String, AbstractTileProvider> tileProviders = new LinkedHashMap<>();
    private static String[] languages;

    private TileProviderFactory() {
        // Singleton
    }

    public static void addMapviewMenuItems(final Activity activity, final PopupMenu menu) {
        final Menu parentMenu = menu.getMenu();
        MenuCompat.setGroupDividerEnabled(parentMenu, true);

        final int currentTileProvider = Settings.getTileProvider().getNumericalId();
        final Set<String> hideTileproviders = Settings.getHideTileproviders();
        int i = 0;
        for (final AbstractTileProvider tileProvider : tileProviders.values()) {
            boolean hide = false;
            for (final String comp : hideTileproviders) {
                if (Strings.CS.equals(comp, tileProvider.getId())) {
                    hide = true;
                }
            }
            if (!hide) {
                final int id = tileProvider.getNumericalId();
                final String displayName = tileProvider.getDisplayName(null);
                parentMenu.add(R.id.menu_group_map_sources_online, id, i, displayName != null ? displayName : tileProvider.getTileProviderName()).setCheckable(true).setChecked(id == currentTileProvider);
            }
            i++;
        }
        final AbstractTileProvider ctp = Settings.getTileProvider();
        parentMenu.setGroupCheckable(R.id.menu_group_map_sources_online, true, true);
        parentMenu.findItem(R.id.menu_hillshading).setCheckable(true).setChecked(Settings.getMapShadingShowLayer()).setVisible(false);
        parentMenu.findItem(R.id.menu_backgroundmap).setCheckable(true).setChecked(Settings.getMapBackgroundMapLayer()).setVisible(false);
        parentMenu.findItem(R.id.menu_check_hillshadingdata).setVisible(false);
        parentMenu.findItem(R.id.menu_download_backgroundmap).setVisible(false);
        parentMenu.findItem(R.id.menu_check_routingdata).setVisible(Settings.useInternalRouting() || ProcessUtils.isInstalled(LocalizationUtils.getPlainString(R.string.package_brouter)));
    }

    public static HashMap<String, AbstractTileProvider> getTileProviders() {
        buildTileProviderList(false);
        return tileProviders;
    }

    public static void buildTileProviderList(final boolean force) {
        if (!(tileProviders.isEmpty() || force)) {
            return;
        }
        tileProviders.clear();

        // Google Maps tile providers only (light build does not support offline/OSM maps)
        if (isGoogleMapsInstalled()) {
            registerTileProvider(new GoogleMapSource());
            registerTileProvider(new GoogleSatelliteSource());
            registerTileProvider(new GoogleTerrainSource());
        }
    }

    private static boolean isGoogleMapsInstalled() {
        final String mapsKey = LocalizationUtils.getPlainString(R.string.maps_api2_key);
        if (StringUtils.length(mapsKey) < 30 || Strings.CS.contains(mapsKey, "key")) {
            Log.w("No Google API key available.");
            return false;
        }

        try {
            Class.forName("com.google.android.gms.maps.SupportMapFragment");
        } catch (final ClassNotFoundException ignored) {
            return false;
        }

        return true;
    }

    private static void registerTileProvider(final AbstractTileProvider tileProvider) {
        tileProviders.put(tileProvider.getId(), tileProvider);
    }

    @Nullable
    public static AbstractTileProvider getTileProvider(final String stringId) {
        buildTileProviderList(false);
        return tileProviders.get(stringId);
    }

    @Nullable
    public static AbstractTileProvider getTileProvider(final int id) {
        for (final AbstractTileProvider tileProvider : tileProviders.values()) {
            if (tileProvider.getNumericalId() == id) {
                return tileProvider;
            }
        }
        return null;
    }

    public static AbstractTileProvider getAnyTileProvider() {
        buildTileProviderList(false);
        return tileProviders.isEmpty() ? null : tileProviders.entrySet().iterator().next().getValue();
    }

    public static void addMapViewLanguageMenuItems(final Menu menu) {
        final MenuItem parentMenu = menu.findItem(R.id.menu_select_language);
        if (languages != null) {
            final int currentLanguage = Settings.getMapLanguageId();
            final SubMenu subMenu = parentMenu.getSubMenu();
            subMenu.clear();
            subMenu.add(R.id.menu_group_map_languages, MAP_LANGUAGE_DEFAULT_ID, 0, R.string.switch_default).setCheckable(true).setChecked(MAP_LANGUAGE_DEFAULT_ID == currentLanguage);
            for (int i = 0; i < languages.length; i++) {
                final int languageId = languages[i].hashCode();
                subMenu.add(R.id.menu_group_map_languages, languageId, i, languages[i]).setCheckable(true).setChecked(languageId == currentLanguage);
            }
            subMenu.setGroupCheckable(R.id.menu_group_map_languages, true, true);
            parentMenu.setVisible(languages.length > 1);
        } else {
            parentMenu.setVisible(false);
        }
    }

    @Nullable
    public static String getLanguage(final int id) {
        if (languages != null) {
            for (final String language : languages) {
                if (language.hashCode() == id) {
                    return language;
                }
            }
        }
        return null;
    }

    public static void setLanguages(final String[] newLanguages) {
        languages = newLanguages;
    }

    public static void resetLanguages() {
        languages = new String[]{};
    }
}
