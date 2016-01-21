package ru.itprospect.everydaybiblereading;


import java.io.IOException;
import java.util.ArrayList;
import java.util.GregorianCalendar;

import org.xmlpull.v1.XmlPullParserException;

import android.content.Context;
import android.content.res.XmlResourceParser;


public class CalSAXParser {
	private GregorianCalendar mDate;
	private Boolean FindSuccess;
	private ArrayList<BookFromSite> listBook;
	//private String fullText;
	private String errorText;
	private String dayType;
	private Context mCntx;
	
//	private final String NODE_YEAR = "Year";
	private final String NODE_DATE = "Date";
	private final String NODE_BOOK = "Book";
	private final String NODE_FRAG = "Fragment";
	
	private final String ATR_BOOK = "book";
	private final String ATR_IS_ORDINARY = "isOrdinary";
	private final String ATR_ALT = "alt";
	private final String ATR_TYPE = "type";
	private final String ATR_CHAPTER_START = "chapterStart";
	private final String ATR_CHAPTER_END = "chapterEnd";
	private final String ATR_STIH_START = "stihStart";
	private final String ATR_STIH_END = "stihEnd";
	
	
	public CalSAXParser(Context cntx) {
		this(cntx, new GregorianCalendar());
	}
	
	public CalSAXParser(Context cntx, GregorianCalendar date) {
		this(cntx, date, WorkSettings.GetDefoltConf());
	}

	public CalSAXParser(Context cntx, String conf) {
		this(cntx, new GregorianCalendar(), conf);
	}
	
	public CalSAXParser(Context cntx, GregorianCalendar date, String conf) {
		mDate = date;
		mCntx = cntx;
		FindSuccess = false;
		//String strYear = String.format("%tY", mDate);
		String strDate = String.format("%tF", mDate);
		BookFromSite book = new BookFromSite();
		listBook = new ArrayList<BookFromSite>();
		int numFrag = 0;
		
		Boolean nashliDatu = false;
		Boolean isDone = false;	
		
		//Получаем настройки
		WorkSettings wSet = new WorkSettings(date, conf);
		
		if (wSet.getFileFindSuccess()) {
			//Определяем id файла
			int fileID = wSet.GetFileId();
			
			//Создаем хмл парсер
			try {
				XmlResourceParser xrp =  mCntx.getResources().getXml(fileID);

				while ((xrp.getEventType() != XmlResourceParser.END_DOCUMENT) && (!isDone)) {

					if (xrp.getEventType() == XmlResourceParser.START_TAG) {

						if (xrp.getName().equals(NODE_DATE)) {
							//стоим на ветке с датой
							if (xrp.getAttributeValue(0).equals(strDate)) {
								//Это ветка с нужной нам датой
								//Toast.makeText(mCntx, xrp.getAttributeValue(0), Toast.LENGTH_SHORT).show();
								nashliDatu = true;
								//TODO берем type - название праздника
								dayType = xrp.getAttributeValue(null, ATR_TYPE);
							}
						}
						
						if (nashliDatu && xrp.getName().equals(NODE_BOOK)) {
							//стоим на ветке книгой
							//нужно создать новый экземпляр книги
							book = new BookFromSite();
							book.book = xrp.getAttributeValue(null, ATR_BOOK);
							String isOrdinary = xrp.getAttributeValue(null, ATR_IS_ORDINARY); 
							if (!(isOrdinary==null) && isOrdinary.equals("false")) {
								book.isOrdinary = false;
							}
							else {
								book.isOrdinary = true;
							}
							
							String alt = xrp.getAttributeValue(null, ATR_ALT);
							if (alt!=null) {
								book.alt = Integer.parseInt(alt);
							}
							else {
								book.alt = 0;
							}
							
							String type =  xrp.getAttributeValue(null, ATR_TYPE);
							if (type==null) book.type = "";
							else book.type =  type;
							
        	                book.date = mDate;
        	                book.dateStr = strDate;
        	                numFrag = 0;
							
						}
						
						if (nashliDatu && xrp.getName().equals(NODE_FRAG)) {
							//стоим на ветке с фрагментом
							ReadStartEnd otr = new ReadStartEnd();
							String chapterStartStr = xrp.getAttributeValue(null, ATR_CHAPTER_START);
							String chapterEndStr = xrp.getAttributeValue(null, ATR_CHAPTER_END);
							//В книге Есфирь есть дополнение C
							if (chapterStartStr.equals("C")) chapterStartStr = "1003";
							if (chapterEndStr.equals("C")) chapterEndStr = "1003";
							
	                		otr.chapterStart = Integer.parseInt(chapterStartStr);
	                		otr.chapterEnd = Integer.parseInt(chapterEndStr);
	                		otr.stihStart = Integer.parseInt(xrp.getAttributeValue(null, ATR_STIH_START));
	                		otr.stihEnd = Integer.parseInt(xrp.getAttributeValue(null, ATR_STIH_END));
	                		
	                		book.otr[numFrag] = otr;
	                		numFrag++;
						}
						//PrevTag = xrp.getName();





					} else if (xrp.getEventType() == XmlResourceParser.END_TAG) {

						if (nashliDatu && xrp.getName().equals(NODE_DATE)) {
							//на прошлом шаге уже нашли дату, а теперь стоим в конце этого блока
							isDone=true;
						};
						
						if (nashliDatu && xrp.getName().equals(NODE_BOOK)) {
							//Закончилась ветка с книгой, нужно добавить ее в список
							book.GenFullText();
        	                listBook.add(book);
						}

					} else if (xrp.getEventType() == XmlResourceParser.TEXT) {


					}

					xrp.next();

				}

				xrp.close();

				FindSuccess = isDone;
				if (!isDone) {
					errorText = mCntx.getString(R.string.error_calendar_failed);
				}

			} catch (XmlPullParserException e) {
				e.printStackTrace();
				errorText = mCntx.getString(R.string.error_parser);
			} catch (IOException e) {
				e.printStackTrace();
				errorText = mCntx.getString(R.string.error_fs);

			}
			
		}
		else {
			errorText = mCntx.getString(R.string.error_calendar_not_find);
		}
		
	
	}
	
	Boolean FindSuccess() {
		return FindSuccess;
	}
	
	String GetTextForWidget() {
		StringBuilder strBuilder = new StringBuilder();
		for (BookFromSite book : listBook) {
			if (book.isOrdinary) {
				strBuilder.append(book.fullText + "\r\n");
			}
		}
		
		return strBuilder.toString().trim();
	}
	
	public ArrayList<BookFromSite> GetListBook() {
		return listBook;
	}
	
	String GetErrorText() {
		return errorText;
	}

	public String getDayType() {
		return dayType;
	}
	
}
