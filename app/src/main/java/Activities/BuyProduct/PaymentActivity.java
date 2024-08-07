package Activities.BuyProduct;

import static constants.keyName.DEFAULT_ADDRESS_ID;
import static constants.keyName.DETAILED_ADDRESS;
import static constants.keyName.DISTRICT_NAME;
import static constants.keyName.PROVINCE_NAME;
import static constants.keyName.USER_ID;
import static constants.keyName.USER_INFO;
import static constants.keyName.WARD_NAME;
import static constants.toastMessage.ADDRESS_REQUIRE;
import static constants.keyName.PAYMENT;
import static utils.Cart.CartUtils.getCartItemFee;
import static utils.Cart.CartUtils.showToast;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.example.stores.R;
import com.example.stores.databinding.ActivityPaymentBinding;

import java.util.ArrayList;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import Activities.AddAddress.DeliveryAddressActivity;
import api.addressApi;
import Activities.MainActivity;
import Adapters.BuyProduct.PaymentAdapter;
import api.invoiceApi;
import api.productApi;
import api.userApi;
import enums.OrderStatus;
import interfaces.CreateDocumentCallback;
import interfaces.GetDocumentCallback;
import interfaces.StatusCallback;
import interfaces.UserCallback;
import models.CartItem;
import models.InvoiceDetail;
import models.User;
import models.Variant;
import utils.FormatHelper;

public class PaymentActivity extends AppCompatActivity {
    ActivityPaymentBinding binding;
    ArrayList<CartItem> payment = null;
    SharedPreferences sharedPreferences;
    String userID = null;
    String defaultAddressID = null;
    String tag = "payment5";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPaymentBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setupUI();

        setupEvents();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getBundles();
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
            if(defaultAddressID == null) {
                Toast.makeText(this, ADDRESS_REQUIRE, Toast.LENGTH_SHORT).show();
            }

            for(CartItem item : payment) {
                Map<String, Object> newInvoice = new HashMap<>();

                String  detailedAddress = binding.txtDetailedCustomer.getText().toString();
                String deliveryAddress = binding.txtCustomerAddress.getText().toString();

                newInvoice.put("customerID", userID);
                newInvoice.put("detailedAddress", detailedAddress);
                newInvoice.put("deliveryAddress", deliveryAddress);
                newInvoice.put("total", getCartItemFee(item));
                newInvoice.put("status", OrderStatus.PENDING_CONFIRMATION.getOrderStatusValue());
                newInvoice.put("createdAt", FormatHelper.getCurrentDateTime());
                newInvoice.put("note", item.getNote());
                newInvoice.put("storeID", item.getStoreID());

//                binding.progressBar.setVisibility(View.VISIBLE);
                binding.progressBar.getIndeterminateDrawable()
                        .setColorFilter(Color.parseColor("#f04d7f"), PorterDuff.Mode.MULTIPLY);
                binding.btnBooking.setBackground(ContextCompat.getDrawable(this, R.color.darkgray));

                invoiceApi invoiceApi = new invoiceApi();

                invoiceApi.createInvoiceApi(newInvoice, new CreateDocumentCallback() {
                    @Override
                    public void onCreateSuccess(String documentID) {
                        createInvoiceDetail(invoiceApi, documentID, item.getListVariants());
                    }

                    @Override
                    public void onCreateFailure(String errorMessage) {
                        Toast.makeText(PaymentActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                        binding.progressBar.setVisibility(View.GONE);
                    }
                });
            }

        });

        binding.txtCustomerAddress.setOnClickListener(v -> {
            myLauncher.launch(new Intent(PaymentActivity.this, DeliveryAddressActivity.class));
        });
    }

    private void createInvoiceDetail(invoiceApi invoiceApi, String invoiceID, ArrayList<Variant> variantsList) {
        ArrayList<InvoiceDetail> invoiceDetails = new ArrayList<>();
        for (Variant variant : variantsList) {
            invoiceDetails.add(new InvoiceDetail(invoiceID, variant.getBaseID(), variant.getProductID(), variant.getProductName(), variant.getNumberInCart()));
        }
        invoiceDetails.forEach(item -> {
            Log.d(tag, "variantID: " + item.getVariantID() + "\nquantity: " + item.getQuantity() + "\nProductName: " + item.getProductName() + "\nProductID: " + item.getProductID());
            Log.d(tag, "--------------------");
        });


        invoiceApi.createDetailInvoiceApi(invoiceDetails, new StatusCallback() {
            @Override
            public void onSuccess(String successMessage) {
                binding.progressBar.setVisibility(View.GONE);
                binding.btnBooking.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.color.primary_color));
                Toast.makeText(PaymentActivity.this, successMessage, Toast.LENGTH_SHORT).show();
                // call API update tồn kho
                productApi m_productApi = new productApi();
                m_productApi.updateInventory(invoiceDetails, new StatusCallback() {
                    @Override
                    public void onSuccess(String successMessage) {
                        showToast(PaymentActivity.this, successMessage);
                    }

                    @Override
                    public void onFailure(String errorMessage) {
                        showToast(PaymentActivity.this, errorMessage);
                    }
                });



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


    private void calculatorPayment() {
        binding.txtTotalProductsFee.setText(FormatHelper.formatVND(getTotalProductsFee()));
        double delivery = 25000;
        double totalDelivery = delivery * payment.size();
        binding.txtTotalDeliveryFee.setText(FormatHelper.formatVND(totalDelivery));
        double total = getTotalProductsFee() + totalDelivery;
        binding.txtTotalPayment.setText(FormatHelper.formatVND(total));
        binding.txtTotalOrder.setText(FormatHelper.formatVND(total));
    }

    private double getTotalProductsFee() {
        double fee = 0;
        for (CartItem item : payment) {
            for (Variant variant : item.getListVariants()) {
                fee += (variant.getNewPrice() * variant.getNumberInCart());
            }
        }
        return fee;
    }


    private void setupUI() {
        getWindow().setStatusBarColor(Color.parseColor("#F04D7F"));
        Objects.requireNonNull(getSupportActionBar()).hide();

        sharedPreferences = getSharedPreferences(USER_INFO, MODE_PRIVATE);
        userID = sharedPreferences.getString(USER_ID, null);
        defaultAddressID = sharedPreferences.getString(DEFAULT_ADDRESS_ID, null);

        userApi userApi = new userApi();
        userApi.getUserInfoApi(userID, new UserCallback() {
            @Override
            public void getUserInfoSuccess(User user) {
                addressApi addressApi = new addressApi();
                addressApi.getAddressDetailApi(user.getDefaultAddressID(), new GetDocumentCallback() {
                    @Override
                    public void onGetDataSuccess(Map<String, Object> data) {
                        String detailedAddress = (String) data.get(DETAILED_ADDRESS);
                        String wardName = (String) data.get(WARD_NAME);
                        String districtName = (String) data.get(DISTRICT_NAME);
                        String provinceName = (String) data.get(PROVINCE_NAME);

                        String address = wardName + ", " + districtName + ", " + provinceName;
                        binding.txtDetailedCustomer.setText(detailedAddress);
                        binding.txtCustomerAddress.setText(address);
                    }

                    @Override
                    public void onGetDataFailure(String errorMessage) {
                        Toast.makeText(PaymentActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                    }
                });

                binding.txtCustomerInfo.setText(user.getFullname() + " | " + user.getPhoneNumber());
            }

            @Override
            public void getUserInfoFailure(String errorMessage) {
                Toast.makeText(PaymentActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }

    ActivityResultLauncher<Intent> myLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        setupUI();
                    }
                }
            });
}