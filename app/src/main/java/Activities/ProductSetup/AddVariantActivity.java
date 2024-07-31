package Activities.ProductSetup;
import static constants.keyName.TYPES;
import static constants.keyName.TYPE_COLOR;
import static constants.keyName.TYPE_GENDER;
import static constants.keyName.TYPE_SIZE_GLOBAL;
import static constants.keyName.TYPE_SIZE_VN;
import static utils.Cart.CartUtils.showToast;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.stores.R;
import com.example.stores.databinding.ActivityAddVariantBinding;
import com.example.stores.databinding.DialogAddVariantBinding;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Objects;
import Adapters.SettingVariant.TypeAdapter;
import interfaces.TypeCallback;
import models.Type;
import models.TypeValue;

public class AddVariantActivity extends AppCompatActivity {

    private ActivityAddVariantBinding binding;
    private final ArrayList<Type> types = new ArrayList<>();
    private TypeAdapter adapter;
    private  HashSet<String> selectableSet = new HashSet<>();
    private final HashSet<String> selectedTypes = new HashSet<>();
    private final HashSet<String> selectedTypeValuesSet = new HashSet<>();
    private boolean isSizeTypeSelected = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddVariantBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initUI();
        setupUI();
        setupEvents();


    }

    private void setupEvents() {
        binding.imageBack.setOnClickListener(v -> finish());
        binding.layoutAddVariant.setOnClickListener(v -> {
           popUpSelectVariantDialog();
        });
        binding.btnSettingVariant.setOnClickListener(v -> {
            if (countSelectedTypeValues() > 0){
                Intent intent = new Intent(AddVariantActivity.this, SettingVariantDetailActivity.class);
                intent.putExtra(TYPES, types);
                startActivity(intent);
            }else {
               showToast(AddVariantActivity.this, "Vui lòng chọn phân loại sản phẩm!");
            }
        });
    }

    private int countSelectedTypeValues(){
        int count = 0;
        for (Type type : types){
            count += type.getListValues().size();
        }
        return count;
    }

    private void setVisibleForLayoutSelectSize(View view){
        if (isSizeTypeSelected){
            view.setVisibility(View.GONE);
        }else {
            view.setVisibility(View.VISIBLE);
        }
    }


    private void setupUI(){
        adapter = new TypeAdapter(AddVariantActivity.this, types, new TypeCallback() {
            @Override
            public void updateSelectedTypeValues(TypeValue typeValue) {
                if (typeValue.isChecked()){
                    if (selectedTypeValuesSet.contains(typeValue.getValue())){
                        selectedTypeValuesSet.add(typeValue.getValue());
                    }
                }else {
                    selectedTypeValuesSet.remove(typeValue.getValue());
                }
            }

            @Override
            public void controlPopUpVariantDialog(boolean popupable) {
                if (popupable) {
                    binding.layoutAddVariant.setVisibility(View.VISIBLE);
                } else {
                    binding.layoutAddVariant.setVisibility(View.GONE);
                }
            }

            @Override
            public void updateSelectableTypeSet(String typeName) {
                selectedTypes.remove(typeName);
                selectableSet.add(typeName);
            }

            @Override
            public void setVisibleForSelectSizeLayout() {
                isSizeTypeSelected = false;
            }

        });
        binding.recyclerView.setAdapter(adapter);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(AddVariantActivity.this, LinearLayoutManager.VERTICAL, false));
    }



    private void popUpSelectVariantDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        DialogAddVariantBinding dialogBinding = DialogAddVariantBinding.inflate(getLayoutInflater());
        builder.setView(dialogBinding.getRoot());
        AlertDialog dialog = builder.create();

        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawableResource(R.drawable.custom_edit_text_border);
        dialog.show();

        Window window = dialog.getWindow();
        if (window != null) {
            window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            window.setGravity(Gravity.BOTTOM);

            Animation slideUp = AnimationUtils.loadAnimation(this, R.anim.slide_up);
            dialogBinding.getRoot().startAnimation(slideUp);
        }

        // Hiển thị các View dựa trên selectableSet
        if (selectableSet.contains(TYPE_COLOR)) {
            dialogBinding.txtColor.setVisibility(View.VISIBLE);
        }
        if (selectableSet.contains(TYPE_SIZE_VN)) {
            dialogBinding.rdoSizeVn.setVisibility(View.VISIBLE);
        }
        if (selectableSet.contains(TYPE_SIZE_GLOBAL)) {
            dialogBinding.rdoSizeGlobal.setVisibility(View.VISIBLE);
        }
        if (selectableSet.contains(TYPE_GENDER)) {
            dialogBinding.txtGender.setVisibility(View.VISIBLE);
        }

        // Cập nhật trạng thái của layout size
        setVisibleForLayoutSelectSize(dialogBinding.layoutSelectSize);

        dialogBinding.imageClose.setOnClickListener(v -> dialog.dismiss());

        dialogBinding.txtColor.setOnClickListener(v -> {
            Type type = new Type(TYPE_COLOR, new ArrayList<>());
            if (!selectedTypes.contains(type.getTypeName())) {
                adapter.addType(type);
            }
            selectedTypes.add(type.getTypeName());
            selectableSet.remove(type.getTypeName());
            dialog.dismiss();
            adapter.setVisibleForSelectVariantDialog();
        });

        dialogBinding.rdoSizeVn.setOnClickListener(v -> {
            Type type = new Type(TYPE_SIZE_VN, new ArrayList<>());
            if (!selectedTypes.contains(type.getTypeName())) {
                adapter.addType(type);
            }
            selectedTypes.add(TYPE_SIZE_VN);
            isSizeTypeSelected = true;
            selectableSet.remove(TYPE_SIZE_VN);
            dialog.dismiss();
            adapter.setVisibleForSelectVariantDialog();
        });

        dialogBinding.rdoSizeGlobal.setOnClickListener(v -> {
            Type type = new Type(TYPE_SIZE_GLOBAL, new ArrayList<>());
            if (!selectedTypes.contains(type.getTypeName())) {
                adapter.addType(type);
            }
            selectedTypes.add(TYPE_SIZE_GLOBAL);
            isSizeTypeSelected = true;
            selectableSet.remove(TYPE_SIZE_GLOBAL);
            dialog.dismiss();
            adapter.setVisibleForSelectVariantDialog();
        });

        dialogBinding.txtGender.setOnClickListener(v -> {
            Type type = new Type(TYPE_GENDER, new ArrayList<>());
            if (!selectedTypes.contains(type.getTypeName())) {
                adapter.addType(type);
            }
            selectedTypes.add(type.getTypeName());
            selectableSet.remove(type.getTypeName());
            dialog.dismiss();
            adapter.setVisibleForSelectVariantDialog();
        });

        dialogBinding.txtCustomVariant.setOnClickListener(v -> dialog.dismiss());
    }


    private void initUI() {
        getWindow().setStatusBarColor(Color.parseColor("#F04D7F"));
        Objects.requireNonNull(getSupportActionBar()).hide();
        selectableSet = new HashSet<>();
        selectableSet.add(TYPE_COLOR);
        selectableSet.add(TYPE_GENDER);
        selectableSet.add(TYPE_SIZE_VN);
        selectableSet.add(TYPE_SIZE_GLOBAL);
    }




}