package Adapters.Invoices;
import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.stores.databinding.ItemProductForDeliveryBinding;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

import models.Product;

public class ProductsAdapterForDelivery extends RecyclerView.Adapter<ProductsAdapterForDelivery.ViewHolder> {
    private final Context context;
    private final ArrayList<Product> list;
    private final boolean showOnlyFirstItem;

    public ProductsAdapterForDelivery(Context context, ArrayList<Product> list, boolean showOnlyFirstItem) {
        this.context = context;
        this.list = list;
        this.showOnlyFirstItem = showOnlyFirstItem;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemProductForDeliveryBinding binding = ItemProductForDeliveryBinding.inflate(LayoutInflater.from(context), parent, false);
        return new ViewHolder(binding);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        ItemProductForDeliveryBinding binding;

        public ViewHolder(ItemProductForDeliveryBinding binding) {
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
        return showOnlyFirstItem ? Math.min(list.size(), 1) : list.size();
    }
}
