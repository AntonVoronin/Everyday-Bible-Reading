package ru.itprospect.everydaybiblereading;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.Toast;

public class SelectChapterFragment extends Fragment {

	private SelectBookActivity selectBookActivity;
	private BookBQ selectedBookBQ;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View layoutFragment = inflater.inflate(R.layout.select_chapter_fragment_layout, container, false);

		TableLayout tableLayout = (TableLayout) layoutFragment.findViewById(R.id.chapterTable); 

		//Определяем количество глав книги
		selectBookActivity = (SelectBookActivity) getActivity();
		selectedBookBQ = selectBookActivity.getSelectedBookBQ();

		if (selectedBookBQ == null) {
			//берем первую книгу в списке
			selectedBookBQ = selectBookActivity.GetArrayBook().get(0);
		}
		int chapterQty;
		chapterQty = selectedBookBQ.chapterQty;

		//на экране должно быть минимум 5 столбцов. Если ширина экрана большая, то можно добавить столбцы
		DisplayMetrics displaymetrics = new DisplayMetrics();
		getActivity().getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
		int screenWidth = displaymetrics.widthPixels;
		int screenHeight = displaymetrics.heightPixels;
		//Toast.makeText(getActivity().getApplicationContext(), "screenWidth " + String.valueOf(screenWidth), Toast.LENGTH_SHORT).show(); 
		//вычисляем количество столбцов и строк
		int columnQty = 5;
		double dRowQty = Math.ceil((double)chapterQty / columnQty);
		int rowQty = (int) dRowQty;

		//LinearLayout.LayoutParams lButtonParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,  LinearLayout.LayoutParams.WRAP_CONTENT);
		OnClickListener oclBtn = new OnClickListener() {
			@Override
			public void onClick(View v) {
				Button b = (Button) v;
				String btnTxt = (String) b.getText(); 
				//Toast.makeText(getActivity(), selectedBookBQ.key + ", глава " + btnTxt, Toast.LENGTH_SHORT).show();
				
				selectBookActivity.ChapterSelected(selectedBookBQ.key, btnTxt);
			}
		};

		int a = 1;
		for (int iRow = 0; iRow < rowQty; iRow++) {
			TableRow tableRow = new TableRow(getActivity().getApplicationContext());

			for (int iCol = 0; iCol < columnQty; iCol++) { 
				if (a <= chapterQty) {

					Button btn =  new Button(getActivity().getApplicationContext());
					btn.setText(String.valueOf(a));
					//btn.setLayoutParams(lButtonParams);
					//btn.setId(i);
					btn.setOnClickListener(oclBtn);
					tableRow.addView(btn);       
				}
				a++;
			}

			tableLayout.addView(tableRow);
		}

		return layoutFragment; 
	}


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);



	}


}

