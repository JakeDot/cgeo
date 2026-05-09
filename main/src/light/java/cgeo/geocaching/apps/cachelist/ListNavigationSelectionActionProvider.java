package cgeo.geocaching.apps.cachelist;

import cgeo.geocaching.ui.AbstractMenuActionProvider;

import android.content.Context;
import android.view.SubMenu;

import androidx.core.view.ActionProvider;
import androidx.core.view.MenuItemCompat;

import android.view.MenuItem;

/**
 * Action provider showing a sub menu with all navigation possibilities for a complete list of caches — stub for light build.
 */
public class ListNavigationSelectionActionProvider extends AbstractMenuActionProvider {

    private Callback callback;

    public interface Callback {
        void onListNavigationSelected(CacheListApp app);
    }

    public ListNavigationSelectionActionProvider(final Context context) {
        super(context);
    }

    public void setCallback(final Callback callback) {
        this.callback = callback;
    }

    @Override
    public void onPrepareSubMenu(final SubMenu subMenu) {
        // not available in light build
    }

    public static void initialize(final MenuItem menuItem, final Callback callback) {
        final ActionProvider actionProvider = MenuItemCompat.getActionProvider(menuItem);
        if (actionProvider instanceof ListNavigationSelectionActionProvider) {
            ((ListNavigationSelectionActionProvider) actionProvider).setCallback(callback);
        }
    }
}
