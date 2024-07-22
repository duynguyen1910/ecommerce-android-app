package Activities;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

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
import Adapters.CartAdapter;
import api.invoiceApi;
import interfaces.GetCollectionCallback;
import interfaces.ToTalFeeCallback;
import models.Invoice;
import models.InvoiceDetail;
import models.Product;
import models.Store;

public class CartActivity extends AppCompatActivity implements ToTalFeeCallback {
    ActivityCartBinding binding;
    CartAdapter cartAdapter;
    ArrayList<Product> listProductsInCart;
    ArrayList<Invoice> storeList;
    ArrayList<InvoiceDetail> productList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCartBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        initUI();
        initCartUI();
//        calculatorCart();
        setupEvents();
    }

    private void setupEvents(){
        binding.imageBack.setOnClickListener(v -> finish());

        binding.btnBuyNow.setOnClickListener(v -> {
                Intent intent = new Intent(CartActivity.this, PaymentActivity.class);
                ArrayList<InvoiceDetail> payment = groupCheckedProductsByStore();
//                intent.putExtra("payment", payment);
                startActivity(intent);
        });

        binding.imvHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               finish();
            }
        });
    }

    private ArrayList<InvoiceDetail> groupCheckedProductsByStore() {
        HashMap<String, ArrayList<Product>> hashMap = new HashMap<>();

        for (Invoice cartItem : storeList) {
//            for (Product product : cartItem.getListProducts()) {
//                if (product.getCheckedStatus()) {
//                    String storeName = cartItem.getStoreName();
//                    if (!hashMap.containsKey(storeName)){
//                        hashMap.put(storeName, new ArrayList<>());
//                    }
//                    Objects.requireNonNull(hashMap.get(storeName)).add(product);
//                }
//            }
        }
        // filter HashMap để tạo InvoiceDetail
        ArrayList<InvoiceDetail> cartItems = new ArrayList<>();
        for (Map.Entry<String, ArrayList<Product>> entry : hashMap.entrySet()) {
            // Tạo InvoiceDetail mới từ tên cửa hàng và danh sách sản phẩm
//            cartItems.add(new InvoiceDetail(entry.getKey(), entry.getValue()));
        }


        return cartItems;
    }

    private ArrayList<InvoiceDetail> groupProductsByStore() {
        HashMap<String, ArrayList<Product>> hashMap = new HashMap<>();
        for (Product product : listProductsInCart) {
            String storeId = product.getStoreId();
            if (!hashMap.containsKey(storeId)) {
                hashMap.put(storeId, new ArrayList<>());
            }
            Objects.requireNonNull(hashMap.get(storeId)).add(product);
        }

        ArrayList<InvoiceDetail> cartItems = new ArrayList<>();

        // filter HashMap để tạo InvoiceDetail
        for (Map.Entry<String, ArrayList<Product>> entry : hashMap.entrySet()) {
            String storeId = entry.getKey();
            ArrayList<Product> products = entry.getValue();

            // Tìm tên cửa hàng theo storeId
            String storeName = "";
//            for (Store store : listStores) {
//                if (store.getBaseId() == storeId) {
//                    storeName = store.getStoreName();
//                    break;
//                }
//            }

            // Tạo InvoiceDetail và thêm vào danh sách cartItems
//            InvoiceDetail cartItem = new InvoiceDetail(storeName, products);
//            cartItems.add(cartItem);
        }

        return cartItems;
    }


    private void initCartUI() {
        storeList = new ArrayList<>();
        invoiceApi invoiceApi = new invoiceApi();

        invoiceApi.getAllInvoices(new GetCollectionCallback<Invoice>() {
            @Override
            public void onGetListSuccess(ArrayList<Invoice> cartList) {
                if (!cartList.isEmpty()) {
                    binding.layoutEmptyCart.setVisibility(View.GONE);
                    binding.layoutCart.setVisibility(View.VISIBLE);

                    cartAdapter = new CartAdapter(CartActivity.this, cartList, new ToTalFeeCallback() {
                        @Override
                        public void totalFeeUpdate(double totalFee) {

                        }
                    });
                    binding.recyclerViewCart.setAdapter(cartAdapter);
                    binding.recyclerViewCart.setLayoutManager(new LinearLayoutManager(CartActivity.this, LinearLayoutManager.VERTICAL, false));
                } else {

                    binding.layoutEmptyCart.setVisibility(View.VISIBLE);
                    binding.layoutCart.setVisibility(View.GONE);
                }


            }

            @Override
            public void onGetListFailure(String errorMessage) {
                Toast.makeText(CartActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void initUI() {
        getWindow().setStatusBarColor(Color.parseColor("#F04D7F"));

        getWindow().setNavigationBarColor(Color.parseColor("#EFEFEF"));
        Objects.requireNonNull(getSupportActionBar()).hide();
    }


    private void calculatorCart() {
        int cartItemCount = storeList.size();
        binding.txtQuantityInCart.setText("Giỏ Hàng (" + getQuantityProductsIncart() + ")" );
        if (cartItemCount == 0) {
            binding.layoutEmptyCart.setVisibility(View.VISIBLE);
            binding.layoutCart.setVisibility(View.GONE);
        }
        double delivery = 0;
        double total = 0;
//        if (getTotalFee() != 0){
//            total = Math.round(getTotalFee() + delivery);
//        }

        NumberFormat formatter = NumberFormat.getInstance(new Locale("vi", "VN"));
        binding.txtTotal.setText("đ" + formatter.format(total));

    }
    private int getQuantityProductsIncart(){
        int count = 0;
        for (Invoice item : storeList) {
//            count += item.getListProducts().size();
        }
        return count;
    }




//    private double getTotalFee() {
//        double fee = 0;
//        for (InvoiceDetail item : storeList) {
//            for (Product product : item.getListProducts()) {
//                if (product.getCheckedStatus()){
//                    fee += (product.getNewPrice() * product.getNumberInCart());
//                }
//
//            }
//        }
//        binding.btnBuyNow.setText("Mua Hàng (" + getQuantityCheckedProducts() + ")" );
//        return fee;
//    }

//    private int getQuantityCheckedProducts() {
//        int count = 0;
//        for (InvoiceDetail item : storeList) {
//            for (Product product : item.getListProducts()) {
//                if (product.getCheckedStatus()){
//                    count += 1;
//                }
//
//            }
//        }
//        if (count > 0) {
//            binding.btnBuyNow.setFocusable(true);
//            binding.btnBuyNow.setBackground(ContextCompat.getDrawable(this, R.color.primary_color));
//        } else {
//            binding.btnBuyNow.setFocusable(false);
//            binding.btnBuyNow.setBackground(ContextCompat.getDrawable(this, R.color.darkgray));
//        }
//        binding.btnBuyNow.setText("Mua Hàng (" + count + ")" );
//        return count;
//    }


    @Override
    public void totalFeeUpdate(double totalFee) {
//        calculatorCart();
    }
}