package cgeo.geocaching.downloader;

/**
 * The "full" flavors (basic/nojit/foss) always have online tile sources
 * available as a fallback, so there is no need to pre-seed a default offline
 * map. See the "lite" flavor's version of this class, which downloads a
 * default world map on first run since lite has no online tile sources.
 */
public final class DefaultOfflineMapDownloader {

    private DefaultOfflineMapDownloader() {
        // utility class
    }

    public static void downloadIfNeededInBackground() {
        // nothing to do
    }
}
