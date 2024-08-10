package Activities;

import static constants.keyName.USER_ID;
import static constants.keyName.USER_INFO;
import static utils.Cart.CartUtils.showToast;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.stores.R;
import com.example.stores.databinding.ActivitySpendingsBinding;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import api.invoiceApi;
import interfaces.GetCollectionCallback;
import utils.Chart.CustomValueMoneyFormatter;
import utils.TimeUtils;

public class SpendingsActivity extends AppCompatActivity {


    private String customerID;
    ActivitySpendingsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySpendingsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initUI();
        getCustomerID();
        setupEvents();


    }

    @Override
    protected void onResume() {
        super.onResume();
        getSpendingInHaftYear();
    }

    private void initUI() {
        getWindow().setStatusBarColor(Color.parseColor("#F04D7F"));
        Objects.requireNonNull(getSupportActionBar()).hide();

    }

    private void getSpendingInHaftYear() {
        binding.progressBar.setVisibility(View.VISIBLE);
        invoiceApi m_invoiceApi = new invoiceApi();
        int m_currentMonth = getCurrentMonth();

        m_invoiceApi.getSpendingsInHaftYearByCustomerID(customerID, m_currentMonth, new GetCollectionCallback<Double>() {
            @Override
            public void onGetListSuccess(ArrayList<Double> listRevenues) {
                binding.progressBar.setVisibility(View.GONE);
                drawSpendingInHaftYear(listRevenues, m_currentMonth);
            }

            @Override
            public void onGetListFailure(String errorMessage) {
                binding.progressBar.setVisibility(View.GONE);
                showToast(SpendingsActivity.this, errorMessage);

            }
        });

    }

    private int getCurrentMonth() {
        int month = Calendar.getInstance().get(Calendar.MONTH) + 1;
        Log.d("currentMonth", "currentMonth: " + month);
        return month;
    }


    private void drawSpendingInHaftYear(ArrayList<Double> listSpendings, int currentMonth) {
        BarChart barChart = binding.spendingsChart;
        List<BarEntry> spendingEntries = new ArrayList<>();
        List<Integer> colors = new ArrayList<>();
        int colorCurrentMonth = ContextCompat.getColor(SpendingsActivity.this, R.color.primary_color);
        int colorOtherMonths = ContextCompat.getColor(SpendingsActivity.this, R.color.light_light_primary);

        if (currentMonth < 7) {
            for (int i = 0; i < listSpendings.size(); i++) {
                float monthLabel = i + 1;
                float spending = listSpendings.get(i).floatValue();
                spendingEntries.add(new BarEntry(monthLabel, spending));
                if (monthLabel == currentMonth) {
                    colors.add(colorCurrentMonth);
                } else {
                    colors.add(colorOtherMonths);
                }
            }
        } else {
            for (int i = 0; i < listSpendings.size(); i++) {
                float monthLabel = currentMonth - 5 + i;
                float spending = listSpendings.get(i).floatValue();
                spendingEntries.add(new BarEntry(monthLabel, spending));
                if (monthLabel == currentMonth) {
                    colors.add(colorCurrentMonth);
                } else {
                    colors.add(colorOtherMonths);
                }
            }
        }

        barChart.getDescription().setEnabled(false);

        BarDataSet dataSet = new BarDataSet(spendingEntries, "Chi tiêu");
        dataSet.setColors(colors); // Set colors for each bar
        dataSet.setValueTextColor(ContextCompat.getColor(SpendingsActivity.this, R.color.black));
        dataSet.setValueTextSize(13f);
        dataSet.setValueFormatter(new CustomValueMoneyFormatter());

        barChart.getLegend().setEnabled(false);
        BarData barData = new BarData(dataSet);
        barChart.setData(barData);
        barChart.animateY(1000);
        barChart.invalidate(); // Refresh the chart

        // Cấu hình XAxis
        XAxis xAxis = barChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(1f);
        xAxis.setTextSize(13f);
        xAxis.setLabelCount(12);
        xAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                int month = Math.round(value);
                return TimeUtils.getMonth(month);
            }
        });

        // Cấu hình trục Y
        YAxis yAxisLeft = barChart.getAxisLeft();
        yAxisLeft.setDrawLabels(false);
        yAxisLeft.setDrawGridLinesBehindData(false);
        yAxisLeft.setDrawGridLines(false);
        yAxisLeft.setDrawAxisLine(false);
        yAxisLeft.setAxisMinimum(0f);
        yAxisLeft.setTextSize(13f);

        barChart.getAxisRight().setEnabled(false); // Ẩn trục Y phải
        barChart.setBackgroundColor(Color.WHITE); // Đặt màu nền của biểu đồ thành trắng
    }

    private void setupEvents() {
        binding.btnBack.setOnClickListener(v -> finish());
    }

    private void getCustomerID() {
        SharedPreferences sharedPreferences = getSharedPreferences(USER_INFO, MODE_PRIVATE);
        customerID = sharedPreferences.getString(USER_ID, null);
    }


}