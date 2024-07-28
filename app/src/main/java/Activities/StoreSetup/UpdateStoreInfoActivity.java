package Activities.StoreSetup;
import static constants.keyName.ADDRESS;
import static constants.keyName.USER_ID;
import static constants.keyName.USER_INFO;
import static constants.toastMessage.IMAGE_REQUIRE;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide;
import com.example.stores.databinding.ActivityUpdateStoreInfoBinding;
import java.util.Objects;

import Activities.AddAddress.CreateAddressActivity;
import Activities.AddAddress.SelectAddressActivity;
import models.User;

public class UpdateStoreInfoActivity extends AppCompatActivity {

    ActivityUpdateStoreInfoBinding binding;
    private Uri imageUri;
    private User user;
    private String userId;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUpdateStoreInfoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getWindow().setStatusBarColor(Color.parseColor("#F04D7F"));
        Objects.requireNonNull(getSupportActionBar()).hide();


        setupEvents();
        initUI();
    }

    private void setupEvents() {
        binding.imageBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        binding.imvAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent photoPicker = new Intent();
                photoPicker.setAction(Intent.ACTION_GET_CONTENT);
                photoPicker.setType("image/*");
                myLauncher.launch(photoPicker);
            }
        });

        binding.txtChangeAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UpdateStoreInfoActivity.this, SelectAddressActivity.class);
                launcherSelectAddress.launch(intent);
            }
        });

    }

    private void initUI() {
        user = new User();
        sharedPreferences = getSharedPreferences(USER_INFO, MODE_PRIVATE);
        userId = sharedPreferences.getString(USER_ID, null);
    }


    ActivityResultLauncher<Intent> launcherSelectAddress = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == 2) {
                    Intent intent = result.getData();
                    if (intent != null) {
                        String address = intent.getStringExtra(ADDRESS);
                        if (address != null) {
                            binding.edtAddress.setText(address);
                        }
                    }
                }
            });
    ActivityResultLauncher<Intent> myLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        imageUri = result.getData().getData();
                        Glide.with(getApplicationContext()).load(imageUri).into(binding.imvAvatar);
                    } else {
                        Toast.makeText(UpdateStoreInfoActivity.this, IMAGE_REQUIRE, Toast.LENGTH_SHORT).show();
                    }
                }
            });
}