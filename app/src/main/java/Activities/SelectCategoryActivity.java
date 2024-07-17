package Activities;

import static constants.toastMessage.INTERNET_ERROR;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.stores.databinding.ActivitySelectCategoryBinding;

import java.util.ArrayList;
import java.util.Objects;

import Adapters.CategoryAdapter;
import interfaces.GetCollectionCallback;
import models.Category;
import models.Product;

public class SelectCategoryActivity extends AppCompatActivity {

    ActivitySelectCategoryBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySelectCategoryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initUI();
        setupRecyclerView();
        setupEvent();





    }
    private void setupRecyclerView(){

        Category category = new Category();

        binding.progressBar.setVisibility(View.VISIBLE);

        category.getCategoryCollection(new GetCollectionCallback<Category>() {
            @Override
            public void onGetDataSuccess(ArrayList<Category> categories) {
                binding.progressBar.setVisibility(View.GONE);
                CategoryAdapter adapter = new CategoryAdapter(SelectCategoryActivity.this, categories);
                binding.recyclerView.setLayoutManager(new LinearLayoutManager(SelectCategoryActivity.this));
                binding.recyclerView.setAdapter(adapter);
            }

            @Override
            public void onGetDataFailure(String errorMessage) {
                Toast.makeText(SelectCategoryActivity.this, INTERNET_ERROR, Toast.LENGTH_SHORT).show();
            }
        });


    }
    private void initUI(){
        getWindow().setStatusBarColor(Color.parseColor("#F04D7F"));
        Objects.requireNonNull(getSupportActionBar()).hide();
    }
    private void setupEvent(){
        binding.imageBack.setOnClickListener(v -> finish());
    }


}