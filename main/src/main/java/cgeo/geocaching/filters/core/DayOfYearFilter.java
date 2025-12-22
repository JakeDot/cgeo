package cgeo.geocaching.filters.core;

import cgeo.geocaching.storage.SqlBuilder;
import cgeo.geocaching.utils.JsonUtils;
import cgeo.geocaching.utils.Log;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.commons.lang3.StringUtils;

/**
 * Filter for matching dates by day of year (month and day), ignoring the year.
 * This is useful for challenges requiring finds on all calendar days (365/366 matrix).
 */
public class DayOfYearFilter {

    private static final DateFormat DAY_MONTH_FORMAT = new SimpleDateFormat("MM-dd", Locale.US);
    public static final DateFormat DAY_MONTH_FORMAT_USER_DISPLAY = DAY_MONTH_FORMAT;

    private String minDayOfYear; // Format: "MM-dd" (e.g., "01-01" for January 1st)
    private String maxDayOfYear; // Format: "MM-dd" (e.g., "12-31" for December 31st)

    public Boolean matches(final Date value) {
        if (value == null) {
            return getMinDayOfYear() == null && getMaxDayOfYear() == null ? true : null;
        }

        if (getMinDayOfYear() == null && getMaxDayOfYear() == null) {
            return true;
        }

        final String valueDayOfYear = DAY_MONTH_FORMAT.format(value);

        // Handle simple case where range doesn't wrap around year boundary
        if (getMinDayOfYear() != null && getMaxDayOfYear() != null) {
            if (getMinDayOfYear().compareTo(getMaxDayOfYear()) <= 0) {
                // Normal range (e.g., 03-01 to 11-30)
                return valueDayOfYear.compareTo(getMinDayOfYear()) >= 0 &&
                       valueDayOfYear.compareTo(getMaxDayOfYear()) <= 0;
            } else {
                // Wraps around year boundary (e.g., 11-01 to 02-28)
                return valueDayOfYear.compareTo(getMinDayOfYear()) >= 0 ||
                       valueDayOfYear.compareTo(getMaxDayOfYear()) <= 0;
            }
        } else if (getMinDayOfYear() != null) {
            return valueDayOfYear.compareTo(getMinDayOfYear()) >= 0;
        } else {
            return valueDayOfYear.compareTo(getMaxDayOfYear()) <= 0;
        }
    }

    public String getMinDayOfYear() {
        return minDayOfYear;
    }

    public String getMaxDayOfYear() {
        return maxDayOfYear;
    }

    public void setMinMaxDayOfYear(final String min, final String max) {
        if (min != null && max != null && min.compareTo(max) > 0) {
            // Allow wrapping around year boundary
            this.minDayOfYear = min;
            this.maxDayOfYear = max;
        } else {
            this.minDayOfYear = min;
            this.maxDayOfYear = max;
        }
    }

    public void setMinMaxDayOfYear(final Date min, final Date max) {
        this.minDayOfYear = min == null ? null : DAY_MONTH_FORMAT.format(min);
        this.maxDayOfYear = max == null ? null : DAY_MONTH_FORMAT.format(max);
    }

    public void setConfig(final List<String> config) {
        if (config != null && !config.isEmpty()) {
            minDayOfYear = config.isEmpty() ? null : parseDayOfYear(config.get(0));
            maxDayOfYear = config.size() > 1 ? parseDayOfYear(config.get(1)) : null;
        }
    }

    public List<String> getConfig() {
        final List<String> config = new ArrayList<>();
        config.add(minDayOfYear == null ? "-" : minDayOfYear);
        config.add(maxDayOfYear == null ? "-" : maxDayOfYear);
        return config;
    }

    private String parseDayOfYear(final String text) {
        if (StringUtils.isBlank(text) || "-".equals(text)) {
            return null;
        }
        // Validate format
        try {
            DAY_MONTH_FORMAT.parse(text);
            return text;
        } catch (ParseException pe) {
            Log.w("Problem parsing '" + text + "' as day-of-year", pe);
            return null;
        }
    }

    public void setJsonConfig(final JsonNode node) {
        if (node != null) {
            minDayOfYear = JsonUtils.getText(node, "minDayOfYear", null);
            maxDayOfYear = JsonUtils.getText(node, "maxDayOfYear", null);
        }
    }

    public ObjectNode getJsonConfig() {
        final ObjectNode node = JsonUtils.createObjectNode();
        JsonUtils.setText(node, "minDayOfYear", minDayOfYear);
        JsonUtils.setText(node, "maxDayOfYear", maxDayOfYear);
        return node;
    }

    public boolean isFilled() {
        return getMinDayOfYear() != null || getMaxDayOfYear() != null;
    }

    public void addToSql(final SqlBuilder sqlBuilder, final String valueExpression) {
        if (valueExpression != null && (getMinDayOfYear() != null || getMaxDayOfYear() != null)) {
            sqlBuilder.openWhere(SqlBuilder.WhereType.AND);
            
            // Extract month and day from the date value in SQLite
            // strftime('%m-%d', date(hidden/1000, 'unixepoch')) gives us "MM-DD" format
            final String dayOfYearExpression = "strftime('%m-%d', date(" + valueExpression + "/1000, 'unixepoch'))";
            
            if (getMinDayOfYear() != null && getMaxDayOfYear() != null) {
                if (getMinDayOfYear().compareTo(getMaxDayOfYear()) <= 0) {
                    // Normal range (doesn't wrap around year boundary)
                    sqlBuilder.addWhere(dayOfYearExpression + " >= '" + getMinDayOfYear() + "'");
                    sqlBuilder.addWhere(dayOfYearExpression + " <= '" + getMaxDayOfYear() + "'");
                } else {
                    // Wraps around year boundary (e.g., 11-01 to 02-28)
                    sqlBuilder.openWhere(SqlBuilder.WhereType.OR);
                    sqlBuilder.addWhere(dayOfYearExpression + " >= '" + getMinDayOfYear() + "'");
                    sqlBuilder.addWhere(dayOfYearExpression + " <= '" + getMaxDayOfYear() + "'");
                    sqlBuilder.closeWhere();
                }
            } else if (getMinDayOfYear() != null) {
                sqlBuilder.addWhere(dayOfYearExpression + " >= '" + getMinDayOfYear() + "'");
            } else {
                sqlBuilder.addWhere(dayOfYearExpression + " <= '" + getMaxDayOfYear() + "'");
            }
            sqlBuilder.closeWhere();
        } else {
            sqlBuilder.addWhereTrue();
        }
    }

    public String getUserDisplayableConfig() {
        String minValueString = null;
        String maxValueString = null;

        if (getMinDayOfYear() != null) {
            minValueString = getMinDayOfYear();
        }
        if (getMaxDayOfYear() != null) {
            maxValueString = getMaxDayOfYear();
        }

        return UserDisplayableStringUtils.getUserDisplayableConfig(minValueString, maxValueString);
    }
}
