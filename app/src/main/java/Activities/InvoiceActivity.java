package Activities;
import android.graphics.Color;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.example.stores.databinding.ActivityInvoiceBinding;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import java.util.Objects;
import Adapters.ViewPager2Adapter;
import Fragments.InvoiceAwaitConfirmationFragment;
import Fragments.InvoiceAwaitDeliveryFragment;
import Fragments.InvoiceAwaitPickupFragment;
import Fragments.InvoiceCancelFragment;
import Fragments.InvoiceCompletedFragment;
public class InvoiceActivity extends AppCompatActivity {

    ActivityInvoiceBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityInvoiceBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initUI();
        setupUI();

        setupEvents();

    }

    private void setupUI() {
        ViewPager2Adapter viewPager2Adapter = new ViewPager2Adapter(this);
        viewPager2Adapter.addFragment(new InvoiceAwaitConfirmationFragment(), "Chờ xác nhận");
        viewPager2Adapter.addFragment(new InvoiceAwaitDeliveryFragment(), "Chờ giao hàng");
        viewPager2Adapter.addFragment(new InvoiceAwaitPickupFragment(), "Chờ lấy hàng");
        viewPager2Adapter.addFragment(new InvoiceCompletedFragment(), "Hoàn thành");
        viewPager2Adapter.addFragment(new InvoiceCancelFragment(), "Đã hủy");

        binding.viewPager2.setAdapter(viewPager2Adapter);
        new TabLayoutMediator(binding.tabLayout, binding.viewPager2, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                if (position == 0) {
                    tab.setText(viewPager2Adapter.getPageTitle(0));
                } else if (position == 1) {
                    tab.setText(viewPager2Adapter.getPageTitle(1));
                }else if (position == 2) {
                    tab.setText(viewPager2Adapter.getPageTitle(2));
                }else if (position == 3) {
                    tab.setText(viewPager2Adapter.getPageTitle(3));
                }else if (position == 4) {
                    tab.setText(viewPager2Adapter.getPageTitle(4));
                }
            }
        }).attach();
    }
    private void setupEvents() {
        binding.imageBack.setOnClickListener(v -> finish());


    }

    private void initUI() {
        getWindow().setStatusBarColor(Color.parseColor("#F04D7F"));
        Objects.requireNonNull(getSupportActionBar()).hide();
    }
}