package Activities.StoreSetup;

import android.graphics.Color;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.stores.R;
import com.example.stores.databinding.ActivityRevenueBinding;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LegendEntry;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.Objects;

import utils.Chart.CustomMarkerView;
import utils.Chart.CustomValueMoney2Formatter;
import utils.Chart.CustomValueMoneyFormatter;
import utils.Chart.CustomValueSoldFormatter;

public class RevenueActivity extends AppCompatActivity {


    ActivityRevenueBinding binding;
    private ArrayList<String> productNames;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRevenueBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getWindow().setStatusBarColor(Color.parseColor("#F04D7F"));
        Objects.requireNonNull(getSupportActionBar()).hide();

        initUIBarChart1();
        initUIBarChart2();

        binding.btnBack.setOnClickListener(v -> finish());


    }

    private void initUIBarChart1() {
        BarChart barChart1 = binding.barChart1;
        ArrayList<BarEntry> entries = new ArrayList<>();

        productNames = new ArrayList<>();
        productNames.add("Lovito Đầm chữ A phối ren hoa đơn giản dành cho nữ LNA38057");
        productNames.add("Lovito Đầm trễ vai ngọc trai trơn đơn giản dành cho nữ L76AD154");
        productNames.add("Đồng Hồ Điện Tử Chống Nước Phong Cách Quân Đội SANDA 2023 Cho Nam");
        productNames.add("Huizumei Váy preppy nữ mùa hè cổ polo nhỏ chắp vá eo nâng cao và giảm béo váy ngắn");
        productNames.add("Quần Kaki màu đen Quảng Châu cho Nam");


        entries.add(new BarEntry(1, 10f));
        entries.add(new BarEntry(2, 20f));
        entries.add(new BarEntry(3, 15f));
        entries.add(new BarEntry(4, 25f));
        entries.add(new BarEntry(5, 37f));

        BarDataSet dataSet = new BarDataSet(entries, "Products");
        dataSet.setColors(ColorTemplate.PASTEL_COLORS);
        dataSet.setValueTextColor(Color.BLACK);
        dataSet.setValueTextSize(12f);
        dataSet.setDrawValues(true);

        BarData barData = new BarData(dataSet);
        barChart1.setData(barData);
        barChart1.setFitBars(true);
        barChart1.getDescription().setEnabled(false);
        barChart1.animateY(2000);


        Legend legend = barChart1.getLegend();
        legend.setEnabled(true);

        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        legend.setMaxSizePercent(1.0f);
        legend.setOrientation(Legend.LegendOrientation.VERTICAL);

        legend.setTextSize(12f); // Thay đổi kích thước văn bản
        legend.setFormSize(12f);
        ArrayList<LegendEntry> legendEntries = new ArrayList<>();
        for (int i = 0; i < productNames.size(); i++) {
            LegendEntry entry = new LegendEntry();
            entry.label = productNames.get(i).substring(0, 36) + "...";
            entry.formColor = ColorTemplate.PASTEL_COLORS[i % ColorTemplate.PASTEL_COLORS.length];
            legendEntries.add(entry);
        }
        legend.setCustom(legendEntries);
        barChart1.setExtraOffsets(10f, 45f, 10f, 0f);
        YAxis yAxis = barChart1.getAxisLeft();
        yAxis.setValueFormatter(new CustomValueSoldFormatter());

        // Tạo và thiết lập MarkerView
        CustomMarkerView markerView = new CustomMarkerView(this, R.layout.layout_marker_view, productNames);
        barChart1.setMarker(markerView);

        // Đảm bảo trục y phải được kích hoạt nếu bạn có trục y phải (điều này sẽ tự động hiện ra nếu không có trục y phải)
        barChart1.getAxisRight().setEnabled(false);
    }
    private void initUIBarChart2() {
        BarChart barChart2 = binding.barChart2;
        ArrayList<BarEntry> entries = new ArrayList<>();

        productNames = new ArrayList<>();
        productNames.add("Lovito Đầm chữ A phối ren hoa đơn giản dành cho nữ LNA38057");
        productNames.add("Lovito Đầm trễ vai ngọc trai trơn đơn giản dành cho nữ L76AD154");
        productNames.add("Đồng Hồ Điện Tử Chống Nước Phong Cách Quân Đội SANDA 2023 Cho Nam");
        productNames.add("Huizumei Váy preppy nữ mùa hè cổ polo nhỏ chắp vá eo nâng cao và giảm béo váy ngắn");
        productNames.add("Quần Kaki màu đen Quảng Châu cho Nam");


        entries.add(new BarEntry(1, 10000000f)); // 10 triệu
        entries.add(new BarEntry(2, 20000000f)); // 20 triệu
        entries.add(new BarEntry(3, 15000000f)); // 15 triệu
        entries.add(new BarEntry(4, 25000000f)); // 25 triệu
        entries.add(new BarEntry(5, 35000000f)); // 35 triệu
        BarDataSet dataSet = new BarDataSet(entries, "Products");
        dataSet.setColors(ColorTemplate.JOYFUL_COLORS);
        dataSet.setValueTextColor(Color.BLACK);
        dataSet.setValueTextSize(12f);
        dataSet.setDrawValues(true);
        dataSet.setValueFormatter(new CustomValueMoney2Formatter());
//        dataSet.setValueFormatter(new CustomValueFormatter());
        BarData barData = new BarData(dataSet);
        barChart2.setData(barData);
        barChart2.setFitBars(true);
        barChart2.getDescription().setEnabled(false);
        barChart2.animateY(2000);


        Legend legend = barChart2.getLegend();
        legend.setEnabled(true);

        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        legend.setMaxSizePercent(1.0f);
        legend.setOrientation(Legend.LegendOrientation.VERTICAL);

        legend.setTextSize(12f); // Thay đổi kích thước văn bản
        legend.setFormSize(12f);
        ArrayList<LegendEntry> legendEntries = new ArrayList<>();
        for (int i = 0; i < productNames.size(); i++) {
            LegendEntry entry = new LegendEntry();
            entry.label = productNames.get(i).substring(0, 36) + "...";
            entry.formColor = ColorTemplate.JOYFUL_COLORS[i % ColorTemplate.JOYFUL_COLORS.length];
            legendEntries.add(entry);
        }
        legend.setCustom(legendEntries);
        barChart2.setExtraOffsets(10f, 45f, 10f, 0f);
        YAxis yAxis = barChart2.getAxisLeft();
        yAxis.setValueFormatter(new CustomValueMoneyFormatter());

        // Tạo và thiết lập MarkerView
        CustomMarkerView markerView = new CustomMarkerView(this, R.layout.layout_marker_view, productNames);
        barChart2.setMarker(markerView);

        // Đảm bảo trục y phải được kích hoạt nếu bạn có trục y phải (điều này sẽ tự động hiện ra nếu không có trục y phải)
        barChart2.getAxisRight().setEnabled(false);
    }



}