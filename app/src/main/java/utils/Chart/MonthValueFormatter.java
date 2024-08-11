package utils.Chart;

import com.github.mikephil.charting.formatter.ValueFormatter;

public class MonthValueFormatter extends ValueFormatter {
    @Override
    public String getFormattedValue(float value) {
        int month = Math.round(value);
        return TimeUtils.getMonthValue(month);
    }
}
