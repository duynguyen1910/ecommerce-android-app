package Activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.stores.databinding.ActivityDecorateMyStoreBinding;


import java.util.Objects;

public class DecorateMyStoreActivity extends AppCompatActivity {

    ActivityDecorateMyStoreBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDecorateMyStoreBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initUI();
        setupEvents();




    }

    private void setupEvents(){
        binding.imageBack.setOnClickListener(v -> finish());
        binding.btnCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DecorateMyStoreActivity.this, CategoryActivity.class);
                startActivity(intent);
            }
        });


    }
    private void initUI(){
        getWindow().setStatusBarColor(Color.parseColor("#F04D7F"));
        Objects.requireNonNull(getSupportActionBar()).hide();

    }


}