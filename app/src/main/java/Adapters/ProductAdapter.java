package Adapters;
import static constants.keyName.PRODUCT_ID;
import static constants.keyName.STORE_ID;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.stores.databinding.ItemProductBinding;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

import Activities.ProductDetailActivity;
import models.Product;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {
    private final Context context;
    ArrayList<Product> list;

    public ProductAdapter(Context context, ArrayList<Product> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ProductAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemProductBinding binding = ItemProductBinding.inflate(LayoutInflater.from(context), parent, false);
        return new ViewHolder(binding);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        ItemProductBinding binding;
        public ViewHolder(ItemProductBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    public void updateProducts(ArrayList<Product> newProducts) {
        this.list.clear();
        this.list.addAll(newProducts);
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(@NonNull ProductAdapter.ViewHolder holder, int position) {

        Product product = list.get(holder.getBindingAdapterPosition());
        holder.binding.txtTitle.setText(product.getProductName());
        NumberFormat formatter = NumberFormat.getInstance(new Locale("vi", "VN"));
        String formattedPrice = formatter.format(product.getNewPrice());

        if (product.getInStock() == 0){
            holder.binding.layoutOutOfStock.setVisibility(View.VISIBLE);
        }
        holder.binding.txtPrice.setText("đ" + formattedPrice);
        holder.binding.txtSold.setText("Đã bán " + 200);
        holder.binding.txtSaleoff.setText("-" + 40 + "%");
        holder.binding.txtRating.setText("(" + 4.5 + ")");
        holder.binding.ratingBar.setRating(4.5F);
//        Glide.with(context).load(product.getProductImages().get(0)).into(holder.binding.imageView);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ProductDetailActivity.class);
                // truyền productId, storeId
                Bundle bundle = new Bundle();

                bundle.putString(PRODUCT_ID, product.getBaseId());
                bundle.putString(STORE_ID, product.getStoreId());
                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
