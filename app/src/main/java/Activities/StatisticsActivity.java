package Activities;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.stores.R;
import com.example.stores.databinding.ActivityStatisticsBinding;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Objects;

import Models.Product;

public class StatisticsActivity extends AppCompatActivity {




    ActivityStatisticsBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityStatisticsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getWindow().setStatusBarColor(Color.parseColor("#F04D7F"));
        Objects.requireNonNull(getSupportActionBar()).hide();

        initUI();

        binding.btnBack.setOnClickListener(v -> finish());


    }

    private void initUI() {
        ArrayList<PieEntry> entries = new ArrayList<>();
        ArrayList<String> listname = new ArrayList<>();
        listname.add("Lovito Đầm chữ A phối ren hoa đơn giản dành cho nữ LNA38057");
        listname.add("Lovito Đầm trễ vai ngọc trai trơn đơn giản dành cho nữ L76AD154");
        listname.add("Đồng Hồ Điện Tử Chống Nước Phong Cách Quân Đội SANDA 2023 Cho Nam");
        listname.add("Huizumei Váy preppy nữ mùa hè cổ polo nhỏ chắp vá eo nâng cao và giảm béo váy ngắn");
        entries.add(new PieEntry(1, listname.get(0).substring(0,45)));
        entries.add(new PieEntry(2, listname.get(1).substring(0,45)));
        entries.add(new PieEntry(3, listname.get(2).substring(0,45)));
        entries.add(new PieEntry(2, listname.get(3).substring(0,45)));

        PieDataSet dataSet = new PieDataSet(entries, "Top sản phẩm được mua nhiều nhất");
        dataSet.setColors(ColorTemplate.MATERIAL_COLORS);

        dataSet.setValueTextSize(14f); // Adjust the size of the value labels
        PieData data = new PieData(dataSet);
        data.setDrawValues(true);
        dataSet.setValueTextColor(Color.parseColor("#ffffff"));

        PieChart pieChart = binding.pieChart;
        pieChart.getDescription().setEnabled(false);
        pieChart.setData(data);
        pieChart.getLegend().setTextSize(14f);
        pieChart.setEntryLabelTypeface(Typeface.DEFAULT_BOLD);
        pieChart.setEntryLabelTextSize(15f);
        pieChart.setEntryLabelColor(Color.BLACK);

        Legend legend = pieChart.getLegend();
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        legend.setOrientation(Legend.LegendOrientation.VERTICAL);
        legend.setXEntrySpace(0f);
        legend.setForm(Legend.LegendForm.SQUARE);
        legend.setYEntrySpace(6f);
        legend.setXEntrySpace(8f);


        pieChart.setDrawEntryLabels(false);
        pieChart.setEntryLabelColor(Color.parseColor("#333333"));
        pieChart.animateXY(2000, 2000);
        pieChart.invalidate();
    }



}