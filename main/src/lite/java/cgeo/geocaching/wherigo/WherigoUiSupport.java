package cgeo.geocaching.wherigo;

import cgeo.geocaching.models.Geocache;
import cgeo.geocaching.ui.ViewUtils;

import android.app.Activity;
import android.text.Spannable;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * The "lite" flavor has no Wherigo player. See the "full" flavor's version of
 * this class.
 */
public final class WherigoUiSupport {

    private WherigoUiSupport() {
        // utility class
    }

    public static void updateWherigoBox(final Geocache cache, final Activity activity, @NonNull final Button wherigoButton, @Nullable final View wherigoView, @Nullable final TextView wherigoText) {
        ViewUtils.setVisibility(wherigoView, View.GONE);
        ViewUtils.setVisibility(wherigoText, View.GONE);
        ViewUtils.setVisibility(wherigoButton, View.GONE);
    }

    public static void htmlReplaceWherigoClickAction(final Activity activity, final String geocode, final Spannable spannable) {
        // nothing to do: no Wherigo player in the "lite" flavor
    }

    public static void addBadgeNotifications(final View view) {
        // nothing to do: no Wherigo player in the "lite" flavor
    }

    public static void launch(final Activity activity, final boolean hideNavigationBar) {
        // nothing to do: no Wherigo player in the "lite" flavor
    }
}
