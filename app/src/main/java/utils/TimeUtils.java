package utils;

public class TimeUtils {
    public static String[] calendarMonths = {
            "Tháng 1", "Tháng 2", "Tháng 3",
            "Tháng 4", "Tháng 5", "Tháng 6",
            "Tháng 7", "Tháng 8", "Tháng 9",
            "Tháng 10", "Tháng 11", "Tháng 12"};
    public static String getMonth(int value) {
        if (value >= 1 && value <= 12) {
            return calendarMonths[value - 1];
        } else {
            return "";
        }
    }

}
