package Adapters.BuyProduct;

import static utils.Cart.CartUtils.getCartItemFee;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.stores.databinding.ItemPaymentBinding;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;


import models.CartItem;
import models.Product;
import models.Variant;


public class PaymentAdapter extends RecyclerView.Adapter<PaymentAdapter.ViewHolder> {
    private final Context context;
    private final ArrayList<CartItem> list;

    public PaymentAdapter(Context context, ArrayList<CartItem> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemPaymentBinding binding = ItemPaymentBinding.inflate(LayoutInflater.from(context), parent, false);
        return new ViewHolder(binding);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        ItemPaymentBinding binding;

        public ViewHolder(ItemPaymentBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        CartItem cartItem = list.get(holder.getBindingAdapterPosition());
        ArrayList<Variant> listVariants = cartItem.getListVariants();

        holder.binding.txtStoreName.setText(cartItem.getStoreName());

        ProductsAdapterForInvoice adapter = new ProductsAdapterForInvoice(context, cartItem.getListVariants());
        holder.binding.recyclerViewProducts.setLayoutManager(new LinearLayoutManager(context));
        holder.binding.recyclerViewProducts.setAdapter(adapter);

        holder.binding.txtQuantityProducts.setText("Tổng số tiền (" + listVariants.size() + " sản phẩm)");


        NumberFormat formatter = NumberFormat.getInstance(new Locale("vi", "VN"));
        holder.binding.txtTotal.setText("đ" + formatter.format(getCartItemFee(cartItem)));

        holder.binding.txtInvoiceNote.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                cartItem.setNote(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}