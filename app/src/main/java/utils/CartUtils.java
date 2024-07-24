package utils;
import android.content.Context;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import models.CartItem;
import models.Product;

public class CartUtils {
    public static ArrayList<CartItem> MYCART = new ArrayList<>();

    public static void showToast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }
    public static int getQuantityProductsIncart(){
        int count = 0;
        for (CartItem item : MYCART) {
            count += item.getListProducts().size();
        }
        return count;
    }
    public static void updateQuantityInCart(TextView textView) {
        int quantity = getQuantityProductsIncart();
        String quantityText = (quantity < 10) ? ("0" + quantity) : String.valueOf(quantity); ;
        textView.setText(quantityText);
    }

    public static double getCartItemFee(CartItem cartItem) {
        double fee = 0;
        for (Product product : cartItem.getListProducts()) {
            fee += (product.getNewPrice()  * product.getNumberInCart());
        }
        return fee;
    }

}

