package Adapters.SettingVariant;

import static constants.keyName.TYPE_COLOR;
import static constants.toastMessage.IMAGE_REQUIRE;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.stores.R;
import com.example.stores.databinding.ItemTypeValueBinding;

import java.util.ArrayList;

import Activities.ProfileSetup.UpdateProfileActivity;
import interfaces.TypeCallback;
import models.TypeValue;

public class TypeValueAdapterForSettingVariant extends 
        RecyclerView.Adapter<TypeValueAdapterForSettingVariant.ViewHolder> {
    private final Context context;
    private ArrayList<TypeValue> typeValuesList;
    private TypeCallback callback;
    private ActivityResultLauncher<Intent> myLauncher;
    private String selectedType; // Color, Size, Gender


    public TypeValueAdapterForSettingVariant(Context context, ArrayList<TypeValue> typeValuesList,
            String selectedType, ActivityResultLauncher<Intent> myLauncher, TypeCallback callback) {
        this.context = context;
        this.typeValuesList = typeValuesList;
        this.selectedType = selectedType;
        this.myLauncher = myLauncher;
        this.callback = callback;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemTypeValueBinding binding = ItemTypeValueBinding.inflate(LayoutInflater.from(context),
                parent, false);
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
        TypeValue typeValue = typeValuesList.get(holder.getBindingAdapterPosition());
        holder.binding.txtValue.setText(typeValue.getValue());

        if(selectedType.equals(TYPE_COLOR)) holder.binding.variantImage.setVisibility(View.VISIBLE);

        if (typeValue.getImage() == null || typeValue.getImage().isEmpty()) {
            holder.binding.variantImage.setImageResource(R.drawable.ic_upload_24);
            holder.binding.variantImage.setColorFilter(ContextCompat.getColor(context, R.color.primary_color));
        } else {
            holder.binding.variantImage.clearColorFilter();

            Glide.with(context)
                    .load(Uri.parse(typeValue.getImage()))
                    .centerCrop()
                    .into(holder.binding.variantImage);
        }

        holder.binding.variantImage.setOnClickListener(v -> {
            if (callback != null) {
                callback.onImageClick(holder.getBindingAdapterPosition());
            }
        });

        holder.binding.imvRemove.setOnClickListener(v -> {
            typeValue.setChecked(false);
            int removePos = holder.getBindingAdapterPosition();
            typeValuesList.remove(removePos);
            notifyItemRemoved(removePos);
            callback.updateSelectedTypeValues(typeValue);
        });

        holder.binding.imvEdit.setOnClickListener(v -> {

        });


    }

    public void addTypeValue(TypeValue typeValue) {
        if (!typeValuesList.contains(typeValue)) {
            typeValuesList.add(typeValue);
        }

        notifyItemInserted(typeValuesList.size());
    }

    public void removeAll(ArrayList<TypeValue> removedList) {
        typeValuesList.removeAll(removedList);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return typeValuesList.size();
    }
}
