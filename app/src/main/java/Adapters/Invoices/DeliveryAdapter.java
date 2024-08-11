package Adapters.Invoices;
import static constants.keyName.DELIVERED_AT;
import static constants.keyName.SHIPPED_AT;
import static constants.keyName.STATUS;
import static utils.Cart.CartUtils.showToast;
import static utils.Chart.TimeUtils.setDateTimeByInvoice;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.PorterDuff;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.stores.R;
import com.example.stores.databinding.ItemDeliveryBinding;
import com.google.firebase.Timestamp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import Activities.Invoices.InvoiceDetailActivity;
import api.invoiceApi;
import enums.OrderStatus;
import interfaces.GetCollectionCallback;
import interfaces.InAdapter.UpdateCountListener;
import interfaces.UpdateDocumentCallback;
import interfaces.UserCallback;
import models.Invoice;
import models.InvoiceDetail;
import models.User;
import utils.Chart.TimeUtils;
import utils.Invoice.DialogCancelInvoiceUtils;
import utils.FormatHelper;
import utils.Invoice.InvoiceUtils;

public class DeliveryAdapter extends RecyclerView.Adapter<DeliveryAdapter.ViewHolder> {
    private final Context context;
    private final ArrayList<Invoice> list;
    private final UpdateCountListener listener;


    public DeliveryAdapter(Context context, ArrayList<Invoice> list, UpdateCountListener listener) {
        this.context = context;
        this.list = list;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemDeliveryBinding binding = ItemDeliveryBinding.inflate(LayoutInflater.from(context), parent, false);
        return new ViewHolder(binding);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        ItemDeliveryBinding binding;

        public ViewHolder(ItemDeliveryBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Invoice invoice = list.get(holder.getBindingAdapterPosition());
        final ArrayList<InvoiceDetail>[] invoiceDetails = new ArrayList[]{new ArrayList<>()};
        User user = new User();

        user.getUserInfo(invoice.getCustomerID(), new UserCallback() {
            @Override
            public void getUserInfoSuccess(User user) {
                holder.binding.txtCustomerName.setText(user.getFullname());
                holder.binding.txtCustomerPhone.setText(user.getPhoneNumber());
                holder.binding.txtCustomerAddress.setText(invoice.getDeliveryAddress());
            }

            @Override
            public void getUserInfoFailure(String errorMessage) {

            }
        });


        setupControlButtons(invoice, holder.binding.btnCancel, holder.binding.btnDelivery, holder.binding.btnComplete);

        holder.binding.progressBar.setVisibility(View.VISIBLE);
        holder.binding.progressBar.getIndeterminateDrawable()
                .setColorFilter(ContextCompat.getColor(context, R.color.primary_color),
                        PorterDuff.Mode.MULTIPLY);

        invoiceApi invoiceApi = new invoiceApi();
        invoiceApi.getInvoiceDetailApi(invoice.getBaseID(), new GetCollectionCallback<InvoiceDetail>() {
            @Override
            public void onGetListSuccess(ArrayList<InvoiceDetail> productList) {
                invoiceDetails[0] = new ArrayList<>(productList);
                holder.binding.progressBar.setVisibility(View.GONE);

                InvoiceDetailAdapter adapter = new
                        InvoiceDetailAdapter(context, productList, InvoiceDetail.ITEM_TO_DISPLAY);
                holder.binding.recyclerViewProducts.setLayoutManager(new LinearLayoutManager(context));
                holder.binding.recyclerViewProducts.setAdapter(adapter);

                holder.binding.txtQuantityProducts.setText(productList.size() + " sản phẩm");
                holder.binding.txtTime.setText(TimeUtils.setDateTimeByInvoice(invoice));
                holder.binding.txtTotal.setText(FormatHelper.formatVND(invoice.getTotal()));

            }

            @Override
            public void onGetListFailure(String errorMessage) {
                holder.binding.progressBar.setVisibility(View.GONE);
                Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show();
            }
        });

        holder.binding.btnDelivery.setOnClickListener(v -> {
            Map<String, Object> newMap = new HashMap<>();
            newMap.put(STATUS, OrderStatus.IN_TRANSIT.getOrderStatusValue());
            newMap.put(SHIPPED_AT, FormatHelper.getCurrentDateTime());

            invoiceApi.updateStatusInvoiceApi(invoice.getBaseID(), newMap, new UpdateDocumentCallback() {
                @Override
                public void onUpdateSuccess(String successMessage) {
                    showToast(context, "Xác nhận đơn, tiến hành giao hàng");
                    removeItemAdapter(holder.getBindingAdapterPosition());
                    listener.updateCount();
                }

                @Override
                public void onUpdateFailure(String errorMessage) {
                    showToast(context, errorMessage);
                }
            });
        });
        holder.binding.btnComplete.setOnClickListener(v -> {
            Map<String, Object> newMap = new HashMap<>();
            newMap.put(STATUS, OrderStatus.DELIVERED.getOrderStatusValue());
            newMap.put(DELIVERED_AT, FormatHelper.getCurrentDateTime());

            invoiceApi.updateStatusInvoiceApi(invoice.getBaseID(), newMap, new UpdateDocumentCallback() {
                @Override
                public void onUpdateSuccess(String successMessage) {
                    showToast(context, "Đã xác nhận hoàn thành đơn hàng");
                    removeItemAdapter(holder.getBindingAdapterPosition());
                    listener.updateCount();
                }

                @Override
                public void onUpdateFailure(String errorMessage) {
                    showToast(context, errorMessage);
                }
            });
        });


        holder.binding.btnCancel.setOnClickListener(v -> {
            DialogCancelInvoiceUtils.popUpCancelInvoiceByDeliveryDialog(this, context, invoice, holder.getBindingAdapterPosition(), invoiceDetails[0]);
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

    }

    public void updateInvoicesCount(){
        listener.updateCount();
    }



    public void removeItemAdapter(int position) {
        list.remove(position);
        notifyItemRemoved(position);
    }

    private void setupControlButtons(Invoice invoice, Button btnCancel, Button btnDelivery, Button btnComplete){
        switch (invoice.getStatus()) {
            case PENDING_SHIPMENT:{
                btnCancel.setVisibility(View.VISIBLE);
                btnDelivery.setVisibility(View.VISIBLE);
                btnComplete.setVisibility(View.GONE);
                break;
            }

            case IN_TRANSIT:{
                btnCancel.setVisibility(View.GONE);
                btnDelivery.setVisibility(View.GONE);
                btnComplete.setVisibility(View.VISIBLE);
                break;
            }
            default:{
                btnCancel.setVisibility(View.GONE);
                btnDelivery.setVisibility(View.GONE);
                btnComplete.setVisibility(View.GONE);
                break;
            }
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
