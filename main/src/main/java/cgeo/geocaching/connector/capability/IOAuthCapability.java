package cgeo.geocaching.connector.capability;

import cgeo.geocaching.connector.IConnector;

/**
 * connector capability for providing public and secret OAuth tokens
 */
public interface IOAuthCapability extends IConnector {

    // Levels of OAuth-Authentication a connector may support
    enum OAuthLevel {
        Level0,
        Level1,
        Level3
    }

    int getTokenPublicPrefKeyId();

    int getTokenSecretPrefKeyId();
}
