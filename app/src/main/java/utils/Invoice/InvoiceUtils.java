package utils.Invoice;

import static android.content.Context.MODE_PRIVATE;
import static constants.keyName.INVOICE_ID;
import static constants.keyName.NOTE;
import static constants.keyName.STORE_ID;
import static constants.keyName.USER_ID;
import static constants.keyName.USER_INFO;
import static utils.FormatHelper.formatDateTime;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.stores.R;
import com.example.stores.databinding.FragmentWithOnlyRecyclerviewBinding;
import com.google.firebase.Timestamp;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;

import Adapters.Invoices.DeliveryAdapter;
import Adapters.Invoices.InvoiceAdapter;
import Adapters.Invoices.RequestInvoiceAdapter;
import api.invoiceApi;
import enums.OrderStatus;
import interfaces.GetCollectionCallback;
import interfaces.InAdapter.UpdateCountListener;
import models.Invoice;

public class InvoiceUtils {
    public static void transferInvoiceDetail(Invoice invoice, Context contextStart, Context contextTarget) {
        Intent intent = new Intent(contextStart, contextTarget.getClass());
        Bundle bundle = new Bundle();

        bundle.putString("invoiceID", invoice.getBaseID());
        bundle.putString("detailedAddress", invoice.getDetailedAddress());
        bundle.putString("deliveryAddress", invoice.getDeliveryAddress());
        bundle.putString("invoiceStatusLabel", invoice.getStatus().getOrderLabel());
        bundle.putString("createdAt", formatDateTime(invoice.getCreatedAt()));
        bundle.putString("confirmedAt", formatDateTime(invoice.getConfirmedAt()));
        bundle.putString("shippedAt", formatDateTime(invoice.getShippedAt()));
        bundle.putString("deliveredAt", formatDateTime(invoice.getDeliveredAt()));
        bundle.putString("cancelledAt", formatDateTime(invoice.getCancelledAt()));
        bundle.putString("cancelledAt", formatDateTime(invoice.getCancelledAt()));
        bundle.putString(NOTE, invoice.getNote());
        bundle.putString("cancelledReason", invoice.getCancelledReason());
        bundle.putDouble("invoiceTotal", invoice.getTotal());
        bundle.putString(STORE_ID, invoice.getStoreID());

        intent.putExtras(bundle);
        contextStart.startActivity(intent);
    }

    public static void initRequestInvoiceByStatus(Context context, FragmentWithOnlyRecyclerviewBinding binding, int orderStatusValue) {
        invoiceApi m_invoiceApi = new invoiceApi();
        SharedPreferences sharedPreferences = context.getSharedPreferences(USER_INFO, MODE_PRIVATE);
        String userID = sharedPreferences.getString(USER_ID, null);
        String storeID = sharedPreferences.getString(STORE_ID, null);

        if (userID == null || storeID == null) return;

        binding.progressBar.setVisibility(View.VISIBLE);
        binding.progressBar.getIndeterminateDrawable()
                .setColorFilter(ContextCompat.getColor(context, R.color.primary_color),
                        PorterDuff.Mode.MULTIPLY);

        m_invoiceApi.getInvoiceByStoreIDApi(storeID, orderStatusValue,
                new GetCollectionCallback<Invoice>() {
                    @Override
                    public void onGetListSuccess(ArrayList<Invoice> requestInvoiceList) {
                        binding.progressBar.setVisibility(View.GONE);

                        RequestInvoiceAdapter adapter = new RequestInvoiceAdapter(context,
                                requestInvoiceList, (UpdateCountListener) context);
                        binding.recyclerView.setAdapter(adapter);
                        binding.recyclerView.setLayoutManager(new LinearLayoutManager(context,
                                LinearLayoutManager.VERTICAL, false));
                    }

                    @Override
                    public void onGetListFailure(String errorMessage) {
                        Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show();
                        binding.progressBar.setVisibility(View.GONE);
                    }
                });
    }

    public static void initCustomerInvoiceByStatus(Context context, FragmentWithOnlyRecyclerviewBinding binding, int orderStatusValue) {
        invoiceApi m_invoiceApi = new invoiceApi();
        SharedPreferences sharedPreferences = context.getSharedPreferences(USER_INFO, MODE_PRIVATE);
        String userID = sharedPreferences.getString(USER_ID, null);

        if (userID == null) return;

        binding.progressBar.setVisibility(View.VISIBLE);
        binding.progressBar.getIndeterminateDrawable()
                .setColorFilter(Color.parseColor("#F04D7F"), PorterDuff.Mode.MULTIPLY);

        m_invoiceApi.getInvoicesByStatusApi(userID, orderStatusValue
                , new GetCollectionCallback<Invoice>() {
                    @Override
                    public void onGetListSuccess(ArrayList<Invoice> invoiceList) {
                        binding.progressBar.setVisibility(View.GONE);

                        InvoiceAdapter invoiceAdapter = new InvoiceAdapter(context, invoiceList);
                        binding.recyclerView.setAdapter(invoiceAdapter);
                        binding.recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));

                    }

                    @Override
                    public void onGetListFailure(String errorMessage) {
                        binding.progressBar.setVisibility(View.GONE);
                        Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show();
                    }
                });


    }

    public static void initDeliveryInvoiceByStatus(Context context, FragmentWithOnlyRecyclerviewBinding binding, int orderStatusValue) {
        invoiceApi m_invoiceApi = new invoiceApi();
        SharedPreferences sharedPreferences = context.getSharedPreferences(USER_INFO, MODE_PRIVATE);
        String userID = sharedPreferences.getString(USER_ID, null);

        if (userID == null) return;

        binding.progressBar.setVisibility(View.VISIBLE);
        binding.progressBar.getIndeterminateDrawable()
                .setColorFilter(Color.parseColor("#F04D7F"), PorterDuff.Mode.MULTIPLY);

        m_invoiceApi.getDeliveryInvoicesByStatusApi(orderStatusValue,
                new GetCollectionCallback<Invoice>() {
                    @Override
                    public void onGetListSuccess(ArrayList<Invoice> invoiceList) {
                        binding.progressBar.setVisibility(View.GONE);
                        DeliveryAdapter invoiceAdapter = new DeliveryAdapter(context, invoiceList, (UpdateCountListener) context);
                        binding.recyclerView.setAdapter(invoiceAdapter);
                        binding.recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
                    }

                    @Override
                    public void onGetListFailure(String errorMessage) {
                        binding.progressBar.setVisibility(View.GONE);
                        Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show();
                    }
                });


    }


    public static void sortInvoicesByStatus(ArrayList<Invoice> invoices, int orderStatusValue) {
        // Sắp xếp invoice theo Thời gian giảm dần
        List<Function<Invoice, Timestamp>> functions = new ArrayList<>();

        // Function nhận tham số đầu vào kiểu Invoice, trả về dữ liệu kiểu Timestamp, thông qua tham chiếu phương thức
        functions.add(Invoice::getCancelledAt);
        functions.add(Invoice::getCreatedAt);
        functions.add(Invoice::getConfirmedAt);
        functions.add(Invoice::getShippedAt);
        functions.add(Invoice::getDeliveredAt);

        invoices.sort((invoice1, invoice2) -> {
            Timestamp date1 = functions.get(orderStatusValue).apply(invoice1);
            Timestamp date2 = functions.get(orderStatusValue).apply(invoice2);
            return date2.compareTo(date1);
        });


    }
}
