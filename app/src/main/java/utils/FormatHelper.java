package utils;

import com.google.firebase.Timestamp;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Pattern;

public class FormatHelper {
    private static Pattern DATE_PATTERN = Pattern.compile("dd-MM-yyyy | HH:mm");

    public static Timestamp getCurrentDateTime() {
        return new Timestamp(new java.util.Date());
    }

    public static String formatDateTime(Timestamp timestamp) {
        if(timestamp == null) return "";

        Date date = timestamp.toDate();
        SimpleDateFormat sdf = new SimpleDateFormat(String.valueOf(DATE_PATTERN), Locale.getDefault());
        String formattedDate = sdf.format(date);

        return formattedDate;
    }

    public static String formatVND(double money) {
        NumberFormat formatter = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
        return formatter.format(money);
    }

    public static String formatVND(String money) {
        try {
            // Loại bỏ ký tự không cần thiết và chuyển đổi thành số
            money = money.replace(",", "").replace("₫", "").trim();
            double amount = Double.parseDouble(money);

            // Sử dụng DecimalFormat để định dạng số
            DecimalFormat formatter = new DecimalFormat("#,###");
            return formatter.format(amount) + "₫";
        } catch (NumberFormatException e) {
            // Xử lý khi không thể chuyển đổi chuỗi thành số
            return money;
        }
    }

}
