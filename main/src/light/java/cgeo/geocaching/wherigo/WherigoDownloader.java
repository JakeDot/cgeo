package cgeo.geocaching.wherigo;

import cgeo.geocaching.connector.StatusResult;

import android.content.Context;
import android.net.Uri;

import androidx.activity.ComponentActivity;

import java.util.function.BiConsumer;
import java.util.function.Function;

/** WherigoDownloader stub for light build — Wherigo not available. */
public class WherigoDownloader {

    public WherigoDownloader(final ComponentActivity activity, final BiConsumer<String, StatusResult> wherigoDownloadConsumer) {
        // not available in light build
    }

    public void downloadWherigo(final String cguid, final Function<String, Uri> targetUriSupplier) {
        // not available in light build
    }

    public static void guideManualDownload(final Context ctx, final String cguid) {
        // not available in light build
    }
}
