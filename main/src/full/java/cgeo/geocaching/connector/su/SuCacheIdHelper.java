package cgeo.geocaching.connector.su;

import cgeo.geocaching.connector.IConnector;

import androidx.annotation.Nullable;

/**
 * Extracted from Geocache.getCacheId() so that class does not need to depend
 * directly on SuConnector, which only exists in the "full" flavors.
 */
public final class SuCacheIdHelper {

    private SuCacheIdHelper() {
        // utility class
    }

    @Nullable
    public static String getCacheId(final IConnector connector, final String geocode) {
        if (connector instanceof SuConnector) {
            return SuConnector.geocodeToId(geocode);
        }
        return null;
    }
}
