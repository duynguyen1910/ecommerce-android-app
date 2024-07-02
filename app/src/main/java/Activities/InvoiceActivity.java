package Activities;
import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.stores.R;
import com.example.stores.databinding.ActivityInvoiceBinding;
import com.example.stores.databinding.ItemTabLayoutBinding;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import java.util.Objects;
import Adapters.ViewPager2Adapter;
import Fragments.InvoiceAwaitConfirmationFragment;
import Fragments.InvoiceAwaitDeliveryFragment;
import Fragments.InvoiceAwaitPickupFragment;
import Fragments.InvoiceCancelFragment;
import Fragments.InvoiceCompletedFragment;
public class InvoiceActivity extends AppCompatActivity {

    ActivityInvoiceBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityInvoiceBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initUI();
        setupUI();

        setupEvents();

    }

    private void setupUI() {
        ViewPager2Adapter viewPager2Adapter = new ViewPager2Adapter(this);
        viewPager2Adapter.addFragment(new InvoiceAwaitConfirmationFragment(), "Chờ xác nhận");
        viewPager2Adapter.addFragment(new InvoiceAwaitDeliveryFragment(), "Chờ giao hàng");
        viewPager2Adapter.addFragment(new InvoiceAwaitPickupFragment(), "Chờ lấy hàng");
        viewPager2Adapter.addFragment(new InvoiceCompletedFragment(), "Hoàn thành");
        viewPager2Adapter.addFragment(new InvoiceCancelFragment(), "Đã hủy");

        binding.viewPager2.setAdapter(viewPager2Adapter);
        binding.viewPager2.setCurrentItem(0);
        new TabLayoutMediator(binding.tabLayout, binding.viewPager2, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                ItemTabLayoutBinding tabLayoutBinding = ItemTabLayoutBinding.inflate(getLayoutInflater());
                TextView tabLabel = tabLayoutBinding.tabLabel;
                if (position == 0) {
                    tabLabel.setText(viewPager2Adapter.getPageTitle(0));
                } else if (position == 1) {
                    tabLabel.setText(viewPager2Adapter.getPageTitle(1));
                }else if (position == 2) {
                    tabLabel.setText(viewPager2Adapter.getPageTitle(2));
                }else if (position == 3) {
                    tabLabel.setText(viewPager2Adapter.getPageTitle(3));
                }else if (position == 4) {
                    tabLabel.setText(viewPager2Adapter.getPageTitle(4));
                }

                tab.setCustomView(tabLayoutBinding.getRoot());
            }
        }).attach();
        binding.tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                View customView = tab.getCustomView();
                if (customView != null) {
                    TextView tabLabel = customView.findViewById(R.id.tabLabel);
                    tabLabel.setTextColor(ContextCompat.getColor(InvoiceActivity.this, R.color.pink));
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                View customView = tab.getCustomView();
                if (customView != null) {
                    TextView tabLabel = customView.findViewById(R.id.tabLabel);
                    tabLabel.setTextColor(ContextCompat.getColor(InvoiceActivity.this, R.color.lightpink));
                }
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                // Optional: Handle tab reselection if needed
            }
        });
    }
    private void setupEvents() {
        binding.imageBack.setOnClickListener(v -> finish());
    }

    private void initUI() {
        getWindow().setStatusBarColor(Color.parseColor("#F04D7F"));
        Objects.requireNonNull(getSupportActionBar()).hide();
    }
}