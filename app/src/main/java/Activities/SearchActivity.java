package Activities;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import com.example.stores.R;
import com.example.stores.databinding.ActivitySearchBinding;
import com.example.stores.databinding.ItemTabLayoutInvoiceBinding;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import java.util.Objects;
import Adapters.ViewPager2Adapter;
import Fragments.SearchProducts.SearchRelateFragment;
import Fragments.SearchProducts.SearchSellingFragment;
import Fragments.SearchProducts.SearchSortedByPriceFragment;

public class SearchActivity extends AppCompatActivity{

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



    private void getBundle(){
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null){
            String stringQuery = intent.getStringExtra("stringQuery");
            if (stringQuery != null){
                binding.searchEdt.setText(stringQuery);
            }
        }
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
                    tabLabel.setTextColor(ContextCompat.getColor(SearchActivity.this, R.color.primary_color));
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                View customView = tab.getCustomView();
                if (customView != null) {
                    TextView tabLabel = customView.findViewById(R.id.tabLabel);
                    tabLabel.setTextColor(ContextCompat.getColor(SearchActivity.this, R.color.darkgray));
                }
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        Intent intent = getIntent();
        if (intent!= null){
            int invoiceStatus = intent.getIntExtra("invoiceStatus", 0);
            binding.viewPager2.setCurrentItem(invoiceStatus);

            TabLayout.Tab firstTab = binding.tabLayout.getTabAt(invoiceStatus);
            if (firstTab != null && firstTab.getCustomView() != null) {
                TextView tabLabel = firstTab.getCustomView().findViewById(R.id.tabLabel);
                tabLabel.setTextColor(ContextCompat.getColor(SearchActivity.this, R.color.primary_color));
            }
        }
    }

    private void setupEvents() {
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

                if (query.isEmpty()){
                    showToast("Vui lòng nhập tìm kiếm");
                }else {
                    SearchRelateFragment fragment = (SearchRelateFragment) viewPager2Adapter.getFragment(0);
                    fragment.fetchProductsByStringQuery(query);
                }

            }
        });
    }

    private void showToast(String message){
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private void initUI() {
        getWindow().setStatusBarColor(Color.parseColor("#F04D7F"));
        Objects.requireNonNull(getSupportActionBar()).hide();
    }

}