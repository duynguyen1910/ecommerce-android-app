package Activities;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.example.stores.R;
import com.example.stores.databinding.ActivityCartBinding;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import Adapters.CartAdapter;
import Models.CartItem;
import Models.Product;
import Models.Store;

public class CartActivity extends AppCompatActivity implements ToTalFeeCallback {

    ActivityCartBinding binding;
    CartAdapter cartAdapter;
    ArrayList<Product> listProductsInCart;
    ArrayList<Store> listStores;
    ArrayList<CartItem> cart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCartBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initUI();
        initCartUI();
        calculatorCart();


        setupEvents();
    }

    private void setupEvents(){
        binding.imageBack.setOnClickListener(v -> finish());

        binding.btnBuyNow.setOnClickListener(v -> {
            if (getQuantityCheckedProducts() > 0){
                Intent intent = new Intent(CartActivity.this, PaymentActivity.class);
                startActivity(intent);
            }

        });
    }


    private void initCart() {

        listStores = new ArrayList<>();
        listStores.add(new Store(1, "Zozo_Unisex", "TP. Hồ Chí Minh", "https://down-bs-vn.img.susercontent.com/fd234f3899f07b72e9c5e5e26f9d997d_tn.webp"));
        listStores.add(new Store(2, "LOVITO OFFICIAL STORE", "TP. Hồ Chí Minh", "https://down-bs-vn.img.susercontent.com/f87c39a4a3702cd4cb149cacd8114a0b_tn.webp"));
        listStores.add(new Store(3, "SANDAshop.vn", "Hà Nội", "https://down-bs-vn.img.susercontent.com/ac5556f336029ae92a1058195f2d4e56_tn.webp"));

//        binding.progressBarProducts.setVisibility(View.VISIBLE);
        listProductsInCart = new ArrayList<>();
        ArrayList<String> picUrls1 = new ArrayList<>();
        picUrls1.add("https://down-vn.img.susercontent.com/file/sg-11134201-7rbmn-lqld5fts53pj7e");
        picUrls1.add("https://down-vn.img.susercontent.com/file/sg-11134201-7rbl4-lqld5gi75d2e60");
        picUrls1.add("https://down-vn.img.susercontent.com/file/sg-11134201-7rbmc-lqld5e0asxgg61");
        picUrls1.add("https://down-vn.img.susercontent.com/file/sg-11134201-7rbk4-lqlpjd5622bp5d");

        ArrayList<String> picUrls2 = new ArrayList<>();
        picUrls2.add("https://down-vn.img.susercontent.com/file/sg-11134201-7rccv-ls294ot5axpz02");
        picUrls2.add("https://down-vn.img.susercontent.com/file/sg-11134201-7rcdn-ls294pp208mn48");
        picUrls2.add("https://down-vn.img.susercontent.com/file/sg-11134201-7rcdp-ls294qoujut336");
        picUrls2.add("https://down-vn.img.susercontent.com/file/sg-11134201-7rcev-ls294uaz9fcv33");


        ArrayList<String> picUrls3 = new ArrayList<>();
        picUrls3.add("https://down-vn.img.susercontent.com/file/cn-11134301-7r98o-lozhxkvh2rbr86");
        picUrls3.add("https://down-vn.img.susercontent.com/file/cn-11134301-7r98o-lozhxkw11xlo75");
        picUrls3.add("https://down-vn.img.susercontent.com/file/cn-11134301-7r98o-lozhxkvh6z13cb");
        picUrls3.add("https://down-vn.img.susercontent.com/file/cn-11134301-7r98o-lozhxkvh5kgn8a");

        ArrayList<String> picUrls4 = new ArrayList<>();
        picUrls4.add("https://down-vn.img.susercontent.com/file/sg-11134201-7rcck-lsaqdevfanrq8f");
        picUrls4.add("https://down-vn.img.susercontent.com/file/sg-11134201-7rce5-lsaqdhlxbx002b");
        picUrls4.add("https://down-vn.img.susercontent.com/file/sg-11134201-7rcen-lsaqdfgsfeeqa7");
        picUrls4.add("https://down-vn.img.susercontent.com/file/sg-11134201-7rcfb-lsaqdfxpqmpo1c");

//        Product(String title, String description, ArrayList<String> picUrl, double price, double rating, int sold)
        listProductsInCart.add(new Product("Lovito Đầm chữ A phối ren hoa đơn giản dành cho nữ LNA38057", getResources().getResourceName(R.string.product_desc1), picUrls1, 149000, 298000, 4.9, 200, 50, 1, 1, false));
        listProductsInCart.add(new Product("Lovito Đầm trễ vai ngọc trai trơn đơn giản dành cho nữ L76AD154", getResources().getResourceName(R.string.product_desc1), picUrls2, 119000, 228000, 4.8, 179, 48, 2, 2, false));
        listProductsInCart.add(new Product("Đồng hồ điện tử Unisex không thông minh thời trang S8 phong cách mới", getResources().getResourceName(R.string.product_desc2), picUrls3, 49000, 70000, 4.5, 559, 30, 3, 1, false));
        listProductsInCart.add(new Product("Huizumei Váy preppy nữ mùa hè cổ polo nhỏ chắp vá eo nâng cao và giảm béo váy ngắn", getResources().getResourceName(R.string.product_desc3), picUrls4, 129000, 235000, 4.7, 989, 45, 1, 3, false));
//

        cart = groupProductsByStore();


    }

    private ArrayList<CartItem> groupProductsByStore() {
        HashMap<Integer, ArrayList<Product>> hashMap = new HashMap<>();
        for (Product product : listProductsInCart) {
            int storeId = product.getStoreID();
            if (!hashMap.containsKey(storeId)) {
                hashMap.put(storeId, new ArrayList<>());
            }
            Objects.requireNonNull(hashMap.get(storeId)).add(product);
        }

        ArrayList<CartItem> cartItems = new ArrayList<>();

        // filter HashMap để tạo CartItem
        for (Map.Entry<Integer, ArrayList<Product>> entry : hashMap.entrySet()) {
            int storeId = entry.getKey();
            ArrayList<Product> products = entry.getValue();

            // Tìm tên cửa hàng theo storeId
            String storeName = "";
            for (Store store : listStores) {
                if (store.getStoreID() == storeId) {
                    storeName = store.getStoreName();
                    break;
                }
            }

            // Tạo CartItem và thêm vào danh sách cartItems
            CartItem cartItem = new CartItem(storeId, storeName, products);
            cartItems.add(cartItem);
        }

        return cartItems;
    }


    private void initCartUI() {

        initCart();
        if (!cart.isEmpty()) {
            binding.layoutEmptyCart.setVisibility(View.GONE);
            binding.layoutCart.setVisibility(View.VISIBLE);

            cartAdapter = new CartAdapter(CartActivity.this, cart, CartActivity.this);
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
        int cartItemCount = cart.size();
        binding.txtQuantityInCart.setText("Giỏ Hàng (" + getQuantityProductsIncart() + ")" );
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
    private int getQuantityProductsIncart(){
        int count = 0;
        for (CartItem item : cart) {
            count += item.getListProducts().size();
        }
        return count;
    }




    private double getTotalFee() {
        double fee = 0;
        for (CartItem item : cart) {
            for (Product product : item.getListProducts()) {
                if (product.getCheckedStatus()){
                    fee += product.getPrice() * product.getNumberInCart();
                }

            }
        }
        binding.btnBuyNow.setText("Mua Hàng (" + getQuantityCheckedProducts() + ")" );
        return fee;
    }

    private int getQuantityCheckedProducts() {
        int count = 0;
        for (CartItem item : cart) {
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