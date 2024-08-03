package utils;

import static android.content.Context.MODE_PRIVATE;
import static constants.keyName.CANCELED_AT;
import static constants.keyName.CANCELED_BY;
import static constants.keyName.CANCELED_REASON;
import static constants.keyName.STATUS;
import static constants.keyName.STORE_ID;
import static constants.keyName.USER_ID;
import static constants.keyName.USER_INFO;
import static constants.toastMessage.CANCEL_ORDER_SUCCESSFULLY;
import static utils.Cart.CartUtils.showToast;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RadioGroup;

import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;

import com.example.stores.R;
import com.example.stores.databinding.DialogCancelInvoiceByCustomerBinding;
import com.example.stores.databinding.DialogCancelInvoiceByDeliveryBinding;
import com.example.stores.databinding.DialogCancelInvoiceByStoreBinding;
import com.example.stores.databinding.DialogCancelledInvoiceDetailBinding;
import com.example.stores.databinding.DialogFilterBinding;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import Activities.BuyProduct.SearchActivity;
import Adapters.SettingVariant.TypeValueAdapterForFilter;
import api.invoiceApi;
import api.userApi;
import enums.OrderStatus;
import enums.UserRole;
import interfaces.InAdapter.FilterListener;
import interfaces.UpdateDocumentCallback;
import interfaces.UserCallback;
import models.Invoice;
import models.TypeValue;
import models.User;

public class DialogCancelInvoiceUtils {
    public static void popUpCancelInvoiceByCustomerDialog(Context context, Invoice invoice) {
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

    public static void popUpCancelInvoiceByDeliveryDialog(Context context, Invoice invoice) {
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

    public static void popUpCancelInvoiceByStoreDialog(Context context, Invoice invoice) {
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


    public static void popUpCancelledInvoiceDetailDialog(Context context, Invoice invoice) {

        // setup UI cho dialog
        Dialog dialog = new Dialog(context, android.R.style.Theme_Material_Light_Dialog);
        DialogCancelledInvoiceDetailBinding dialogBinding = DialogCancelledInvoiceDetailBinding.inflate((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE));
        dialog.setContentView(dialogBinding.getRoot());

        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        Window window = dialog.getWindow();
        if (window != null) {
            layoutParams.copyFrom(window.getAttributes());
            layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
            layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
            window.setAttributes(layoutParams);

        }



        dialogBinding.progressBar.getIndeterminateDrawable()
                .setColorFilter(ContextCompat.getColor(context, R.color.primary_color),
                        PorterDuff.Mode.MULTIPLY);
        dialogBinding.progressBar.setVisibility(View.VISIBLE);
        //lấy role của người hủy đơn
        userApi myUserApi = new userApi();
        myUserApi.getUserInfoApi(invoice.getCancelledBy(), new UserCallback() {
            @Override
            public void getUserInfoSuccess(User user) {
                dialog.show();
                dialogBinding.progressBar.setVisibility(View.GONE);

                Animation slideUp = AnimationUtils.loadAnimation(context, R.anim.slide_in_from_right);
                dialogBinding.getRoot().startAnimation(slideUp);
                UserRole role = user.getRole();
                switch (role) {
                    case STORE_OWNER_ROLE:
                        dialogBinding.txtRole.setText("Người bán");
                        break;
                    case CUSTOMER_ROLE:
                        dialogBinding.txtRole.setText("Người mua");
                        break;
                    case SHIPPER_ROLE:
                        dialogBinding.txtRole.setText("Đơn vị vận chuyển");
                        break;
                    default:
                        dialogBinding.txtRole.setText("Ecommerce");
                        break;
                }
                dialogBinding.txtCanceledAt.setText(
                        FormatHelper.formatDateTime(invoice.getCancelledAt()));
                dialogBinding.txtCanceledReason.setText((invoice.getCancelledReason()));


            }

            @Override
            public void getUserInfoFailure(String errorMessage) {
                dialogBinding.progressBar.setVisibility(View.GONE);
                showToast(context, errorMessage);
            }
        });
        dialogBinding.btnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });


    }
}
