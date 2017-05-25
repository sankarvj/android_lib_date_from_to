package com.example.date_from_to_presenter.adapter;

import android.app.Dialog;
import android.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatButton;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;


import com.example.date_from_to_presenter.R;
import com.example.date_from_to_presenter.util.Constants;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DateDialogFragment} factory method to
 * create an instance of this fragment.
 */
public class DateDialogFragment extends DialogFragment implements DateAdapter.DateSetListener  {

    DateAdapter dateAdapter;
    Calendar cal_instance;
    AppCompatButton action_done;
    DateSelectedListener dListener;

    Calendar fromtime;
    Calendar totime;
    ArrayList<String> totalWeeks;

    protected static DateDialogFragment newInstance(Calendar fromtime,Calendar totime,ArrayList<String> weeks) {
        DateDialogFragment fragment = new DateDialogFragment();
        Bundle args = new Bundle();
        args.putSerializable(Constants.ARG_FROM_TIME, fromtime);
        args.putSerializable(Constants.ARG_TO_TIME,totime);
        args.putStringArrayList(Constants.ARG_TOTAL_WEEKS_COUNT,weeks);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        View v = inflater.inflate(R.layout.layout_datepicker, null);
        builder.setView(v);
        onViewCreated(v.findViewById(R.id.date_layout), savedInstanceState);
        return builder.create();
    }

    @Override
    public void onResume() {
//        ViewGroup.LayoutParams params = getDialog().getWindow().getAttributes();
//        params.width = LinearLayout.LayoutParams.MATCH_PARENT;
//        params.height = 800;
//        getDialog().getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);
        super.onResume();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        fromtime = (Calendar) getArguments().getSerializable(Constants.ARG_FROM_TIME);
        totime = (Calendar)getArguments().getSerializable(Constants.ARG_TO_TIME);
        totalWeeks = getArguments().getStringArrayList(Constants.ARG_TOTAL_WEEKS_COUNT);

        action_done = (AppCompatButton)view.findViewById(R.id.action_done);
        populateDateView(view);

        action_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().dismiss();
                dListener.OnDateSelected(dateAdapter);
            }
        });
    }
    @Override
    public void onDateSet(Calendar cal_instance) {
        action_done.setSupportBackgroundTintList(getResources().getColorStateList(R.color.highlight_list_selected));
        this.cal_instance = cal_instance;
        action_done.setText(dateAdapter.lengthOfTrip());
    }

    private void populateDateView(View parent_view) {
        dateAdapter = new DateAdapter(this.getContext(),fromtime,totime,totalWeeks,false);
        ((ListView)parent_view.findViewById(R.id.from_list)).setAdapter(dateAdapter);
        dateAdapter.setDateSetListener(this);
    }

    public interface DateSelectedListener {
        public void OnDateSelected(DateAdapter dateAdapter);
    }

    public void setDateSetListener(DateSelectedListener dListener) {
        this.dListener = dListener;
    }



}
