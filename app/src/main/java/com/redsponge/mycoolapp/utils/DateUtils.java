package com.redsponge.mycoolapp.utils;

import java.util.Calendar;
import java.util.Locale;

public class DateUtils {

    private static Calendar cal = Calendar.getInstance(Locale.UK);

    public static String getFormatted(long date) {
        cal.setTimeInMillis(date);
        return String.format(Locale.UK, "%02d/%02d/%04d", cal.get(Calendar.DAY_OF_MONTH), cal.get(Calendar.MONTH), cal.get(Calendar.YEAR));
    }

}
