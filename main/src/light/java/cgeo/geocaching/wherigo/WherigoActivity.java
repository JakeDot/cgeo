package cgeo.geocaching.wherigo;

import cgeo.geocaching.activity.CustomMenuEntryActivity;
import cgeo.geocaching.enumerations.QuickLaunchItem;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.Nullable;

/** WherigoActivity stub for light build — Wherigo game engine not available. */
public class WherigoActivity extends CustomMenuEntryActivity {

    public static void start(final Activity parent, final boolean forceHideNavigationBar) {
        // not available in light build
    }

    public static void startForGuid(final Activity parent, final String guid, final String geocode, final boolean forceHideNavigationBar) {
        // not available in light build
    }

    @Override
    public QuickLaunchItem.VALUES getRelatedQuickLaunchItem() {
        return QuickLaunchItem.VALUES.WHERIGO;
    }

    @Override
    public void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        finish();
    }
}
