package Activities;

import android.graphics.Color;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.example.stores.databinding.ActivitySelectAddressBinding;

import java.util.Objects;

public class SelectAddressActivity extends AppCompatActivity {

    ActivitySelectAddressBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySelectAddressBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getWindow().setStatusBarColor(Color.parseColor("#F04D7F"));
        Objects.requireNonNull(getSupportActionBar()).hide();


        binding.imageBack.setOnClickListener(v -> finish());
        binding.btnAccept.setOnClickListener(v -> finish());

    }


}