package cgeo.geocaching.ui;

import cgeo.geocaching.R;
import cgeo.geocaching.databinding.DateRangeSelectorViewBinding;
import cgeo.geocaching.utils.functions.Action1;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang3.tuple.ImmutablePair;

/**
 * UI component for selecting a day-of-year range (month and day, ignoring year).
 * Used for 365/366 matrix challenges.
 */
public class DayOfYearRangeSelector extends LinearLayout {

    private final DateTimeEditor minDateEditor = new DateTimeEditor();
    private final DateTimeEditor maxDateEditor = new DateTimeEditor();
    private Action1<ImmutablePair<Date, Date>> changeListener;

    public DayOfYearRangeSelector(final Context context) {
        super(context);
        init();
    }

    public DayOfYearRangeSelector(final Context context, @Nullable final AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public DayOfYearRangeSelector(final Context context, @Nullable final AttributeSet attrs, final int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public DayOfYearRangeSelector(final Context context, final AttributeSet attrs, final int defStyleAttr, final int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    public void setChangeListener(final Action1<ImmutablePair<Date, Date>> listener) {
        this.changeListener = listener;
    }

    private void init() {
        final View view = LayoutInflater.from(getContext()).inflate(R.layout.date_range_selector_view, this, true);
        final DateRangeSelectorViewBinding binding = DateRangeSelectorViewBinding.bind(view);
        this.setOrientation(VERTICAL);

        // Use a fixed year (e.g., 2000) as we only care about month and day
        final Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, 2000);
        final Date preselectDate = cal.getTime();

        minDateEditor.init(binding.dateFrom, null, binding.dateFromReset, ((FragmentActivity) getContext()).getSupportFragmentManager());
        minDateEditor
                .setPreselectDate(preselectDate)
                .setChangeListener(d -> onChange(d, true));
        
        maxDateEditor.init(binding.dateTo, null, binding.dateToReset, ((FragmentActivity) getContext()).getSupportFragmentManager());
        maxDateEditor
                .setPreselectDate(preselectDate)
                .setChangeListener(d -> onChange(d, false));
        
        minDateEditor.setDate(null);
        maxDateEditor.setDate(null);
    }

    private void onChange(final Date d, final boolean minFieldChanged) {
        if (minFieldChanged) {
            maxDateEditor.setPreselectDate(d);
        } else {
            minDateEditor.setPreselectDate(d);
        }

        if (changeListener != null) {
            changeListener.call(new ImmutablePair<>(minDateEditor.getDate(), maxDateEditor.getDate()));
        }
    }

    public void setMinMaxDate(final Date min, final Date max) {
        minDateEditor.setDate(min);
        maxDateEditor.setDate(max);
    }

    public Date getMinDate() {
        return minDateEditor.getDate();
    }

    public Date getMaxDate() {
        return maxDateEditor.getDate();
    }
}
