package Activities;

import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.stores.databinding.ActivityProductDetailBinding;
import com.example.stores.databinding.ActivityProductImagesDetailBinding;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Objects;

import Adapters.SizeAdapter;
import Adapters.SliderAdapter;
import Adapters.ViewPager2Adapter;
import Fragments.DescriptionFragment;
import Fragments.ReviewFragment;
import Fragments.SoldFragment;
import Models.Product;
import Models.SliderItem;

public class ProductImagesDetailActivity extends AppCompatActivity {


    private int numberOrder = 1;

    private Handler slideHandler = new Handler();
    ActivityProductImagesDetailBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProductImagesDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initUI();

        initSlider();

    }


    private void initSlider() {
        ArrayList sliderItems = (ArrayList) getIntent().getSerializableExtra("sliderItems");
        int currentPosition = getIntent().getIntExtra("currentPosition", 0);
        binding.imageBack.setOnClickListener(v -> finish());
        binding.viewPager2Slider.setAdapter(new SliderAdapter(this, sliderItems));
        binding.viewPager2Slider.setCurrentItem(currentPosition);
        binding.viewPager2Slider.setOffscreenPageLimit(2);
        binding.indicator.setViewPager(binding.viewPager2Slider);
    }


    private void initUI() {
        getWindow().setStatusBarColor(Color.parseColor("#F04D7F"));

        getWindow().setNavigationBarColor(Color.parseColor("#EFEFEF"));
        Objects.requireNonNull(getSupportActionBar()).hide();
    }
}