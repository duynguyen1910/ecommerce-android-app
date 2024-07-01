package Adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import java.util.ArrayList;
import Fragments.HomeFragment;
import Fragments.VoucherFragment;
import Fragments.NotificationFragment;
import Fragments.ProfileFragment;
import Fragments.HistoryFragment;

public class MainViewPager2Adapter extends FragmentStateAdapter {
    private ArrayList<Fragment> list;
    public MainViewPager2Adapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
        initFragmentsList();
    }

    private void initFragmentsList(){
        list = new ArrayList<>();
        list.add(new HomeFragment());
        list.add(new VoucherFragment());
        list.add(new HistoryFragment());
        list.add(new NotificationFragment());
        list.add(new ProfileFragment());
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return list.get(position);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
