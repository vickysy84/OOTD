package com.vickysy.ootd.ui;

import android.widget.ArrayAdapter;
import android.widget.Spinner;

/**
 * Created by vickysy on 3/12/15.
 */
public class UIUtilities {

    public static void setSpinnerValue (Spinner spinner, String compareValue) {
        ArrayAdapter<CharSequence> adapter= (ArrayAdapter)spinner.getAdapter();
        if (!compareValue.equals(null)) {
            int spinnerPosition = adapter.getPosition(compareValue);
            spinner.setSelection(spinnerPosition);
        }
    }
}
