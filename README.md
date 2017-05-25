# android_lib_date_from_to

android library to show and select date range in a single calendar view

# implementation

Use DateRangeHelper instance to open date picker dialog. Pass fromTime, toTime or null values for current date. Set the number of weeks to load as the last param.

    DateRangeHelper.getInstance(fromTime,toTime,20).openDialog(this,new DateDialogFragment.DateSelectedListener() {
        @Override
        public void OnDateSelected(DateAdapter updatedDateAdapter) {
               fromTime = updatedDateAdapter.getFromDate();
               toTime = updatedDateAdapter.getToDate();         
               date.setText(DateViewHelper.getDateRange(updatedDateAdapter.getFromDate().getTimeInMillis(),
               updatedDateAdapter.getToDate().getTimeInMillis()));
        }
    });
    

  
        
Also get the new instance of dateAdapter and bind that to any view using    

    DateRangeHelper.getInstance(fromTime,toTime,20).getNewDateAdapter(context,listener)
