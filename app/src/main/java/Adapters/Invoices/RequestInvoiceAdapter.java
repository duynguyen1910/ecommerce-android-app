package Adapters.Invoices;

import static constants.keyName.CANCELED_AT;
import static constants.keyName.CONFIRMED_AT;
import static constants.keyName.STATUS;
import static utils.CartUtils.showToast;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.PorterDuff;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.stores.R;
import com.example.stores.databinding.ItemRequestInvoiceBinding;
import com.google.firebase.Timestamp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import api.invoiceApi;
import api.productApi;
import enums.OrderStatus;
import interfaces.GetCollectionCallback;
import interfaces.StatusCallback;
import interfaces.UpdateDocumentCallback;
import interfaces.UserCallback;
import models.Invoice;
import models.InvoiceDetail;
import models.User;
import utils.FormatHelper;

public class RequestInvoiceAdapter extends RecyclerView.Adapter<RequestInvoiceAdapter.ViewHolder> {
    private final Context context;
    private final ArrayList<Invoice> list;


    public RequestInvoiceAdapter(Context context, ArrayList<Invoice> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemRequestInvoiceBinding binding = ItemRequestInvoiceBinding.inflate(LayoutInflater.from(context), parent, false);
        return new ViewHolder(binding);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ItemRequestInvoiceBinding binding;

        public ViewHolder(ItemRequestInvoiceBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Invoice invoice = list.get(holder.getBindingAdapterPosition());
        User user = new User();

        user.getUserInfo(invoice.getCustomerID(), new UserCallback() {
            @Override
            public void getUserInfoSuccess(User user) {
                holder.binding.txtCustomerName.setText(user.getFullname());
                holder.binding.txtCustomerPhone.setText(user.getPhoneNumber());
                holder.binding.txtCustomerAddress.setText(user.getUserAddress());
            }

            @Override
            public void getUserInfoFailure(String errorMessage) {

            }
        });

//        holder.binding.txtInvoiceStatus.setText(invoice.getStatus().getOrderLabel());

        holder.binding.progressBar.setVisibility(View.VISIBLE);
        holder.binding.progressBar.getIndeterminateDrawable()
                .setColorFilter(ContextCompat.getColor(context, R.color.primary_color),
                        PorterDuff.Mode.MULTIPLY);

        invoiceApi invoiceApi = new invoiceApi();
        invoiceApi.getInvoiceDetailApi(invoice.getBaseID(), new GetCollectionCallback<InvoiceDetail>() {
            @Override
            public void onGetListSuccess(ArrayList<InvoiceDetail> productList) {
                holder.binding.progressBar.setVisibility(View.GONE);

                InvoiceDetailAdapter adapter = new
                        InvoiceDetailAdapter(context, productList);
                holder.binding.recyclerViewProducts.setLayoutManager(new LinearLayoutManager(context));
                holder.binding.recyclerViewProducts.setAdapter(adapter);

                holder.binding.txtQuantityProducts.setText(productList.size() + " sản phẩm");
                holder.binding.txtInvoiceStatus.setText(
                        FormatHelper.formatDateTime(setDateTimeByInvoice(invoice)));
                holder.binding.txtTotal.setText(FormatHelper.formatVND(invoice.getTotal()));

            }

            @Override
            public void onGetListFailure(String errorMessage) {
                holder.binding.progressBar.setVisibility(View.GONE);
                Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show();
            }
        });

        holder.binding.btnConfirmInvoice.setOnClickListener(v -> {

            //1.  Cập nhật trạng thái đơn hàng
            Map<String, Object> newMap = new HashMap<>();
            newMap.put(STATUS, OrderStatus.PENDING_SHIPMENT.getOrderStatusValue());
            newMap.put(CONFIRMED_AT, FormatHelper.getCurrentDateTime());

            invoiceApi.updateStatusInvoiceApi(invoice.getBaseID(), newMap, new UpdateDocumentCallback() {
                @Override
                public void onUpdateSuccess(String successMessage) {
                    Toast.makeText(context, successMessage, Toast.LENGTH_SHORT).show();
                    removeItemAdapter(holder.getBindingAdapterPosition());
                }

                @Override
                public void onUpdateFailure(String errorMessage) {
                    Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show();
                }
            });

            //2. Trừ tồn kho của tất cả sản phẩm trong đơn hàng

            productApi productApi = new productApi();
            productApi.updateProductWhenConfirmInvoice(invoice.getBaseID(), new StatusCallback() {
                @Override
                public void onSuccess(String successMessage) {
                    showToast(context, successMessage);
                }

                @Override
                public void onFailure(String errorMessage) {
                    showToast(context, errorMessage);
                }
            });
        });


        holder.binding.btnCancel.setOnClickListener(v -> {
            Map<String, Object> newMap = new HashMap<>();
            newMap.put(STATUS, OrderStatus.CANCELLED.getOrderStatusValue());
            newMap.put(CANCELED_AT, FormatHelper.getCurrentDateTime());

            invoiceApi.updateStatusInvoiceApi(invoice.getBaseID(), newMap, new UpdateDocumentCallback() {
                @Override
                public void onUpdateSuccess(String successMessage) {

                    Toast.makeText(context, successMessage, Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onUpdateFailure(String errorMessage) {
                    Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show();
                }
            });
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    private void removeItemAdapter(int position) {
        list.remove(position);
        notifyItemRemoved(position);
    }

    private Timestamp setDateTimeByInvoice(Invoice invoice) {
        switch (invoice.getStatus()) {
            case PENDING_CONFIRMATION:
                return invoice.getCreatedAt();
            case PENDING_SHIPMENT:
                return invoice.getShippedAt();
            default:
                return invoice.getCancelledAt();
        }
    }

}