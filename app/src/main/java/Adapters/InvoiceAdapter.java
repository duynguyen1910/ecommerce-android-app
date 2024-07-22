package Adapters;

import static constants.keyName.STORE_NAME;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.stores.databinding.ItemInvoiceBinding;
import com.google.firebase.Timestamp;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

import Activities.InvoiceDetailActivity;

import api.invoiceApi;
import api.storeApi;
import interfaces.GetCollectionCallback;
import interfaces.GetDocumentCallback;
import models.Invoice;
import models.InvoiceDetail;

public class InvoiceAdapter extends RecyclerView.Adapter<InvoiceAdapter.ViewHolder> {
    private final Context context;
    private final ArrayList<Invoice> invoiceList;
//    private final ArrayList<String> invoiceIDs;


    public InvoiceAdapter(Context context, ArrayList<Invoice> invoiceList) {
        this.context = context;
        this.invoiceList = invoiceList;
//        this.invoiceIDs = new ArrayList<>(map.keySet());
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemInvoiceBinding binding = ItemInvoiceBinding.inflate(LayoutInflater.from(context), parent, false);
        return new ViewHolder(binding);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        ItemInvoiceBinding binding;

        public ViewHolder(ItemInvoiceBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Invoice invoice = invoiceList.get(holder.getBindingAdapterPosition());


        holder.binding.progressBar.setVisibility(View.VISIBLE);
        holder.binding.progressBar.getIndeterminateDrawable()
                .setColorFilter(Color.parseColor("#F04D7F"), PorterDuff.Mode.MULTIPLY);

        invoiceApi invoiceApi = new invoiceApi();
        invoiceApi.getInvoiceDetail(invoice.getBaseID(), new GetCollectionCallback<InvoiceDetail>() {
            @Override
            public void onGetListSuccess(ArrayList<InvoiceDetail> productList) {
                holder.binding.progressBar.setVisibility(View.GONE);

                getStoreNameByID(productList, holder.binding.txtStoreName);

                ProductsListAdapterForInvoiceItem adapter = new ProductsListAdapterForInvoiceItem(context,
                        productList, true);
                holder.binding.recyclerViewProducts.setLayoutManager(new LinearLayoutManager(context));
                holder.binding.recyclerViewProducts.setAdapter(adapter);

            }

            @Override
            public void onGetListFailure(String errorMessage) {
                holder.binding.progressBar.setVisibility(View.GONE);
                Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show();
            }
        });

        holder.binding.txtQuantityProducts.setText(invoiceList.size() + " sản phẩm");

        Timestamp timestamp = invoice.getCreatedAt();
        Date date = timestamp.toDate();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yy, HH:mm", Locale.getDefault());
        String formattedDate = sdf.format(date);

        holder.binding.txtCreatedDate.setText(formattedDate);


        NumberFormat formatter = NumberFormat.getInstance(new Locale("vi", "VN"));
        holder.binding.txtTotal.setText("đ" + formatter.format(invoice.getTotal()));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, InvoiceDetailActivity.class);
                Bundle bundle = new Bundle();

                bundle.putString("invoiceID", invoice.getBaseID());
                bundle.putString("deliveryAddress", invoice.getDeliveryAddress());
                bundle.putString("createdAt", formattedDate);
                bundle.putDouble("invoiceTotal", invoice.getTotal());


                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        });

    }


    private void getStoreNameByID(ArrayList<InvoiceDetail> productList, TextView txtStoreName) {
        storeApi storeApi = new storeApi();
        storeApi.getStoreDetailApi(productList.get(0).getStoreID(), new GetDocumentCallback() {
            @Override
            public void onGetDataSuccess(Map<String, Object> data) {
                txtStoreName.setText((CharSequence) data.get(STORE_NAME));
            }

            @Override
            public void onGetDataFailure(String errorMessage) {
                Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return invoiceList.size();
    }
}
