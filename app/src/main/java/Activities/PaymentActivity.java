package Activities;
import static constants.keyName.USER_ID;
import static constants.keyName.USER_INFO;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.stores.R;
import com.example.stores.databinding.ActivityPaymentBinding;
import com.example.stores.databinding.LayoutOrderBinding;
import com.google.firebase.Timestamp;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

import api.invoiceApi;
import enums.OrderStatus;
import interfaces.CreateDocumentCallback;
import interfaces.StatusCallback;
import models.Invoice;
import models.InvoiceDetail;

public class PaymentActivity extends AppCompatActivity {

    ActivityPaymentBinding binding;
    ArrayList<InvoiceDetail> payment = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPaymentBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setupUI();
//        getBundles();
        setupEvents();
    }

    private void getBundles() {

        Intent intent = getIntent();
        if (intent != null) {
//            payment = (ArrayList<InvoiceDetail>) getIntent().getSerializableExtra("payment");

//            PaymentAdapter paymentAdapter = new PaymentAdapter(PaymentActivity.this, payment);
//            binding.recyclerViewPayment.setAdapter(paymentAdapter);
//            binding.recyclerViewPayment.setLayoutManager(new LinearLayoutManager(PaymentActivity.this, LinearLayoutManager.VERTICAL, false));

        }

        calculatorPayment();
    }

    private void setupEvents() {
        binding.imageBack.setOnClickListener(v -> finish());

        binding.btnBooking.setOnClickListener(v -> {
            SharedPreferences sharedPreferences = getSharedPreferences(USER_INFO, MODE_PRIVATE);
            String userId = sharedPreferences.getString(USER_ID, null);

            ArrayList<Invoice> invoiceList = new ArrayList<>();
            Invoice invoice1 = new Invoice(
                    userId, "quận 7, Hồ Chí Minh", 120000, OrderStatus.PENDING_CONFIRMATION,
                    new Timestamp(new java.util.Date()), "Để trước cổng lúc giao hàng"
            );
            invoiceList.add(invoice1);

            Invoice invoice2 = new Invoice(
                    userId, "quận Phú Nhuận, Hồ Chí Minh", 99999, OrderStatus.PENDING_CONFIRMATION,
                    new Timestamp(new java.util.Date()), "Lấy thêm 2 loại nữa"
            );
            invoiceList.add(invoice2);


            for (Invoice invoice : invoiceList) {
                Map<String, Object> newInvoice = new HashMap<>();
                newInvoice.put("customerID", invoice.getCustomerID());
                newInvoice.put("deliveryAddress", invoice.getDeliveryAddress());
                newInvoice.put("total", invoice.getTotal());
                newInvoice.put("status", invoice.getStatus().getOrderStatusValue());
                newInvoice.put("createdAt", invoice.getCreatedAt());
                newInvoice.put("note", invoice.getNote());

                invoiceApi invoiceApi = new invoiceApi();
                invoiceApi.createInvoiceApi(newInvoice, new CreateDocumentCallback() {
                    @Override
                    public void onCreateSuccess(String documentID) {
                        createInvoiceDetail(invoiceApi, documentID);
                    }

                    @Override
                    public void onCreateFailure(String errorMessage) {

                    }
                });
            }

//            startActivity(new Intent(PaymentActivity.this, InvoiceActivity.class));
        });

//        binding.txtPaymentMethod.setOnClickListener(v -> {
//            Intent intent = new Intent(PaymentActivity.this, PaymentMethodActivity.class);
//            startActivity(intent);
//        });
    }

    private void createInvoiceDetail(invoiceApi invoiceApi, String invoiceID) {
        ArrayList<InvoiceDetail> products = new ArrayList<>();
        products.add(new InvoiceDetail(invoiceID, invoiceID, 3));
        products.add(new InvoiceDetail(invoiceID, invoiceID, 5));

        invoiceApi.createDetailInvoice(products, new StatusCallback() {
            @Override
            public void onSuccess(String successMessage) {
                Toast.makeText(PaymentActivity.this, successMessage, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(String errorMessage) {
                Toast.makeText(PaymentActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private String generateTime() {
        Calendar calendar = Calendar.getInstance();
        Date currentDate = calendar.getTime();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd, HH:mm");
        String invoiceId = dateFormat.format(currentDate);
        return invoiceId;
    }

//    private double getTotalForCartItem(CartItem cartItem) {
//        double fee = 0;
//
//        for (Product product : cartItem.getListProducts()) {
//            if (product.getCheckedStatus()) {
//                fee += (product.getOldPrice() * product.getNumberInCart());
//            }
//
//        }
//
//        return fee;
//    }

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
//        binding.txtTotalProductsFee.setText("đ" + formatter.format(getTotalProductsFee()));
        double delivery = 25000;
        double totalDelivery = delivery * payment.size();
        double ecommerceDeliveryDiscount = 50000;

        binding.txtTotalDeliveryFee.setText("đ" + formatter.format(totalDelivery));

//        binding.txtEcommerceDeliveryDiscount.setText("-đ" + formatter.format(ecommerceDeliveryDiscount));
//        binding.txtTotalDiscount.setText("-đ" + formatter.format(ecommerceDeliveryDiscount));

//        double total = getTotalProductsFee() + totalDelivery;
//        binding.txtTotalPayment.setText("đ" + formatter.format(total));
//        binding.txtTotalOrder.setText("đ" + formatter.format(total));
//        return total;

        return 999999;
    }

//    private double getTotalProductsFee() {
//        double fee = 0;
//        for (CartItem item : payment) {
//            for (Product product : item.getListProducts()) {
//                    fee += (product.getNewPrice() * product.getNumberInCart());
//            }
//        }
//        return fee;
//    }


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