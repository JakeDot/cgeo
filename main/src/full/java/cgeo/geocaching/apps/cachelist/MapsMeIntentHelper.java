package cgeo.geocaching.apps.cachelist;

import android.content.Context;
import android.content.Intent;

import androidx.annotation.Nullable;

/**
 * Extracted from CacheDetailActivity so that class does not need to depend
 * directly on MapsMeCacheListApp, which only exists in the "full" flavors.
 */
public final class MapsMeIntentHelper {

    private MapsMeIntentHelper() {
        // utility class
    }

    @Nullable
    public static String getCacheFromMapsWithMe(final Context context, final Intent intent) {
        return MapsMeCacheListApp.getCacheFromMapsWithMe(context, intent);
    }
}
