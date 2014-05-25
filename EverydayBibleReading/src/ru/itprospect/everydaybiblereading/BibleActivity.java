package ru.itprospect.everydaybiblereading;

import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;

public class BibleActivity extends ActionBarActivity {
	
	private PrefManager prefManager;
	
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bible_activity_layout);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        
        Uri uri = getIntent().getData();

        String book = uri.getQueryParameter("book");
        String chapter = uri.getQueryParameter("chapter");
        String stih = uri.getQueryParameter("stih");
        
        updateFromPreferences();
        
        TextFragment textFragment = (TextFragment) getSupportFragmentManager().findFragmentById(R.id.text_fragment);
        textFragment.setBook(book, chapter, stih);
        
    }
    
	private void updateFromPreferences() {
		if (prefManager==null) {
			prefManager = new PrefManager(getApplicationContext());
		}
		
		int actBarBacId = prefManager.getColorSchemeActionBarId();
		Drawable d=getResources().getDrawable(actBarBacId);  
		getSupportActionBar().setBackgroundDrawable(d);
		
		
	}
    
}

