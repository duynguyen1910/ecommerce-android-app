package Adapters;

import static constants.keyName.CATEGORY_ID;
import static constants.keyName.CATEGORY_NAME;
import static constants.keyName.STORE_ID;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.stores.databinding.ItemCategoryBinding;

import java.util.ArrayList;

import Activities.SearchActivity;
import models.Category;

public class CategoryAdapterForSearch extends RecyclerView.Adapter<CategoryAdapterForSearch.ViewHolder> {
    private final Context context;
    private final ArrayList<Category> list;
    private String storeId;

    public CategoryAdapterForSearch(Context context, ArrayList<Category> list, String storeId) {
        this.context = context;
        this.list = list;
        this.storeId =storeId;
    }

    @NonNull
    @Override
    public CategoryAdapterForSearch.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemCategoryBinding binding = ItemCategoryBinding.inflate(LayoutInflater.from(context), parent, false);
        return new ViewHolder(binding);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        ItemCategoryBinding binding;
        public ViewHolder(ItemCategoryBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryAdapterForSearch.ViewHolder holder, int position) {
        Category category = list.get(holder.getBindingAdapterPosition());
        holder.binding.txtCategoryName.setText(category.getCategoryName());
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, SearchActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString(CATEGORY_NAME, category.getCategoryName());
            bundle.putString(CATEGORY_ID, category.getBaseID());
            bundle.putString(STORE_ID, storeId);

            intent.putExtras(bundle);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
