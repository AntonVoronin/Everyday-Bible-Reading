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
	private BQ bq;
	private int mYear;
	private int mMonth;
	private int mDay;
	private TextView mTextBible;
	private ZoomingScrollView mScrollView;
	private String pickDateText;
	private String textBibleText;
	private int firstVisableCharacterOffset = 0;
	private boolean mustScroll = false;
	private int pixelOffsetAfterResume = -1;
	private PrefManager prefManager;
	private ShareActionProvider mShareActionProvider;
	private MenuItem mSetDateMenuItem;
	private Button actionBtnSetDate;
	
	private static final String YEAR_STATE_KEY = "YEAR_STATE_KEY";
	private static final String MONTH_STATE_KEY = "MONTH_STATE_KEY";
	private static final String DAY_STATE_KEY = "DAY_STATE_KEY";
	private static final String PICK_DATE_STATE_KEY = "PICK_DATE_STATE_KEY";
	private static final String TEXT_BIBLE_STATE_KEY = "TEXT_BIBLE_STATE_KEY";
	private static final String SCROLL_POSITION_STATE_KEY = "SCROLL_POSITION_STATE_KEY";
	private static final int SHOW_PREFERENCES = 1;
	
	private DatePickerDialog.OnDateSetListener mDateSetListener =
	        new DatePickerDialog.OnDateSetListener() {
	            public void onDateSet(DatePicker view, int year, 
	                    int monthOfYear, int dayOfMonth) {
	                mYear = year;
	                mMonth = monthOfYear;
	                mDay = dayOfMonth;
	                UpdateText();
	                
	            }
	        }; 

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
	    try {
	        ViewConfiguration config = ViewConfiguration.get(this);
	        Field menuKeyField = ViewConfiguration.class.getDeclaredField("sHasPermanentMenuKey");
	        if(menuKeyField != null) {
	            menuKeyField.setAccessible(true);
	            menuKeyField.setBoolean(config, false);
	        }
	    } catch (Exception ex) {
	    	ex.printStackTrace();
	    }
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_layout);
		
		getSupportActionBar().setDisplayShowHomeEnabled(false);
		
		mScrollView = (ZoomingScrollView)findViewById(R.id.zoomScroll);
		mTextBible = (TextView)findViewById(R.id.textBible);
		mScrollView.setTextView(mTextBible);
		
		mTextBible.setLinksClickable(true);
		mTextBible.setMovementMethod(new LinkMovementMethod());
		
		//Восстанавливаем размер шрифта и др настройки
		updateFromPreferences();
		
		//Восстановление состояния
		GregorianCalendar c = new GregorianCalendar();
		//Log.e(TAG, "OnCreate");
		if (savedInstanceState != null) {
			mYear = savedInstanceState.getInt(YEAR_STATE_KEY, c.get(Calendar.YEAR));
			mMonth = savedInstanceState.getInt(MONTH_STATE_KEY, c.get(Calendar.MONTH));
			mDay = savedInstanceState.getInt(DAY_STATE_KEY, c.get(Calendar.DAY_OF_MONTH));
			pickDateText = savedInstanceState.getString(PICK_DATE_STATE_KEY);
			textBibleText = savedInstanceState.getString(TEXT_BIBLE_STATE_KEY);
			
			SetTextForElements();

		}
		else {
			mYear = c.get(Calendar.YEAR);
			mMonth = c.get(Calendar.MONTH);
			mDay = c.get(Calendar.DAY_OF_MONTH);
			
			UpdateText();
		}
		
		
		ViewTreeObserver vto = mTextBible.getViewTreeObserver();
	    vto.addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
	        @Override
	        public void onGlobalLayout() {
	        	if (mustScroll) {
	        		android.text.Layout textBibleLayout = mTextBible.getLayout();
	        		if (textBibleLayout != null) {
	        			final int firstVisableLineOffset = textBibleLayout.getLineForOffset(firstVisableCharacterOffset);
	        			//Log.e(TAG, "firstVisableCharacterOffset = " + String.valueOf(firstVisableCharacterOffset));
	        			int pixelOffset = textBibleLayout.getLineTop(firstVisableLineOffset);
	        			//Log.e(TAG, "vto, layout создан, позиция обновлена pixelOffset = " + String.valueOf(pixelOffset));
	        			SetPositionForTextElement(pixelOffset, 400);
	        		}  
	        	}
	        }
	    });
	    pixelOffsetAfterResume = -1;
		
	}
	
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		
		//Log.e(TAG, "onSaveInstanceState");
		
		//сохраняем позицию в переменную на случай, если activity будет снова активна
		pixelOffsetAfterResume = mScrollView.getScrollY();
		
		outState.putInt(YEAR_STATE_KEY, mYear);
		outState.putInt(MONTH_STATE_KEY, mMonth);
		outState.putInt(DAY_STATE_KEY, mDay);
		outState.putString(PICK_DATE_STATE_KEY, pickDateText);
		outState.putString(TEXT_BIBLE_STATE_KEY, textBibleText);
		
		final int firstVisableLineOffset = mTextBible.getLayout().getLineForVertical(mScrollView.getScrollY());
	    final int firstVisableCharacterOffset = mTextBible.getLayout().getLineStart(firstVisableLineOffset);
	    outState.putInt(SCROLL_POSITION_STATE_KEY, firstVisableCharacterOffset);
		
		super.onSaveInstanceState(outState);
	}
	
	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		
		if (savedInstanceState != null) {
			//Восстанавливаем позицию
			firstVisableCharacterOffset = savedInstanceState.getInt(SCROLL_POSITION_STATE_KEY);
			mustScroll = true;
			//Log.e(TAG, "onRestoreInstanceState - savedInstanceState != null");
		}
		else {
			mustScroll = false;
			//Log.e(TAG, "onRestoreInstanceState - savedInstanceState == null");
		}
		
		
	}
	
	
	
/*	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		super.onWindowFocusChanged(hasFocus);
		
		Log.e(TAG, "onWindowFocusChanged");
		if(mustScroll) {
			int pixelOffset = 0;
			try {
				android.text.Layout textBibleLayout = mTextBible.getLayout();
				if (textBibleLayout != null) {
					final int firstVisableLineOffset = textBibleLayout.getLineForOffset(firstVisableCharacterOffset);
					pixelOffset = textBibleLayout.getLineTop(firstVisableLineOffset);
				}
				Log.e(TAG, "onWindowFocusChanged - mustScroll, получили позицию");
	        }
	        catch (Exception e) {}
					
			//Устанавливаем позицию
			SetPositionForTextElement(pixelOffset);
	        
		}
	}*/

	@Override
	protected void onResume() {
		super.onResume();
		
		//Log.e(TAG, "onResume " + String.valueOf(pixelOffsetAfterResume));
		if(pixelOffsetAfterResume>0) {
			//Устанавливаем позицию
			SetPositionForTextElement(pixelOffsetAfterResume, 200);
			pixelOffsetAfterResume = -1;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		
		if (requestCode == SHOW_PREFERENCES) {
			updateFromPreferences();
			UpdateText();
			sendBroadcast(new Intent(WidgetActivity.WIDGET_FORCE_UPDATE));
		}
	}

//	@Override
//	protected Dialog onCreateDialog(int id) {
//	    switch (id) {
//	    case DATE_DIALOG_ID:
//	    	return new DatePickerDialog(this,
//	                mDateSetListener,
//	                mYear, mMonth, mDay);
//	    }
//	    return null;
//	}
	
	private void UpdateText() {
		//Log.e(TAG, "UpdateText");
		if (prefManager==null) {
			prefManager = new PrefManager(getApplicationContext());
		}
		String confession = prefManager.getConfession();
		String confessionName = prefManager.getConfessionName();
		
		if (bq==null) {
			bq = new BQ(getBaseContext());
		};
		
		GregorianCalendar date = new GregorianCalendar(mYear, mMonth, mDay);
		pickDateText = DateFormat.format("dd.MM.yyyy", date).toString();
		
		CalSAXParser pars = new CalSAXParser(getBaseContext(), date, confession);
		if (pars.FindSuccess()) {
			textBibleText = bq.GetTextForArrayWithHead(pars.GetListBook(), confessionName, date);
		}
		else {
			textBibleText = pars.GetErrorText();
		}
		
		SetTextForElements();
		
		//Устанавливаем позицию текста в начало
		firstVisableCharacterOffset = 0;
		mustScroll = true;
		
		setShareIntent();
	}
	
	private void SetTextForElements() {
		//Log.e(TAG, "SetTextForElements");
		
		Spanned s = Html.fromHtml(textBibleText);
		mTextBible.setText(s);
		
		try {
			if (actionBtnSetDate != null) {
				actionBtnSetDate.setText(pickDateText);
				}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void SetPositionForTextElement(final int pixelOffset, int suspend) {
		mScrollView.postDelayed(new Runnable() {
	        public void run() {
	        	try {
	        		//Log.e(TAG, "SetPositionForTextElement " + String.valueOf(pixelOffset));
	        		mScrollView.scrollTo(0, pixelOffset);
	        		//Log.e(TAG, "теперь текущая позиция " + String.valueOf(mScrollView.getScrollY()));
	        	}
	        	catch (Exception e) {}
	        	
	        }
	    }, suspend);
		mustScroll = false;
	}
	
	private void updateFromPreferences() {
		if (prefManager==null) {
			prefManager = new PrefManager(getApplicationContext());
		}
		int textSize = prefManager.getTextSize();
		mScrollView.setTextSize(textSize);
		
		colorFromPref();
		
		int actBarBacId = prefManager.getColorSchemeActionBarId();
		Drawable d=getResources().getDrawable(actBarBacId);  
		getSupportActionBar().setBackgroundDrawable(d);
				
	}
	
	private void colorFromPref() {
		if (prefManager==null) {
			prefManager = new PrefManager(getApplicationContext());
		}
		
		Boolean isNight = prefManager.isNight();
		
		if (isNight) {
			mTextBible.setTextColor(Color.WHITE);
			mTextBible.setBackgroundColor(Color.BLACK);
		}
		else {
			int textColour = prefManager.getTextColour();
			mTextBible.setTextColor(textColour);
			int backgroundColour = prefManager.getBackgroundColour();
			mTextBible.setBackgroundColor(backgroundColour);
		}
	}
	
	private void setShareIntent() {
		Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
		sharingIntent.setType("text/plain");
		
		String shareBody = Html.fromHtml(textBibleText).toString();
		shareBody = shareBody.replaceAll("\n\n", "\n");
		
		PrefManager pm = new PrefManager(getApplicationContext());
		if (pm.getTranslit()) {
			shareBody = Translit.toTranslit(shareBody);
		}
		sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, getApplicationContext().getString(R.string.app_name));
		sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
		
		if (mShareActionProvider != null) {
	        mShareActionProvider.setShareIntent(sharingIntent);
	    }
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		
		//Ищем пункт меню установки даты
		mSetDateMenuItem = menu.findItem(R.id.set_date);
		View dateView = MenuItemCompat.getActionView(mSetDateMenuItem);
		actionBtnSetDate = (Button) dateView.findViewById(R.id.action_buttton_set_date);
		actionBtnSetDate.setText(pickDateText);
		actionBtnSetDate.setOnClickListener(new View.OnClickListener() {
		    public void onClick(View v) {
//		        showDialog(DATE_DIALOG_ID);
		    	FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		    	DatePickerFragment newFragment = new DatePickerFragment();
		    	newFragment.setParam(mDateSetListener, mYear, mMonth, mDay);
		    	newFragment.show(ft, "datePicker");
		    }
		});
		
		// Set up ShareActionProvider's default share intent
	    MenuItem shareItem = menu.findItem(R.id.menu_item_share);
	    mShareActionProvider = (ShareActionProvider)MenuItemCompat.getActionProvider(shareItem);
	    setShareIntent();
		
		return true;
	}
	
	public boolean onOptionsItemSelected(MenuItem item) {
		super.onOptionsItemSelected(item);
		switch (item.getItemId()) {

			case (R.id.action_settings):
				Intent i = new Intent(this, SettingsActivity.class);
				startActivityForResult(i, SHOW_PREFERENCES);
				return true;
			case (R.id.menu_item_today):
				GregorianCalendar c = new GregorianCalendar();
				mYear = c.get(Calendar.YEAR);
				mMonth = c.get(Calendar.MONTH);
				mDay = c.get(Calendar.DAY_OF_MONTH);
				UpdateText();
				return true;
			case (R.id.about):
				Dialog d = new Dialog(MainActivity.this);
				d.setCanceledOnTouchOutside(true);
				d.requestWindowFeature(Window.FEATURE_NO_TITLE);
				d.setContentView(R.layout.about_layout);
				d.show();
				return true;
			case (R.id.day_night):
				PrefManager prefManager = new PrefManager(getApplicationContext());
				Boolean currentIsNight = prefManager.isNight();
				prefManager.putIsNight(!currentIsNight);
				colorFromPref();
				return true;
			case (R.id.set_confession):
				Dialog dialogConfession = dialogConfession();
				dialogConfession.show();
				return true;
			case (R.id.feed_back):
				String appPackageName= getPackageName();
				Intent marketIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id="+appPackageName));
				marketIntent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY|Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET|Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
				startActivity(marketIntent);
				return true;	
		}
		
		// Верните false, если вы не обработали это событие.
		return false;
		
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
				UpdateText();
				sendBroadcast(new Intent(WidgetActivity.WIDGET_FORCE_UPDATE));
				dialog.cancel();
			}
		});
		return builder.create();
	}


	
	
	

}
