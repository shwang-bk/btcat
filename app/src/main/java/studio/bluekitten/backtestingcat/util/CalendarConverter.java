package studio.bluekitten.backtestingcat.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class CalendarConverter {

    private static SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

    public static String toString(Calendar calendar){
        return format.format(calendar.getTime());
    }

    public static Calendar toCalendar(String string){
        Calendar calendar = Calendar.getInstance();
        try {
            calendar.setTime(format.parse(string));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return calendar;
    }

    public static Date toDate(String string){
        Calendar calendar = Calendar.getInstance();
        try {
            calendar.setTime(format.parse(string));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return calendar.getTime();
    }

    public static Date toDate(Calendar calendar){
        return calendar.getTime();
    }
}
