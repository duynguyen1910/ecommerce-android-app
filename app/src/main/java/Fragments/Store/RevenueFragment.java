package Fragments.Store;

import static android.content.Context.MODE_PRIVATE;
import static constants.keyName.STORE_ID;
import static constants.keyName.USER_INFO;
import static utils.Cart.CartUtils.showToast;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.stores.databinding.FragmentRevenueBinding;
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

import Activities.SpendingsActivity;
import api.invoiceApi;
import interfaces.GetAggregateCallback;
import utils.Chart.CustomValueMoneyFormatter;
import utils.FormatHelper;

public class RevenueFragment extends Fragment {
    FragmentRevenueBinding binding;
    String g_sStoreID;

    @Nullable
    @Override

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentRevenueBinding.inflate(getLayoutInflater());
        getStoreID();
        setupEvents();
        return binding.getRoot();
    }


    @Override
    public void onResume() {
        super.onResume();
        getRevenueInYear();
        getRevenueInMonth(7);
    }
    private void getRevenueInYear() {
        invoiceApi invoiceApi = new invoiceApi();
        invoiceApi.getRevenueByStoreID(g_sStoreID, new GetAggregateCallback() {
            @Override
            public void onSuccess(double spendings) {
                binding.txtRevenueInYear.setText(FormatHelper.formatVND(spendings));
                setupSpendingChart(spendings);
            }

            @Override
            public void onFailure(String errorMessage) {
                showToast(requireActivity(), errorMessage);
            }
        });

    }



    private void getRevenueInMonth(int month) {
        binding.progressBar.setVisibility(View.VISIBLE);
        invoiceApi invoiceApi = new invoiceApi();
        invoiceApi.getRevenueInAMonthByStoreID(g_sStoreID, month, new GetAggregateCallback() {
            @Override
            public void onSuccess(double spendings) {
                binding.progressBar.setVisibility(View.GONE);
                binding.txtRevenueInMonth.setText(FormatHelper.formatVND(spendings));
                setupSpendingChart(spendings);
            }

            @Override
            public void onFailure(String errorMessage) {
                binding.progressBar.setVisibility(View.GONE);
                showToast(requireActivity(), errorMessage);
            }
        });

    }

    private void setupSpendingChart(double spendings) {
        BarChart barChart1 = binding.spendingsChart;
        ArrayList<BarEntry> entries = new ArrayList<>();
        entries.add(new BarEntry(1, (float) spendings));

        BarDataSet dataSet = new BarDataSet(entries, "Products");
        dataSet.setColors(ColorTemplate.JOYFUL_COLORS);
        dataSet.setValueTextColor(Color.BLACK);
        dataSet.setValueTextSize(14f);
        dataSet.setDrawValues(true);

        BarData barData = new BarData(dataSet);
        barChart1.setData(barData);
        barData.setBarWidth(0.4f);

        barChart1.setFitBars(true);
        barChart1.getDescription().setEnabled(false);
        barChart1.animateY(2000);

        setupBarChart(barChart1);

        ArrayList<LegendEntry> legendEntries = new ArrayList<>();

        LegendEntry entry = new LegendEntry();
        entry.label = "Doanh thu";
        entry.formColor = ColorTemplate.JOYFUL_COLORS[0 % ColorTemplate.JOYFUL_COLORS.length];
        legendEntries.add(entry);
        barChart1.getLegend().setCustom(legendEntries);
        barChart1.setExtraOffsets(10f, 80f, 10f, 40f);

        YAxis yAxis = barChart1.getAxisLeft();
        yAxis.setTextSize(12f);
        yAxis.setValueFormatter(new CustomValueMoneyFormatter());


        barChart1.invalidate();
    }

    private void setupBarChart(BarChart barChart) {
        Legend legend = barChart.getLegend();
        legend.setEnabled(true);
        legend.setWordWrapEnabled(true);
        legend.setDirection(Legend.LegendDirection.LEFT_TO_RIGHT);
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        legend.setYOffset(0f);
        legend.setOrientation(Legend.LegendOrientation.VERTICAL);
        legend.setTextSize(14f);
        legend.setFormSize(14f);
        legend.setDrawInside(false);

        barChart.setFitBars(true);
        barChart.getDescription().setEnabled(false);
        barChart.animateY(2000);

        YAxis yAxisLeft = barChart.getAxisLeft();
        yAxisLeft.setAxisMinimum(0f);
        yAxisLeft.setDrawZeroLine(true);
        yAxisLeft.setDrawAxisLine(true);

        // Thiết lập trục X
        XAxis xAxis = barChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM); // Đặt trục X ở phía dưới
        xAxis.setGranularity(1f); // Đảm bảo các nhãn được phân bố đều
        xAxis.setLabelRotationAngle(20f); // Xoay nhãn để tránh chồng chéo
        xAxis.setLabelCount(5, true); // Thiết lập số lượng nhãn
        xAxis.setTextSize(12f);


        barChart.getAxisRight().setEnabled(false);
    }

    private void getStoreID() {
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences(USER_INFO, MODE_PRIVATE);
        g_sStoreID = sharedPreferences.getString(STORE_ID, null);
    }
    private void setupEvents() {
        String[] calendarMonths = {
                "Tháng 1", "Tháng 2", "Tháng 3",
                "Tháng 4", "Tháng 5", "Tháng 6",
                "Tháng 7", "Tháng 8", "Tháng 9",
                "Tháng 10", "Tháng 11", "Tháng 12"};
        ArrayAdapter<String> monthsAdapter = new ArrayAdapter<>(requireActivity(),
                android.R.layout.simple_spinner_item, calendarMonths);
        monthsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spnSelectMonth.setAdapter(monthsAdapter);
        binding.spnSelectMonth.setSelection(6); // July
        binding.spnSelectMonth.setDropDownVerticalOffset(100);
        binding.spnSelectMonth.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                binding.txtSelectedMonth.setText(calendarMonths[position] + " / 2024");
                getRevenueInMonth(position + 1);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }


}