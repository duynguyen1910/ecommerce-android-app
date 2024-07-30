package utils.Chart;

import com.github.mikephil.charting.formatter.ValueFormatter;

public class CustomValueMoney2Formatter extends ValueFormatter {

    @Override
    public String getFormattedValue(float value) {
        return String.format("%.1f", value / 1000000); // Hiển thị giá trị theo triệu
    }
}
