package cgeo.geocaching.settings.fragments;

import cgeo.geocaching.R;
import cgeo.geocaching.utils.LocalizationUtils;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import org.apache.commons.lang3.Strings;

/**
 * Settings screens for the connectors that only exist in the "full" flavors
 * (basic/nojit/foss). The "lite" flavor has its own version of this class
 * that returns {@code null} for all keys, since none of these connectors or
 * their preference screens are compiled into it.
 */
public final class ConnectorSettingsFragments {

    private ConnectorSettingsFragments() {
        // utility class
    }

    @Nullable
    public static Fragment getFragmentForKey(final String baseKey) {
        if (Strings.CS.equals(baseKey, LocalizationUtils.getPlainString(R.string.preference_screen_geokrety))) {
            return new PreferenceServiceGeokretyOrgFragment();
        } else if (Strings.CS.equals(baseKey, LocalizationUtils.getPlainString(R.string.preference_screen_ocde))) {
            return new PreferenceServiceOpencachingDeFragment();
        } else if (Strings.CS.equals(baseKey, LocalizationUtils.getPlainString(R.string.preference_screen_ocuk))) {
            return new PreferenceServiceOpencacheUkFragment();
        } else if (Strings.CS.equals(baseKey, LocalizationUtils.getPlainString(R.string.preference_screen_ocnl))) {
            return new PreferenceServiceOpencachingNlFragment();
        } else if (Strings.CS.equals(baseKey, LocalizationUtils.getPlainString(R.string.preference_screen_ocpl))) {
            return new PreferenceServiceOpencachingPlFragment();
        } else if (Strings.CS.equals(baseKey, LocalizationUtils.getPlainString(R.string.preference_screen_ocus))) {
            return new PreferenceServiceOpencachingUsFragment();
        } else if (Strings.CS.equals(baseKey, LocalizationUtils.getPlainString(R.string.preference_screen_ocro))) {
            return new PreferenceServiceOpencachingRoFragment();
        } else if (Strings.CS.equals(baseKey, LocalizationUtils.getPlainString(R.string.preference_screen_ec))) {
            return new PreferenceServiceExtremcachingComFragment();
        } else if (Strings.CS.equals(baseKey, LocalizationUtils.getPlainString(R.string.preference_screen_su))) {
            return new PreferenceServiceGeocachingSuFragment();
        }
        return null;
    }
}
