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
import Fragments.Invoice.PendingConfirmationFragment;
import Fragments.Invoice.PendingShipmentFragment;
import Fragments.Invoice.InTransitFragment;
import Fragments.Invoice.CancelledFragment;
import Fragments.Invoice.DeliveredFragment;
import enums.OrderStatus;
import utils.DecorateUtils;

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
        viewPager2Adapter.addFragment(new PendingConfirmationFragment(), OrderStatus.PENDING_CONFIRMATION.getOrderLabel());
        viewPager2Adapter.addFragment(new PendingShipmentFragment(), OrderStatus.PENDING_SHIPMENT.getOrderLabel());
        viewPager2Adapter.addFragment(new InTransitFragment(), OrderStatus.IN_TRANSIT.getOrderLabel());
        viewPager2Adapter.addFragment(new DeliveredFragment(), OrderStatus.DELIVERED.getOrderLabel());
        viewPager2Adapter.addFragment(new CancelledFragment(), OrderStatus.CANCELLED.getOrderLabel());
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
                    DecorateUtils.decorateSelectedTextViews(InvoiceActivity.this, tabLabel);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                View customView = tab.getCustomView();
                if (customView != null) {
                    TextView tabLabel = customView.findViewById(R.id.tabLabel);
                    DecorateUtils.decorateUnselectedTextViews(InvoiceActivity.this, tabLabel);
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
                DecorateUtils.decorateSelectedTextViews(InvoiceActivity.this, tabLabel);
            }
        }
    }

    private void setupEvents() {
        binding.imageBack.setOnClickListener(v -> finish());
        binding.imvHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              finish();
            }
        });
    }

    private void initUI() {
        getWindow().setStatusBarColor(Color.parseColor("#F04D7F"));
        Objects.requireNonNull(getSupportActionBar()).hide();
    }
}