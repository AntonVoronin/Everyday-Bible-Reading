package ru.itprospect.everydaybiblereading;

import java.util.Calendar;
import java.util.GregorianCalendar;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.view.Window;
import android.widget.DatePicker;

public class MainActivity extends ActionBarActivity implements DatePickerDialog.OnDateSetListener, DialogInterface.OnClickListener{

	private PrefManager prefManager;
	private static final int SHOW_PREFERENCES = 1;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
//	    try {
//	        ViewConfiguration config = ViewConfiguration.get(this);
//	        Field menuKeyField = ViewConfiguration.class.getDeclaredField("sHasPermanentMenuKey");
//	        if(menuKeyField != null) {
//	            menuKeyField.setAccessible(true);
//	            menuKeyField.setBoolean(config, false);
//	        }
//	    } catch (Exception ex) {
//	    	ex.printStackTrace();
//	    }
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_activity_layout_f);
		
		getSupportActionBar().setDisplayShowHomeEnabled(false);
		
		updateActFromPreferences();
		
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		
		if (requestCode == SHOW_PREFERENCES) {
			updateActFromPreferences();
			MainFragment mainFragment = (MainFragment) getSupportFragmentManager().findFragmentById(R.id.main_fragment);
			if (mainFragment != null) {
				mainFragment.UpdateText();
				mainFragment.updateFromPreferences();
				sendBroadcast(new Intent(WidgetActivity.WIDGET_FORCE_UPDATE));
			}
		}
	}


	private void updateActFromPreferences() {
		if (prefManager==null) {
			prefManager = new PrefManager(getApplicationContext());
		}
		
		int actBarBacId = prefManager.getColorSchemeActionBarId();
		Drawable d=getResources().getDrawable(actBarBacId);  
		getSupportActionBar().setBackgroundDrawable(d);
				
	}
	
    public void ShowPreferences() {
		Intent i = new Intent(this, SettingsActivity.class);
		startActivityForResult(i, SHOW_PREFERENCES);
    }
	
    public void ShowAbout() {
		Dialog d = new Dialog(this);
		d.setCanceledOnTouchOutside(true);
		d.requestWindowFeature(Window.FEATURE_NO_TITLE);
		d.setContentView(R.layout.about_layout);
		d.show();
    }
    
    public void ShowConfession() {
    	Dialog dialogConfession = dialogConfession();
    	dialogConfession.show();
    }

	private Dialog dialogConfession() {
		final String[] confArray = getResources().getStringArray(R.array.confession_name);
		final String[] confArrayValue = getResources().getStringArray(R.array.confession_value);
		if (prefManager==null) {
			prefManager = new PrefManager(getApplicationContext());
		}
		String oldValue = prefManager.getConfession();
		int oldValueIndex = -1;
		for (int i=0; i<confArrayValue.length; i++) {
			if (oldValue.equals(confArrayValue[i])) {
				oldValueIndex = i;
			}
		}
		
		
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(getApplicationContext().getString(R.string.settings_confession))
		.setCancelable(true)

		// добавляем одну кнопку для закрытия диалога
		.setNegativeButton(getApplicationContext().getString(R.string.cancel),
				new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog,
					int id) {
				dialog.cancel();
			}
		})

		// добавляем переключатели
		.setSingleChoiceItems(confArray, oldValueIndex,
				new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int item) {
				prefManager.putConfession(confArrayValue[item]);
				MainFragment mainFragment = (MainFragment) getSupportFragmentManager().findFragmentById(R.id.main_fragment);
				if (mainFragment != null) {
					mainFragment.UpdateText();
				}
				sendBroadcast(new Intent(WidgetActivity.WIDGET_FORCE_UPDATE));
				dialog.cancel();
			}
		});
		return builder.create();
	}
    
    public void ShowDialogFragmentDatePick(int year, int monthOfYear, int dayOfMonth) {
    	FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
    	DatePickerFragment datePickFragment = new DatePickerFragment();
    	datePickFragment.setDate(year, monthOfYear, dayOfMonth);
    	datePickFragment.show(ft, "datePicker");
    }
    
	@Override
	public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
		MainFragment mainFragment = (MainFragment) getSupportFragmentManager().findFragmentById(R.id.main_fragment);
		if (mainFragment != null) {
			mainFragment.UpdateText(year, monthOfYear, dayOfMonth);
		}
	}

	@Override
	public void onClick(DialogInterface dialog, int which) {
		MainFragment mainFragment = (MainFragment) getSupportFragmentManager().findFragmentById(R.id.main_fragment);
		if (mainFragment != null) {
			GregorianCalendar c = new GregorianCalendar();
			mainFragment.UpdateText(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
		}
	}

}
