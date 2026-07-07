package cgeo.geocaching.wherigo;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;

/**
 * The "lite" flavor has no Wherigo player, so this bar is never shown. See
 * the "full" flavor's version of this class, which is embedded directly in
 * shared layouts (main_activity.xml, filter_sort_bar.xml).
 */
public class WherigoInfoBarView extends RelativeLayout {

    public WherigoInfoBarView(final Context context) {
        super(context);
        setVisibility(View.GONE);
    }

    public WherigoInfoBarView(final Context context, @Nullable final AttributeSet attrs) {
        super(context, attrs);
        setVisibility(View.GONE);
    }

    public WherigoInfoBarView(final Context context, @Nullable final AttributeSet attrs, final int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setVisibility(View.GONE);
    }

    public WherigoInfoBarView(final Context context, final AttributeSet attrs, final int defStyleAttr, final int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        setVisibility(View.GONE);
    }
}
