package Adapters;

import static constants.keyName.CATEGORY_ID;
import static constants.keyName.CATEGORY_NAME;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.stores.databinding.ItemCategoryGridBinding;

import java.util.ArrayList;

import Activities.SearchActivity;
import models.Category;

public class CategoryGridAdapter extends RecyclerView.Adapter<CategoryGridAdapter.ViewHolder> {
    private final Context context;
    private final ArrayList<Category> list;



    public CategoryGridAdapter(Context context, ArrayList<Category> list) {
        this.context = context;
        this.list = list;

    }

    @NonNull
    @Override
    public CategoryGridAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemCategoryGridBinding binding = ItemCategoryGridBinding.inflate(LayoutInflater.from(context), parent, false);
        return new ViewHolder(binding);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        ItemCategoryGridBinding binding;
        public ViewHolder(ItemCategoryGridBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryGridAdapter.ViewHolder holder, int position) {
        Category category = list.get(holder.getBindingAdapterPosition());
        holder.binding.txtCategoryName.setText(category.getCategoryName());
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, SearchActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString(CATEGORY_NAME, category.getCategoryName());
            bundle.putString(CATEGORY_ID, category.getBaseID());
            bundle.putInt("selectedPosition", position );
            intent.putExtras(bundle);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
