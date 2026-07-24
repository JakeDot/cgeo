package cgeo.geocaching.wherigo;

import cgeo.geocaching.storage.ContentStorage;
import cgeo.geocaching.utils.TextUtils;

import java.util.Arrays;

/**
 * Extracted from SystemInformation so that class does not need to depend
 * directly on Wherigo classes, which only exist in the "full" flavors.
 */
public final class WherigoSystemInfo {

    private WherigoSystemInfo() {
        // utility class
    }

    public static void append(final StringBuilder body) {
        final WherigoGame game = WherigoGame.get();
        final ContentStorage.FileInformation cartridgeFileInfo = game.getCartridgeInfo() == null ? null : game.getCartridgeInfo().getFileInfo();
        final CharSequence loadFileInfo = TextUtils.join(WherigoSavegameInfo.getAllSaveFiles(cartridgeFileInfo), WherigoSavegameInfo::toShortString, ", ");
        final CharSequence visibleThingsCounts = TextUtils.join(Arrays.asList(WherigoThingType.values()), tt -> tt.name() + ":" + tt.getThingsForUserDisplay().size(), ", ");
        body.append("\n")
            .append("\nWherigo")
            .append("\n-------")
            .append("\n- playing:").append(game.isPlaying()).append(", debug:").append(game.isDebugMode()).append(", debugFC:").append(game.isDebugModeForCartridge())
            .append("\n- Name: ").append(game.getCartridgeName()).append(" (").append(game.getCGuid()).append(")")
            .append("\n- Cache context: ").append(game.getContextGeocacheName())
            .append("\n- Last Error: ").append(game.getLastError())
            .append("\n- Last Played: ").append(game.getLastPlayedCGuid()).append(" / ").append(game.getLastSetContextGeocode())
            .append("\n- Visible things: ").append(visibleThingsCounts)
            .append("\n- Cartridge File: ").append(cartridgeFileInfo)
            .append("\n- Load Slots: ").append(loadFileInfo);
    }
}
