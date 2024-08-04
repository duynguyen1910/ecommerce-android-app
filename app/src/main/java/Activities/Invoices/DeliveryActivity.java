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
import com.example.stores.databinding.ActivityDeliveryBinding;

import com.example.stores.databinding.ItemTabLabelAndQuantityBinding;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.Objects;

import Activities.MainActivity;
import Adapters.ViewPager2Adapter;
import Fragments.Delivery.DeliveryAwaitPickUpFragment;
import Fragments.Delivery.DeliveryBeingTransportedFragment;
import Fragments.Delivery.DeliveryCancelledFragment;
import Fragments.Delivery.DeliveryCompletedFragment;
import api.invoiceApi;
import enums.OrderStatus;
import interfaces.GetAggregateCallback;
import interfaces.InAdapter.UpdateCountListener;
import utils.DecorateUtils;

public class DeliveryActivity extends AppCompatActivity implements UpdateCountListener {

    ActivityDeliveryBinding binding;
    int pendingShipmentQuantity = 0;
    int inTransitQuantity = 0;
    int deliveredQuantity = 0;
    int cancelledQuantity = 0;
    int countCompleted = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDeliveryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initUI();
        setupUI();
        getCountOfInvoices();
        setupEvents();

    }
    @Override
    protected void onResume() {
        super.onResume();
        getCountOfInvoices();
    }

    private void getCountOfInvoices() {

        invoiceApi myInvoiceApi = new invoiceApi();
        myInvoiceApi.countDeliveryInvoicesByStatus(
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
        myInvoiceApi.countDeliveryInvoicesByStatus(
                OrderStatus.IN_TRANSIT.getOrderStatusValue(),
                new GetAggregateCallback() {
                    @Override
                    public void onSuccess(double aggregateResult) {
                        inTransitQuantity = (int) aggregateResult;
                        countCompleted++;
                        updateTabQuantity();
                    }

                    @Override
                    public void onFailure(String errorMessage) {
                        countCompleted++;
                        updateTabQuantity();
                    }
                });
        myInvoiceApi.countDeliveryInvoicesByStatus(

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


        myInvoiceApi.countDeliveryInvoicesByStatus(
                OrderStatus.CANCELLED.getOrderStatusValue(),
                new GetAggregateCallback() {
                    @Override
                    public void onSuccess(double aggregateResult) {
                        cancelledQuantity = (int) aggregateResult;
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
            updateTabLayout(0, pendingShipmentQuantity);
            updateTabLayout(1, inTransitQuantity);
            updateTabLayout(2, deliveredQuantity);
            updateTabLayout(3, cancelledQuantity);
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
        viewPager2Adapter.addFragment(new DeliveryAwaitPickUpFragment(), "Chờ lấy hàng"); //0
        viewPager2Adapter.addFragment(new DeliveryBeingTransportedFragment(), "Đang vận chuyển"); //1
        viewPager2Adapter.addFragment(new DeliveryCompletedFragment(), "Hoàn thành"); //2
        viewPager2Adapter.addFragment(new DeliveryCancelledFragment(), "Đã hủy"); //3

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
                    DecorateUtils.decorateSelectedTextViews(DeliveryActivity.this, tabLabel, tabQuantity);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                View customView = tab.getCustomView();
                if (customView != null) {
                    TextView tabLabel = customView.findViewById(R.id.tabLabel);
                    TextView tabQuantity = tab.getCustomView().findViewById(R.id.tabQuantity);
                    DecorateUtils.decorateUnselectedTextViews(DeliveryActivity.this, tabLabel, tabQuantity);
                }
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


        TabLayout.Tab tab = binding.tabLayout.getTabAt(0);
        if (tab != null && tab.getCustomView() != null) {
            TextView tabLabel = tab.getCustomView().findViewById(R.id.tabLabel);
            TextView tabQuantity = tab.getCustomView().findViewById(R.id.tabQuantity);
            DecorateUtils.decorateSelectedTextViews(DeliveryActivity.this, tabLabel, tabQuantity);
        }
    }


    private void setupEvents() {
        binding.imageBack.setOnClickListener(v -> finish());
        binding.imvHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DeliveryActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
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
        getCountOfInvoices();
    }
}