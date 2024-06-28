package Activities;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager2.widget.ViewPager2;

import com.example.stores.R;
import com.example.stores.databinding.ActivityMainBinding;
import com.google.android.material.navigation.NavigationBarView;

import Adapters.MainViewPager2Adapter;
import Fragments.HomeFragment;
import Fragments.MailFragment;
import Fragments.NotificationFragment;
import Fragments.ProfileFragment;
import Fragments.VideoFragment;

public class MainActivity extends AppCompatActivity {


    private ActivityMainBinding binding;
    MainViewPager2Adapter mainViewPager2Adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());
        initUI();

        getWindow().setStatusBarColor(Color.parseColor("#F04D7F"));
        getWindow().setNavigationBarColor(Color.WHITE);
        getSupportActionBar().hide();


        binding.toolbarTitleTv.setText("Home");
//        setupViewPager();

        binding.bottomNavigation.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();
                Fragment fragment = null;
                String tag = "";
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                if (itemId == R.id.homeMenu) {
                    tag = "Home";
                    fragment = fragmentManager.findFragmentByTag(tag);
                    if (fragment == null) {
                        fragment = new HomeFragment();
                    }
//                    binding.viewPager2.setCurrentItem(0);
                    binding.toolbarTitleTv.setText("Home");
                } else if (itemId == R.id.categoryMenu) {
                    tag = "Mail";
                    fragment = fragmentManager.findFragmentByTag(tag);
                    if (fragment == null) {
                        fragment = new MailFragment();
                    }
//                    binding.viewPager2.setCurrentItem(1);
                    binding.toolbarTitleTv.setText("Mail");
                } else if (itemId == R.id.receiptMenu) {
                    tag = "Video";
                    fragment = fragmentManager.findFragmentByTag(tag);
                    if (fragment == null) {
                        fragment = new VideoFragment();
                    }
//                    binding.viewPager2.setCurrentItem(2);
                    binding.toolbarTitleTv.setText("Video");
                } else if (itemId == R.id.memberMenu) {
                    tag = "Notification";
                    fragment = fragmentManager.findFragmentByTag(tag);
                    if (fragment == null) {
                        fragment = new NotificationFragment();
                    }
//                    binding.viewPager2.setCurrentItem(3);
                    binding.toolbarTitleTv.setText("Notification");
                } else if (itemId == R.id.profileMenu) {
                    tag = "Profile";
                    fragment = fragmentManager.findFragmentByTag(tag);
                    if (fragment == null) {
                        fragment = new ProfileFragment();
                    }
//                    binding.viewPager2.setCurrentItem(4);
                    binding.toolbarTitleTv.setText("Profile");
                }
                fragmentTransaction
                        .replace(binding.frameLayout.getId(), fragment, tag)
                        .commit();
                return true;
            }
        });


//        binding.viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
//            @Override
//            public void onPageSelected(int position) {
//                super.onPageSelected(position);
//                binding.bottomNavigation.getMenu().getItem(position).setChecked(true);
//            }
//        });


    }

    private void initUI() {
        binding.bottomNavigation.setSelectedItemId(R.id.homeMenu);
        mainViewPager2Adapter = new MainViewPager2Adapter(this);
//        binding.viewPager2.setAdapter(mainViewPager2Adapter);
    }


//        private void setupViewPager() {
//            viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
//
//            mViewPager.setAdapter(viewPagerAdapter);
//
//            mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
//                @Override
//                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//
//                }
//
//                @Override
//                public void onPageSelected(int position) {
//                    Fragment fragment = viewPagerAdapter.getItem(position);
//                    backImv.setVisibility(View.GONE);
//
//
//                    switch (position) {
//                        case 0:
//                            onSetPageSelected(R.id.homeMenu, "Home");
//                            break;
//                        case 1:
//                            onSetPageSelected(R.id.categoryMenu, "Category");
//                            break;
//                        case 2:
//                            onSetPageSelected(R.id.receiptMenu, "Receipt");
//                            break;
//                        case 3:
//                            onSetPageSelected(R.id.memberMenu, "Member");
//                            break;
//                        case 4:
//                            onSetPageSelected(R.id.profileMenu, "Profile");
//                            break;
//                        default:
//                            onSetPageSelected(R.id.categoryMenu, "Category");
//                            break;
//                    }
//                }
//
//                @Override
//                public void onPageScrollStateChanged(int state) {
//
//                }
//            });
//        }

//        private void onSetPageSelected (int iconName, String fragmentName) {
//            bottomNavigation.getMenu().findItem(iconName).setChecked(true);
//            toolbarTitleTv.setText(fragmentName);
//            toolbar.setVisibility(View.VISIBLE);
//        }

}

