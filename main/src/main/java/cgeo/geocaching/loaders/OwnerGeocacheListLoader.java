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

    @NonNull private final Map<String, String> connectorUsernameMap;

    public OwnerGeocacheListLoader(final Activity activity, final GeocacheSort sort, @NonNull final Map<String, String> connectorToUsername) {
        super(activity, sort);
        this.connectorUsernameMap = connectorToUsername;
    }

    @Override
    public GeocacheFilterType getFilterType() {
        return GeocacheFilterType.OWNER;
    }

    @Override
    public IGeocacheFilter getAdditionalFilterParameter() {
        final OrGeocacheFilter aggregator = new OrGeocacheFilter();
        
        for (final Map.Entry<String, String> pair : connectorUsernameMap.entrySet()) {
            final String platformName = pair.getKey();
            final String ownerName = pair.getValue();
            
            final boolean skipConnectorCheck = platformName == null || platformName.equals("default");
            
            if (skipConnectorCheck) {
                aggregator.addChild(createOwnerOnlyFilter(ownerName));
                continue;
            }
            
            final IConnector platform = cgeo.geocaching.connector.ConnectorFactory.getConnectorByName(platformName);
            if (platform == null) {
                continue;
            }
            
            aggregator.addChild(createPlatformAndOwnerFilter(platform, ownerName));
        }
        
        return aggregator;
    }
    
    private OwnerGeocacheFilter createOwnerOnlyFilter(final String ownerName) {
        final OwnerGeocacheFilter filter = GeocacheFilterType.OWNER.create();
        filter.getStringFilter().setTextValue(ownerName);
        return filter;
    }
    
    private AndGeocacheFilter createPlatformAndOwnerFilter(final IConnector platform, final String ownerName) {
        final AndGeocacheFilter combo = new AndGeocacheFilter();
        
        final OriginGeocacheFilter platformPart = GeocacheFilterType.ORIGIN.create();
        platformPart.addValue(platform);
        combo.addChild(platformPart);
        
        combo.addChild(createOwnerOnlyFilter(ownerName));
        
        return combo;
    }
}
