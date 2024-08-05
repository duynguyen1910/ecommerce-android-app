package Adapters;

import static android.content.Context.MODE_PRIVATE;
import static constants.keyName.PRODUCT_ID;
import static constants.keyName.STORE_ID;
import static constants.keyName.USER_INFO;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.stores.databinding.ItemGridProductBinding;
import com.example.stores.databinding.ItemPreviewImageBinding;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Objects;

import Activities.BuyProduct.ProductDetailActivity;
import models.Product;


public class PreviewImageAdapter extends RecyclerView.Adapter<PreviewImageAdapter.ViewHolder> {
    private final Context context;
    ArrayList<Uri> imagesUriList;

    public PreviewImageAdapter(Context context, ArrayList<Uri> imagesUriList) {
        this.context = context;
        this.imagesUriList = imagesUriList;
    }

    @NonNull
    @Override
    public PreviewImageAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemPreviewImageBinding binding = ItemPreviewImageBinding.inflate(LayoutInflater.from(context), parent, false);
        return new ViewHolder(binding);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        ItemPreviewImageBinding binding;

        public ViewHolder(ItemPreviewImageBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }


    @Override
    public void onBindViewHolder(@NonNull PreviewImageAdapter.ViewHolder holder, int position) {
        Uri uri = imagesUriList.get(holder.getBindingAdapterPosition());
        if(uri != null) {
            Glide.with(context).load(uri).into(holder.binding.previewImageView);
        }
    }



    @Override
    public int getItemCount() {
        return imagesUriList.size();
    }
}
