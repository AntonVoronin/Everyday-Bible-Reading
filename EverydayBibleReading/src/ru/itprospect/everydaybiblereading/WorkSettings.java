package ru.itprospect.everydaybiblereading;

import java.util.GregorianCalendar;

public class WorkSettings {
	
	public static final String CONF_ORTHODOXY = "orthodoxy"; //����������� 
	public static final String CONF_CATHOLIC = "catholic"; //������������
	
	private Boolean fileFindSuccess = false;
	private GregorianCalendar date; 
	private String conf;
	
	public WorkSettings(GregorianCalendar date, String conf) {
		this.date = date;
		this.conf = conf;
		
		//�������� ������� ��� ��� ���� � ���������
		if (conf.equals(CONF_ORTHODOXY)) {
			if (date.compareTo(new GregorianCalendar(2013, 2, 1))>=0 && date.compareTo(new GregorianCalendar(2023, 5, 30))<=0) {
				fileFindSuccess = true;
			}
		} 
		else if (conf.equals(CONF_CATHOLIC)) {
			if (date.compareTo(new GregorianCalendar(2013, 11, 1))>=0 && date.compareTo(new GregorianCalendar(2023, 5, 30))<=0) {
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
			else if (date.compareTo(new GregorianCalendar(2018, 0, 14))>=0 && date.compareTo(new GregorianCalendar(2019, 0, 13))<=0) {
				return R.xml.read_cal_ru_orth_2018;
			}
			else if (date.compareTo(new GregorianCalendar(2019, 0, 14))>=0 && date.compareTo(new GregorianCalendar(2020, 0, 13))<=0) {
				return R.xml.read_cal_ru_orth_2019;
			}
			else if (date.compareTo(new GregorianCalendar(2020, 0, 14))>=0 && date.compareTo(new GregorianCalendar(2021, 0, 13))<=0) {
				return R.xml.read_cal_ru_orth_2020;
			}
			else if (date.compareTo(new GregorianCalendar(2021, 0, 14))>=0 && date.compareTo(new GregorianCalendar(2022, 0, 13))<=0) {
				return R.xml.read_cal_ru_orth_2021;
			}
			else if (date.compareTo(new GregorianCalendar(2022, 0, 14))>=0 && date.compareTo(new GregorianCalendar(2022, 11, 31))<=0) {
				return R.xml.read_cal_ru_orth_2022;
			}
			else if (date.compareTo(new GregorianCalendar(2023, 0, 1))>=0 && date.compareTo(new GregorianCalendar(2023, 11, 31))<=0) {
				return R.xml.read_cal_ru_orth_2023;
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
			else if (date.compareTo(new GregorianCalendar(2018, 0, 1))>=0 && date.compareTo(new GregorianCalendar(2018, 11, 31))<=0) {
				return R.xml.read_cal_ru_cat_2018;
			}
			else if (date.compareTo(new GregorianCalendar(2019, 0, 1))>=0 && date.compareTo(new GregorianCalendar(2019, 11, 31))<=0) {
				return R.xml.read_cal_ru_cat_2019;
			}
			else if (date.compareTo(new GregorianCalendar(2020, 0, 1))>=0 && date.compareTo(new GregorianCalendar(2020, 11, 31))<=0) {
				return R.xml.read_cal_ru_cat_2020;
			}
			else if (date.compareTo(new GregorianCalendar(2021, 0, 1))>=0 && date.compareTo(new GregorianCalendar(2021, 11, 31))<=0) {
				return R.xml.read_cal_ru_cat_2021;
			}
			else if (date.compareTo(new GregorianCalendar(2022, 0, 1))>=0 && date.compareTo(new GregorianCalendar(2022, 11, 31))<=0) {
				return R.xml.read_cal_ru_cat_2022;
			}
			else if (date.compareTo(new GregorianCalendar(2023, 0, 1))>=0 && date.compareTo(new GregorianCalendar(2023, 11, 31))<=0) {
				return R.xml.read_cal_ru_cat_2023;
			}
		};		
		
		return R.xml.read_cal_ru_orth_2019;
	}
	
	public static String GetDefoltConf() {
		return CONF_ORTHODOXY;
	}
	
	public Boolean getFileFindSuccess() {
		return fileFindSuccess;
	}
	
}
