package ru.itprospect.everydaybiblereading;

import java.util.ArrayList;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
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
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class SelectBookFragment extends ListFragment {

	private String[] arrayBookName;
	private ArrayList<BookBQ> arrayListBook;
		
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		SelectBookActivity selectBookActivity;
		selectBookActivity = (SelectBookActivity) getActivity();
		arrayBookName = selectBookActivity.GetArrayBookName();
		arrayListBook = selectBookActivity.GetArrayBook();

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
				android.R.layout.simple_list_item_1, arrayBookName);
		setListAdapter(adapter);
		
	}

	public void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		//Toast.makeText(getActivity(), "position = " + position + ", bq = " + arrayListBook.get(position).toString(), Toast.LENGTH_SHORT).show();
		SelectBookActivity selectBookActivity = (SelectBookActivity) getActivity();
		selectBookActivity.BookSelected(arrayListBook.get(position));
	}

}
