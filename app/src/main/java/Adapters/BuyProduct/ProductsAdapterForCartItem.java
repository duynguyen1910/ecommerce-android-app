package Adapters.BuyProduct;

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
import com.example.stores.databinding.ItemProductForCartItemBinding;


import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

import interfaces.InAdapter.CartItemListener;
import interfaces.InAdapter.ToTalFeeCallback;
import models.Product;
import models.Variant;

public class ProductsAdapterForCartItem extends RecyclerView.Adapter<ProductsAdapterForCartItem.ViewHolder> {
    private final Context context;
    private final ArrayList<Variant> list;
    private final ToTalFeeCallback callbackClass;
    private final CartItemListener cartItemListener;


    public ProductsAdapterForCartItem(Context context, ArrayList<Variant> list, ToTalFeeCallback callbackClass, CartItemListener cartItemListener) {
        this.context = context;
        this.list = list;
        this.callbackClass = callbackClass;
        this.cartItemListener = cartItemListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemProductForCartItemBinding binding = ItemProductForCartItemBinding.inflate(LayoutInflater.from(context), parent, false);
        return new ViewHolder(binding);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        ItemProductForCartItemBinding binding;

        public ViewHolder(ItemProductForCartItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }


    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Variant variant = list.get(holder.getBindingAdapterPosition());
        holder.binding.checkbox.setOnCheckedChangeListener(null);

        holder.binding.checkbox.setChecked(variant.getCheckedStatus());
        holder.binding.checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                variant.setCheckedStatus(isChecked);
                if (getQuantityChecked() == list.size()) {
                    // Nếu tất cả các checkbox items được check, check checkboxAll
                    cartItemListener.updateCheckboxAllStatus(true);
                } else if (getQuantityChecked() == 0) {
                    cartItemListener.updateCheckboxAllStatus(false);
                }
                // Cập nhật tổng phí
                callbackClass.totalFeeUpdate(getTotalFee());
            }
        });

        if(variant.getVariantImageUrl() != null) {
            Glide.with(context).load(variant.getVariantImageUrl()).into(holder.binding.imageView);
        }


        holder.binding.txtProductName.setText(variant.getProductName());
        if (variant.getVariantName() != null){
            holder.binding.txtVariantName.setText(variant.getVariantName());
        }else {
            holder.binding.txtVariantName.setVisibility(View.GONE);
        }

        NumberFormat formatter = NumberFormat.getInstance(new Locale("vi", "VN"));
        String formattedOldPrice = formatter.format(variant.getOldPrice());
        holder.binding.txtOldPrice.setText("đ" + formattedOldPrice);
        holder.binding.txtOldPrice.setPaintFlags(holder.binding.txtOldPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        String formattedPrice = formatter.format(variant.getOldPrice());

        holder.binding.txtPrice.setText(formattedPrice);

        holder.binding.txtQuantity.setText(String.valueOf(variant.getNumberInCart()));

        holder.binding.btnPlus.setOnClickListener(v -> {

            int quantity = Integer.parseInt(holder.binding.txtQuantity.getText().toString().trim());
            holder.binding.txtQuantity.setText((quantity + 1) + "");
            variant.setNumberInCart(quantity + 1);
            if (holder.binding.checkbox.isChecked()) {
                callbackClass.totalFeeUpdate(getTotalFee());
            }
        });


        holder.binding.btnMinus.setOnClickListener(v -> {

            int quantity = Integer.parseInt(holder.binding.txtQuantity.getText().toString().trim());
            int currentPosition = holder.getBindingAdapterPosition();
            if (quantity == 1) {
                list.remove(currentPosition);
                notifyItemRemoved(currentPosition);
                if (list.isEmpty()) {
                    cartItemListener.updateCartItem();
                }
            } else {
                holder.binding.txtQuantity.setText((quantity - 1) + "");
                variant.setNumberInCart(quantity - 1);

            }
            if (holder.binding.checkbox.isChecked()) {
                callbackClass.totalFeeUpdate(getTotalFee());
            }

        });
    }

    private int getQuantityChecked() {
        int count = 0;
        for (Variant variant : list) {
            if (variant.getCheckedStatus()) {
                count += 1;
            }
        }
        return count;
    }

    private double getTotalFee() {
        double fee = 0;
        for (Variant variant : list) {
            if (variant.getCheckedStatus()) {
                fee += (variant.getNewPrice()  * variant.getNumberInCart());
            }

        }
        return fee;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}