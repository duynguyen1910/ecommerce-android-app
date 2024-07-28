package Fragments.BottomNavigation;

import static constants.toastMessage.INTERNET_ERROR;
import static utils.CartUtils.updateQuantityInCart;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;

import com.example.stores.databinding.FragmentHomeBinding;

import java.util.ArrayList;

import Activities.BuyProduct.CartActivity;
import Activities.BuyProduct.SearchActivity;
import Activities.StatisticsActivity;
import Adapters.Category.CategoryGridAdapter;
import Adapters.ProductAdapter;
import Adapters.SliderAdapter;
import interfaces.GetCollectionCallback;
import models.Category;
import models.Product;
import models.SliderItem;

public class HomeFragment extends Fragment {
    FragmentHomeBinding binding;
    ArrayList<SliderItem> sliderItems;
    Handler sliderHandler;
    Runnable sliderRunnable;
    ArrayList<Category> categoriesList = new ArrayList<>();
    boolean getCategoriesSuccess = false;

    @Nullable
    @Override

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(getLayoutInflater());

        initBanner();
        getCategories();
        setupCategoryUI();
        initProducts();
        setupEvents();

        return binding.getRoot();

    }

    @Override
    public void onResume() {
        super.onResume();
        updateQuantityInCart(binding.txtQuantityInCart);
    }


    private void initBanner() {
        sliderItems = new ArrayList<>();
        sliderItems.add(new SliderItem("https://cf.shopee.vn/file/vn-11134258-7r98o-ly0hr2zmqifv2e_xxhdpi"));
        sliderItems.add(new SliderItem("https://cf.shopee.vn/file/vn-11134258-7r98o-lxuu1mpyl1w9ec_xxhdpi"));
        sliderItems.add(new SliderItem("https://cf.shopee.vn/file/vn-11134258-7r98o-lxutdtxck0yj9c_xxhdpi"));
        sliderItems.add(new SliderItem("https://cf.shopee.vn/file/vn-11134258-7r98o-lwztkkhj1al5ec_xhdpi"));
        sliderItems.add(new SliderItem("https://cf.shopee.vn/file/vn-11134258-7r98o-lwztm87bnbvd54_xhdpi"));
        sliderItems.add(new SliderItem("https://cf.shopee.vn/file/vn-11134258-7r98o-lwzasb4nio637c_xxhdpi"));
        binding.progressBarBanner.setVisibility(View.GONE);
        banners(sliderItems);
        startSliderAutoCycle();
    }


    private void setupCategoryUI(){
        if (getCategoriesSuccess){
            binding.progressBarCategory.setVisibility(View.GONE);
            binding.recyclerViewCategory.setLayoutManager(new GridLayoutManager(requireActivity(), 3, GridLayoutManager.HORIZONTAL, false));
            binding.recyclerViewCategory.setAdapter(new CategoryGridAdapter(requireActivity(), categoriesList));
        }
    }

    private void getCategories() {
        Category category = new Category();

        binding.progressBarCategory.setVisibility(View.VISIBLE);
        category.getCategoryCollection(new GetCollectionCallback<Category>() {
            @Override

            public void onGetListSuccess(ArrayList<Category> categories) {
                categoriesList = new ArrayList<>(categories);
                getCategoriesSuccess = true;
                setupCategoryUI();
            }

            @Override
            public void onGetListFailure(String errorMessage) {
                Toast.makeText(requireActivity(), INTERNET_ERROR, Toast.LENGTH_SHORT).show();
            }
        });


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
                sliderHandler.postDelayed(this, 8000);
            }
        };
        sliderHandler.postDelayed(sliderRunnable, 3000);
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
        binding.indicator.setViewPager(binding.viewPager2Slider);
    }

    private void initProducts() {

//        binding.progressBarProducts.setVisibility(View.VISIBLE);
        ArrayList<Product> list = new ArrayList<>();

        if (!list.isEmpty()) {
            binding.recyclerViewProducts.setLayoutManager(new GridLayoutManager(requireActivity(), 2));
            binding.recyclerViewProducts.setAdapter(new ProductAdapter(requireActivity(), list));
        }
        binding.progressBarProducts.setVisibility(View.GONE);

    }

    private void setupEvents() {
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

        binding.btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String stringQuery = binding.searchEdt.getText().toString().trim();
                Intent intent = new Intent(requireActivity(), SearchActivity.class);
                intent.putExtra("stringQuery", stringQuery);
                startActivity(intent);

            }
        });
    }

}
