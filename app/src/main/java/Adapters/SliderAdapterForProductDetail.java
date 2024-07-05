package Adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.stores.R;

import java.util.ArrayList;

import Activities.ProductDetailActivity;
import Activities.ProductImagesDetailActivity;
import Models.SliderItem;

public class SliderAdapterForProductDetail extends RecyclerView.Adapter<SliderAdapterForProductDetail.ViewHolder> {
    private Context context;
    private final ArrayList<SliderItem> sliderItems;


    public SliderAdapterForProductDetail(Context context, ArrayList<SliderItem> sliderItems) {
        this.context = context;
        this.sliderItems = sliderItems;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_slider, parent, false));
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final ImageView imageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageSlide);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Glide.with(context).load(sliderItems.get(position).getUrl()).into(holder.imageView);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentPosition = holder.getBindingAdapterPosition();
                Intent intent = new Intent(context, ProductImagesDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("currentPosition", currentPosition);
                bundle.putSerializable("sliderItems", sliderItems);
                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return sliderItems.size();
    }
}
