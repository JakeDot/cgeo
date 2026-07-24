package cgeo.geocaching.playservices;

/**
 * The "lite" flavor has no Google Play Services dependency, so it is never
 * available. See the "full" flavor's version of this class for the actual
 * check used by basic/nojit/foss.
 */
public final class GooglePlayServices {

    private GooglePlayServices() {
        // utility class
    }

    /**
     * Check if Google Play services is available on this device
     */
    public static boolean isAvailable() {
        return false;
    }
}
