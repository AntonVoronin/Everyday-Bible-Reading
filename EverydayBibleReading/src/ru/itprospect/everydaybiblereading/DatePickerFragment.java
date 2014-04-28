package ru.itprospect.everydaybiblereading;

import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

public class DatePickerFragment extends DialogFragment {
    private OnDateSetListener listener;
	private int mYear;
	private int mMonth;
	private int mDay;
    
    public void setParam(OnDateSetListener listener, int mYear, int mMonth, int mDay) {
    	this.listener = listener;
    	this.mYear = mYear;
    	this.mMonth = mMonth;
    	this.mDay = mDay;
    }

    public Dialog onCreateDialog(Bundle savedInstanceState) {
         return new DatePickerDialog(getActivity(), listener, mYear, mMonth, mDay);
    }
}