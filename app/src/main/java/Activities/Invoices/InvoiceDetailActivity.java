package Activities.Invoices;

import static constants.keyName.STORE_NAME;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.stores.databinding.ActivityInvoiceDetailBinding;

import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;
import Adapters.Invoices.InvoiceDetailAdapter;
import api.invoiceApi;
import api.storeApi;
import enums.OrderStatus;
import interfaces.GetCollectionCallback;
import interfaces.GetDocumentCallback;
import models.InvoiceDetail;
import utils.FormatHelper;

public class InvoiceDetailActivity extends AppCompatActivity {

    ActivityInvoiceDetailBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityInvoiceDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initUI();
        setupUI();
        setupEvent();

    }

    private void setupUI(){
        Intent intent = getIntent();
        if (intent != null) {
            Bundle bundle = intent.getExtras();
            String invoiceID = bundle.getString("invoiceID");
            String deliveryAddress = bundle.getString("deliveryAddress");
            String invoiceStatusLabel = bundle.getString("invoiceStatusLabel");

            String createdAt = bundle.getString("createdAt");
            String confirmedAt = bundle.getString("confirmedAt");
            String shippedAt = bundle.getString("shippedAt");
            String deliveredAt = bundle.getString("deliveredAt");

            double invoiceTotal = bundle.getDouble("invoiceTotal");

            binding.progressBar.setVisibility(View.VISIBLE);
            binding.progressBar.getIndeterminateDrawable()
                    .setColorFilter(Color.parseColor("#F04D7F"), PorterDuff.Mode.MULTIPLY);

            binding.txtAddress.setText(deliveryAddress);
            binding.txtInvoiceStatus.setText("Đơn hàng của bạn " + invoiceStatusLabel);

            binding.txtTotal.setText(FormatHelper.formatVND(invoiceTotal));
            binding.txtInvoiceID.setText(invoiceID);

            binding.txtCreatedDate.setText(createdAt);

            if(!confirmedAt.isEmpty()) {
                binding.confirmedAtRL.setVisibility(View.VISIBLE);
                binding.txtConfirmedAt.setText(confirmedAt);
            }

            if(!shippedAt.isEmpty()) {
                binding.shippedAtRL.setVisibility(View.VISIBLE);
                binding.txtShippedAt.setText(shippedAt);
            }

            if(!deliveredAt.isEmpty()) {
                binding.deliveredAtRL.setVisibility(View.VISIBLE);
                binding.txtDeliveredAt.setText(deliveredAt);
            }

           binding.btnCancelInvoice.setVisibility(invoiceStatusLabel.equals(
                   OrderStatus.PENDING_CONFIRMATION.getOrderLabel()) ? View.VISIBLE : View.GONE);


            invoiceApi invoiceApi = new invoiceApi();
            invoiceApi.getInvoiceDetailApi(invoiceID, new GetCollectionCallback<InvoiceDetail>() {
                @Override
                public void onGetListSuccess(ArrayList<InvoiceDetail> productList) {
                    binding.progressBar.setVisibility(View.GONE);

                    getStoreNameByID(productList);
                    InvoiceDetailAdapter adapter =
                            new InvoiceDetailAdapter(InvoiceDetailActivity.this,
                                    productList);
                    binding.recyclerView.setAdapter(adapter);
                    binding.recyclerView.setLayoutManager(new LinearLayoutManager(
                            InvoiceDetailActivity.this, LinearLayoutManager.VERTICAL,
                            false));
                }

                @Override
                public void onGetListFailure(String errorMessage) {
                    binding.progressBar.setVisibility(View.GONE);
                    Toast.makeText(InvoiceDetailActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                }
            });

        }
    }

    private void getStoreNameByID(ArrayList<InvoiceDetail> productList) {
        storeApi storeApi = new storeApi();

        storeApi.getStoreDetailApi(productList.get(0).getStoreID(), new GetDocumentCallback() {
            @Override
            public void onGetDataSuccess(Map<String, Object> data) {
                binding.txtStoreName.setText(" " + (CharSequence) data.get(STORE_NAME));
            }

            @Override
            public void onGetDataFailure(String errorMessage) {
                Toast.makeText(InvoiceDetailActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupEvent(){
        binding.imageBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initUI(){
        getWindow().setStatusBarColor(Color.parseColor("#F04D7F"));
        Objects.requireNonNull(getSupportActionBar()).hide();
    }


}