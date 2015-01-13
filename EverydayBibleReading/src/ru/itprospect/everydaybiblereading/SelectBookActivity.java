package ru.itprospect.everydaybiblereading;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBar.Tab;
import android.support.v7.app.ActionBar.TabListener;
import android.support.v7.app.ActionBarActivity;


public class SelectBookActivity extends ActionBarActivity implements TabListener  {

	private String book = "";
	private String type = "";
	private SelectBookFragment selectBookFragment;
	private SelectChapterFragment selectChapterFragment;
	private BQ bq;
	private BookBQ selectedBookBQ;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.select_book_activity_layout);
		ActionBar bar = getSupportActionBar(); 
		bar.setDisplayHomeAsUpEnabled(true);

		Bundle extras = getIntent().getExtras();
		if (extras != null) {        		
			book = extras.getString("BOOK");
			type = extras.getString("TYPE");
		}

		bar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);


		selectBookFragment = new SelectBookFragment();
		selectChapterFragment = new SelectChapterFragment();

		
//		FragmentTransaction fTrans = getSupportFragmentManager().beginTransaction();
//		boolean tabBookSelected;
//		if (type.equals("chapter")) {
//			fTrans.add(R.id.frgmCont, selectChapterFragment);
//			tabBookSelected = false;
//		}
//		else {
//			fTrans.add(R.id.frgmCont, selectBookFragment);
//			tabBookSelected = true;
//		}
//		fTrans.commit();

		boolean tabBookSelected;
		if (type.equals("chapter")) {
			tabBookSelected = false;
		}
		else {
			tabBookSelected = true;
		}
		
		Tab tab = bar.newTab();
		tab.setText(getBaseContext().getString(R.string.tab_book));
		tab.setTabListener(this);
		tab.setTag("book");
		bar.addTab(tab, tabBookSelected);

		tab = bar.newTab();
		tab.setText(getBaseContext().getString(R.string.tab_chapter));
		tab.setTabListener(this);
		tab.setTag("chapter");
		bar.addTab(tab, !tabBookSelected);
	}

	@Override
	public void onTabReselected(Tab tab, FragmentTransaction ft) {

	}

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) {
		if (selectBookFragment !=null && selectChapterFragment !=null) {
			String tag = (String) tab.getTag();
			if (tag.equals("book")) {
				ft.replace(R.id.frgmCont, selectBookFragment);
			}
			else {
				ft.replace(R.id.frgmCont, selectChapterFragment);
			}
		}
	}

	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {

	}
	
	public String[] GetArrayBookName() {
		if (bq==null) {
    		bq = new BQ(getApplicationContext());
    	};
    	return bq.GetArrayBookName();
	}
	
	public ArrayList<BookBQ> GetArrayBook() {
		if (bq==null) {
    		bq = new BQ(getApplicationContext());
    	};
    	return bq.GetArrayBook();
	}
	
	public BQ getBQ() {
		if (bq==null) {
    		bq = new BQ(getApplicationContext());
    	};		
		return bq;
	}
	
	public BookBQ getSelectedBookBQ() {
		return selectedBookBQ;
	}
	
	
	public void BookSelected(BookBQ selectedBookBQ) {
		book = selectedBookBQ.key;
		this.selectedBookBQ = selectedBookBQ;
		getSupportActionBar().setSelectedNavigationItem(1);
	}
	
	public void ChapterSelected(String book, String chapter) {
		Intent intent = new Intent();
		intent.putExtra("book", book);
	    intent.putExtra("chapter", chapter);
	    setResult(RESULT_OK, intent);
	    finish();
	}

}
