package Activities;

import static constants.toastMessage.DEFAULT_REQUIRE;
import static utils.CartUtils.showToast;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.stores.R;
import com.example.stores.databinding.ActivitySearchBinding;
import com.example.stores.databinding.DialogFilterBinding;
import com.example.stores.databinding.ItemTabLayoutInvoiceBinding;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.Objects;

import Adapters.ViewPager2Adapter;
import Fragments.SearchProducts.SearchRelateFragment;
import Fragments.SearchProducts.SearchSellingFragment;
import Fragments.SearchProducts.SearchSortedByPriceFragment;
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

        final double[] min = {0};
        final double[] max = {0};
        dialogBinding.rdoGroupPrice.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.rdoPrice0_100) {
                    min[0] = 0;
                    max[0] = 100000;
                    dialogBinding.edtMinPrice.setText(FormatHelper.formatVND(min[0]));
                    dialogBinding.edtMaxPrice.setText(FormatHelper.formatVND(max[0]));
                } else if (checkedId == R.id.rdoPrice100_200) {
                    min[0] = 100000;
                    max[0] = 200000;
                    dialogBinding.edtMinPrice.setText(FormatHelper.formatVND(min[0]));
                    dialogBinding.edtMaxPrice.setText(FormatHelper.formatVND(max[0]));
                } else if (checkedId == R.id.rdoPrice200_300) {
                    min[0] = 200000;
                    max[0] = 300000;
                    dialogBinding.edtMinPrice.setText(FormatHelper.formatVND(min[0]));
                    dialogBinding.edtMaxPrice.setText(FormatHelper.formatVND(max[0]));
                }
            }
        });


        dialogBinding.btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
            }
        });
        dialogBinding.btnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String minPriceStr = dialogBinding.edtMinPrice.getText().toString().trim();
                String maxPriceStr = dialogBinding.edtMaxPrice.getText().toString().trim();
                if (minPriceStr.isEmpty() && maxPriceStr.isEmpty()) {
                    // call get All Products API


                } else if (maxPriceStr.isEmpty()) {
                    // call getALLProduct with min Price

                } else if (minPriceStr.isEmpty()) {
                    // call getALLProduct with max Price

                }
//                dialog.dismiss();
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
                ItemTabLayoutInvoiceBinding tabLayoutBinding = ItemTabLayoutInvoiceBinding.inflate(getLayoutInflater());
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

                if (query.isEmpty()) {
                    showToast(SearchActivity.this, "Vui lòng nhập tìm kiếm");
                } else {
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