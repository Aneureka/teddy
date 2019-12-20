package util;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

/**
 * @author Aneureka
 * @createdAt 2019-12-20 10:14
 * @description
 **/
public class DateTimeUtil {

    public static String dateTimeToGMTString(LocalDateTime localDateTime) {
        if (localDateTime == null) {
            throw new NullPointerException("localDateTime is null");
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEE, dd MMM yyyy HH:mm:ss 'GMT'");
        return localDateTime.atOffset(ZoneOffset.UTC).format(formatter);
    }
}
