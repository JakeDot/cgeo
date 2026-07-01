package cgeo.geocaching.apps.navi;

import cgeo.geocaching.R;
import cgeo.geocaching.apps.App;
import cgeo.geocaching.location.Geopoint;
import cgeo.geocaching.models.Geocache;
import cgeo.geocaching.models.Waypoint;

import android.app.Activity;
import android.view.MenuItem;

import androidx.annotation.NonNull;

import java.util.Collections;
import java.util.List;

public final class NavigationAppFactory {

    private static final App NOOP_APP = new App() {
        @Override
        public boolean isInstalled() {
            return false;
        }

        @Override
        public boolean isUsableAsDefaultNavigationApp() {
            return false;
        }

        @NonNull
        @Override
        public String getName() {
            return "";
        }

        @Override
        public boolean isEnabled(@NonNull final Geocache cache) {
            return false;
        }
    };

    private NavigationAppFactory() {
        // utility class
    }

    public enum NavigationAppsEnum {
        COMPASS(0, R.string.pref_navigation_menu_compass),
        INTERNAL_MAP(2, R.string.pref_navigation_menu_internal_map);

        public final App app = NOOP_APP;
        public final int id;
        public final int preferenceKey;

        NavigationAppsEnum(final int id, final int preferenceKey) {
            this.id = id;
            this.preferenceKey = preferenceKey;
        }

        @Override
        public String toString() {
            return "";
        }
    }

    public static void showNavigationMenu(final Activity activity, final Geocache cache, final Waypoint waypoint, final Geopoint destination, final boolean showInternalMap, final boolean showDefaultNavigation, final int menuResToEnableOnDismiss) {
        // not available in light build
    }

    public static void showNavigationMenu(final Activity activity, final Geocache cache, final Waypoint waypoint, final Geopoint destination) {
        // not available in light build
    }

    static List<NavigationAppsEnum> getInstalledNavigationApps() {
        return Collections.emptyList();
    }

    static List<NavigationAppsEnum> getActiveNavigationApps() {
        return Collections.emptyList();
    }

    public static List<NavigationAppsEnum> getInstalledDefaultNavigationApps() {
        return Collections.emptyList();
    }

    public static boolean onMenuItemSelected(final MenuItem item, final Activity activity, final Geocache cache) {
        return false;
    }

    public static void startDefaultNavigationApplication(final int defaultNavigation, final Activity activity, final Geocache cache) {
        // not available in light build
    }

    public static void startDefaultNavigationApplication(final int defaultNavigation, final Activity activity, final Waypoint waypoint) {
        // not available in light build
    }

    public static void startDefaultNavigationApplication(final int defaultNavigation, final Activity activity, final Geopoint destination) {
        // not available in light build
    }

    public static App getDefaultNavigationApplication() {
        return NOOP_APP;
    }

    public static App getNavigationAppForId(final int navigationAppId) {
        return NOOP_APP;
    }
}
