package cgeo.geocaching.apps.navi;

import cgeo.geocaching.models.Geocache;
import cgeo.geocaching.ui.AbstractMenuActionProvider;

import android.content.Context;
import android.view.SubMenu;

import androidx.core.view.ActionProvider;
import androidx.core.view.MenuItemCompat;

import android.view.MenuItem;

/**
 * Action provider listing all available navigation actions as sub menu — stub for light build.
 */
public class NavigationSelectionActionProvider extends AbstractMenuActionProvider {

    public NavigationSelectionActionProvider(final Context context) {
        super(context);
    }

    public void setTarget(final Geocache cache) {
        // not available in light build
    }

    @Override
    public void onPrepareSubMenu(final SubMenu subMenu) {
        // not available in light build
    }

    public static void initialize(final MenuItem menuItem, final Geocache cache) {
        final ActionProvider actionProvider = MenuItemCompat.getActionProvider(menuItem);
        if (actionProvider instanceof NavigationSelectionActionProvider) {
            ((NavigationSelectionActionProvider) actionProvider).setTarget(cache);
        }
    }
}
