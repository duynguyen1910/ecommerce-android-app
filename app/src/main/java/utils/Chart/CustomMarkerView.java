package utils.Chart;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.widget.TextView;
import com.example.stores.R;
import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;

import java.util.ArrayList;

public class CustomMarkerView extends MarkerView {

    private TextView tvContent;
    private ArrayList<String> productNames;

    public CustomMarkerView(Context context, int layoutResource, ArrayList<String> productNames) {
        super(context, layoutResource);
        // Lấy tham chiếu đến TextView trong layout MarkerView
        tvContent = findViewById(R.id.markerTextView);
        this.productNames = productNames;
    }

    @Override
    public void refreshContent(Entry e, Highlight highlight) {
        // Lấy tên sản phẩm từ danh sách dựa trên chỉ số của Entry
        int index = (int) e.getX() - 1; // Lấy chỉ số X của Entry
        for (int i=0; i<productNames.size(); i++){
            String productName = productNames.get(index);
            tvContent.setText("Item: " + productName);
        }
        super.refreshContent(e, highlight);
    }


}
