package Activities;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.stores.R;
import com.example.stores.databinding.ActivityMainBinding;

import java.util.HashMap;
import java.util.Objects;

import Fragments.BottomNavigation.HomeFragment;
import Fragments.BottomNavigation.VoucherFragment;
import Fragments.BottomNavigation.NotificationFragment;
import Fragments.BottomNavigation.ProfileFragment;
import Fragments.BottomNavigation.HistoryFragment;

public class MainActivity extends AppCompatActivity {


    private ActivityMainBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initUI();
        setupBottomNav();
    }

    private void setupBottomNav(){
        HashMap<Integer, Fragment> fragmentMap = new HashMap<>();
        fragmentMap.put(R.id.homeMenu, new HomeFragment());
        fragmentMap.put(R.id.voucherMenu, new VoucherFragment());
        fragmentMap.put(R.id.historyMenu, new HistoryFragment());
        fragmentMap.put(R.id.notificationMenu, new NotificationFragment());
        fragmentMap.put(R.id.profileMenu, new ProfileFragment());

        binding.bottomNavigation.setSelectedItemId(0);
        binding.bottomNavigation.setOnItemSelectedListener(item -> {
            Fragment fragment = fragmentMap.get(item.getItemId());
            if (fragment != null) {

                String tag = fragment.getClass().getSimpleName();

                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(binding.frameLayout.getId(), fragment, tag)
                        .commit();

                return true;
            }
            return false;
        });
    }


    @Override
    protected void onResume() {
        super.onResume();
        setupBottomNav();
    }

    private void initUI() {
        getWindow().setStatusBarColor(Color.parseColor("#FFFFFF")); // Đặt màu cho thanh trạng thái
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR); // Đặt văn bản và biểu tượng thành màu đen
        getWindow().setNavigationBarColor(Color.parseColor("#EFEFEF"));
        Objects.requireNonNull(getSupportActionBar()).hide();
        binding.bottomNavigation.setSelectedItemId(R.id.homeMenu);

        getSupportFragmentManager()
                .beginTransaction()
                .add(binding.frameLayout.getId(), new HomeFragment(), "HomeFragment")
                .commit();

    }
}

