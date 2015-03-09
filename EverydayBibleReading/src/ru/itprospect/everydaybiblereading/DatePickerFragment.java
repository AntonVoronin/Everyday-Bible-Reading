package ru.itprospect.everydaybiblereading;

import java.util.Calendar;
import java.util.GregorianCalendar;

import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.content.DialogInterface;
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
    	DatePickerDialog dialog = new DatePickerDialog(getActivity(), listener, mYear, mMonth, mDay);
    	dialog.setButton(DialogInterface.BUTTON_NEUTRAL, 
    			getActivity().getApplicationContext().getText(R.string.today), 
    			new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == DialogInterface.BUTTON_NEUTRAL) {
                        	GregorianCalendar c = new GregorianCalendar();
                        	if (listener !=null) {
                        		listener.onDateSet(null, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
                        		}
                        	else {
                        		//TODO ≈сли экран был перевернут, то listener становитс€ null и установить на сегодн€ не удаетс€
                        		}
                        	//Toast.makeText(getActivity().getApplicationContext(), "today", Toast.LENGTH_SHORT).show();
                        }
                     }
                   });
    	
    	return dialog;
    }

}