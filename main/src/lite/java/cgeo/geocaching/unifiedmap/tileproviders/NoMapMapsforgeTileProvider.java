package cgeo.geocaching.unifiedmap.tileproviders;

import cgeo.geocaching.R;
import cgeo.geocaching.unifiedmap.mapsforgevtm.MapsforgeVtmFragment;
import cgeo.geocaching.utils.LocalizationUtils;

import android.net.Uri;

import androidx.core.util.Pair;

import org.oscim.map.Map;

/**
 * The "lite" flavor only has the offline VTM engine, so unlike the "full"
 * flavor's version of this class, this one is based on
 * {@link AbstractMapsforgeVTMTileProvider} rather than the (CPU-based, "full"
 * flavor only) {@code AbstractMapsforgeTileProvider}.
 */
public class NoMapMapsforgeTileProvider extends AbstractMapsforgeVTMTileProvider {
    NoMapMapsforgeTileProvider() {
        super(LocalizationUtils.getString(R.string.map_source_nomap), Uri.parse(""), 0, 18, new Pair<>("", false));
        supportsBackgroundMaps = true;
    }

    @Override
    public void addTileLayer(final MapsforgeVtmFragment fragment, final Map map) {
        // nothing to do
    }
}
