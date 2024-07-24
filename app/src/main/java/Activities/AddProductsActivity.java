package Activities;

import static constants.keyName.CATEGORY_ID;
import static constants.keyName.CATEGORY_NAME;
import static constants.keyName.PRODUCT_DESC;
import static constants.keyName.PRODUCT_INSTOCK;
import static constants.keyName.PRODUCT_NAME;
import static constants.keyName.PRODUCT_NEW_PRICE;
import static constants.keyName.PRODUCT_OLD_PRICE;
import static constants.keyName.STORE_ID;
import static constants.keyName.USER_INFO;
import static constants.toastMessage.CREATE_PRODUCT_SUCCESSFULLY;
import static constants.toastMessage.DEFAULT_REQUIRE;
import static utils.CartUtils.showToast;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.example.stores.databinding.ActivityAddProductsBinding;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import interfaces.CreateDocumentCallback;
import models.Product;

public class AddProductsActivity extends AppCompatActivity {

    ActivityAddProductsBinding binding;
    String storeId;
    String categoryId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddProductsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initUI();
        setupEvents();


    }

    private void setupEvents() {
        binding.imageBack.setOnClickListener(v -> finish());
        binding.layoutDeliveryFee.setOnClickListener(v -> {
            Intent intent = new Intent(AddProductsActivity.this, DeliveryFeeActivity.class);
            startActivity(intent);
        });

        binding.btnSave.setOnClickListener(v -> {
            Map<String, Object> productData = validateForm();
            if (productData != null) {
                Product product = new Product();
                product.onCreateProduct(productData, new CreateDocumentCallback() {
                    @Override
                    public void onCreateSuccess(String documentId) {
                        showToast(AddProductsActivity.this, CREATE_PRODUCT_SUCCESSFULLY);
                        finish();
                    }

                    @Override
                    public void onCreateFailure(String errorMessage) {
                        Toast.makeText(AddProductsActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                    }
                });

            } else {
                showToast(AddProductsActivity.this, "Vui lòng điền đầy đủ thông tin");
            }


        });


        binding.layoutCategory.setOnClickListener(v -> {
            Intent intent = new Intent(AddProductsActivity.this, SelectCategoryActivity.class);
            launcherSelectCategory.launch(intent);
        });

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

    private Map<String, Object> validateForm() {
        String productName = Objects.requireNonNull(binding.edtTitle.getText()).toString().trim();
        String description = Objects.requireNonNull(binding.edtDescription.getText()).toString().trim();
        String priceStr = Objects.requireNonNull(binding.edtPrice.getText()).toString().trim();
        String inStockStr = Objects.requireNonNull(binding.edtInStock.getText()).toString().trim();
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

        if (priceStr.isEmpty()) {
            binding.edtPrice.setError(DEFAULT_REQUIRE);
            isValid = false;
        }

        if (inStockStr.isEmpty()) {
            binding.edtInStock.setError(DEFAULT_REQUIRE);
            isValid = false;
        }

        if (categoryName.isEmpty()) {
            binding.edtCategory.setError(DEFAULT_REQUIRE);
            isValid = false;
        }

        if (isValid) {
            try {
                double price = Double.parseDouble(priceStr.replace(",", ""));
                int inStock = Integer.parseInt(inStockStr.replace(",", ""));
                Map<String, Object> newProduct = new HashMap<>();

                newProduct.put(PRODUCT_NAME, productName);
                newProduct.put(PRODUCT_DESC, description);
                newProduct.put(PRODUCT_NEW_PRICE, price);
                newProduct.put(PRODUCT_OLD_PRICE, price);
                newProduct.put(PRODUCT_INSTOCK, inStock);
                newProduct.put(CATEGORY_ID, categoryId);
                newProduct.put(CATEGORY_NAME, categoryName);
                newProduct.put(STORE_ID, storeId);

                return newProduct;
            } catch (NumberFormatException ignored) {

            }
        }

        return null;
    }


    private void initUI() {
        getWindow().setStatusBarColor(Color.parseColor("#F04D7F"));
        Objects.requireNonNull(getSupportActionBar()).hide();
        SharedPreferences sharedPreferences = getSharedPreferences(USER_INFO, MODE_PRIVATE);
        storeId = sharedPreferences.getString(STORE_ID, null);
    }


}