package Adapters.Invoices;

import static android.content.Context.MODE_PRIVATE;
import static constants.keyName.CANCELED_AT;
import static constants.keyName.CANCELED_BY;
import static constants.keyName.CANCELED_REASON;
import static constants.keyName.CONFIRMED_AT;
import static constants.keyName.STATUS;
import static constants.keyName.STORE_ID;
import static constants.keyName.USER_ID;
import static constants.keyName.USER_INFO;
import static constants.toastMessage.CANCEL_ORDER_SUCCESSFULLY;
import static utils.Cart.CartUtils.showToast;
import static utils.FormatHelper.formatDateTime;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Log;
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
import com.example.stores.databinding.DialogCancelInvoiceByStoreBinding;
import com.example.stores.databinding.ItemRequestInvoiceBinding;
import com.google.firebase.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import Activities.Invoices.InvoiceDetailActivity;
import api.invoiceApi;
import api.productApi;
import enums.OrderStatus;
import interfaces.GetCollectionCallback;
import interfaces.InAdapter.UpdateCountListener;
import interfaces.StatusCallback;
import interfaces.UpdateDocumentCallback;
import interfaces.UserCallback;
import models.Invoice;
import models.InvoiceDetail;
import models.User;
import utils.DialogCancelInvoiceUtils;
import utils.FormatHelper;

public class RequestInvoiceAdapter extends RecyclerView.Adapter<RequestInvoiceAdapter.ViewHolder> {
    private final Context context;
    private final ArrayList<Invoice> list;
    private final UpdateCountListener listener;


    public RequestInvoiceAdapter(Context context, ArrayList<Invoice> list, UpdateCountListener listener) {
        this.context = context;
        this.list = list;
        this.listener = listener;
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
        final ArrayList<InvoiceDetail>[] invoiceDetails = new ArrayList[]{new ArrayList<>()};
        User user = new User();

        user.getUserInfo(invoice.getCustomerID(), new UserCallback() {
            @Override
            public void getUserInfoSuccess(User user) {
                holder.binding.txtCustomerName.setText(user.getFullname());
                holder.binding.txtCustomerPhone.setText(user.getPhoneNumber());
                holder.binding.txtDetailedCustomer.setText(invoice.getDetailedAddress());
                holder.binding.txtCustomerAddress.setText(invoice.getDeliveryAddress());
            }

            @Override
            public void getUserInfoFailure(String errorMessage) {

            }
        });

        setupControlButtons(invoice, holder.binding.btnCancel, holder.binding.btnConfirm);

        holder.binding.progressBar.setVisibility(View.VISIBLE);
        holder.binding.progressBar.getIndeterminateDrawable()
                .setColorFilter(ContextCompat.getColor(context, R.color.primary_color),
                        PorterDuff.Mode.MULTIPLY);

        invoiceApi invoiceApi = new invoiceApi();
        invoiceApi.getInvoiceDetailApi(invoice.getBaseID(), new GetCollectionCallback<InvoiceDetail>() {
            @Override
            public void onGetListSuccess(ArrayList<InvoiceDetail> productList) {
                invoiceDetails[0] = new ArrayList<>(productList);
                invoiceDetails[0].forEach(item -> {
                    Log.d("invoiceDetails", "productID: " + item.getProductID() + "\nsold: " + item.getQuantity());
                });
                holder.binding.progressBar.setVisibility(View.GONE);

                InvoiceDetailAdapter adapter = new
                        InvoiceDetailAdapter(context, productList, InvoiceDetail.ITEM_TO_DISPLAY);
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

        holder.binding.btnConfirm.setOnClickListener(v -> {

            //1.  Cập nhật trạng thái đơn hàng
            Map<String, Object> newMap = new HashMap<>();
            newMap.put(STATUS, OrderStatus.PENDING_SHIPMENT.getOrderStatusValue());
            newMap.put(CONFIRMED_AT, FormatHelper.getCurrentDateTime());

            invoiceApi.updateStatusInvoiceApi(invoice.getBaseID(), newMap, new UpdateDocumentCallback() {
                @Override
                public void onUpdateSuccess(String successMessage) {
                    Toast.makeText(context, successMessage, Toast.LENGTH_SHORT).show();
                    removeItemAdapter(holder.getBindingAdapterPosition());
                    listener.updateCount();
                }

                @Override
                public void onUpdateFailure(String errorMessage) {
                    Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show();
                }
            });

            //2. Trừ tồn kho của tất cả sản phẩm trong đơn hàng

            productApi productApi = new productApi();
            productApi.updateSoldQuantity(invoiceDetails[0], new StatusCallback() {
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
          DialogCancelInvoiceUtils.popUpCancelInvoiceByStoreDialog(this, context, invoice, holder.getBindingAdapterPosition());
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, InvoiceDetailActivity.class);
                Bundle bundle = new Bundle();

                bundle.putString("invoiceID", invoice.getBaseID());
                bundle.putString("detailedAddress", invoice.getDetailedAddress());
                bundle.putString("deliveryAddress", invoice.getDeliveryAddress());
                bundle.putString("invoiceStatusLabel", invoice.getStatus().getOrderLabel());

                bundle.putString("createdAt", formatDateTime(invoice.getCreatedAt()));
                bundle.putString("confirmedAt", formatDateTime(invoice.getConfirmedAt()));
                bundle.putString("shippedAt", formatDateTime(invoice.getShippedAt()));
                bundle.putString("deliveredAt", formatDateTime(invoice.getDeliveredAt()));

                bundle.putDouble("invoiceTotal", invoice.getTotal());

                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        });


    }
    public void updateInvoicesCount(){
        listener.updateCount();
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public void removeItemAdapter(int position) {
        list.remove(position);
        notifyItemRemoved(position);
    }
    private void setupControlButtons(Invoice invoice, Button btnCancel, Button btnConfirm){
        switch (invoice.getStatus()) {
            case PENDING_CONFIRMATION:{
                btnCancel.setVisibility(View.VISIBLE);
                btnConfirm.setVisibility(View.VISIBLE);
                break;
            }

            default:{
                btnCancel.setVisibility(View.GONE);
                btnConfirm.setVisibility(View.GONE);
                break;
            }

        }
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