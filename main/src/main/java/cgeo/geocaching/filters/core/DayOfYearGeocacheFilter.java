package cgeo.geocaching.filters.core;

import cgeo.geocaching.models.Geocache;
import cgeo.geocaching.storage.SqlBuilder;
import cgeo.geocaching.utils.config.LegacyFilterConfig;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.Date;

import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * Filters geocaches by day of year (month and day) of their hidden date, ignoring the year.
 * This is useful for 365/366 matrix challenges where caches need to be found on all calendar days.
 */
public class DayOfYearGeocacheFilter extends BaseGeocacheFilter {

    private final DayOfYearFilter dayOfYearFilter = new DayOfYearFilter();

    protected Date getDate(final Geocache cache) {
        return cache.getHiddenDate();
    }

    protected String getSqlColumnName() {
        return "hidden";
    }

    @Override
    public Boolean filter(final Geocache cache) {
        return dayOfYearFilter.matches(getDate(cache));
    }

    public String getMinDayOfYear() {
        return dayOfYearFilter.getMinDayOfYear();
    }

    public String getMaxDayOfYear() {
        return dayOfYearFilter.getMaxDayOfYear();
    }

    public void setMinMaxDayOfYear(final String min, final String max) {
        this.dayOfYearFilter.setMinMaxDayOfYear(min, max);
    }

    public void setMinMaxDayOfYear(final Date min, final Date max) {
        this.dayOfYearFilter.setMinMaxDayOfYear(min, max);
    }

    public DayOfYearFilter getDayOfYearFilter() {
        return dayOfYearFilter;
    }

    @Override
    public void setConfig(final LegacyFilterConfig config) {
        dayOfYearFilter.setConfig(config.get(null));
    }

    @Override
    public LegacyFilterConfig getConfig() {
        final LegacyFilterConfig config = new LegacyFilterConfig();
        config.put(null, dayOfYearFilter.getConfig());
        return config;
    }

    @Nullable
    @Override
    public ObjectNode getJsonConfig() {
        return dayOfYearFilter.getJsonConfig();
    }

    @Override
    public void setJsonConfig(@NonNull final ObjectNode node) {
        dayOfYearFilter.setJsonConfig(node);
    }

    @Override
    public boolean isFiltering() {
        return dayOfYearFilter.isFilled();
    }

    @Override
    public void addToSql(final SqlBuilder sqlBuilder) {
        addToSql(sqlBuilder, getSqlColumnName() == null ? null : sqlBuilder.getMainTableId() + "." + getSqlColumnName());
    }

    protected void addToSql(final SqlBuilder sqlBuilder, final String valueExpression) {
        dayOfYearFilter.addToSql(sqlBuilder, valueExpression);
    }

    @Override
    protected String getUserDisplayableConfig() {
        return dayOfYearFilter.getUserDisplayableConfig();
    }
}
