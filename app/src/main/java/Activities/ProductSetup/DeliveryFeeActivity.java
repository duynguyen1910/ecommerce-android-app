package Activities.ProductSetup;

import android.graphics.Color;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.stores.databinding.ActivityDeliveryFeeBinding;

import java.util.Objects;

public class DeliveryFeeActivity extends AppCompatActivity {

    ActivityDeliveryFeeBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDeliveryFeeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initUI();
        setupEvents();




    }

    private void setupEvents(){
        binding.imageBack.setOnClickListener(v -> finish());
        binding.btnSave.setOnClickListener(v -> finish());

    }
    private void initUI(){
        getWindow().setStatusBarColor(Color.parseColor("#F04D7F"));
        Objects.requireNonNull(getSupportActionBar()).hide();

    }


}