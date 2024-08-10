package utils.Invoice;
import static android.content.Context.MODE_PRIVATE;
import static constants.keyName.CANCELED_AT;
import static constants.keyName.CANCELED_BY;
import static constants.keyName.CANCELED_REASON;
import static constants.keyName.STATUS;
import static constants.keyName.USER_ID;
import static constants.keyName.USER_INFO;
import static constants.toastMessage.CANCEL_ORDER_SUCCESSFULLY;
import static utils.Cart.CartUtils.showToast;
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
import android.widget.RadioGroup;
import androidx.appcompat.app.AlertDialog;
import com.example.stores.R;
import com.example.stores.databinding.DialogCancelInvoiceByCustomerBinding;
import com.example.stores.databinding.DialogCancelInvoiceByDeliveryBinding;
import com.example.stores.databinding.DialogCancelInvoiceByStoreBinding;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import Adapters.Invoices.DeliveryAdapter;
import Adapters.Invoices.InvoiceAdapter;
import Adapters.Invoices.RequestInvoiceAdapter;
import api.invoiceApi;
import enums.OrderStatus;
import interfaces.UpdateDocumentCallback;
import models.Invoice;
import utils.FormatHelper;

public class DialogCancelInvoiceUtils {
    public static void popUpCancelInvoiceByCustomerDialog(InvoiceAdapter adapter, Context context, Invoice invoice, int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        DialogCancelInvoiceByCustomerBinding dialogBinding = DialogCancelInvoiceByCustomerBinding.inflate((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE));
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
                    cancelReason[0] = dialogBinding.rdoReason1.getText().toString();
                } else if (checkedId == R.id.rdoReason2) {
                    cancelReason[0] = dialogBinding.rdoReason2.getText().toString();
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
                String cancelledReason = "";
                String otherReason = dialogBinding.edtOtherReason.getText().toString().trim();
                if (otherReason.isEmpty()) {
                    cancelledReason = "Đã hủy bởi người mua. Lý do hủy: " + cancelReason[0];
                } else {
                    cancelledReason = "Đã hủy bởi người mua. Lý do hủy: " + cancelReason[0] + "Lý do khác: " + otherReason;
                }
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
                        adapter.removeItemAdapter(position);
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

    public static void popUpCancelInvoiceByDeliveryDialog(DeliveryAdapter adapter, Context context, Invoice invoice, int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        DialogCancelInvoiceByDeliveryBinding dialogBinding = DialogCancelInvoiceByDeliveryBinding.inflate((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE));
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
                    cancelReason[0] = dialogBinding.rdoReason1.getText().toString();
                } else if (checkedId == R.id.rdoReason2) {
                    cancelReason[0] = dialogBinding.rdoReason2.getText().toString();
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
                String cancelledReason = "";
                String otherReason = dialogBinding.edtOtherReason.getText().toString().trim();
                if (otherReason.isEmpty()) {
                    cancelledReason = "Đã hủy bởi đơn vị vận chuyển. Lý do hủy: " + cancelReason[0];
                } else {
                    cancelledReason = "Đã hủy bởi đơn vị vận chuyển. Lý do hủy: " + cancelReason[0] + "Lý do khác: " + otherReason;
                }

                SharedPreferences sharedPreferences = context.getSharedPreferences(USER_INFO, MODE_PRIVATE);
                String userID = sharedPreferences.getString(USER_ID, null);
                invoiceUpdate.put(CANCELED_BY, userID);
                invoiceUpdate.put(CANCELED_REASON, cancelledReason);
                invoiceUpdate.put(CANCELED_AT, FormatHelper.getCurrentDateTime());

                invoiceApi invoiceApi = new invoiceApi();
                invoiceApi.updateStatusInvoiceApi(invoice.getBaseID(), invoiceUpdate, new UpdateDocumentCallback() {
                    @Override
                    public void onUpdateSuccess(String successMessage) {
                        dialogBinding.progressBar.setVisibility(View.GONE);
                        showToast(context, CANCEL_ORDER_SUCCESSFULLY);
                        dialog.dismiss();
                        adapter.removeItemAdapter(position);
                        adapter.updateInvoicesCount();
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

    public static void popUpCancelInvoiceByStoreDialog(RequestInvoiceAdapter adapter, Context context, Invoice invoice, int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        DialogCancelInvoiceByStoreBinding dialogBinding = DialogCancelInvoiceByStoreBinding.inflate((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE));
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
                    cancelReason[0] = dialogBinding.rdoReason1.getText().toString();
                } else if (checkedId == R.id.rdoReason2) {
                    cancelReason[0] = dialogBinding.rdoReason2.getText().toString();
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
                String otherReason = dialogBinding.edtOtherReason.getText().toString().trim();
                cancelReason[0] += otherReason;
                String cancelledReason = "";
                if (otherReason.isEmpty()) {
                    cancelledReason = "Đã hủy bởi người bán. Lý do hủy: " + cancelReason[0];
                } else {
                    cancelledReason = "Đã hủy bởi người bán. Lý do hủy: " + cancelReason[0] + "Lý do khác: " + otherReason;
                }


                SharedPreferences sharedPreferences = context.getSharedPreferences(USER_INFO, MODE_PRIVATE);
                String userID = sharedPreferences.getString(USER_ID, null);

                invoiceUpdate.put(CANCELED_BY, userID);
                invoiceUpdate.put(CANCELED_REASON, cancelledReason);
                invoiceUpdate.put(CANCELED_AT, FormatHelper.getCurrentDateTime());

                invoiceApi invoiceApi = new invoiceApi();
                invoiceApi.updateStatusInvoiceApi(invoice.getBaseID(), invoiceUpdate, new UpdateDocumentCallback() {
                    @Override
                    public void onUpdateSuccess(String successMessage) {
                        dialogBinding.progressBar.setVisibility(View.GONE);
                        showToast(context, CANCEL_ORDER_SUCCESSFULLY);
                        dialog.dismiss();
                        adapter.removeItemAdapter(position);
                        adapter.updateInvoicesCount();
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

}
