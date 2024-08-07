package Activities.BuyProduct;

import static constants.keyName.PRODUCT_DESC;
import static constants.keyName.PRODUCT_ID;
import static constants.keyName.PRODUCT_IMAGES;
import static constants.keyName.PRODUCT_INSTOCK;
import static constants.keyName.PRODUCT_NAME;
import static constants.keyName.PRODUCT_NEW_PRICE;
import static constants.keyName.PRODUCT_OLD_PRICE;
import static constants.keyName.STORE_ID;
import static constants.keyName.STORE_NAME;
import static constants.keyName.USER_ID;
import static constants.keyName.USER_INFO;
import static constants.toastMessage.INTERNET_ERROR;

import static utils.Cart.CartUtils.MY_CART;
import static utils.Cart.CartUtils.showToast;
import static utils.Cart.CartUtils.updateQuantityInCart;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;

import com.bumptech.glide.Glide;
import com.example.stores.R;
import com.example.stores.databinding.ActivityProductDetailBinding;
import com.example.stores.databinding.DialogAddToCartBinding;
import com.example.stores.databinding.DialogProductImageExpandBinding;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

import Activities.LoginActivity;
import Activities.StoreSetup.ViewMyStoreActivity;
import Adapters.BuyProduct.SliderAdapterForProductDetail;
import Adapters.BuyProduct.VariantGridAdapter;
import api.variantApi;
import interfaces.GetCollectionCallback;
import interfaces.GetDocumentCallback;
import interfaces.InAdapter.ItemSelectionListener;
import models.CartItem;
import models.Product;
import models.SliderItem;
import models.Store;
import models.Variant;
import utils.Cart.CartUtils;
import utils.FormatHelper;

public class ProductDetailActivity extends AppCompatActivity {

    private ActivityProductDetailBinding binding;
    private String productID;
    private String storeID;
    private String storeName;
    private boolean buyable;
    private Product currentProduct;
    private int g_selectedPos = -1;
    private int g_totalInstock = 0;
    private Variant g_selectedVariant = null;
    ArrayList<Variant> g_variants = new ArrayList<>();
    String tagVariant = "variant6";


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

    @Override
    protected void onResume() {
        super.onResume();
        CartUtils.updateQuantityInCart(binding.txtQuantityInCart);
    }

    private void getBundle() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            productID = bundle.getString(PRODUCT_ID, null);
            storeID = bundle.getString(STORE_ID, null);
            buyable = bundle.getBoolean("buyable");

            if (!buyable) {
                binding.btnAddToCart.setBackground(ContextCompat.getDrawable(this, R.color.gray));
                binding.layoutBuyNow.setBackground(ContextCompat.getDrawable(this, R.color.darkgray));
                binding.txtAddToCart.setTextColor(ContextCompat.getColor(this, R.color.black));
                Drawable drawable = ContextCompat.getDrawable(this, R.drawable.ic_cart);
                drawable.setColorFilter(ContextCompat.getColor(this, R.color.black), PorterDuff.Mode.SRC_IN);
                binding.viewAnimation.setImageDrawable(drawable);
            }

        }
    }


    private void setupProductInfo() {
        binding.layoutProductsInfo.setVisibility(View.GONE);
        binding.progressBarProduct.setVisibility(View.VISIBLE);

        if (storeID != null && productID != null) {
            Product product = new Product();
            product.getProductDetail(productID, new GetDocumentCallback() {
                @Override
                public void onGetDataSuccess(Map<String, Object> productDetail) {
                    currentProduct = new Product(
                            (String) productDetail.get(PRODUCT_NAME),
                            (ArrayList<String>) productDetail.get(PRODUCT_IMAGES),
                            (String) productDetail.get(PRODUCT_DESC),
                            (double) productDetail.get(PRODUCT_NEW_PRICE),
                            (double) productDetail.get(PRODUCT_OLD_PRICE),
                            ((Long) productDetail.get(PRODUCT_INSTOCK)).intValue(),
                            storeID,
                            1);

                    currentProduct.setBaseID(productID);
                    binding.progressBarProduct.setVisibility(View.GONE);
                    binding.layoutProductsInfo.setVisibility(View.VISIBLE);
                    // setup product information
                    binding.txtTitle.setText(currentProduct.getProductName());
                    binding.txtOldPrice.setText(FormatHelper.formatVND(currentProduct.getOldPrice()));
                    binding.txtOldPrice.setPaintFlags(binding.txtOldPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);


                    binding.txtNewPrice.setText(FormatHelper.formatDecimal(currentProduct.getNewPrice()));
                    binding.ratingBar.setRating(5.0F);

                    binding.txtSold.setText("Đã bán " + currentProduct.getSold());
                    binding.txtProdctDescription.setText(currentProduct.getDescription());
                    binding.txtNewPriceInBuyNow.setText(FormatHelper.formatVND(currentProduct.getNewPrice()));


                    // setup productImages
                    ArrayList<SliderItem> sliderItems = new ArrayList<>();
                    for (int i = 0; i < currentProduct.getProductImages().size(); i++) {
                        sliderItems.add(new SliderItem(currentProduct.getProductImages().get(i)));
                    }

                    binding.viewPager2Slider.setAdapter(
                            new SliderAdapterForProductDetail(ProductDetailActivity.this,
                                    sliderItems));
                    binding.viewPager2Slider.setOffscreenPageLimit(2);
                    binding.indicator.attachTo(binding.viewPager2Slider);

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
        if (storeID != null) {
            Store store = new Store();
            store.onGetStoreDetail(storeID, new GetDocumentCallback() {
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

    private void addToCart(String storeName, Product product, Variant selectedVariant, int quantity) {
        if (storeName == null || product == null) {
            // Handle error or throw exception
            return;
        }
        Log.d(tagVariant, "Adding to cart: StoreName = " + storeName + ", ProductName = " + product.getProductName() + ", SelectedVariant = " + selectedVariant);
        boolean storeFound = false;
        boolean productFound = false;

        for (CartItem cartItem : MY_CART) {
            if (storeName.equals(cartItem.getStoreName())) {
                storeFound = true;
                if (selectedVariant == null) {
                    // Case when product has no variant
                    for (Variant variant : cartItem.getListVariants()) {
                        if (product.getProductName().equals(variant.getProductName())) {
                            Log.d(tagVariant, "1. storeFound, đã có sản phẩm này (0 có variant), tăng số lượng lên thôi");
                            int currQuantity = variant.getNumberInCart();
                            variant.setNumberInCart(currQuantity + quantity);
                            productFound = true;

                            break;
                        }
                    }
                    if (!productFound) {
                        Log.d(tagVariant, "2. storeFound, chưa có sản phẩm này (0 có variant), thêm variant fake vào");
                        Variant newVariant = new Variant(null, product.getOldPrice(), product.getNewPrice(), product.getInStock()
                                , product.getProductImages().get(0), productID);
                        newVariant.setProductName(product.getProductName());
                        newVariant.setNumberInCart(quantity);
                        cartItem.getListVariants().add(newVariant);
                    }
                } else {
                    // Case when product has variant
                    boolean variantFound = false;
                    for (Variant variant : cartItem.getListVariants()) {
                        if (selectedVariant.getBaseID() != null && selectedVariant.getBaseID().equals(variant.getBaseID())) {
                            Log.d(tagVariant, "3. storeFound, productFound, có variant này, tăng quantity thôi");
                            int currQuantity = variant.getNumberInCart();
                            variant.setNumberInCart(currQuantity + quantity);
                            variantFound = true;
                            break;
                        }
                    }
                    if (!variantFound) {
                        Log.d(tagVariant, "4. storeFound, productFound, chưa có variant này, thêm variant vào");
                        selectedVariant.setNumberInCart(quantity);
                        selectedVariant.setProductName(product.getProductName());
                        cartItem.getListVariants().add(selectedVariant);
                    }
                }
                break;
            }
        }

        if (!storeFound) {
            ArrayList<Variant> variants = new ArrayList<>();
            if (selectedVariant == null) {
                Log.d(tagVariant, "5. storeNotFound, chưa có sản phẩm này, (0 có variant), thêm variant fake vào");
                // Case when product has no variant
                Variant newVariant = new Variant(null, product.getOldPrice(), product.getNewPrice(), product.getInStock()
                        , product.getProductImages().get(0), productID);
                newVariant.setProductName(product.getProductName());
                newVariant.setNumberInCart(quantity);
                variants.add(newVariant);
            } else {
                Log.d(tagVariant, "6. storeNotFound, productNotfound, (có variant), thêm variant này vào");
                // Case when product has variant
                selectedVariant.setNumberInCart(quantity);
                selectedVariant.setProductName(product.getProductName());
                variants.add(selectedVariant);
            }
            MY_CART.add(new CartItem(storeID, storeName, variants));
        }
        Log.d(tagVariant, "---------------------------");
    }

    private void setupEvents() {
        binding.btnAddToCart.setOnClickListener(v -> {
            if (buyable) {
                popUpAddToCartDialog();
            } else {
                showToast(ProductDetailActivity.this, "Bạn đang bán sản phẩm này\nKhông thể mua");
            }

        });
        binding.imageBack.setOnClickListener(v -> finish());

        binding.iconCart.setOnClickListener(v -> {
            Intent intent = new Intent(ProductDetailActivity.this, CartActivity.class);
            startActivity(intent);
        });


        binding.btnViewStore.setOnClickListener(v -> {
            Intent intent = new Intent(ProductDetailActivity.this, ViewMyStoreActivity.class);
            intent.putExtra(STORE_ID, storeID);
            startActivity(intent);
        });

        binding.layoutBuyNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (buyable) {
//                    popUpBuyNowDialog();
                } else {
                    showToast(ProductDetailActivity.this, "Bạn đang bán sản phẩm này\nKhông thể mua");
                }
            }
        });
    }

    private void popUpAddToCartDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        DialogAddToCartBinding dialogBinding = DialogAddToCartBinding.inflate(getLayoutInflater());
        builder.setView(dialogBinding.getRoot());
        AlertDialog dialog = builder.create();

        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawableResource(R.drawable.custom_edit_text_border);
        dialog.show();


        Window window = dialog.getWindow();
        if (window != null) {
            window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            window.setGravity(Gravity.BOTTOM);

            Animation slideUp = AnimationUtils.loadAnimation(this, R.anim.slide_up);
            dialogBinding.getRoot().startAnimation(slideUp);
        }

        //reset thông tin trên dialog mỗi lần popup dialog
        g_selectedVariant = null;
        g_totalInstock = 0;
        g_selectedPos = -1;
        dialogBinding.txtOldPrice.setText(FormatHelper.formatVND(currentProduct.getOldPrice()));
        dialogBinding.txtOldPrice.setPaintFlags(binding.txtOldPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        dialogBinding.txtNewPrice.setText(FormatHelper.formatDecimal(currentProduct.getNewPrice()));
        dialogBinding.txtProductName.setText(currentProduct.getProductName());
        Glide.with(ProductDetailActivity.this).load(currentProduct.getProductImages().get(0)).into(dialogBinding.imageView);
        dialogBinding.txtQuantity.setText(String.valueOf(1));
        dialogBinding.progressBar.setVisibility(View.VISIBLE);


        // gọi api lấy toàn bộ variant của sản phẩm về
        variantApi mVariantApi = new variantApi();


        mVariantApi.getVariantsByProductIdApi(productID, new GetCollectionCallback<Variant>() {
            @Override
            public void onGetListSuccess(ArrayList<Variant> variants) {
                g_variants = new ArrayList<>(variants);
                dialogBinding.progressBar.setVisibility(View.GONE);

                if (!g_variants.isEmpty()) {
                    //Case Product có variant, totalInstock = tất cả inStock của các variant
                    g_variants.forEach(item -> {
                        g_totalInstock += item.getInStock();
                    });
                    dialogBinding.txtInStock.setText("Kho: " + g_totalInstock);
                    dialogBinding.txtSelectVariant.setVisibility(View.VISIBLE);

                    dialogBinding.recyclerView.setLayoutManager(new GridLayoutManager(ProductDetailActivity.this, 2));
                    dialogBinding.recyclerView.setAdapter(new VariantGridAdapter(ProductDetailActivity.this, g_variants, g_selectedPos, new ItemSelectionListener<Variant>() {
                        @Override
                        public void transferInfo(int transferPosition, Variant variant) {
                            if (transferPosition != -1) {
                                dialogBinding.txtVariantName.setVisibility(View.VISIBLE);
                            } else {
                                dialogBinding.txtVariantName.setVisibility(View.GONE);
                            }
                            if (g_selectedPos != transferPosition) {
                                g_selectedPos = transferPosition;
                                g_selectedVariant = variant;
                                Glide.with(ProductDetailActivity.this).load(variant.getVariantImageUrl()).centerCrop().into(dialogBinding.imageView);
                                dialogBinding.txtVariantName.setText(variant.getVariantName());
                                dialogBinding.txtInStock.setText("Kho: " + variant.getInStock());
                                dialogBinding.txtNewPrice.setText(FormatHelper.formatVND(variant.getOldPrice()));
                                dialogBinding.txtOldPrice.setPaintFlags(binding.txtOldPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                                dialogBinding.txtNewPrice.setText(FormatHelper.formatDecimal(variant.getNewPrice()));
                            }
                        }
                    }));
                } else {
                    //Case Product không có variant, totalInstock =  inStock của product
                    g_totalInstock = currentProduct.getInStock();
                    dialogBinding.txtInStock.setText("Kho: " + g_totalInstock);
                    dialogBinding.txtSelectVariant.setVisibility(View.GONE);
                    g_selectedVariant = null;
                }
            }

            @Override
            public void onGetListFailure(String errorMessage) {
                dialogBinding.progressBar.setVisibility(View.GONE);
                showToast(ProductDetailActivity.this, INTERNET_ERROR);
            }
        });


        // setup Events

        dialogBinding.btnPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int quantity = Integer.parseInt(dialogBinding.txtQuantity.getText().toString().trim());
                if (quantity < 99) {
                    dialogBinding.txtQuantity.setText(String.valueOf(quantity + 1));
                }

            }
        });

        dialogBinding.imageExpand.setOnClickListener(v -> popUpProductImageExpandDialog(g_selectedVariant));
        dialogBinding.btnMinus.setOnClickListener(v -> {
            int quantity = Integer.parseInt(dialogBinding.txtQuantity.getText().toString().trim());
            if (quantity > 1) {
                dialogBinding.txtQuantity.setText(String.valueOf(quantity - 1));
            }

        });

        dialogBinding.imageClose.setOnClickListener(v -> dialog.dismiss());

        dialogBinding.btnAddToCart.setOnClickListener(v -> {
            SharedPreferences sharedPreferences = getSharedPreferences(USER_INFO, MODE_PRIVATE);
            String userID = sharedPreferences.getString(USER_ID, null);

            if (userID == null) {
                startActivity(new Intent(ProductDetailActivity.this, LoginActivity.class));
                return;
            }
            if ((!g_variants.isEmpty()) && g_selectedPos == -1){
                showToast(ProductDetailActivity.this, "Hãy chọn sản phẩm trước");
                return;
            }else if (!g_variants.isEmpty()){
                if (g_selectedVariant.getNumberInCart() > g_selectedVariant.getInStock()) {
                    showToast(ProductDetailActivity.this, "Uiii, số lượng sản phẩm không đủ!");
                } else {
                    int quantity = Integer.parseInt(dialogBinding.txtQuantity.getText().toString().trim());
                    addToCart(storeName, currentProduct, g_selectedVariant, quantity);
                    showToast(ProductDetailActivity.this, "Đã thêm sản phẩm vào giỏ hàng");
                    updateQuantityInCart(binding.txtQuantityInCart);
                    dialog.dismiss();
                }
            } else {
                if (currentProduct.getNumberInCart() > currentProduct.getInStock()) {
                    showToast(ProductDetailActivity.this, "Uiii, số lượng sản phẩm không đủ!");
                } else {
                    int quantity = Integer.parseInt(dialogBinding.txtQuantity.getText().toString().trim());
                    addToCart(storeName, currentProduct, null, quantity);
                    showToast(ProductDetailActivity.this, "Đã thêm sản phẩm vào giỏ hàng");
                    updateQuantityInCart(binding.txtQuantityInCart);
                    dialog.dismiss();
                }
            }


        });
    }


    private void popUpProductImageExpandDialog(Variant variant) {
        Dialog dialog = new Dialog(this, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        DialogProductImageExpandBinding dialogBinding = DialogProductImageExpandBinding.inflate(getLayoutInflater());
        dialog.setContentView(dialogBinding.getRoot());
        dialog.show();
        Animation slideUp = AnimationUtils.loadAnimation(this, R.anim.slide_in_from_right);
        dialogBinding.getRoot().startAnimation(slideUp);
        if (variant != null) {
            Glide.with(ProductDetailActivity.this).load(variant.getVariantImageUrl()).into(dialogBinding.imageView);
        } else {
            Glide.with(ProductDetailActivity.this).load(currentProduct.getProductImages().get(0)).into(dialogBinding.imageView);
        }


        dialogBinding.imageClose.setOnClickListener(v -> dialog.dismiss());
    }

    private void initUI() {
        getWindow().setStatusBarColor(Color.parseColor("#F04D7F"));
        getWindow().setNavigationBarColor(Color.parseColor("#EFEFEF"));
        Objects.requireNonNull(getSupportActionBar()).hide();
        updateQuantityInCart(binding.txtQuantityInCart);
    }


}