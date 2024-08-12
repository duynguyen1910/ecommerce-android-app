package Adapters.Statistics;
import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.stores.databinding.ItemProductForStatisticsBinding;

import java.util.ArrayList;

import models.Product;
import utils.Chart.CustomValueMoneyFormatter;

public class ProductStatisticsAdapter extends RecyclerView.Adapter<ProductStatisticsAdapter.ViewHolder> {
    private final Context context;
    private final ArrayList<Product> list;

    public ProductStatisticsAdapter(Context context, ArrayList<Product> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemProductForStatisticsBinding binding = ItemProductForStatisticsBinding.inflate(LayoutInflater.from(context), parent, false);
        return new ViewHolder(binding);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        ItemProductForStatisticsBinding binding;

        public ViewHolder(ItemProductForStatisticsBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }


    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Product product = list.get(holder.getBindingAdapterPosition());
        holder.binding.imageView.setVisibility(View.VISIBLE);
        if (product.getProductImages() != null) {
            Glide.with(context).load(product.getProductImages().get(0)).into(holder.binding.imageView);
        }
        holder.binding.txtIndex.setText(String.valueOf(holder.getBindingAdapterPosition() + 1));
        holder.binding.txtProductName.setText(product.getProductName());

        holder.binding.txtNewPrice.setText(new CustomValueMoneyFormatter().getFormattedValue((float) product.getNewPrice()));

        holder.binding.txtSold.setText(String.valueOf(product.getSold()));
        holder.binding.txtProductRevenue.setText(new CustomValueMoneyFormatter().getFormattedValue((float) product.getProductRevenue()));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
