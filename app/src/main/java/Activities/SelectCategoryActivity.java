package Activities;

import android.graphics.Color;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.stores.databinding.ActivitySelectCategoryBinding;

import java.util.ArrayList;
import java.util.Objects;

import Adapters.CategoryAdapter;
import interfaces.GetCategoryCollectionCallback;
import models.Category;

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

        category.getCategoryCollection(new GetCategoryCollectionCallback() {
            @Override
            public void onGetDataSuccess(ArrayList<Category> categories) {
                CategoryAdapter adapter = new CategoryAdapter(SelectCategoryActivity.this, categories);
                binding.recyclerView.setLayoutManager(new LinearLayoutManager(SelectCategoryActivity.this));
                binding.recyclerView.setAdapter(adapter);
            }

            @Override
            public void onGetDataFailure(String errorMessage) {

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