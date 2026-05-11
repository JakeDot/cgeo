package cgeo.geocaching;

import cgeo.geocaching.settings.Settings;

import org.oscim.backend.CanvasAdapter;

/** Applies VTM (vector tile map) canvas scaling settings from user preferences. */
public class VtmSettings {

    private VtmSettings() {
        // utility class
    }

    public static void applyUserScale() {
        CanvasAdapter.userScale = Settings.getInt(R.string.pref_vtmUserScale, 100) / 100.0f;
        CanvasAdapter.textScale = Settings.getInt(R.string.pref_vtmTextScale, 100) / 100f;
        CanvasAdapter.symbolScale = Settings.getInt(R.string.pref_vtmSymbolScale, 100) / 100f;
    }
}
