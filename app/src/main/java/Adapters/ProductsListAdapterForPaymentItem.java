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

import Models.Product;

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

        holder.binding.txtProductTitle.setText(product.getTitle());
        NumberFormat formatter = NumberFormat.getInstance(new Locale("vi", "VN"));
        String formattedPrice = formatter.format(product.getOldPrice() * (100 - product.getSaleoff()) / 100);

        holder.binding.txtPrice.setText(formattedPrice);

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
                fee += product.getPrice() * product.getNumberInCart();
            }

        }
        return fee;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
