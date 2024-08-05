package Activities.BuyProduct;
import static constants.keyName.PAYMENT;
import static utils.Cart.CartUtils.MY_CART;
import static utils.Cart.CartUtils.getQuantityProductsInCart;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.core.content.ContextCompat;

import com.example.stores.R;
import com.example.stores.databinding.ActivityCartBinding;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

import Activities.MainActivity;
import Adapters.BuyProduct.CartAdapter;
import interfaces.InAdapter.ToTalFeeCallback;
import models.CartItem;
import models.Product;

public class CartActivity extends AppCompatActivity implements ToTalFeeCallback {
    ActivityCartBinding binding;
    CartAdapter cartAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCartBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initUI();
        setupCart();
        calculatorCart();

        setupEvents();
    }

    private void setupEvents(){
        binding.imageBack.setOnClickListener(v -> finish());

        binding.btnBuyNow.setOnClickListener(v -> {
            if (getQuantityCheckedProducts() > 0){
                Intent intent = new Intent(CartActivity.this, PaymentActivity.class);
                ArrayList<CartItem> payment = setupPayment();

                intent.putExtra(PAYMENT, payment);
                startActivity(intent);
            }
        });

        binding.imvHome.setOnClickListener(v -> {
            Intent intent = new Intent(CartActivity.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        });
    }

    public static ArrayList<CartItem> setupPayment() {
        HashMap<String, HashMap<String, ArrayList<Product>>> hashMap = new HashMap<>();

        for (CartItem cartItem : MY_CART) {
            String storeName = cartItem.getStoreName();
            String storeID = cartItem.getStoreID();

            if (!hashMap.containsKey(storeName)) {
                hashMap.put(storeName, new HashMap<>());
            }

            if (!hashMap.get(storeName).containsKey(storeID)) {
                Objects.requireNonNull(hashMap.get(storeName)).put(storeID, new ArrayList<>());
            }

            for (Product product : cartItem.getListProducts()) {
                if (product.getCheckedStatus()) {
                    Objects.requireNonNull(hashMap.get(storeName)).get(storeID).add(product);
                }
            }
        }

        // filter HashMap để tạo CartItem
        ArrayList<CartItem> cartItems = new ArrayList<>();
        for (Map.Entry<String, HashMap<String, ArrayList<Product>>> storeEntry : hashMap.entrySet()) {
            for (Map.Entry<String, ArrayList<Product>> entry : storeEntry.getValue().entrySet()) {
                String storeName = storeEntry.getKey();
                String storeID = entry.getKey();
                ArrayList<Product> products = entry.getValue();
                // Tạo CartItem mới từ tên cửa hàng, storeID và danh sách sản phẩm
                cartItems.add(new CartItem(storeID, storeName, products));
            }
        }

        return cartItems;
    }



    private void setupCart() {
        if (!MY_CART.isEmpty()) {
            binding.layoutEmptyCart.setVisibility(View.GONE);
            binding.layoutCart.setVisibility(View.VISIBLE);
            cartAdapter = new CartAdapter(CartActivity.this, MY_CART, CartActivity.this);
            binding.recyclerViewCart.setAdapter(cartAdapter);
            binding.recyclerViewCart.setLayoutManager(new LinearLayoutManager(CartActivity.this, LinearLayoutManager.VERTICAL, false));
        } else {
            binding.layoutEmptyCart.setVisibility(View.VISIBLE);
            binding.layoutCart.setVisibility(View.GONE);
        }
    }

    private void initUI() {
        getWindow().setStatusBarColor(Color.parseColor("#F04D7F"));
        getWindow().setNavigationBarColor(Color.parseColor("#EFEFEF"));
        Objects.requireNonNull(getSupportActionBar()).hide();
    }

    private void calculatorCart() {
        int cartItemCount = MY_CART.size();
        binding.txtQuantityInCart.setText("Giỏ Hàng (" + getQuantityProductsInCart() + ")");
        if (cartItemCount == 0) {
            binding.layoutEmptyCart.setVisibility(View.VISIBLE);
            binding.layoutCart.setVisibility(View.GONE);
        }
        double delivery = 0;
        double total = 0;
        if (getTotalFee() != 0){
            total = Math.round(getTotalFee() + delivery);
        }

        NumberFormat formatter = NumberFormat.getInstance(new Locale("vi", "VN"));
        binding.txtTotal.setText("đ" + formatter.format(total));

    }



    private double getTotalFee() {
        double fee = 0;
        for (CartItem item : MY_CART) {
            for (Product product : item.getListProducts()) {
                if (product.getCheckedStatus()){
                    fee += (product.getNewPrice() * product.getNumberInCart());
                }

            }
        }
        binding.btnBuyNow.setText("Mua Hàng (" + getQuantityCheckedProducts() + ")" );
        return fee;
    }

    private int getQuantityCheckedProducts() {
        int count = 0;
        for (CartItem item : MY_CART) {
            for (Product product : item.getListProducts()) {
                if (product.getCheckedStatus()){
                    count += 1;
                }

            }
        }
        if (count > 0) {
            binding.btnBuyNow.setFocusable(true);
            binding.btnBuyNow.setBackground(ContextCompat.getDrawable(this, R.color.primary_color));
        } else {
            binding.btnBuyNow.setFocusable(false);
            binding.btnBuyNow.setBackground(ContextCompat.getDrawable(this, R.color.darkgray));
        }
        binding.btnBuyNow.setText("Mua Hàng (" + count + ")" );
        return count;
    }


    @Override
    public void totalFeeUpdate(double totalFee) {
        calculatorCart();
    }
}