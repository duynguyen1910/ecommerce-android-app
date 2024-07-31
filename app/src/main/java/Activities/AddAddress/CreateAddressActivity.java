package Activities.AddAddress;

import static constants.keyName.ADDRESS_ID;
import static constants.keyName.DEFAULT_ADDRESS_ID;
import static constants.keyName.DETAILED_ADDRESS;
import static constants.keyName.DISTRICT_ID;
import static constants.keyName.DISTRICT_NAME;
import static constants.keyName.FULLNAME;
import static constants.keyName.PASSWORD;
import static constants.keyName.PROVINCE_NAME;
import static constants.keyName.USER_ID;
import static constants.keyName.USER_INFO;
import static constants.keyName.WARD_NAME;
import static constants.toastMessage.ADD_ADDRESS_SUCCESSFULLY;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.example.stores.databinding.ActivityCreateAddressBinding;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import api.addressApi;
import interfaces.CreateDocumentCallback;
import interfaces.StatusCallback;
import models.Addresses;
import models.User;

public class CreateAddressActivity extends AppCompatActivity {

    ActivityCreateAddressBinding binding;
    Addresses provinceSelected;
    Addresses districtSelected;
    Addresses wardSelected;
    addressApi addressApi;
    boolean isCheckedDefault = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCreateAddressBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initUI();
        setupEvent();
    }

    ActivityResultLauncher<Intent> launcherSelectAddress = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == 2) {
                    Intent intent = result.getData();
                    if (intent != null) {
                        provinceSelected = (Addresses)
                                intent.getSerializableExtra("provinceSelected");
                        districtSelected = (Addresses)
                                intent.getSerializableExtra("districtSelected");
                        wardSelected = (Addresses)
                                intent.getSerializableExtra("wardSelected");

                        String address = wardSelected.getName() + ", " + districtSelected.getName() +
                                ", " + provinceSelected.getName();
                        binding.txtSetAddress.setText(address);
                    }
                }
            }
    );

    private void setupEvent() {
        binding.imageBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        binding.btnComplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String detailedAddress = binding.txtDetailedAddress.getText().toString().trim();

                if(detailedAddress.isEmpty()) {
                    Toast.makeText(CreateAddressActivity.this, "Nhập địa chỉ cụ thể", Toast.LENGTH_SHORT).show();
                    return;
                }

                Map<String, Object> newAddress = new HashMap<>();
                newAddress.put(DETAILED_ADDRESS, detailedAddress);
                newAddress.put(WARD_NAME, wardSelected.getName());
                newAddress.put(DISTRICT_NAME, districtSelected.getName());
                newAddress.put(PROVINCE_NAME, provinceSelected.getName());

                addressApi = new addressApi();
                addressApi.createAddressApi(newAddress, new CreateDocumentCallback() {
                    @Override
                    public void onCreateSuccess(String documentId) {
                        onCreateUserAddress(documentId);
                    }

                    @Override
                    public void onCreateFailure(String errorMessage) {
                        Toast.makeText(CreateAddressActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        binding.addressDefaultSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                isCheckedDefault = isChecked;
            }
        });

        binding.txtSetAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Open một activity để chọn địa chỉ qua API, chọn tỉnh/thành phố --> Quận/Huyện --> Phường/Xã
                // Sau khi chọn xong thì đổi text của textView này
//                String newAddress = "TP. Hồ Chí Minh\nQuận 12, Phường Trung Mỹ Tây";
//                binding.txtSetAddress.setText(newAddress);

                Intent intent = new Intent(CreateAddressActivity.this, SelectAddressActivity.class);
                launcherSelectAddress.launch(intent);
            }
        });

    }

    public void onCreateUserAddress(String addressID) {
        SharedPreferences sharedPreferences = getSharedPreferences(USER_INFO, MODE_PRIVATE);
        String userID = sharedPreferences.getString(USER_ID, null);

        Map<String, Object> newUserAddress = new HashMap<>();
        newUserAddress.put(ADDRESS_ID, addressID);
        newUserAddress.put(USER_ID, userID);

        addressApi.createUserAddressApi(newUserAddress, new CreateDocumentCallback() {
            @Override
            public void onCreateSuccess(String documentId) {
                if(isCheckedDefault) {
                    setDefaultAddress(addressID, userID);
                } else {
                    Toast.makeText(CreateAddressActivity.this, ADD_ADDRESS_SUCCESSFULLY, Toast.LENGTH_SHORT).show();
                }

                setResult(RESULT_OK);
                finish();
            }

            @Override
            public void onCreateFailure(String errorMessage) {
                Toast.makeText(CreateAddressActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void setDefaultAddress(String defaultAddressID, String userID) {
        SharedPreferences sharedPreferences = getSharedPreferences(USER_INFO, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(DEFAULT_ADDRESS_ID, defaultAddressID);
        editor.apply();

        User user = new User();
        Map<String, Object> userUpdates = new HashMap<>();
        userUpdates.put(DEFAULT_ADDRESS_ID, defaultAddressID);

        user.updateUserInfo(userID, userUpdates, new StatusCallback() {
            @Override
            public void onSuccess(String successMessage) {
                finish();
                Toast.makeText(CreateAddressActivity.this, ADD_ADDRESS_SUCCESSFULLY, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(String errorMessage) {

            }
        });
    }

    private void initUI() {
        getWindow().setStatusBarColor(Color.parseColor("#F04D7F"));
        Objects.requireNonNull(getSupportActionBar()).hide();
    }


}