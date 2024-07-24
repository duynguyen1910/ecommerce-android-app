package Adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.stores.R;
import com.example.stores.databinding.ItemCategoryGridForDialogBinding;

import java.util.ArrayList;

import interfaces.CategoryDialogListener;
import models.Category;

public class CategoryGridForDialogAdapter extends RecyclerView.Adapter<CategoryGridForDialogAdapter.ViewHolder> {
    private final Context context;
    private final ArrayList<Category> list;
    private final CategoryDialogListener listener;
    private int selectedPosition = -1;


    public CategoryGridForDialogAdapter(Context context, ArrayList<Category> list, CategoryDialogListener listener, int selectedPosition) {
        this.context = context;
        this.list = list;
        this.listener = listener;
        this.selectedPosition = selectedPosition;
    }

    @NonNull
    @Override
    public CategoryGridForDialogAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemCategoryGridForDialogBinding binding = ItemCategoryGridForDialogBinding.inflate(LayoutInflater.from(context), parent, false);
        return new ViewHolder(binding);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        ItemCategoryGridForDialogBinding binding;
        public ViewHolder(ItemCategoryGridForDialogBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryGridForDialogAdapter.ViewHolder holder, int position) {
        Category category = list.get(holder.getBindingAdapterPosition());
        holder.binding.txtCategoryName.setText(category.getCategoryName());


        if (selectedPosition == holder.getBindingAdapterPosition()) {
            holder.itemView.setBackgroundResource(R.drawable.custom_border_primary_color);
        } else {
            holder.itemView.setBackgroundColor(Color.parseColor("#EBFAF8"));  // hoặc background mặc định của bạn
        }
        holder.itemView.setOnClickListener(v -> {
            int previousSelectedPosition = selectedPosition;
            selectedPosition = holder.getBindingAdapterPosition();

            // Cập nhật background cho item trước đó và item hiện tại
            notifyItemChanged(previousSelectedPosition);
            notifyItemChanged(selectedPosition);
            listener.transferCategory(selectedPosition, category.getBaseID(), category.getCategoryName());
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
