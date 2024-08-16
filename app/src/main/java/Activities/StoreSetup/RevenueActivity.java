package Activities.StoreSetup;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.example.stores.R;
import com.example.stores.databinding.ActivityRevenueBinding;
import com.example.stores.databinding.ItemTabLabelBinding;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.Objects;

import Adapters.ViewPager2Adapter;
import Fragments.Store.RevenueFragment;
import Fragments.Store.StatisticsProductsFragment;
import models.Product;
import utils.DecorateUtils;

public class RevenueActivity extends AppCompatActivity {


    private ActivityRevenueBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRevenueBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initUI();
        setupUI();
        setupEvents();
    }

    private void setupEvents() {
        binding.btnBack.setOnClickListener(v -> finish());
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void initUI() {
        getWindow().setStatusBarColor(Color.parseColor("#F04D7F"));
        Objects.requireNonNull(getSupportActionBar()).hide();
    }



    private void setupUI() {
        ViewPager2Adapter viewPager2Adapter = new ViewPager2Adapter(this);
        viewPager2Adapter.addFragment(new RevenueFragment(), "Doanh thu");
        viewPager2Adapter.addFragment(new StatisticsProductsFragment(), "Sản phẩm");
        binding.viewPager2.setAdapter(viewPager2Adapter);


        new TabLayoutMediator(binding.tabLayout, binding.viewPager2, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                ItemTabLabelBinding tabLayoutBinding = ItemTabLabelBinding.inflate(getLayoutInflater());
                TextView tabLabel = tabLayoutBinding.tabLabel;
                tabLabel.setText(viewPager2Adapter.getPageTitle(position));
                tab.setCustomView(tabLayoutBinding.getRoot());
            }
        }).attach();
        // Đặt màu hồng cho tab đầu tiên sau khi attach()
        TabLayout.Tab firstTab = binding.tabLayout.getTabAt(0);
        if (firstTab != null){
            View customView = firstTab.getCustomView();
            TextView tabLabel = customView.findViewById(R.id.tabLabel);
            DecorateUtils.decorateSelectedTextViews(RevenueActivity.this, tabLabel);
        }


        binding.tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                View customView = tab.getCustomView();
                if (customView != null) {
                    TextView tabLabel = customView.findViewById(R.id.tabLabel);
                    DecorateUtils.decorateSelectedTextViews(RevenueActivity.this, tabLabel);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                View customView = tab.getCustomView();
                if (customView != null) {
                    TextView tabLabel = customView.findViewById(R.id.tabLabel);
                    DecorateUtils.decorateUnselectedTextViews(RevenueActivity.this, tabLabel);
                }
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

}