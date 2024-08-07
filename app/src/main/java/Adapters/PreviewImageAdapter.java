package Adapters;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.stores.databinding.ItemPreviewImageBinding;

import java.util.ArrayList;

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
