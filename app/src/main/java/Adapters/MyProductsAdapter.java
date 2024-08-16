package Adapters;

import static constants.keyName.PRODUCT_ID;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.stores.databinding.ItemMyProductBinding;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

import Activities.ProductSetup.UpdateProductActivity;
import models.Product;

public class MyProductsAdapter extends RecyclerView.Adapter<MyProductsAdapter.ViewHolder> {
    private final Context context;
    private final ArrayList<Product> list;

    public MyProductsAdapter(Context context, ArrayList<Product> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       ItemMyProductBinding binding = ItemMyProductBinding.inflate(LayoutInflater.from(context), parent, false);
        return new ViewHolder(binding);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

       ItemMyProductBinding binding;

        public ViewHolder(ItemMyProductBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }


    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Product product = list.get(holder.getBindingAdapterPosition());

        if(product.getProductImages() != null) {
            Glide.with(context).load(product.getProductImages().get(0)).into(holder.binding.imageView);
        }

        holder.binding.txtTitle.setText(product.getProductName());
        NumberFormat formatter = NumberFormat.getInstance(new Locale("vi", "VN"));
        String formattedNewPrice = formatter.format(product.getNewPrice());
        holder.binding.txtNewPrice.setText("đ" + formattedNewPrice);

        holder.binding.txtInStock.setText(String.valueOf(product.getInStock()));
        holder.binding.txtSold.setText(String.valueOf(product.getSold()));

        holder.binding.btnUpdate.setOnClickListener(v -> {
            Intent intent = new Intent(context, UpdateProductActivity.class);
            intent.putExtra(PRODUCT_ID, product.getBaseID());
            context.startActivity(intent);

        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
