package Adapters.BuyProduct;
import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.stores.databinding.ItemProductForInvoiceBinding;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

import models.Product;

public class ProductsAdapterForInvoice extends RecyclerView.Adapter<ProductsAdapterForInvoice.ViewHolder> {
    private final Context context;
    private final ArrayList<Product> list;

    public ProductsAdapterForInvoice(Context context, ArrayList<Product> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemProductForInvoiceBinding binding = ItemProductForInvoiceBinding.inflate(LayoutInflater.from(context), parent, false);
        return new ViewHolder(binding);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        ItemProductForInvoiceBinding binding;

        public ViewHolder(ItemProductForInvoiceBinding binding) {
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
        holder.binding.txtNewPrice.setText("đ" + formattedPrice);
        holder.binding.txtQuantity.setText("x" + product.getNumberInCart());

//        Glide.with(context).load(product.getProductImages().get(0)).into(holder.binding.imageView);

    }
    @Override
    public int getItemCount() {
        return list.size();
    }
}
