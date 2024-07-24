package Activities;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.example.stores.R;
import com.example.stores.databinding.ActivityAddVariantBinding;
import com.example.stores.databinding.DialogAddVariantBinding;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Objects;

public class AddVariantActivity extends AppCompatActivity {

    ActivityAddVariantBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddVariantBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initUI();
        setupEvents();


    }

    private void setupEvents() {
        binding.imageBack.setOnClickListener(v -> finish());
        binding.layoutAddVariant.setOnClickListener(v -> {
            popUpChooseVariantDialog();
        });


    }

    private void popUpChooseVariantDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        DialogAddVariantBinding dialogBinding = DialogAddVariantBinding.inflate(getLayoutInflater());
        builder.setView(dialogBinding.getRoot());
        AlertDialog dialog = builder.create();

        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawableResource(R.drawable.custom_edit_text_border);
        dialog.show();


        Window window = dialog.getWindow();
        if (window != null) {
            window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            window.setGravity(Gravity.BOTTOM);

            Animation slideUp = AnimationUtils.loadAnimation(this, R.anim.slide_up);
            dialogBinding.getRoot().startAnimation(slideUp);
        }

        dialogBinding.imageClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialogBinding.txtSize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialogBinding.txtColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialogBinding.txtGender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialogBinding.txtCustomVariant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });


    }

    private void initUI() {
        getWindow().setStatusBarColor(Color.parseColor("#F04D7F"));
        Objects.requireNonNull(getSupportActionBar()).hide();
    }

    private static ArrayList<String> setupColor() {
        String[] colorArray = {
                "Đỏ", "Cam", "Vàng", "Xanh lá", "Xanh dương",
                "Tím", "Hồng", "Tím nhạt", "Trắng", "Bạc", "Nâu",
                "Màu kaki", "Đen", "Vàng gold", "Xám tro", "Be",
                "Hồng dâu", "Nâu cafe", "Vàng đồng", "Nâu đậm",
                "Xanh lục bảo", "Xám", "Nâu nhạt", "Đen nhám", "Xanh rêu",
                "Tím than", "Trắng kem", "Xanh ngọc bích", "Xanh lá đậm",
                "Bạc ánh kim", "Xám lông chuột", "Bạch kim", "Xám khói"
        };
        return new ArrayList<>(Arrays.asList(colorArray));
    }

    private static HashMap<String, ArrayList<String>> setupSize() {
        String[] sizeVNArray = {
                "34", "35", "36", "37", "38", "39", "40", "41",
                "42", "43", "44", "45", "46", "47", "48", "49"
        };
        String[] sizeGlobalArray = {
                "Free Size", "L", "M", "S", "XL", "XS", "XXL", "XXS"
        };
        HashMap<String, ArrayList<String>> sizeMap = new HashMap<>();
        sizeMap.put("sizeVNArray", new ArrayList<>(Arrays.asList(sizeVNArray)));
        sizeMap.put("sizeGlobalArray", new ArrayList<>(Arrays.asList(sizeGlobalArray)));
        return sizeMap;
    }

    private static ArrayList<String> setupGender() {
        String[] genderArray = {"Bé trai", "Bé gái", "Nam", "Nữ", "Unisex"};
        return new ArrayList<>(Arrays.asList(genderArray));
    }


}