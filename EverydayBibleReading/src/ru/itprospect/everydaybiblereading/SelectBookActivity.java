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
	private String tab_selected = "book";

	private BQ bq;
	private BookBQ selectedBookBQ;
	
	private final String TAG_F_BOOK = "f_book";
	private final String TAG_F_CHAPTER = "f_chapter";
	private final String TAG_TAB_CHAPTER = "chapter";
	private final String TAG_TAB_BOOK = "book";

	//TODO Запоминать книгу при повороте экрана
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.select_book_activity_layout);
		ActionBar bar = getSupportActionBar(); 
		bar.setDisplayHomeAsUpEnabled(true);
		
		Log.e("EBR", "SelectBookAct: onCreate");

		//восстановление состояния активности
		boolean fromIntent = false;
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			tab_selected = extras.getString("TYPE");
			book = extras.getString("BOOK");
			if (tab_selected != null) {
				fromIntent = true;
				getIntent().removeExtra("TYPE");
				Log.e("EBR", "SelectBookAct: from intent");
			}
		}
		if (savedInstanceState != null && !fromIntent) {
			book = savedInstanceState.getString("BOOK");
			tab_selected = savedInstanceState.getString("TYPE");
			Log.e("EBR", "SelectBookAct: from saved state");
		}
		if (tab_selected == null) {
			tab_selected = TAG_TAB_BOOK;
		}
		//getIntent().setAction("");
		
		bar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		boolean tabBookSelected;
		if (tab_selected.equals(TAG_TAB_CHAPTER)) {
			tabBookSelected = false;
		}
		else {
			tabBookSelected = true;
		}
		
		Tab tab = bar.newTab();
		tab.setText(getBaseContext().getString(R.string.tab_book));
		tab.setTabListener(this);
		tab.setTag(TAG_TAB_BOOK);
		bar.addTab(tab, tabBookSelected);

		tab = bar.newTab();
		tab.setText(getBaseContext().getString(R.string.tab_chapter));
		tab.setTabListener(this);
		tab.setTag(TAG_TAB_CHAPTER);
		bar.addTab(tab, !tabBookSelected);
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		outState.putString("BOOK", book);
		outState.putString("TYPE", tab_selected);
		Log.e("EBR", "SelectBookAct: onSaveInstanceState");
		super.onSaveInstanceState(outState);
	}
	
	@Override
	protected void onDestroy() {
		Log.e("EBR", "SelectBookAct: onDestroy");
		super.onDestroy();
	}
	
	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) {
		String tag = (String) tab.getTag();
		
		FragmentManager fm = getSupportFragmentManager();
		SelectBookFragment selectBookFragment = (SelectBookFragment) fm.findFragmentByTag(TAG_F_BOOK);
		SelectChapterFragment selectChapterFragment = (SelectChapterFragment) fm.findFragmentByTag(TAG_F_CHAPTER);
		
		if (selectBookFragment == null) {
			selectBookFragment = new SelectBookFragment();
			ft.add(R.id.frgmCont, selectBookFragment, TAG_F_BOOK);
		}
		if (selectChapterFragment == null) {
			selectChapterFragment = new SelectChapterFragment();
			ft.add(R.id.frgmCont, selectChapterFragment, TAG_F_CHAPTER);
		}
		
		if (tag.equals(TAG_TAB_BOOK)) {
			ft.hide(selectChapterFragment);
			ft.show(selectBookFragment);
			setTitle(getBaseContext().getString(R.string.books));
			tab_selected = TAG_TAB_BOOK;
		}
		else {
			ft.hide(selectBookFragment);
			ft.detach(selectChapterFragment);
			ft.attach(selectChapterFragment);
			ft.show(selectChapterFragment);
			
			setTitle(getSelectedBookBQ().fullName);
			tab_selected = TAG_TAB_CHAPTER;
		}
		
	}
	
	public String[] GetArrayBookName() {
    	return getBQ().GetArrayBookName();
	}
	
	public ArrayList<BookBQ> GetArrayBook() {
    	return getBQ().GetArrayBook();
	}
	
	public BQ getBQ() {
		if (bq==null) {
    		bq = new BQ(getApplicationContext());
    	};		
		return bq;
	}
	
	public BookBQ getSelectedBookBQ() {
		if (selectedBookBQ == null) {
			if (book != null) {
				selectedBookBQ = getBQ().GetBookByShortName(book);
			}
			else {
				selectedBookBQ = GetArrayBook().get(0);
			}
		}
		return selectedBookBQ;
	}
	
	
	public void BookSelected(BookBQ selectedBookBQ) {
		book = selectedBookBQ.key;
		tab_selected = TAG_TAB_CHAPTER;
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

	@Override
	public void onTabReselected(Tab arg0, FragmentTransaction arg1) {
		
		
	}

	@Override
	public void onTabUnselected(Tab arg0, FragmentTransaction arg1) {
		
		
	}

}
