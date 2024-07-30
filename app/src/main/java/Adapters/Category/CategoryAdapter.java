package Adapters.Category;
import static constants.keyName.CATEGORY_ID;
import static constants.keyName.CATEGORY_NAME;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.stores.databinding.ItemCategoryBinding;
import java.util.ArrayList;
import java.util.Map;

import api.categoryApi;
import interfaces.GetDocumentCallback;
import models.Category;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {
    private final Context context;
    private final ArrayList<Category> list;

    public CategoryAdapter(Context context, ArrayList<Category> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public CategoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
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
    public void onBindViewHolder(@NonNull CategoryAdapter.ViewHolder holder, int position) {
        Category category = list.get(holder.getBindingAdapterPosition());
        holder.binding.txtCategoryName.setText(category.getCategoryName());
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent();
            Bundle bundle = new Bundle();
            bundle.putString(CATEGORY_NAME, category.getCategoryName());
            bundle.putString(CATEGORY_ID, category.getBaseID());

            intent.putExtras(bundle);
            ((Activity) context).setResult(1, intent);
            ((Activity) context).finish();
        });
    }



    @Override
    public int getItemCount() {
        return list.size();
    }
}