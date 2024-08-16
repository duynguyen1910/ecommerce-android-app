package utils.Chart;

import com.github.mikephil.charting.formatter.ValueFormatter;

public class CustomValueMoneyFormatter extends ValueFormatter {

    @Override
    public String getFormattedValue(float value) {
        if (value == 0) {
            return ""; // Không hiển thị giá trị khi nó bằng 0
        }
        if (value < 1000000) {
            return String.format("%.0fk", value / 1000);
        }
        if (value < 1000000000 ){
            return String.format("%.1fTr", value / 1000000);
        }
        return String.format("%.1fB", value / 1000000000);
    }
}
