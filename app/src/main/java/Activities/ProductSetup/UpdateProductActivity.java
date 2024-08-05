package Activities.ProductSetup;

import static constants.keyName.CATEGORY_ID;
import static constants.keyName.CATEGORY_NAME;
import static constants.keyName.PRODUCT_DESC;
import static constants.keyName.PRODUCT_ID;
import static constants.keyName.PRODUCT_INSTOCK;
import static constants.keyName.PRODUCT_NAME;
import static constants.keyName.PRODUCT_NEW_PRICE;
import static constants.keyName.PRODUCT_OLD_PRICE;
import static constants.toastMessage.DEFAULT_REQUIRE;
import static constants.toastMessage.INTERNET_ERROR;
import static utils.Cart.CartUtils.showToast;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.example.stores.databinding.ActivityUpdateProductBinding;

import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

import interfaces.GetDocumentCallback;
import interfaces.UpdateDocumentCallback;
import models.Product;
import models.UserAddress;

public class UpdateProductActivity extends AppCompatActivity {

    private ActivityUpdateProductBinding binding;
    private String categoryId;
    private String productID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUpdateProductBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initUI();
        getBundle();
        setupEvents();


    }

    private void getBundle() {
        Intent intent = getIntent();
        if (intent != null) {
            productID = intent.getStringExtra(PRODUCT_ID);
            if (productID != null) {
                Product product = new Product();
                product.getProductDetail(productID, new GetDocumentCallback() {
                    @Override
                    public void onGetDataSuccess(Map<String, Object> productDetail) {
                        binding.edtCategory.setText((CharSequence) productDetail.get(CATEGORY_NAME));
                        binding.edtDescription.setText((CharSequence) productDetail.get(PRODUCT_DESC));
                        binding.edtTitle.setText((CharSequence) productDetail.get(PRODUCT_NAME));

                        NumberFormat formatter = NumberFormat.getInstance(new Locale("vi", "VN"));
                        double newPrice = (double) productDetail.get(PRODUCT_NEW_PRICE);
                        binding.edtPrice.setText(formatter.format(newPrice));

                        int inStock =   ((Long) Objects.requireNonNull(productDetail.get(PRODUCT_INSTOCK))).intValue();
                        binding.edtInStock.setText(String.valueOf(inStock));

                        categoryId = (String) productDetail.get(CATEGORY_ID);
                    }

                    @Override
                    public void onGetDataFailure(String errorMessage) {
                        showToast(UpdateProductActivity.this,INTERNET_ERROR);
                    }
                });
            }
        }
    }

    ActivityResultLauncher<Intent> launcherSelectCategory = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == 1) {
                    Intent intent = result.getData();
                    if (intent != null) {
                        Bundle bundle = intent.getExtras();
                        if (bundle != null) {
                            String categoryName = bundle.getString(CATEGORY_NAME);
                            categoryId = bundle.getString(CATEGORY_ID);
                            binding.txtChooseCategory.setText("Ngành hàng: ");
                            binding.edtCategory.setText(categoryName);
                        }
                    }
                }
            });

    private void setupEvents() {
        binding.imageBack.setOnClickListener(v -> finish());

        binding.layoutVariant.setOnClickListener(v -> {
            Intent intent = new Intent(UpdateProductActivity.this, AddVariantActivity.class);
            intent.putExtra(PRODUCT_ID, productID);
            startActivity(intent);
        });

        binding.layoutDeliveryFee.setOnClickListener(v -> {
            Intent intent = new Intent(UpdateProductActivity.this, DeliveryFeeActivity.class);
            startActivity(intent);
        });

        binding.btnSave.setOnClickListener(v -> {
            Map<String, Object> productData = validateForm();
            if (productData != null) {
                Product product = new Product();
                product.updateProduct(productData, productID, new UpdateDocumentCallback() {
                    @Override
                    public void onUpdateSuccess(String updateSuccess) {

                        showToast(UpdateProductActivity.this, updateSuccess);
                        finish();
                    }

                    @Override
                    public void onUpdateFailure(String errorMessage) {
                        showToast(UpdateProductActivity.this, errorMessage);
                    }
                });
            }



        });

        binding.layoutCategory.setOnClickListener(v -> {
            Intent intent = new Intent(UpdateProductActivity.this, SelectCategoryActivity.class);
            launcherSelectCategory.launch(intent);
        });

    }



    private Map<String, Object> validateForm() {
        String productName = Objects.requireNonNull(binding.edtTitle.getText()).toString().trim();
        String description = Objects.requireNonNull(binding.edtDescription.getText()).toString().trim();

        String price = Objects.requireNonNull(binding.edtPrice.getText()).toString().trim().replace(".", "");
        String inStock = Objects.requireNonNull(binding.edtInStock.getText()).toString().trim();
        String categoryName = Objects.requireNonNull(binding.edtCategory.getText()).toString().trim();


        boolean isValid = true;

        if (productName.isEmpty()) {
            binding.edtTitle.setError(DEFAULT_REQUIRE);
            isValid = false;
        }

        if (description.isEmpty()) {
            binding.edtDescription.setError(DEFAULT_REQUIRE);
            isValid = false;
        }

        if (price.isEmpty()) {
            binding.edtPrice.setError(DEFAULT_REQUIRE);
            isValid = false;
        }

        if (inStock.isEmpty()) {
            binding.edtInStock.setError(DEFAULT_REQUIRE);
            isValid = false;
        }
        if (categoryName.isEmpty()) {
            binding.edtCategory.setError(DEFAULT_REQUIRE);
            isValid = false;
        }

        if (isValid) {
            Map<String, Object> newProduct = new HashMap<>();
            newProduct.put(PRODUCT_NAME, productName);
            newProduct.put(PRODUCT_DESC, description);
            newProduct.put(PRODUCT_NEW_PRICE, Double.parseDouble(price));
            newProduct.put(PRODUCT_OLD_PRICE, Double.parseDouble(price));
            newProduct.put(PRODUCT_INSTOCK, Integer.parseInt(inStock));
            newProduct.put(CATEGORY_ID, categoryId);
            newProduct.put(CATEGORY_NAME, categoryName);


            return newProduct;
        } else {
            return null;
        }
    }

    private void initUI() {
        getWindow().setStatusBarColor(Color.parseColor("#F04D7F"));
        Objects.requireNonNull(getSupportActionBar()).hide();

    }


}