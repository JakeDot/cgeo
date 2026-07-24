package cgeo.geocaching.wherigo;

import cgeo.geocaching.models.MapSelectableItem;
import cgeo.geocaching.unifiedmap.geoitemlayer.GeoItemLayer;
import cgeo.geocaching.unifiedmap.layers.WherigoLayer;
import cgeo.geocaching.wherigo.openwig.EventTable;
import cgeo.geocaching.wherigo.openwig.Zone;

import android.app.Activity;
import android.view.View;

import androidx.annotation.Nullable;

/**
 * Wherigo hooks used by UnifiedMapActivity, extracted so that class does not
 * need to depend directly on Wherigo classes, which only exist in the "full"
 * flavors.
 */
public final class WherigoMapSupport {

    private WherigoMapSupport() {
        // utility class
    }

    public static void setClickableLayer(final GeoItemLayer<String> layer) {
        WherigoLayer.get().setLayer(layer);
    }

    public static boolean isWherigoKey(final String key) {
        return key.startsWith(WherigoLayer.WHERIGO_KEY_PRAEFIX);
    }

    @Nullable
    public static MapSelectableItem createSelectableItem(final String key) {
        final String zoneName = key.substring(WherigoLayer.WHERIGO_KEY_PRAEFIX.length());
        return new MapSelectableItem(WherigoGame.get().getZone(zoneName), zoneName, WherigoGame.get().getCartridgeName(), WherigoThingType.LOCATION.getIconId());
    }

    public static boolean isWherigoZoneData(@Nullable final Object data) {
        return data instanceof Zone;
    }

    public static void displayThing(final Activity activity, final Object data) {
        if (data instanceof EventTable) {
            WherigoViewUtils.displayThing(activity, (EventTable) data, false);
        }
    }

    public static int registerVisibilityListener(final Activity activity, final int wherigoContainerViewId) {
        final int[] listenerId = new int[1];
        listenerId[0] = WherigoGame.get().addListener(nt -> {
            if (activity.isFinishing() || activity.isDestroyed()) {
                WherigoGame.get().removeListener(listenerId[0]);
                return;
            }
            final View view = activity.findViewById(wherigoContainerViewId);
            if (view != null) {
                view.setVisibility(WherigoGame.get().isPlaying() ? View.VISIBLE : View.GONE);
            }
        });
        return listenerId[0];
    }

    public static void unregisterListener(final int listenerId) {
        WherigoGame.get().removeListener(listenerId);
    }

    public static void showQuickViewDialog(final Activity activity) {
        WherigoViewUtils.getQuickViewDialog(activity).show();
    }
}
