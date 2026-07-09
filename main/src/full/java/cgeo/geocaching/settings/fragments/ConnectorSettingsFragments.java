package cgeo.geocaching.settings.fragments;

import cgeo.geocaching.R;
import cgeo.geocaching.utils.LocalizationUtils;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

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
        final Supplier<Fragment> supplier = getFragmentSuppliers().get(baseKey);
        return supplier == null ? null : supplier.get();
    }

    private static Map<String, Supplier<Fragment>> getFragmentSuppliers() {
        final Map<String, Supplier<Fragment>> suppliers = new HashMap<>();
        suppliers.put(LocalizationUtils.getPlainString(R.string.preference_screen_geokrety), PreferenceServiceGeokretyOrgFragment::new);
        suppliers.put(LocalizationUtils.getPlainString(R.string.preference_screen_ocde), PreferenceServiceOpencachingDeFragment::new);
        suppliers.put(LocalizationUtils.getPlainString(R.string.preference_screen_ocuk), PreferenceServiceOpencacheUkFragment::new);
        suppliers.put(LocalizationUtils.getPlainString(R.string.preference_screen_ocnl), PreferenceServiceOpencachingNlFragment::new);
        suppliers.put(LocalizationUtils.getPlainString(R.string.preference_screen_ocpl), PreferenceServiceOpencachingPlFragment::new);
        suppliers.put(LocalizationUtils.getPlainString(R.string.preference_screen_ocus), PreferenceServiceOpencachingUsFragment::new);
        suppliers.put(LocalizationUtils.getPlainString(R.string.preference_screen_ocro), PreferenceServiceOpencachingRoFragment::new);
        suppliers.put(LocalizationUtils.getPlainString(R.string.preference_screen_ec), PreferenceServiceExtremcachingComFragment::new);
        suppliers.put(LocalizationUtils.getPlainString(R.string.preference_screen_su), PreferenceServiceGeocachingSuFragment::new);
        return suppliers;
    }
}
