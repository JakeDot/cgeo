package cgeo.geocaching.settings.fragments;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

/**
 * The "lite" flavor has none of the extra connectors (OpenCaching family,
 * ExtremCaching, Geocaching.su, Geokrety) so there is no settings screen to
 * show for them. See the "full" flavor's version of this class.
 */
public final class ConnectorSettingsFragments {

    private ConnectorSettingsFragments() {
        // utility class
    }

    @Nullable
    public static Fragment getFragmentForKey(final String baseKey) {
        return null;
    }
}
