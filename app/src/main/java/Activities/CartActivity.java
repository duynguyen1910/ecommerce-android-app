package Activities;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.stores.R;
import com.example.stores.databinding.ActivityCartBinding;
import com.example.stores.databinding.LayoutOrderBinding;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Objects;
import Adapters.CartAdapter;
import Models.Product;
import Service.EcommerceService;
import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;

public class CartActivity extends AppCompatActivity {

    ActivityCartBinding binding;
    CartAdapter cartAdapter;
    ArrayList<Product> cart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCartBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initUI();
        initCart();
        calculatorCart();

        binding.imageBack.setOnClickListener(v -> finish());

        binding.btnBuyNow.setOnClickListener(v -> {
            Intent intent = new Intent(CartActivity.this, EcommerceService.class);
            String data = "Đơn hàng của bạn đã được gửi đến người bán";
            intent.putExtra("data", data);
            startService(intent);
            showThankyouDialog();
        });

    }
    private void showThankyouDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutOrderBinding orderBinding = LayoutOrderBinding.inflate(getLayoutInflater());
        builder.setView(orderBinding.getRoot());
        AlertDialog dialog = builder.create();
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawableResource(R.drawable.custom_edit_text_border);
        dialog.show();

        orderBinding.imageCancel.setOnClickListener(v -> dialog.dismiss());
    }


    private void calculatorCart() {
        int cartItemCount = cart.size();
        if (cartItemCount == 0){
            binding.layoutEmptyCart.setVisibility(View.VISIBLE);
            binding.layoutCart.setVisibility(View.GONE);
        }
        double percentTax = 0.08;
        double delivery = 50000;
        double tax = (double) Math.round(getTotalFee() * percentTax);
        double total = Math.round(getTotalFee() + tax + delivery);
        NumberFormat formatter = NumberFormat.getInstance(new Locale("vi", "VN"));
        binding.txtTotal.setText("đ" + formatter.format(total));

    }
    private double getTotalFee(){

        double fee = 0;
        int listSize = cart.size();
        for (int i = 0; i< listSize; i++){
            fee = fee + cart.get(i).getPrice() * cart.get(i).getNumberInCart();
        }
        return fee;
    }


    private void initCartList() {

//        binding.progressBarProducts.setVisibility(View.VISIBLE);
        cart = new ArrayList<>();
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
        cart.add(new Product("Lovito Đầm chữ A phối ren hoa đơn giản dành cho nữ LNA38057", getResources().getResourceName(R.string.product_desc1), picUrls1, 149000, 298000, 4.9, 200, 50, 1));
        cart.add(new Product("Lovito Đầm trễ vai ngọc trai trơn đơn giản dành cho nữ L76AD154", getResources().getResourceName(R.string.product_desc1), picUrls2, 119000, 228000, 4.8, 179, 48, 2));
        cart.add(new Product("Đồng hồ điện tử Unisex không thông minh thời trang S8 phong cách mới", getResources().getResourceName(R.string.product_desc2), picUrls3, 49000, 70000, 4.5, 559, 30, 1));
        cart.add(new Product("Huizumei Váy preppy nữ mùa hè cổ polo nhỏ chắp vá eo nâng cao và giảm béo váy ngắn", getResources().getResourceName(R.string.product_desc3), picUrls4, 129000, 235000, 4.7, 989, 45,3));
//



    }


    private void initCart(){

        initCartList();
        if (!cart.isEmpty()){
            binding.layoutEmptyCart.setVisibility(View.GONE);
            binding.layoutCart.setVisibility(View.VISIBLE);

            cartAdapter = new CartAdapter(CartActivity.this, cart, this::calculatorCart);
            binding.recyclerViewCart.setAdapter(cartAdapter);
            binding.recyclerViewCart.setLayoutManager(new LinearLayoutManager(CartActivity.this, LinearLayoutManager.VERTICAL, false));
        }else {
            binding.layoutEmptyCart.setVisibility(View.VISIBLE);
            binding.layoutCart.setVisibility(View.GONE);
        }

        ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getBindingAdapterPosition();
                cart.remove(position);
                calculatorCart();
                cartAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                new RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                        .addSwipeLeftBackgroundColor(ContextCompat.getColor(CartActivity.this, R.color.primary_color))
                        .addActionIcon(R.drawable.ic_delete)
                        .setSwipeLeftLabelColor(ContextCompat.getColor(CartActivity.this, R.color.white))
                        .addSwipeLeftLabel("Delete")
                        .setIconHorizontalMargin(TypedValue.COMPLEX_UNIT_DIP, 16)
                        .addCornerRadius(TypedValue.COMPLEX_UNIT_DIP, 12)
                        .setSwipeLeftLabelTextSize(TypedValue.COMPLEX_UNIT_SP, 14)
                        .create()
                        .decorate();
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        };


        new ItemTouchHelper(simpleCallback).attachToRecyclerView(binding.recyclerViewCart);


    }

    private void initUI() {
        getWindow().setStatusBarColor(Color.parseColor("#F04D7F"));

        getWindow().setNavigationBarColor(Color.parseColor("#EFEFEF"));
        Objects.requireNonNull(getSupportActionBar()).hide();
    }
}