package Adapters;
import static constants.keyName.TYPE_COLOR;
import static constants.keyName.TYPE_GENDER;
import static constants.keyName.TYPE_SIZE_GLOBAL;
import static constants.keyName.TYPE_SIZE_VN;
import static utils.TypeUtils.popUpSelectColorDialog;
import static utils.TypeUtils.popUpSelectGenderDialog;
import static utils.TypeUtils.popUpSelectSizeGlobalDialog;
import static utils.TypeUtils.popUpSelectSizeVnDialog;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.stores.databinding.ItemTypeBinding;
import java.util.ArrayList;
import models.Type;


public class TypeAdapter extends RecyclerView.Adapter<TypeAdapter.ViewHolder> {
    private final Context context;
    private final ArrayList<Type> list;


    public TypeAdapter(Context context, ArrayList<Type> list) {
        this.context = context;
        this.list = list;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemTypeBinding binding = ItemTypeBinding.inflate(LayoutInflater.from(context), parent, false);
        return new ViewHolder(binding);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        ItemTypeBinding binding;

        public ViewHolder(ItemTypeBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Type type = list.get(holder.getBindingAdapterPosition());
        String selectedType = type.getTypeName();
        holder.binding.txtTypeName.setText(type.getTypeName());
        holder.binding.txtRemove.setOnClickListener(v -> {
            int removePos = holder.getBindingAdapterPosition();
            list.remove(removePos);
            notifyItemRemoved(removePos);
        });

        TypeValueAdapterForSettingVariant adapter = new TypeValueAdapterForSettingVariant(context, type.getListValues(), selectedType);
        holder.binding.recyclerView.setLayoutManager(new LinearLayoutManager(context));
        holder.binding.recyclerView.setAdapter(adapter);

        holder.binding.layoutAddTypeValue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedType.equals(TYPE_COLOR)){
                    popUpSelectColorDialog(context);
                }else if (selectedType.equals(TYPE_GENDER)){
                    popUpSelectGenderDialog(context);
                }
                else if (selectedType.equals(TYPE_SIZE_VN)){
                    popUpSelectSizeVnDialog(context);
                }else if (selectedType.equals(TYPE_SIZE_GLOBAL)){
                    popUpSelectSizeGlobalDialog(context);
                }
            }
        });

    }


    @Override
    public int getItemCount() {
        return list.size();
    }
}
