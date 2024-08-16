package Activities.BuyProduct;

import static utils.Cart.CartUtils.showToast;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import com.example.stores.R;
import com.example.stores.databinding.ActivitySearchBinding;
import com.example.stores.databinding.DialogFilterBinding;
import com.example.stores.databinding.ItemTabLabelBinding;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import java.util.ArrayList;
import java.util.Objects;
import Adapters.SettingVariant.TypeValueAdapterForFilter;
import Adapters.ViewPager2Adapter;
import Fragments.SearchProducts.SearchRelateFragment;
import Fragments.SearchProducts.SearchSellingFragment;
import Fragments.SearchProducts.SearchSortedByPriceFragment;
import interfaces.InAdapter.FilterListener;
import models.TypeValue;
import utils.DecorateUtils;
import utils.FormatHelper;

public class SearchActivity extends AppCompatActivity {

    ActivitySearchBinding binding;
    ViewPager2Adapter viewPager2Adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySearchBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getBundle();
        initUI();
        setupUI();
        setupEvents();

    }


    private void getBundle() {
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            String stringQuery = intent.getStringExtra("stringQuery");
            if (stringQuery != null) {
                binding.searchEdt.setText(stringQuery);
            }
        }
    }

    private void popUpFilterDialog() {
        Dialog dialog = new Dialog(this, android.R.style.Theme_Material_Light_Dialog);
        DialogFilterBinding dialogBinding = DialogFilterBinding.inflate(getLayoutInflater());
        dialog.setContentView(dialogBinding.getRoot());

        // Thiết lập kích thước và vị trí của dialog
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        Window window = dialog.getWindow();
        if (window != null) {
            layoutParams.copyFrom(window.getAttributes());
            layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
            layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT;

            layoutParams.horizontalMargin = (int) (100 * getResources().getDisplayMetrics().density); // 100dp
            window.setAttributes(layoutParams);

        }

        dialog.show();
        Animation slideUp = AnimationUtils.loadAnimation(this, R.anim.slide_in_from_right);
        dialogBinding.getRoot().startAnimation(slideUp);


        final double[] minPrice = {0};
        final double[] maxPrice = {Double.POSITIVE_INFINITY};

        ArrayList<TypeValue> filterRangeValues = new ArrayList<>();
        filterRangeValues.add(new TypeValue("0-100k"));
        filterRangeValues.add(new TypeValue("100k-200k"));
        filterRangeValues.add(new TypeValue("200k-300k"));
        filterRangeValues.add(new TypeValue("300k-500k"));
        filterRangeValues.add(new TypeValue("500k-1000k"));
        final int[] checkedPossition = {-1};
        TypeValueAdapterForFilter typeValueAdapter = new TypeValueAdapterForFilter(SearchActivity.this, filterRangeValues, checkedPossition[0], new FilterListener() {
            @Override
            public void transfer(int selectedPosition, String[] filterRange) {
                checkedPossition[0] = selectedPosition;
                minPrice[0] = Double.parseDouble(filterRange[0]) * 1000;
                maxPrice[0] = Double.parseDouble(filterRange[1]) * 1000;
                dialogBinding.edtMinPrice.setText(FormatHelper.formatDecimal(minPrice[0]));
                dialogBinding.edtMaxPrice.setText(FormatHelper.formatDecimal(maxPrice[0]));
            }
        });
        dialogBinding.recyclerView.setLayoutManager(new GridLayoutManager(SearchActivity.this, 3, GridLayoutManager.VERTICAL, false));
        dialogBinding.recyclerView.setAdapter(typeValueAdapter);
        dialogBinding.btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
            }
        });


        dialogBinding.edtMinPrice.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus){
                    typeValueAdapter.resetSelectedPos();
                }
            }
        });
        dialogBinding.edtMaxPrice.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus){
                    typeValueAdapter.resetSelectedPos();
                }
            }
        });


        dialogBinding.btnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String minPriceStr = dialogBinding.edtMinPrice.getText().toString().trim().replace(".", "");
                String maxPriceStr = dialogBinding.edtMaxPrice.getText().toString().trim().replace(".", "");

                try {

                    if (minPriceStr.isEmpty() && maxPriceStr.isEmpty()) {
                        // call get All Products API
                        showToast(SearchActivity.this, "getAll");

                    } else {
                        if (maxPriceStr.isEmpty()) {
                            double min = Double.parseDouble(minPriceStr);
                            // call getALLProduct with min Price
                            showToast(SearchActivity.this, min + "to infinite");

                        } else if (minPriceStr.isEmpty()) {
                            double max = Double.parseDouble(maxPriceStr);
                            // call getALLProduct with max Price
                            showToast(SearchActivity.this, "0 to " + max);

                        } else {
                            double min = Double.parseDouble(minPriceStr);
                            double max = Double.parseDouble(maxPriceStr);
                            showToast(SearchActivity.this, min + " to " + max);
                            // call getAllProduct with minPrice and max Price
                        }
                    }

                } catch (Exception e) {
                    showToast(SearchActivity.this, "Loi " + e);
                }

                dialog.dismiss();
            }
        });
    }


    private void setupUI() {
        viewPager2Adapter = new ViewPager2Adapter(this);
        viewPager2Adapter.addFragment(new SearchRelateFragment(), "Liên quan");//0
        viewPager2Adapter.addFragment(new SearchSellingFragment(), "Bán chạy");//1
        viewPager2Adapter.addFragment(new SearchSortedByPriceFragment(), "Giá ▲▼");//2
        binding.viewPager2.setAdapter(viewPager2Adapter);

        new TabLayoutMediator(binding.tabLayout, binding.viewPager2, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                ItemTabLabelBinding tabLayoutBinding = ItemTabLabelBinding.inflate(getLayoutInflater());
                TextView tabLabel = tabLayoutBinding.tabLabel;
                tabLabel.setText(viewPager2Adapter.getPageTitle(position));
                tab.setCustomView(tabLayoutBinding.getRoot());
            }
        }).attach();
        // Đặt màu hồng cho tab đầu tiên sau khi attach()


        binding.tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                View customView = tab.getCustomView();
                if (customView != null) {
                    TextView tabLabel = customView.findViewById(R.id.tabLabel);
                    DecorateUtils.decorateSelectedTextViews(SearchActivity.this, tabLabel);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                View customView = tab.getCustomView();
                if (customView != null) {
                    TextView tabLabel = customView.findViewById(R.id.tabLabel);
                    DecorateUtils.decorateUnselectedTextViews(SearchActivity.this, tabLabel);
                }
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        Intent intent = getIntent();
        if (intent != null) {
            int invoiceStatus = intent.getIntExtra("invoiceStatus", 0);
            binding.viewPager2.setCurrentItem(invoiceStatus);

            TabLayout.Tab firstTab = binding.tabLayout.getTabAt(invoiceStatus);
            if (firstTab != null && firstTab.getCustomView() != null) {
                TextView tabLabel = firstTab.getCustomView().findViewById(R.id.tabLabel);
                DecorateUtils.decorateSelectedTextViews(SearchActivity.this, tabLabel);
            }
        }
    }

    private void setupEvents() {

        binding.layoutFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popUpFilterDialog();
            }
        });
        binding.imageBack.setOnClickListener(v -> finish());

        binding.searchEdt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                binding.layoutFilter.setVisibility(View.GONE);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        binding.btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.layoutFilter.setVisibility(View.VISIBLE);
                String query = binding.searchEdt.getText().toString().trim().replace(",", "");
                if (!query.isEmpty()) {
                    SearchRelateFragment fragment = (SearchRelateFragment) viewPager2Adapter.getFragment(0);
                    fragment.fetchProductsByStringQuery(query);
                }
            }
        });
    }


    private void initUI() {
        getWindow().setStatusBarColor(Color.parseColor("#F04D7F"));
        Objects.requireNonNull(getSupportActionBar()).hide();
    }

}