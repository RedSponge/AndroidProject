package com.redsponge.mycoolapp.project.event;

import android.app.DatePickerDialog;
import android.util.Log;
import android.view.View;

import com.redsponge.mycoolapp.R;
import com.redsponge.mycoolapp.utils.AbstractActivity;

import java.util.Calendar;
import java.util.Date;

public class NewEventActivity extends AbstractActivity {

    @Override
    protected void initialize() {
        setContentView(R.layout.activity_new_event);
    }

    public void changeEventDate(View view) {
        Date current = Calendar.getInstance().getTime();
        Calendar calendar = Calendar.getInstance();
        Log.i("Date", calendar.get(Calendar.YEAR) + "," + calendar.get(Calendar.MONTH) + "," + calendar.get(Calendar.DAY_OF_MONTH));

        DatePickerDialog dpd = new DatePickerDialog(this, null, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        dpd.show();

    }
}
