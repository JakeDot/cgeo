package cgeo.geocaching.calendar;

import cgeo.geocaching.models.Geocache;

import android.app.Activity;

import androidx.annotation.NonNull;

public class CalendarAdder {

    private CalendarAdder() {
        // utility class
    }

    public static void addToCalendar(@NonNull final Activity activity, @NonNull final Geocache cache) {
        // not available in light build
    }
}
