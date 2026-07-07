package cgeo.geocaching.wherigo;

import android.content.Context;
import android.content.Intent;

import androidx.annotation.Nullable;

/**
 * Extracted so that callers (MainActivity, CreateShortcutActivity) do not
 * need to depend directly on WherigoActivity, which only exists in the
 * "full" flavors.
 */
public final class WherigoIntents {

    private WherigoIntents() {
        // utility class
    }

    @Nullable
    public static Intent createPlayerIntent(final Context context) {
        return new Intent(context, WherigoActivity.class);
    }
}
