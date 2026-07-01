package cgeo.watchdog;

import cgeo.geocaching.connector.ConnectorFactory;
import cgeo.geocaching.connector.IConnector;
import cgeo.geocaching.connector.gc.GCLogAPITest;
import cgeo.geocaching.connector.internal.InternalConnector;
import cgeo.geocaching.connector.trackable.TrackableConnector;
import cgeo.geocaching.network.Network;
import cgeo.geocaching.test.NotForIntegrationTests;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * This test is intended to run regularly on our CI server, to verify the availability of several geocaching websites
 * and our ability to parse a cache from it.
 * <p>
 * You need all the opencaching API keys for this test to run.
 * </p>
 */

@SuppressWarnings("PMD.JUnitTestsShouldIncludeAssert")
public class WatchdogTest {

    @NotForIntegrationTests
    @Test
    public void testGeocachingLogCache() {
        new GCLogAPITest().cacheLoggingLifecycleTest();
    }

    @NotForIntegrationTests
    @Test
    public void testGeocachingLogTrackable() {
        new GCLogAPITest().trackableLoggingLifecycleTest();
    }

    private static void checkWebsite(final String connectorName, final String url) {
        final String page = Network.getResponseData(Network.getRequest(url));
        assertThat(page).overridingErrorMessage("Failed to get response from " + connectorName).isNotEmpty();
    }

    @NotForIntegrationTests
    @Test
    public void testTrackableWebsites() {
        for (final TrackableConnector trackableConnector : ConnectorFactory.getTrackableConnectors()) {
            if (!trackableConnector.equals(ConnectorFactory.UNKNOWN_TRACKABLE_CONNECTOR)) {
                checkWebsite("trackable website " + trackableConnector.getHost(), trackableConnector.getTestUrl());
                if (StringUtils.isNotBlank(trackableConnector.getProxyUrl())) {
                    checkWebsite("trackable website " + trackableConnector.getHost() + " proxy " + trackableConnector.getProxyUrl(), trackableConnector.getProxyUrl());
                }
            }
        }
    }

    @NotForIntegrationTests
    @Test
    public void testGeocachingWebsites() {
        for (final IConnector connector : ConnectorFactory.getConnectors()) {
            if (!connector.equals(ConnectorFactory.UNKNOWN_CONNECTOR) && !(connector instanceof InternalConnector)) {
                checkWebsite("geocaching website " + connector.getName(), connector.getTestUrl());
            }
        }
    }
}
