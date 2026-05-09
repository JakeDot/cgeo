package cgeo.geocaching.wherigo;

import cgeo.geocaching.storage.ContentStorage;
import cgeo.geocaching.utils.AudioManager;
import cgeo.geocaching.utils.ListenerHelper;
import cgeo.geocaching.wherigo.openwig.Zone;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

/** WherigoGame stub for light build — Wherigo game engine not available. */
public class WherigoGame {

    public static final Object GP_CONVERTER = null;

    public enum NotifyType {
        REFRESH, START, END, LOCATION, DIALOG_OPEN, DIALOG_CLOSE
    }

    private final AudioManager audioManager = new AudioManager();
    private final ListenerHelper<Consumer<NotifyType>> listeners = new ListenerHelper<>();

    private static final WherigoGame INSTANCE = new WherigoGame();

    public static WherigoGame get() {
        return INSTANCE;
    }

    private WherigoGame() {
        // singleton
    }

    public int addListener(final Consumer<NotifyType> listener) {
        return listeners.addListener(listener);
    }

    public void removeListener(final int listenerId) {
        listeners.removeListener(listenerId);
    }

    public boolean isPlaying() {
        return false;
    }

    public boolean dialogIsPaused() {
        return false;
    }

    public void unpauseDialog() {
        // not available in light build
    }

    public void newGame(@NonNull final ContentStorage.FileInformation cartridgeInfo) {
        // not available in light build
    }

    public void loadGame(@NonNull final ContentStorage.FileInformation cartridgeFileInfo, @Nullable final WherigoSavegameInfo saveGame) {
        // not available in light build
    }

    public void saveGame(final WherigoSavegameInfo saveGame) {
        // not available in light build
    }

    public void stopGame() {
        // not available in light build
    }

    public String getCGuid() {
        return "";
    }

    public WherigoCartridgeInfo getCartridgeInfo() {
        return null;
    }

    public String getCartridgeName() {
        return "-";
    }

    public String getLastError() {
        return null;
    }

    public String getLastErrorCGuid() {
        return null;
    }

    public void clearLastError() {
        // not available in light build
    }

    public boolean isLastErrorNotSeen() {
        return false;
    }

    public void clearLastErrorNotSeen() {
        // not available in light build
    }

    public List<Zone> getZones() {
        return Collections.emptyList();
    }

    @Nullable
    public Zone getZone(final String name) {
        return null;
    }

    public List<?> getThings() {
        return Collections.emptyList();
    }

    public List<?> getTasks() {
        return Collections.emptyList();
    }

    public List<?> getInventory() {
        return Collections.emptyList();
    }

    public List<?> getItems() {
        return Collections.emptyList();
    }

    @Nullable
    public Object getPlayer() {
        return null;
    }

    public String getContextGeocode() {
        return null;
    }

    public String getContextGeocacheName() {
        return null;
    }

    public String getLastPlayedCGuid() {
        return null;
    }

    public String getLastSetContextGeocode() {
        return null;
    }

    public void notifyListeners(final NotifyType type) {
        listeners.forEachListener(l -> l.accept(type));
    }

    public void setContextGeocode(final String geocode) {
        // not available in light build
    }

    public void refresh() {
        // not available in light build
    }

    public void start() {
        // not available in light build
    }

    public void end() {
        // not available in light build
    }

    public void destroy() {
        // not available in light build
    }

    public void showError(final String errorMessage) {
        // not available in light build
    }

    public AudioManager getAudioManager() {
        return audioManager;
    }

    public boolean isDebugModeForCartridge() {
        return false;
    }

    public boolean isDebugMode() {
        return false;
    }

    @Override
    public String toString() {
        return "WherigoGame[light-stub]";
    }
}
