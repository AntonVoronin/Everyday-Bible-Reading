package ru.itprospect.everydaybiblereading;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.ShareActionProvider;
import android.text.Html;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.Window;
import android.widget.TextView;

public class TextFragment extends Fragment {
	private String book = "";
	private String chapter = "";
	private String chapterText = "";
	private BQ bq;
	private PrefManager prefManager;
	private TextView mTextBible;
	private String mNameOfBook;
	private ZoomingScrollView mScrollView;
	private ShareActionProvider mShareActionProvider;
	private MenuItem chapterMenuItem;
	private int firstVisableCharacterOffset;
	private boolean AlreadyPositionSet = true;

	public boolean uriAlreadyGet = false;

	
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
    	Log.e("EBR", "fragment: onCreateView");
    	View layoutFragment = inflater.inflate(R.layout.text_fragment_layout, container, false);
        
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
					Log.e("EBR", "vto, layout создан, позици€ обновлена pixelOffset = " + String.valueOf(pixelOffset));
					SetPositionForTextElement(pixelOffset, 5);
				}  
			}
		});
		
		return layoutFragment; 
    }
    
    
    @Override
	public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	
    	setHasOptionsMenu(true);
    	setRetainInstance(true);
    	Log.e("EBR", "fragment: onCreate");
  
	}
    
	public void UpdateText() {
		Log.e("EBR", "fragment: UpdateText");
		
    	MainApp app = ((MainApp) getActivity().getApplicationContext());
    	book = app.getBook();
    	chapter = app.getChapter();
		
    	if (bq==null) {
    		bq = new BQ(getActivity().getApplicationContext());
    	};
    	chapterText = bq.GetTextForChapterWithHead(book, chapter);
    	
		Spanned s = Html.fromHtml(chapterText);
		mTextBible.setText(s);
		//set app label
		mNameOfBook = bq.GetNameForBook(book, "");
		getActivity().setTitle(mNameOfBook);
		if (chapterMenuItem != null) chapterMenuItem.setTitle(chapter);
		
		firstVisableCharacterOffset = 0;
		
		setShareIntent();
    }
    
    private void PrevChapter() {
    	int newChapter = Integer.parseInt(chapter) - 1;
    	if (newChapter>0) {
    		chapter = String.valueOf(newChapter);
    		MainApp app = ((MainApp) getActivity().getApplicationContext());
    		app.setChapter(chapter);
    		UpdateText();
    	}
    }

    private void NextChapter() {
    	if (bq==null) {
    		bq = new BQ(getActivity().getApplicationContext());
    	};
    	int chapterQty = bq.GetChapterQty(book);
    	
    	int newChapter = Integer.parseInt(chapter) + 1;
    	if (newChapter <= chapterQty) {
    		chapter = String.valueOf(newChapter);
    		MainApp app = ((MainApp) getActivity().getApplicationContext());
    		app.setChapter(chapter);
    		UpdateText();
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
		if (chapterText != null) {
			shareBody = Html.fromHtml(chapterText).toString();
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
		inflater.inflate(R.menu.bible_fragment_menu, menu);
		
		//Set up ShareActionProvider's default share intent
	    MenuItem shareItem = menu.findItem(R.id.menu_item_share);
	    mShareActionProvider = (ShareActionProvider)MenuItemCompat.getActionProvider(shareItem);
	    setShareIntent();
	    
	    //»щем пункт меню выбора главы
	    chapterMenuItem = menu.findItem(R.id.select_chapter);
	    chapterMenuItem.setTitle(chapter);
	    //chapterMenuItem.getActionView();
	    //TODO сделать шрифт больше
		
		super.onCreateOptionsMenu(menu, inflater);
	}
	
	public boolean onOptionsItemSelected(MenuItem item) {
		super.onOptionsItemSelected(item);
		BibleActivity ba;
		switch (item.getItemId()) {
			case (R.id.action_settings):
				ba = (BibleActivity) getActivity();
				ba.ShowPreferences();
				return true;
			case (R.id.about):
				Dialog d = new Dialog(getActivity());
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
			case (R.id.feed_back):
				String appPackageName= getActivity().getPackageName();
				Intent marketIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id="+appPackageName));
				marketIntent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY|Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET|Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
				startActivity(marketIntent);
				return true;
			case (R.id.chapter_left):
				PrevChapter(); 
				return true;
			case (R.id.chapter_right):
				NextChapter(); 
				return true;
			case (R.id.select_book):
				ba = (BibleActivity) getActivity();
				ba.SelectBook(book, "book"); 
				return true;
			case (R.id.select_chapter):
				ba = (BibleActivity) getActivity();
				ba.SelectBook(book, "chapter"); 
				return true;
		}
		
		return false;
		
	}
	
	@Override
	public void onPause() {
		Log.e("EBR", "fragment: onPause");

		if (AlreadyPositionSet) {
			final int firstVisableLineOffset = mTextBible.getLayout().getLineForVertical(mScrollView.getScrollY());
			firstVisableCharacterOffset = mTextBible.getLayout().getLineStart(firstVisableLineOffset);
			Log.e("EBR", "firstVisableCharacterOffset onPause, посчитали позицию заново");
		}
		Log.e("EBR", "firstVisableCharacterOffset onPause = " + String.valueOf(firstVisableCharacterOffset));
		super.onPause();
	}
	
	@Override
	public void onResume() {
		super.onResume();
		
		Log.e("EBR", "fragment: onResume");
		Spanned s = Html.fromHtml(chapterText);
		mTextBible.setText(s);
		getActivity().setTitle(mNameOfBook);
		
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

