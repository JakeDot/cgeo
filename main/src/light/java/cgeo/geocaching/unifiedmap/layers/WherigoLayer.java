package cgeo.geocaching.unifiedmap.layers;

import cgeo.geocaching.unifiedmap.geoitemlayer.GeoItemLayer;

/** WherigoLayer stub for light build — Wherigo game engine not available. */
public class WherigoLayer {

    private static final WherigoLayer INSTANCE = new WherigoLayer();

    public static final String WHERIGO_KEY_PRAEFIX = "WHERIGO-";

    public static WherigoLayer get() {
        return INSTANCE;
    }

    private WherigoLayer() {
        // singleton
    }

    public void setLayer(final GeoItemLayer<String> layer) {
        // not available in light build
    }

    public void refresh() {
        // not available in light build
    }
}
