package Activities;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.example.stores.databinding.ActivityRegisterStoreOwnerSuccessfulBinding;

import java.util.Objects;

public class RegisterStoreOwnerSuccessfulActivity extends AppCompatActivity {

    ActivityRegisterStoreOwnerSuccessfulBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterStoreOwnerSuccessfulBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initUI();


        setupEvent();


    }

    private void setupEvent(){
        binding.imageBack.setOnClickListener(v -> {
            Intent intent = new Intent(RegisterStoreOwnerSuccessfulActivity.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        });
//        binding.imageBack.setOnClickListener(v -> finish());
        binding.btnViewStore.setOnClickListener(v -> {
            Intent intent = new Intent(RegisterStoreOwnerSuccessfulActivity.this, StoreOwnerActivity.class);
;            startActivity(intent);
        });

    }

    private void initUI(){
        getWindow().setStatusBarColor(Color.parseColor("#F04D7F"));
        Objects.requireNonNull(getSupportActionBar()).hide();
    }


}