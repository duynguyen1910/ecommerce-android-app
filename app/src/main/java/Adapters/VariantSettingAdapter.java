package Adapters;

import static constants.keyName.PRODUCT_ID;
import static constants.keyName.STORE_ID;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.stores.databinding.ItemSettingVariantDetailBinding;
import com.google.errorprone.annotations.Var;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

import Activities.ProductDetailActivity;
import models.Product;
import models.Variant;

public class VariantSettingAdapter extends RecyclerView.Adapter<VariantSettingAdapter.ViewHolder> {
    private final Context context;
    ArrayList<Variant> list;

    public VariantSettingAdapter(Context context, ArrayList<Variant> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public VariantSettingAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemSettingVariantDetailBinding binding = ItemSettingVariantDetailBinding.inflate(LayoutInflater.from(context), parent, false);
        return new ViewHolder(binding);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ItemSettingVariantDetailBinding binding;

        public ViewHolder(ItemSettingVariantDetailBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }


    @Override
    public void onBindViewHolder(@NonNull VariantSettingAdapter.ViewHolder holder, int position) {
        Variant variant = list.get(holder.getBindingAdapterPosition());
        holder.binding.txtVariantName.setText(variant.getVariantName());

        holder.binding.edtNewPrice.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                updateNewPrice(holder.binding.edtNewPrice, variant);
            }
        });

        holder.binding.edtInStock.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                updateInStock(holder.binding.edtInStock, variant);
            }
        });
    }

    private void updateNewPrice(EditText edtNewPrice, Variant variant) {
        String newPriceStr = edtNewPrice.getText().toString().trim();
        variant.setNewPrice(Double.parseDouble(newPriceStr));
        variant.setOldPrice(Double.parseDouble(newPriceStr));
    }

    private void updateInStock(EditText edtInStock, Variant variant) {
        String inStockStr = edtInStock.getText().toString().trim();
        variant.setInStock(Integer.parseInt(inStockStr));
    }

    public ArrayList<Variant> getVariantsList() {
        return list;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
