package cgeo.geocaching.apps.cachelist;

import android.content.Context;
import android.content.Intent;

import androidx.annotation.Nullable;

/**
 * The "lite" flavor has no Maps.ME integration. See the "full" flavor's
 * version of this class.
 */
public final class MapsMeIntentHelper {

    private MapsMeIntentHelper() {
        // utility class
    }

    @Nullable
    public static String getCacheFromMapsWithMe(final Context context, final Intent intent) {
        return null;
    }
}
