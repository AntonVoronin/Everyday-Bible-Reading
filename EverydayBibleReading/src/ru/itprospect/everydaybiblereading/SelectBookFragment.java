package ru.itprospect.everydaybiblereading;

import java.util.ArrayList;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

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
