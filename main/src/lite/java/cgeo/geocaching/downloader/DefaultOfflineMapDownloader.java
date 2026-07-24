package cgeo.geocaching.downloader;

import cgeo.geocaching.models.Download;
import cgeo.geocaching.network.Network;
import cgeo.geocaching.settings.Settings;
import cgeo.geocaching.storage.ContentStorage;
import cgeo.geocaching.storage.PersistableFolder;
import cgeo.geocaching.unifiedmap.tileproviders.TileProviderFactory;
import cgeo.geocaching.utils.AndroidRxUtils;
import cgeo.geocaching.utils.FileUtils;
import cgeo.geocaching.utils.Log;

import android.net.Uri;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Locale;

import org.apache.commons.io.IOUtils;

/**
 * The "lite" flavor has no online tile sources, so a brand-new install would
 * show a blank map until the user downloads an offline map manually. To avoid
 * that, this downloads a small default world map in the background on first
 * run, unless the user already has an offline map.
 */
public final class DefaultOfflineMapDownloader {

    private static final String REMOTE_URL = "https://ftp-stud.hs-esslingen.de/pub/Mirrors/download.mapsforge.org/maps/v5/world/world.map";
    private static final String LOCAL_FILENAME = "world.map";
    private static final String DISPLAY_NAME = "World";

    private DefaultOfflineMapDownloader() {
        // utility class
    }

    public static void downloadIfNeededInBackground() {
        if (Settings.hasAttemptedDefaultOfflineMapDownload()) {
            return;
        }
        AndroidRxUtils.networkScheduler.scheduleDirect(DefaultOfflineMapDownloader::downloadIfNeeded);
    }

    private static synchronized void downloadIfNeeded() {
        if (Settings.hasAttemptedDefaultOfflineMapDownload()) {
            return;
        }
        Settings.setAttemptedDefaultOfflineMapDownload(true);
        if (hasExistingOfflineMap()) {
            return;
        }
        Uri target = null;
        try {
            target = ContentStorage.get().create(PersistableFolder.OFFLINE_MAPS, LOCAL_FILENAME);
            if (target == null) {
                Log.i("DefaultOfflineMapDownloader: storage error, target URI is null");
                return;
            }
            try (InputStream in = Network.getResponseStream(Network.getRequest(REMOTE_URL));
                 OutputStream out = ContentStorage.get().openForWrite(target)) {
                if (in == null || out == null) {
                    Log.i("DefaultOfflineMapDownloader: no connection or storage error, skipping default map download");
                    ContentStorage.get().delete(target);
                    return;
                }
                IOUtils.copy(in, out);
            }
            CompanionFileUtils.writeInfo(REMOTE_URL, LOCAL_FILENAME, DISPLAY_NAME, System.currentTimeMillis(), Download.DownloadType.DOWNLOADTYPE_MAP_MAPSFORGE.id);
            TileProviderFactory.buildTileProviderList(true);
            Log.i("DefaultOfflineMapDownloader: default world map downloaded successfully");
        } catch (final Exception e) {
            Log.i("DefaultOfflineMapDownloader: failed to download default world map", e);
            if (target != null) {
                ContentStorage.get().delete(target);
            }
        }
    }

    private static boolean hasExistingOfflineMap() {
        for (final ContentStorage.FileInformation fi : ContentStorage.get().list(PersistableFolder.OFFLINE_MAPS, true)) {
            if (!fi.isDirectory && fi.name.toLowerCase(Locale.getDefault()).endsWith(FileUtils.MAP_FILE_EXTENSION)) {
                return true;
            }
        }
        return false;
    }
}
