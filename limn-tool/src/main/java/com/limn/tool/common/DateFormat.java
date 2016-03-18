package com.limn.tool.common;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import com.limn.tool.exception.SeleniumException;

public class DateFormat {
	/**
	 * 
	 * @return yyyy-MM-dd HH:mm:ss
	 */
	public static String getDateToString(){
		return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(GregorianCalendar.getInstance().getTime());
	}
	
	/**
	 * 获取日期
	 * @return yyyy-MM-dd
	 */
	public static String getDateString(){
		return new SimpleDateFormat("yyyy-MM-dd").format(GregorianCalendar.getInstance().getTime());
	}
	
	public static Date getDate(){
		return GregorianCalendar.getInstance().getTime();
	}
	
	/**
	 * 获取当前时间戳
	 * @return
	 */
	public static String getCurrentTimeMillis(){
		return String.valueOf(System.currentTimeMillis());
	}
	
	/**
	 * 获取今日以后、以前的日期
	 * @param num 今天开始计算的天数,负数为之前
	 * @return yyyy-MM-dd
	 * @throws SeleniumException
	 */
	public static String getAddDay(int num) throws SeleniumException{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal = Calendar.getInstance();  
        try {
			cal.setTime(sdf.parse(getDateString()));
		} catch (ParseException e) {
			throw new SeleniumException(22220052, e.getMessage());
		}  
        cal.add(Calendar.DAY_OF_YEAR, +num);  
        String nextDate_1 = sdf.format(cal.getTime());  
        return nextDate_1;
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
	
	public static void main(String[] args){
		String a = getDateString();
		try {
			a = getAddDay(1);
		} catch (SeleniumException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(a);

	}
	
}
