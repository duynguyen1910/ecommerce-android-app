package Activities;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.example.stores.databinding.ActivityStoreOwnerBinding;
import java.util.Objects;

public class StoreOwnerActivity extends AppCompatActivity {

    ActivityStoreOwnerBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityStoreOwnerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initUI();
        setupEvents();




    }

    private void setupEvents(){
        binding.imageBack.setOnClickListener(v -> finish());
        binding.btnViewStore.setOnClickListener(v -> {
            Intent intent = new Intent(StoreOwnerActivity.this, DecorateStoreActivity.class);
            startActivity(intent);
        });

        binding.layoutProducts.setOnClickListener(v -> {
            Intent intent = new Intent(StoreOwnerActivity.this, MyProductsActivity.class);
            startActivity(intent);
        });
    }
    private void initUI(){
        getWindow().setStatusBarColor(Color.parseColor("#F04D7F"));
        Objects.requireNonNull(getSupportActionBar()).hide();

    }


}