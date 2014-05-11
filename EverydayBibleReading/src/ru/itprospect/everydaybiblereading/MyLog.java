package ru.itprospect.everydaybiblereading;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.Context;
import android.widget.Toast;

public class MyLog {
	private final static String FILE_NAME = "ebr_log.txt";
	
	public static void WriteLog(Context cntx, String logText) {
		try {
			FileOutputStream fos = cntx.openFileOutput(FILE_NAME, Context.MODE_APPEND);
			PrintWriter pWriter = new PrintWriter(fos);
			pWriter.println(logText);
			pWriter.flush();
			fos.close();
			
			//Toast.makeText(cntx, "Записали лог", Toast.LENGTH_SHORT).show();
		} catch (IOException e) {
			
			Toast.makeText(cntx, "Не удалось записать лог", Toast.LENGTH_SHORT).show();
			e.printStackTrace();
		}
		
	} 
	
	public static String ReadLog(Context cntx) {
		String logText = "";
		try {
			FileInputStream fis = cntx.openFileInput(FILE_NAME);
			InputStreamReader isr = new InputStreamReader(fis);
			BufferedReader in = new BufferedReader(isr);
			String strLine;
			
			while ((strLine = in.readLine()) != null) {
				logText = logText + strLine + "\n";
			}
			
			
			fis.close();
			
		} catch (IOException e) {
			
			e.printStackTrace();
		}
		
		return logText;
	} 

	public static void ClearLog(Context cntx) {
		try {
			FileOutputStream fos = cntx.openFileOutput(FILE_NAME, Context.MODE_PRIVATE);
			PrintWriter pWriter = new PrintWriter(fos);
			pWriter.println("Start");
			pWriter.flush();
			fos.close();
			
			Toast.makeText(cntx, "Очистили лог", Toast.LENGTH_SHORT).show();
		} catch (IOException e) {
			
			Toast.makeText(cntx, "Не удалось очистить лог", Toast.LENGTH_SHORT).show();
			e.printStackTrace();
		}
		
	} 

	
	public static void WriteLogDate(Context cntx) {
		//GregorianCalendar now = new GregorianCalendar();
		//String dateStr = DateFormat.format("dd.MM.yyyy hh:mm:ss", now).toString();
		
		String dateStr = getCurrentTime("dd.MM.yyyy HH:mm:ss");
		
		WriteLog(cntx, dateStr);
	}
	
	public static void WriteLogDate(Context cntx, String str) {
		//GregorianCalendar now = new GregorianCalendar();
		//String dateStr = DateFormat.format("dd.MM.yyyy hh:mm:ss", now).toString();
		
		String dateStr = getCurrentTime("dd.MM.yyyy HH:mm:ss");
		
		WriteLog(cntx, dateStr+" - "+str);
	}
	
	public static String getCurrentTime(String timeformat){
	      Format formatter = new SimpleDateFormat(timeformat);
	         return formatter.format(new Date());
	     }

	
}
