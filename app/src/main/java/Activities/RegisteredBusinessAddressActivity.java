package Activities;

import android.graphics.Color;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.stores.databinding.ActivityRegisteredBusinessAddressBinding;


import java.util.Objects;

public class RegisteredBusinessAddressActivity extends AppCompatActivity {

    ActivityRegisteredBusinessAddressBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisteredBusinessAddressBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initUI();
        setupEvents();

    }

    private void setupEvents() {
        binding.imageBack.setOnClickListener(v -> finish());
        binding.btnSave.setOnClickListener(v -> finish());
        binding.btnPrevious.setOnClickListener(v -> finish());
    }


    private void initUI() {
        getWindow().setStatusBarColor(Color.parseColor("#F04D7F"));
        Objects.requireNonNull(getSupportActionBar()).hide();
    }


}