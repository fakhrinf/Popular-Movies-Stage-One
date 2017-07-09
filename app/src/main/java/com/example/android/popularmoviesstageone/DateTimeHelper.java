package com.example.android.popularmoviesstageone;

import android.content.Context;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by bembengcs on 6/30/2017.
 */

public class DateTimeHelper {

    private static Date getFormattedDate(String date, String format) throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);

        return simpleDateFormat.parse(date);
    }

    public static String getLocalizedDate(Context context, String date, String format)
            throws ParseException {
        DateFormat dateFormat = android.text.format.DateFormat.getDateFormat(context);

        return dateFormat.format(getFormattedDate(date, format));
    }

}
