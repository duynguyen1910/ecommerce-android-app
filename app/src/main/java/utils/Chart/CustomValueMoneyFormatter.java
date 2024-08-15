package utils.Chart;

import com.github.mikephil.charting.formatter.ValueFormatter;

public class CustomValueMoneyFormatter extends ValueFormatter {

    @Override
    public String getFormattedValue(float value) {
        if (value < 1000000) {
            return String.format("%.0f k", value / 1000);
        }
        return String.format("%.1f Tr", value / 1000000);
    }
}
