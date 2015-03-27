package ru.itprospect.everydaybiblereading;

import java.util.Calendar;
import java.util.GregorianCalendar;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.ShareActionProvider;
import android.text.Html;
import android.text.Spanned;
import android.text.format.DateFormat;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.Button;
import android.widget.TextView;

public class MainFragment extends Fragment {
	private BQ bq;
	private PrefManager prefManager;
	private TextView mTextBible;
	private ZoomingScrollView mScrollView;
	private ShareActionProvider mShareActionProvider;
	private int firstVisableCharacterOffset;
	private boolean AlreadyPositionSet = true;
	private int mYear;
	private int mMonth;
	private int mDay;
	private String pickDateText;
	private String textBibleText;
	private MenuItem mSetDateMenuItem;
	private Button actionBtnSetDate;
	
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
    	Log.e("EBR", "m fragment: onCreateView");
    	View layoutFragment = inflater.inflate(R.layout.main_fragment_layout, container, false);
        
		mScrollView = (ZoomingScrollView)layoutFragment.findViewById(R.id.zoomScroll);
		mTextBible = (TextView)layoutFragment.findViewById(R.id.textBible);
		mScrollView.setTextView(mTextBible);
		
		mTextBible.setLinksClickable(true);
		mTextBible.setMovementMethod(new LinkMovementMethod());
		
		updateFromPreferences();
		
		ViewTreeObserver vto = mTextBible.getViewTreeObserver();
		vto.addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
			@Override
			public void onGlobalLayout() {
				android.text.Layout textBibleLayout = mTextBible.getLayout();
				if (textBibleLayout != null) {
					final int firstVisableLineOffset = textBibleLayout.getLineForOffset(firstVisableCharacterOffset);
					//Log.e("EBR", "firstVisableCharacterOffset = " + String.valueOf(firstVisableCharacterOffset));
					int pixelOffset = textBibleLayout.getLineTop(firstVisableLineOffset);
					Log.e("EBR", "m vto, layout создан, позици€ обновлена pixelOffset = " + String.valueOf(pixelOffset));
					SetPositionForTextElement(pixelOffset, 5);
				}  
			}
		});
		
		return layoutFragment; 
    }
    
    
    @Override
	public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	
    	Log.e("EBR", "m fragment: onCreate");
    	setHasOptionsMenu(true);
    	setRetainInstance(true);
    	
		GregorianCalendar c = new GregorianCalendar();
		mYear = c.get(Calendar.YEAR);
		mMonth = c.get(Calendar.MONTH);
		mDay = c.get(Calendar.DAY_OF_MONTH);
    	UpdateText();
  
	}
    
	public void UpdateText() {
		Log.e("EBR", "m fragment: UpdateText");
		
		if (prefManager==null) {
			prefManager = new PrefManager(getActivity().getApplicationContext());
		}
		String confession = prefManager.getConfession();
		String confessionName = prefManager.getConfessionName();
		
		if (bq==null) {
			bq = new BQ(getActivity().getBaseContext());
		};
		
		GregorianCalendar date = new GregorianCalendar(mYear, mMonth, mDay);
		pickDateText = DateFormat.format("dd.MM.yyyy", date).toString();
		
		CalSAXParser pars = new CalSAXParser(getActivity().getBaseContext(), date, confession);
		if (pars.FindSuccess()) {
			textBibleText = bq.GetTextForArrayWithHead(pars.GetListBook(), confessionName, date);
		}
		else {
			textBibleText = pars.GetErrorText();
		}
		
		SetTextForElements();
		
		firstVisableCharacterOffset = 0;
		
		setShareIntent();
    }
	
	public void UpdateText(int year, int monthOfYear, int dayOfMonth) {
		mYear = year;
		mMonth = monthOfYear;
		mDay = dayOfMonth;
		UpdateText();
	}
    
	private void SetTextForElements() {
		if (textBibleText != null && mTextBible != null) {
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
	}
    
	public void updateFromPreferences() {
		if (prefManager==null) {
			prefManager = new PrefManager(getActivity().getApplicationContext());
		}
		int textSize = prefManager.getTextSize();
		mScrollView.setTextSize(textSize);
		
		colorFromPref();
	}
	
	private void colorFromPref() {
		if (prefManager==null) {
			prefManager = new PrefManager(getActivity().getApplicationContext());
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
		String shareBody = "";
		if (textBibleText != null) {
			shareBody = Html.fromHtml(textBibleText).toString();
		}
		shareBody = shareBody.replaceAll("\n\n", "\n");
		
		PrefManager pm = new PrefManager(getActivity().getApplicationContext());
		if (pm.getTranslit()) {
			shareBody = Translit.toTranslit(shareBody);
		}
		sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, getActivity().getApplicationContext().getString(R.string.app_name));
		sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
		
		if (mShareActionProvider != null) {
	        mShareActionProvider.setShareIntent(sharingIntent);
	    }
	}


	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.main, menu);
		
		//»щем пункт меню установки даты
		mSetDateMenuItem = menu.findItem(R.id.set_date);
		View dateView = MenuItemCompat.getActionView(mSetDateMenuItem);
		actionBtnSetDate = (Button) dateView.findViewById(R.id.action_buttton_set_date);
		actionBtnSetDate.setText(pickDateText);
		actionBtnSetDate.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				((MainActivity) getActivity()).ShowDialogFragmentDatePick(mYear, mMonth, mDay);
			}
		});
		//TODO надпись на кнопке съезжает вниз...
		
		//Set up ShareActionProvider's default share intent
	    MenuItem shareItem = menu.findItem(R.id.menu_item_share);
	    mShareActionProvider = (ShareActionProvider)MenuItemCompat.getActionProvider(shareItem);
	    setShareIntent();
		
		super.onCreateOptionsMenu(menu, inflater);
	}
	
	public boolean onOptionsItemSelected(MenuItem item) {
		super.onOptionsItemSelected(item);
		MainActivity ma;
		switch (item.getItemId()) {
		case (R.id.action_settings):
			ma = (MainActivity) getActivity();
			ma.ShowPreferences();
			return true;
		case (R.id.menu_item_today):
			GregorianCalendar c = new GregorianCalendar();
			mYear = c.get(Calendar.YEAR);
			mMonth = c.get(Calendar.MONTH);
			mDay = c.get(Calendar.DAY_OF_MONTH);
			UpdateText();
			return true;
		case (R.id.about):
			ma = (MainActivity) getActivity();
			ma.ShowAbout();
			return true;
		case (R.id.day_night):
			PrefManager prefManager = new PrefManager(getActivity().getApplicationContext());
			Boolean currentIsNight = prefManager.isNight();
			prefManager.putIsNight(!currentIsNight);
			colorFromPref();
			return true;
		case (R.id.set_confession):
//			Dialog dialogConfession = dialogConfession();
			ma = (MainActivity) getActivity();
			ma.ShowConfession();
			return true;
		case (R.id.feed_back):
			String appPackageName= getActivity().getPackageName();
			Intent marketIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id="+appPackageName));
			marketIntent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY|Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET|Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
			startActivity(marketIntent);
			return true;	
		}
		
		return false;
		
	}
	
	@Override
	public void onPause() {
		Log.e("EBR", "m fragment: onPause");

		if (AlreadyPositionSet) {
			final int firstVisableLineOffset = mTextBible.getLayout().getLineForVertical(mScrollView.getScrollY());
			firstVisableCharacterOffset = mTextBible.getLayout().getLineStart(firstVisableLineOffset);
			Log.e("EBR", "m firstVisableCharacterOffset onPause, посчитали позицию заново");
		}
		
		Log.e("EBR", "m firstVisableCharacterOffset onPause = " + String.valueOf(firstVisableCharacterOffset));
		super.onPause();
	}
	
	@Override
	public void onResume() {
		super.onResume();
		
		Log.e("EBR", "m fragment: onResume");
		Spanned s = Html.fromHtml(textBibleText);
		mTextBible.setText(s);
		
		AlreadyPositionSet = false;
		
	}
	
	public void SetPositionForTextElement(final int pixelOffset, int suspend) {
		mScrollView.postDelayed(new Runnable() {
	        public void run() {
	        	try {
	        		mScrollView.scrollTo(0, pixelOffset);
	        	}
	        	catch (Exception e) {}
	        	AlreadyPositionSet = true;
	        }
	    }, suspend);
		
	}
	
}
