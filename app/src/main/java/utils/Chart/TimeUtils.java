package utils.Chart;

import java.util.Calendar;

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


    public static int getCurrentMonthValue() {
        return Calendar.getInstance().get(Calendar.MONTH) + 1;
    }
    public static int getCurrentYearValue() {
        return Calendar.getInstance().get(Calendar.YEAR);
    }

    public static String setMonthText( int year, int month) {
        return "Tháng " + month + " / " + year;
    }

}
