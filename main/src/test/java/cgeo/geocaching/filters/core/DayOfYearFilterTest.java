package cgeo.geocaching.filters.core;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.junit.Test;
import static org.assertj.core.api.Java6Assertions.assertThat;

public class DayOfYearFilterTest {

    private static final DateFormat FORMATTER = new SimpleDateFormat("yyyy-MM-dd", Locale.US);

    @Test
    public void sameDayDifferentYears() throws Exception {
        final DayOfYearFilter filter = new DayOfYearFilter();
        final Date date2020 = FORMATTER.parse("2020-04-06");
        final Date date2021 = FORMATTER.parse("2021-04-06");
        final Date date2022 = FORMATTER.parse("2022-04-06");

        filter.setMinMaxDayOfYear(date2021, date2021);
        
        assertThat(filter.matches(date2020)).isTrue();
        assertThat(filter.matches(date2021)).isTrue();
        assertThat(filter.matches(date2022)).isTrue();
    }

    @Test
    public void differentDays() throws Exception {
        final DayOfYearFilter filter = new DayOfYearFilter();
        final Date april6 = FORMATTER.parse("2022-04-06");
        final Date april7 = FORMATTER.parse("2022-04-07");

        filter.setMinMaxDayOfYear(april7, april7);
        
        assertThat(filter.matches(april6)).isFalse();
        assertThat(filter.matches(april7)).isTrue();
    }

    @Test
    public void rangeWithinYear() throws Exception {
        final DayOfYearFilter filter = new DayOfYearFilter();
        final Date march1 = FORMATTER.parse("2022-03-01");
        final Date june30 = FORMATTER.parse("2022-06-30");

        filter.setMinMaxDayOfYear(march1, june30);
        
        final Date april15 = FORMATTER.parse("2021-04-15");
        assertThat(filter.matches(april15)).isTrue();

        final Date february15 = FORMATTER.parse("2022-02-15");
        assertThat(filter.matches(february15)).isFalse();

        final Date july15 = FORMATTER.parse("2022-07-15");
        assertThat(filter.matches(july15)).isFalse();
    }

    @Test
    public void rangeWrappingYearBoundary() throws Exception {
        final DayOfYearFilter filter = new DayOfYearFilter();
        final Date november1 = FORMATTER.parse("2022-11-01");
        final Date february28 = FORMATTER.parse("2022-02-28");

        filter.setMinMaxDayOfYear(november1, february28);
        
        final Date december15 = FORMATTER.parse("2021-12-15");
        assertThat(filter.matches(december15)).isTrue();

        final Date january15 = FORMATTER.parse("2022-01-15");
        assertThat(filter.matches(january15)).isTrue();

        final Date march15 = FORMATTER.parse("2022-03-15");
        assertThat(filter.matches(march15)).isFalse();

        final Date october15 = FORMATTER.parse("2022-10-15");
        assertThat(filter.matches(october15)).isFalse();
    }

    @Test
    public void nullHandling() throws Exception {
        final DayOfYearFilter filter = new DayOfYearFilter();
        
        // Empty filter matches everything including null
        assertThat(filter.matches(null)).isTrue();
        
        // Filter with range returns null for null input (inconclusive)
        final Date date = FORMATTER.parse("2022-04-06");
        filter.setMinMaxDayOfYear(date, date);
        assertThat(filter.matches(null)).isNull();
    }

    @Test
    public void stringConversion() throws Exception {
        final DayOfYearFilter filter = new DayOfYearFilter();
        final Date date = FORMATTER.parse("2022-04-06");
        
        filter.setMinMaxDayOfYear(date, date);
        
        assertThat(filter.getMinDayOfYear()).isEqualTo("04-06");
        assertThat(filter.getMaxDayOfYear()).isEqualTo("04-06");
    }

    @Test
    public void isFilled() throws Exception {
        final DayOfYearFilter filter = new DayOfYearFilter();
        
        assertThat(filter.isFilled()).isFalse();
        
        final Date date = FORMATTER.parse("2022-04-06");
        filter.setMinMaxDayOfYear(date, date);
        
        assertThat(filter.isFilled()).isTrue();
    }
}
