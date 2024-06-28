package Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;

import com.example.stores.databinding.FragmentHomeBinding;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;

import Adapters.SliderAdapter;
import Models.SliderItem;

public class HomeFragment extends Fragment {
    FragmentHomeBinding binding;

    @Nullable
    @Override

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(getLayoutInflater());
        initBanner();
        return binding.getRoot();

    }

    private void initBanner() {
//        binding.progressBarBanner.setVisibility(View.VISIBLE);
        ArrayList<SliderItem> sliderItems = new ArrayList<>();
        sliderItems.add(new SliderItem("https://cf.shopee.vn/file/vn-11134258-7r98o-lwztkkhj1al5ec_xhdpi"));
        sliderItems.add(new SliderItem("https://cf.shopee.vn/file/vn-11134258-7r98o-lwztm87bnbvd54_xhdpi"));
        sliderItems.add(new SliderItem("https://cf.shopee.vn/file/vn-11134258-7r98o-lwzterl4qh97a7_xxhdpi"));
        binding.progressBarBanner.setVisibility(View.GONE);
        banners(sliderItems);

    }

    private void banners(ArrayList<SliderItem> sliderItems) {
        binding.viewPager2Slider.setAdapter(new SliderAdapter(requireActivity(), sliderItems));
        binding.viewPager2Slider.setOffscreenPageLimit(3);
        CompositePageTransformer compositePageTransformer = new CompositePageTransformer();
        compositePageTransformer.addTransformer(new MarginPageTransformer(40));
        binding.viewPager2Slider.setPageTransformer(compositePageTransformer);

        binding.indicator.setViewPager(binding.viewPager2Slider);
//        new TabLayoutMediator(binding.tabLayout, binding.viewPager2Slider, new TabLayoutMediator.TabConfigurationStrategy() {
//            @Override
//            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
//                // Bạn có thể tùy chỉnh các tab ở đây nếu cần
//            }
//        }).attach();
    }
}
