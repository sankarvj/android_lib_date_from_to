package com.example.date_from_to_presenter.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

/**
 * Created by user on 25/05/17.
 */

public class DateViewHelper {

    public static String getDay(Calendar date){
        SimpleDateFormat sdf = new SimpleDateFormat("dd", Locale.US);
        return sdf.format(date.getTime());
    }

    public static String getMonthAndDay(Calendar date){
        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd", Locale.US);
        return sdf.format(date.getTime());
    }

    public static String getMonthAndDayTime(Calendar date){
        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd @HH:mm", Locale.US);
        return sdf.format(date.getTime());
    }

    public static String getMonthAndDay(long  time_in_millis){
        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd", Locale.US);
        return sdf.format(time_in_millis);
    }

    public static String getMonthAndDayTime(long  time_in_millis){
        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd @HH:mm", Locale.US);
        return sdf.format(time_in_millis);
    }

    public static String getDayTime(long  time_in_millis){
        SimpleDateFormat sdf = new SimpleDateFormat("@HH:mm", Locale.US);
        return sdf.format(time_in_millis);
    }

    public static String getDateRange(long start,long end){
        Calendar fromDate = Calendar.getInstance();
        fromDate.setTimeInMillis(start);
        Calendar toDate = Calendar.getInstance();
        toDate.setTimeInMillis(end);

        String day_range;
        long trip_length_val = TimeUnit.DAYS.convert(end-start, TimeUnit.MILLISECONDS);
        if( trip_length_val < 1){
            day_range = "On "+ getMonthAndDay(fromDate);
        }else if( fromDate.get(Calendar.MONTH) == toDate.get(Calendar.MONTH) && fromDate.get(Calendar.YEAR) == toDate.get(Calendar.YEAR)){
            day_range =  getMonthAndDay(fromDate)+" to "+getDay(toDate);
        }else{
            day_range =  getMonthAndDay(fromDate)+" to "+getMonthAndDay(toDate);
        }

        return day_range;
    }
}
