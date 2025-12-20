package cgeo.geocaching.apps;

import androidx.annotation.NonNull;

import java.util.List;

import cgeo.geocaching.models.Geocache;
import cgeo.geocaching.storage.DataStore;

public enum JakeDotApp implements App {
    JAKEDOT_NET_APP;

    public static final String JAKEDOT_NET = "jakedot.net";

    public static App app() {
        return new App.Over(JAKEDOT_NET_APP);
    }

    @Override
    public boolean isInstalled() {
        return true;
    }

    @Override
    public boolean isUsableAsDefaultNavigationApp() {
        return true;
    }

    @NonNull
    @Override
    public String getName() {
        return "JakeDot.net App";
    }

    @Override
    public boolean isEnabled(@NonNull Geocache cache) {
        return (cache.getShortDescription() + cache.getDescription()).contains(JAKEDOT_NET);
    }

    public List<Geocache> supportedGeocaches() {
        List<Geocache> caches = DataStore.getLastOpenedCaches();

        caches = caches.stream().filter(this::isEnabled).toList();

        return caches;
    }
}
