package Activities.Admin;
import static constants.keyName.FULLNAME;
import static constants.keyName.USER_ID;
import static constants.keyName.USER_INFO;
import static utils.Cart.CartUtils.showToast;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide;
import com.example.stores.databinding.ActivityAdminBinding;

import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;
import api.categoryApi;
import api.invoiceApi;
import api.storeApi;
import interfaces.GetAggregate.GetAggregateCallback;
import interfaces.GetCollectionCallback;
import interfaces.ImageCallback;
import models.User;
import utils.Chart.DrawChartUtils;
import utils.TimeUtils;
import utils.FormatHelper;

public class AdminActivity extends AppCompatActivity {

    private ActivityAdminBinding binding;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAdminBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initUI();
        setupEvents();

    }

    private void getCount() {
        storeApi m_storeApi = new storeApi();
        m_storeApi.getCountOfStores(new GetAggregateCallback() {
            @Override
            public void onSuccess(double countStores) {
                FormatHelper.formatQuantityTextView(binding.txtTotalStores, (int) countStores);
            }

            @Override
            public void onFailure(String errorMessage) {
                showToast(AdminActivity.this, errorMessage);

            }
        });

        categoryApi m_categoryApi = new categoryApi();
        m_categoryApi.getCountOfCategories(new GetAggregateCallback() {
            @Override
            public void onSuccess(double countCategories) {
                FormatHelper.formatQuantityTextView(binding.txtTotalCategories, (int) countCategories);
            }

            @Override
            public void onFailure(String errorMessage) {
                showToast(AdminActivity.this, errorMessage);

            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        getUserInfo();
        getCount();
        getEcomRevenue();
    }

    private void getEcomRevenue(){
        binding.progressBar.setVisibility(View.VISIBLE);
        invoiceApi m_invoiceApi = new invoiceApi();
        int m_currentMonth = TimeUtils.getCurrentMonthValue();
        int m_currentYear = TimeUtils.getCurrentYearValue();
        binding.txtCurrentYear.setText(String.valueOf(m_currentYear));

        m_invoiceApi.getEcommerceMonthRevenueByYear(m_currentYear, new GetCollectionCallback<Double>() {
            @Override
            public void onGetListSuccess(ArrayList<Double> listRevenues) {
                binding.progressBar.setVisibility(View.GONE);
                AtomicReference<Double> yearRevenue = new AtomicReference<>((double) 0);
                listRevenues.forEach(item -> {
                    yearRevenue.updateAndGet(v -> v + item);
                });
                binding.txtRevenueInYear.setText(FormatHelper.formatVND(yearRevenue.get()));
                DrawChartUtils.drawRevenuesLineChart(AdminActivity.this, listRevenues, binding.lineChart);

                binding.layoutCompare.setVisibility(View.VISIBLE);
                DrawChartUtils.compareRevenueToLastMonth(AdminActivity.this, binding.txtCompareToLastMonth, listRevenues, m_currentMonth);
                binding.txtCurrentMonth.setText(TimeUtils.setMonthText(m_currentYear, m_currentMonth));
                binding.txtCurrentMonthRevenue.setText(FormatHelper.formatVND(listRevenues.get(m_currentMonth - 1)));
            }

            @Override
            public void onGetListFailure(String errorMessage) {
                binding.progressBar.setVisibility(View.GONE);
                showToast(AdminActivity.this, errorMessage);

            }
        });

    }
    private void getUserInfo() {
        SharedPreferences sharedPreferences = getSharedPreferences(USER_INFO, MODE_PRIVATE);
        String userID = sharedPreferences.getString(USER_ID, null);
        String fullname = sharedPreferences.getString(FULLNAME, null);
        binding.txtFullname.setText(fullname);

        if (userID != null) {
            user.getUserImageUrl(userID, new ImageCallback() {
                @Override
                public void getImageSuccess(String downloadUri) {
                    Glide.with(AdminActivity.this).load(downloadUri).into(binding.imvAvatar);
                }

                @Override
                public void getImageFailure(String errorMessage) {
                    showToast(AdminActivity.this, errorMessage);
                }
            });
        }
    }



    private void setupEvents() {
        binding.layoutTotalStores.setOnClickListener(v -> {
            Intent intent = new Intent(AdminActivity.this, EcomRevenueActivity.class);
            startActivity(intent);
        });
        binding.layoutTotalCategories.setOnClickListener(v -> {
            Intent intent = new Intent(AdminActivity.this, EcomCategoryActivity.class);
            startActivity(intent);
        });
        binding.imageBack.setOnClickListener(v -> finish());
    }

    private void initUI() {
        user = new User();
        getWindow().setStatusBarColor(Color.parseColor("#F04D7F"));
        Objects.requireNonNull(getSupportActionBar()).hide();
        binding.txtToday.setText(TimeUtils.getToday());
    }

}
