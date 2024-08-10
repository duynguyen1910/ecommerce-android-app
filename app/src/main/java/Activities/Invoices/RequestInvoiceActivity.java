package Activities.Invoices;

import static constants.keyName.STORE_ID;
import static constants.keyName.USER_INFO;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.stores.R;
import com.example.stores.databinding.ActivityRequestInvoiceBinding;
import com.example.stores.databinding.ItemTabLabelAndQuantityBinding;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.Objects;

import Activities.MainActivity;
import Adapters.ViewPager2Adapter;
import Fragments.RequestedInvoices.AwaitConfirmFragment;
import Fragments.RequestedInvoices.CancelledRequestFragment;
import Fragments.RequestedInvoices.ConfirmedFragment;
import Fragments.RequestedInvoices.DeliveredRequestFragment;
import api.invoiceApi;
import enums.OrderStatus;
import interfaces.GetAggregateCallback;
import interfaces.InAdapter.UpdateCountListener;
import utils.DecorateUtils;

public class RequestInvoiceActivity extends AppCompatActivity implements UpdateCountListener {

    ActivityRequestInvoiceBinding binding;
    int pendingConfirmQuantity = 0;
    int pendingShipmentQuantity = 0;
    int canceledQuantity = 0;
    int deliveredQuantity = 0;
    int countCompleted = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRequestInvoiceBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initUI();
        setupUI();
        setupEvents();

    }

    @Override
    protected void onResume() {
        super.onResume();
        getCountOfRequestInvoices();
    }

    private void getCountOfRequestInvoices() {
        SharedPreferences sharedPreferences = getSharedPreferences(USER_INFO, MODE_PRIVATE);
        String storeId = sharedPreferences.getString(STORE_ID, null);
        invoiceApi myInvoiceApi = new invoiceApi();

        myInvoiceApi.countRequestInvoicesByStoreIDAndStatus(
                storeId,
                OrderStatus.PENDING_CONFIRMATION.getOrderStatusValue(),
                new GetAggregateCallback() {
                    @Override
                    public void onSuccess(double aggregateResult) {
                        pendingConfirmQuantity = (int) aggregateResult;
                        countCompleted++;
                        updateTabQuantity();
                    }

                    @Override
                    public void onFailure(String errorMessage) {
                        countCompleted++;
                        updateTabQuantity();
                    }
                });

        myInvoiceApi.countRequestInvoicesByStoreIDAndStatus(
                storeId,
                OrderStatus.PENDING_SHIPMENT.getOrderStatusValue(),
                new GetAggregateCallback() {
                    @Override
                    public void onSuccess(double aggregateResult) {
                        pendingShipmentQuantity = (int) aggregateResult;
                        countCompleted++;
                        updateTabQuantity();
                    }

                    @Override
                    public void onFailure(String errorMessage) {
                        countCompleted++;
                        updateTabQuantity();
                    }
                });
        myInvoiceApi.countRequestInvoicesByStoreIDAndStatus(
                storeId,
                OrderStatus.CANCELLED.getOrderStatusValue(),
                new GetAggregateCallback() {
                    @Override
                    public void onSuccess(double aggregateResult) {
                        canceledQuantity = (int) aggregateResult;
                        countCompleted++;
                        updateTabQuantity();
                    }

                    @Override
                    public void onFailure(String errorMessage) {
                        countCompleted++;
                        updateTabQuantity();
                    }
                });
        myInvoiceApi.countRequestInvoicesByStoreIDAndStatus(
                storeId,
                OrderStatus.DELIVERED.getOrderStatusValue(),
                new GetAggregateCallback() {
                    @Override
                    public void onSuccess(double aggregateResult) {
                        deliveredQuantity = (int) aggregateResult;
                        countCompleted++;
                        updateTabQuantity();
                    }

                    @Override
                    public void onFailure(String errorMessage) {
                        countCompleted++;
                        updateTabQuantity();
                    }
                });


    }
    private void updateTabQuantity() {
        if (countCompleted >= 4) {
            updateTabLayout(0, pendingConfirmQuantity);
            updateTabLayout(1, pendingShipmentQuantity);
            updateTabLayout(2, canceledQuantity);
            updateTabLayout(3, deliveredQuantity);
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
        viewPager2Adapter.addFragment(new ConfirmedFragment(), "Chờ lấy hàng"); // 1
        viewPager2Adapter.addFragment(new CancelledRequestFragment(), "Đơn hủy"); // 2
        viewPager2Adapter.addFragment(new DeliveredRequestFragment(), "Hoàn thành"); // 3

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
            int tabSelected = intent.getIntExtra("tabSelected", 0);
            binding.viewPager2.setCurrentItem(tabSelected);

            TabLayout.Tab tab = binding.tabLayout.getTabAt(tabSelected);
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

    @Override
    public void updateCount() {
        countCompleted = 0;
        getCountOfRequestInvoices();
    }
}