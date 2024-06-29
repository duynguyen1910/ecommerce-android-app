package Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.stores.databinding.ItemCartBinding;


import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

import Activities.CallbackClass;
import Models.Product;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {
    private final Context context;
    private final ArrayList<Product> list;
    CallbackClass callbackClass;


    public CartAdapter(Context context, ArrayList<Product> list, CallbackClass callbackClass) {
        this.context = context;
        this.list = list;
        this.callbackClass = callbackClass;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemCartBinding binding = ItemCartBinding.inflate(LayoutInflater.from(context), parent, false);
        return new ViewHolder(binding);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        ItemCartBinding binding;

        public ViewHolder(ItemCartBinding binding) {
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
        holder.binding.txtOldPrice.setPaintFlags(holder.binding.txtOldPrice.getPaintFlags()| Paint.STRIKE_THRU_TEXT_FLAG);
        String formattedPrice = formatter.format(product.getOldPrice()*(100-product.getSaleoff())/100);

        holder.binding.txtPrice.setText(formattedPrice);

        holder.binding.txtQuantity.setText(String.valueOf(product.getNumberInCart()));
        Glide.with(context).load(product.getPicUrl().get(0)).into(holder.binding.imageView);

        holder.binding.btnPlus.setOnClickListener(v -> {
            int quantity = Integer.parseInt(holder.binding.txtQuantity.getText().toString().trim());
            holder.binding.txtQuantity.setText((quantity + 1) + "");
            product.setNumberInCart(quantity + 1);
            callbackClass.callbackFunction();
            notifyDataSetChanged();
        });


        holder.binding.btnMinus.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onClick(View v) {
                int quantity = Integer.parseInt(holder.binding.txtQuantity.getText().toString().trim());
                if (quantity == 1) {
                    list.remove(product);

                } else {

                    holder.binding.txtQuantity.setText((quantity - 1) + "");
                    product.setNumberInCart(quantity - 1);
                }
                notifyDataSetChanged();
                callbackClass.callbackFunction();
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
