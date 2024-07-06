package Activities;

import android.annotation.SuppressLint;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.stores.R;
import com.example.stores.databinding.ActivityMyProductsBinding;
import com.example.stores.databinding.ItemTabLayoutProductsBinding;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.Objects;

import Adapters.ViewPager2Adapter;
import Fragments.ProductsHiddenFragment;
import Fragments.ProductsInStockFragment;
import Fragments.ProductsOutOfStockFragment;

public class MyProductsActivity extends AppCompatActivity {

    ActivityMyProductsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMyProductsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initUI();
        setupUI();

        setupEvents();

    }

    private void setupUI() {
        ViewPager2Adapter viewPager2Adapter = new ViewPager2Adapter(this);
        viewPager2Adapter.addFragment(new ProductsInStockFragment(), "Còn hàng");
        viewPager2Adapter.addFragment(new ProductsOutOfStockFragment(), "Hết hàng");
        viewPager2Adapter.addFragment(new ProductsHiddenFragment(), "Bị ẩn");

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
        // Đặt màu hồng cho tab đầu tiên sau khi attach()
        TabLayout.Tab firstTab = binding.tabLayout.getTabAt(0);
        if (firstTab != null && firstTab.getCustomView() != null) {
            TextView tabLabel = firstTab.getCustomView().findViewById(R.id.tabLabel);
            TextView tabQuantity = firstTab.getCustomView().findViewById(R.id.tabQuantity);
            tabLabel.setTextColor(ContextCompat.getColor(MyProductsActivity.this, R.color.primary_color));
            tabQuantity.setTextColor(ContextCompat.getColor(MyProductsActivity.this, R.color.primary_color));
        }

        binding.tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                View customView = tab.getCustomView();
                if (customView != null) {
                    TextView tabLabel = customView.findViewById(R.id.tabLabel);
                    TextView tabQuantity = customView.findViewById(R.id.tabQuantity);
                    tabLabel.setTextColor(ContextCompat.getColor(MyProductsActivity.this, R.color.primary_color));
                    tabQuantity.setTextColor(ContextCompat.getColor(MyProductsActivity.this, R.color.primary_color));
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                View customView = tab.getCustomView();
                if (customView != null) {
                    TextView tabLabel = customView.findViewById(R.id.tabLabel);
                    TextView tabQuantity = customView.findViewById(R.id.tabQuantity);
                    tabLabel.setTextColor(ContextCompat.getColor(MyProductsActivity.this, R.color.darkgray));
                    tabQuantity.setTextColor(ContextCompat.getColor(MyProductsActivity.this, R.color.darkgray));
                }
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private void setupEvents() {
        binding.imageBack.setOnClickListener(v -> finish());
        binding.btnAddProducts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyProductsActivity.this, AddProductsActivity.class);
                startActivity(intent);
            }
        });

    }

    private void initUI() {
        getWindow().setStatusBarColor(Color.parseColor("#F04D7F"));
        Objects.requireNonNull(getSupportActionBar()).hide();


    }
}