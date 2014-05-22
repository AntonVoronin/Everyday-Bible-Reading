package ru.itprospect.everydaybiblereading;

import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

public class BibleActivity extends ActionBarActivity {
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bible_activity_layout);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        
        Uri uri = getIntent().getData();

        String book = uri.getQueryParameter("book");
        String chapter = uri.getQueryParameter("chapter");
        String stih = uri.getQueryParameter("stih");
        
        
        
    }
}

