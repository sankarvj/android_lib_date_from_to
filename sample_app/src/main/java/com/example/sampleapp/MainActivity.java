package com.example.sampleapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.date_from_to_presenter.adapter.DateAdapter;
import com.example.date_from_to_presenter.adapter.DateDialogFragment;
import com.example.date_from_to_presenter.adapter.DateRangeHelper;
import com.example.date_from_to_presenter.util.DateViewHelper;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    private TextView date;
    private Calendar fromTime = null;
    private Calendar toTime = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        date = (TextView) findViewById(R.id.date);
    }


    public void fireDatePicker(View v) {
        //In case if you want to increase the total no of weeks shown. Add them here
        DateRangeHelper.getInstance(fromTime,toTime,2).openDialog(this,dateSelectedListener);

    }

    DateDialogFragment.DateSelectedListener dateSelectedListener = new DateDialogFragment.DateSelectedListener() {
        @Override
        public void OnDateSelected(DateAdapter updatedDateAdapter) {
            fromTime = updatedDateAdapter.getFromDate();
            toTime = updatedDateAdapter.getToDate();
            date.setText(DateViewHelper.getDateRange(updatedDateAdapter.getFromDate().getTimeInMillis(),updatedDateAdapter.getToDate().getTimeInMillis()));
        }
    };
}
