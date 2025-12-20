package cgeo.geocaching.apps;

import cgeo.geocaching.models.Geocache;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public interface App {

    class Over extends AbstractApp {

        public Over(@NonNull App app) {
            this(app.getName(), null, null);
        }

        public Over(@NonNull String name, @Nullable String intent, @Nullable String packageName) {
            super(name, intent, packageName);
        }
    }

    boolean isInstalled();

    /**
     * Whether or not an application can be used as the default navigation.
     */
    boolean isUsableAsDefaultNavigationApp();

    @NonNull
    String getName();

    /**
     * Whether or not the app can be used with the given cache (may depend on properties of the cache).
     */
    boolean isEnabled(@NonNull Geocache cache);
}
