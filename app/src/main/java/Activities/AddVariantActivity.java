package Activities;
import static constants.keyName.TYPE_COLOR;
import static constants.keyName.TYPE_GENDER;
import static constants.keyName.TYPE_SIZE_GLOBAL;
import static constants.keyName.TYPE_SIZE_VN;
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
import Adapters.TypeAdapter;
import interfaces.TypeCallback;
import models.Type;
import models.TypeValue;

public class AddVariantActivity extends AppCompatActivity {

    ActivityAddVariantBinding binding;
    ArrayList<Type> types = new ArrayList<>();
    TypeAdapter adapter;
    HashSet<String> fourTypes = new HashSet<>();
    HashSet<String> selectedTypes = new HashSet<>();


    private final HashSet<String> selectedTypeValuesSet = new HashSet<>();

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

        dialogBinding.imageClose.setOnClickListener(v -> dialog.dismiss());
        dialogBinding.txtSizeVN.setOnClickListener(v -> {
            Type type = new Type(TYPE_SIZE_VN, new ArrayList<>());
            if (!selectedTypes.contains(type.getTypeName())){
                adapter.addType(type);
            }
            selectedTypes.add(type.getTypeName());

            dialog.dismiss();
            adapter.updateVisibility();

        });
        dialogBinding.txtSizeGlobal.setOnClickListener(v -> {
            Type type = new Type(TYPE_SIZE_GLOBAL, new ArrayList<>());
            if (!selectedTypes.contains(type.getTypeName())){
                adapter.addType(type);
            }
            selectedTypes.add(type.getTypeName());
            dialog.dismiss();
            adapter.updateVisibility();

        });
        dialogBinding.txtColor.setOnClickListener(v -> {
            Type type = new Type(TYPE_COLOR, new ArrayList<>());
            if (!selectedTypes.contains(type.getTypeName())){
                adapter.addType(type);
            }
            selectedTypes.add(type.getTypeName());
            dialog.dismiss();

            adapter.updateVisibility();
        });
        dialogBinding.txtGender.setOnClickListener(v -> {
            Type type = new Type(TYPE_GENDER, new ArrayList<>());
            if (!selectedTypes.contains(type.getTypeName())){
                adapter.addType(type);
            }
            selectedTypes.add(type.getTypeName());
            dialog.dismiss();
            adapter.updateVisibility();

        });
        dialogBinding.txtCustomVariant.setOnClickListener(v -> dialog.dismiss());


    }

    private void initUI() {
        getWindow().setStatusBarColor(Color.parseColor("#F04D7F"));
        Objects.requireNonNull(getSupportActionBar()).hide();
        HashSet<String> fourTypes = new HashSet<>();
        fourTypes.add(TYPE_COLOR);
        fourTypes.add(TYPE_GENDER);
        fourTypes.add(TYPE_SIZE_VN);
        fourTypes.add(TYPE_SIZE_GLOBAL);

        HashSet<String> selectedTypes = new HashSet<>();


    }




}