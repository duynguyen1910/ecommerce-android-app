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
import com.example.stores.databinding.ActivityRequestInvoiceBinding;
import com.example.stores.databinding.ItemTabLayoutInvoiceBinding;
import com.example.stores.databinding.ItemTabLayoutProductsBinding;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.Objects;

import Adapters.ViewPager2Adapter;
import Fragments.CustomerInvoices.InvoiceAwaitConfirmationFragment;
import Fragments.CustomerInvoices.InvoiceAwaitDeliveryFragment;
import Fragments.ProductsInStockFragment;
import Fragments.ProductsOutOfStockFragment;
import Fragments.RequestedInvoices.RequestInvoiceAwaitConfirmFragment;
import Fragments.RequestedInvoices.RequestInvoiceCancelFragment;

public class RequestInvoiceActivity extends AppCompatActivity {

    ActivityRequestInvoiceBinding binding;
    int awaitConfirmQuantity = -1;
    int canceledQuantity = -1;
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
    private void getCountOfInvoices(){
        countCompleted = 2;
        // call api đếm số lượng invoices của từng loại sau đó update tabQuantity
        updateTabQuantity();
    }
    private void updateTabQuantity() {
        if (countCompleted >= 2) {
            updateTabUI(0, awaitConfirmQuantity);
            updateTabUI(1, canceledQuantity);
        }
    }
    private void updateTabUI(int index, int quantity) {
        TabLayout.Tab tab = binding.tabLayout.getTabAt(index);
        if (tab != null && tab.getCustomView() != null) {
            TextView tabQuantity = tab.getCustomView().findViewById(R.id.tabQuantity);
            TextView tabLabel = tab.getCustomView().findViewById(R.id.tabLabel);
            if (index == 0){
                tabLabel.setTextColor(ContextCompat.getColor(RequestInvoiceActivity.this, R.color.primary_color));
                tabQuantity.setTextColor(ContextCompat.getColor(RequestInvoiceActivity.this, R.color.primary_color));
            }

            tabQuantity.setText("(" + quantity + ")");
        }
    }

    private void setupUI() {
        ViewPager2Adapter viewPager2Adapter = new ViewPager2Adapter(this);
        viewPager2Adapter.addFragment(new RequestInvoiceAwaitConfirmFragment(), "Đơn chờ xác nhận");
        viewPager2Adapter.addFragment(new RequestInvoiceCancelFragment(), "Đơn hủy");

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

//        Intent intent = getIntent();
//        if (intent!= null){
//            int invoiceStatus = intent.getIntExtra("invoiceStatus", 0);
//            binding.viewPager2.setCurrentItem(invoiceStatus);
//
//            TabLayout.Tab firstTab = binding.tabLayout.getTabAt(invoiceStatus);
//            if (firstTab != null && firstTab.getCustomView() != null) {
//                TextView tabLabel = firstTab.getCustomView().findViewById(R.id.tabLabel);
//                tabLabel.setTextColor(ContextCompat.getColor(RequestInvoiceActivity.this, R.color.primary_color));
//            }
//        }
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