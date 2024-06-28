package Activities;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import com.example.stores.R;
import com.example.stores.databinding.ActivityMainBinding;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

import Fragments.HomeFragment;
import Fragments.MailFragment;
import Fragments.NotificationFragment;
import Fragments.ProfileFragment;
import Fragments.VideoFragment;

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
        fragmentMap.put(R.id.categoryMenu, new MailFragment());
        fragmentMap.put(R.id.receiptMenu, new VideoFragment());
        fragmentMap.put(R.id.memberMenu, new NotificationFragment());
        fragmentMap.put(R.id.profileMenu, new ProfileFragment());

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



    private void initUI() {
        getWindow().setStatusBarColor(Color.parseColor("#F04D7F"));
        getWindow().setNavigationBarColor(Color.parseColor("#EFEFEF"));
        Objects.requireNonNull(getSupportActionBar()).hide();
        binding.bottomNavigation.setSelectedItemId(R.id.homeMenu);

        getSupportFragmentManager()
                .beginTransaction()
                .add(binding.frameLayout.getId(), new HomeFragment(), "HomeFragment")
                .commit();

    }
}

