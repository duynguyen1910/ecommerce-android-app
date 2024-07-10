package Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;

import com.example.stores.R;
import com.example.stores.databinding.FragmentHomeBinding;

import java.util.ArrayList;

import Activities.CartActivity;
import Activities.StatisticsActivity;
import Adapters.PopularBrandAdapter;
import Adapters.ProductAdapter;
import Adapters.SliderAdapter;
import models.OfficialBrand;
import models.Product;
import models.SliderItem;

public class HomeFragment extends Fragment {
    FragmentHomeBinding binding;
    ArrayList<SliderItem> sliderItems;
    Handler sliderHandler;
    Runnable sliderRunnable;

    @Nullable
    @Override

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(getLayoutInflater());
        initBanner();
        initOfficialBrand();
        initProducts();
        setupEvents();

        return binding.getRoot();

    }

    private void initBanner() {
//        binding.progressBarBanner.setVisibility(View.VISIBLE);
        sliderItems = new ArrayList<>();
        sliderItems.add(new SliderItem("https://cf.shopee.vn/file/vn-11134258-7r98o-lwztkkhj1al5ec_xhdpi"));
        sliderItems.add(new SliderItem("https://cf.shopee.vn/file/vn-11134258-7r98o-lwztm87bnbvd54_xhdpi"));
        sliderItems.add(new SliderItem("https://cf.shopee.vn/file/vn-11134258-7r98o-lwzterl4qh97a7_xxhdpi"));
        sliderItems.add(new SliderItem("https://cf.shopee.vn/file/vn-11134258-7r98o-lwzasb4nio637c_xxhdpi"));
        binding.progressBarBanner.setVisibility(View.GONE);
        banners(sliderItems);
        startSliderAutoCycle();
    }

    private void startSliderAutoCycle() {
        sliderHandler = new Handler();
        sliderRunnable = new Runnable() {
            @Override
            public void run() {
                int currentItem = binding.viewPager2Slider.getCurrentItem();
                int nextItem = currentItem + 1;
                if (nextItem >= sliderItems.size()) {
                    nextItem = 0;
                }
                binding.viewPager2Slider.setCurrentItem(nextItem);
                sliderHandler.postDelayed(this, 3000);
            }
        };
        sliderHandler.postDelayed(sliderRunnable, 1500);
    }

    private void stopSliderAutoCycle() {
        sliderHandler.removeCallbacks(sliderRunnable);
    }

    @Override
    public void onDestroyView() {
        stopSliderAutoCycle();
        super.onDestroyView();
    }

    private void banners(ArrayList<SliderItem> sliderItems) {
        binding.viewPager2Slider.setAdapter(new SliderAdapter(requireActivity(), sliderItems));
        binding.viewPager2Slider.setOffscreenPageLimit(3);
        CompositePageTransformer compositePageTransformer = new CompositePageTransformer();
        compositePageTransformer.addTransformer(new MarginPageTransformer(40));
        binding.viewPager2Slider.setPageTransformer(compositePageTransformer);
//        binding.indicator.setViewPager(binding.viewPager2Slider);
    }

    private void initOfficialBrand() {
//        DatabaseReference popularRef = database.getReference("Items");
//        binding.progressBarOfficialBrand.setVisibility(View.VISIBLE);
        ArrayList<OfficialBrand> list = new ArrayList<>();

        list.add(new OfficialBrand("Adidas", 1, "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQ1dtlrN5hP1x-m9AwA-NqGuUv2rwyehMoIkg&s"));
        list.add(new OfficialBrand("Nike", 2, "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQcWxm80LumKRT5BMbTqslFU9otu470BgW6LA&s"));
        list.add(new OfficialBrand("Puma", 3, "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQCP0XlN_ojXgAauWzIj9rk1XEGh-X-ixbieQ&s"));
        list.add(new OfficialBrand("Zara", 4, "https://cdn.icon-icons.com/icons2/2845/PNG/512/zara_logo_icon_181327.png"));
        list.add(new OfficialBrand("Chanel", 5, "https://i.pinimg.com/736x/d1/aa/c9/d1aac9ca68abe81dfbab955a9073167c.jpg"));

        if (!list.isEmpty()) {
            binding.recyclerViewOfficialBrand.setLayoutManager(new LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false));
            binding.recyclerViewOfficialBrand.setAdapter(new PopularBrandAdapter(requireActivity(), list));
        }
        binding.progressBarOfficialBrand.setVisibility(View.GONE);


    }

    private void initProducts() {

//        binding.progressBarProducts.setVisibility(View.VISIBLE);
        ArrayList<Product> list = new ArrayList<>();
        ArrayList<String> picUrls1 = new ArrayList<>();
        picUrls1.add("https://down-vn.img.susercontent.com/file/sg-11134201-7rbmn-lqld5fts53pj7e");
        picUrls1.add("https://down-vn.img.susercontent.com/file/sg-11134201-7rbl4-lqld5gi75d2e60");
        picUrls1.add("https://down-vn.img.susercontent.com/file/sg-11134201-7rbmc-lqld5e0asxgg61");
        picUrls1.add("https://down-vn.img.susercontent.com/file/sg-11134201-7rbk4-lqlpjd5622bp5d");

        ArrayList<String> picUrls2 = new ArrayList<>();
        picUrls2.add("https://down-vn.img.susercontent.com/file/sg-11134201-7rccv-ls294ot5axpz02");
        picUrls2.add("https://down-vn.img.susercontent.com/file/sg-11134201-7rcdn-ls294pp208mn48");
        picUrls2.add("https://down-vn.img.susercontent.com/file/sg-11134201-7rcdp-ls294qoujut336");
        picUrls2.add("https://down-vn.img.susercontent.com/file/sg-11134201-7rcev-ls294uaz9fcv33");


        ArrayList<String> picUrls3 = new ArrayList<>();
        picUrls3.add("https://down-vn.img.susercontent.com/file/e55e62939961126e8ef3b54061f0307d");
        picUrls3.add("https://down-vn.img.susercontent.com/file/91d4a1fe3c410262bf67de9ac30e837d");
        picUrls3.add("https://down-vn.img.susercontent.com/file/94ef82139c4ba8bfe56df151ff783e36");
        picUrls3.add("https://down-vn.img.susercontent.com/file/4444ceee25d81cdc39228cdaec86c89e");

        ArrayList<String> picUrls4 = new ArrayList<>();
        picUrls4.add("https://down-vn.img.susercontent.com/file/sg-11134201-7rcck-lsaqdevfanrq8f");
        picUrls4.add("https://down-vn.img.susercontent.com/file/sg-11134201-7rce5-lsaqdhlxbx002b");
        picUrls4.add("https://down-vn.img.susercontent.com/file/sg-11134201-7rcen-lsaqdfgsfeeqa7");
        picUrls4.add("https://down-vn.img.susercontent.com/file/sg-11134201-7rcfb-lsaqdfxpqmpo1c");

//        Product(String title, String description, ArrayList<String> picUrl, double price, double rating, int sold)
        list.add(new Product("Lovito Đầm chữ A phối ren hoa đơn giản dành cho nữ LNA38057", getResources().getResourceName(R.string.product_desc1).toString(), picUrls1, 149000, 298000, 4.9, 200, 50, 2));
        list.add(new Product("Lovito Đầm trễ vai ngọc trai trơn đơn giản dành cho nữ L76AD154", getResources().getResourceName(R.string.product_desc1).toString(), picUrls2, 119000, 228000, 4.8, 179, 48, 2));
        list.add(new Product("Đồng Hồ Điện Tử Chống Nước Phong Cách Quân Đội SANDA 2023 Cho Nam", getResources().getResourceName(R.string.product_desc2).toString(), picUrls3, 189000, 350000, 5.0, 559, 46, 3));
        list.add(new Product("Huizumei Váy preppy nữ mùa hè cổ polo nhỏ chắp vá eo nâng cao và giảm béo váy ngắn", getResources().getResourceName(R.string.product_desc3).toString(), picUrls4, 129000, 235000, 4.7, 989, 45, 1));
//
        if (!list.isEmpty()) {
            binding.recyclerViewProducts.setLayoutManager(new GridLayoutManager(requireActivity(), 2));
            binding.recyclerViewProducts.setAdapter(new ProductAdapter(requireActivity(), list));
        }
        binding.progressBarProducts.setVisibility(View.GONE);

    }

    private void setupEvents(){
        binding.iconCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(requireActivity(), CartActivity.class);
                startActivity(intent);
            }
        });

        binding.layoutStatistics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(requireActivity(), StatisticsActivity.class);
                startActivity(intent);
            }
        });
    }

}
