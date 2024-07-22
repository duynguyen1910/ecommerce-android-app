package Activities.StoreSetup;

import static constants.keyName.STORE_ID;
import static constants.keyName.STORE_NAME;
import static constants.keyName.USER_INFO;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.stores.databinding.ActivityStoreOwnerBinding;

import java.util.Map;
import java.util.Objects;

import interfaces.GetDocumentCallback;
import models.Store;

public class StoreOwnerActivity extends AppCompatActivity {

    ActivityStoreOwnerBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityStoreOwnerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initUI();
        setupEvents();


    }

    private void setupEvents() {
        binding.imageBack.setOnClickListener(v -> finish());
        binding.btnViewStore.setOnClickListener(v -> {
            SharedPreferences sharedPreferences = getSharedPreferences(USER_INFO, MODE_PRIVATE);
            String storeId = sharedPreferences.getString(STORE_ID, null);
            Intent intent = new Intent(StoreOwnerActivity.this, ViewMyStoreActivity.class);
            intent.putExtra("storeId", storeId);
            startActivity(intent);
        });

        binding.layoutProducts.setOnClickListener(v -> {
            Intent intent = new Intent(StoreOwnerActivity.this, MyProductsActivity.class);
            startActivity(intent);
        });

    }

    private void initUI() {
        getWindow().setStatusBarColor(Color.parseColor("#F04D7F"));
        Objects.requireNonNull(getSupportActionBar()).hide();

        binding.progressBarStoreName.setVisibility(View.VISIBLE);
        SharedPreferences sharedPreferences = getSharedPreferences(USER_INFO, MODE_PRIVATE);


        String storeId = sharedPreferences.getString(STORE_ID, null);
        // lấy thông tin avatar, invoice



        if (storeId != null) {
            Store store = new Store();
            store.onGetStoreDetail(storeId, new GetDocumentCallback() {
                @Override
                public void onGetDataSuccess(Map<String, Object> data) {
                    binding.progressBarStoreName.setVisibility(View.GONE);
                    binding.txtStoreName.setText((CharSequence) data.get(STORE_NAME));

                    // set up UI avatar, invoice

                }

                @Override
                public void onGetDataFailure(String errorMessage) {
                    Toast.makeText(StoreOwnerActivity.this, "Uiii, lỗi mạng rồi :(((", Toast.LENGTH_SHORT).show();
                }
            });


        }

    }


}