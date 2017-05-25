package com.example.date_from_to_presenter.adapter;

/**
 * Created by user on 25/05/17.
 */

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;

import com.example.date_from_to_presenter.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;

/**
 * Created by vijayasankar on 19/05/15.
 */
public class DateAdapter extends ArrayAdapter<String> {
    private final Context mContext;
    private final LayoutInflater inflater;
    private final Calendar currentDate;
    private Calendar fromDate;
    private Calendar toDate;
    private final ArrayList<String> dateList;
    private final ArrayList<Integer> monthListValue;
    private final ArrayList<String> monthList;
    private final ArrayList<Integer> yearListValue;
    private DateSetListener dListener;
    private final boolean is_single_select;
    private int bg_calendar_color_default;
    private int bg_calendar_color_selected;
    private static ArrayList<String> totalWeeks = new ArrayList<>();
    private static String TAG="DateAdapter";


    protected DateAdapter(Context mContext,Calendar mfromDate,Calendar mtoDate,ArrayList<String> totalWeeks, boolean is_single_select) {
        super(mContext, android.R.layout.simple_list_item_1, totalWeeks);

        this.inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.mContext = mContext;
        this.totalWeeks = totalWeeks;
        dateList = new ArrayList<>();
        monthList = new ArrayList<>();
        monthListValue = new ArrayList<>();
        yearListValue = new ArrayList<>();

        this.is_single_select = is_single_select;
        this.currentDate = Calendar.getInstance();

        this.fromDate = Calendar.getInstance();
        this.toDate = this.fromDate;

        if(mfromDate != null && mtoDate != null){
            this.fromDate = mfromDate;
            this.toDate = mtoDate;
        }else {
            setToInvalid(fromDate);
            setToInvalid(toDate);
        }

        new PopulateDatesTask().execute(new String[]{});


        bg_calendar_color_default = mContext.getResources().getColor(android.R.color.white);
        bg_calendar_color_selected = mContext.getResources().getColor(R.color.colorPrimary);
    }

    private void populateDatesInHash() {

        int today_diff_to_sunday = currentDate.get(Calendar.DAY_OF_WEEK) - 1;
        currentDate.add(Calendar.DATE, -today_diff_to_sunday);
        //Add empty space from sunday
        for (int i = 0; i < today_diff_to_sunday; i++) {
            dateList.add("-1");
            int month = currentDate.get(Calendar.MONTH);
            monthListValue.add(month);
            monthList.add(getMonthString(month));
            yearListValue.add(currentDate.get(Calendar.YEAR));
            currentDate.add(Calendar.DATE, 1);
        }
        addDates();
    }

    private void addDates(){
        int total_dates = totalWeeks.size() * 7;
        for (int i = 0; i < total_dates; i++) {
            int theDate = currentDate.get(Calendar.DATE);
            int theDay = currentDate.get(Calendar.DAY_OF_WEEK);
            int theMonth = currentDate.get(Calendar.MONTH);
            if (theDate == 1) {
                incrementFor7Days(theMonth, theDay);
            } else {
                dateList.add(theDate + "");
                monthListValue.add(theMonth);
                monthList.add("");
                yearListValue.add(currentDate.get(Calendar.YEAR));
                currentDate.add(Calendar.DATE, 1);
            }
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = setHolder(parent);
        }
        populateRowView(position, (ViewHolder) convertView.getTag());
        return convertView;
    }

    private void setToInvalid(Calendar calInst) {
        calInst.clear();
    }

    private static class ViewHolder {
        public Button button0;
        public Button button1;
        public Button button2;
        public Button button3;
        public Button button4;
        public Button button5;
        public Button button6;
        public Button button7;
    }

    private View setHolder(ViewGroup parent) {
        ViewHolder holder = new ViewHolder();
        View rowView = inflater.inflate(R.layout.item_date, parent, false);
        holder.button0 = (Button) rowView.findViewById(R.id.button0);
        holder.button1 = (Button) rowView.findViewById(R.id.button1);
        holder.button2 = (Button) rowView.findViewById(R.id.button2);
        holder.button3 = (Button) rowView.findViewById(R.id.button3);
        holder.button4 = (Button) rowView.findViewById(R.id.button4);
        holder.button5 = (Button) rowView.findViewById(R.id.button5);
        holder.button6 = (Button) rowView.findViewById(R.id.button6);
        holder.button7 = (Button) rowView.findViewById(R.id.button7);

        assignTouchListeners(holder.button1);
        assignTouchListeners(holder.button2);
        assignTouchListeners(holder.button3);
        assignTouchListeners(holder.button4);
        assignTouchListeners(holder.button5);
        assignTouchListeners(holder.button6);
        assignTouchListeners(holder.button7);

        // Set tag
        rowView.setTag(holder);
        return rowView;
    }

    private void assignTouchListeners(Button button) {
        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Integer date_position = (Integer) v.getTag();
                String theDateStr = dateList.get(date_position);
                if (theDateStr.equals("-1")) {
                    Log.e(TAG, "You have selected an invalid menu_date");
                    return;
                }
                Calendar date_selected = Calendar.getInstance();
                date_selected.set(yearListValue.get(date_position), monthListValue.get(date_position), Integer.parseInt(theDateStr), 0, 0, 0);
                date_selected.set(Calendar.MILLISECOND, 0);

                if (fromDate.getTimeInMillis() < 0 || date_selected.before(fromDate)) {
                    fromDate = date_selected;
                    toDate = date_selected;
                } else if ((date_selected.after(toDate) || date_selected.after(fromDate)) && !is_single_select) {
                    toDate = date_selected;
                } else if ((date_selected.after(toDate) || date_selected.after(fromDate)) && is_single_select) {
                    fromDate = date_selected;
                    toDate = date_selected;
                } else if (date_selected.equals(fromDate)) {
                    fromDate.clear();
                    toDate.clear();
                }
                dListener.onDateSet(fromDate);
                goAndChangeColors();
            }

        });
    }

    private void goAndChangeColors() {
        this.notifyDataSetChanged();
    }

    private void populateRowView(int position, ViewHolder holder) {
        if(monthList.isEmpty()){
            return;
        }

        int date_position = (position * 7) ;
        holder.button0.setText(monthList.get(date_position));

        assignColorsAndTags(holder.button1, date_position);
        assignColorsAndTags(holder.button2, date_position + 1);
        assignColorsAndTags(holder.button3, date_position + 2);
        assignColorsAndTags(holder.button4, date_position + 3);
        assignColorsAndTags(holder.button5, date_position + 4);
        assignColorsAndTags(holder.button6, date_position + 5);
        assignColorsAndTags(holder.button7, date_position + 6);
    }

    private void incrementFor7Days(int monthVal, int theDay) {
        if (theDay != 1) {
            for (int i = 0; i < 7; i++) {
                dateList.add("-1");
                monthListValue.add(-1);
                monthList.add(getMonthString(monthVal));
                yearListValue.add(-1);
            }
        }

        dateList.add("1");
        monthListValue.add(monthVal);
        monthList.add(getMonthString(monthVal));
        yearListValue.add(currentDate.get(Calendar.YEAR));
        currentDate.add(Calendar.DATE, 1);
    }

    private void assignColorsAndTags(Button button, int date_position) {
        if(dateList.size() <= date_position){
            return;
        }

        String theDateStr = dateList.get(date_position);
        if (theDateStr.equals("-1")) {
            button.setText("");
        } else {
            button.setText(theDateStr);
        }
        button.setTag(date_position);

        Calendar date_selected = Calendar.getInstance();
        date_selected.set(yearListValue.get(date_position), monthListValue.get(date_position), Integer.parseInt(theDateStr), 0, 0, 0);
        date_selected.set(Calendar.MILLISECOND, 0);
        if (isSameDay(date_selected)) {
            button.setTextColor(getContext().getResources().getColor(android.R.color.white));
            button.setBackgroundColor(bg_calendar_color_selected);
        } else {
            button.setTextColor(getContext().getResources().getColor(R.color.body_text_1));
            button.setBackgroundColor(bg_calendar_color_default);
        }



    }

    private boolean isSameDay(Calendar date_selected) {

        System.out.println("date_selected ==== "+date_selected.get(Calendar.DATE)+"/"+date_selected.get(Calendar.MONTH));
        System.out.println("fromDate ==== "+fromDate.get(Calendar.DATE)+"/"+fromDate.get(Calendar.MONTH));
        System.out.println("toDate ==== "+toDate.get(Calendar.DATE)+"/"+toDate.get(Calendar.MONTH));

        if ((date_selected.after(fromDate) && date_selected.before(toDate)) || date_selected.equals(fromDate) || date_selected.equals(toDate)) {
            System.out.println("---------------------------------------------------------------------YESSS----------------------------------------------------------------------");
            return true;
        }
        return false;
    }

    public String lengthOfTrip(){
        long diff = toDate.getTimeInMillis() - fromDate.getTimeInMillis() ;
        long trip_length_val = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
        String trip_length;
        if( trip_length_val < 1){
            trip_length = "Single day";
        }else{
            trip_length = trip_length_val+1+" days";
        }
        return trip_length;
    }

    private String getMonthString(int month) {
        switch (month) {
            case 0:
                return "Jan";
            case 1:
                return "Feb";
            case 2:
                return "Mar";
            case 3:
                return "Apr";
            case 4:
                return "May";
            case 5:
                return "Jun";
            case 6:
                return "Jul";
            case 7:
                return "Aug";
            case 8:
                return "Sep";
            case 9:
                return "Oct";
            case 10:
                return "Nov";
            case 11:
                return "Dec";
            default:
                return "";
        }
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    public Calendar getFromDate() {
        fromDate.set(Calendar.HOUR_OF_DAY, 0);
        fromDate.set(Calendar.MINUTE, 0);
        return fromDate;
    }

    public Calendar getToDate() {
        toDate.set(Calendar.HOUR_OF_DAY, 23);
        toDate.set(Calendar.MINUTE, 59);
        return toDate;
    }

    public int getTotalWeeksCount() {
        return totalWeeks.size();
    }


    public void setFromDate(int year, int month, int day) {
        fromDate.set(year, month, day);
    }

    public interface DateSetListener {
        public void onDateSet(Calendar cal_instance);
    }

    public void setDateSetListener(DateSetListener dListener) {
        this.dListener = dListener;
    }

    private class PopulateDatesTask extends AsyncTask<String, Integer, Long> {
        protected Long doInBackground(String... urls) {
            populateDatesInHash();
            return 1l;
        }
        protected void onPostExecute(Long result) {
        }
    }



}
