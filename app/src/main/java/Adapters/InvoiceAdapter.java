package Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.stores.databinding.ItemInvoiceBinding;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Map;

import Models.CartItem;
import Models.Invoice;
import Models.Product;

public class InvoiceAdapter extends RecyclerView.Adapter<InvoiceAdapter.ViewHolder> {
    private final Context context;
    private final Map<String, Invoice> map;
    private final ArrayList<String> invoiceIDs;


    public InvoiceAdapter(Context context, Map<String, Invoice> map) {
        this.context = context;
        this.map = map;
        this.invoiceIDs = new ArrayList<>(map.keySet());
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


        String invoiceID = invoiceIDs.get(holder.getBindingAdapterPosition());
        Invoice invoice = map.get(invoiceID);

        CartItem cartItem = invoice.getCartItem();
        holder.binding.txtStoreName.setText(cartItem.getStoreName());
        ProductsListAdapterForInvoiceItem adapter = new ProductsListAdapterForInvoiceItem(context, cartItem.getListProducts(), true);

        holder.binding.recyclerViewProducts.setLayoutManager(new LinearLayoutManager(context));
        holder.binding.recyclerViewProducts.setAdapter(adapter);

        holder.binding.txtQuantityProducts.setText(cartItem.getListProducts().size() + " sản phẩm");


        NumberFormat formatter = NumberFormat.getInstance(new Locale("vi", "VN"));
        holder.binding.txtTotal.setText("đ" + formatter.format(getCartItemFee(cartItem)));

    }

    private double getCartItemFee(CartItem cartItem) {
        double fee = 0;
        for (Product product : cartItem.getListProducts()) {
            fee += (product.getPrice() * (1 - product.getSaleoff() / 100) * product.getNumberInCart());
        }
        return fee;
    }


    @Override
    public int getItemCount() {
        return map.size();
    }
}
