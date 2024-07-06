package Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.stores.databinding.ItemMyProductBinding;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

import Models.Product;

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

        holder.binding.txtTitle.setText(product.getTitle());
        NumberFormat formatter = NumberFormat.getInstance(new Locale("vi", "VN"));
        String formattedOldPrice = formatter.format(product.getOldPrice());
        holder.binding.txtOldPrice.setText("đ" + formattedOldPrice);

        Glide.with(context).load(product.getPicUrl().get(0)).into(holder.binding.imageView);

        holder.binding.txtInStock.setText(String.valueOf(product.getInStock()));
        holder.binding.txtSold.setText(String.valueOf(product.getSold()));
        holder.binding.txtLikes.setText(String.valueOf(product.getLikes()));
        holder.binding.txtViews.setText(String.valueOf(product.getViews()));

        holder.binding.btnHide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        holder.binding.btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        holder.binding.btnMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });





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
        return list.size();
    }
}
