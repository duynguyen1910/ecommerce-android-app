package Activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.stores.R;
import com.example.stores.databinding.ActivityPaymentBinding;
import com.example.stores.databinding.LayoutOrderBinding;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Objects;

import Adapters.CartAdapter;
import Adapters.PaymentAdapter;
import Models.CartItem;
import Models.Product;
import Service.EcommerceService;

public class PaymentActivity extends AppCompatActivity {

    ActivityPaymentBinding binding;
    ArrayList<CartItem> cart = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPaymentBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setupUI();
        getBundles();
        setupEvents();


    }

    private void setupEvents() {
        binding.imageBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        binding.btnPay.setOnClickListener(v -> {
//            Intent intent = new Intent(PaymentActivity.this, EcommerceService.class);
//            String data = "Đơn hàng của bạn đã được gửi đến người bán";
//            intent.putExtra("data", data);
//            startService(intent);
//            showThankyouDialog();
        });
    }

    private void getBundles() {
        Intent intent = getIntent();
        if (intent != null) {
            cart = (ArrayList<CartItem>) intent.getSerializableExtra("payment");
            PaymentAdapter paymentAdapter = new PaymentAdapter(PaymentActivity.this, cart);
            binding.recyclerViewPayment.setAdapter(paymentAdapter);
            binding.recyclerViewPayment.setLayoutManager(new LinearLayoutManager(PaymentActivity.this, LinearLayoutManager.VERTICAL, false));
            calculatorPayment();
        }
    }

    private void calculatorPayment() {

        NumberFormat formatter = NumberFormat.getInstance(new Locale("vi", "VN"));
        binding.txtTotalProductsFee.setText("đ" + formatter.format(getTotalProductsFee()));


        double delivery = 18000;
        double totalDelivery = delivery * cart.size();
        double ecommerceDeliveryDiscount = 50000;

        binding.txtTotalDeliveryFee.setText("đ" + formatter.format(totalDelivery));

        binding.txtEcommerceDeliveryDiscount.setText("-đ" + formatter.format(ecommerceDeliveryDiscount));
        binding.txtTotalDiscount.setText("-đ" + formatter.format(ecommerceDeliveryDiscount));

        double total = getTotalProductsFee() + totalDelivery - ecommerceDeliveryDiscount;
        binding.txtTotalPayment.setText("đ" + formatter.format(total));
        binding.txtTotalOrder.setText("đ" + formatter.format(total));
    }

    private double getTotalProductsFee() {
        double fee = 0;
        for (CartItem item : cart) {
            for (Product product : item.getListProducts()) {
                if (product.getCheckedStatus()) {
                    fee += (product.getPrice() * (1 - product.getSaleoff() / 100) * product.getNumberInCart());
                }

            }
        }
        return fee;
    }


    private void showThankyouDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutOrderBinding orderBinding = LayoutOrderBinding.inflate(getLayoutInflater());
        builder.setView(orderBinding.getRoot());
        AlertDialog dialog = builder.create();
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawableResource(R.drawable.custom_edit_text_border);
        dialog.show();

        orderBinding.imageCancel.setOnClickListener(v -> dialog.dismiss());
    }

    private void setupUI() {
        getWindow().setStatusBarColor(Color.parseColor("#F04D7F"));
        Objects.requireNonNull(getSupportActionBar()).hide();

    }


}