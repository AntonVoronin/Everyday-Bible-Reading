package ru.itprospect.everydaybiblereading;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

public class BibleActivity extends ActionBarActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bible_activity_layout);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        
    }
}

