package cgeo.geocaching.unifiedmap.layers;

import cgeo.geocaching.storage.ContentStorage;
import cgeo.geocaching.storage.Folder;
import cgeo.geocaching.storage.PersistableFolder;
import cgeo.geocaching.utils.FileUtils;
import cgeo.geocaching.utils.Log;

import android.content.Context;

import java.io.File;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang3.StringUtils;
import org.mapsforge.map.android.graphics.AndroidGraphicFactory;
import org.mapsforge.map.android.mbtiles.MBTilesFile;
import org.mapsforge.map.android.mbtiles.TileMBTilesLayer;
import org.mapsforge.map.layer.cache.InMemoryTileCache;
import org.mapsforge.map.view.MapView;
import org.oscim.android.tiling.source.mbtiles.MBTilesBitmapTileSource;
import org.oscim.layers.tile.bitmap.BitmapTileLayer;

public class MBTilesLayerHelper {

    private static final String TAG = "MBTilesLayerHelper";

    // Cache to track which URIs have been copied to temp files to avoid re-copying on every map load
    // Using ConcurrentHashMap for thread-safety
    private static final Map<String, CachedTempFile> TEMP_FILE_CACHE = new ConcurrentHashMap<>();

    private static class CachedTempFile {
        final File file;
        final long lastModified;

        CachedTempFile(final File file, final long lastModified) {
            this.file = file;
            this.lastModified = lastModified;
        }
    }

    private MBTilesLayerHelper() {
        //no instance
    }

    /** returns a list of BitmapTileLayers for all .mbtiles used for background maps (VTM variant) */
    public static ArrayList<BitmapTileLayer> getBitmapTileLayersVTM(final Context context, final org.oscim.map.Map map) {
        final ArrayList<BitmapTileLayer> result = new ArrayList<>();
        final File[] files = getMBTilesSources(context);
        if (files != null) {
            for (File file : files) {
                result.add(new BitmapTileLayer(map, new MBTilesBitmapTileSource(file.getAbsolutePath(), 192, null)));
            }
        }
        return result;
    }

    /** returns a list of BitmapTileLayers for all .mbtiles used for background maps (Mapsforge variant) */
    public static ArrayList<TileMBTilesLayer> getBitmapTileLayersMapsforge(final Context context, final MapView mapView) {
        final ArrayList<TileMBTilesLayer> result = new ArrayList<>();
        final File[] files = getMBTilesSources(context);
        if (files != null) {
            for (File file : files) {
                Log.e("file: " + file);
                result.add(new TileMBTilesLayer(new InMemoryTileCache(500), mapView.getModel().mapViewPosition, true, new MBTilesFile(file), AndroidGraphicFactory.INSTANCE));
            }
        }
        return result;
    }

    /** returns a list of .mbtiles files found in public folder with fallback to app-specific media folder */
    private static File[] getMBTilesSources(final Context context) {
        final ArrayList<File> result = new ArrayList<>();

        // First, try the new location: public offline maps folder
        final Folder backgroundMapsFolder = PersistableFolder.BACKGROUND_MAPS.getFolder();
        if (backgroundMapsFolder.getBaseType() == Folder.FolderType.FILE) {
            // For FILE-based folders, we can directly use the files
            for (ContentStorage.FileInformation fi : ContentStorage.get().list(backgroundMapsFolder)) {
                if (!fi.isDirectory && StringUtils.endsWithIgnoreCase(fi.name, FileUtils.BACKGROUND_MAP_FILE_EXTENSION)) {
                    final String path = fi.uri.getPath();
                    if (path != null) {
                        final File file = new File(path);
                        if (file.exists()) {
                            result.add(file);
                        }
                    }
                }
            }
        } else {
            // For DOCUMENT-based (SAF) folders, we need to copy files to temp cache
            // since MBTiles libraries require File objects
            for (ContentStorage.FileInformation fi : ContentStorage.get().list(backgroundMapsFolder)) {
                if (!fi.isDirectory && StringUtils.endsWithIgnoreCase(fi.name, FileUtils.BACKGROUND_MAP_FILE_EXTENSION)) {
                    final File cachedFile = getCachedTempFile(fi);
                    if (cachedFile != null && cachedFile.exists()) {
                        result.add(cachedFile);
                    }
                }
            }
        }

        // Fallback to old location: app-specific media folder
        final File[] externalMediaDirs = context.getExternalMediaDirs();
        if (externalMediaDirs != null && externalMediaDirs.length > 0 && externalMediaDirs[0] != null) {
            final File[] legacyFiles = externalMediaDirs[0].listFiles((dir, name) -> StringUtils.endsWithIgnoreCase(name, FileUtils.BACKGROUND_MAP_FILE_EXTENSION));
            if (legacyFiles != null) {
                for (File file : legacyFiles) {
                    result.add(file);
                }
            }
        }

        return result.toArray(new File[0]);
    }

    /**
     * Gets a cached temporary file for a content:// URI.
     * If the file is already cached and hasn't changed, returns the cached version.
     * Otherwise, copies the content to a new temp file.
     */
    private static File getCachedTempFile(final ContentStorage.FileInformation fi) {
        final String uriString = fi.uri.toString();
        final CachedTempFile cached = TEMP_FILE_CACHE.get(uriString);

        // Check if we have a valid cached file that hasn't been modified
        if (cached != null && cached.file.exists() && cached.lastModified == fi.lastModified) {
            Log.d(TAG + ": Using cached temp file for: " + fi.name);
            return cached.file;
        }

        // Need to copy the file to temp storage
        Log.d(TAG + ": Copying " + fi.name + " to temp cache for MBTiles access");
        final File tempFile = ContentStorage.get().writeUriToTempFile(fi.uri, "mbtiles_" + fi.name);
        if (tempFile != null) {
            TEMP_FILE_CACHE.put(uriString, new CachedTempFile(tempFile, fi.lastModified));
        }
        return tempFile;
    }

}
