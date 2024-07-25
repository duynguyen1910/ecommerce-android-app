package utils;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.GridLayoutManager;

import com.example.stores.R;
import com.example.stores.databinding.DialogSelectColorBinding;
import com.example.stores.databinding.DialogSelectGenderBinding;
import com.example.stores.databinding.DialogSelectSizeGlobalBinding;
import com.example.stores.databinding.DialogSelectSizeVnBinding;

import java.util.ArrayList;
import java.util.Objects;

import Adapters.TypeValueAdapterForDialog;
import models.TypeValue;

public class TypeUtils {


    public static void popUpSelectColorDialog(Context context) {
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

        TypeValueAdapterForDialog categoryAdapter = new TypeValueAdapterForDialog(context, setupColor());
        binding.recyclerView.setLayoutManager(new GridLayoutManager(context, 3, GridLayoutManager.VERTICAL, false));
        binding.recyclerView.setAdapter(categoryAdapter);

        binding.btnSubmit.setOnClickListener(v -> {
            dialog.dismiss();
        });

        binding.imageClose.setOnClickListener(v -> {
            dialog.dismiss();
        });

    }
    public static void popUpSelectGenderDialog(Context context) {
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

        TypeValueAdapterForDialog categoryAdapter = new TypeValueAdapterForDialog(context, setupGender());
        binding.recyclerView.setLayoutManager(new GridLayoutManager(context, 3, GridLayoutManager.VERTICAL, false));
        binding.recyclerView.setAdapter(categoryAdapter);

        binding.btnSubmit.setOnClickListener(v -> {
            dialog.dismiss();
        });

        binding.imageClose.setOnClickListener(v -> {
            dialog.dismiss();
        });

    }
    public static void popUpSelectSizeVnDialog(Context context) {
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

        TypeValueAdapterForDialog categoryAdapter = new TypeValueAdapterForDialog(context, setupSizeVn());
        binding.recyclerView.setLayoutManager(new GridLayoutManager(context, 3, GridLayoutManager.VERTICAL, false));
        binding.recyclerView.setAdapter(categoryAdapter);

        binding.btnSubmit.setOnClickListener(v -> {
            dialog.dismiss();
        });

        binding.imageClose.setOnClickListener(v -> {
            dialog.dismiss();
        });

    }
    public static void popUpSelectSizeGlobalDialog(Context context) {
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

        TypeValueAdapterForDialog categoryAdapter = new TypeValueAdapterForDialog(context, setupSizeGlobal());
        binding.recyclerView.setLayoutManager(new GridLayoutManager(context, 3, GridLayoutManager.VERTICAL, false));
        binding.recyclerView.setAdapter(categoryAdapter);

        binding.btnSubmit.setOnClickListener(v -> {
            dialog.dismiss();
        });

        binding.imageClose.setOnClickListener(v -> {
            dialog.dismiss();
        });

    }

    public static ArrayList<TypeValue> setupColor() {
        String[] colorArray = {
                "Đỏ", "Cam", "Vàng", "Xanh lá", "Xanh dương",
                "Tím", "Hồng", "Tím nhạt", "Trắng", "Bạc", "Nâu",
                "Màu kaki", "Đen", "Vàng gold", "Xám tro", "Be",
                "Hồng dâu", "Nâu cafe", "Vàng đồng", "Nâu đậm",
                "Xanh lục bảo", "Xám", "Nâu nhạt", "Đen nhám", "Xanh rêu",
                "Tím than", "Trắng kem", "Xanh ngọc bích", "Xanh lá đậm",
                "Bạc ánh kim", "Xám lông chuột", "Bạch kim", "Xám khói"
        };
        ArrayList<TypeValue> colorValues = new ArrayList<>();
        for (String color : colorArray) {
            TypeValue typeValue = new TypeValue(color);
            colorValues.add(typeValue);

        }
        return colorValues;
    }
    public static ArrayList<TypeValue> setupSizeVn() {
        String[] sizeVNArray = {
                "34", "35", "36", "37", "38", "39", "40", "41",
                "42", "43", "44", "45", "46", "47", "48", "49"
        };
        ArrayList<TypeValue> sizeVNValues = new ArrayList<>();
        for (String color : sizeVNArray) {
            TypeValue typeValue = new TypeValue(color);
            sizeVNValues.add(typeValue);

        }
        return sizeVNValues;
    }
    public static ArrayList<TypeValue> setupSizeGlobal() {
        String[] sizeGlobalArray = {
                "Free Size", "L", "M", "S", "XL", "XS", "XXL", "XXS"
        };
        ArrayList<TypeValue> sizeGlobalValues = new ArrayList<>();
        for (String color : sizeGlobalArray) {
            TypeValue typeValue = new TypeValue(color);
            sizeGlobalValues.add(typeValue);
        }
        return sizeGlobalValues;
    }
    public static ArrayList<TypeValue> setupGender() {
        String[] genderArray = {"Bé trai", "Bé gái", "Nam", "Nữ", "Unisex"};

        ArrayList<TypeValue> genderValues = new ArrayList<>();
        for (String color : genderArray) {
            TypeValue typeValue = new TypeValue(color);
            genderValues.add(typeValue);

        }
        return genderValues;
    }
}
