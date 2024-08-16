package Activities.Admin;

import static constants.keyName.CATEGORY_NAME;
import static constants.toastMessage.DEFAULT_REQUIRE;
import static utils.Cart.CartUtils.showToast;

import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.stores.R;
import com.example.stores.databinding.ActivityEcomCategoryBinding;
import com.example.stores.databinding.DialogAddEcomCategoryBinding;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import Adapters.Category.EcomCategoryAdapter;
import api.categoryApi;
import interfaces.GetCollectionCallback;
import interfaces.StatusCallback;
import models.Category;

public class EcomCategoryActivity extends AppCompatActivity {

    private ActivityEcomCategoryBinding binding;
    private EcomCategoryAdapter g_adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEcomCategoryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initUI();
        setupRecyclerView();
        setupEvent();


    }

    private void setupRecyclerView() {

        Category category = new Category();

        binding.progressBar.setVisibility(View.VISIBLE);

        category.getCategoryCollection(new GetCollectionCallback<Category>() {
            @Override
            public void onGetListSuccess(ArrayList<Category> categories) {
                binding.progressBar.setVisibility(View.GONE);
                g_adapter = new EcomCategoryAdapter(EcomCategoryActivity.this, categories);
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(EcomCategoryActivity.this,LinearLayoutManager.VERTICAL, false);
                binding.recyclerView.setLayoutManager(linearLayoutManager);
                binding.recyclerView.setAdapter(g_adapter);
            }

            @Override
            public void onGetListFailure(String errorMessage) {
                Toast.makeText(EcomCategoryActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
            }
        });


    }

    private void initUI() {
        getWindow().setStatusBarColor(Color.parseColor("#F04D7F"));
        Objects.requireNonNull(getSupportActionBar()).hide();
    }

    private void popupAddDialog() {
        Dialog dialog = new Dialog(EcomCategoryActivity.this, android.R.style.Theme_Material_Light_Dialog);
        DialogAddEcomCategoryBinding dialogBinding = DialogAddEcomCategoryBinding.inflate(getLayoutInflater());
        dialog.setContentView(dialogBinding.getRoot());


        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        Window window = dialog.getWindow();
        if (window != null) {
            layoutParams.copyFrom(window.getAttributes());
            layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
            layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
            layoutParams.horizontalMargin = (int) (100 * getResources().getDisplayMetrics().density); // 100dp
            window.setAttributes(layoutParams);
        }

        dialog.show();
        Animation slideUp = AnimationUtils.loadAnimation(EcomCategoryActivity.this, R.anim.slide_in_from_right);
        dialogBinding.getRoot().startAnimation(slideUp);

        dialogBinding.btnClose.setOnClickListener(v -> dialog.dismiss());


        dialogBinding.btnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogBinding.progressBar.setVisibility(View.VISIBLE);
                categoryApi m_categoryApi = new categoryApi();
                String newCategoryName = Objects.requireNonNull(dialogBinding.edtCategoryName.getText()).toString().trim();
                if (!newCategoryName.isEmpty()) {
                    Map<String, Object> newCategory = new HashMap<>();
                    newCategory.put(CATEGORY_NAME, newCategoryName);

                    m_categoryApi.createCategory(newCategory, new StatusCallback() {
                        @Override
                        public void onSuccess(String successMessage) {
                            showToast(EcomCategoryActivity.this, successMessage);
                            g_adapter.reloadList(dialogBinding.progressBar, dialog);
                        }

                        @Override
                        public void onFailure(String errorMessage) {
                            showToast(EcomCategoryActivity.this, errorMessage);
                        }
                    });
                } else {
                    dialogBinding.edtCategoryName.setError(DEFAULT_REQUIRE);
                }

            }
        });
    }

    private void setupEvent() {
        binding.imageBack.setOnClickListener(v -> finish());
        binding.btnAddCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupAddDialog();
            }
        });
    }

}