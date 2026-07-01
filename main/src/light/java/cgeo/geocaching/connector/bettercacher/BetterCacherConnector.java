package cgeo.geocaching.connector.bettercacher;

import cgeo.geocaching.SearchResult;
import cgeo.geocaching.connector.AbstractConnector;
import cgeo.geocaching.connector.capability.ICacheAmendment;
import cgeo.geocaching.models.Geocache;

import androidx.annotation.NonNull;

/** BetterCacherConnector stub for light build. */
public class BetterCacherConnector extends AbstractConnector implements ICacheAmendment {

    public static final BetterCacherConnector INSTANCE = new BetterCacherConnector();

    private BetterCacherConnector() {
    }

    @Override
    public boolean canHandle(@NonNull final String geocode) {
        return false;
    }

    @Override
    @NonNull
    public String getName() {
        return "BetterCacher";
    }

    @Override
    @NonNull
    public String getNameAbbreviated() {
        return "BC";
    }

    @Override
    @NonNull
    public String getHost() {
        return "";
    }

    @Override
    public boolean isOwner(@NonNull final Geocache cache) {
        return false;
    }

    @Override
    public void amendCaches(@NonNull final SearchResult searchResult) {
        // not available in light build
    }

    @Override
    @NonNull
    protected String getCacheUrlPrefix() {
        return "";
    }

    public String getHostUrl() {
        return "";
    }

    public String getCacheUrl(@NonNull final Geocache cache) {
        return "";
    }
}
