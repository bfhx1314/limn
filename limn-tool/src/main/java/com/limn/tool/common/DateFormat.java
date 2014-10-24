package com.limn.tool.common;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;

public class DateFormat {
	/**
	 * 
	 * @return yyyy-MM-dd HH:mm:ss
	 */
	public static String getDateToString(){
		return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(GregorianCalendar.getInstance().getTime());
	}
	
	
	public static Date getDate(){
		return GregorianCalendar.getInstance().getTime();
	}
	
	
	/**
	 * 
	 * @param format yyyy-MM-dd HH:mm:ss
	 * @return 
	 */
	public static String getDate(String format){
		return new SimpleDateFormat(format).format(GregorianCalendar.getInstance().getTime());
	}
	
	public static String getData(String format , Date data){
		String date = null;
		date =  new SimpleDateFormat(format).format(data);
		
		return date;
	}
	
	
	public static Date getData(String format , String data){
		java.util.Date date = null;
		try {
			date =  new SimpleDateFormat(format).parse(data);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return date;
	}
	
}
