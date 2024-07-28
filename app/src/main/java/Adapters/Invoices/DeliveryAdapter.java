package Adapters.Invoices;
import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.stores.databinding.ItemDeliveryBinding;

import java.util.ArrayList;

import models.CartItem;
import models.Invoice;
import models.Product;

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


//        Invoice invoice = list.get(holder.getBindingAdapterPosition());
//
//        holder.binding.txtDeliveryAddress.setText(invoice.getDeliveryAddress());
//        CartItem cartItem = invoice.getCartItem();
//        holder.binding.txtCustomerName.setText(invoice.getCustomerName());
//        ProductsAdapterForDelivery adapter = new ProductsAdapterForDelivery(context, cartItem.getListProducts(), true);
//        // 0 Chờ người bán xác nhận
//        // 1 người bán đã xác nhận, chờ lấy hàng
//        // 2 Đã hủy
//        // 3 Đang giao hàng
//        // 4 đã hoàn thành
//        if (invoice.getInvoiceStatus() == 1){
//            holder.binding.txtInvoiceStatus.setText("Chờ lấy hàng");
//            holder.binding.txtInvoiceStatus.setTextColor(ContextCompat.getColor(context, R.color.black));
//            holder.binding.layoutControl.setVisibility(View.VISIBLE);
//            holder.binding.btnDelivery.setVisibility(View.VISIBLE);
//            holder.binding.btnCancel.setVisibility(View.VISIBLE);
//            holder.binding.btnComplete.setVisibility(View.GONE);
//        }else if(invoice.getInvoiceStatus() == 3) {
//            holder.binding.txtInvoiceStatus.setText("Đang giao hàng");
//            holder.binding.txtInvoiceStatus.setTextColor(ContextCompat.getColor(context, R.color.primary_color));
//            holder.binding.layoutControl.setVisibility(View.VISIBLE);
//            holder.binding.btnCancel.setVisibility(View.GONE);
//            holder.binding.btnDelivery.setVisibility(View.GONE);
//            holder.binding.btnComplete.setVisibility(View.VISIBLE);
//        }else if(invoice.getInvoiceStatus() == 4){
//            holder.binding.txtInvoiceStatus.setText("Hoàn thành");
//            holder.binding.txtInvoiceStatus.setTextColor(ContextCompat.getColor(context, R.color.secondary_color));
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
//
//        holder.itemView.setOnClickListener(v -> {
//            Intent intent = new Intent(context, InvoiceDetailActivity.class);
//            intent.putExtra("invoice", invoice);
//            context.startActivity(intent);
//        });
//        holder.binding.btnCancel.setOnClickListener(v -> {
//            invoice.setInvoiceStatus(2);
//            holder.binding.txtInvoiceStatus.setText("Đã hủy");
//        });
//        holder.binding.btnDelivery.setOnClickListener(v -> {
//            invoice.setInvoiceStatus(3);
//            holder.binding.txtInvoiceStatus.setText("Đang giao hàng");
//        });
//
//        holder.binding.btnComplete.setOnClickListener(v -> {
//            invoice.setInvoiceStatus(4);
//            holder.binding.txtInvoiceStatus.setText("Hoàn thành");
//        });

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
