package Activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.stores.databinding.ActivityInvoiceDetailBinding;

import java.text.NumberFormat;
import java.util.Locale;
import java.util.Objects;
import Adapters.ProductsAdapterForInvoiceDetail;
import models.Invoice;

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
            Invoice invoice = (Invoice) intent.getSerializableExtra("invoice");
            if (invoice != null){
                binding.txtAddress.setText(invoice.getDeliveryAddress());
                binding.txtStoreName.setText(invoice.getCartItem().getStoreName());

                NumberFormat formatter = NumberFormat.getInstance(new Locale("vi", "VN"));
                binding.txtTotal.setText("đ" + formatter.format(invoice.getTotal()));

                ProductsAdapterForInvoiceDetail adapter = new ProductsAdapterForInvoiceDetail(InvoiceDetailActivity.this, invoice.getCartItem().getListProducts());
                binding.recyclerView.setAdapter(adapter);
                binding.recyclerView.setLayoutManager(new LinearLayoutManager(InvoiceDetailActivity.this, LinearLayoutManager.VERTICAL, false));

                binding.txtPaidDate.setText(invoice.getPaidDate());
                if (invoice.getPaidDate() == ""){
                    binding.txtPaidDate.setText("Chưa thanh toán");
                }
                binding.txtGiveToDeliveryDate.setText(invoice.getGiveToDeliveryDate());
                if (invoice.getPaidDate() == ""){
                    binding.txtGiveToDeliveryDate.setText("Chưa bàn giao");
                }
                binding.txtCompletedDate.setText(invoice.getCompletedDate());
                if (invoice.getPaidDate() == ""){
                    binding.txtCompletedDate.setText("Chưa hoàn thành");
                }

                if (invoice.getPaymentMethod() == 0){
                    binding.txtPaymentMethod.setText("Thanh toán khi nhận hàng");
                }

                binding.txtInvoiceID.setText(invoice.getInvoiceID());
                binding.txtCreatedDate.setText(invoice.getCreatedDate());
            }

        }
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