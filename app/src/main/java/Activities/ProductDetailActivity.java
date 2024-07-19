package Activities;

import static constants.keyName.PRODUCT_DESC;
import static constants.keyName.PRODUCT_NAME;
import static constants.keyName.PRODUCT_NEW_PRICE;
import static constants.keyName.PRODUCT_OLD_PRICE;
import static constants.keyName.STORE_NAME;
import static constants.toastMessage.INTERNET_ERROR;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.example.stores.R;
import com.example.stores.databinding.ActivityProductDetailBinding;
import com.example.stores.databinding.LayoutProductDetailBinding;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import Adapters.SliderAdapterForProductDetail;
import interfaces.GetDocumentCallback;
import models.Product;
import models.SliderItem;
import models.Store;

public class ProductDetailActivity extends AppCompatActivity {

    ActivityProductDetailBinding binding;
    private String productId;
    private String storeId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProductDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initUI();
        getBundle();
        setupProductInfo();
        setupStoreInfo();

        setupEvents();
    }

    private void getBundle() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            productId = bundle.getString("productId", null);
            storeId = bundle.getString("storeId", null);
        }
    }


    private void setupProductInfo() {
        binding.layoutProductsInfo.setVisibility(View.GONE);
        binding.progressBarProduct.setVisibility(View.VISIBLE);

        if (storeId != null && productId != null) {
            Product product = new Product();
            product.getProductDetail(storeId, productId, new GetDocumentCallback() {
                @Override
                public void onGetDataSuccess(Map<String, Object> data) {
                    binding.progressBarProduct.setVisibility(View.GONE);
                    binding.layoutProductsInfo.setVisibility(View.VISIBLE);
                    // setup product information
                    binding.txtTitle.setText((CharSequence) data.get(PRODUCT_NAME));
                    NumberFormat formatter = NumberFormat.getInstance(new Locale("vi", "VN"));
                    String formattedOldPrice = formatter.format(data.get(PRODUCT_OLD_PRICE));
                    binding.txtOldPrice.setText("đ" + formattedOldPrice);
                    binding.txtOldPrice.setPaintFlags(binding.txtOldPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

                    String formattedNewPrice = formatter.format(data.get(PRODUCT_NEW_PRICE));
                    binding.txtNewPrice.setText(formattedNewPrice);
                    binding.ratingBar.setRating(4.5F);
                    binding.txtRating.setText(4.5 + " / 5");
                    binding.txtSold.setText("Đã bán " + 200);
                    binding.txtProdctDescription.setText((CharSequence) data.get(PRODUCT_DESC));


                    // setup productImages
//                    ArrayList<SliderItem> sliderItems = new ArrayList<>();
//                    for (int i = 0; i < data.getProductImages().size(); i++) {
//                        sliderItems.add(new SliderItem(object.getProductImages().get(i)));
//                    }
//
//                    binding.viewPager2Slider.setAdapter(new SliderAdapterForProductDetail(ProductDetailActivity.this, sliderItems));
//                    binding.viewPager2Slider.setOffscreenPageLimit(2);
//                    binding.indicator.attachTo(binding.viewPager2Slider);
                }

                @Override
                public void onGetDataFailure(String errorMessage) {
                    Toast.makeText(ProductDetailActivity.this, INTERNET_ERROR, Toast.LENGTH_SHORT).show();
                }
            });

        }
    }


    private void setupStoreInfo() {
        binding.progressBarStore.setVisibility(View.VISIBLE);
        // lấy thông tin avatar, invoice
        if (storeId != null) {
            Store store = new Store();
            store.onGetStoreData(storeId, new GetDocumentCallback() {
                @Override
                public void onGetDataSuccess(Map<String, Object> data) {
                    binding.progressBarStore.setVisibility(View.GONE);
                    binding.txtStoreName.setText((CharSequence) data.get(STORE_NAME));

                    // set up UI avatar, invoice

                }

                @Override
                public void onGetDataFailure(String errorMessage) {
                    Toast.makeText(ProductDetailActivity.this, "Uiii, lỗi mạng rồi :(((", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
    private void setupEvents() {
        binding.btnAddToCart.setOnClickListener(v -> {

        });
        binding.imageBack.setOnClickListener(v -> finish());

        binding.iconCart.setOnClickListener(v -> {
            Intent intent = new Intent(ProductDetailActivity.this, CartActivity.class);
            startActivity(intent);
        });

        binding.txtProductDetail.setOnClickListener(v -> popUpProductDetailDialog());

        binding.btnViewStore.setOnClickListener(v -> {
            Intent intent = new Intent(ProductDetailActivity.this, ViewMyStoreActivity.class);
            startActivity(intent);
        });
    }

    private void popUpProductDetailDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutProductDetailBinding detailBinding = LayoutProductDetailBinding.inflate(getLayoutInflater());
        builder.setView(detailBinding.getRoot());
        AlertDialog dialog = builder.create();

        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawableResource(R.drawable.custom_edit_text_border);
        dialog.show();

        // Set the dialog window attributes
        Window window = dialog.getWindow();
        if (window != null) {
            window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            window.setGravity(Gravity.BOTTOM);

        }

        detailBinding.imageCancel.setOnClickListener(v -> dialog.dismiss());
        detailBinding.btnClose.setOnClickListener(v -> dialog.dismiss());
    }
    private void initUI() {
        getWindow().setStatusBarColor(Color.parseColor("#F04D7F"));
        getWindow().setNavigationBarColor(Color.parseColor("#EFEFEF"));
        Objects.requireNonNull(getSupportActionBar()).hide();
    }
}