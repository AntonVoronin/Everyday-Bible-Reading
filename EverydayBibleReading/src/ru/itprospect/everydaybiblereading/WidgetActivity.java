package ru.itprospect.everydaybiblereading;

import java.util.Calendar;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.text.format.DateFormat;
import android.widget.RemoteViews;

public class WidgetActivity extends AppWidgetProvider {
	
	public static final String ACTION_WIDGET_START_MAIN_ACT = "ActionWidgetStartMainActivity";
	public static final String WIDGET_FORCE_UPDATE = "ru.itprospect.everydaybiblereading.WIDGET_FORCE_UPDATE";
	public static final String WIDGET_ALARM_UPDATE = "ru.itprospect.everydaybiblereading.WIDGET_ALARM_UPDATE";
	private static final Boolean writeLog = false; 
	
	@Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
		updateRead(context, appWidgetManager, appWidgetIds);
		if (writeLog) {Log.WriteLogDate(context, "auto update");}
	}
	
	@Override
	 public void onDisabled(Context context) {
	  Intent intent = new Intent(WIDGET_ALARM_UPDATE);
	  PendingIntent sender = PendingIntent.getBroadcast(context, 0, intent, 0);
	  AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
	  alarmManager.cancel(sender);
	  
	  super.onDisabled(context);
	  if (writeLog) {Log.WriteLogDate(context, "on disabled, cancel alarm");}
	 }
	 
	 @Override
	 public void onEnabled(Context context) {
	  super.onEnabled(context);
	  AlarmManager am=(AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
	  Intent intent = new Intent(WIDGET_ALARM_UPDATE);
	  PendingIntent pi = PendingIntent.getBroadcast(context, 0, intent, 0);
	  
	  //Время срабатывания
	  Calendar calendar = Calendar.getInstance();

	  calendar.setTimeInMillis(System.currentTimeMillis());
      //Первый раз триггер должен сработать на следующий день, в 00:00:15
      calendar.add(Calendar.DAY_OF_MONTH, 1);
      calendar.set(Calendar.HOUR_OF_DAY, 0);
      calendar.set(Calendar.MINUTE, 0);
      calendar.set(Calendar.SECOND, 15);
	  
	  am.setInexactRepeating(AlarmManager.RTC, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pi);
	  
	  if (writeLog) {Log.WriteLogDate(context, "on enabled, set alarm at " + DateFormat.format("dd.MM.yyyy k:m:s z", calendar).toString());}
	 }
	
	public void updateRead(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
		//Создаем новый RemoteViews
		RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_layout_calendar);

		PrefManager prefManager = new PrefManager(context);
		String confession = prefManager.getConfession();
		CalSAXParser pars = new CalSAXParser(context, confession);
		if (pars.FindSuccess()) {
			String str = pars.GetTextForWidget();
			remoteViews.setTextViewText(R.id.w_text_book, str);
		} 

		//Устанавливаем нужный фон
		int colorSchemeId = prefManager.getColorSchemeWidgetId(); 
		remoteViews.setInt(R.id.w_text_book, "setBackgroundResource", colorSchemeId);
		
		//Подготавливаем Intent
		Intent startIntent = new Intent(context, MainActivity.class);
		startIntent.setAction(ACTION_WIDGET_START_MAIN_ACT);
		startIntent.putExtra("conf", WorkSettings.GetDefoltConf());
		PendingIntent actionPendingIntent = PendingIntent.getActivity(context, 0, startIntent, 0);
		//регистрируем наше событие
		remoteViews.setOnClickPendingIntent(R.id.w_text_book, actionPendingIntent);

		//обновляем виджет
		appWidgetManager.updateAppWidget(appWidgetIds, remoteViews);
	}
	
	public void updateRead(Context context) {
		ComponentName thisWidget = new ComponentName(context, WidgetActivity.class);
		AppWidgetManager appWidgetManager =	AppWidgetManager.getInstance(context);
		int[] appWidgetIds = appWidgetManager.getAppWidgetIds(thisWidget);
		
		updateRead(context, appWidgetManager, appWidgetIds);
	}
	
	public void onReceive(Context context, Intent intent) {
		super.onReceive(context, intent);
		
		if (intent.getAction().equals(WIDGET_FORCE_UPDATE)) {
			updateRead(context);
			if (writeLog) {Log.WriteLogDate(context, "force update");}
		}
		else if (intent.getAction().equals(WIDGET_ALARM_UPDATE)) {
			updateRead(context);
			if (writeLog) {Log.WriteLogDate(context, "alarm update");}
		}
	}

}
