package Activities.BuyProduct;
import static constants.keyName.PAYMENT;
import static constants.keyName.USER_ID;
import static constants.keyName.USER_INFO;
import static utils.Cart.CartUtils.getCartItemFee;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.example.stores.R;
import com.example.stores.databinding.ActivityPaymentBinding;

import java.text.NumberFormat;
import java.util.ArrayList;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

import Activities.MainActivity;
import Adapters.BuyProduct.PaymentAdapter;
import api.invoiceApi;
import api.userApi;
import enums.OrderStatus;
import interfaces.CreateDocumentCallback;
import interfaces.StatusCallback;
import interfaces.UserCallback;
import models.CartItem;
import models.InvoiceDetail;
import models.Product;
import models.User;
import utils.FormatHelper;

public class PaymentActivity extends AppCompatActivity {
    ActivityPaymentBinding binding;
    ArrayList<CartItem> payment = null;
    SharedPreferences sharedPreferences;
    String userID = null;
    User currentUser = null;

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
            payment = (ArrayList<CartItem>) getIntent().getSerializableExtra(PAYMENT);
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

        binding.btnBooking.setOnClickListener(v -> {
            String deliveryAddress = "quận 7, tp.Hồ Chí Minh"; // hard code

            for(CartItem item : payment) {
                Map<String, Object> newInvoice = new HashMap<>();

                newInvoice.put("customerID", userID);
                newInvoice.put("deliveryAddress", deliveryAddress);
                newInvoice.put("total", getCartItemFee(item));
                newInvoice.put("status", OrderStatus.PENDING_CONFIRMATION.getOrderStatusValue());
                newInvoice.put("createdAt", FormatHelper.getCurrentDateTime());
                newInvoice.put("note", item.getNote());
                newInvoice.put("storeID", item.getStoreID());

                binding.progressBar.setVisibility(View.VISIBLE);
                binding.progressBar.getIndeterminateDrawable()
                        .setColorFilter(Color.parseColor("#f04d7f"), PorterDuff.Mode.MULTIPLY);
                binding.btnBooking.setBackground(ContextCompat.getDrawable(this, R.color.darkgray));

                invoiceApi invoiceApi = new invoiceApi();
                invoiceApi.createInvoiceApi(newInvoice, new CreateDocumentCallback() {
                    @Override
                    public void onCreateSuccess(String documentID) {
                        createInvoiceDetail(invoiceApi, documentID, item.getListProducts());
                    }

                    @Override
                    public void onCreateFailure(String errorMessage) {
                        Toast.makeText(PaymentActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                        binding.progressBar.setVisibility(View.GONE);
                    }
                });
            }

        });

//        binding.txtPaymentMethod.setOnClickListener(v -> {
//            Intent intent = new Intent(PaymentActivity.this, PaymentMethodActivity.class);
//            startActivity(intent);
//        });
    }

    private void createInvoiceDetail(invoiceApi invoiceApi, String invoiceID, ArrayList<Product> productList) {

        ArrayList<InvoiceDetail> invoiceDetails = new ArrayList<>();
        for (Product product : productList) {
            invoiceDetails.add(new InvoiceDetail(invoiceID, product.getBaseID(), product.getNumberInCart()));
        }

        invoiceApi.createDetailInvoiceApi(invoiceDetails, new StatusCallback() {
            @Override
            public void onSuccess(String successMessage) {
                binding.progressBar.setVisibility(View.GONE);
                binding.btnBooking.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.color.primary_color));
                Toast.makeText(PaymentActivity.this, successMessage, Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(PaymentActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }

            @Override
            public void onFailure(String errorMessage) {
                binding.progressBar.setVisibility(View.GONE);
                binding.btnBooking.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.color.primary_color));
                Toast.makeText(PaymentActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
            }
        });

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


    private void setupUI() {
        getWindow().setStatusBarColor(Color.parseColor("#F04D7F"));
        Objects.requireNonNull(getSupportActionBar()).hide();

        sharedPreferences = getSharedPreferences(USER_INFO, MODE_PRIVATE);
        userID = sharedPreferences.getString(USER_ID, null);

        userApi userApi = new userApi();
        userApi.getUserInfoApi(userID, new UserCallback() {
            @Override
            public void getUserInfoSuccess(User user) {
                currentUser = user;
                binding.txtCustomerInfo.setText(user.getFullname() + " | " + user.getPhoneNumber());
            }

            @Override
            public void getUserInfoFailure(String errorMessage) {

            }
        });
    }


}