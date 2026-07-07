package cgeo.geocaching.playservices;

import cgeo.geocaching.settings.Settings;

import android.content.Context;
import android.content.pm.PackageManager;

import org.apache.commons.lang3.StringUtils;
import com.google.android.gms.common.GoogleApiAvailability;

/**
 * Extracted from SystemInformation so that class does not need to depend
 * directly on GMS classes, which only exist in the "full" flavors.
 */
public final class PlayServicesSystemInfo {

    private PlayServicesSystemInfo() {
        // utility class
    }

    public static void append(final Context context, final StringBuilder body) {
        final boolean googlePlayServicesAvailable = GooglePlayServices.isAvailable();
        body.append("\n- Google Play services: ").append(googlePlayServicesAvailable ? (Settings.useGooglePlayServices() ? "enabled" : "disabled") : "unavailable");
        if (googlePlayServicesAvailable) {
            body.append(" - ");
            try {
                body.append(StringUtils.defaultIfBlank(context.getPackageManager().getPackageInfo(GoogleApiAvailability.GOOGLE_PLAY_SERVICES_PACKAGE, 0).versionName, "unknown version"));
            } catch (final PackageManager.NameNotFoundException e) {
                body.append("unretrievable version (").append(e.getMessage()).append(')');
            }
        }
    }
}
