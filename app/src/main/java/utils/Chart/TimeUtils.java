package utils.Chart;

import com.google.firebase.Timestamp;

import java.util.Calendar;

import models.Invoice;
import utils.FormatHelper;

public class TimeUtils {
    public static String[] monthNames = {
            "Tháng 1", "Tháng 2", "Tháng 3",
            "Tháng 4", "Tháng 5", "Tháng 6",
            "Tháng 7", "Tháng 8", "Tháng 9",
            "Tháng 10", "Tháng 11", "Tháng 12"};

    public static String[] monthValues = {
            "1", "2", "3",
            "4", "5", "6",
            "7", "8", "9",
            "10", "11", "12"};
    public static int getMonthInCalendar(int month) {
        int[] calendarMonths = {
                Calendar.JANUARY, Calendar.FEBRUARY, Calendar.MARCH,
                Calendar.APRIL, Calendar.MAY, Calendar.JUNE,
                Calendar.JULY, Calendar.AUGUST, Calendar.SEPTEMBER,
                Calendar.OCTOBER, Calendar.NOVEMBER, Calendar.DECEMBER};
        return calendarMonths[month - 1];
    }

    public static String getToday() {
//        12 tháng 08/2024

        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DATE);
        int month = calendar.get(Calendar.MONTH) + 1;
        int year = calendar.get(Calendar.YEAR);
        return (day + " tháng " + month + "/" + year);
    }

    public static Timestamp[] getDayRange(int year, int month) {
        Calendar startCalendar = Calendar.getInstance();
        startCalendar.set(year, getMonthInCalendar(month), 1, 0, 0, 0); // Set to 00:00:00 of the 1st day
        Timestamp startDate = new Timestamp(startCalendar.getTime());

        Calendar endCalendar = Calendar.getInstance();
        endCalendar.set(year, getMonthInCalendar(month), endCalendar.getActualMaximum(Calendar.DAY_OF_MONTH), 23, 59, 59); // Set to 23:59:59 of the last day
        Timestamp endDate = new Timestamp(endCalendar.getTime());

        return new Timestamp[]{startDate, endDate};
    }

    public static Timestamp[] getYearRange(int year) {
        Calendar startCalendar = Calendar.getInstance();
        startCalendar.set(year, Calendar.JANUARY, 1, 0, 0, 0);
        Timestamp startDate = new Timestamp(startCalendar.getTime());

        Calendar endCalendar = Calendar.getInstance();
        endCalendar.set(year, Calendar.DECEMBER, 31, 23, 59, 59);
        Timestamp endDate = new Timestamp(endCalendar.getTime());

        return new Timestamp[]{startDate, endDate};
    }
    public static String getMonthValue(int value) {
        if (value >= 1 && value <= 12) {
            return monthValues[value - 1];
        } else {
            return "";
        }
    }
    public static String getMonthName(int value) {
        if (value >= 1 && value <= 12) {
            return monthNames[value - 1];
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

    public static String setDateTimeByInvoice(Invoice invoice) {
        Timestamp time;
        switch (invoice.getStatus()) {
            case PENDING_CONFIRMATION:
                time = invoice.getCreatedAt();
                break;
            case PENDING_SHIPMENT:
                time = invoice.getConfirmedAt();
                break;
            case IN_TRANSIT:
                time = invoice.getShippedAt();
                break;
            case DELIVERED:
                time = invoice.getDeliveredAt();
                break;
            default:
                time = invoice.getCancelledAt();
                break;
        }
        return FormatHelper.formatDateTime(time);
    }

}
