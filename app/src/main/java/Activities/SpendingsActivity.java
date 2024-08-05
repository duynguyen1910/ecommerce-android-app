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

import com.example.stores.databinding.ActivitySpendingsBinding;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LegendEntry;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.Objects;

import api.invoiceApi;
import interfaces.GetAggregateCallback;
import utils.Chart.CustomValueMoneyFormatter;
import utils.FormatHelper;

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
        getSpendings(8);
    }

    private void initUI() {
        getWindow().setStatusBarColor(Color.parseColor("#F04D7F"));
        Objects.requireNonNull(getSupportActionBar()).hide();

    }

    private void getSpendings(int month) {
        binding.progressBar.setVisibility(View.VISIBLE);
        invoiceApi invoiceApi = new invoiceApi();
        invoiceApi.getSpendingsInAMonthByCustomerID(customerID, month, new GetAggregateCallback() {
            @Override
            public void onSuccess(double spendings) {
                binding.progressBar.setVisibility(View.GONE);
                binding.txtSpendings.setText(FormatHelper.formatVND(spendings));
                setupSpendingChart(spendings);
            }

            @Override
            public void onFailure(String errorMessage) {
                binding.progressBar.setVisibility(View.GONE);
                showToast(SpendingsActivity.this, errorMessage);
            }
        });

    }

    private void setupSpendingChart(double spendings) {
        BarChart barChart1 = binding.spendingsChart;
        ArrayList<BarEntry> entries = new ArrayList<>();
        entries.add(new BarEntry(1, (float) spendings));

        BarDataSet dataSet = new BarDataSet(entries, "Chi tiêu");
        dataSet.setColors(ColorTemplate.JOYFUL_COLORS);
        dataSet.setValueTextColor(Color.BLACK);
        dataSet.setValueTextSize(14f);
        dataSet.setValueFormatter(new CustomValueMoneyFormatter());
        dataSet.setDrawValues(true); // Hiển thị giá trị trên cột dữ liệu

        BarData barData = new BarData(dataSet);
        barChart1.setData(barData);
        barData.setBarWidth(0.2f);

        barChart1.setFitBars(true);
        barChart1.getDescription().setEnabled(false);
        barChart1.animateY(2000);

        setupBarChart(barChart1);


        barChart1.getAxisLeft().setDrawLabels(false); // Ẩn nhãn trục Y
        barChart1.getAxisLeft().setDrawGridLines(false); // Ẩn đường lưới trục Y
        barChart1.getXAxis().setEnabled(false); // Ẩn trục X
        barChart1.getAxisRight().setEnabled(false); // Ẩn trục Y phải
        barChart1.setBackgroundColor(Color.WHITE); // Đặt màu nền của biểu đồ thành trắng

        barChart1.invalidate();
    }

    private void setupBarChart(BarChart barChart) {
        barChart.getLegend().setEnabled(false);

        barChart.setFitBars(true);
        barChart.getDescription().setEnabled(false);
        barChart.animateY(2000);

        YAxis yAxisLeft = barChart.getAxisLeft();
        yAxisLeft.setAxisMinimum(0f);
        yAxisLeft.setDrawZeroLine(true);
        yAxisLeft.setDrawAxisLine(true);
        yAxisLeft.setDrawLabels(false); // Ẩn nhãn trục Y

        // Thiết lập trục X
        XAxis xAxis = barChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM); // Đặt trục X ở phía dưới
        xAxis.setGranularity(1f); // Đảm bảo các nhãn được phân bố đều
        xAxis.setLabelRotationAngle(20f); // Xoay nhãn để tránh chồng chéo
        xAxis.setLabelCount(5, true); // Thiết lập số lượng nhãn
        xAxis.setTextSize(12f);
        xAxis.setDrawLabels(false); // Ẩn nhãn trục X

        barChart.getAxisRight().setEnabled(false);
    }

    private void setupEvents() {
        String[] calendarMonths = {
                "Tháng 1", "Tháng 2", "Tháng 3",
                "Tháng 4", "Tháng 5", "Tháng 6",
                "Tháng 7", "Tháng 8", "Tháng 9",
                "Tháng 10", "Tháng 11", "Tháng 12"};
        ArrayAdapter<String> monthsAdapter = new ArrayAdapter<>(SpendingsActivity.this,
                android.R.layout.simple_spinner_item, calendarMonths);
        monthsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spnSelectMonth.setAdapter(monthsAdapter);
        binding.spnSelectMonth.setSelection(7); // August
        binding.spnSelectMonth.setDropDownVerticalOffset(100);
        binding.spnSelectMonth.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                binding.txtSelectedMonth.setText(calendarMonths[position] + " / 2024");
                getSpendings(position + 1);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        binding.btnBack.setOnClickListener(v -> finish());
    }

    private void getCustomerID() {
        SharedPreferences sharedPreferences = getSharedPreferences(USER_INFO, MODE_PRIVATE);
        customerID = sharedPreferences.getString(USER_ID, null);
    }


}