package ru.itprospect.everydaybiblereading;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class TextFragment extends Fragment {
	private String book;
	private String chapter;
	private String stih;
	
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
        Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.text_fragment_layout, container, false);
    }
    
    
    public void setBook(String book, String chapter, String stih) {
    	this.book = book;
    	this.chapter = chapter;
    	this.stih = stih;
    }
}

