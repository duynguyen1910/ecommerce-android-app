package Activities.Admin;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.example.stores.databinding.ActivityEcomRevenueBinding;
import java.util.ArrayList;
import java.util.Objects;
import Adapters.Statistics.StoreRevenueAdapter;
import api.invoiceApi;
import interfaces.GetCollectionCallback;
import models.Store;
import utils.TimeUtils;

public class EcomRevenueActivity extends AppCompatActivity {

    ActivityEcomRevenueBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEcomRevenueBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initUI();
        setupEvents();

    }

    private void getRevenue() {
        binding.progressBar.setVisibility(View.VISIBLE);
        invoiceApi m_invoiceApi = new invoiceApi();
        int m_currentYear = TimeUtils.getCurrentYearValue();
        m_invoiceApi.getAllStoreRevenueByYear(m_currentYear, new GetCollectionCallback<Store>() {
            @Override
            public void onGetListSuccess(ArrayList<Store> stores) {
                binding.progressBar.setVisibility(View.GONE);
                StoreRevenueAdapter adapter = new StoreRevenueAdapter(EcomRevenueActivity.this, stores);
                binding.recyclerView.setAdapter(adapter);
                binding.recyclerView.setLayoutManager(new LinearLayoutManager(EcomRevenueActivity.this, LinearLayoutManager.VERTICAL, false));

            }

            @Override
            public void onGetListFailure(String errorMessage) {

            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();
        getRevenue();
    }






    private void setupEvents() {
        binding.imageBack.setOnClickListener(v -> finish());
    }

    private void initUI() {
        getWindow().setStatusBarColor(Color.parseColor("#F04D7F"));
        Objects.requireNonNull(getSupportActionBar()).hide();
    }

}
