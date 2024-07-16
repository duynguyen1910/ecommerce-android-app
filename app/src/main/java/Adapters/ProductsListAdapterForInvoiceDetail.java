package Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.stores.databinding.ItemProductForInvoiceDetailBinding;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

import models.Product;

public class ProductsListAdapterForInvoiceDetail extends RecyclerView.Adapter<ProductsListAdapterForInvoiceDetail.ViewHolder> {
    private final Context context;
    private final ArrayList<Product> list;

    public ProductsListAdapterForInvoiceDetail(Context context, ArrayList<Product> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemProductForInvoiceDetailBinding binding = ItemProductForInvoiceDetailBinding.inflate(LayoutInflater.from(context), parent, false);
        return new ViewHolder(binding);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        ItemProductForInvoiceDetailBinding binding;

        public ViewHolder(ItemProductForInvoiceDetailBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }


    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Product product = list.get(holder.getBindingAdapterPosition());

        holder.binding.txtProductTitle.setText(product.getTitle());
        NumberFormat formatter = NumberFormat.getInstance(new Locale("vi", "VN"));
        String formattedOldPrice = formatter.format(product.getOldPrice());
        holder.binding.txtOldPrice.setText("đ" + formattedOldPrice);
        holder.binding.txtOldPrice.setPaintFlags(holder.binding.txtOldPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        String formattedPrice = formatter.format(product.getOldPrice() * (100 - product.getSaleoff()) / 100);

        holder.binding.txtPrice.setText(formattedPrice);

        holder.binding.txtQuantity.setText("x" + product.getNumberInCart());
        Glide.with(context).load(product.getPicUrl().get(0)).into(holder.binding.imageView);

    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
