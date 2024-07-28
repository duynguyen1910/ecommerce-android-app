package Activities.ProductSetup;

import static constants.keyName.TYPES;
import static utils.CartUtils.showToast;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.stores.databinding.ActivitySettingVariantDetailBinding;

import java.util.ArrayList;
import java.util.Objects;

import Adapters.SettingVariant.VariantSettingAdapter;
import models.Type;
import models.Variant;

public class SettingVariantDetailActivity extends AppCompatActivity {

    ActivitySettingVariantDetailBinding binding;
    ArrayList<Type> types;
    ArrayList<Variant> variants = new ArrayList<>();
    VariantSettingAdapter variantAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySettingVariantDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initUI();
        getBundle();
        setupVariants();
        setupEvents();


    }

    private void getBundle() {
        Intent intent = getIntent();
        if (intent != null) {
            types = (ArrayList<Type>) intent.getSerializableExtra(TYPES);
            if (types != null) {
                showToast(SettingVariantDetailActivity.this, "typesSize: " + types.size());
            }
        }
    }

    private void setupVariants() {
        types.get(0).getListValues().forEach(item0 -> {
            types.get(1).getListValues().forEach(item1 -> {
                String variantName = item0.getValue() + " | " + item1.getValue();
                Variant newVariant = new Variant(variantName, 0, 0, 0, "");
                variants.add(newVariant);
            });
        });


        variantAdapter = new VariantSettingAdapter(this, variants);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerView.setAdapter(variantAdapter);
    }


    private void setupEvents() {
        binding.imageBack.setOnClickListener(v -> finish());
        binding.btnSave.setOnClickListener(v -> {
            ArrayList<Variant> result = variantAdapter.getVariantsList();
            StringBuilder listInstock = new StringBuilder();
            for (Variant variant : result){
                listInstock.append(" ").append(variant.getInStock()).append(" ");
                showToast(SettingVariantDetailActivity.this, "listInstock: " + listInstock);
            }
            // call Variant API

        });
    }

    private void initUI() {
        getWindow().setStatusBarColor(Color.parseColor("#F04D7F"));
        Objects.requireNonNull(getSupportActionBar()).hide();
    }


}