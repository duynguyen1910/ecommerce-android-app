package Activities;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;

import com.example.stores.databinding.ActivityActivateStoreBinding;

import java.util.Objects;

public class ActivateStoreActivity extends AppCompatActivity {

    ActivityActivateStoreBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityActivateStoreBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getWindow().setStatusBarColor(Color.parseColor("#F04D7F"));
        Objects.requireNonNull(getSupportActionBar()).hide();

        binding.imageBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }


}