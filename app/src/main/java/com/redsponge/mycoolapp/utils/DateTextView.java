package com.redsponge.mycoolapp.utils;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;

import com.redsponge.mycoolapp.R;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


public class DateTextView extends TextView {

    private DatePickerDialog dpd;
    private Calendar cal;
    private int year;
    private int month;
    private int day;
    private boolean allowPast;

    public DateTextView(Context context) {
        super(context);
        initialize();
    }

    public DateTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        getAttrs(attrs);
        initialize();
    }

    public DateTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        getAttrs(attrs);
        initialize();
    }

    private void getAttrs(AttributeSet attrs) {
        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.DateTextView);
        allowPast = a.getBoolean(R.styleable.DateTextView_allowPast, false);
        a.recycle();
    }

    private void initialize() {
        if(isInEditMode()) {
            this.setText(R.string.date_text_view_preview);
        } else {
            cal = Calendar.getInstance();

            setClickable(true);
            year = cal.get(Calendar.YEAR);
            month = cal.get(Calendar.MONTH);
            day = cal.get(Calendar.DAY_OF_MONTH);

            setDate(year, month, day);

            if(!allowPast) {
                dpd.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
            }

            setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    dpd.show();
                }
            });

            updateDisplay();
        }
    }

    public void setDate(int year, int month, int day) {
        this.day = day;
        this.month = month;
        this.year = year;

        dpd = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                setDate(year, month, dayOfMonth);
            }
        }, year, month, day);

        updateDisplay();
    }

    public void setDate(long time) {
        cal.setTimeInMillis(time);
        setDate(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
    }

    private void updateDisplay() {
        setText(String.format(Locale.ENGLISH, "%02d/%02d/%04d", day, month + 1, year));
    }

    public long getAsMilliseconds() {
        cal.set(year, month, day);
        return cal.getTimeInMillis();
    }
}
