package ru.itprospect.everydaybiblereading;

import java.lang.reflect.Field;
import java.util.Calendar;
import java.util.GregorianCalendar;

import android.net.Uri;
import android.os.Bundle;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.text.Spanned;
import android.text.format.DateFormat;
import android.text.method.LinkMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.ShareActionProvider;

public class MainActivity extends ActionBarActivity {

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
    
	


	
	
	

}
