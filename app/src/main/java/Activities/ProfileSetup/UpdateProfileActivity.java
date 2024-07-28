package Activities.ProfileSetup;
import static constants.keyName.EMAIL;
import static constants.keyName.FULLNAME;
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
import com.example.stores.databinding.ActivityUpdateProfileBinding;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import api.uploadApi;
import interfaces.ImageCallback;
import interfaces.StatusCallback;
import interfaces.UploadCallback;
import interfaces.UserCallback;
import models.User;

public class UpdateProfileActivity extends AppCompatActivity {

    ActivityUpdateProfileBinding binding;
    private Uri imageUri;
    private User user;
    private String userId;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUpdateProfileBinding.inflate(getLayoutInflater());
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

        binding.updateProfileImv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadApi uploadApi = new uploadApi();
                uploadApi.uploadImageToStorageApi(imageUri, new UploadCallback() {
                    @Override
                    public void uploadSuccess(Uri downloadUri) {
                        user.onSaveUserImage(downloadUri.toString(), userId);
                    }

                    @Override
                    public void uploadFailure(String errorMessage) {
                        Toast.makeText(UpdateProfileActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                    }
                });

                Map<String, Object> userUpdates = new HashMap<>();
                userUpdates.put(FULLNAME, binding.txtFullname.getText().toString());
                userUpdates.put(EMAIL, binding.txtEmail.getText().toString());

                user.updateUserInfo(userId, userUpdates, new StatusCallback() {
                    @Override
                    public void onSuccess(String successMessage) {
                        Toast.makeText(UpdateProfileActivity.this, successMessage, Toast.LENGTH_SHORT).show();
                        finish();
                    }

                    @Override
                    public void onFailure(String errorMessage) {
                        Toast.makeText(UpdateProfileActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    private void initUI() {
        user = new User();
        sharedPreferences = getSharedPreferences(USER_INFO, MODE_PRIVATE);
        userId = sharedPreferences.getString(USER_ID, null);

        user.getUserImageUrl(userId, new ImageCallback() {
            @Override
            public void getImageSuccess(String downloadUri) {
                imageUri = Uri.parse(downloadUri);
                Glide.with(getApplicationContext()).load(downloadUri).into(binding.imvAvatar);
            }

            @Override
            public void getImageFailure(String errorMessage) {
                Toast.makeText(UpdateProfileActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
            }
        });


        user.getUserInfo(userId, new UserCallback() {
            @Override
            public void getUserInfoSuccess(User user) {
                binding.txtFullname.setText(user.getFullname());
                binding.txtPhoneNumber.setText(user.getPhoneNumber());
                binding.txtEmail.setText(user.getEmail() != null ? user.getEmail() : "");
            }

            @Override
            public void getUserInfoFailure(String errorMessage) {
                Toast.makeText(UpdateProfileActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }


    ActivityResultLauncher<Intent> myLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        imageUri = result.getData().getData();
                        Glide.with(getApplicationContext()).load(imageUri).into(binding.imvAvatar);
                    } else {
                        Toast.makeText(UpdateProfileActivity.this, IMAGE_REQUIRE, Toast.LENGTH_SHORT).show();
                    }
                }
            });
}