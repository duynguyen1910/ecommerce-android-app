package Activities;
import static constants.keyName.TYPE_COLOR;
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
import java.util.Arrays;
import java.util.HashMap;
import java.util.Objects;

import Adapters.MyProductsAdapter;
import Adapters.TypeAdapter;
import models.Type;
import models.TypeValue;

public class AddVariantActivity extends AppCompatActivity {

    ActivityAddVariantBinding binding;
    ArrayList<Type> types;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddVariantBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initUI();
        fakeTypeData();
        setupUI();
        setupEvents();


    }

    private void setupEvents() {
        binding.imageBack.setOnClickListener(v -> finish());
        binding.layoutAddVariant.setOnClickListener(v -> {
            popUpChooseVariantDialog();
        });
    }

    private void setupUI(){
        TypeAdapter adapter = new TypeAdapter(AddVariantActivity.this, types);
        binding.recyclerView.setAdapter(adapter);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(AddVariantActivity.this, LinearLayoutManager.VERTICAL, false));
    }

    private void fakeTypeData(){
        types = new ArrayList<>();
        ArrayList<TypeValue> listColorValues = new ArrayList<>();
        TypeValue color1 = new TypeValue("https://down-vn.img.susercontent.com/file/vn-11134207-7r98o-lrwxcikwgrt07d", "Xanh lá đậm");
//        TypeValue color2 = new TypeValue("https://down-vn.img.susercontent.com/file/vn-11134207-7r98o-lrwxcikww81w79", "Xám tro");
//        TypeValue color3 = new TypeValue("https://down-vn.img.susercontent.com/file/vn-11134207-7r98o-lrwxcikxbo9l47", "Trắng kem");
//        TypeValue color4 = new TypeValue("https://down-vn.img.susercontent.com/file/vn-11134207-7r98o-lrwxcikww80pe0", "Hồng nhạt");

        listColorValues.add(color1);
//        listColorValues.add(color2);
//        listColorValues.add(color3);
//        listColorValues.add(color4);
        Type color = new Type(TYPE_COLOR, listColorValues);

        ArrayList<TypeValue> listSizeGlobalValues = new ArrayList<>();
        TypeValue sizeL = new TypeValue("L");
        TypeValue sizeM = new TypeValue("M");
//        TypeValue sizeS = new TypeValue("S");
//        TypeValue sizeXL = new TypeValue("XL");
//        TypeValue sizeXXL = new TypeValue("XXL");
        listSizeGlobalValues.add(sizeL);
        listSizeGlobalValues.add(sizeM);
//        listSizeGlobalValues.add(sizeS);
//        listSizeGlobalValues.add(sizeXL);
//        listSizeGlobalValues.add(sizeXXL);


        Type sizeGlobal = new Type(TYPE_SIZE_GLOBAL, listSizeGlobalValues);


        ArrayList<TypeValue> listSizeVnValues = new ArrayList<>();
        TypeValue size40 = new TypeValue("40");
        TypeValue size42 = new TypeValue("42");
        listSizeVnValues.add(size40);
        listSizeVnValues.add(size42);

        Type sizeVn = new Type(TYPE_SIZE_VN, listSizeVnValues);


        ArrayList<TypeValue> listGenderValues = new ArrayList<>();
        TypeValue gender1 = new TypeValue("Bé trai");
        TypeValue gender2 = new TypeValue("Bé gái");
//        TypeValue gender3 = new TypeValue("Nam");
//        TypeValue gender4 = new TypeValue("Nữ");
//        TypeValue gender5 = new TypeValue("Unisex");

        listGenderValues.add(gender1);
        listGenderValues.add(gender2);
//        listGenderValues.add(gender3);
//        listGenderValues.add(gender4);
//        listGenderValues.add(gender5);

        Type gender = new Type("Giới tính", listGenderValues);


        types.add(color);
        types.add(sizeGlobal);
        types.add(sizeVn);
        types.add(gender);

    }

    private void popUpChooseVariantDialog() {
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

        dialogBinding.imageClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialogBinding.txtSizeVN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialogBinding.txtSizeGlobal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialogBinding.txtColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialogBinding.txtGender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialogBinding.txtCustomVariant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });


    }

    private void initUI() {
        getWindow().setStatusBarColor(Color.parseColor("#F04D7F"));
        Objects.requireNonNull(getSupportActionBar()).hide();
    }




}