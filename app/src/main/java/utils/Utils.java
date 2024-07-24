package utils;
import android.content.Context;
import android.widget.Toast;
import java.util.ArrayList;
import models.CartItem;

public class Utils {
    public static ArrayList<CartItem> MYCART = new ArrayList<>();

    public static void showToast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }
}
