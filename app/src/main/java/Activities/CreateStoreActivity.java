package Activities;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.stores.R;
import com.example.stores.databinding.ActivityCreateStoreBinding;
import com.example.stores.databinding.ItemTabLayout2Binding;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.Objects;

import Adapters.ViewPager2Adapter;
import Fragments.SettingDeliveryFragment;
import Fragments.StoreIdentifierInfoFragment;
import Fragments.StoreInfoFragment;
import Fragments.TaxInfoFragment;
import kotlin.Pair;

public class CreateStoreActivity extends AppCompatActivity {

    ActivityCreateStoreBinding binding;
    int currentState = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCreateStoreBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initUI();
        setupUI();

        setupEvents();

    }

    private void setupEvents() {
        binding.imageBack.setOnClickListener(v -> finish());

        binding.btnNext.setOnClickListener(v -> {
            if (currentState < 4) {
                currentState++;
                binding.stepProgressbar.setProgress(new Pair<>(currentState, 100f));
                binding.viewPager2.setCurrentItem(currentState - 1);
                setupButton();
            }
        });

        binding.btnPrevious.setOnClickListener(v -> {
            if (currentState >= 2) {
                currentState--;
                binding.stepProgressbar.setProgress(new Pair<>(currentState, 100f));
                binding.viewPager2.setCurrentItem(currentState - 1);
                setupButton();
            }
        });
    }

    private void setupButton() {
        if (currentState == 1) {
            binding.btnPrevious.setVisibility(View.GONE);
            binding.btnNext.setText("Tiếp theo");
        } else {
            binding.btnPrevious.setVisibility(View.VISIBLE);
            binding.btnPrevious.setText("Quay lại");
            if (currentState >= 4) {
                binding.btnNext.setText("Lưu");
            } else {
                binding.btnNext.setText("Tiếp theo");
            }
        }
    }


    private void setupUI() {
        ViewPager2Adapter viewPager2Adapter = new ViewPager2Adapter(this);
        viewPager2Adapter.addFragment(new StoreInfoFragment(), "Thông tin Store");
        viewPager2Adapter.addFragment(new SettingDeliveryFragment(), "Cài đặt vận chuyển");
        viewPager2Adapter.addFragment(new TaxInfoFragment(), "Thông tin thuế");
        viewPager2Adapter.addFragment(new StoreIdentifierInfoFragment(), "Thông tin định danh");


        binding.viewPager2.setAdapter(viewPager2Adapter);
        binding.viewPager2.setCurrentItem(0);

        new TabLayoutMediator(binding.tabLayout, binding.viewPager2, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                ItemTabLayout2Binding tabLayoutBinding = ItemTabLayout2Binding.inflate(getLayoutInflater());
                TextView tabLabel = tabLayoutBinding.tabLabel;
                tabLabel.setText(viewPager2Adapter.getPageTitle(position));
                tab.setCustomView(tabLayoutBinding.getRoot());
            }
        }).attach();
        // Đặt màu hồng cho tab đầu tiên sau khi attach()
        TabLayout.Tab firstTab = binding.tabLayout.getTabAt(0);
        if (firstTab != null && firstTab.getCustomView() != null) {
            TextView tabLabel = firstTab.getCustomView().findViewById(R.id.tabLabel);
            tabLabel.setTextColor(ContextCompat.getColor(CreateStoreActivity.this, R.color.primary_color));
        }

        binding.tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                View customView = tab.getCustomView();
                if (customView != null) {
                    TextView tabLabel = customView.findViewById(R.id.tabLabel);
                    tabLabel.setTextColor(ContextCompat.getColor(CreateStoreActivity.this, R.color.primary_color));
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                View customView = tab.getCustomView();
                if (customView != null) {
                    TextView tabLabel = customView.findViewById(R.id.tabLabel);
                    tabLabel.setTextColor(ContextCompat.getColor(CreateStoreActivity.this, R.color.darkgray));
                }
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private void initUI() {
        getWindow().setStatusBarColor(Color.parseColor("#F04D7F"));
        Objects.requireNonNull(getSupportActionBar()).hide();


    }


}