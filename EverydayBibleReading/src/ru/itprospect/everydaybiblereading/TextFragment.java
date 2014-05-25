package ru.itprospect.everydaybiblereading;

import java.util.Calendar;
import java.util.GregorianCalendar;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

public class TextFragment extends Fragment {
	private String book;
	private String chapter;
	private String stih;
	private BQ bq;
	private PrefManager prefManager;
	
	private TextView mTextBible;
	private ZoomingScrollView mScrollView;
	
	
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
        Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View layoutFragment = inflater.inflate(R.layout.text_fragment_layout, container, false);
        
		mScrollView = (ZoomingScrollView)layoutFragment.findViewById(R.id.zoomScroll);
		mTextBible = (TextView)layoutFragment.findViewById(R.id.textBible);
		mScrollView.setTextView(mTextBible);
		
		mTextBible.setLinksClickable(true);
		mTextBible.setMovementMethod(new LinkMovementMethod());
		
		updateFromPreferences();
		
		return layoutFragment; 
    }
    
    
    public void setBook(String book, String chapter, String stih) {
    	this.book = book;
    	this.chapter = chapter;
    	this.stih = stih;
    	
    	UpdateText();
    }
    
    private void UpdateText() {
    	if (bq==null) {
    		bq = new BQ(getActivity().getApplicationContext());
    	};
    	String chapterText = bq.GetTextForChapter(book, chapter);
    	
		Spanned s = Html.fromHtml(chapterText);
		mTextBible.setText(s);
    }
    
	private void updateFromPreferences() {
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


	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.bible_fragment_menu, menu);
		
		super.onCreateOptionsMenu(menu, inflater);
	}
	
	public boolean onOptionsItemSelected(MenuItem item) {
		super.onOptionsItemSelected(item);
		switch (item.getItemId()) {

//			case (R.id.action_settings):
//				Intent i = new Intent(this, SettingsActivity.class);
//				startActivityForResult(i, SHOW_PREFERENCES);
//				return true;
			case (R.id.about):
				Dialog d = new Dialog(getActivity().getApplicationContext());
				d.setCanceledOnTouchOutside(true);
				d.requestWindowFeature(Window.FEATURE_NO_TITLE);
				d.setContentView(R.layout.about_layout);
				d.show();
				return true;
			case (R.id.day_night):
				PrefManager prefManager = new PrefManager(getActivity().getApplicationContext());
				Boolean currentIsNight = prefManager.isNight();
				prefManager.putIsNight(!currentIsNight);
				colorFromPref();
				return true;
		}
		
		// ¬ерните false, если вы не обработали это событие.
		return false;
		
	}
	
    
}

