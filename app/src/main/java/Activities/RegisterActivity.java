package Activities;

import static constants.keyName.FULLNAME;
import static constants.keyName.PASSWORD;
import static constants.keyName.PHONE_NUMBER;
import static constants.keyName.STORE_ID;
import static constants.keyName.USER_ID;
import static constants.keyName.USER_INFO;
import static constants.keyName.USER_ROLE;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.stores.databinding.ActivityLoginBinding;
import com.example.stores.databinding.ActivityRegisterBinding;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import constants.toastMessage;
import interfaces.LoginCallback;
import interfaces.RegisterCallback;
import models.User;
import enums.UserRole;

public class RegisterActivity extends AppCompatActivity {
    private SharedPreferences sharedPreferences;
    private ActivityRegisterBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getWindow().setStatusBarColor(Color.parseColor("#F04D7F"));
        Objects.requireNonNull(getSupportActionBar()).hide();

        setupEvents();
    }

    private void setupEvents(){
        binding.loginRedirectTv.setOnClickListener(v -> {
            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        });

        binding.btnSigninCt.setOnClickListener(v -> {
            onRegister();
        });
    };

    private void onRegister() {
        String phoneNumber = binding.edtPhoneNumber.getText().toString();
        String fullname = binding.edtFullname.getText().toString();
        String password = binding.edtPassword.getText().toString();
        String rePassword = binding.edtRePassword.getText().toString();

        if (phoneNumber.isEmpty()) {
            binding.edtPhoneNumber.setError(toastMessage.PHONE_NUMBER_REQUIRE);
            return;
        }

        if (fullname.isEmpty()) {
            binding.edtFullname.setError(toastMessage.FULLNAME_REQUIRE);
            return;
        }

        if (password.isEmpty()) {
            binding.edtPassword.setError(toastMessage.PASSWORD_REQUIRE);
            return;
        }

        if (rePassword.isEmpty()) {
            binding.edtRePassword.setError(toastMessage.PASSWORD_REQUIRE);
            return;
        }

        binding.progressBar.setVisibility(View.VISIBLE);
        binding.progressBar.getIndeterminateDrawable()
                .setColorFilter(Color.parseColor("#F04D7F"), PorterDuff.Mode.MULTIPLY);

        User user = new User();
        Map<String, Object> newUser = new HashMap<>();
        newUser.put(PHONE_NUMBER, phoneNumber);
        newUser.put(FULLNAME, fullname);
        newUser.put(PASSWORD, rePassword);
        newUser.put(USER_ROLE, UserRole.CUSTOMER_ROLE.getRoleValue());
        newUser.put(STORE_ID, null);


        user.onRegister(newUser, new RegisterCallback() {
            @Override
            public void onRegisterSuccess(String successMessage) {
                binding.progressBar.setVisibility(View.GONE);
                Toast.makeText(RegisterActivity.this, successMessage, Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
//                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }

            @Override
            public void onRegisterFailure(String errorMessage) {
                binding.progressBar.setVisibility(View.GONE);
                Toast.makeText(RegisterActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }

}