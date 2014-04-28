package ru.itprospect.everydaybiblereading;
import java.util.GregorianCalendar;


public class BookFromSite {
	GregorianCalendar date;
	String dateStr;
	String fullText;
	String book;
	String adress;
	String type;
	Boolean isOrdinary=false;
	int alt=0;
	ReadStartEnd [] otr = new ReadStartEnd[20];
	
	

	
	@Override
	public String toString() {
		String StrOtr = "# ";
		for (ReadStartEnd otrivok : otr) {
			if (otrivok!=null) {
				StrOtr = StrOtr + otrivok.toString() + " # ";
			};
		};
		
		return "BookFromSite [fullText=" + fullText + ", book="
				+ book  + ", date="	+ dateStr + ", otr=" + StrOtr + "]";
	}
	
	public void GenFullText() {
		StringBuilder strBuilder = new StringBuilder();
		//strBuilder.append(book + " ");
		int oldChapterEnd = 0;
		int oldChapterStart = 0;
		
		for (ReadStartEnd otrivok : otr) {
			if (otrivok!=null) {
				String chapterStartStr = String.valueOf(otrivok.chapterStart);
				if (otrivok.chapterStart == 1003) {
					chapterStartStr = "C";
				} 
				String chapterEndStr = String.valueOf(otrivok.chapterEnd);
				if (otrivok.chapterEnd == 1003) {
					chapterEndStr = "C";
				} 	
				
				if (oldChapterStart == oldChapterEnd && otrivok.chapterStart == oldChapterEnd && otrivok.chapterStart == otrivok.chapterEnd) {
					strBuilder.append(otrivok.stihStart);
				}
				else {
					strBuilder.append(chapterStartStr+":"+otrivok.stihStart);
				}
				if (otrivok.chapterStart == otrivok.chapterEnd) {
					if (otrivok.stihStart == otrivok.stihEnd) {
						strBuilder.append(", ");
					}
					else {
						strBuilder.append("-" + otrivok.stihEnd + ", ");
					}
				} 
				else {
					strBuilder.append("-" + chapterEndStr + ":" + otrivok.stihEnd + ", ");
				}
				
				oldChapterEnd = otrivok.chapterEnd;
				oldChapterStart = otrivok.chapterStart;
			}
		}
		

		//Убираем лишнюю запятую
		if (strBuilder.substring(strBuilder.length()-2).equals(", ")) {
			strBuilder.delete(strBuilder.length()-2, strBuilder.length());
		}
		adress = strBuilder.toString();
		fullText = book + " " + adress;
	}
	
}	


class ReadStartEnd {
	int chapterStart;
	int stihStart;
	int chapterEnd;
	int stihEnd;
	
	@Override
	public String toString() {
		return "ReadStartEnd [chapterStart=" + chapterStart + ", StihStart="
				+ stihStart + ", chapterEnd=" + chapterEnd + ", StihEnd="
				+ stihEnd + "]";
	}
	
}