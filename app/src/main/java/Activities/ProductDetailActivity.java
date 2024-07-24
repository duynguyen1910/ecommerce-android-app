package Activities;

import static constants.keyName.PRODUCT_DESC;
import static constants.keyName.PRODUCT_INSTOCK;
import static constants.keyName.PRODUCT_NAME;
import static constants.keyName.PRODUCT_NEW_PRICE;
import static constants.keyName.PRODUCT_OLD_PRICE;
import static constants.keyName.STORE_NAME;
import static constants.toastMessage.INTERNET_ERROR;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.stores.R;
import com.example.stores.databinding.ActivityProductDetailBinding;
import com.example.stores.databinding.DialogProductImageExpandBinding;
import com.example.stores.databinding.DialogSelectVariantBinding;
import com.example.stores.databinding.LayoutProductDetailBinding;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

import Activities.StoreSetup.ViewMyStoreActivity;
import interfaces.GetDocumentCallback;
import models.InvoiceDetail;
import models.Product;
import models.Store;

public class ProductDetailActivity extends AppCompatActivity {

    ActivityProductDetailBinding binding;
    private String productId;
    private String storeId;
    Product thisProduct;
    private String storeName;

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
            product.getProductDetail(productId, new GetDocumentCallback() {
                @Override
                public void onGetDataSuccess(Map<String, Object> productDetail) {
                    thisProduct = new Product(
                            (String) productDetail.get(PRODUCT_NAME),
                            (String) productDetail.get(PRODUCT_DESC),
                            (double) productDetail.get(PRODUCT_NEW_PRICE),
                            (double) productDetail.get(PRODUCT_OLD_PRICE),
                            ((Long) productDetail.get(PRODUCT_INSTOCK)).intValue(),
                            storeId,
                            1);
                    binding.progressBarProduct.setVisibility(View.GONE);
                    binding.layoutProductsInfo.setVisibility(View.VISIBLE);
                    // setup product information
                    binding.txtTitle.setText(thisProduct.getProductName());
                    NumberFormat formatter = NumberFormat.getInstance(new Locale("vi", "VN"));
                    String formattedOldPrice = formatter.format(thisProduct.getOldPrice());
                    binding.txtOldPrice.setText("đ" + formattedOldPrice);
                    binding.txtOldPrice.setPaintFlags(binding.txtOldPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

                    String formattedNewPrice = formatter.format(thisProduct.getNewPrice());
                    binding.txtNewPrice.setText(formattedNewPrice);
                    binding.ratingBar.setRating(4.5F);
                    binding.txtRating.setText(4.5 + " / 5");
                    binding.txtSold.setText("Đã bán " + 200);
                    binding.txtProdctDescription.setText(thisProduct.getDescription());
                    binding.txtNewPriceInBuyNow.setText(formattedNewPrice);


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
            store.onGetStoreDetail(storeId, new GetDocumentCallback() {
                @Override
                public void onGetDataSuccess(Map<String, Object> data) {
                    storeName = (String) data.get(STORE_NAME);
                    binding.progressBarStore.setVisibility(View.GONE);
                    binding.txtStoreName.setText(storeName);

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
            // Khởi tạo UserApi update subCollection CART của Collection User


        });
        binding.imageBack.setOnClickListener(v -> finish());

        binding.iconCart.setOnClickListener(v -> {
            Intent intent = new Intent(ProductDetailActivity.this, CartActivity.class);
            startActivity(intent);
        });

        binding.txtProductDetail.setOnClickListener(v -> popUpProductDetailDialog());

        binding.btnViewStore.setOnClickListener(v -> {
            Intent intent = new Intent(ProductDetailActivity.this, ViewMyStoreActivity.class);
            intent.putExtra("storeId", storeId);
            startActivity(intent);
        });

        binding.layoutBuyNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popUpSelectVariantDialog();

            }
        });
    }


    private void popUpSelectVariantDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        DialogSelectVariantBinding variantBinding = DialogSelectVariantBinding.inflate(getLayoutInflater());
        builder.setView(variantBinding.getRoot());
        AlertDialog dialog = builder.create();

        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawableResource(R.drawable.custom_edit_text_border);
        dialog.show();


        Window window = dialog.getWindow();
        if (window != null) {
            window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            window.setGravity(Gravity.BOTTOM);

            Animation slideUp = AnimationUtils.loadAnimation(this, R.anim.slide_up);
            variantBinding.getRoot().startAnimation(slideUp);
        }
        NumberFormat formatter = NumberFormat.getInstance(new Locale("vi", "VN"));
        String formattedOldPrice = formatter.format(thisProduct.getOldPrice());
        variantBinding.txtOldPrice.setText("đ" + formattedOldPrice);
        variantBinding.txtOldPrice.setPaintFlags(binding.txtOldPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

        String formattedNewPrice = formatter.format(thisProduct.getNewPrice());

        variantBinding.txtNewPrice.setText(formattedNewPrice);
        variantBinding.txtInStock.setText("Kho: " + thisProduct.getInStock());

        //reset số lượng sau mỗi lần popup dialog
        thisProduct.setNumberInCart(1);
        variantBinding.txtQuantity.setText(String.valueOf(thisProduct.getNumberInCart()));

        variantBinding.btnPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int quantity = thisProduct.getNumberInCart();
                if (quantity < 99) {
                    thisProduct.setNumberInCart(quantity + 1);
                    variantBinding.txtQuantity.setText(String.valueOf(thisProduct.getNumberInCart()));
                }

            }
        });

        variantBinding.imageExpand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popUpProductImageExpandDialog();
            }
        });
        variantBinding.btnMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int quantity = thisProduct.getNumberInCart();
                if (quantity > 1) {
                    thisProduct.setNumberInCart(quantity - 1);
                    variantBinding.txtQuantity.setText(String.valueOf(thisProduct.getNumberInCart()));
                }

            }
        });

        variantBinding.imageClose.setOnClickListener(v -> dialog.dismiss());

        variantBinding.btnBuyNow.setOnClickListener(v -> {
            if (thisProduct.getNumberInCart() > thisProduct.getInStock()) {
                Toast.makeText(ProductDetailActivity.this, "Uiii, số lượng sản phẩm không đủ!", Toast.LENGTH_SHORT).show();
            } else {
                Intent intent = new Intent(ProductDetailActivity.this, PaymentActivity.class);
                ArrayList<InvoiceDetail> payment = new ArrayList<>();
//                InvoiceDetail cartItem = new InvoiceDetail();
                ArrayList<Product> listProducts = new ArrayList<>();

                listProducts.add(thisProduct);
//                cartItem.setStoreName(storeName);
//                cartItem.setListProducts(listProducts);
//                payment.add(cartItem);

                intent.putExtra("payment", payment);
                startActivity(intent);
            }

        });
    }


    private void popUpProductImageExpandDialog() {
        Dialog dialog = new Dialog(this, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        DialogProductImageExpandBinding dialogBinding = DialogProductImageExpandBinding.inflate(getLayoutInflater());
        dialog.setContentView(dialogBinding.getRoot());
        dialog.show();

        Animation slideUp = AnimationUtils.loadAnimation(this, R.anim.slide_up);
        dialogBinding.getRoot().startAnimation(slideUp);

        dialogBinding.imageClose.setOnClickListener(v -> dialog.dismiss());
    }



    private void popUpProductDetailDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutProductDetailBinding detailBinding = LayoutProductDetailBinding.inflate(getLayoutInflater());
        builder.setView(detailBinding.getRoot());
        AlertDialog dialog = builder.create();

        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawableResource(R.drawable.custom_edit_text_border);
        dialog.show();


        Window window = dialog.getWindow();
        if (window != null) {
            window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            window.setGravity(Gravity.BOTTOM);

            Animation slideUp = AnimationUtils.loadAnimation(this, R.anim.slide_up);
            detailBinding.getRoot().startAnimation(slideUp);
        }


        detailBinding.btnClose.setOnClickListener(v -> dialog.dismiss());
    }

    private void initUI() {
        getWindow().setStatusBarColor(Color.parseColor("#F04D7F"));
        getWindow().setNavigationBarColor(Color.parseColor("#EFEFEF"));
        Objects.requireNonNull(getSupportActionBar()).hide();
    }
}