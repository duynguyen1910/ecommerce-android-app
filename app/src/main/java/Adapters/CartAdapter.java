package Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.stores.R;
import com.example.stores.databinding.ItemCartBinding;
import com.google.android.material.checkbox.MaterialCheckBox;

import java.util.ArrayList;

import api.invoiceApi;
import interfaces.CartItemListener;
import interfaces.GetCollectionCallback;
import interfaces.ToTalFeeCallback;
import models.Invoice;
import models.InvoiceDetail;
import models.Product;
import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {
    private final Context context;
    private final ArrayList<Invoice> list;
    private final ToTalFeeCallback totalFeeCallback;

    public CartAdapter(Context context, ArrayList<Invoice> list, ToTalFeeCallback totalFeeCallback) {
        this.context = context;
        this.list = list;
        this.totalFeeCallback = totalFeeCallback;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemCartBinding binding = ItemCartBinding.inflate(LayoutInflater.from(context), parent, false);
        return new ViewHolder(binding);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ItemCartBinding binding;

        public ViewHolder(ItemCartBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Invoice invoice = list.get(holder.getBindingAdapterPosition());
        holder.binding.txtStoreName.setText("DUY STORE");

        invoiceApi invoiceApi = new invoiceApi();
        invoiceApi.getInvoiceDetail(invoice.getBaseID(), new GetCollectionCallback<InvoiceDetail>() {
            @Override
            public void onGetListSuccess(ArrayList<InvoiceDetail> productList) {
                ProductsListAdapterForCartItem adapter = new ProductsListAdapterForCartItem(context, productList, totalFeeCallback, new CartItemListener() {
                    @Override
                    public void updateCartItem() {
                        int cartItemPosition = holder.getBindingAdapterPosition();
                        productList.remove(cartItemPosition);
//                totalFeeCallback.totalFeeUpdate(getTotalFee());
                        notifyItemRemoved(cartItemPosition);
                    }

                    @Override
                    public void updateCheckboxAllStatus(boolean isChecked) {
                        holder.binding.checkboxAll.setChecked(isChecked);
                    }
                });

                holder.binding.recyclerViewProducts.setLayoutManager(new LinearLayoutManager(context));
                holder.binding.recyclerViewProducts.setAdapter(adapter);
            }

            @Override
            public void onGetListFailure(String errorMessage) {
                Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show();
            }
        });

//
//        holder.binding.checkboxAll.addOnCheckedStateChangedListener(new MaterialCheckBox.OnCheckedStateChangedListener() {
//            @Override
//            public void onCheckedStateChangedListener(@NonNull MaterialCheckBox checkBox, int state) {
//                if (state == MaterialCheckBox.STATE_CHECKED) {
//                    for (Product product : listProducts) {
//                        product.setCheckedStatus(true);
//                    }
//                } else if (state == MaterialCheckBox.STATE_UNCHECKED) {
//                    for (Product product : listProducts) {
//                        product.setCheckedStatus(false);
//                    }
//                }
//                adapter.notifyDataSetChanged();
////                totalFeeCallback.totalFeeUpdate(getTotalFee());
//            }
//        });
//
//        ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
//            @Override
//            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
//                return false;
//            }
//
//            @SuppressLint("NotifyDataSetChanged")
//            @Override
//            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
//                int productPosition = viewHolder.getBindingAdapterPosition();
//                listProducts.remove(productPosition);
////                totalFeeCallback.totalFeeUpdate(getTotalFee());
//                adapter.notifyItemRemoved(productPosition);
//
//                if (listProducts.size() == 0) {
//                    int cartItemPosition = holder.getBindingAdapterPosition();
//                    list.remove(cartItemPosition);
//                    notifyItemRemoved(cartItemPosition);
////                    totalFeeCallback.totalFeeUpdate(getTotalFee());
//                }
//            }
//
//            @Override
//            public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
//                new RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
//                        .addSwipeLeftBackgroundColor(ContextCompat.getColor(context, R.color.primary_color))
//                        .addActionIcon(R.drawable.ic_delete)
//                        .setSwipeLeftLabelColor(ContextCompat.getColor(context, R.color.white))
//                        .addSwipeLeftLabel("Delete")
//                        .setIconHorizontalMargin(TypedValue.COMPLEX_UNIT_DIP, 16)
//                        .addCornerRadius(TypedValue.COMPLEX_UNIT_DIP, 12)
//                        .setSwipeLeftLabelTextSize(TypedValue.COMPLEX_UNIT_SP, 14)
//                        .create()
//                        .decorate();
//                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
//            }
//        };
//        new ItemTouchHelper(simpleCallback).attachToRecyclerView(holder.binding.recyclerViewProducts);
    }

//    private double getTotalFee() {
//        double fee = 0;
//        for (Invoice invoice : list) {
//            for (InvoiceDetail detail : invoice.getDetails()) {
//                Product product = detail.getProduct();
//                if (product.getCheckedStatus()) {
//                    fee += product.getNewPrice() * product.getNumberInCart();
//                }
//            }
//        }
//        return fee;
//    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
