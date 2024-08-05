package utils.Chart;

import com.github.mikephil.charting.formatter.ValueFormatter;

public class CustomValueMoneyFormatter extends ValueFormatter {

    @Override
    public String getFormattedValue(float value) {
        return String.format("%.2f triệu", value / 1000000); // Hiển thị giá trị theo triệu
    }
}
