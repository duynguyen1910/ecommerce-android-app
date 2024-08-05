package Activities.ProductSetup;

import static constants.keyName.CATEGORY_ID;
import static constants.keyName.CATEGORY_NAME;
import static constants.keyName.PRODUCT_DESC;
import static constants.keyName.PRODUCT_IMAGES;
import static constants.keyName.PRODUCT_INSTOCK;
import static constants.keyName.PRODUCT_NAME;
import static constants.keyName.PRODUCT_NEW_PRICE;
import static constants.keyName.PRODUCT_OLD_PRICE;
import static constants.keyName.STORE_ID;
import static constants.keyName.USER_INFO;
import static constants.toastMessage.CREATE_PRODUCT_SUCCESSFULLY;
import static constants.toastMessage.DEFAULT_REQUIRE;
import static constants.toastMessage.IMAGE_REQUIRE;
import static utils.Cart.CartUtils.showToast;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.example.stores.databinding.ActivityAddProductsBinding;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import Activities.ProfileSetup.UpdateProfileActivity;
import Adapters.PreviewImageAdapter;
import api.productApi;
import api.uploadApi;
import interfaces.CreateDocumentCallback;
import interfaces.GetCollectionCallback;


public class AddProductsActivity extends AppCompatActivity {
    private ActivityAddProductsBinding binding;
    private String storeID;
    private String categoryID;
    private ArrayList<Uri> imagesUriList;

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

        binding.btnAdd.setOnClickListener(v -> {
            submitProductWithValidation();
        });


        binding.layoutCategory.setOnClickListener(v -> {
            Intent intent = new Intent(AddProductsActivity.this, SelectCategoryActivity.class);
            launcherSelectCategory.launch(intent);
        });

        binding.imageUploadLN.setOnClickListener(v -> {
            Intent photoPicker = new Intent(Intent.ACTION_PICK,
                    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            photoPicker.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
            photoPicker.setType("image/*");
            productImagesLauncher.launch(photoPicker);
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
                            categoryID = bundle.getString(CATEGORY_ID);
                            binding.txtChooseCategory.setText("Ngành hàng: ");
                            binding.edtCategory.setText(categoryName);
                        }
                    }
                }
            });


    private void submitProductWithValidation() {
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

        if(isValid) {
            binding.progressBar.setVisibility(View.VISIBLE);
            binding.progressBar.getIndeterminateDrawable()
                    .setColorFilter(Color.parseColor("#f04d7f"), PorterDuff.Mode.MULTIPLY);


            uploadApi uploadApi = new uploadApi();
            uploadApi.uploadMultiImageToStorageApi(imagesUriList, new GetCollectionCallback() {
                @Override
                public void onGetListSuccess(ArrayList imagesUrlList) {
                    Map<String, Object> newProduct = new HashMap<>();
                    double price = Double.parseDouble(priceStr.replace(",", ""));
                    int inStock = Integer.parseInt(inStockStr.replace(",", ""));


                    newProduct.put(PRODUCT_NAME, productName);
                    newProduct.put(PRODUCT_IMAGES, imagesUrlList);
                    newProduct.put(PRODUCT_DESC, description);
                    newProduct.put(PRODUCT_NEW_PRICE, price);
                    newProduct.put(PRODUCT_OLD_PRICE, price);
                    newProduct.put(PRODUCT_INSTOCK, inStock);
                    newProduct.put(CATEGORY_ID, categoryID);
                    newProduct.put(STORE_ID, storeID);

                    onAddProduct(newProduct);
                }

                @Override
                public void onGetListFailure(String errorMessage) {
                    Toast.makeText(AddProductsActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                    binding.progressBar.setVisibility(View.GONE);
                }
            });
        };
    }


    private void onAddProduct(Map<String, Object> newProduct) {
        if (newProduct != null) {
            productApi productApi = new productApi();
            productApi.createProductApi(newProduct, new CreateDocumentCallback() {
                @Override
                public void onCreateSuccess(String documentId) {
                    binding.progressBar.setVisibility(View.GONE);
                    showToast(AddProductsActivity.this, CREATE_PRODUCT_SUCCESSFULLY);
                    finish();
                }

                @Override
                public void onCreateFailure(String errorMessage) {
                    binding.progressBar.setVisibility(View.GONE);
                    Toast.makeText(AddProductsActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                }
            });

        } else {
            binding.progressBar.setVisibility(View.GONE);
            showToast(AddProductsActivity.this, "Vui lòng điền đầy đủ thông tin");
        }
    }

    private void initUI() {
        getWindow().setStatusBarColor(Color.parseColor("#F04D7F"));
        Objects.requireNonNull(getSupportActionBar()).hide();

        SharedPreferences sharedPreferences = getSharedPreferences(USER_INFO, MODE_PRIVATE);
        storeID = sharedPreferences.getString(STORE_ID, null);

        imagesUriList = new ArrayList<>();
    }

    private void loadPreviewImages() {
        PreviewImageAdapter adapter = new PreviewImageAdapter(this, imagesUriList);
        binding.previewImagesRecyclerView.setAdapter(adapter);
        binding.previewImagesRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
    }


    ActivityResultLauncher<Intent> productImagesLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        imagesUriList.clear();

                        if (result.getData() != null && result.getData().getClipData() != null) {
                            int count = result.getData().getClipData().getItemCount();

                            for(int i = 0; i < count; i++) {
                                Uri imageUri = result.getData().getClipData().getItemAt(i).getUri();
                                imagesUriList.add(imageUri);
                            }
                        }

                        loadPreviewImages();

                    } else {
                        Toast.makeText(AddProductsActivity.this, IMAGE_REQUIRE, Toast.LENGTH_SHORT).show();
                    }
                }
            });
}