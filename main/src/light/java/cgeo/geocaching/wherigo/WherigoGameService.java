package cgeo.geocaching.wherigo;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;

/** WherigoGameService stub for light build — Wherigo game engine not available. */
public class WherigoGameService extends Service {

    @Nullable
    @Override
    public IBinder onBind(final Intent intent) {
        return null;
    }

    public static void startService() {
        // not available in light build
    }

    public static void stopService() {
        // not available in light build
    }
}
