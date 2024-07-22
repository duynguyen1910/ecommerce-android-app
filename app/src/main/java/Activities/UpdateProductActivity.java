package Activities;
import static constants.keyName.CATEGORY_ID;
import static constants.keyName.CATEGORY_NAME;
import static constants.keyName.PRODUCT_DESC;
import static constants.keyName.PRODUCT_INSTOCK;
import static constants.keyName.PRODUCT_NAME;
import static constants.keyName.PRODUCT_NAME_CHUNK;
import static constants.keyName.PRODUCT_NEW_PRICE;
import static constants.keyName.PRODUCT_OLD_PRICE;
import static constants.keyName.STORE_ID;
import static constants.toastMessage.CREATE_PRODUCT_SUCCESSFULLY;
import static constants.toastMessage.DEFAULT_REQUIRE;
import static constants.toastMessage.UPDATE_PRODUCT_FAILED;
import static constants.toastMessage.UPDATE_PRODUCT_SUCCESSFULLY;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Toast;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import com.example.stores.databinding.ActivityUpdateProductBinding;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import interfaces.CreateDocumentCallback;
import interfaces.UpdateDocumentCallback;
import models.Product;

public class UpdateProductActivity extends AppCompatActivity {

    ActivityUpdateProductBinding binding;
    String storeId;
    String categoryId;
    String productId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUpdateProductBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initUI();
        setupEvents();




    }
    ActivityResultLauncher<Intent> launcherSelectCategory = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == 1) {
                    Intent intent = result.getData();
                    if (intent != null) {
                        Bundle bundle = intent.getExtras();
                        if (bundle != null){
                            String categoryName = bundle.getString("categoryName");
                            categoryId = bundle.getString("categoryId") ;
                            binding.txtChooseCategory.setText("Ngành hàng: ");
                            binding.edtCategory.setText(categoryName);
                        }
                    }
                }
            });
    private void setupEvents() {
        binding.imageBack.setOnClickListener(v -> finish());
        binding.layoutDeliveryFee.setOnClickListener(v -> {
            Intent intent = new Intent(UpdateProductActivity.this, DeliveryFeeActivity.class);
            startActivity(intent);
        });

        binding.btnSave.setOnClickListener(v -> {
            Map<String, Object> productData = validateForm();
            if (productData != null) {
                Product product = new Product();
               product.updateProduct(productData, storeId, productId, new UpdateDocumentCallback() {
                   @Override
                   public void onUpdateSuccess() {
                       showToast(UPDATE_PRODUCT_SUCCESSFULLY);
                   }

                   @Override
                   public void onUpdateFailure(String errorMessage) {
                       showToast(UPDATE_PRODUCT_FAILED);
                   }
               });
            }

            finish();

        });

        binding.layoutCategory.setOnClickListener(v -> {
            Intent intent = new Intent(UpdateProductActivity.this, SelectCategoryActivity.class);
            launcherSelectCategory.launch(intent);
        });

    }
    private void showToast(String message) {
        Toast.makeText(UpdateProductActivity.this, message, Toast.LENGTH_SHORT).show();
    }
    private Map<String, Object> validateForm() {
        String productName = Objects.requireNonNull(binding.edtTitle.getText()).toString().trim();
        String description = Objects.requireNonNull(binding.edtDescription.getText()).toString().trim();
        String price = Objects.requireNonNull(binding.edtPrice.getText()).toString().trim();
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
            Product product = new Product();
            Map<String, Object> newProduct = new HashMap<>();
            ArrayList<String> productNameSplit = product.splitProductNameBySpace(productName);

            newProduct.put(PRODUCT_NAME, productName);
            newProduct.put(PRODUCT_DESC, description);
            newProduct.put(PRODUCT_NEW_PRICE, Double.parseDouble(price));
            newProduct.put(PRODUCT_OLD_PRICE, Double.parseDouble(price));
            newProduct.put(PRODUCT_INSTOCK, Integer.parseInt(inStock));
            newProduct.put(CATEGORY_ID, categoryId);
            newProduct.put(CATEGORY_NAME, categoryName);
            newProduct.put(STORE_ID, storeId);
            newProduct.put(PRODUCT_NAME_CHUNK, productNameSplit);

            return newProduct;
        } else {
            return null;
        }
    }
    private void initUI(){
        getWindow().setStatusBarColor(Color.parseColor("#F04D7F"));
        Objects.requireNonNull(getSupportActionBar()).hide();

    }


}