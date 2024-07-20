package Activities;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import com.example.stores.R;
import com.example.stores.databinding.ActivityInvoiceBinding;
import com.example.stores.databinding.ItemTabLayoutInvoiceBinding;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import java.util.Objects;
import Adapters.ViewPager2Adapter;
import Fragments.Invoice.ConfirmationFragment;
import Fragments.Invoice.DeliveryFragment;
import Fragments.Invoice.PickupFragment;
import Fragments.Invoice.CancelFragment;
import Fragments.Invoice.CompletedFragment;

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
        viewPager2Adapter.addFragment(new ConfirmationFragment(), "Chờ xác nhận");//0
        viewPager2Adapter.addFragment(new DeliveryFragment(), "Chờ giao hàng");//1
        viewPager2Adapter.addFragment(new PickupFragment(), "Chờ lấy hàng");//2
        viewPager2Adapter.addFragment(new CompletedFragment(), "Hoàn thành");//3
        viewPager2Adapter.addFragment(new CancelFragment(), "Đã hủy");//4
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
                    tabLabel.setTextColor(ContextCompat.getColor(InvoiceActivity.this, R.color.primary_color));
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                View customView = tab.getCustomView();
                if (customView != null) {
                    TextView tabLabel = customView.findViewById(R.id.tabLabel);
                    tabLabel.setTextColor(ContextCompat.getColor(InvoiceActivity.this, R.color.darkgray));
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
                tabLabel.setTextColor(ContextCompat.getColor(InvoiceActivity.this, R.color.primary_color));
            }
        }
    }

    private void setupEvents() {
        binding.imageBack.setOnClickListener(v -> finish());
        binding.imvHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(InvoiceActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });
    }

    private void initUI() {
        getWindow().setStatusBarColor(Color.parseColor("#F04D7F"));
        Objects.requireNonNull(getSupportActionBar()).hide();


    }
}