package ru.itprospect.everydaybiblereading;

import java.util.GregorianCalendar;

public class WorkSettings {
	
	public static final String CONF_ORTHODOXY = "orthodoxy"; //Православие 
	public static final String CONF_CATHOLIC = "catholic"; //Католичество
	
	private Boolean fileFindSuccess = false;
	private GregorianCalendar date; 
	private String conf;
	
	public WorkSettings(GregorianCalendar date, String conf) {
		this.date = date;
		this.conf = conf;
		
		//Проверка наличия хмл для даты и конфессии
		if (conf.equals(CONF_ORTHODOXY)) {
			if (date.compareTo(new GregorianCalendar(2013, 2, 1))>=0 && date.compareTo(new GregorianCalendar(2017, 8, 30))<=0) {
				fileFindSuccess = true;
			}
		} 
		else if (conf.equals(CONF_CATHOLIC)) {
			if (date.compareTo(new GregorianCalendar(2013, 11, 1))>=0 && date.compareTo(new GregorianCalendar(2017, 8, 30))<=0) {
				fileFindSuccess = true;
			}			
		};
		
	}
	
	public int GetFileId() {
		if (conf.equals(CONF_ORTHODOXY)) {
			if (date.compareTo(new GregorianCalendar(2013, 2, 1))>=0 && date.compareTo(new GregorianCalendar(2014, 0, 12))<=0) {
				return R.xml.read_cal_ru_orth_2013;
			}
			else if (date.compareTo(new GregorianCalendar(2014, 0, 14))>=0 && date.compareTo(new GregorianCalendar(2015, 0, 13))<=0) {
				return R.xml.read_cal_ru_orth_2014;
			}
			else if (date.compareTo(new GregorianCalendar(2015, 0, 14))>=0 && date.compareTo(new GregorianCalendar(2016, 0, 3))<=0) {
				return R.xml.read_cal_ru_orth_2015;
			}
			else if (date.compareTo(new GregorianCalendar(2016, 0, 1))>=0 && date.compareTo(new GregorianCalendar(2017, 0, 13))<=0) {
				return R.xml.read_cal_ru_orth_2016;
			}
			else if (date.compareTo(new GregorianCalendar(2017, 0, 14))>=0 && date.compareTo(new GregorianCalendar(2018, 0, 13))<=0) {
				return R.xml.read_cal_ru_orth_2017;
			}
		} 
		else if (conf.equals(CONF_CATHOLIC)) {
			if (date.compareTo(new GregorianCalendar(2013, 11, 1))>=0 && date.compareTo(new GregorianCalendar(2014, 11, 31))<=0) {
				return R.xml.read_cal_ru_cat_2014;
			}
			else if (date.compareTo(new GregorianCalendar(2015, 0, 1))>=0 && date.compareTo(new GregorianCalendar(2015, 11, 31))<=0) {
				return R.xml.read_cal_ru_cat_2015;
			}
			else if (date.compareTo(new GregorianCalendar(2016, 0, 1))>=0 && date.compareTo(new GregorianCalendar(2016, 11, 31))<=0) {
				return R.xml.read_cal_ru_cat_2016;
			}
			else if (date.compareTo(new GregorianCalendar(2017, 0, 1))>=0 && date.compareTo(new GregorianCalendar(2017, 11, 31))<=0) {
				return R.xml.read_cal_ru_cat_2017;
			}
		};		
		
		return R.xml.read_cal_ru_orth_2014;
	}
	
	public static String GetDefoltConf() {
		return CONF_ORTHODOXY;
	}
	
	public Boolean getFileFindSuccess() {
		return fileFindSuccess;
	}
	
}
