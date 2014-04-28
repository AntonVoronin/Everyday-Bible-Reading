package ru.itprospect.everydaybiblereading;

import android.content.Context;
import android.preference.ListPreference;
import android.util.AttributeSet;

public class ListPreferenceShowSummary extends ListPreference {

    //private final static String TAG = ListPreferenceShowSummary.class.getName();

    public ListPreferenceShowSummary(Context context, AttributeSet attrs) {
        super(context, attrs);

    }

    public ListPreferenceShowSummary(Context context) {
        super(context);

    }


    
    @Override
    public void setValue(String value) {
        super.setValue(value);
        setSummary(getEntry());
    }

    @Override
    public CharSequence getSummary() {
        return super.getEntry();
    }
}
