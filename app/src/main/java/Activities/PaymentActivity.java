package Activities;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.stores.R;
import com.example.stores.databinding.ActivityPaymentBinding;
import com.example.stores.databinding.LayoutOrderBinding;

import java.util.Objects;

import Service.EcommerceService;

public class PaymentActivity extends AppCompatActivity {

    ActivityPaymentBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPaymentBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setupUI();
        setupEvents();



    }
    private void setupEvents(){
        binding.imageBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        binding.btnPay.setOnClickListener(v -> {
            Intent intent = new Intent(PaymentActivity.this, EcommerceService.class);
            String data = "Đơn hàng của bạn đã được gửi đến người bán";
            intent.putExtra("data", data);
            startService(intent);
            showThankyouDialog();
        });
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
    private void setupUI(){
        getWindow().setStatusBarColor(Color.parseColor("#F04D7F"));
        Objects.requireNonNull(getSupportActionBar()).hide();

    }


}