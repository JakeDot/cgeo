package cgeo.geocaching.loaders;

import cgeo.geocaching.connector.IConnector;
import cgeo.geocaching.filters.core.AndGeocacheFilter;
import cgeo.geocaching.filters.core.GeocacheFilterType;
import cgeo.geocaching.filters.core.IGeocacheFilter;
import cgeo.geocaching.filters.core.OrGeocacheFilter;
import cgeo.geocaching.filters.core.OriginGeocacheFilter;
import cgeo.geocaching.filters.core.OwnerGeocacheFilter;
import cgeo.geocaching.sorting.GeocacheSort;

import android.app.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.Map;

public class OwnerGeocacheListLoader extends LiveFilterGeocacheListLoader {

    @Nullable private final String singleUsername;
    @Nullable private final Map<String, String> connectorUsernameMap;

    public OwnerGeocacheListLoader(final Activity activity, final GeocacheSort sort, @NonNull final String username) {
        super(activity, sort);
        this.singleUsername = username;
        this.connectorUsernameMap = null;
    }

    public OwnerGeocacheListLoader(final Activity activity, final GeocacheSort sort, @NonNull final Map<String, String> connectorToUsername) {
        super(activity, sort);
        this.singleUsername = null;
        this.connectorUsernameMap = connectorToUsername;
    }

    @Override
    public GeocacheFilterType getFilterType() {
        return GeocacheFilterType.OWNER;
    }

    @Override
    public IGeocacheFilter getAdditionalFilterParameter() {
        if (connectorUsernameMap != null && !connectorUsernameMap.isEmpty()) {
            return buildMultiConnectorFilter();
        } else if (singleUsername != null) {
            return buildSingleUsernameFilter(singleUsername);
        }
        return null;
    }

    private IGeocacheFilter buildSingleUsernameFilter(final String ownerName) {
        final OwnerGeocacheFilter filter = GeocacheFilterType.OWNER.create();
        filter.getStringFilter().setTextValue(ownerName);
        return filter;
    }

    private IGeocacheFilter buildMultiConnectorFilter() {
        final OrGeocacheFilter outerOr = new OrGeocacheFilter();
        
        for (final Map.Entry<String, String> entry : connectorUsernameMap.entrySet()) {
            final AndGeocacheFilter innerAnd = new AndGeocacheFilter();
            
            final OriginGeocacheFilter connectorFilter = GeocacheFilterType.ORIGIN.create();
            final IConnector connector = cgeo.geocaching.connector.ConnectorFactory.getConnectorByName(entry.getKey());
            connectorFilter.addValue(connector);
            innerAnd.addChild(connectorFilter);
            
            final OwnerGeocacheFilter ownerFilter = GeocacheFilterType.OWNER.create();
            ownerFilter.getStringFilter().setTextValue(entry.getValue());
            innerAnd.addChild(ownerFilter);
            
            outerOr.addChild(innerAnd);
        }
        
        return outerOr;
    }
}
