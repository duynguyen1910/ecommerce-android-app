package Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.stores.R;
import com.example.stores.databinding.ItemRequestInvoiceBinding;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

import Activities.InvoiceDetailActivity;
import models.CartItem;
import models.Invoice;
import models.Product;

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

//        CartItem cartItem = invoice.getCartItem();
//        holder.binding.txtCustomerName.setText(invoice.getCustomerName());
//        ProductsAdapterForRequestInvoice adapter = new ProductsAdapterForRequestInvoice(context, cartItem.getListProducts(), true);

        // 0 Chờ người bán xác nhận
        // 1 người bán đã xác nhận, chờ lấy hàng
        // 2 Đã hủy
        // 3 Đang giao hàng
        // 4 đã hoàn thành
//        if (invoice.getInvoiceStatus() == 0){
//            holder.binding.txtInvoiceStatus.setText("Chờ xác nhận");
//            holder.binding.txtInvoiceStatus.setTextColor(ContextCompat.getColor(context, R.color.primary_color));
//            holder.binding.layoutControl.setVisibility(View.VISIBLE);
//        }else if(invoice.getInvoiceStatus() == 1) {
//            holder.binding.txtInvoiceStatus.setText("Đã xác nhận");
//            holder.binding.txtInvoiceStatus.setTextColor(ContextCompat.getColor(context, R.color.secondary_color));
//            holder.binding.layoutControl.setVisibility(View.VISIBLE);
//            holder.binding.btnSubmit.setVisibility(View.GONE);
//        }else if(invoice.getInvoiceStatus() == 2){
//            holder.binding.txtInvoiceStatus.setText("Đã hủy");
//            holder.binding.txtInvoiceStatus.setTextColor(ContextCompat.getColor(context, R.color.black));
//            holder.binding.layoutControl.setVisibility(View.GONE);
//        }
//        holder.binding.recyclerViewProducts.setLayoutManager(new LinearLayoutManager(context));
//        holder.binding.recyclerViewProducts.setAdapter(adapter);
//
//        holder.binding.txtQuantityProducts.setText(cartItem.getListProducts().size() + " sản phẩm");
//        holder.binding.txtCreatedDate.setText(invoice.getCreatedDate());
//
//
//        NumberFormat formatter = NumberFormat.getInstance(new Locale("vi", "VN"));
//        holder.binding.txtTotal.setText("đ" + formatter.format(getCartItemFee(cartItem)));
//        invoice.setTotal(getCartItemFee(cartItem));

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, InvoiceDetailActivity.class);
            intent.putExtra("invoice", invoice);
            context.startActivity(intent);
        });
        holder.binding.btnSubmit.setOnClickListener(v -> {
//            invoice.setInvoiceStatus(1); //đã xác nhận đơn hàng
            holder.binding.txtInvoiceStatus.setText("Đã xác nhận");
        });
        holder.binding.btnCancel.setOnClickListener(v -> {
//            invoice.setInvoiceStatus(2); //đã hủy
            holder.binding.txtInvoiceStatus.setText("Đã hủy");
        });

    }

    private double getCartItemFee(CartItem cartItem) {
        double fee = 0;
        for (Product product : cartItem.getListProducts()) {
            fee += (product.getNewPrice() * product.getNumberInCart());
        }
        return fee;
    }


    @Override
    public int getItemCount() {
        return list.size();
    }
}