package Activities;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.stores.R;
import com.example.stores.databinding.ActivitySelectDeliveryMethodBinding;
import com.example.stores.databinding.ItemTabLayout2Binding;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.Objects;

import Adapters.ViewPager2Adapter;
import Fragments.SettingDeliveryFragment;
import Fragments.StoreIdentifierInfoFragment;
import Fragments.StoreInfoFragment;
import Fragments.TaxInfoFragment;
import kotlin.Pair;

public class SelectDeliveryMethodActivity extends AppCompatActivity {

    ActivitySelectDeliveryMethodBinding binding;
    int currentState = 1;
    int steps = 4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySelectDeliveryMethodBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initUI();
        setupEvents();

    }

    private void setupEvents() {
        binding.imageBack.setOnClickListener(v -> finish());
    }


    private void initUI() {
        getWindow().setStatusBarColor(Color.parseColor("#F04D7F"));
        Objects.requireNonNull(getSupportActionBar()).hide();
    }


}