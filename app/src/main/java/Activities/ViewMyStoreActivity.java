package Activities;

import static constants.keyName.STORE_ID;
import static constants.keyName.STORE_NAME;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.stores.R;
import com.example.stores.databinding.ActivityViewMyStoreBinding;
import com.example.stores.databinding.ItemTabLayoutInvoiceBinding;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.Map;
import java.util.Objects;

import Adapters.ViewPager2Adapter;
import Fragments.Store.StoreCategoriesFragment;
import Fragments.Store.StoreProductsFragment;
import interfaces.GetDocumentCallback;
import models.Store;

public class ViewMyStoreActivity extends AppCompatActivity {

    ActivityViewMyStoreBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityViewMyStoreBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initUI();
        setupUI();
        setupEvents();
    }

    private void setupEvents() {
        binding.imageBack.setOnClickListener(v -> finish());
//        binding.btnDecorateMyStore.setOnClickListener(v -> {
//            Intent intent = new Intent(ViewMyStoreActivity.this, DecorateMyStoreActivity.class);
//            startActivity(intent);
//        });
    }
    private void initUI() {
        getWindow().setStatusBarColor(Color.parseColor("#F04D7F"));
        Objects.requireNonNull(getSupportActionBar()).hide();

        binding.progressBar.setVisibility(View.VISIBLE);



        String storeId = getIntent().getStringExtra(STORE_ID);

        // lấy thông tin avatar, invoice



        if (storeId != null) {
            Store store = new Store();
            store.onGetStoreDetail(storeId, new GetDocumentCallback() {
                @Override
                public void onGetDataSuccess(Map<String, Object> data) {
                    binding.progressBar.setVisibility(View.GONE);
                    binding.txtStoreName.setText((CharSequence) data.get(STORE_NAME));

                    // set up UI avatar, invoice

                }

                @Override
                public void onGetDataFailure(String errorMessage) {
                    Toast.makeText(ViewMyStoreActivity.this, "Uiii, lỗi mạng rồi :(((", Toast.LENGTH_SHORT).show();
                }
            });


        }

    }

    private void setupUI() {
        ViewPager2Adapter viewPager2Adapter = new ViewPager2Adapter(this);
        viewPager2Adapter.addFragment(new StoreProductsFragment(), "Sản phẩm");
        viewPager2Adapter.addFragment(new StoreCategoriesFragment(), "Danh mục hàng");
        binding.viewPager2.setNestedScrollingEnabled(true);
        binding.viewPager2.setAdapter(viewPager2Adapter);
        binding.viewPager2.setCurrentItem(0);

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
        TabLayout.Tab firstTab = binding.tabLayout.getTabAt(0);
        if (firstTab != null && firstTab.getCustomView() != null) {
            TextView tabLabel = firstTab.getCustomView().findViewById(R.id.tabLabel);
            tabLabel.setTextColor(ContextCompat.getColor(ViewMyStoreActivity.this, R.color.primary_color));
        }

        binding.tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                View customView = tab.getCustomView();
                if (customView != null) {
                    TextView tabLabel = customView.findViewById(R.id.tabLabel);
                    tabLabel.setTextColor(ContextCompat.getColor(ViewMyStoreActivity.this, R.color.primary_color));
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                View customView = tab.getCustomView();
                if (customView != null) {
                    TextView tabLabel = customView.findViewById(R.id.tabLabel);
                    tabLabel.setTextColor(ContextCompat.getColor(ViewMyStoreActivity.this, R.color.darkgray));
                }
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }


}