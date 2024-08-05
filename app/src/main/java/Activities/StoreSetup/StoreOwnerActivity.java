package Activities.StoreSetup;

import static constants.keyName.STORE_ID;
import static constants.keyName.STORE_NAME;
import static constants.keyName.USER_INFO;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.stores.databinding.ActivityStoreOwnerBinding;

import java.util.Map;
import java.util.Objects;

import Activities.Invoices.RequestInvoiceActivity;
import api.invoiceApi;
import enums.OrderStatus;
import interfaces.GetAggregateCallback;
import interfaces.GetDocumentCallback;
import models.Store;
import utils.FormatHelper;

public class StoreOwnerActivity extends AppCompatActivity {

    ActivityStoreOwnerBinding binding;
    String storeId;
    int pendingConfirmQuantity = 0;
    int pendingShipmentQuantity = 0;
    int canceledQuantity = 0;
    int deliveredQuantity = 0;

    int countCompleted = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityStoreOwnerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initUI();
        setupEvents();


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
                        updateQuantityTextViews();
                    }

                    @Override
                    public void onFailure(String errorMessage) {
                        countCompleted++;
                        updateQuantityTextViews();
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
                        updateQuantityTextViews();
                    }

                    @Override
                    public void onFailure(String errorMessage) {
                        countCompleted++;
                        updateQuantityTextViews();
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
                        updateQuantityTextViews();
                    }

                    @Override
                    public void onFailure(String errorMessage) {
                        countCompleted++;
                        updateQuantityTextViews();
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
                        updateQuantityTextViews();
                    }

                    @Override
                    public void onFailure(String errorMessage) {
                        countCompleted++;
                        updateQuantityTextViews();
                    }
                });


    }

    private void updateQuantityTextViews() {
        if (countCompleted >= 4) {
            FormatHelper.formatQuantityTextView(binding.txtPendingConfirmInvoice, pendingConfirmQuantity);
            FormatHelper.formatQuantityTextView(binding.txtPendingShipmentInvoice, pendingShipmentQuantity);
            FormatHelper.formatQuantityTextView(binding.txtCanceledInvoice, canceledQuantity);
            FormatHelper.formatQuantityTextView(binding.txtDeliveredInvoice, deliveredQuantity);
        }
    }



    @Override
    protected void onResume() {
        super.onResume();
        getCountOfRequestInvoices();
    }

    private void setupEvents() {
        binding.imageBack.setOnClickListener(v -> finish());
        binding.btnViewStore.setOnClickListener(v -> {
            SharedPreferences sharedPreferences = getSharedPreferences(USER_INFO, MODE_PRIVATE);
            String storeId = sharedPreferences.getString(STORE_ID, null);
            Intent intent = new Intent(StoreOwnerActivity.this, ViewMyStoreActivity.class);
            intent.putExtra(STORE_ID, storeId);
            startActivity(intent);
        });

        binding.layoutProducts.setOnClickListener(v -> {
            Intent intent = new Intent(StoreOwnerActivity.this, MyProductsActivity.class);
            startActivity(intent);
        });
        binding.txtViewHistory.setOnClickListener(v -> {
            Intent intent = new Intent(StoreOwnerActivity.this, RequestInvoiceActivity.class);
            intent.putExtra(STORE_ID, storeId);
            startActivity(intent);
        });

        binding.layoutAwaitConfirmedInvoice.setOnClickListener(v -> {
            Intent intent = new Intent(StoreOwnerActivity.this, RequestInvoiceActivity.class);
            intent.putExtra("tabSelected", 0); //0 chờ xác nhận
            startActivity(intent);
        });
        binding.layoutConfirmedInvoice.setOnClickListener(v -> {
            Intent intent = new Intent(StoreOwnerActivity.this, RequestInvoiceActivity.class);
            intent.putExtra("tabSelected", 1); //1 đã xác nhận
            startActivity(intent);
        });
        binding.layoutCanceledInvoice.setOnClickListener(v -> {
            Intent intent = new Intent(StoreOwnerActivity.this, RequestInvoiceActivity.class);
            intent.putExtra("tabSelected", 2); //2 đã hủy
            startActivity(intent);
        });
        binding.layoutDeliveredInvoice.setOnClickListener(v -> {
            Intent intent = new Intent(StoreOwnerActivity.this, RequestInvoiceActivity.class);
            intent.putExtra("tabSelected", 3); //3 hoàn thành
            startActivity(intent);
        });
        binding.imvSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StoreOwnerActivity.this, UpdateStoreInfoActivity.class);
                startActivity(intent);
            }
        });

        binding.layoutRevenue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StoreOwnerActivity.this, RevenueActivity.class);
                startActivity(intent);
            }
        });

    }

    private void initUI() {
        getWindow().setStatusBarColor(Color.parseColor("#F04D7F"));
        Objects.requireNonNull(getSupportActionBar()).hide();

        binding.progressBarStoreName.setVisibility(View.VISIBLE);
        SharedPreferences sharedPreferences = getSharedPreferences(USER_INFO, MODE_PRIVATE);


        storeId = sharedPreferences.getString(STORE_ID, null);
        // lấy thông tin avatar, invoice


        if (storeId != null) {
            Store store = new Store();
            store.onGetStoreDetail(storeId, new GetDocumentCallback() {
                @Override
                public void onGetDataSuccess(Map<String, Object> data) {
                    binding.progressBarStoreName.setVisibility(View.GONE);
                    binding.txtStoreName.setText((CharSequence) data.get(STORE_NAME));

                    // set up UI avatar, invoice
                }

                @Override
                public void onGetDataFailure(String errorMessage) {
                    Toast.makeText(StoreOwnerActivity.this, "Uiii, lỗi mạng rồi :(((", Toast.LENGTH_SHORT).show();
                }
            });


        }

    }


}