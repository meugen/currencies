package meugeninua.android.currencies.ui.views;

import android.content.Context;
import android.content.res.Resources;
import android.util.AttributeSet;
import android.widget.AdapterView;

import androidx.appcompat.widget.AppCompatSpinner;

public class SmartSpinner extends AppCompatSpinner {

    private Runnable pendingRunnable;

    public SmartSpinner(final Context context) {
        super(context);
    }

    public SmartSpinner(final Context context, final int mode) {
        super(context, mode);
    }

    public SmartSpinner(final Context context, final AttributeSet attrs) {
        super(context, attrs);
    }

    public SmartSpinner(final Context context, final AttributeSet attrs, final int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public SmartSpinner(final Context context, final AttributeSet attrs, final int defStyleAttr, final int mode) {
        super(context, attrs, defStyleAttr, mode);
    }

    public SmartSpinner(final Context context, final AttributeSet attrs, final int defStyleAttr, final int mode, final Resources.Theme popupTheme) {
        super(context, attrs, defStyleAttr, mode, popupTheme);
    }

    public void setSelectionQuietly(final int position) {
        AdapterView.OnItemSelectedListener listener = getOnItemSelectedListener();
        setOnItemSelectedListener(null);
        setSelection(position);
        post(() -> setOnItemSelectedListener(listener));
    }
}
