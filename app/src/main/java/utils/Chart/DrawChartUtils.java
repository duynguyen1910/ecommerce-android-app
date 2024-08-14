package utils.Chart;

import android.content.Context;
import android.graphics.Color;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.example.stores.R;
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

import java.util.ArrayList;
import java.util.List;

import utils.FormatHelper;

public class DrawChartUtils {
    public static void drawSpendingBarChart(Context context, ArrayList<Double> listData, int currentMonth, BarChart barChart) {
        List<BarEntry> barEntries = new ArrayList<>();
        List<Integer> colors = new ArrayList<>();
        int colorCurrentMonth = ContextCompat.getColor(context, R.color.primary_color);
        int colorOtherMonths = ContextCompat.getColor(context, R.color.light_light_primary);

        if (currentMonth <= 6) {
            for (int i = 0; i < listData.size(); i++) {
                float monthLabel = i + 1;
                float value = listData.get(i).floatValue();
                barEntries.add(new BarEntry(monthLabel, value));
                if (monthLabel == currentMonth) {
                    colors.add(colorCurrentMonth);
                } else {
                    colors.add(colorOtherMonths);
                }
            }
        } else {
            for (int i = 0; i < listData.size(); i++) {
                float monthLabel = currentMonth - 5 + i;
                float value = listData.get(i).floatValue();
                barEntries.add(new BarEntry(monthLabel, value));
                if (monthLabel == currentMonth) {
                    colors.add(colorCurrentMonth);
                } else {
                    colors.add(colorOtherMonths);
                }
            }
        }

        barChart.getDescription().setEnabled(false);

        BarDataSet dataSet = new BarDataSet(barEntries, "Bar Chart");
        dataSet.setColors(colors);
        dataSet.setValueTextColor(ContextCompat.getColor(context, R.color.black));
        dataSet.setValueTextSize(14f);
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
        xAxis.setTextSize(14f);
        xAxis.setLabelCount(12);
        xAxis.setValueFormatter(new MonthNameFormatter());


        YAxis yAxisLeft = barChart.getAxisLeft();
        yAxisLeft.setDrawLabels(false);
        yAxisLeft.setDrawGridLinesBehindData(false);
        yAxisLeft.setDrawGridLines(false);
        yAxisLeft.setDrawAxisLine(false);
        yAxisLeft.setAxisMinimum(0f);
        yAxisLeft.setTextSize(14f);

        barChart.getAxisRight().setEnabled(false);
        barChart.setBackgroundColor(Color.WHITE);
    }


    public static void drawRevenuesLineChart(Context context, ArrayList<Double> listData, LineChart lineChart) {

        ArrayList<Entry> lineEntries = new ArrayList<>();
        for (int i = 0; i < 12; i++) {
            float monthLabel = i + 1;
            float value = listData.get(i).floatValue();
            lineEntries.add(new Entry(monthLabel, value));
        }

        lineChart.getDescription().setEnabled(false);
        LineDataSet dataSet = new LineDataSet(lineEntries, "Line Chart");
        dataSet.setColor(ContextCompat.getColor(context, R.color.secondary_color));
        dataSet.setValueTextColor(ContextCompat.getColor(context, R.color.black));
        dataSet.setValueTextSize(12f);
        dataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        dataSet.setValueFormatter(new CustomValueMoneyFormatter());

        dataSet.setDrawFilled(true);
        dataSet.setFillColor(ContextCompat.getColor(context, R.color.secondary_color));
        dataSet.setFillAlpha(100);

        lineChart.getLegend().setEnabled(false);
        LineData lineData = new LineData(dataSet);
        lineChart.setData(lineData);
        lineChart.animateY(1000);
        lineChart.invalidate();


        XAxis xAxis = lineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(1f);
        xAxis.setTextSize(12f);
        xAxis.setLabelCount(12);
        xAxis.setValueFormatter(new MonthValueFormatter());

        YAxis yAxisLeft = lineChart.getAxisLeft();
        yAxisLeft.setDrawLabels(false);
        yAxisLeft.setDrawGridLinesBehindData(false);
        yAxisLeft.setDrawGridLines(false);
        yAxisLeft.setDrawAxisLine(false);
        yAxisLeft.setAxisMinimum(0f);
        yAxisLeft.setTextSize(12f);


        lineChart.getAxisRight().setEnabled(false);
        lineChart.setBackgroundColor(Color.WHITE);
    }

    public static void drawRevenuesBarChart(Context context, ArrayList<Double> listData, int currentMonth, BarChart barChart) {

        List<BarEntry> barEntries = new ArrayList<>();
        List<Integer> colors = new ArrayList<>();
        int colorCurrentMonth = ContextCompat.getColor(context, R.color.primary_color);
        int colorOtherMonths = ContextCompat.getColor(context, R.color.light_light_primary);

        for (int i = 0; i < 12; i++) {
            float monthLabel = i + 1;
            float value = listData.get(i).floatValue();
            barEntries.add(new BarEntry(monthLabel, value));
            if (monthLabel == currentMonth) {
                colors.add(colorCurrentMonth);
            } else {
                colors.add(colorOtherMonths);
            }
        }

        barChart.getDescription().setEnabled(false);

        BarDataSet dataSet = new BarDataSet(barEntries, "Bar Chart");
        dataSet.setColors(colors);
        dataSet.setValueTextColor(ContextCompat.getColor(context, R.color.black));
        dataSet.setValueTextSize(12f);
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
        xAxis.setTextSize(12f);
        xAxis.setLabelCount(12);
        xAxis.setValueFormatter(new MonthValueFormatter());


        YAxis yAxisLeft = barChart.getAxisLeft();
        yAxisLeft.setDrawLabels(false);
        yAxisLeft.setDrawGridLinesBehindData(false);
        yAxisLeft.setDrawGridLines(false);
        yAxisLeft.setDrawAxisLine(false);
        yAxisLeft.setAxisMinimum(0f);
        yAxisLeft.setTextSize(12f);

        barChart.getAxisRight().setEnabled(false);
        barChart.setBackgroundColor(Color.WHITE);
    }

    public static void compareRevenueToLastMonth(Context context, TextView textView, ArrayList<Double> listRevenues, int currentMonth) {

        double currentRevenue = listRevenues.get(currentMonth - 1);
        double lastMonthRevenue = listRevenues.get(currentMonth - 2);
        if (lastMonthRevenue == 0) {
            textView.setText("Không có dữ liệu tháng trước");
            return;
        }
        double percent = (currentRevenue / lastMonthRevenue) * 100 - 100;
        double remain = currentRevenue - lastMonthRevenue;
        if (percent > 1){
            textView.setText("Tăng " + String.format("%.2f", percent) + "% (+" + FormatHelper.formatVND(remain) + ")");
            textView.setTextColor(ContextCompat.getColor(context, R.color.green));
        }else {
            textView.setText("Giảm " + String.format("%.2f", -percent) + "% (" + FormatHelper.formatVND(remain) + ")");
            textView.setTextColor(ContextCompat.getColor(context, R.color.primary_color));
        }
    }

}
