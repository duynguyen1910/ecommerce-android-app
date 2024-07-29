package Activities.Invoices;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.stores.R;
import com.example.stores.databinding.ActivityRequestInvoiceBinding;
import com.example.stores.databinding.ItemTabLabelAndQuantityBinding;
import com.example.stores.databinding.ItemTabLabelBinding;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.Objects;

import Activities.MainActivity;
import Adapters.ViewPager2Adapter;
import Fragments.RequestedInvoices.AwaitConfirmFragment;
import Fragments.RequestedInvoices.CancelledFragment;
import Fragments.RequestedInvoices.ConfirmedFragment;
import utils.DecorateUtils;

public class RequestInvoiceActivity extends AppCompatActivity {

    ActivityRequestInvoiceBinding binding;
    int awaitConfirmQuantity = 0;
    int canceledQuantity = 0;
    int confirmedQuantity = 0;
    int countCompleted = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRequestInvoiceBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initUI();
        setupUI();
        getCountOfInvoices();
        setupEvents();

    }

    private void getCountOfInvoices() {
        countCompleted = 3;
        // call api đếm số lượng invoices của từng loại sau đó update tabQuantity
        // xem MyProductsActivity để xem rõ hơn
        updateTabQuantity();
    }

    private void updateTabQuantity() {
        if (countCompleted >= 3) {
            updateTabLayout(0, awaitConfirmQuantity);
            updateTabLayout(1, confirmedQuantity);
            updateTabLayout(2, canceledQuantity);
        }
    }

    private void updateTabLayout(int index, int quantity) {
        TabLayout.Tab tab = binding.tabLayout.getTabAt(index);
        if (tab != null && tab.getCustomView() != null) {
            TextView tabQuantity = tab.getCustomView().findViewById(R.id.tabQuantity);
            tabQuantity.setText("(" + quantity + ")");
        }
    }

    private void setupUI() {
        ViewPager2Adapter viewPager2Adapter = new ViewPager2Adapter(this);
        viewPager2Adapter.addFragment(new AwaitConfirmFragment(), "Chờ xác nhận"); // 0
        viewPager2Adapter.addFragment(new ConfirmedFragment(), "Đã xác nhận"); // 1
        viewPager2Adapter.addFragment(new CancelledFragment(), "Đơn hủy"); // 2

        binding.viewPager2.setAdapter(viewPager2Adapter);


        new TabLayoutMediator(binding.tabLayout, binding.viewPager2, (tab, position) -> {
            ItemTabLabelAndQuantityBinding tabLayoutBinding = ItemTabLabelAndQuantityBinding.inflate(getLayoutInflater());
            TextView tabLabel = tabLayoutBinding.tabLabel;
            tabLabel.setText(viewPager2Adapter.getPageTitle(position));
            tab.setCustomView(tabLayoutBinding.getRoot());
        }).attach();
        binding.tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                View customView = tab.getCustomView();
                if (customView != null) {
                    TextView tabLabel = customView.findViewById(R.id.tabLabel);
                    TextView tabQuantity = tab.getCustomView().findViewById(R.id.tabQuantity);
                    DecorateUtils.decorateSelectedTextViews(RequestInvoiceActivity.this, tabLabel, tabQuantity);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                View customView = tab.getCustomView();
                if (customView != null) {
                    TextView tabLabel = customView.findViewById(R.id.tabLabel);
                    TextView tabQuantity = tab.getCustomView().findViewById(R.id.tabQuantity);
                    DecorateUtils.decorateUnselectedTextViews(RequestInvoiceActivity.this, tabLabel, tabQuantity);
                }
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        Intent intent = getIntent();
        if (intent != null) {
            int invoiceStatus = intent.getIntExtra("invoiceStatus", 0);
            binding.viewPager2.setCurrentItem(invoiceStatus);

            TabLayout.Tab tab = binding.tabLayout.getTabAt(invoiceStatus);
            if (tab != null && tab.getCustomView() != null) {
                TextView tabLabel = tab.getCustomView().findViewById(R.id.tabLabel);
                TextView tabQuantity = tab.getCustomView().findViewById(R.id.tabQuantity);
                DecorateUtils.decorateSelectedTextViews(RequestInvoiceActivity.this, tabLabel, tabQuantity);
            }
        }
    }


    private void setupEvents() {
        binding.imageBack.setOnClickListener(v -> finish());
        binding.imvHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RequestInvoiceActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });
    }

    private void initUI() {
        getWindow().setStatusBarColor(Color.parseColor("#F04D7F"));
        Objects.requireNonNull(getSupportActionBar()).hide();


    }
}