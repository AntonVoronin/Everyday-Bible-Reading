package ru.itprospect.everydaybiblereading;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.StringTokenizer;

import android.content.Context;
import android.text.format.DateFormat;

public class BQ{

	private Context mCntx;
	private String chapterSign;
	private String verseSign;
	private int bookQty;
	private HashMap<String, BookBQ> bookMap;
	
	private final static String DIR_BQ_RST = "bq/RST/";
	private final static String INI_BQ_RST = "bibleqt.ini";
	private final static String TEG_CHAPTER_END = " />";
	
	public BQ(Context cntx) {
		mCntx = cntx;
		Initialization();
	}
	
	private void Initialization() {
		
		try {
			InputStream is = mCntx.getAssets().open(DIR_BQ_RST + INI_BQ_RST);
			InputStreamReader isr = new InputStreamReader(is, "windows-1251");
			BufferedReader in = new BufferedReader(isr);
			
			String line;
			Boolean isDone = false;
			while (!isDone && (line=in.readLine()) != null) {
				if (!(line.length()==0 || line.startsWith("//"))) {
					
					if (line.startsWith("ChapterSign =")) {
						chapterSign = line.substring(13).trim();
					}
					else if (line.startsWith("VerseSign =")) {
						verseSign = line.substring(11).trim();
					}
					else if (line.startsWith("BookQty =")) {
						bookQty = Integer.parseInt(line.substring(9).trim());
						isDone = true;
					}
				}
			}
			
			if (!isDone) {
				return; //Ошибка, дальше не идем
			}
			
			bookMap = new HashMap<String, BookBQ>();
			//Начинаем цикл по книгам
			for (int a = 0; a<bookQty; a++) {
				BookBQ bookBQ = new BookBQ();
				
				for (int b = 0; b<5; b++) {
					line=in.readLine();
					
					if (line.startsWith("PathName =")) {
						bookBQ.pathName = line.substring(10).trim();
					}
					else if (line.startsWith("FullName =")) {
						bookBQ.fullName = line.substring(10).trim();
					}
					else if (line.startsWith("ChapterQty =")) {
						bookBQ.chapterQty = Integer.parseInt(line.substring(12).trim());
					}
					else if (line.startsWith("ShortName =")) {
						//здесь нужно выделить все варианты краткого названия книги и добавить книгу в Map столько раз, сколько сокращений найдено
						String strShortName = line.substring(11).trim();
						StringTokenizer t = new StringTokenizer(strShortName);
						while (t.hasMoreTokens()) {
							String token = t.nextToken().toUpperCase(Locale.getDefault());
							bookMap.put(token, bookBQ);
						}
						
					}
				}
			}
			
			
			is.close();
			
		} catch (IOException e) {
			
			e.printStackTrace();
		}
		
	}
	
	public String GetNameForBook(BookFromSite book) {
		String bookFullName = book.fullText;
		String bookNameBezProbelov = book.book.replaceAll(" ", "").toUpperCase(Locale.getDefault());

		BookBQ bookBQ = bookMap.get(bookNameBezProbelov);
		if (bookBQ != null) {
			bookFullName = bookBQ.fullName + " " + book.adress;
		}
		
		return bookFullName;
	} 

	public String GetNameForBook(String shortName, String chapter) {
		String bookNameBezProbelov = shortName.replaceAll(" ", "").toUpperCase(Locale.getDefault());
		
		BookBQ bookBQ = bookMap.get(bookNameBezProbelov);
		if (bookBQ != null) {
			return bookBQ.fullName + " " + chapter;
		}
		else {
			return shortName + " " + chapter;
		}
	}	
	
	public String GetTextForBook(BookFromSite book) {
		String str = "";
		StringBuilder msgStr = new StringBuilder();
		String signChapStart = "";
		String signVerseStart = "";
		String signChapEnd = "";
		String signVerseEnd = "";
		
		PrefManager pm = new PrefManager(mCntx);
		Boolean useColorFromBQ = pm.getUseColorFromBQ();
		
		//Убираем из краткого названия книги пробелы
		String bookNameBezProbelov = book.book.replaceAll(" ", "").toUpperCase(Locale.getDefault());
		
		BookBQ bookBQ = bookMap.get(bookNameBezProbelov);
		if (bookBQ != null) {
			try {
				InputStream is = mCntx.getAssets().open(DIR_BQ_RST + bookBQ.pathName);
				InputStreamReader isr = new InputStreamReader(is, "windows-1251");
				BufferedReader in = new BufferedReader(isr);

				String line;
				Boolean chapterStartFind = false;
				Boolean verseStartFind = false;
				Boolean chapterEndFind = false;
				Boolean verseEndFind = false;
				int prevChapterEnd = 0;
				int prevVerseEnd = 0;
				Boolean verseAlreadyAdd = false;
				
				int numOtr = 0;
				ReadStartEnd otr = book.otr[numOtr];
				Boolean isDone = (otr==null);
				if (!isDone) {
					//<chapter />1      <chapter />1|<chapter.+>1</
					//<chapter  NAME="glava1">1</chapter>
					//signChapStart = chapterSign + TEG_CHAPTER_END + String.valueOf(otr.chapterStart) + ;
					signChapStart = chapterSign + TEG_CHAPTER_END + String.valueOf(otr.chapterStart) + "|" + chapterSign + ".+>" + String.valueOf(otr.chapterStart) + "</.+";
					signVerseStart = verseSign + String.valueOf(otr.stihStart);
					signChapEnd = chapterSign + TEG_CHAPTER_END + String.valueOf(otr.chapterEnd) + "|" + chapterSign + ".+>" + String.valueOf(otr.chapterEnd) + "</.+";
					signVerseEnd = verseSign + String.valueOf(otr.stihEnd);
				}
				
				while (!isDone && (line=in.readLine()) != null) {
					//********Ищем начало********
					
					//Перед тем, как искать начало, проверим: если текущий отрывок начинается в той же главе,
					//в которой заканчивается предыдущий, то мы автоматически начало нашли
					if (otr.chapterStart == prevChapterEnd) {
						chapterStartFind = true;
						if (otr.chapterStart == otr.chapterEnd) { //Это и последняя глава отрывка тоже
							chapterEndFind = true;
						}
						if (otr.stihStart == prevVerseEnd) {
							verseStartFind = true;
							if (otr.stihStart == otr.stihEnd) { //Это и последний стих отрывка тоже
								verseEndFind = true;
								//значит нам вообще не нужно добавлять ничего, уже все добавлено в прошлом цикле
								verseAlreadyAdd = true;
							}
						}
					}
					
					if (!chapterStartFind) { //Начало главы еще не найдено
						if (line.matches(signChapStart)) { //Нашли начало главы  
							chapterStartFind = true;
							if (otr.chapterStart == otr.chapterEnd) { //Это и последняя глава отрывка тоже
								chapterEndFind = true;
							}
						}
					}
					else { //Начало главы найдено, ищем стих
						if (line.startsWith(signVerseStart)) { //нашли начальный стих
							verseStartFind = true;
							if (chapterEndFind && otr.stihStart == otr.stihEnd) { //это одновременно и последний стих отрывка
								verseEndFind = true;
							}
						} 
					}
					
					if (verseStartFind && !verseAlreadyAdd) { //если нашли начальный стих, то подбираем строку в любом случае
						msgStr.append(FromatChapterText(line, useColorFromBQ)); // + "\n"
					}
					
					//********Ищем конец********
					if (!chapterEndFind) { //конец главы еще не найден
						if (line.matches(signChapEnd)) { //Нашли конец главы
							chapterEndFind = true;
						}
					}
					else { //конец главы найден, ищем стих
						if (line.startsWith(signVerseEnd)) { //нашли конечный стих
							verseEndFind = true;
						} 
					}
					
					
					if (chapterEndFind && verseEndFind) {
						//Отрывок закончился. Переходим на следующий или завершаем цикл
						prevChapterEnd = otr.chapterEnd;
						prevVerseEnd = otr.stihEnd;
						
						numOtr++;
						otr = book.otr[numOtr];
						
						isDone = (otr==null);
						
						if (!isDone) {
							signChapStart = chapterSign + TEG_CHAPTER_END + String.valueOf(otr.chapterStart) + "|" + chapterSign + ".+>" + String.valueOf(otr.chapterStart) + "</.+";
							signVerseStart = verseSign + String.valueOf(otr.stihStart);
							signChapEnd = chapterSign + TEG_CHAPTER_END + String.valueOf(otr.chapterEnd) + "|" + chapterSign + ".+>" + String.valueOf(otr.chapterEnd) + "</.+";
							signVerseEnd = verseSign + String.valueOf(otr.stihEnd);
							chapterStartFind = false;
							verseStartFind = false;
							chapterEndFind = false;
							verseEndFind = false;
							verseAlreadyAdd = false;
							
							if (otr.chapterStart != prevChapterEnd || (otr.stihStart - prevVerseEnd)>1) msgStr.append("<p> ..."); // \n
						}
					}
				}
				is.close();
				str = msgStr.toString();
			} catch (IOException e) {
				str = mCntx.getString(R.string.err_file_book_not_open);
				e.printStackTrace();
			}
		}
		else {
			str = mCntx.getString(R.string.err_book_not_find);
		}
		
		return str;
	}
	
	/**
	 * Функция возвращает текст всей главы
	 * @param book название книги
	 * @param chapter номер главы
	 * @return тест главы
	 */
	public String GetTextForChapter(String book, String chapter) {
		StringBuilder strBuilder = new StringBuilder();
		String signChapStart = "";
		String signChapEnd = "";
		int chapterEnd = Integer.parseInt(chapter) + 1;
		String str = "";
		
		PrefManager pm = new PrefManager(mCntx);
		Boolean useColorFromBQ = pm.getUseColorFromBQ();
		
		//Убираем из краткого названия книги пробелы
		String bookNameBezProbelov = book.replaceAll(" ", "").toUpperCase(Locale.getDefault());
		
		BookBQ bookBQ = bookMap.get(bookNameBezProbelov);
		if (bookBQ != null) {
			try {
				InputStream is = mCntx.getAssets().open(DIR_BQ_RST + bookBQ.pathName);
				InputStreamReader isr = new InputStreamReader(is, "windows-1251");
				BufferedReader in = new BufferedReader(isr);

				String line;
				Boolean chapterStartFind = false;
				boolean isDone = false;
				
				//<chapter />1      <chapter />1|<chapter.+>1</
				//<chapter  NAME="glava1">1</chapter>
				//signChapStart = chapterSign + TEG_CHAPTER_END + String.valueOf(otr.chapterStart) + ;
				signChapStart = chapterSign + TEG_CHAPTER_END + chapter + "|" + chapterSign + ".+>" + chapter + "</.+";
				signChapEnd = chapterSign + TEG_CHAPTER_END + String.valueOf(chapterEnd) + "|" + chapterSign + ".+>" + String.valueOf(chapterEnd) + "</.+";
				
				
				while (!isDone && (line=in.readLine()) != null) {
					//********Ищем начало********
					if (!chapterStartFind) { //Начало главы еще не найдено
						if (line.matches(signChapStart)) { //Нашли начало главы  
							chapterStartFind = true;
							continue;
						}
					}
					
					//********Ищем конец********
					if (!isDone) { //конец главы еще не найден
						if (line.matches(signChapEnd)) { //Нашли конец главы
							isDone = true;
						}
					}
					
					if (chapterStartFind && !isDone) { //если нашли начальный стих, то подбираем строку в любом случае
						strBuilder.append(FromatChapterText(line, useColorFromBQ)); // + "\n"
					}
					
					
				} //while
				is.close();
				str = strBuilder.toString();
			} catch (IOException e) {
				str = mCntx.getString(R.string.err_file_book_not_open);
				e.printStackTrace();
			}
		}
		else {
			str = mCntx.getString(R.string.err_book_not_find);
		}
		
		
		return str;
	}
	
	public String GetTextForChapterWithHead(String book, String chapter) {
		//TODO
		return "<h2>" + GetNameForBook(book, chapter) + "</h2>\n" + GetTextForChapter(book, chapter);
	}
	
	/**
	 * Форматируем строку текста: если это название главы, то добавляем хтмл-тэг, если нужно, убираем цвет текста
	 убираем <font COLOR="purple">  </font>
	 * @param line строка, которую нужно отформатировать
	 * @return отформатированная строка
	 */
	private String FromatChapterText(String line, Boolean useColorFromBQ) {
		String formatedLine = "";
		if (line.startsWith(chapterSign)) {
			int indexOfNum = line.indexOf(">")+1;
			int indexOfEndNum = line.lastIndexOf("<");
			if (indexOfEndNum <= 0 || indexOfEndNum <= indexOfNum) {
				indexOfEndNum = line.length();
			}
			String num = line.substring(indexOfNum, indexOfEndNum);
			formatedLine = verseSign + mCntx.getString(R.string.chapter_name) + " " + num;
		}
		else {
			if (useColorFromBQ) {
				formatedLine = line;
			}
			else {
				formatedLine = line.replaceAll("<font.+?>", "").replaceAll("</font>", "");
			}
			//formatedLine = formatedLine.replaceAll("<p>", "<br>");
		}
		return formatedLine;	
	}
	
	public String GetTextForArray(ArrayList<BookFromSite> ab) {
		//Нужно ли выводить все чтения, или только рядовые
		PrefManager pm = new PrefManager(mCntx);
		Boolean onlyOrdinaryReading = pm.getOnlyOrdinaryReading();
		
		StringBuilder msgStr = new StringBuilder();
		
		for (BookFromSite book : ab) {
			if (!onlyOrdinaryReading || (book.isOrdinary && book.alt==0)) {
				if (book.alt>0) {
					msgStr.append("<p><I>или</I></p>\n");
				}
				msgStr.append("<h2>" + GetNameForBook(book));
				//msgStr.append("<h2>" + book.fullText);
				if (! book.type.equals("") && !(book.type==null)) {
					msgStr.append(" (" + book.type + ")");
				}
				msgStr.append("</h2>\n" + GetTextForBook(book) + "\n");
				//TODO добавить ссылку для чтения дальше в другой активности
				//Формируем адрес первой ссылки
				String chapter = "";
				String stih = "";
				if (book.otr[0] != null) {
					chapter = String.valueOf(book.otr[0].chapterEnd);
					stih = String.valueOf(book.otr[0].stihEnd);
				}
				msgStr.append("<br> <a href=\"activity-run://BibleActivityHost?book=" + book.book + "&chapter=" + chapter + "&stih=" + stih + "\">" + mCntx.getString(R.string.read_next) + "</a>");
			}
		}
		
		String str = msgStr.toString().trim();
		
		return str;
	}
	
	public String GetTextForArrayWithHead(ArrayList<BookFromSite> ab, String confession, GregorianCalendar date) {
		final String[] monthArray = mCntx.getResources().getStringArray(R.array.month_of_year);
		
		StringBuilder msgStr = new StringBuilder();
		msgStr.append(confession + ", " + String.valueOf(date.get(Calendar.DAY_OF_MONTH)) + " " + monthArray[date.get(Calendar.MONTH)]);
		//msgStr.append(confession + ", " + DateFormat.format("dd.MM.yyyy", date).toString());
		
		msgStr.append(GetTextForArray(ab));
		return msgStr.toString();
	}
	
	@Override
	public String toString() {
		return "BQ [chapterSign=" + chapterSign
				+ ", verseSign=" + verseSign + ", bookQty=" + bookQty
				+ ", bookMap=" + bookMap + "]";
	}

	
}


class BookBQ {
	public String pathName;
	public String fullName;
	public int chapterQty;
	
	@Override
	public String toString() {
		return "BookBQ [pathName=" + pathName + ", fullName=" + fullName
				+ ", chapterQty=" + chapterQty + "]";
	}
}

