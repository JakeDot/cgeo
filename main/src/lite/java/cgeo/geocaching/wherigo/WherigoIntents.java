package cgeo.geocaching.wherigo;

import android.content.Context;
import android.content.Intent;

import androidx.annotation.Nullable;

/**
 * The "lite" flavor has no Wherigo player. See the "full" flavor's version of
 * this class.
 */
public final class WherigoIntents {

    private WherigoIntents() {
        // utility class
    }

    @Nullable
    public static Intent createPlayerIntent(final Context context) {
        return null;
    }
}
