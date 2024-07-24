package Adapters;
import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.stores.databinding.ItemTypeValueBinding;
import java.util.ArrayList;
import models.TypeValue;

public class TypeValueAdapter extends RecyclerView.Adapter<TypeValueAdapter.ViewHolder> {
    private final Context context;
    private final ArrayList<TypeValue> list;

    public TypeValueAdapter(Context context, ArrayList<TypeValue> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemTypeValueBinding binding = ItemTypeValueBinding.inflate(LayoutInflater.from(context), parent, false);
        return new ViewHolder(binding);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        ItemTypeValueBinding binding;

        public ViewHolder(ItemTypeValueBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }


    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        TypeValue typeValue = list.get(holder.getBindingAdapterPosition());
        holder.binding.txtValue.setText(typeValue.getValue());
        if (typeValue.getImage() != null){
            holder.binding.image.setVisibility(View.VISIBLE);
            Glide.with(context).load(typeValue.getImage()).centerCrop().into(holder.binding.image);
        }else {
            holder.binding.image.setVisibility(View.GONE);
        }

        holder.binding.imvRemove.setOnClickListener(v -> {
            int removePos = holder.getBindingAdapterPosition();
            list.remove(removePos);
            notifyItemRemoved(removePos);
        });

        holder.binding.imvEdit.setOnClickListener(v -> {

        });


    }
    @Override
    public int getItemCount() {
        return list.size();
    }
}
