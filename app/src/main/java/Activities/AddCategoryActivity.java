package Activities;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.example.stores.databinding.ActivityAddCategoryBinding;
import java.util.Objects;

public class AddCategoryActivity extends AppCompatActivity {

    ActivityAddCategoryBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddCategoryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initUI();
        setupEvents();




    }

    private void setupEvents(){
        binding.imageBack.setOnClickListener(v -> finish());
        binding.layoutAddProducts.setOnClickListener(v -> {
            Intent intent = new Intent(AddCategoryActivity.this, SelectProductsToCategoryActivity.class);
            startActivity(intent);
        });


    }
    private void initUI(){
        getWindow().setStatusBarColor(Color.parseColor("#F04D7F"));
        Objects.requireNonNull(getSupportActionBar()).hide();

    }


}