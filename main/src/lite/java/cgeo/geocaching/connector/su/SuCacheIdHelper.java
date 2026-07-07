package cgeo.geocaching.connector.su;

import cgeo.geocaching.connector.IConnector;

import androidx.annotation.Nullable;

/**
 * The "lite" flavor has no Geocaching.su connector, so there is never an
 * extra cache id to derive. See the "full" flavor's version of this class.
 */
public final class SuCacheIdHelper {

    private SuCacheIdHelper() {
        // utility class
    }

    @Nullable
    public static String getCacheId(final IConnector connector, final String geocode) {
        return null;
    }
}
