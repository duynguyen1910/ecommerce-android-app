package Activities;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.stores.R;
import com.example.stores.databinding.ActivityProductDetailBinding;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Objects;

import Adapters.SizeAdapter;
import Adapters.SliderAdapter;
import Adapters.SliderAdapterForProductDetail;
import Adapters.ViewPager2Adapter;
import Fragments.DescriptionFragment;
import Fragments.HomeFragment;
import Fragments.ReviewFragment;
import Fragments.SoldFragment;
import Models.Product;
import Models.SliderItem;

public class ProductDetailActivity extends AppCompatActivity {

    private Product object;
    private int numberOrder = 1;

    private Handler slideHandler = new Handler();
    ActivityProductDetailBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProductDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initUI();
        getBundles();

        initSlider();

        initSize();

        setUpViewPager2();
    }

    private void setUpViewPager2(){
        ViewPager2Adapter adapter = new ViewPager2Adapter(this);
        DescriptionFragment tab1 = new DescriptionFragment();
        ReviewFragment tab2 = new ReviewFragment();
        SoldFragment tab3 = new SoldFragment();

        Bundle bundle1 = new Bundle();
        Bundle bundle2 = new Bundle();
        Bundle bundle3 = new Bundle();
        bundle1.putString("Description", object.getDescription());
        bundle2.putInt("Reviews", object.getReview());




        tab1.setArguments(bundle1);
        tab2.setArguments(bundle2);
        tab3.setArguments(bundle3);


        adapter.addFragment(tab1, "Description");
        adapter.addFragment(tab2, "Reviews");
        adapter.addFragment(tab3, "Sold");

        binding.viewPager2SizeSlider.setAdapter(adapter);
        new TabLayoutMediator(binding.tabLayout, binding.viewPager2SizeSlider, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                if (position == 0){
                    tab.setText("Description");
                }else if (position == 1){
                    tab.setText("Reviews");
                }else {
                    tab.setText("Sold");
                }
            }
        }).attach();



    }

    private void initSize() {
        ArrayList<String> list = new ArrayList<>();
        list.add("S");
        list.add("M");
        list.add("L");
        list.add("XL");
        list.add("XXL");
        binding.recyclerViewSize.setAdapter(new SizeAdapter(this, list));
        binding.recyclerViewSize.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

    }

    private void initSlider() {
        ArrayList<SliderItem> sliderItems = new ArrayList<>();
        for (int i = 0; i < object.getPicUrl().size(); i++) {
            sliderItems.add(new SliderItem(object.getPicUrl().get(i)));
        }

        binding.viewPager2Slider.setAdapter(new SliderAdapterForProductDetail(this, sliderItems));
        binding.viewPager2Slider.setOffscreenPageLimit(2);
        binding.indicator.setViewPager(binding.viewPager2Slider);



    }

    private void getBundles() {
        object = (Product) getIntent().getSerializableExtra("object");
        binding.txtTitle.setText(object.getTitle());
        NumberFormat formatter = NumberFormat.getInstance(new Locale("vi", "VN"));
        String formattedOldPrice = formatter.format(object.getOldPrice());
        binding.txtOldPrice.setText("đ" + formattedOldPrice);
        binding.txtOldPrice.setPaintFlags(binding.txtOldPrice.getPaintFlags()| Paint.STRIKE_THRU_TEXT_FLAG);
        String formattedPrice = formatter.format(object.getOldPrice()*(100-object.getSaleoff())/100);

        binding.txtPrice.setText(formattedPrice);

        binding.ratingBar.setRating((float) object.getRating());
        binding.txtRating.setText(String.valueOf(object.getRating()) + " / 5");
        binding.txtSold.setText("Đã bán " + object.getSold());
        binding.btnAddToCart.setOnClickListener(v -> {
//            object.setNumberinCart(numberOrder);
//            managmentCart.insertItem(object);
        });
        binding.imageBack.setOnClickListener(v -> finish());


    }

    private void initUI() {
        getWindow().setStatusBarColor(Color.parseColor("#F04D7F"));

        getWindow().setNavigationBarColor(Color.parseColor("#EFEFEF"));
        Objects.requireNonNull(getSupportActionBar()).hide();
    }
}