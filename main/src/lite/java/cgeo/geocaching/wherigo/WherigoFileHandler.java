package cgeo.geocaching.wherigo;

import android.app.Activity;

/**
 * The "lite" flavor has no Wherigo player. See the "full" flavor's version of
 * this class.
 */
public final class WherigoFileHandler {

    private WherigoFileHandler() {
        // utility class
    }

    public static boolean handleWherigoFile(final Activity activity, final String guid) {
        return false;
    }
}
