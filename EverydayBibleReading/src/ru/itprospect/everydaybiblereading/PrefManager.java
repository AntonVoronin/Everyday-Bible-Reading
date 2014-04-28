package ru.itprospect.everydaybiblereading;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.preference.PreferenceManager;

public class PrefManager {
	
	public static final String KEY_PREF_TEXT_SIZE="PREF_TEXT_SIZE";
	public static final String KEY_PREF_TEXT_COLOUR="PREF_TEXT_COLOUR";
	public static final String KEY_PREF_BACKGROUND_COLOUR="PREF_BACKGROUND_COLOUR";
	public static final String KEY_PREF_COLOR_FROM_BQ="PREF_COLOR_FROM_BQ";
	public static final String KEY_PREF_ONLY_ORDINARY_READING="PREF_ONLY_ORDINARY_READING";
	public static final String KEY_PREF_COLOR_SCHEME="PREF_COLOR_SCHEME";
	public static final String KEY_PREF_IS_NIGHT="PREF_IS_NIGHT";
	public static final String KEY_PREF_TRANSLIT="PREF_SHARE_TRANSLIT";
	public static final String KEY_PREF_CONFESSION="PREF_CONFESSION";
		
	private SharedPreferences prefs;
	private Context context;

	public PrefManager(Context context) {
		this.context = context;
		prefs = PreferenceManager.getDefaultSharedPreferences(context);
	}

	
	public int getTextSize() {
		int textSize = Integer.parseInt(prefs.getString(KEY_PREF_TEXT_SIZE, context.getString(R.string.text_size_default)));
		return textSize;
	}
	
	public void putTextSize(int textSize) {
		SharedPreferences.Editor editor = prefs.edit();
		editor.putString(KEY_PREF_TEXT_SIZE, Integer.toString(textSize));
		editor.commit();
		
	}
	
	public void putTextSize(String textSize) {
		SharedPreferences.Editor editor = prefs.edit();
		editor.putString(KEY_PREF_TEXT_SIZE, textSize);
		editor.commit();
		
	}
	
	public int getTextColour() {
		int textColour = prefs.getInt(KEY_PREF_TEXT_COLOUR,Color.BLACK);
		return textColour;
	}
	
	public int getBackgroundColour() {
		int backgroundColour = prefs.getInt(KEY_PREF_BACKGROUND_COLOUR,Color.WHITE);
		return backgroundColour;
	}
	
	public Boolean getUseColorFromBQ() {
		Boolean useColorFromBQ = prefs.getBoolean(KEY_PREF_COLOR_FROM_BQ, false);
		return useColorFromBQ;
	}
	
	public Boolean getOnlyOrdinaryReading() {
		Boolean onlyOrdinaryReading = prefs.getBoolean(KEY_PREF_ONLY_ORDINARY_READING, true);
		return onlyOrdinaryReading;
	}
	
	public Boolean isNight() {
		Boolean isNight = prefs.getBoolean(KEY_PREF_IS_NIGHT, false);
		return isNight;
	}
	
	public Boolean getTranslit() {
		Boolean translit = prefs.getBoolean(KEY_PREF_TRANSLIT, false);
		return translit;
	}
	
	public String getConfession() {
		String confession = prefs.getString(KEY_PREF_CONFESSION, context.getString(R.string.confession_default));
		return confession;
	}
	
	public void putConfession(String confession) {
		SharedPreferences.Editor editor = prefs.edit();
		editor.putString(KEY_PREF_CONFESSION, confession);
		editor.commit();
	}
	
	public void putIsNight(Boolean isNight) {
		SharedPreferences.Editor editor = prefs.edit();
		editor.putBoolean(KEY_PREF_IS_NIGHT, isNight);
		editor.commit();
		
	}
	
//	public Drawable getColorSchemeDrawId() {
//		String colorScheme = prefs.getString(KEY_PREF_COLOR_SCHEME, context.getString(R.string.color_scheme_default));
//		
//		TypedArray draws = context.getResources().obtainTypedArray(R.array.color_scheme_draw);
//		String[] colorArray = context.getResources().getStringArray(R.array.color_scheme_values);
//		
//		Drawable drawable = draws.getDrawable(0);
//		
//		for (int i=0; i<colorArray.length; i++) {
//			if (colorArray[i].equals(colorScheme)) {
//				drawable = draws.getDrawable(i);
//				break;
//			}
//		}
//		draws.recycle();
//		
//		return drawable;
//	}
	
	public Drawable getColorSchemeWidgetDraw() {
		String colorScheme = prefs.getString(KEY_PREF_COLOR_SCHEME, context.getString(R.string.color_scheme_default));
		
		TypedArray draws = context.getResources().obtainTypedArray(R.array.color_scheme_widget_draw);
		String[] colorArray = context.getResources().getStringArray(R.array.color_scheme_values);
		
		Drawable drawable = draws.getDrawable(0);
		
		for (int i=0; i<colorArray.length; i++) {
			if (colorArray[i].equals(colorScheme)) {
				drawable = draws.getDrawable(i);
				break;
			}
		}
		draws.recycle();
		
		return drawable;
	}
	
	public int getColorSchemeActionBarId() {
		String colorScheme = prefs.getString(KEY_PREF_COLOR_SCHEME, context.getString(R.string.color_scheme_default));
		String[] colorArray = context.getResources().getStringArray(R.array.color_scheme_values);
		
		int[] idArray = {R.drawable.title_act_bar_red, R.drawable.title_act_bar_green, R.drawable.title_act_bar_blue};
		
		int drawableInt = idArray[0];
		
		for (int i=0; i<colorArray.length; i++) {
			if (colorArray[i].equals(colorScheme)) {
				drawableInt = idArray[i];
				break;
			}
		}
		
		return drawableInt;
	}
	
	public int getColorSchemeWidgetId() {
		String colorScheme = prefs.getString(KEY_PREF_COLOR_SCHEME, context.getString(R.string.color_scheme_default));
		String[] colorArray = context.getResources().getStringArray(R.array.color_scheme_values);
		
		int[] idArray = {R.drawable.widget_red, R.drawable.widget_green, R.drawable.widget_blue};
		
		int drawableInt = idArray[0];
		
		for (int i=0; i<colorArray.length; i++) {
			if (colorArray[i].equals(colorScheme)) {
				drawableInt = idArray[i];
				break;
			}
		}
		
		return drawableInt;
	}
}
