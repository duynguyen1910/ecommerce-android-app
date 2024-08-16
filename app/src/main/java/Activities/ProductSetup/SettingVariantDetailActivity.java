package Activities.ProductSetup;

import static constants.keyName.PRODUCT_ID;
import static constants.keyName.TYPES;
import static utils.Cart.CartUtils.showToast;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.stores.R;
import com.example.stores.databinding.ActivitySettingVariantDetailBinding;

import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.CountDownLatch;

import Adapters.SettingVariant.VariantSettingAdapter;
import api.uploadApi;
import api.variantApi;
import interfaces.StatusCallback;
import interfaces.UploadCallback;
import models.Type;
import models.Variant;

public class SettingVariantDetailActivity extends AppCompatActivity {

    private ActivitySettingVariantDetailBinding binding;
    private ArrayList<Type> types;
    private ArrayList<Variant> variants = new ArrayList<>();
    private VariantSettingAdapter variantAdapter;
    private String productID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySettingVariantDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initUI();
        getBundle();
        setupVariants();
        setupEvents();
    }

    private void getBundle() {
        Intent intent = getIntent();
        if (intent != null) {
            productID = intent.getStringExtra(PRODUCT_ID);

            types = (ArrayList<Type>) intent.getSerializableExtra(TYPES);
            if (types != null) {
                showToast(SettingVariantDetailActivity.this, "Đã chọn " +
                        types.size() + " phân loại hàng");
            }
        }
    }

    private void setupVariants() {
        if (types.size() == 2) {
            types.get(0).getListValues().forEach(item0 -> {
                types.get(1).getListValues().forEach(item1 -> {
                    String variantName = item0.getValue() + " | " + item1.getValue();
                    String imageUri = item0.getImage() != null ? item0.getImage() : item1.getImage();

                    Variant newVariant = new Variant(variantName, 0, 0, 0,
                            imageUri, productID);
                    variants.add(newVariant);
                });
            });
        } else if (types.size() == 1) {
            types.get(0).getListValues().forEach(item0 -> {
                String variantName = item0.getValue();
                String imageUri = item0.getImage();

                Variant newVariant = new Variant(variantName, 0, 0, 0,
                        imageUri, productID);
                variants.add(newVariant);
            });
        }

        variantAdapter = new VariantSettingAdapter(this, variants);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerView.setAdapter(variantAdapter);
    }

    private void setupEvents() {
        binding.imageBack.setOnClickListener(v -> finish());
        binding.btnSave.setOnClickListener(v -> {
            ArrayList<Variant> variantsList = variantAdapter.getVariantsList();
            CountDownLatch latch = new CountDownLatch(variantsList.size());

            binding.progressBar.setVisibility(View.VISIBLE);
            binding.progressBar.getIndeterminateDrawable()
                    .setColorFilter(Color.parseColor("#f04d7f"), PorterDuff.Mode.MULTIPLY);
            binding.btnSave.setBackground(ContextCompat.getDrawable(this, R.color.darkgray));

            for (Variant variant : variantsList) {
                uploadApi uploadApi = new uploadApi();
                uploadApi.uploadImageToStorageApi(Uri.parse(variant.getVariantImageUrl()),
                        new UploadCallback() {
                            @Override
                            public void uploadSuccess(Uri downloadUri) {
                                variant.setVariantImageUrl(downloadUri.toString());
                                latch.countDown();

                                if (latch.getCount() == 0) {
                                    createVariantsApi(variantsList);
                                }
                            }

                            @Override
                            public void uploadFailure(String errorMessage) {
                                Toast.makeText(SettingVariantDetailActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                                latch.countDown(); // Giảm số đếm ngay cả khi thất bại
                            }
                        });
            };
        });
    }

    private void createVariantsApi(ArrayList<Variant> variantsList) {
        variantApi variantApi = new variantApi();
        variantApi.createVariantsApi(variantsList, new StatusCallback() {
            @Override
            public void onSuccess(String successMessage) {
                binding.progressBar.setVisibility(View.GONE);
                binding.btnSave.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.color.primary_color));
                Toast.makeText(SettingVariantDetailActivity.this, successMessage, Toast.LENGTH_SHORT).show();

//                Intent intent = new Intent(SettingVariantDetailActivity.this, MyProductsAdapter.class);
//                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                startActivity(intent);
//                finish();
            }

            @Override
            public void onFailure(String errorMessage) {
                binding.progressBar.setVisibility(View.GONE);
                binding.btnSave.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.color.primary_color));
                Toast.makeText(SettingVariantDetailActivity.this, errorMessage, Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void initUI() {
        getWindow().setStatusBarColor(Color.parseColor("#F04D7F"));
        Objects.requireNonNull(getSupportActionBar()).hide();
    }


}