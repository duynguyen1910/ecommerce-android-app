package Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.stores.databinding.ItemProductForInvoiceItemBinding;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

import models.Product;

public class ProductsListAdapterForInvoiceItem extends RecyclerView.Adapter<ProductsListAdapterForInvoiceItem.ViewHolder> {
    private final Context context;
    private final ArrayList<Product> list;
    private boolean showOnlyFirstItem;

    public ProductsListAdapterForInvoiceItem(Context context, ArrayList<Product> list, boolean showOnlyFirstItem) {
        this.context = context;
        this.list = list;
        this.showOnlyFirstItem = showOnlyFirstItem;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemProductForInvoiceItemBinding binding = ItemProductForInvoiceItemBinding.inflate(LayoutInflater.from(context), parent, false);
        return new ViewHolder(binding);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        ItemProductForInvoiceItemBinding binding;

        public ViewHolder(ItemProductForInvoiceItemBinding binding) {
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
        String formattedPrice = formatter.format(product.getPrice() * (1 - product.getSaleoff() / 100));
        holder.binding.txtPrice.setText("đ" + formattedPrice);

        holder.binding.txtQuantity.setText("x" + product.getNumberInCart());
        Glide.with(context).load(product.getPicUrl().get(0)).into(holder.binding.imageView);

    }

    private int getQuantityChecked() {
        int count = 0;
        for (Product product : list) {
            if (product.getCheckedStatus()) {
                count += 1;
            }
        }
        return count;
    }

    private double getTotalFee() {
        double fee = 0;
        for (Product product : list) {
            if (product.getCheckedStatus()) {
                fee += (product.getPrice() * (1 - product.getSaleoff() / 100) * product.getNumberInCart());
            }

        }
        return fee;
    }

    @Override
    public int getItemCount() {
        return showOnlyFirstItem ? Math.min(list.size(), 1) : list.size();
    }
}
