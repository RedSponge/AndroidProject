package com.redsponge.mycoolapp.utils;

import android.app.DatePickerDialog;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Locale;


public class DateTextView extends TextView {

    private DatePickerDialog dpd;
    private Calendar cal;
    private int year;
    private int month;
    private int day;

    public DateTextView(Context context) {
        super(context);
        initialize();
    }

    public DateTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize();
    }

    public DateTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialize();
    }

    private void initialize() {
        cal = Calendar.getInstance();
        setClickable(true);
        year = cal.get(Calendar.YEAR);
        month = cal.get(Calendar.MONTH);
        day = cal.get(Calendar.DAY_OF_MONTH);

        dpd = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                setDate(year, month, dayOfMonth);
            }
        }, year, month, day);

        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dpd.show();
            }
        });

        updateDisplay();
    }

    public void setDate(int year, int month, int day) {
        this.day = day;
        this.month = month;
        this.year = year;

        updateDisplay();
    }

    private void updateDisplay() {
        setText(String.format(Locale.ENGLISH, "%2d/%2d/%4d", day, month, year));
    }
}
