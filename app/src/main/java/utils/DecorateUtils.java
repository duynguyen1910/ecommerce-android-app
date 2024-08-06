package utils;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.TextView;


import androidx.core.content.ContextCompat;

import com.example.stores.R;

public class DecorateUtils {
    public static void decorateBorderUnSelectedView(View view){
        view.setBackgroundResource(R.drawable.custom_border_unselected);
    }
    public static void decorateBorderSelectedView(View view){
        view.setBackgroundResource(R.drawable.custom_border_primary_color);
    }
    public static void decorateSelectedCompoundButton(Context context, CompoundButton compoundButton){
        compoundButton.setBackgroundResource(R.drawable.custom_border_primary_color);
        compoundButton.setTextColor(ContextCompat.getColor(context, R.color.primary_color));
    }
    public static void decorateUnselectedCompoundButton(Context context, CompoundButton compoundButton){
        compoundButton.setBackgroundColor(Color.parseColor("#EFEFEF"));
        compoundButton.setTextColor(ContextCompat.getColor(context, R.color.black));
    }
    public static void decorateSelectedTextViews(Context context, TextView... views) {
        for (TextView view : views) {
            view.setTextColor(ContextCompat.getColor(context, R.color.primary_color));
        }
    }

    public static void decorateUnselectedTextViews(Context context, TextView... views){
        for (TextView view : views){
            view.setTextColor(ContextCompat.getColor(context, R.color.darkgray));
        }

    }
}
