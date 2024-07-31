package Activities.AddAddress;

import static constants.keyName.DEFAULT_ADDRESS_ID;
import static constants.keyName.DETAILED_ADDRESS;
import static constants.keyName.DISTRICT_NAME;
import static constants.keyName.FULLNAME;
import static constants.keyName.PROVINCE_NAME;
import static constants.keyName.STORE_ID;
import static constants.keyName.USER_ID;
import static constants.keyName.USER_INFO;
import static constants.keyName.WARD_NAME;
import static constants.toastMessage.IMAGE_REQUIRE;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
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
import com.example.stores.databinding.ActivityDeliveryAddressBinding;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import Activities.UpdateProfileActivity;
import Adapters.DeliveryAddressAdapter;
import api.addressApi;
import api.userApi;
import interfaces.GetCollectionCallback;
import interfaces.GetDocumentCallback;
import interfaces.StatusCallback;
import interfaces.UserCallback;
import models.User;
import models.UserAddress;

public class DeliveryAddressActivity extends AppCompatActivity {

    ActivityDeliveryAddressBinding binding;
    private addressApi addressApi;
    private SharedPreferences sharedPreferences;
    private userApi userApi;
    private String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDeliveryAddressBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initUI();

        setupEvent();
    }

    private void setupEvent() {
        binding.addNewAddressLN.setOnClickListener(v -> {
            myLauncher.launch(new Intent(DeliveryAddressActivity.this,
                    CreateAddressActivity.class));
        });

        binding.imageBack.setOnClickListener(v -> finish());

        renderAddressList();
        setDefaultAddress();
    }

    private void setDefaultAddress() {
        SharedPreferences.Editor editor = sharedPreferences.edit();

        userApi = new userApi();
        userApi.getUserInfoApi(userID, new UserCallback() {
            @Override
            public void getUserInfoSuccess(User user) {
                editor.putString(DEFAULT_ADDRESS_ID, user.getDefaultAddressID());
            }

            @Override
            public void getUserInfoFailure(String errorMessage) {
                Toast.makeText(DeliveryAddressActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void renderAddressList() {
        binding.progressBar.setVisibility(View.VISIBLE);
        binding.progressBar.getIndeterminateDrawable()
                .setColorFilter(Color.parseColor("#F04D7F"), PorterDuff.Mode.MULTIPLY);

        addressApi = new addressApi();
        addressApi.getAddressByUserIDApi(userID, new GetCollectionCallback<UserAddress>() {
            @Override
            public void onGetListSuccess(ArrayList<UserAddress> addressList) {
                binding.progressBar.setVisibility(View.GONE);

                DeliveryAddressAdapter adapter = new DeliveryAddressAdapter(
                        DeliveryAddressActivity.this, addressList);

                binding.recyclerDeliveryAddress.setLayoutManager(
                        new LinearLayoutManager(DeliveryAddressActivity.this));
                binding.recyclerDeliveryAddress.setAdapter(adapter);

                binding.imageBack.setOnClickListener(v -> {
                    String selectedRadio = adapter.getSelectedRadio();

                    updateDefaultAddress(selectedRadio);
                });
            }

            @Override
            public void onGetListFailure(String errorMessage) {
                binding.progressBar.setVisibility(View.GONE);
                Toast.makeText(DeliveryAddressActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateDefaultAddress(String defaultAddress) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(DEFAULT_ADDRESS_ID, defaultAddress);

        Map<String, Object> userUpdates = new HashMap<>();
        userUpdates.put(DEFAULT_ADDRESS_ID, defaultAddress);
        userApi.updateUserInfoApi(userID, userUpdates, new StatusCallback() {
            @Override
            public void onSuccess(String successMessage) {
                setResult(RESULT_OK);
                finish();
            }

            @Override
            public void onFailure(String errorMessage) {
                Toast.makeText(DeliveryAddressActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initUI() {
        getWindow().setStatusBarColor(Color.parseColor("#F04D7F"));
        Objects.requireNonNull(getSupportActionBar()).hide();


        sharedPreferences = getSharedPreferences(USER_INFO, MODE_PRIVATE);
        userID = sharedPreferences.getString(USER_ID, null);
    }


    ActivityResultLauncher<Intent> myLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        renderAddressList();
                    }
                }
            });
}