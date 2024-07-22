package Activities;

import static constants.keyName.PRODUCT_DESC;
import static constants.keyName.PRODUCT_NAME;
import static constants.keyName.PRODUCT_NEW_PRICE;
import static constants.keyName.PRODUCT_OLD_PRICE;
import static constants.keyName.STORE_NAME;
import static constants.toastMessage.INTERNET_ERROR;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.stores.R;
import com.example.stores.databinding.ActivityPaymentBinding;
import com.example.stores.databinding.LayoutOrderBinding;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

import Adapters.PaymentAdapter;
import interfaces.GetDocumentCallback;
import models.CartItem;
import models.Invoice;
import models.Product;
import models.Store;

public class PaymentActivity extends AppCompatActivity {

    ActivityPaymentBinding binding;
    ArrayList<CartItem> payment = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPaymentBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setupUI();
        getBundles();
        setupEvents();


    }

    private void getBundles() {

        Intent intent = getIntent();
        if (intent != null) {
            payment = (ArrayList<CartItem>) getIntent().getSerializableExtra("payment");

            if (payment != null){
                PaymentAdapter paymentAdapter = new PaymentAdapter(PaymentActivity.this, payment);
                binding.recyclerViewPayment.setAdapter(paymentAdapter);
                binding.recyclerViewPayment.setLayoutManager(new LinearLayoutManager(PaymentActivity.this, LinearLayoutManager.VERTICAL, false));
            }


        }

        calculatorPayment();

    }

    private void setupEvents() {
        binding.imageBack.setOnClickListener(v -> finish());
//        binding.btnPay.setOnClickListener(v -> {
//
//            String deliveryAddress = "Ngọc Đại | 012345678\nFPT Polytechnic TP.HCM - Tòa F,\nCông Viên Phần Mềm Quang Trung, Tòa nhà GenPacific \nLô 3 đường 16, Trung Mỹ Tây, Quận 12, Hồ Chí Minh";
//            String createdDate = generateTime();
//            String paidDate = "";
//            String giveToDeliveryDate = "";
//            String completedDate = "";
//            String note = "";
//            int paymentMethod = 0; // 0: Thanh toán khi nhận hàng
//            int orderStatus = 0; // 0: Chờ xác nhận
//            int customerID = 1; // 1: Ngọc Đại
//
//            HashMap<String, Invoice> invoicesMap = new HashMap<>();
//
//            for (int i = 0; i < cart.size(); i++) {
//                Invoice newInvoice = new Invoice(deliveryAddress, createdDate, paidDate, giveToDeliveryDate, completedDate, getTotalForCartItem(cart.get(i)), note, paymentMethod, orderStatus, cart.get(i), customerID);
//                invoicesMap.put(generateInvoiceId(i), newInvoice);
//            }
//            Intent intent = new Intent(PaymentActivity.this, InvoiceActivity.class);
//            intent.putExtra("invoicesMap", invoicesMap);
//            startActivity(intent);
//            // call API gửi order cho Người bán
//        });
//        binding.txtPaymentMethod.setOnClickListener(v -> {
//            Intent intent = new Intent(PaymentActivity.this, PaymentMethodActivity.class);
//            startActivity(intent);
//        });

    }

    private String generateTime() {
        Calendar calendar = Calendar.getInstance();
        Date currentDate = calendar.getTime();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd, HH:mm");
        String invoiceId = dateFormat.format(currentDate);
        return invoiceId;
    }

    private double getTotalForCartItem(CartItem cartItem) {
        double fee = 0;

        for (Product product : cartItem.getListProducts()) {
            if (product.getCheckedStatus()) {
                fee += (product.getOldPrice() * product.getNumberInCart());
            }

        }

        return fee;
    }

    private String generateInvoiceId(int index) {
        Calendar calendar = Calendar.getInstance();
        Date currentDate = calendar.getTime();
        // Định dạng chuỗi ID cho hóa đơn
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        String invoiceId = dateFormat.format(currentDate) + index;
        return invoiceId;
    }

    private String getDateToday() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dinhDangNgay = new SimpleDateFormat("dd/MM/yyyy", Locale.CHINESE);
        return dinhDangNgay.format(calendar.getTime());
    }


    private double calculatorPayment() {
        NumberFormat formatter = NumberFormat.getInstance(new Locale("vi", "VN"));
        binding.txtTotalProductsFee.setText("đ" + formatter.format(getTotalProductsFee()));
        double delivery = 25000;
        double totalDelivery = delivery * payment.size();
        double ecommerceDeliveryDiscount = 50000;

        binding.txtTotalDeliveryFee.setText("đ" + formatter.format(totalDelivery));

//        binding.txtEcommerceDeliveryDiscount.setText("-đ" + formatter.format(ecommerceDeliveryDiscount));
//        binding.txtTotalDiscount.setText("-đ" + formatter.format(ecommerceDeliveryDiscount));

        double total = getTotalProductsFee() + totalDelivery;
        binding.txtTotalPayment.setText("đ" + formatter.format(total));
        binding.txtTotalOrder.setText("đ" + formatter.format(total));
        return total;
    }

    private double getTotalProductsFee() {
        double fee = 0;
        for (CartItem item : payment) {
            for (Product product : item.getListProducts()) {
                    fee += (product.getNewPrice() * product.getNumberInCart());
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