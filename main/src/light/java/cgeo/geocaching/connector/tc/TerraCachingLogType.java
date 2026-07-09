package cgeo.geocaching.connector.tc;

import cgeo.geocaching.log.LogType;

import androidx.annotation.NonNull;

/** TerraCachingLogType stub for light build. */
public final class TerraCachingLogType {

    private TerraCachingLogType() {
    }

    public static LogType getLogType(@NonNull final String logtype) {
        return LogType.UNKNOWN;
    }
}
