package cgeo.geocaching.playservices;

import cgeo.geocaching.sensors.GeoData;

import android.content.Context;

import io.reactivex.rxjava3.core.Observable;

/**
 * The "lite" flavor has no Google Play Services dependency. {@link GooglePlayServices#isAvailable()}
 * always returns {@code false} in this flavor, so {@code Settings.useGooglePlayServices()} is
 * always false too, and these methods are never actually invoked by
 * LocationDataProvider (which is shared with the "full" flavors) - they only
 * need to satisfy the compiler. See the "full" flavor's version of this class
 * for the actual FusedLocationProvider-based implementation.
 */
public final class GoogleLocationProvider {

    private GoogleLocationProvider() {
        // utility class
    }

    public static Observable<GeoData> getMostPrecise(final Context context) {
        return Observable.error(new UnsupportedOperationException("Google Play Services is not available in this build"));
    }

    public static Observable<GeoData> getLowPower(final Context context) {
        return Observable.error(new UnsupportedOperationException("Google Play Services is not available in this build"));
    }
}
