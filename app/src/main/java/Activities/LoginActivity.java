package Activities;

import static constants.keyName.FULLNAME;
import static constants.keyName.PASSWORD;
import static constants.keyName.PHONE_NUMBER;
import static constants.keyName.STORE_ID;
import static constants.keyName.USER_ID;
import static constants.keyName.USER_INFO;
import static constants.keyName.USER_ROLE;
import static constants.toastMessage.LOGIN_SUCCESSFULLY;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.stores.databinding.ActivityLoginBinding;

import java.util.Objects;

import interfaces.UserCallback;
import models.User;
import constants.toastMessage;

public class LoginActivity extends AppCompatActivity {
    private SharedPreferences sharedPreferences;
    private ActivityLoginBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getWindow().setStatusBarColor(Color.parseColor("#F04D7F"));
        Objects.requireNonNull(getSupportActionBar()).hide();

        setupEvents();
    }

    private void setupEvents() {
        binding.registerRedirectTv.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);

            startActivity(intent);
            finish();
        });

        binding.btnLoginCt.setOnClickListener(v -> {
            onLogin();
        });
    }

    ;

    private void onLogin() {
        String phoneNumber = binding.edtPhoneNumber.getText().toString();
        String password = binding.edtPassword.getText().toString();

        if (phoneNumber.isEmpty()) {
            binding.edtPhoneNumber.setError(toastMessage.PHONE_NUMBER_REQUIRE);
            return;
        }

        if (password.isEmpty()) {
            binding.edtPassword.setError(toastMessage.PASSWORD_REQUIRE);
            return;
        }

        binding.progressBar.setVisibility(View.VISIBLE);
        binding.progressBar.getIndeterminateDrawable()
                .setColorFilter(Color.parseColor("#F04D7F"), PorterDuff.Mode.MULTIPLY);

        User user = new User();
       user.onLogin(phoneNumber, password, new UserCallback() {
           @Override
           public void getUserInfoSuccess(User user) {
               binding.progressBar.setVisibility(View.GONE);
               Toast.makeText(LoginActivity.this, LOGIN_SUCCESSFULLY, Toast.LENGTH_SHORT).show();

               onSaveUserInfo(user);

               Intent resultIntent = new Intent();
               resultIntent.putExtra(LOGIN_SUCCESSFULLY, true);
               setResult(Activity.RESULT_OK, resultIntent);
               finish();
           }
           @Override
           public void getUserInfoFailure(String errorMessage) {
               binding.progressBar.setVisibility(View.GONE);
               Toast.makeText(LoginActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
           }
       });
    }

    private void onSaveUserInfo(User user) {
        sharedPreferences = getSharedPreferences(USER_INFO, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(USER_ID, user.getBaseID());
        editor.putString(PHONE_NUMBER, user.getPhoneNumber());
        editor.putString(FULLNAME, user.getFullname());
        editor.putString(PASSWORD, user.getPassword());
        editor.putInt(USER_ROLE, user.getRole().getRoleValue());
        editor.putString(STORE_ID, user.getStoreID());
        editor.apply();
    }


    private void getDataRememberLogin() {
        sharedPreferences = getSharedPreferences("dataRememberLogin", MODE_PRIVATE);
        boolean checkedStatus = sharedPreferences.getBoolean("checked", false);
        binding.chkRemember.setChecked(checkedStatus);  // Set the checkbox status
        if (checkedStatus) {
            String key_phoneNumber = sharedPreferences.getString("phoneNumber", "");
            String key_password = sharedPreferences.getString("password", "");
            binding.edtPhoneNumber.setText(key_phoneNumber);
            binding.edtPassword.setText(key_password);
        } else {
            binding.edtPhoneNumber.setText("");
            binding.edtPassword.setText("");
        }
    }

    @Override
    protected void onResume() {
        getDataRememberLogin();
        super.onResume();
    }


}