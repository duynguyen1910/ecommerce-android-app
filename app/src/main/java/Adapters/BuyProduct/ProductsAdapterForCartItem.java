package Adapters.BuyProduct;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.stores.databinding.ItemProductForCartItemBinding;


import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

import interfaces.InAdapter.CartItemListener;
import interfaces.InAdapter.ToTalFeeCallback;
import models.Product;

public class ProductsAdapterForCartItem extends RecyclerView.Adapter<ProductsAdapterForCartItem.ViewHolder> {
    private final Context context;
    private final ArrayList<Product> list;
    private final ToTalFeeCallback callbackClass;
    private final CartItemListener cartItemListener;


    public ProductsAdapterForCartItem(Context context, ArrayList<Product> list, ToTalFeeCallback callbackClass, CartItemListener cartItemListener) {
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
        Product product = list.get(holder.getBindingAdapterPosition());
        holder.binding.checkbox.setOnCheckedChangeListener(null);

        holder.binding.checkbox.setChecked(product.getCheckedStatus());
        holder.binding.checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                product.setCheckedStatus(isChecked);
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


        holder.binding.txtTitle.setText(product.getProductName());
        NumberFormat formatter = NumberFormat.getInstance(new Locale("vi", "VN"));
        String formattedOldPrice = formatter.format(product.getOldPrice());
        holder.binding.txtOldPrice.setText("đ" + formattedOldPrice);
        holder.binding.txtOldPrice.setPaintFlags(holder.binding.txtOldPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        String formattedPrice = formatter.format(product.getOldPrice());

        holder.binding.txtPrice.setText(formattedPrice);

        holder.binding.txtQuantity.setText(String.valueOf(product.getNumberInCart()));
//        Glide.with(context).load(product.getProductImages().get(0)).into(holder.binding.imageView);

        holder.binding.btnPlus.setOnClickListener(v -> {

            int quantity = Integer.parseInt(holder.binding.txtQuantity.getText().toString().trim());
            holder.binding.txtQuantity.setText((quantity + 1) + "");
            product.setNumberInCart(quantity + 1);
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
                product.setNumberInCart(quantity - 1);

            }
            if (holder.binding.checkbox.isChecked()) {
                callbackClass.totalFeeUpdate(getTotalFee());
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
                fee += (product.getNewPrice()  * product.getNumberInCart());
            }

        }
        return fee;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}