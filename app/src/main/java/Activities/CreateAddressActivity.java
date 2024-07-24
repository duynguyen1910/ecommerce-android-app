package Activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.stores.databinding.ActivityCreateAddressBinding;

import java.util.Objects;

public class CreateAddressActivity extends AppCompatActivity {

    ActivityCreateAddressBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCreateAddressBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initUI();


        setupEvent();


    }

    private void setupEvent(){
        binding.imageBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        binding.btnComplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        binding.txtSetAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Open một activity để chọn địa chỉ qua API, chọn tỉnh/thành phố --> Quận/Huyện --> Phường/Xã
                // Sau khi chọn xong thì đổi text của textView này
                String newAddress = "TP. Hồ Chí Minh\nQuận 12, Phường Trung Mỹ Tây";
                binding.txtSetAddress.setText(newAddress);
            }
        });
        binding.txtDetailLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Open google Map để chọn địa điểm nếu có thể :)))
                String detailLocation = "FPT Polytechnic TP.HCM - Tòa F,\nCông Viên Phần Mềm Quang Trung, Tòa nhà GenPacific \nLô 3 đường 16";
                binding.txtDetailLocation.setText(detailLocation);
            }
        });
    }

    private void initUI(){
        getWindow().setStatusBarColor(Color.parseColor("#F04D7F"));
        Objects.requireNonNull(getSupportActionBar()).hide();
    }


}