package cgeo.geocaching.wherigo;

import cgeo.geocaching.databinding.WherigolistItemBinding;
import cgeo.geocaching.ui.SimpleItemListModel;
import cgeo.geocaching.ui.SimpleItemListView;
import cgeo.geocaching.ui.TextParam;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.text.Spannable;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

/** WherigoViewUtils stub for light build — Wherigo game engine not available. */
public class WherigoViewUtils {

    private WherigoViewUtils() {
        // utility class
    }

    public static void safeDismissDialog(final Dialog dialog) {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }

    public static void ensureRunOnUi(final Runnable r) {
        if (r != null) {
            r.run();
        }
    }

    @NonNull
    public static AlertDialog createFullscreenDialog(@NonNull final Activity activity, @Nullable final CharSequence title, @NonNull final View contentView) {
        return new AlertDialog.Builder(activity).setView(contentView).create();
    }

    public static void setTitle(final Dialog dialog, final String title) {
        // not available in light build
    }

    public static <T> void setViewActions(final Iterable<T> actions, final SimpleItemListView view, final int columnCount, final Function<T, TextParam> displayMapper, final Consumer<T> clickHandler) {
        // not available in light build
    }

    @Nullable
    public static SimpleItemListModel<WherigoThingType> createThingTypeTable(final Activity activity, final SimpleItemListView target, final Consumer<Object> thingSelectAction) {
        return null;
    }

    public static void updateThingTypeTable(final SimpleItemListModel<WherigoThingType> model, final SimpleItemListView target) {
        // not available in light build
    }

    public static void chooseThing(@NonNull final Activity activity, @NonNull final List<?> things, @Nullable final String title, final Consumer<Object> thingSelectedAction) {
        // not available in light build
    }

    public static void displayThing(@Nullable final Activity activity, @NonNull final Object thing, final boolean forceDisplay) {
        // not available in light build
    }

    @Nullable
    public static Dialog getQuickViewDialog(final Activity activity) {
        return null;
    }

    public static void showErrorDialog(final Activity activity) {
        // not available in light build
    }

    public static void addWherigoBadgeNotifications(final View view) {
        // not available in light build
    }

    public static void executeForOneCartridge(final Activity activity, final List<String> wherigoGuids, final Consumer<String> guidAction) {
        // not available in light build
    }

    public static void fillCartridgeSelectItem(final WherigolistItemBinding binding, final WherigoCartridgeInfo info) {
        // not available in light build
    }

    public static void htmlReplaceWherigoClickAction(final Activity activity, final String geocode, final Spannable spannable) {
        // not available in light build
    }
}
