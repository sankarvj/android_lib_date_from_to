package com.example.date_from_to_presenter.adapter;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by user on 25/05/17.
 */

public class DateRangeHelper {

    private static DateRangeHelper dateRangeHelper;
    private static Calendar from;
    private static Calendar to;
    private static int totalWeeksCount;
    private DateAdapter dateAdapter;

    private DateRangeHelper(){

    }

    public static DateRangeHelper getInstance(Calendar from, Calendar to, int totalWeeksCount){
        if(dateRangeHelper == null){
            dateRangeHelper = new DateRangeHelper();
        }
        dateRangeHelper.from = from;
        dateRangeHelper.to = to;
        dateRangeHelper.totalWeeksCount = totalWeeksCount;
        return dateRangeHelper;
    }

    public void openDialog(final AppCompatActivity activity, final DateDialogFragment.DateSelectedListener viewlistener){

        DateDialogFragment newFragment =  DateDialogFragment.newInstance(dateRangeHelper.from ,dateRangeHelper.to,getWeeks());
        newFragment.setDateSetListener(new DateDialogFragment.DateSelectedListener() {
            @Override
            public void OnDateSelected(DateAdapter updatedDateAdapter) {
                dateAdapter = updatedDateAdapter;
                dateRangeHelper.from = dateAdapter.getFromDate();
                dateRangeHelper.to = dateAdapter.getToDate();
                viewlistener.OnDateSelected(dateAdapter);
            }
        });
        newFragment.show(activity.getSupportFragmentManager(), "DatePicker");
    }

    public DateAdapter getDateAdaper(){
        return this.dateAdapter;
    }

    public DateAdapter getNewDateAdapter(Context context,DateAdapter.DateSetListener dListener){
        dateAdapter =  new DateAdapter(context,from,to,getWeeks(),false);
        dateAdapter.setDateSetListener(dListener);
        return dateAdapter;
    }

    private static ArrayList<String> getWeeks(){
        ArrayList<String> totalWeeks = new ArrayList<>();
        for (int i = 0; i < totalWeeksCount; i++) {
            totalWeeks.add(i+"");
        }
        return totalWeeks;
    }


}
