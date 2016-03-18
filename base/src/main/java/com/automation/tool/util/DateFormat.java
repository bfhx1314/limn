package com.automation.tool.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
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
    /**
     *
     * @return yyyy-MM-dd HH:mm:ss
     */
    public static String getDateToString(long currentTimeMillis){
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(currentTimeMillis);
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
    public static long getCurrentTimeMillis(){
        return System.currentTimeMillis();
    }

	/**
	 * 获取今日以后、以前的日期
	 * @param num 今天开始计算的天数,负数为之前
	 * @return yyyy-MM-dd
	 */
	public static String getAddDay(int num){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal = Calendar.getInstance();  
        try {
			cal.setTime(sdf.parse(getDateString()));
		} catch (ParseException e) {
            e.printStackTrace();
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

    /**
     *
     * @param format yyyy-MM-dd HH:mm:ss
     * @return
     */
    public static String getDate(String format, long currentTimeMillis){
        return new SimpleDateFormat(format).format(currentTimeMillis);
    }

	public static String getData(String format , Date data){
        return new SimpleDateFormat(format).format(data);
	}
	
	
	public static Date getData(String format , String data){
		Date date = null;
		try {
			date =  new SimpleDateFormat(format).parse(data);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return date;
	}
//
//	public static void main(String[] args){
//		String a = getDateString();
//		a = getAddDay(1);
//        a = getDate("yyyy-MM-dd HH:mm:ss");
//        a = getDate("yyyy-MM-dd_HH-mm-ss",System.currentTimeMillis());
//        try {
//            Date b = new SimpleDateFormat("yyyy-MM-dd").parse("2015-12-28");
//            a = getData("w",b);
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//        System.out.println(a);
//	}

}
