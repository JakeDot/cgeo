package cgeo.geocaching.wherigo;

import cgeo.geocaching.models.MapSelectableItem;
import cgeo.geocaching.unifiedmap.geoitemlayer.GeoItemLayer;

import android.app.Activity;

import androidx.annotation.Nullable;

/**
 * The "lite" flavor has no Wherigo player, so the map never shows Wherigo
 * zones or the Wherigo quick-view popup. See the "full" flavor's version of
 * this class.
 */
public final class WherigoMapSupport {

    private WherigoMapSupport() {
        // utility class
    }

    public static void setClickableLayer(final GeoItemLayer<String> layer) {
        // nothing to do: no Wherigo player in the "lite" flavor
    }

    public static boolean isWherigoKey(final String key) {
        return false;
    }

    @Nullable
    public static MapSelectableItem createSelectableItem(final String key) {
        return null;
    }

    public static boolean isWherigoZoneData(@Nullable final Object data) {
        return false;
    }

    public static void displayThing(final Activity activity, final Object data) {
        // nothing to do: no Wherigo player in the "lite" flavor
    }

    public static int registerVisibilityListener(final Activity activity, final int wherigoContainerViewId) {
        return -1;
    }

    public static void unregisterListener(final int listenerId) {
        // nothing to do: no Wherigo player in the "lite" flavor
    }

    public static void showQuickViewDialog(final Activity activity) {
        // nothing to do: no Wherigo player in the "lite" flavor
    }
}
