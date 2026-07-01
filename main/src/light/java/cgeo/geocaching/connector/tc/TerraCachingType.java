package cgeo.geocaching.connector.tc;

import cgeo.geocaching.enumerations.CacheType;

import androidx.annotation.NonNull;

/** TerraCachingType stub for light build. */
public final class TerraCachingType {

    private TerraCachingType() {
    }

    public static CacheType getCacheType(@NonNull final String style) {
        return CacheType.UNKNOWN;
    }
}
