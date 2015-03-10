package ru.itprospect.everydaybiblereading;


import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

public class DatePickerFragment extends DialogFragment {
    private int mYear;
	private int mMonth;
	private int mDay;
	
	public void setDate(int year, int monthOfYear, int dayOfMonth) {
		mYear = year;
		mMonth = monthOfYear;
		mDay = dayOfMonth;
	}

    public Dialog onCreateDialog(Bundle savedInstanceState) {
    	Log.e("EBR", "dpd: onCreateDialog");
    	OnDateSetListener dateSetListener = (MainActivity) getActivity();
    	DialogInterface.OnClickListener clickListener = (MainActivity) getActivity();
    	
    	DatePickerDialog dialog = new DatePickerDialog(getActivity(), dateSetListener, mYear, mMonth, mDay);
    	dialog.setButton(DialogInterface.BUTTON_NEUTRAL,
    			getActivity().getApplicationContext().getText(R.string.today),
    			clickListener);
    	
    	return dialog;
    }

}