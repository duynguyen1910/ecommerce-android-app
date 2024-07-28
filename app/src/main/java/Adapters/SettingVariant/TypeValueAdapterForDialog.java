package Adapters.SettingVariant;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.stores.databinding.ItemTypeValueForDialogBinding;

import java.util.ArrayList;
import models.TypeValue;
import utils.DecorateUtils;

public class TypeValueAdapterForDialog extends RecyclerView.Adapter<TypeValueAdapterForDialog.ViewHolder> {
    private final Context context;
    private final ArrayList<TypeValue> list;

    public TypeValueAdapterForDialog(Context context, ArrayList<TypeValue> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public TypeValueAdapterForDialog.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemTypeValueForDialogBinding binding = ItemTypeValueForDialogBinding.inflate(LayoutInflater.from(context), parent, false);
        return new ViewHolder(binding);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        ItemTypeValueForDialogBinding binding;
        public ViewHolder(ItemTypeValueForDialogBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull TypeValueAdapterForDialog.ViewHolder holder, int position) {
        TypeValue typeValue = list.get(holder.getBindingAdapterPosition());

        holder.binding.chkValue.setText(typeValue.getValue());

        if (typeValue.isChecked()){
            DecorateUtils.decorateSelectedCompoundButton(context, holder.binding.chkValue);
            holder.binding.chkValue.setChecked(true);
        }else {
            DecorateUtils.decorateUnselectedCompoundButton(context, holder.binding.chkValue);
        }

        holder.binding.chkValue.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked){
                DecorateUtils.decorateSelectedCompoundButton(context, holder.binding.chkValue);
                typeValue.setChecked(true);
            }else {
                DecorateUtils.decorateUnselectedCompoundButton(context, holder.binding.chkValue);
                typeValue.setChecked(false);
            }

        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
