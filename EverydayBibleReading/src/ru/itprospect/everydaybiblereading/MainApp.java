package ru.itprospect.everydaybiblereading;

import android.app.Application;

public class MainApp extends Application {
	
	private String book;
	private String chapter;
	public String getBook() {
		return book;
	}
	public void setBook(String book) {
		this.book = book;
	}
	public String getChapter() {
		return chapter;
	}
	public void setChapter(String chapter) {
		this.chapter = chapter;
	}

}
