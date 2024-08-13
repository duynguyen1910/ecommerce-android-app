package Adapters;
import static android.content.Context.MODE_PRIVATE;
import static constants.keyName.PRODUCT_ID;
import static constants.keyName.STORE_ID;
import static constants.keyName.USER_INFO;
import static constants.keyName.USER_ROLE;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.stores.databinding.ItemGridProductBinding;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Objects;
import Activities.BuyProduct.ProductDetailActivity;
import models.Product;
import utils.FormatHelper;


public class ProductGridAdapter extends RecyclerView.Adapter<ProductGridAdapter.ViewHolder> {
    private final Context context;
    ArrayList<Product> list;

    public ProductGridAdapter(Context context, ArrayList<Product> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ProductGridAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemGridProductBinding binding = ItemGridProductBinding.inflate(LayoutInflater.from(context), parent, false);
        return new ViewHolder(binding);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        ItemGridProductBinding binding;
        public ViewHolder(ItemGridProductBinding binding) {
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
    public void onBindViewHolder(@NonNull ProductGridAdapter.ViewHolder holder, int position) {

        Product product = list.get(holder.getBindingAdapterPosition());

        if(product.getProductImages() != null) {
            Glide.with(context).load(product.getProductImages().get(0)).into(holder.binding.imageView);
        }

        holder.binding.txtTitle.setText(product.getProductName());
        if (product.getInStock() == 0){
            holder.binding.layoutOutOfStock.setVisibility(View.VISIBLE);
        }
        holder.binding.txtPrice.setText(FormatHelper.formatVND(product.getNewPrice()));
        holder.binding.txtSold.setText("Đã bán " + product.getSold());
        holder.binding.ratingBar.setRating(5.0F);


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreferences = context.getSharedPreferences(USER_INFO, MODE_PRIVATE);
                String storeId = sharedPreferences.getString(STORE_ID, null);
                int roleValue = sharedPreferences.getInt(USER_ROLE, -1);
                Intent intent = new Intent(context, ProductDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString(PRODUCT_ID, product.getBaseID());
                bundle.putString(STORE_ID, product.getStoreID());
                if (Objects.equals(storeId, product.getStoreID()) || roleValue == 3){
                    bundle.putBoolean("buyable", false);
                }else {
                    bundle.putBoolean("buyable", true);
                }
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
