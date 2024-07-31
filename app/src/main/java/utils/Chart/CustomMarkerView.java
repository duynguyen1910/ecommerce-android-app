package utils.Chart;
import android.content.Context;
import android.widget.TextView;
import com.example.stores.R;
import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;

import java.util.ArrayList;

public class CustomMarkerView extends MarkerView {

    private final TextView tvContent;
    private final ArrayList<String> productNames;

    public CustomMarkerView(Context context, int layoutResource, ArrayList<String> productNames) {
        super(context, layoutResource);
        tvContent = findViewById(R.id.markerTextView);
        this.productNames = productNames;
    }


    @Override
    public void refreshContent(Entry e, Highlight highlight) {
        int index = (int) e.getX() - 1;
        for (int i=0; i<productNames.size(); i++){
            String productName = productNames.get(index);
            tvContent.setText("Item: " + productName);
        }
        super.refreshContent(e, highlight);
    }
}
