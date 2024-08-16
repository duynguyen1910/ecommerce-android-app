package Adapters.Invoices;
import static constants.keyName.STORE_NAME;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.stores.databinding.ItemInvoiceBinding;

import java.util.ArrayList;
import java.util.Map;

import Activities.Invoices.InvoiceDetailActivity;
import api.invoiceApi;
import api.storeApi;
import enums.OrderStatus;
import interfaces.GetCollectionCallback;
import interfaces.GetDocumentCallback;
import models.Invoice;
import models.InvoiceDetail;
import utils.TimeUtils;
import utils.Invoice.DialogCancelInvoiceUtils;
import utils.FormatHelper;
import utils.Invoice.InvoiceUtils;

public class InvoiceAdapter extends RecyclerView.Adapter<InvoiceAdapter.ViewHolder> {
    private final Context context;
    private final ArrayList<Invoice> invoiceList;


    public InvoiceAdapter(Context context, ArrayList<Invoice> invoiceList) {
        this.context = context;
        this.invoiceList = invoiceList;
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
        final ArrayList<InvoiceDetail>[] invoiceDetails = new ArrayList[]{new ArrayList<>()};
        holder.binding.progressBar.setVisibility(View.VISIBLE);
        holder.binding.progressBar.getIndeterminateDrawable()
                .setColorFilter(Color.parseColor("#F04D7F"), PorterDuff.Mode.MULTIPLY);

        holder.binding.txtTotal.setText(FormatHelper.formatVND(invoice.getTotal()));
        holder.binding.btnCancelInvoice.setVisibility(
                invoice.getStatus() == OrderStatus.PENDING_CONFIRMATION ? View.VISIBLE : View.GONE);

        invoiceApi invoiceApi = new invoiceApi();
        invoiceApi.getInvoiceDetailApi(invoice.getBaseID(), new GetCollectionCallback<InvoiceDetail>() {
            @Override
            public void onGetListSuccess(ArrayList<InvoiceDetail> productList) {
                invoiceDetails[0] = new ArrayList<>(productList);
                holder.binding.progressBar.setVisibility(View.GONE);

                getStoreNameByID(invoice.getStoreID(), holder.binding.txtStoreName);
                holder.binding.txtQuantityProducts.setText(productList.size() + " sản phẩm");

                InvoiceDetailAdapter adapter = new InvoiceDetailAdapter(context,
                        productList, InvoiceDetail.ITEM_TO_DISPLAY);
                holder.binding.recyclerViewProducts.setLayoutManager(new LinearLayoutManager(context));
                holder.binding.recyclerViewProducts.setAdapter(adapter);
                holder.binding.txtTime.setText(TimeUtils.setDateTimeByInvoice(invoice));

            }

            @Override
            public void onGetListFailure(String errorMessage) {
                holder.binding.progressBar.setVisibility(View.GONE);
                Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show();
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    InvoiceUtils.transferInvoiceDetail(invoice, context, InvoiceDetailActivity.class.newInstance());
                } catch (IllegalAccessException | InstantiationException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        holder.binding.btnCancelInvoice.setOnClickListener(v -> {
            DialogCancelInvoiceUtils.popUpCancelInvoiceByCustomerDialog(this, context, invoice, holder.getBindingAdapterPosition(), invoiceDetails[0]);
        });

    }

    private void getStoreNameByID(String storeID, TextView txtStoreName) {

        storeApi storeApi = new storeApi();
        storeApi.getStoreDetailApi(storeID, new GetDocumentCallback() {
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
    public void removeItemAdapter(int position) {
        invoiceList.remove(position);
        notifyItemRemoved(position);
    }
    @Override
    public int getItemCount() {
        return invoiceList.size();
    }
}