package Activities;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

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
        binding.imageBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void initUI(){
        getWindow().setStatusBarColor(Color.parseColor("#F04D7F"));
        Objects.requireNonNull(getSupportActionBar()).hide();
    }


}