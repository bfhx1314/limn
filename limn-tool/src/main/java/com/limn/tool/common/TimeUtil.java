package com.limn.tool.common;

import org.joda.time.DateTime;

import java.util.Date;

/**
 * Created by lilei on 2016/4/29.
 */
public class TimeUtil {
    private static DateTime date = new DateTime();
    private static DateTime currStartTime = new DateTime(date.getYear(),date.getMonthOfYear(),date.getDayOfMonth(),0,0,0);
    private static DateTime currEndTime = new DateTime(date.getYear(),date.getMonthOfYear(),date.getDayOfMonth(),23,59,59);
    private static DateTime lastMonStartTime = currStartTime.dayOfMonth().withMinimumValue().minusDays(1);
    private static DateTime lastMonEndTime = currEndTime.dayOfMonth().withMinimumValue().minusDays(1);
    private static DateTime nextMonStartTime = currStartTime.dayOfMonth().withMaximumValue().plusDays(1);
    private static DateTime nextMonEndTime = currEndTime.dayOfMonth().withMaximumValue().plusDays(1);

    private static Date startOfWeek(DateTime dateTime) {
        return dateTime.dayOfWeek().withMinimumValue().toDate();
    }

    private static Date startOfMon(DateTime dateTime) {
        return dateTime.dayOfMonth().withMinimumValue().toDate();
    }
    private static Date startOfCurrentDay(DateTime dateTime) {
        return dateTime.toDate();
    }
    private static Date endOfCurrentDay(DateTime dateTime) {
        return dateTime.toDate();
    }

    private static Date endOfWeek(DateTime dateTime) {
        return dateTime.dayOfWeek().withMaximumValue().toDate();
    }
    private static Date endOfMon(DateTime dateTime) {
        return dateTime.dayOfMonth().withMaximumValue().toDate();
    }

    public static Date getStartOfCurrWeek() {
        return startOfWeek(currStartTime);
    }
    public static Date getEndOfCurrWeek() {
        return endOfWeek(currEndTime);
    }
    public static Date getStartOfLastWeek() {
        return startOfWeek(currStartTime.minusDays(7));
    }
    public static Date getEndOfLastWeek() {
        return endOfWeek(currEndTime.minusDays(7));
    }
    public static Date getStartOfNextWeek() {
        return startOfWeek(currStartTime.plusDays(7));
    }
    public static Date getEndOfNextWeek() {
        return endOfWeek(currEndTime.plusDays(7));
    }

    public static Date getStartOfCurrMon() {
        return startOfMon(currStartTime);
    }
    public static Date getEndOfCurrMon() {
        return endOfMon(currEndTime);
    }
    public static Date getStartOfLastMon() {
        return startOfMon(lastMonStartTime);
    }
    public static Date getEndOfLastMon() {
        return endOfMon(lastMonEndTime);
    }

    public static Date getStartOfNextMon() {
        return startOfMon(nextMonStartTime);
    }
    public static Date getEndOfNextMon() {
        return endOfMon(nextMonEndTime);
    }

    public static Date getStartOfCurrentDay() {
        return startOfCurrentDay(currStartTime);
    }
    public static Date getEndOfCurrentDay() {
        return endOfCurrentDay(currEndTime);
    }
    public static void main(String[] args) {
        DateTime date = new DateTime();
        int a =date.getYear();
        int b = date.getDayOfMonth();
        int c = date.getMonthOfYear();
        DateTime d2 = new DateTime(a,b,c, 0,0);
        getStartOfCurrWeek();
        getEndOfCurrWeek();
        getStartOfLastWeek();
        getEndOfLastWeek();
        getStartOfNextWeek();
        getEndOfNextWeek();
        getStartOfCurrMon();
        getEndOfCurrMon();
        getStartOfLastMon();
        getEndOfLastMon();
        getStartOfNextMon();
        getEndOfNextMon();
    }

}
