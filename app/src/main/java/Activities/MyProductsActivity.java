package Activities;

import static constants.keyName.STORE_ID;
import static constants.keyName.USER_INFO;

import android.annotation.SuppressLint;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.stores.R;
import com.example.stores.databinding.ActivityMyProductsBinding;
import com.example.stores.databinding.ItemTabLayoutProductsBinding;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;
import java.util.Objects;

import Adapters.ViewPager2Adapter;
import Fragments.ProductsInStockFragment;
import Fragments.ProductsOutOfStockFragment;
import interfaces.GetCountCallback;
import models.CartItem;
import models.Product;

public class MyProductsActivity extends AppCompatActivity {

    ActivityMyProductsBinding binding;
    int inStock = -1;
    int outOfStock = -1;
    int countCompleted = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMyProductsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initUI();
        setupUI();
        getCountOfProduct();
        setupEvents();

    }

    @Override
    protected void onResume() {
        super.onResume();
        setupUI();
        getCountOfProduct();

    }

    private void getCountOfProduct() {
        SharedPreferences sharedPreferences = getSharedPreferences(USER_INFO, MODE_PRIVATE);
        String storeId = sharedPreferences.getString(STORE_ID, null);
        Product product = new Product();

        product.countProductsOutOfStockByStoreId(storeId, new GetCountCallback<Product>() {
            @Override
            public void onGetCountSuccess(int count) {
                outOfStock = count;
                countCompleted++;
                updateTabQuantity();
            }

            @Override
            public void onGetCountFailure(String errorMessage) {
                countCompleted++;
                updateTabQuantity();
            }
        });

        product.countProductsInStockByStoreId(storeId, new GetCountCallback<Product>() {
            @Override
            public void onGetCountSuccess(int count) {
                inStock = count;
                countCompleted++;
                updateTabQuantity();
            }

            @Override
            public void onGetCountFailure(String errorMessage) {
                countCompleted++;
                updateTabQuantity();
            }
        });
    }

    private void setupUI() {
        ViewPager2Adapter viewPager2Adapter = new ViewPager2Adapter(this);
        viewPager2Adapter.addFragment(new ProductsInStockFragment(), "Còn hàng");
        viewPager2Adapter.addFragment(new ProductsOutOfStockFragment(), "Hết hàng");

        binding.viewPager2.setAdapter(viewPager2Adapter);
        binding.viewPager2.setCurrentItem(0);

        new TabLayoutMediator(binding.tabLayout, binding.viewPager2, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                ItemTabLayoutProductsBinding tabLayoutBinding = ItemTabLayoutProductsBinding.inflate(getLayoutInflater());
                TextView tabLabel = tabLayoutBinding.tabLabel;
                tabLabel.setText(viewPager2Adapter.getPageTitle(position));
                tab.setCustomView(tabLayoutBinding.getRoot());
            }
        }).attach();
    }

    private void updateTabQuantity() {
        if (countCompleted >= 2) {
            updateTabUI(0, inStock);
            updateTabUI(1, outOfStock);
        }
    }

    private void updateTabUI(int index, int quantity) {
        TabLayout.Tab tab = binding.tabLayout.getTabAt(index);
        if (tab != null && tab.getCustomView() != null) {
            TextView tabQuantity = tab.getCustomView().findViewById(R.id.tabQuantity);
            TextView tabLabel = tab.getCustomView().findViewById(R.id.tabLabel);
            if (index == 0){
                tabLabel.setTextColor(ContextCompat.getColor(MyProductsActivity.this, R.color.primary_color));
                tabQuantity.setTextColor(ContextCompat.getColor(MyProductsActivity.this, R.color.primary_color));
            }

            tabQuantity.setText("(" + quantity + ")");
        }
    }

    private void setupEvents() {
        binding.imageBack.setOnClickListener(v -> finish());
        binding.btnAddProducts.setOnClickListener(v -> {
            Intent intent = new Intent(MyProductsActivity.this, AddProductsActivity.class);
            startActivity(intent);
        });
    }

    private void initUI() {
        getWindow().setStatusBarColor(Color.parseColor("#F04D7F"));
        Objects.requireNonNull(getSupportActionBar()).hide();
    }
}