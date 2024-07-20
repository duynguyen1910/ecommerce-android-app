package Activities;

import static constants.keyName.CATEGORY_NAME;
import static constants.keyName.PRODUCT_DESC;
import static constants.keyName.PRODUCT_INSTOCK;
import static constants.keyName.PRODUCT_PRICE;
import static constants.keyName.PRODUCT_TITLE;
import static constants.keyName.STORE_ID;
import static constants.keyName.USER_ID;
import static constants.keyName.USER_INFO;
import static constants.toastMessage.CREATE_PRODUCT_SUCCESSFULLY;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.example.stores.databinding.ActivityAddProductsBinding;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import interfaces.CreateProductCallback;
import models.Product;

public class AddProductsActivity extends AppCompatActivity {

    ActivityAddProductsBinding binding;
    private SharedPreferences sharedPreferences;

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
            if (validateForm() != null){
                sharedPreferences = getSharedPreferences(USER_INFO, MODE_PRIVATE);
                String storeId = sharedPreferences.getString(STORE_ID, null);
                Map<String, Object> productData = validateForm();
                productData.put(STORE_ID, storeId);
                Product product = new Product();
                product.onCreateProduct(productData, storeId, new CreateProductCallback() {
                    @Override
                    public void onCreateSuccess(String successMessage) {
                        Toast.makeText(AddProductsActivity.this, successMessage, Toast.LENGTH_SHORT).show();

                    }

                    @Override
                    public void onCreateFailure(String errorMessage) {
                        Toast.makeText(AddProductsActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                    }
                });

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
                    if (intent != null){
                        String data = intent.getStringExtra("data");
                        if (data != null){
                            binding.txtChooseCategory.setText("Ngành hàng: ");
                            binding.edtCategory.setText(data);
                        }
                    }


                }
            });

    private Map<String, Object> validateForm() {

        String productName = Objects.requireNonNull(binding.edtTitle.getText()).toString().trim();
        String description = Objects.requireNonNull(binding.edtDescription.getText()).toString().trim();
        String price = Objects.requireNonNull(binding.edtPrice.getText()).toString().trim();
        String inStock = Objects.requireNonNull(binding.edtInStock.getText()).toString().trim();
        String categoryName = Objects.requireNonNull(binding.edtCategory.getText()).toString().trim();

        boolean isValid = true;


        if (productName.isEmpty()) {
            binding.edtTitle.setError("Tên sản phẩm không được bỏ trống");
            isValid = false;
        }

        if (description.isEmpty()) {
            binding.edtDescription.setError("Mô tả không được bỏ trống");
            isValid = false;
        }

        if (price.isEmpty()) {
            binding.edtPrice.setError("Giá không được bỏ trống");
            isValid = false;
        }

        if (inStock.isEmpty()) {
            binding.edtInStock.setError("Tồn kho không được bỏ trống");
            isValid = false;
        }
        if (categoryName.isEmpty()) {
            binding.edtCategory.setText("Vui lòng chọn ngành hàng");
            isValid = false;
        }



        if (isValid) {
            Map<String, Object> productData = new HashMap<>();
            productData.put(PRODUCT_TITLE, productName);
            productData.put(PRODUCT_DESC, description);
            productData.put(PRODUCT_PRICE, price);
            productData.put(PRODUCT_INSTOCK, inStock);
            productData.put(CATEGORY_NAME, categoryName);

            return productData;
        } else {
            return null;
        }
    }


    private void initUI() {
        getWindow().setStatusBarColor(Color.parseColor("#F04D7F"));
        Objects.requireNonNull(getSupportActionBar()).hide();

    }


}