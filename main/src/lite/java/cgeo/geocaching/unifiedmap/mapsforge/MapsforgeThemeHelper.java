package cgeo.geocaching.unifiedmap.mapsforge;

/**
 * The "lite" flavor has no Mapsforge (CPU) map engine, so there is no map
 * theme folder to synchronize. See the "full" flavor's version of this class
 * for the actual implementation used by basic/nojit/foss.
 */
public final class MapsforgeThemeHelper {

    private MapsforgeThemeHelper() {
        // utility class
    }

    public static boolean isThemeSynchronizationActive() {
        return false;
    }

    public static void resynchronizeOrDeleteMapThemeFolder() {
        // nothing to do: no Mapsforge (CPU) engine in the "lite" flavor
    }
}
