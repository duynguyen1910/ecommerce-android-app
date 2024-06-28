package Activities;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.stores.databinding.ActivityLoginBinding;
import java.util.Objects;

public class LoginActivity extends AppCompatActivity {
    SharedPreferences sharedPreferences;
    ActivityLoginBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getWindow().setStatusBarColor(Color.parseColor("#F04D7F"));
        Objects.requireNonNull(getSupportActionBar()).hide();

        binding.btnSignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    private boolean handleValidateLoginForm(String phoneNumber, String password){
        if (phoneNumber.isEmpty()){
            binding.edtPhoneNumber.setError("Vui lòng nhập trường này");
            return false;
        }
        if (password.isEmpty()){
            binding.edtPassword.setError("Vui lòng nhập trường này");
            return false;
        }
        if (password.length() < 6){
            Toast.makeText(this, "Mật khẩu ít nhất 6 ký tự", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }



    private void getDataRememberLogin() {
        sharedPreferences = getSharedPreferences("dataRememberLogin", MODE_PRIVATE);
        boolean checkedStatus = sharedPreferences.getBoolean("checked", false);
        binding.chkRemember.setChecked(checkedStatus);  // Set the checkbox status
        if (checkedStatus){
            String key_phoneNumber = sharedPreferences.getString("phoneNumber", "");
            String key_password = sharedPreferences.getString("password", "");
            binding.edtPhoneNumber.setText(key_phoneNumber);
            binding.edtPassword.setText(key_password);
        }else {
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