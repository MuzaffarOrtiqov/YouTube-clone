package uz.urinov.youtube.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class MapperUtil {

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static String getStringValue(Object o) {
        return o == null ? null : String.valueOf(o);
    }

    public static <T> T getEnumValue(T o) {
        return o == null ? null : (T) String.valueOf(o);
    }


    public static Double getDoubleValue(Object o) {
        return o == null ? null : (Double) o;
    }

    public static Long getLongValue(Object o) {
        return o == null ? null : (Long) o;
    }

    public static Integer getIntegerValue(Object o) {
        return o == null ? null : (Integer) o;
    }

    public static LocalDateTime getLocalDateValue(Object o) {
        return o == null ? null : LocalDateTime.parse(String.valueOf(o).substring(0, 19), formatter);
    }
}
