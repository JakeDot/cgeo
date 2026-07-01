package cgeo.geocaching.unifiedmap.layers;

import cgeo.geocaching.utils.FileUtils;

import android.content.Context;

import java.io.File;
import java.util.ArrayList;

import org.apache.commons.lang3.Strings;
import org.oscim.android.tiling.source.mbtiles.MBTilesBitmapTileSource;
import org.oscim.layers.tile.bitmap.BitmapTileLayer;
import org.oscim.map.Map;

public class MBTilesLayerHelper {

    private MBTilesLayerHelper() {
        //no instance
    }

    /** returns a list of BitmapTileLayers for all .mbtiles used for background maps */
    public static ArrayList<BitmapTileLayer> getBitmapTileLayersVTM(final Context context, final Map map) {
        final ArrayList<BitmapTileLayer> result = new ArrayList<>();
        final File[] files = getMBTilesSources(context);
        if (files != null) {
            for (File file : files) {
                result.add(new BitmapTileLayer(map, new MBTilesBitmapTileSource(file.getAbsolutePath(), 192, null)));
            }
        }
        return result;
    }

    /** returns a list of .mbtiles files found in app-specific media folder, typically /Android/media/(app-id)/*.mbtiles */
    private static File[] getMBTilesSources(final Context context) {
        return context.getExternalMediaDirs()[0].listFiles((dir, name) -> Strings.CS.endsWith(name, FileUtils.BACKGROUND_MAP_FILE_EXTENSION));
    }
}
