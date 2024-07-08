package ViewsComponents;



import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;

public class LimitedScrollView extends ScrollView {
    private int maxScrollY;

    public LimitedScrollView(Context context) {
        super(context);
        init(context);
    }

    public LimitedScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public LimitedScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        setMaxScrollYInDp(context, 100); // Đặt giá trị maxScrollY là 100dp
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        if (t > maxScrollY) {
            scrollTo(l, maxScrollY);
        } else {
            super.onScrollChanged(l, t, oldl, oldt);
        }
    }

    public void setMaxScrollYInDp(Context context, int maxScrollYInDp) {
        float density = context.getResources().getDisplayMetrics().density;
        this.maxScrollY = (int) (maxScrollYInDp * density);
    }

}
