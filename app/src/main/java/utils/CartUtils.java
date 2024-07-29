package utils;
import android.content.Context;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import models.CartItem;
import models.Product;

public class CartUtils {
    public static ArrayList<CartItem> MY_CART = new ArrayList<>();

    public static void showToast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }
    public static int getQuantityProductsInCart(){
        int count = 0;
        for (CartItem item : MY_CART) {
            count += item.getListProducts().size();
        }
        return count;
    }
    public static void updateQuantityInCart(TextView textView) {
        int quantity = getQuantityProductsInCart();
        String quantityText = (quantity < 10) ? ((quantity > 0) ?  ("0" + quantity) : ("0")) : String.valueOf(quantity);
        textView.setText(quantityText);
    }

    public static double getCartItemFee(CartItem cartItem) {
        double fee = 0;
        for (Product product : cartItem.getListProducts()) {
            fee += (product.getNewPrice()  * product.getNumberInCart());
        }
        return fee;
    }

    public static void clearMyCart() {
        MY_CART.clear();
    }
}

