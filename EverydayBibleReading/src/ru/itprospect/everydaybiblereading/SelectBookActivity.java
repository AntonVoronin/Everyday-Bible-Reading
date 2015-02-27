package ru.itprospect.everydaybiblereading;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBar.Tab;
import android.support.v7.app.ActionBar.TabListener;
import android.support.v7.app.ActionBarActivity;


public class SelectBookActivity extends ActionBarActivity implements TabListener  {

	private String book = "";
	private String type = "";

	private BQ bq;
	private BookBQ selectedBookBQ;

	//TODO Запоминать книгу при повороте экрана
	
	
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
		String tag = (String) tab.getTag();
		
		FragmentManager fm = getSupportFragmentManager();
		SelectBookFragment selectBookFragment = (SelectBookFragment) fm.findFragmentByTag("f_book");
		SelectChapterFragment selectChapterFragment = (SelectChapterFragment) fm.findFragmentByTag("f_chapter");
		
		if (selectBookFragment == null) {
			selectBookFragment = new SelectBookFragment();
			ft.add(R.id.frgmCont, selectBookFragment, "f_book");
		}
		if (selectChapterFragment == null) {
			selectChapterFragment = new SelectChapterFragment();
			ft.add(R.id.frgmCont, selectChapterFragment, "f_chapter");
		}
		
		if (tag.equals("book")) {
			ft.hide(selectChapterFragment);
			ft.show(selectBookFragment);
			setTitle(getBaseContext().getString(R.string.books));
		}
		else {
			ft.hide(selectBookFragment);
			ft.detach(selectChapterFragment);
			ft.attach(selectChapterFragment);
			ft.show(selectChapterFragment);
			
			setTitle(getSelectedBookBQ().fullName);

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
		if (selectedBookBQ == null) {
			selectedBookBQ = GetArrayBook().get(0);
		}
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
