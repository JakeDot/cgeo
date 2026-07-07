package cgeo.geocaching.wherigo;

import cgeo.geocaching.R;
import cgeo.geocaching.models.Geocache;
import cgeo.geocaching.settings.Settings;
import cgeo.geocaching.settings.SettingsActivity;
import cgeo.geocaching.ui.ViewUtils;

import android.app.Activity;
import android.text.Spannable;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

/**
 * Wherigo hooks used outside the wherigo package (cache detail screen, quick
 * launch), extracted so those classes do not need to depend directly on
 * Wherigo classes, which only exist in the "full" flavors.
 */
public final class WherigoUiSupport {

    private WherigoUiSupport() {
        // utility class
    }

    public static void updateWherigoBox(final Geocache cache, final Activity activity, @NonNull final Button wherigoButton, @Nullable final View wherigoView, @Nullable final TextView wherigoText) {
        final List<String> wherigoGuis = WherigoUtils.getWherigoGuids(cache);
        final boolean isEnabled = !wherigoGuis.isEmpty();

        ViewUtils.setVisibility(wherigoView, isEnabled ? View.VISIBLE : View.GONE);

        ViewUtils.setVisibility(wherigoText, isEnabled ? View.VISIBLE : View.GONE);
        ViewUtils.setText(wherigoText, (!isEnabled || Settings.hasGCCredentials()) ? R.string.cache_wherigo_start : R.string.cache_wherigo_credentials);

        ViewUtils.setVisibility(wherigoButton, isEnabled ? View.VISIBLE : View.GONE);
        if (isEnabled) {
            wherigoButton.setOnClickListener(v -> {
                if (Settings.hasGCCredentials()) {
                    WherigoViewUtils.executeForOneCartridge(activity, wherigoGuis, guid ->
                            WherigoActivity.startForGuid(activity, guid, cache.getGeocode(), true));
                } else {
                    SettingsActivity.openForScreen(R.string.preference_screen_gc, activity);
                }
            });
        }
    }

    public static void htmlReplaceWherigoClickAction(final Activity activity, final String geocode, final Spannable spannable) {
        WherigoViewUtils.htmlReplaceWherigoClickAction(activity, geocode, spannable);
    }

    public static void addBadgeNotifications(final View view) {
        WherigoViewUtils.addWherigoBadgeNotifications(view);
    }

    public static void launch(final Activity activity, final boolean hideNavigationBar) {
        WherigoActivity.start(activity, hideNavigationBar);
    }
}
