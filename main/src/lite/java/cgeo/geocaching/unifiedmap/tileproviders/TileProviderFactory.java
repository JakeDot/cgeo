package cgeo.geocaching.unifiedmap.tileproviders;

import cgeo.geocaching.R;
import cgeo.geocaching.maps.MapUtils;
import cgeo.geocaching.settings.Settings;
import cgeo.geocaching.storage.ContentStorage;
import cgeo.geocaching.storage.PersistableFolder;
import cgeo.geocaching.utils.CollectionStream;
import cgeo.geocaching.utils.FileUtils;
import cgeo.geocaching.utils.LocalizationUtils;
import cgeo.geocaching.utils.ProcessUtils;
import cgeo.geocaching.utils.TextUtils;
import static cgeo.geocaching.unifiedmap.mapsforge.MapsforgeFileUtils.isValidMapFile;

import android.app.Activity;
import android.net.Uri;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.PopupMenu;
import androidx.core.view.MenuCompat;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Strings;
import org.apache.commons.lang3.tuple.ImmutablePair;

/**
 * "lite" flavor: only a single offline VTM tile provider (plus the "no map"
 * option) is compiled in. See the "full" flavor's TileProviderFactory for the
 * full set of engines (Google Maps, Mapsforge/CPU, VTM online + offline).
 */
public class TileProviderFactory {
    public static final int MAP_LANGUAGE_DEFAULT_ID = 432198765;

    private static final HashMap<String, AbstractTileProvider> tileProviders = new LinkedHashMap<>();
    private static String[] languages;

    private TileProviderFactory() {
        // Singleton
    }

    public static void addMapviewMenuItems(final Activity activity, final PopupMenu menu) {
        final Menu parentMenu = menu.getMenu();
        MenuCompat.setGroupDividerEnabled(parentMenu, true);

        final int currentTileProvider = Settings.getTileProvider().getNumericalId();
        final Set<String> hideTileproviders = Settings.getHideTileproviders();
        int i = 0;
        for (AbstractTileProvider tileProvider : tileProviders.values()) {
            boolean hide = false;
            for (String comp : hideTileproviders) {
                if (Strings.CS.equals(comp, tileProvider.getId())) {
                    hide = true;
                }
            }
            if (!hide) {
                final int id = tileProvider.getNumericalId();
                final String displayName = tileProvider.getDisplayName(null);
                parentMenu.add(R.id.menu_group_map_sources, id, i, (displayName != null
                        ? displayName
                        : tileProvider.getTileProviderName()
                )).setCheckable(true).setChecked(id == currentTileProvider);
            }
            i++;
        }
        final AbstractTileProvider ctp = Settings.getTileProvider();
        parentMenu.setGroupCheckable(R.id.menu_group_map_sources, true, true);
        parentMenu.findItem(R.id.menu_hillshading).setCheckable(true).setChecked(Settings.getMapShadingShowLayer()).setVisible(MapUtils.hasHillshadingTiles() && ctp.supportsHillshading());
        parentMenu.findItem(R.id.menu_backgroundmap).setCheckable(true).setChecked(Settings.getMapBackgroundMapLayer()).setVisible(ctp.supportsBackgroundMaps());
        parentMenu.findItem(R.id.menu_download_backgroundmap).setVisible(ctp.supportsBackgroundMaps);
        parentMenu.findItem(R.id.menu_check_routingdata).setVisible(Settings.useInternalRouting() || ProcessUtils.isInstalled(LocalizationUtils.getPlainString(R.string.package_brouter)));
    }

    public static HashMap<String, AbstractTileProvider> getTileProviders() {
        buildTileProviderList(false);
        return tileProviders;
    }

    public static void buildTileProviderList(final boolean force) {
        if (!(tileProviders.isEmpty() || force)) {
            return;
        }
        tileProviders.clear();

        // --------------------------------------------------------------------
        // offline-based map providers
        // --------------------------------------------------------------------

        // collect available offline map files
        final List<ImmutablePair<String, Uri>> temp =
                CollectionStream.of(ContentStorage.get().list(PersistableFolder.OFFLINE_MAPS, true))
                        .filter(fi -> !fi.isDirectory && fi.name.toLowerCase(Locale.getDefault()).endsWith(FileUtils.MAP_FILE_EXTENSION) && isValidMapFile(fi.uri))
                        .map(fi -> new ImmutablePair<>(StringUtils.capitalize(StringUtils.substringBeforeLast(fi.name, ".")), fi.uri)).toList();

        // OSM offline tile providers (VTM)
        if (temp.size() > 1) {
            registerTileProvider(new MapsforgeVTMMultiOfflineTileProvider(temp));
        }

        // sort according to displayName and register
        final List<ImmutablePair<String, AbstractMapsforgeVTMOfflineTileProvider>> offlineMaps = new ArrayList<>();
        for (ImmutablePair<String, Uri> data : temp) {
            final AbstractMapsforgeVTMOfflineTileProvider tp = new AbstractMapsforgeVTMOfflineTileProvider(data.left, data.right, 2, 18); // @todo: get actual values for zoomMin/zoomMax
            offlineMaps.add(new ImmutablePair<>(tp.getDisplayName(data.left), tp));
        }
        Collections.sort(offlineMaps, (o1, o2) -> TextUtils.COLLATOR.compare(o1.left, o2.left));
        for (ImmutablePair<String, AbstractMapsforgeVTMOfflineTileProvider> data : offlineMaps) {
            registerTileProvider(data.right);
        }

        // --------------------------------------------------------------------
        // "no map" tile provider
        registerTileProvider(new NoMapMapsforgeTileProvider());
    }

    private static void registerTileProvider(final AbstractTileProvider tileProvider) {
        tileProviders.put(tileProvider.getId(), tileProvider);
    }

    @Nullable
    public static AbstractTileProvider getTileProvider(final String stringId) {
        buildTileProviderList(false);
        return tileProviders.get(stringId);
    }

    @Nullable
    public static AbstractTileProvider getTileProvider(final int id) {
        for (final AbstractTileProvider tileProvider : tileProviders.values()) {
            if (tileProvider.getNumericalId() == id) {
                return tileProvider;
            }
        }
        return null;
    }

    public static AbstractTileProvider getAnyTileProvider() {
        buildTileProviderList(false);
        return tileProviders.isEmpty() ? null : tileProviders.entrySet().iterator().next().getValue();
    }

    // -----------------------------------------------------------------------------------------------
    // map language related methods
    // -----------------------------------------------------------------------------------------------

    /**
     * Fills the "Map language" submenu with the languages provided by the current map
     * Makes menu visible if more than one language is available, invisible if not
     */
    public static void addMapViewLanguageMenuItems(final Menu menu) {
        final MenuItem parentMenu = menu.findItem(R.id.menu_select_language);
        if (languages != null) {
            final int currentLanguage = Settings.getMapLanguageId();
            final SubMenu subMenu = parentMenu.getSubMenu();
            subMenu.clear();
            subMenu.add(R.id.menu_group_map_languages, MAP_LANGUAGE_DEFAULT_ID, 0, R.string.switch_default).setCheckable(true).setChecked(MAP_LANGUAGE_DEFAULT_ID == currentLanguage);
            for (int i = 0; i < languages.length; i++) {
                final int languageId = languages[i].hashCode();
                subMenu.add(R.id.menu_group_map_languages, languageId, i, languages[i]).setCheckable(true).setChecked(languageId == currentLanguage);
            }
            subMenu.setGroupCheckable(R.id.menu_group_map_languages, true, true);
            parentMenu.setVisible(languages.length > 1);
        } else {
            parentMenu.setVisible(false);
        }
    }

    /**
     * Return a language by id.
     *
     * @param id the language id
     * @return the language, or <tt>null</tt> if <tt>id</tt> does not correspond to a registered language
     */
    @Nullable
    public static String getLanguage(final int id) {
        if (languages != null) {
            for (final String language : languages) {
                if (language.hashCode() == id) {
                    return language;
                }
            }
        }
        return null;
    }

    public static void setLanguages(final String[] newLanguages) {
        languages = newLanguages;
    }

    public static void resetLanguages() {
        languages = new String[]{};
    }
}
