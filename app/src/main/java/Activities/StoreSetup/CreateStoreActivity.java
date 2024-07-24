package Activities.StoreSetup;
import static constants.keyName.STORE_ADDRESS;
import static constants.keyName.STORE_ID;
import static constants.keyName.STORE_NAME;
import static constants.keyName.STORE_OWNER_ID;
import static constants.keyName.USER_ID;
import static constants.keyName.USER_INFO;
import static constants.toastMessage.INTERNET_ERROR;
import static constants.toastMessage.STOREA_ADDRESS_REQUIRE;
import static constants.toastMessage.STORENAME_REQUIRE;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import com.example.stores.R;
import com.example.stores.databinding.ActivityCreateStoreBinding;
import com.example.stores.databinding.ItemTabLayoutCreateStoreBinding;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import Adapters.ViewPager2Adapter;
import Fragments.StoreSettings.SettingDeliveryFragment;
import Fragments.StoreSettings.StoreIdentifierInfoFragment;
import Fragments.StoreSettings.StoreInfoFragment;
import Fragments.StoreSettings.TaxInfoFragment;
import interfaces.CreateDocumentCallback;
import interfaces.UpdateDocumentCallback;
import kotlin.Pair;
import models.Store;
import models.User;

public class CreateStoreActivity extends AppCompatActivity {

    ActivityCreateStoreBinding binding;
    private SharedPreferences sharedPreferences;
    int currentState = 1;
    int steps = 4;
    private String storeName;
    private String emailStore;
    ViewPager2Adapter viewPager2Adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCreateStoreBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initUI();
        setupUI();

        setupEvents();
    }

    private void setupEvents() {
        binding.imageBack.setOnClickListener(v -> finish());

        binding.btnNext.setOnClickListener(v -> {
            if (currentState < steps) {
                currentState++;
                binding.stepProgressbar.setProgress(new Pair<>(currentState, 100f));
                binding.viewPager2.setCurrentItem(currentState - 1);
                setupButton();
            } else if (currentState == steps) {

                binding.progressBar.setVisibility(View.VISIBLE);
                binding.btnNext.setFocusable(false);
                binding.btnNext.setBackground(ContextCompat.getDrawable(this, R.color.darkgray));
                binding.progressBar.getIndeterminateDrawable()
                        .setColorFilter(Color.parseColor("#F04D7F"), PorterDuff.Mode.MULTIPLY);


                // Khởi tạo đối tượng Store để gọi api kết nối với Collection Store

                Store newStore = new Store();
                sharedPreferences = getSharedPreferences(USER_INFO, MODE_PRIVATE);
                String userId = sharedPreferences.getString(USER_ID, null);


                // Lấy thông tin store từ fragment StoreInfo
                StoreInfoFragment storeInfoFragment = (StoreInfoFragment) viewPager2Adapter.getFragment(0);

                String storeName = storeInfoFragment.getStoreName();
                if (storeName == null){
                    binding.btnNext.setFocusable(true);
                    binding.btnNext.setBackground(ContextCompat.getDrawable(CreateStoreActivity.this, R.color.primary_color));
                    binding.viewPager2.setCurrentItem(0);
                    binding.progressBar.setVisibility(View.GONE);
                    Toast.makeText(this, STORENAME_REQUIRE, Toast.LENGTH_SHORT).show();
                    return;
                }
                String storeAddress = storeInfoFragment.getStoreAddress();
                if (storeAddress == null){
                    binding.btnNext.setFocusable(true);
                    binding.btnNext.setBackground(ContextCompat.getDrawable(CreateStoreActivity.this, R.color.primary_color));
                    binding.progressBar.setVisibility(View.GONE);
                    binding.viewPager2.setCurrentItem(0);
                    Toast.makeText(this, STOREA_ADDRESS_REQUIRE, Toast.LENGTH_SHORT).show();
                    return;
                }

                // setup thông tin cho new store
                Map<String, Object> storeInfo = new HashMap<>();
                storeInfo.put(STORE_NAME, storeName);
                storeInfo.put(STORE_ADDRESS, storeAddress);
                storeInfo.put(STORE_OWNER_ID, userId);
                // call API
                newStore.onCreateStore(storeInfo, new CreateDocumentCallback() {
                    @Override
                    public void onCreateSuccess(String documentId) {
                        // Sau khi tạo  store xong, ta lấy storeId vừa tạo, call api update user
                        User user = new User();
                        Map<String, Object> updateData = new HashMap<>();
                        updateData.put(STORE_ID, documentId);
                        user.onUpdate(updateData, userId, new UpdateDocumentCallback() {
                            @Override
                            public void onUpdateSuccess() {
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString(STORE_ID, documentId);
                                editor.apply();

                                binding.progressBar.setVisibility(View.GONE);
                                Intent intent = new Intent(CreateStoreActivity.this, RegisterStoreSuccessfulActivity.class);
                                startActivity(intent);
                                finish();
                            }

                            @Override
                            public void onUpdateFailure(String errorMessage) {
                                Toast.makeText(CreateStoreActivity.this, INTERNET_ERROR, Toast.LENGTH_SHORT).show();
                            }
                        });



                    }

                    @Override
                    public void onCreateFailure(String errorMessage) {
                        binding.btnNext.setFocusable(true);
                        binding.btnNext.setBackground(ContextCompat.getDrawable(CreateStoreActivity.this, R.color.primary_color));
                        binding.progressBar.setVisibility(View.GONE);
                        Toast.makeText(CreateStoreActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                    }
                });




            }
        });

        binding.btnPrevious.setOnClickListener(v -> {
            if (currentState > 1) {
                currentState--;
                binding.stepProgressbar.setProgress(new Pair<>(currentState, 100f));
                binding.viewPager2.setCurrentItem(currentState - 1);
                setupButton();
            }
        });
    }
    private void updateStoreId(String newStoreId) {
        sharedPreferences = getSharedPreferences(USER_INFO, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(STORE_ID, newStoreId);
        editor.apply();
    }


    private void setupButton() {
        if (currentState == 1) {
            binding.btnPrevious.setVisibility(View.GONE);
            binding.btnNext.setText("Tiếp theo");
        } else {
            binding.btnPrevious.setVisibility(View.VISIBLE);
            binding.btnPrevious.setText("Quay lại");
            if (currentState >= steps) {
                binding.btnNext.setText("Hoàn tất");
            } else {
                binding.btnNext.setText("Tiếp theo");
            }
        }
    }

    private void setupUI() {
        viewPager2Adapter = new ViewPager2Adapter(this);

        viewPager2Adapter.addFragment(new StoreInfoFragment(), "Thông tin Store");
        viewPager2Adapter.addFragment(new SettingDeliveryFragment(), "Cài đặt vận chuyển");
        viewPager2Adapter.addFragment(new TaxInfoFragment(), "Thông tin thuế");
        viewPager2Adapter.addFragment(new StoreIdentifierInfoFragment(), "Thông tin định danh");


        binding.viewPager2.setAdapter(viewPager2Adapter);
        binding.viewPager2.setCurrentItem(0);

        new TabLayoutMediator(binding.tabLayout, binding.viewPager2, (tab, position) -> {
            ItemTabLayoutCreateStoreBinding tabLayoutBinding = ItemTabLayoutCreateStoreBinding.inflate(getLayoutInflater());
            TextView tabLabel = tabLayoutBinding.tabLabel;
            tabLabel.setText(viewPager2Adapter.getPageTitle(position));
            tab.setCustomView(tabLayoutBinding.getRoot());

        }).attach();
        // Đặt màu hồng cho tab đầu tiên sau khi attach()
        TabLayout.Tab firstTab = binding.tabLayout.getTabAt(0);
        if (firstTab != null && firstTab.getCustomView() != null) {
            TextView tabLabel = firstTab.getCustomView().findViewById(R.id.tabLabel);
            tabLabel.setTextColor(ContextCompat.getColor(CreateStoreActivity.this, R.color.primary_color));
        }

        binding.tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                currentState= tab.getPosition() + 1;
                binding.stepProgressbar.setProgress(new Pair<>(currentState, 100f));
                setupButton();
                View customView = tab.getCustomView();
                if (customView != null) {
                    TextView tabLabel = customView.findViewById(R.id.tabLabel);
                    tabLabel.setTextColor(ContextCompat.getColor(CreateStoreActivity.this, R.color.primary_color));
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                View customView = tab.getCustomView();
                if (customView != null) {
                    TextView tabLabel = customView.findViewById(R.id.tabLabel);
                    tabLabel.setTextColor(ContextCompat.getColor(CreateStoreActivity.this, R.color.darkgray));
                }
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private void initUI() {
        getWindow().setStatusBarColor(Color.parseColor("#F04D7F"));
        Objects.requireNonNull(getSupportActionBar()).hide();
    }


    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }
}