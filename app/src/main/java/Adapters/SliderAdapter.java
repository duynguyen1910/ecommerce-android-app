package Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.stores.R;


import java.util.ArrayList;

import Models.SliderItem;

public class SliderAdapter extends RecyclerView.Adapter<SliderAdapter.ViewHolder> {
    private Context context;
    private final ArrayList<SliderItem> sliderItems;


    public SliderAdapter(Context context, ArrayList<SliderItem> sliderItems) {
        this.context = context;
        this.sliderItems = sliderItems;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.slider_item_container, parent, false));
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final ImageView imageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(com.example.stores.R.id.imageSlide);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Glide.with(context).load(sliderItems.get(position).getUrl()).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return sliderItems.size();
    }
}
