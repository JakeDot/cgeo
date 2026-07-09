package cgeo.geocaching.wherigo;

import cgeo.geocaching.R;
import cgeo.geocaching.location.Geopoint;
import cgeo.geocaching.models.Geocache;
import cgeo.geocaching.ui.ImageParam;
import cgeo.geocaching.ui.TextParam;
import cgeo.geocaching.utils.LocalizationUtils;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;

import androidx.annotation.Nullable;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

/** WherigoUtils stub for light build — Wherigo game engine not available. */
public class WherigoUtils {

    public static final TextParam TP_OK_BUTTON = TextParam.id(R.string.ok).setAllCaps(true).setImage(ImageParam.id(R.drawable.ic_menu_done));
    public static final TextParam TP_CLOSE_BUTTON = TextParam.id(R.string.close).setAllCaps(true).setImage(ImageParam.id(R.drawable.ic_menu_done));
    public static final TextParam TP_CANCEL_BUTTON = TextParam.id(R.string.cancel).setAllCaps(true).setImage(ImageParam.id(R.drawable.ic_menu_cancel));

    public static final Object GP_CONVERTER = null;

    private WherigoUtils() {
        // utility class
    }

    public static boolean isVisibleToPlayer(final Object et) {
        return false;
    }

    public static Geopoint getZoneCenter(final Object zone) {
        return null;
    }

    public static String getDisplayableDistance(final Geopoint from, final Geopoint to) {
        return "";
    }

    public static String getDisplayableDistanceTo(final Object zone) {
        return "";
    }

    public static String getUserDisplayableActionText(final Object action) {
        return "";
    }

    public static List<?> getActions(final Object thing, final boolean all) {
        return Collections.emptyList();
    }

    public static void callAction(final Object thing, final Object action, final Activity activity) {
        // not available in light build
    }

    public static List<?> getActionTargets(final Object action) {
        return Collections.emptyList();
    }

    @Nullable
    public static Drawable getThingIconAsDrawable(final Context context, final Object et) {
        return null;
    }

    public static Comparator<?> getThingsComparator() {
        return (o1, o2) -> 0;
    }

    public static Object readCartridge(final Object uri) {
        return null;
    }

    public static void closeCartridgeQuietly(final Object file) {
        // not available in light build
    }

    @Nullable
    public static Drawable getDrawableForImageData(@Nullable final Context ctx, final byte[] data) {
        return null;
    }

    public static void ensureNoGameRunning(final Activity activity, final Runnable runOnClosedGameOnly) {
        if (runOnClosedGameOnly != null) {
            runOnClosedGameOnly.run();
        }
    }

    public static List<String> getWherigoGuids(@Nullable final Geocache cache) {
        return Collections.emptyList();
    }

    public static String getWherigoDetailsUrl(@Nullable final String guid) {
        return "";
    }

    public static String getWherigoDownloadUrl(@Nullable final String guid) {
        return "";
    }

    public static List<String> scanWherigoGuids(@Nullable final String textToScan) {
        return Collections.emptyList();
    }

    public static void loadGame(final Activity activity, final Object cartridgeInfo) {
        // not available in light build
    }

    public static void saveGame(final Activity activity) {
        // not available in light build
    }

    public static String findGeocacheNameForGeocode(@Nullable final String geocode) {
        return "";
    }

    public static CharSequence getUserDisplayableName(final Object et, final boolean doShort) {
        return "";
    }

    public static String eventTableDebugInfo(final Object et) {
        return "";
    }
}
