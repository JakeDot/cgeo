package cgeo.geocaching.speech;

import cgeo.geocaching.location.Geopoint;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import androidx.activity.ComponentActivity;
import androidx.annotation.Nullable;

/** Service to speak the compass directions — stub for light build. */
public class SpeechService extends Service {

    public static void toggleService(final ComponentActivity activity, final Geopoint dstCoords) {
        // not available in light build
    }

    public static boolean isRunning() {
        return false;
    }

    @Nullable
    @Override
    public IBinder onBind(final Intent intent) {
        return null;
    }
}
