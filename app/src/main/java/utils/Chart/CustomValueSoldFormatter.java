package utils.Chart;

import com.github.mikephil.charting.formatter.ValueFormatter;

public class CustomValueSoldFormatter extends ValueFormatter {

    @Override
    public String getFormattedValue(float value) {
        return String.format("%.0f đã bán", value); // Hiển thị giá trị theo triệu
    }
}
