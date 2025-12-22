package cgeo.geocaching.filters.gui;

import cgeo.geocaching.filters.core.DayOfYearGeocacheFilter;
import cgeo.geocaching.ui.DayOfYearRangeSelector;

import android.view.View;


public class DayOfYearFilterViewHolder extends BaseFilterViewHolder<DayOfYearGeocacheFilter> {

    private DayOfYearRangeSelector dayOfYearRangeSelector;

    public DayOfYearFilterViewHolder() {
        // Default constructor
    }

    @Override
    public View createView() {
        dayOfYearRangeSelector = new DayOfYearRangeSelector(getActivity());
        return dayOfYearRangeSelector;
    }

    @Override
    public void setViewFromFilter(final DayOfYearGeocacheFilter filter) {
        // Convert day-of-year strings to dates for the UI component
        final java.util.Date minDate = parseDayOfYearToDate(filter.getMinDayOfYear());
        final java.util.Date maxDate = parseDayOfYearToDate(filter.getMaxDayOfYear());
        dayOfYearRangeSelector.setMinMaxDate(minDate, maxDate);
    }

    @Override
    public DayOfYearGeocacheFilter createFilterFromView() {
        final DayOfYearGeocacheFilter filter = createFilter();
        filter.setMinMaxDayOfYear(dayOfYearRangeSelector.getMinDate(), dayOfYearRangeSelector.getMaxDate());
        return filter;
    }

    /**
     * Parse a day-of-year string (MM-dd) to a Date object.
     * Uses year 2000 as the base year since we only care about month and day.
     */
    private java.util.Date parseDayOfYearToDate(final String dayOfYear) {
        if (dayOfYear == null || dayOfYear.isEmpty()) {
            return null;
        }
        try {
            final String[] parts = dayOfYear.split("-");
            if (parts.length != 2) {
                return null;
            }
            final java.util.Calendar cal = java.util.Calendar.getInstance();
            cal.set(java.util.Calendar.YEAR, 2000);
            cal.set(java.util.Calendar.MONTH, Integer.parseInt(parts[0]) - 1); // Month is 0-based
            cal.set(java.util.Calendar.DAY_OF_MONTH, Integer.parseInt(parts[1]));
            cal.set(java.util.Calendar.HOUR_OF_DAY, 0);
            cal.set(java.util.Calendar.MINUTE, 0);
            cal.set(java.util.Calendar.SECOND, 0);
            cal.set(java.util.Calendar.MILLISECOND, 0);
            return cal.getTime();
        } catch (Exception e) {
            return null;
        }
    }
}
