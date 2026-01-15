package libgdx.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class DateUtils {

    private static final String DEFAULT_PATTERN = "yyyy/MM/dd HH:mm:ss.SSS";

    public static long getNowMillis() {
        return new Date().getTime();
    }

    public static Date getDate(String dateString) {
        dateString = addMillisToDateString(dateString);
        try {
            return createDateFormat().parse(dateString);
        } catch (ParseException e) {
            throw new RuntimeException();
        }
    }

    private static SimpleDateFormat createDateFormat() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DEFAULT_PATTERN, Locale.ENGLISH);
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("EET"));
        return simpleDateFormat;
    }

    private static String addMillisToDateString(String dateString) {
        dateString = dateString.contains(".") ? dateString : dateString + ".000";
        return dateString;
    }

    public static String getNowDateString() {
        return getDateString(new Date());
    }

    public static String getDateString(Date date) {
        return createDateFormat().format(date);
    }

    public static Date minusDays(int days) {
        return minusDays(new Date(), days);
    }

    public static Date minusDays(Date date, int days) {
        return minusTime(date, days, Calendar.DAY_OF_YEAR);
    }

    public static Date minusSeconds(int seconds) {
        return minusSeconds(new Date(), seconds);
    }

    public static Date minusSeconds(Date date, int seconds) {
        return minusTime(date, seconds, Calendar.SECOND);
    }

    public static Date minusMillis(int millis) {
        return minusMillis(new Date(), millis);
    }

    public static Date minusMillis(Date date, int millis) {
        return minusTime(date, millis, Calendar.MILLISECOND);
    }

    public static Date minusMinutes(int minutes) {
        return minusMinutes(new Date(), minutes);
    }

    public static Date minusMinutes(Date date, int minutes) {
        return minusTime(date, minutes, Calendar.MINUTE);
    }

    public static Date plusDays(Date date, int days) {
        return minusDays(date, -days);
    }

    private static Date minusTime(Date date, int amount, int type) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(date.getTime());
        cal.add(type, -amount);
        return cal.getTime();
    }
}
