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
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.stores.R;
import com.example.stores.databinding.FragmentRevenueBinding;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LegendEntry;
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
import api.invoiceApi;
import interfaces.GetAggregateCallback;
import interfaces.GetCollectionCallback;
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
        getRevenueForAllMonths();
        getTotalRevenueInYear();
        getRevenueInMonth(7);
    }
    private void getTotalRevenueInYear() {
        invoiceApi invoiceApi = new invoiceApi();
        invoiceApi.getRevenueByStoreID(g_sStoreID, new GetAggregateCallback() {
            @Override
            public void onSuccess(double revenue) {
                binding.txtRevenueInYear.setText(FormatHelper.formatVND(revenue));
                setupRevenueInMonthChart(revenue);
            }

            @Override
            public void onFailure(String errorMessage) {
                showToast(requireActivity(), errorMessage);
            }
        });

    }

    private void getRevenueForAllMonths(){
        binding.progressBarLineChart.setVisibility(View.VISIBLE);
        invoiceApi mInvoiceApi = new invoiceApi();
        mInvoiceApi.getRevenueForAllMonthsByStoreID(g_sStoreID, new GetCollectionCallback<Double>() {
            @Override
            public void onGetListSuccess(ArrayList<Double> listRevenues) {
                binding.progressBarLineChart.setVisibility(View.GONE);
                drawRevenuesInYear(listRevenues);
            }

            @Override
            public void onGetListFailure(String errorMessage) {
                binding.progressBarLineChart.setVisibility(View.GONE);
                showToast(requireActivity(), errorMessage);

            }
        });
    }

    private void drawRevenuesInYear(ArrayList<Double> listRevenues){
        LineChart lineChart = binding.lineChart;


        // Dữ liệu mẫu cho doanh thu của 12 tháng
        ArrayList<Entry> revenueEntries = new ArrayList<>();
        for (int i = 0; i < 12; i++) {
            float revenue = listRevenues.get(i).floatValue();
            revenueEntries.add(new Entry(i + 1, revenue));
        }


        lineChart.getDescription().setEnabled(false);
        LineDataSet dataSet = new LineDataSet(revenueEntries, "Doanh thu");
        dataSet.setColor(ContextCompat.getColor(requireActivity(), R.color.secondary_color));
        dataSet.setValueTextColor(ContextCompat.getColor(requireActivity(), R.color.black));
        dataSet.setValueTextSize(12f);
        dataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);

        dataSet.setDrawFilled(true);
        dataSet.setFillColor(ContextCompat.getColor(requireActivity(), R.color.secondary_color));
        dataSet.setFillAlpha(100);

        Legend legend = lineChart.getLegend();
        legend.setTextSize(14f);
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        LineData lineData = new LineData(dataSet);
        lineChart.setData(lineData);
        lineChart.invalidate(); // Refresh the chart

        // Cấu hình XAxis
        XAxis xAxis = lineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(1f);
        xAxis.setTextSize(12f);
        xAxis.setLabelCount(12);
        xAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                int month = Math.round(value); // Convert the float to int
                switch (month) {
                    case 1: return "T1";
                    case 2: return "T2";
                    case 3: return "T3";
                    case 4: return "T4";
                    case 5: return "T5";
                    case 6: return "T6";
                    case 7: return "T7";
                    case 8: return "T8";
                    case 9: return "T9";
                    case 10: return "T10";
                    case 11: return "T11";
                    case 12: return "T12";
                    default: return "";
                }
            }
        });


        // Cấu hình YAxis
        YAxis leftAxis = lineChart.getAxisLeft();
        leftAxis.setTextSize(12f);
        leftAxis.setAxisMinimum(0f); // Minimum value for YAxis
        lineChart.getAxisRight().setEnabled(false); // Disable right YAxis
    }



    private void getRevenueInMonth(int month) {
        binding.progressBar.setVisibility(View.VISIBLE);
        invoiceApi invoiceApi = new invoiceApi();
        invoiceApi.getRevenueInAMonthByStoreID(g_sStoreID, month, new GetAggregateCallback() {
            @Override
            public void onSuccess(double revenue) {
                binding.progressBar.setVisibility(View.GONE);
                binding.txtRevenueInMonth.setText(FormatHelper.formatVND(revenue));
                setupRevenueInMonthChart(revenue);
            }

            @Override
            public void onFailure(String errorMessage) {
                binding.progressBar.setVisibility(View.GONE);
                showToast(requireActivity(), errorMessage);
            }
        });

    }

    private void setupRevenueInMonthChart(double revenue) {
        BarChart barChart1 = binding.spendingsChart;
        ArrayList<BarEntry> entries = new ArrayList<>();
        entries.add(new BarEntry(1, (float) revenue));

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