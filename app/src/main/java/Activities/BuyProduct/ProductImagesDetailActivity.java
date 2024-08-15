package Activities.BuyProduct;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.example.stores.databinding.ActivityProductImagesDetailBinding;

import java.util.ArrayList;
import java.util.Objects;

import Adapters.SliderAdapter;
import models.SliderItem;

public class ProductImagesDetailActivity extends AppCompatActivity {

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
        ArrayList<SliderItem> sliderItems = (ArrayList<SliderItem>) getIntent().getSerializableExtra("sliderItems");
        int currentPosition = getIntent().getIntExtra("currentPosition", 0);
        binding.imageBack.setOnClickListener(v -> finish());
        binding.viewPager2Slider.setAdapter(new SliderAdapter(this, sliderItems));
        binding.viewPager2Slider.setCurrentItem(currentPosition);
        binding.viewPager2Slider.setOffscreenPageLimit(2);
        binding.indicator.attachTo(binding.viewPager2Slider);
    }


    private void initUI() {
        getWindow().setStatusBarColor(Color.parseColor("#F04D7F"));
        getWindow().setNavigationBarColor(Color.parseColor("#EFEFEF"));
        Objects.requireNonNull(getSupportActionBar()).hide();
    }
}