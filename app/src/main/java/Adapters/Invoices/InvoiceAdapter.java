package Adapters.Invoices;

import static constants.keyName.CANCELED_AT;
import static constants.keyName.CANCELED_BY;
import static constants.keyName.CANCELED_REASON;
import static constants.keyName.STATUS;
import static constants.keyName.STORE_NAME;
import static constants.toastMessage.CANCEL_ORDER_SUCCESSFULLY;
import static utils.Cart.CartUtils.showToast;
import static utils.FormatHelper.formatDateTime;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.stores.R;
import com.example.stores.databinding.DialogCancelInvoiceBinding;
import com.example.stores.databinding.ItemInvoiceBinding;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import Activities.Invoices.InvoiceDetailActivity;
import api.invoiceApi;
import api.storeApi;
import enums.OrderStatus;
import interfaces.GetCollectionCallback;
import interfaces.GetDocumentCallback;
import interfaces.UpdateDocumentCallback;
import models.Invoice;
import models.InvoiceDetail;
import models.UserAddress;
import utils.FormatHelper;

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

        holder.binding.progressBar.setVisibility(View.VISIBLE);
        holder.binding.progressBar.getIndeterminateDrawable()
                .setColorFilter(Color.parseColor("#F04D7F"), PorterDuff.Mode.MULTIPLY);

        holder.binding.txtInvoiceStatus.setText(invoice.getStatus().getOrderLabel());

        holder.binding.txtTotal.setText(FormatHelper.formatVND(invoice.getTotal()));

        holder.binding.btnCancelInvoice.setVisibility(
                invoice.getStatus() == OrderStatus.PENDING_CONFIRMATION ? View.VISIBLE : View.GONE);

        invoiceApi invoiceApi = new invoiceApi();
        invoiceApi.getInvoiceDetailApi(invoice.getBaseID(), new GetCollectionCallback<InvoiceDetail>() {
            @Override
            public void onGetListSuccess(ArrayList<InvoiceDetail> productList) {
                holder.binding.progressBar.setVisibility(View.GONE);

                getStoreNameByID(productList, holder.binding.txtStoreName);
                holder.binding.txtQuantityProducts.setText(productList.size() + " sản phẩm");

                InvoiceDetailAdapter adapter = new InvoiceDetailAdapter(context,
                        productList, InvoiceDetail.ITEM_TO_DISPLAY);
                holder.binding.recyclerViewProducts.setLayoutManager(new LinearLayoutManager(context));
                holder.binding.recyclerViewProducts.setAdapter(adapter);

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

        holder.binding.btnCancelInvoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popUpCancelInvoiceDialog(invoice);
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

    private void popUpCancelInvoiceDialog(Invoice invoice) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        DialogCancelInvoiceBinding dialogBinding = DialogCancelInvoiceBinding.inflate((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE));
        builder.setView(dialogBinding.getRoot());
        AlertDialog dialog = builder.create();

        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawableResource(R.drawable.custom_edit_text_border);
        dialog.show();

        Window window = dialog.getWindow();
        if (window != null) {
            window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            window.setGravity(Gravity.BOTTOM);

            Animation slideUp = AnimationUtils.loadAnimation(context, R.anim.slide_up);
            dialogBinding.getRoot().startAnimation(slideUp);
        }



        final String[] cancelReason = {""};

        dialogBinding.layoutSelectReason.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.rdoReason1) {
                    cancelReason[0] += dialogBinding.rdoReason1.getText().toString();
                } else if (checkedId == R.id.rdoReason2) {
                    cancelReason[0] += dialogBinding.rdoReason2.getText().toString();
                }
            }
        });

        dialogBinding.btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cancelReason[0].isEmpty()) {
                    showToast(context, "Bạn cần chọn lý do hủy đơn hàng.");
                    return;
                }
                dialogBinding.progressBar.setVisibility(View.VISIBLE);
                dialogBinding.progressBar.getIndeterminateDrawable()
                        .setColorFilter(Color.parseColor("#f04d7f"), PorterDuff.Mode.MULTIPLY);
                Map<String, Object> invoiceUpdate = new HashMap<>();
                invoiceUpdate.put(STATUS, OrderStatus.CANCELLED.getOrderStatusValue());
                String otherReason = "\nOther reason: " + dialogBinding.edtOtherReason.getText().toString().trim();
                cancelReason[0] += otherReason;
                String cancelledReason = "Cancel reason " + cancelReason[0];
                invoiceUpdate.put(CANCELED_BY, invoice.getCustomerID());
                invoiceUpdate.put(CANCELED_REASON, cancelledReason);
                invoiceUpdate.put(CANCELED_AT, FormatHelper.getCurrentDateTime());

                invoiceApi invoiceApi = new invoiceApi();
                invoiceApi.updateStatusInvoiceApi(invoice.getBaseID(), invoiceUpdate, new UpdateDocumentCallback() {
                    @Override
                    public void onUpdateSuccess(String successMessage) {
                        dialogBinding.progressBar.setVisibility(View.GONE);
                        showToast(context, CANCEL_ORDER_SUCCESSFULLY);
                        dialog.dismiss();
                    }

                    @Override
                    public void onUpdateFailure(String errorMessage) {
                        dialogBinding.progressBar.setVisibility(View.GONE);
                        showToast(context, errorMessage);
                        dialog.dismiss();
                    }
                });

            }
        });


    }

    @Override
    public int getItemCount() {
        return invoiceList.size();
    }
}