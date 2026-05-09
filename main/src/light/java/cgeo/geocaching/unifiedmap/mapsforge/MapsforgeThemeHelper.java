package cgeo.geocaching.unifiedmap.mapsforge;

import android.app.Activity;

/** MapsforgeThemeHelper stub for light build — Mapsforge offline maps not available. */
public class MapsforgeThemeHelper {

    public enum RenderThemeType {
        RTT_NONE("", new String[]{}),
        RTT_ELEVATE("", new String[]{"elevate", "elements"}),
        RTT_FZK_BASE("freizeitkarte-v5", new String[]{"freizeitkarte"}),
        RTT_FZK_OUTDOOR_CONTRAST("fzk-outdoor-contrast-v5", new String[]{"fzk-outdoor-contrast"}),
        RTT_FZK_OUTDOOR_SOFT("fzk-outdoor-soft-v5", new String[]{"fzk-outdoor-soft"}),
        RTT_PAWS("paws_4", new String[]{"paws_4"}),
        RTT_VOLUNTARY("", new String[]{"voluntary v5", "velocity v5"});

        public final String relPath;
        public final String[] searchPaths;

        RenderThemeType(final String relPath, final String[] searchPaths) {
            this.relPath = relPath;
            this.searchPaths = searchPaths;
        }
    }

    public MapsforgeThemeHelper(final Activity activity) {
        // not available in light build
    }

    public void reapplyMapTheme(final Object rendererLayer, final Object tileCache) {
        // not available in light build
    }

    public void selectMapTheme(final Object tileLayer, final Object tileCache) {
        // not available in light build
    }

    public void selectMapThemeOptions() {
        // not available in light build
    }

    public boolean themeOptionsAvailable() {
        return false;
    }

    public static boolean isThemeSynchronizationActive() {
        return false;
    }

    public static RenderThemeType getRenderThemeType() {
        return RenderThemeType.RTT_NONE;
    }

    public static void resynchronizeOrDeleteMapThemeFolder() {
        // not available in light build
    }
}
