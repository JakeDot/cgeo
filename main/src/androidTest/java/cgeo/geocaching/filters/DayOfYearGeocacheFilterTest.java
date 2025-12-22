package cgeo.geocaching.filters;

import cgeo.geocaching.filters.core.DayOfYearGeocacheFilter;
import cgeo.geocaching.filters.core.GeocacheFilterType;
import cgeo.geocaching.models.Geocache;
import cgeo.geocaching.utils.functions.Action1;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.junit.Test;
import static org.assertj.core.api.Java6Assertions.assertThat;

/**
 * Tests for the DayOfYearGeocacheFilter.
 * This filter matches caches by day-of-year (month and day) ignoring the year.
 */
public class DayOfYearGeocacheFilterTest {

    private static final DateFormat FORMATTER = new SimpleDateFormat("yyyy-MM-dd", Locale.US);

    @Test
    @SuppressWarnings("PMD.JUnitTestsShouldIncludeAssert") // is done in called test method
    public void sameDayDifferentYears() throws Exception {
        // Test that same day in different years matches
        final Date date2020 = FORMATTER.parse("2020-04-06");
        final Date date2021 = FORMATTER.parse("2021-04-06");
        final Date date2022 = FORMATTER.parse("2022-04-06");

        assertSingle(c -> c.setHidden(date2020), f -> f.setMinMaxDayOfYear(date2021, date2021), true);
        assertSingle(c -> c.setHidden(date2022), f -> f.setMinMaxDayOfYear(date2021, date2021), true);
    }

    @Test
    @SuppressWarnings("PMD.JUnitTestsShouldIncludeAssert")
    public void differentDays() throws Exception {
        // Test that different days don't match
        final Date april6 = FORMATTER.parse("2022-04-06");
        final Date april7 = FORMATTER.parse("2022-04-07");

        assertSingle(c -> c.setHidden(april6), f -> f.setMinMaxDayOfYear(april7, april7), false);
        assertSingle(c -> c.setHidden(april7), f -> f.setMinMaxDayOfYear(april6, april6), false);
    }

    @Test
    @SuppressWarnings("PMD.JUnitTestsShouldIncludeAssert")
    public void rangeWithinYear() throws Exception {
        // Test a range that doesn't wrap around year boundary
        final Date march1 = FORMATTER.parse("2022-03-01");
        final Date june30 = FORMATTER.parse("2022-06-30");
        final Date april15 = FORMATTER.parse("2021-04-15");

        // april15 should match because it's between march1 and june30 (ignoring year)
        assertSingle(c -> c.setHidden(april15), f -> f.setMinMaxDayOfYear(march1, june30), true);

        final Date february15 = FORMATTER.parse("2022-02-15");
        // february15 should NOT match
        assertSingle(c -> c.setHidden(february15), f -> f.setMinMaxDayOfYear(march1, june30), false);

        final Date july15 = FORMATTER.parse("2022-07-15");
        // july15 should NOT match
        assertSingle(c -> c.setHidden(july15), f -> f.setMinMaxDayOfYear(march1, june30), false);
    }

    @Test
    @SuppressWarnings("PMD.JUnitTestsShouldIncludeAssert")
    public void rangeWrappingYearBoundary() throws Exception {
        // Test a range that wraps around year boundary (e.g., Nov to Feb)
        final Date november1 = FORMATTER.parse("2022-11-01");
        final Date february28 = FORMATTER.parse("2022-02-28");

        // December should match
        final Date december15 = FORMATTER.parse("2021-12-15");
        assertSingle(c -> c.setHidden(december15), f -> f.setMinMaxDayOfYear(november1, february28), true);

        // January should match
        final Date january15 = FORMATTER.parse("2022-01-15");
        assertSingle(c -> c.setHidden(january15), f -> f.setMinMaxDayOfYear(november1, february28), true);

        // March should NOT match
        final Date march15 = FORMATTER.parse("2022-03-15");
        assertSingle(c -> c.setHidden(march15), f -> f.setMinMaxDayOfYear(november1, february28), false);

        // October should NOT match
        final Date october15 = FORMATTER.parse("2022-10-15");
        assertSingle(c -> c.setHidden(october15), f -> f.setMinMaxDayOfYear(november1, february28), false);
    }

    @Test
    @SuppressWarnings("PMD.JUnitTestsShouldIncludeAssert")
    public void leapYearHandling() throws Exception {
        // Test February 29th
        final Date feb29_2020 = FORMATTER.parse("2020-02-29");
        final Date feb29_2024 = FORMATTER.parse("2024-02-29");

        // Same leap day in different years should match
        assertSingle(c -> c.setHidden(feb29_2020), f -> f.setMinMaxDayOfYear(feb29_2024, feb29_2024), true);
    }

    @Test
    public void stringConversion() throws Exception {
        final Date date = FORMATTER.parse("2022-04-06");
        final DayOfYearGeocacheFilter filter = GeocacheFilterType.DAY_OF_YEAR.create();
        filter.setMinMaxDayOfYear(date, date);
        
        assertThat(filter.getMinDayOfYear()).isEqualTo("04-06");
        assertThat(filter.getMaxDayOfYear()).isEqualTo("04-06");
    }

    @Test
    public void nullHandling() throws Exception {
        // Test that null dates are handled properly
        assertSingle(c -> c.setHidden(null), f -> f.setMinMaxDayOfYear((Date) null, null), true);
        
        final Date date = FORMATTER.parse("2022-04-06");
        assertSingle(c -> c.setHidden(null), f -> f.setMinMaxDayOfYear(date, date), null);
    }

    private void assertSingle(final Action1<Geocache> cacheSetter, final Action1<DayOfYearGeocacheFilter> filterSetter, final Boolean expectedResult) {
        GeocacheFilterTestUtils.testSingle(GeocacheFilterType.DAY_OF_YEAR, cacheSetter, filterSetter, expectedResult);
    }
}
