package cgeo.geocaching.settings;

import cgeo.geocaching.activity.OAuthAuthorizationActivity.OAuthParameters;
import cgeo.geocaching.utils.LocalizationUtils;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;

public class OAuthPreference extends AbstractClickablePreference {

    private static final int NO_KEY = -1;

    private enum OAuthActivityMapping {
        NONE(NO_KEY, null, null, -1, -1);

        public final int prefKeyId;
        public final int publicKeyId;
        public final int secretKeyId;
        public final Class<?> authActivity;
        public final OAuthParameters authParams;

        OAuthActivityMapping(final int prefKeyId, final Class<?> authActivity, final OAuthParameters authParams, final int publicKeyId, final int secretKeyId) {
            this.prefKeyId = prefKeyId;
            this.authActivity = authActivity;
            this.authParams = authParams;
            this.publicKeyId = publicKeyId;
            this.secretKeyId = secretKeyId;
        }
    }

    private final OAuthActivityMapping oAuthMapping;

    private OAuthActivityMapping getAuthorization() {
        final String prefKey = getKey();
        for (final OAuthActivityMapping auth : OAuthActivityMapping.values()) {
            if (auth.prefKeyId != NO_KEY && prefKey.equals(LocalizationUtils.getPlainString(auth.prefKeyId))) {
                return auth;
            }
        }
        return OAuthActivityMapping.NONE;
    }

    public OAuthPreference(final Context context, final AttributeSet attrs) {
        super(context, attrs);
        this.oAuthMapping = getAuthorization();
    }

    public OAuthPreference(final Context context, final AttributeSet attrs, final int defStyle) {
        super(context, attrs, defStyle);
        this.oAuthMapping = getAuthorization();
    }

    @Override
    protected OnPreferenceClickListener getOnPreferenceClickListener(final SettingsActivity activity) {
        return preference -> {
            if (oAuthMapping.authActivity != null && oAuthMapping.authParams != null) {
                final Intent authIntent = new Intent(preference.getContext(),
                        oAuthMapping.authActivity);
                oAuthMapping.authParams.setOAuthExtras(authIntent);
                activity.startActivityForResult(authIntent,
                        oAuthMapping.prefKeyId);
            }
            return false; // no shared preference has to be changed
        };

    }

    @Override
    protected boolean isAuthorized() {
        if (oAuthMapping.publicKeyId < 0 || oAuthMapping.secretKeyId < 0) {
            return false;
        }
        return Settings.hasOAuthAuthorization(oAuthMapping.publicKeyId, oAuthMapping.secretKeyId);
    }

    @Override
    protected void revokeAuthorization() {
        if (oAuthMapping.publicKeyId < 0 || oAuthMapping.secretKeyId < 0) {
            return;
        }

        Settings.setTokens(oAuthMapping.publicKeyId, null, oAuthMapping.secretKeyId, null);
    }
}
