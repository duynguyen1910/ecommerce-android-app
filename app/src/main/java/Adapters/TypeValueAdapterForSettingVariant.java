package Adapters;
import static utils.CartUtils.showToast;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.stores.databinding.ItemTypeValueBinding;
import java.util.ArrayList;

import interfaces.TypeCallback;
import models.TypeValue;

public class TypeValueAdapterForSettingVariant extends RecyclerView.Adapter<TypeValueAdapterForSettingVariant.ViewHolder> {
    private final Context context;
    private final ArrayList<TypeValue> list;
    TypeCallback callback;
    private String selectedType; // Color, Size, Gender

    public TypeValueAdapterForSettingVariant(Context context, ArrayList<TypeValue> list, String selectedType, TypeCallback callback) {
        this.context = context;
        this.list = list;
        this.selectedType = selectedType;
        this.callback = callback;
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

        holder.binding.imvRemove.setOnClickListener(v -> {
            typeValue.setChecked(false);
            int removePos = holder.getBindingAdapterPosition();
            list.remove(removePos);
            notifyItemRemoved(removePos);
            callback.updateSelectedTypeValues(typeValue);
        });

        holder.binding.imvEdit.setOnClickListener(v -> {

        });


    }
    public void addTypeValue(TypeValue typeValue){
        if (!list.contains(typeValue)){
            list.add(typeValue);
        }

        notifyItemInserted(list.size());
    }
    public void removeAll(ArrayList<TypeValue> removedList){
        list.removeAll(removedList);
        notifyDataSetChanged();
    }
    @Override
    public int getItemCount() {
        return list.size();
    }
}
