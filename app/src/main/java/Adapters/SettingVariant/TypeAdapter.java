package Adapters.SettingVariant;

import static constants.keyName.TYPE_COLOR;
import static constants.keyName.TYPE_GENDER;
import static constants.keyName.TYPE_SIZE_GLOBAL;
import static constants.keyName.TYPE_SIZE_VN;
import static utils.TypeUtils.setupColor;
import static utils.TypeUtils.setupGender;
import static utils.TypeUtils.setupSizeGlobal;
import static utils.TypeUtils.setupSizeVn;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.stores.R;
import com.example.stores.databinding.DialogSelectColorBinding;
import com.example.stores.databinding.DialogSelectGenderBinding;
import com.example.stores.databinding.DialogSelectSizeGlobalBinding;
import com.example.stores.databinding.DialogSelectSizeVnBinding;
import com.example.stores.databinding.ItemTypeBinding;

import java.util.ArrayList;
import java.util.Objects;

import interfaces.TypeCallback;
import models.Type;
import models.TypeValue;


public class TypeAdapter extends RecyclerView.Adapter<TypeAdapter.ViewHolder> {
    private final Context context;
    private final ArrayList<Type> list;
    public static TypeCallback callback = null;
    private static final ArrayList<TypeValue> colorValues = setupColor();
    private static final ArrayList<TypeValue> genderValues = setupGender();
    private static final ArrayList<TypeValue> sizeVnValues = setupSizeVn();
    private static final ArrayList<TypeValue> sizeGlobalValues = setupSizeGlobal();


    public TypeAdapter(Context context, ArrayList<Type> list, TypeCallback callback) {
        this.context = context;
        this.list = list;
        TypeAdapter.callback = callback;
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
            if (type.getTypeName().equals(TYPE_SIZE_VN) || type.getTypeName().equals(TYPE_SIZE_GLOBAL)) {
                callback.setVisibleForSelectSizeLayout();
            }
            list.remove(removePos);
            callback.updateSelectableTypeSet(type.getTypeName());
            setVisibleForSelectVariantDialog();
            notifyItemRemoved(removePos);
        });


        TypeValueAdapterForSettingVariant typeValueAdapter = new TypeValueAdapterForSettingVariant(context, type.getListValues(), selectedType, callback);
        holder.binding.recyclerView.setLayoutManager(new LinearLayoutManager(context));
        holder.binding.recyclerView.setAdapter(typeValueAdapter);

        holder.binding.layoutAddTypeValue.setOnClickListener(v -> {
            switch (selectedType) {
                case TYPE_COLOR:
                    popUpSelectColorDialog(typeValueAdapter, context);
                    break;
                case TYPE_GENDER:
                    popUpSelectGenderDialog(typeValueAdapter, context);
                    break;
                case TYPE_SIZE_VN:
                    popUpSelectSizeVnDialog(typeValueAdapter, context);
                    break;
                case TYPE_SIZE_GLOBAL:
                    popUpSelectSizeGlobalDialog(typeValueAdapter, context);
                    break;
            }
        });

    }

    public void addType(Type type) {
        list.add(type);
        notifyItemInserted(list.size());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void setVisibleForSelectVariantDialog() {
        boolean popupable = getItemCount() < 2;
        callback.controlPopUpVariantDialog(popupable);
    }

    public static void popUpSelectColorDialog(TypeValueAdapterForSettingVariant adapter, Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        DialogSelectColorBinding binding = DialogSelectColorBinding.inflate((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE));
        builder.setView(binding.getRoot());
        AlertDialog dialog = builder.create();

        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawableResource(R.drawable.custom_edit_text_border);
        dialog.show();
        Window window = dialog.getWindow();
        if (window != null) {
            window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            window.setGravity(Gravity.BOTTOM);

            Animation slideUp = AnimationUtils.loadAnimation(context, R.anim.slide_up);
            binding.getRoot().startAnimation(slideUp);
        }

        dialog.setCancelable(true);


        TypeValueAdapterForDialog typeValueAdapter = new TypeValueAdapterForDialog(context, colorValues);
        binding.recyclerView.setLayoutManager(new GridLayoutManager(context, 3, GridLayoutManager.VERTICAL, false));
        binding.recyclerView.setAdapter(typeValueAdapter);


        ArrayList<TypeValue> removedList = new ArrayList<>();
        binding.btnSubmit.setOnClickListener(v -> {
            colorValues.forEach(item -> {
                if (item.isChecked()) {
                    callback.updateSelectedTypeValues(item);
                    adapter.addTypeValue(item);
                } else {
                    removedList.add(item);
                    callback.updateSelectedTypeValues(item);
                }
            });
            adapter.removeAll(removedList);
            dialog.dismiss();
        });

        binding.imageClose.setOnClickListener(v -> {
            dialog.dismiss();
        });

    }

    public static void popUpSelectGenderDialog(TypeValueAdapterForSettingVariant adapter, Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        DialogSelectGenderBinding binding = DialogSelectGenderBinding.inflate((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE));
        builder.setView(binding.getRoot());
        AlertDialog dialog = builder.create();

        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawableResource(R.drawable.custom_edit_text_border);
        dialog.show();
        Window window = dialog.getWindow();
        if (window != null) {
            window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            window.setGravity(Gravity.BOTTOM);

            Animation slideUp = AnimationUtils.loadAnimation(context, R.anim.slide_up);
            binding.getRoot().startAnimation(slideUp);
        }

        dialog.setCancelable(true);

        TypeValueAdapterForDialog categoryAdapter = new TypeValueAdapterForDialog(context, genderValues);
        binding.recyclerView.setLayoutManager(new GridLayoutManager(context, 3, GridLayoutManager.VERTICAL, false));
        binding.recyclerView.setAdapter(categoryAdapter);

        binding.btnSubmit.setOnClickListener(v -> {
            for (TypeValue item : genderValues) {
                if (item.isChecked()) {
                    callback.updateSelectedTypeValues(item);
                    adapter.addTypeValue(item);
                }
            }
            dialog.dismiss();
        });

        binding.imageClose.setOnClickListener(v -> {
            dialog.dismiss();
        });

    }

    public static void popUpSelectSizeVnDialog(TypeValueAdapterForSettingVariant adapter, Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        DialogSelectSizeVnBinding binding = DialogSelectSizeVnBinding.inflate((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE));
        builder.setView(binding.getRoot());
        AlertDialog dialog = builder.create();

        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawableResource(R.drawable.custom_edit_text_border);
        dialog.show();
        Window window = dialog.getWindow();
        if (window != null) {
            window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            window.setGravity(Gravity.BOTTOM);

            Animation slideUp = AnimationUtils.loadAnimation(context, R.anim.slide_up);
            binding.getRoot().startAnimation(slideUp);
        }

        dialog.setCancelable(true);

        TypeValueAdapterForDialog categoryAdapter = new TypeValueAdapterForDialog(context, sizeVnValues);
        binding.recyclerView.setLayoutManager(new GridLayoutManager(context, 3, GridLayoutManager.VERTICAL, false));
        binding.recyclerView.setAdapter(categoryAdapter);

        binding.btnSubmit.setOnClickListener(v -> {
            for (TypeValue item : sizeVnValues) {
                if (item.isChecked()) {
                    callback.updateSelectedTypeValues(item);
                    adapter.addTypeValue(item);
                }
            }
            dialog.dismiss();
        });

        binding.imageClose.setOnClickListener(v -> {
            dialog.dismiss();
        });

    }

    public static void popUpSelectSizeGlobalDialog(TypeValueAdapterForSettingVariant adapter, Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        DialogSelectSizeGlobalBinding binding = DialogSelectSizeGlobalBinding.inflate((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE));
        builder.setView(binding.getRoot());
        AlertDialog dialog = builder.create();

        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawableResource(R.drawable.custom_edit_text_border);
        dialog.show();
        Window window = dialog.getWindow();
        if (window != null) {
            window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            window.setGravity(Gravity.BOTTOM);

            Animation slideUp = AnimationUtils.loadAnimation(context, R.anim.slide_up);
            binding.getRoot().startAnimation(slideUp);
        }

        dialog.setCancelable(true);

        TypeValueAdapterForDialog categoryAdapter = new TypeValueAdapterForDialog(context, sizeGlobalValues);
        binding.recyclerView.setLayoutManager(new GridLayoutManager(context, 3, GridLayoutManager.VERTICAL, false));
        binding.recyclerView.setAdapter(categoryAdapter);

        binding.btnSubmit.setOnClickListener(v -> {
            for (TypeValue item : sizeGlobalValues) {
                if (item.isChecked()) {
                    callback.updateSelectedTypeValues(item);
                    adapter.addTypeValue(item);
                }
            }
            dialog.dismiss();
        });
        binding.imageClose.setOnClickListener(v -> {
            dialog.dismiss();
        });

    }


}
