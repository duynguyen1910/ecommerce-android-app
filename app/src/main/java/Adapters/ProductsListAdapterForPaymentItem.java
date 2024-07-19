package Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.stores.databinding.ItemProductForPaymentItemBinding;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

import models.Product;

public class ProductsListAdapterForPaymentItem extends RecyclerView.Adapter<ProductsListAdapterForPaymentItem.ViewHolder> {
    private final Context context;
    private final ArrayList<Product> list;

    public ProductsListAdapterForPaymentItem(Context context, ArrayList<Product> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemProductForPaymentItemBinding binding = ItemProductForPaymentItemBinding.inflate(LayoutInflater.from(context), parent, false);
        return new ViewHolder(binding);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        ItemProductForPaymentItemBinding binding;

        public ViewHolder(ItemProductForPaymentItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }


    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Product product = list.get(holder.getBindingAdapterPosition());

        holder.binding.txtProductTitle.setText(product.getProductName());
        NumberFormat formatter = NumberFormat.getInstance(new Locale("vi", "VN"));
        String formattedPrice = formatter.format(product.getNewPrice());
        holder.binding.txtPrice.setText("đ" + formattedPrice);

        holder.binding.txtQuantity.setText("x" + product.getNumberInCart());
//        Glide.with(context).load(product.getProductImages().get(0)).into(holder.binding.imageView);

    }
    @Override
    public int getItemCount() {
        return list.size();
    }
}
