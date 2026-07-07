package cgeo.geocaching.playservices;

import android.content.Context;

/**
 * The "lite" flavor has no Google Play Services dependency. See the "full"
 * flavor's version of this class.
 */
public final class PlayServicesSystemInfo {

    private PlayServicesSystemInfo() {
        // utility class
    }

    public static void append(final Context context, final StringBuilder body) {
        body.append("\n- Google Play services: unavailable");
    }
}
