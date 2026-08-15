package cgeo.geocaching.ui.notifications;

import cgeo.geocaching.CgeoApplication;
import cgeo.geocaching.settings.Settings;
import cgeo.geocaching.ui.ViewUtils;
import cgeo.geocaching.utils.Log;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

public class ToastNotification {

    public static final int LENGTH_SHORT = Toast.LENGTH_SHORT;
    public static final int LENGTH_LONG = Toast.LENGTH_LONG;

    private static final long DURATION_SHORT_MILLIS = 2_000L;
    private static final long DURATION_LONG_MILLIS = 4_000L;

    private final Context context;
    private final CharSequence text;
    private final int duration;

    private ToastNotification(final Context context, final CharSequence text, final int duration) {
        this.context = context;
        this.text = text;
        this.duration = duration;
    }

    public static ToastNotification makeText(@Nullable final Context context, @Nullable final CharSequence text, final int duration) {
        final Context toastContext = context == null ? CgeoApplication.getInstance() : context;
        final CharSequence toastText = text == null ? "---" : text;
        return new ToastNotification(toastContext, toastText, duration);
    }

    public void show() {
        final NotificationCompat.Builder builder = Notifications.newBuilder(context, NotificationChannels.TOAST_REPLACEMENT_NOTIFICATION)
                .setContentText(text)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(text))
                .setOnlyAlertOnce(true)
                .setSilent(true)
                .setAutoCancel(true)
                .setTimeoutAfter(duration == LENGTH_SHORT ? DURATION_SHORT_MILLIS : DURATION_LONG_MILLIS);
        try {
            Notifications.getNotificationManager(context).notify(Settings.getUniqueNotificationId(), builder.build());
        } catch (SecurityException se) {
            Log.w("Could not show silent notification, falling back to system toast", se);
            ViewUtils.runOnUiThread(false, () -> Toast.makeText(context, text, duration).show());
        }
    }
}
