package Activities;

import static constants.keyName.USER_ID;
import static constants.keyName.USER_INFO;
import static utils.Cart.CartUtils.showToast;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.stores.R;
import com.example.stores.databinding.ActivitySpendingsBinding;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import java.sql.Time;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import api.invoiceApi;
import interfaces.GetAggregateCallback;
import interfaces.GetCollectionCallback;
import interfaces.GetManyAggregateCallback;
import utils.Chart.CustomValueMoneyFormatter;
import utils.Chart.MonthNameFormatter;
import utils.Chart.TimeUtils;


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
        int m_currentMonth = TimeUtils.getCurrentMonthValue();
        int m_currentYear = TimeUtils.getCurrentYearValue();

        binding.txtCurrentMonth.setText(TimeUtils.setMonthText(m_currentYear, m_currentMonth));

        m_invoiceApi.getSpendingsInHaftYearByCustomerID(customerID, m_currentYear, m_currentMonth, new GetCollectionCallback<Double>() {
            @Override
            public void onGetListSuccess(ArrayList<Double> listSpendings) {
                binding.progressBar.setVisibility(View.GONE);
                drawSpendingInHaftYear(listSpendings, m_currentMonth);
            }

            @Override
            public void onGetListFailure(String errorMessage) {
                binding.progressBar.setVisibility(View.GONE);
                showToast(SpendingsActivity.this, errorMessage);

            }
        });

        m_invoiceApi.getCountOfInvoiceInAMonth(customerID, m_currentYear, m_currentMonth, new GetManyAggregateCallback() {
            @Override
            public void onSuccess(double... aggregateResults) {
                float currentMonthSpending = (float) aggregateResults[0];
                int countMonthInvoice = (int) aggregateResults[1];

                binding.txtCurrentMonthSpending.setText(new CustomValueMoneyFormatter().getFormattedValue(currentMonthSpending));
                binding.txtCurrentMonthCountInvoice.setText(countMonthInvoice + " đơn");
            }

            @Override
            public void onFailure(String errorMessage) {
                Log.d("getCountOfInvoiceInAMonth", errorMessage);
            }
        });

    }


    private void drawSpendingInHaftYear(ArrayList<Double> listSpendings, int currentMonth) {
        BarChart barChart = binding.spendingsChart;
        List<BarEntry> spendingEntries = new ArrayList<>();
        List<Integer> colors = new ArrayList<>();
        int colorCurrentMonth = ContextCompat.getColor(SpendingsActivity.this, R.color.primary_color);
        int colorOtherMonths = ContextCompat.getColor(SpendingsActivity.this, R.color.light_light_primary);

        if (currentMonth <= 6) {
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
        dataSet.setColors(colors);
        dataSet.setValueTextColor(ContextCompat.getColor(SpendingsActivity.this, R.color.black));
        dataSet.setValueTextSize(13f);
        dataSet.setValueFormatter(new CustomValueMoneyFormatter());

        barChart.getLegend().setEnabled(false);
        BarData barData = new BarData(dataSet);
        barChart.setData(barData);
        barData.setBarWidth(0.5f);
        barChart.animateY(1000);
        barChart.invalidate();


        XAxis xAxis = barChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(1f);
        xAxis.setTextSize(13f);
        xAxis.setLabelCount(12);
        xAxis.setValueFormatter(new MonthNameFormatter());


        YAxis yAxisLeft = barChart.getAxisLeft();
        yAxisLeft.setDrawLabels(false);
        yAxisLeft.setDrawGridLinesBehindData(false);
        yAxisLeft.setDrawGridLines(false);
        yAxisLeft.setDrawAxisLine(false);
        yAxisLeft.setAxisMinimum(0f);
        yAxisLeft.setTextSize(13f);

        barChart.getAxisRight().setEnabled(false);
        barChart.setBackgroundColor(Color.WHITE);
    }

    private void setupEvents() {
        binding.btnBack.setOnClickListener(v -> finish());
    }

    private void getCustomerID() {
        SharedPreferences sharedPreferences = getSharedPreferences(USER_INFO, MODE_PRIVATE);
        customerID = sharedPreferences.getString(USER_ID, null);
    }


}