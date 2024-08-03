package Adapters.Invoices;
import static android.content.Context.MODE_PRIVATE;
import static constants.keyName.CANCELED_AT;
import static constants.keyName.CANCELED_BY;
import static constants.keyName.CANCELED_REASON;
import static constants.keyName.DELIVERED_AT;
import static constants.keyName.SHIPPED_AT;
import static constants.keyName.STATUS;
import static constants.keyName.STORE_ID;
import static constants.keyName.USER_ID;
import static constants.keyName.USER_INFO;
import static constants.toastMessage.CANCEL_ORDER_SUCCESSFULLY;
import static utils.Cart.CartUtils.showToast;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.stores.R;
import com.example.stores.databinding.DialogCancelInvoiceByDeliveryBinding;
import com.example.stores.databinding.ItemDeliveryBinding;
import com.google.firebase.Timestamp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import api.invoiceApi;
import enums.OrderStatus;
import interfaces.GetCollectionCallback;
import interfaces.UpdateDocumentCallback;
import interfaces.UserCallback;
import models.Invoice;
import models.InvoiceDetail;
import models.User;
import utils.DialogCancelInvoiceUtils;
import utils.FormatHelper;

public class DeliveryAdapter extends RecyclerView.Adapter<DeliveryAdapter.ViewHolder> {
    private final Context context;
    private final ArrayList<Invoice> list;


    public DeliveryAdapter(Context context, ArrayList<Invoice> list) {
        this.context = context;
        this.list = list;
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

        if (invoice.getStatus() == OrderStatus.CANCELLED){
            holder.binding.layoutCancelReason.setVisibility(View.VISIBLE);
            holder.binding.txtCanceledReason.setText(invoice.getCancelledReason());
        }else {
            holder.binding.layoutCancelReason.setVisibility(View.GONE);
        }
        setupControlButtons(invoice, holder.binding.btnCancel, holder.binding.btnDelivery, holder.binding.btnComplete);

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
                holder.binding.txtTime.setText(
                        FormatHelper.formatDateTime(setDateTimeByInvoice(invoice)));
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
                }

                @Override
                public void onUpdateFailure(String errorMessage) {
                    showToast(context, errorMessage);
                }
            });
        });


        holder.binding.btnCancel.setOnClickListener(v -> {
            DialogCancelInvoiceUtils.popUpCancelInvoiceByDeliveryDialog(context, invoice);
        });
        holder.binding.btnViewCancelledInvoiceDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogCancelInvoiceUtils.popUpCancelledInvoiceDetailDialog(context, invoice);
            }
        });


    }



    private void removeItemAdapter(int position) {
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

    private Timestamp setDateTimeByInvoice(Invoice invoice) {
        switch (invoice.getStatus()) {
            case PENDING_SHIPMENT:
                return invoice.getCreatedAt();
            case IN_TRANSIT:
                return invoice.getShippedAt();
            case DELIVERED:
                return invoice.getDeliveredAt();
            default:
                return invoice.getCancelledAt();
        }
    }


    @Override
    public int getItemCount() {
        return list.size();
    }
}
