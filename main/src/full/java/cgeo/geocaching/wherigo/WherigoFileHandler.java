package cgeo.geocaching.wherigo;

import android.app.Activity;

/**
 * Extracted from HandleLocalFilesActivity so that class does not need to
 * depend directly on WherigoActivity, which only exists in the "full" flavors.
 */
public final class WherigoFileHandler {

    private WherigoFileHandler() {
        // utility class
    }

    public static boolean handleWherigoFile(final Activity activity, final String guid) {
        WherigoActivity.startForGuid(activity, guid, null, false);
        return true;
    }
}
