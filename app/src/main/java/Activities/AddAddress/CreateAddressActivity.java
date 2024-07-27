package Activities.AddAddress;

import static constants.keyName.CATEGORY_ID;
import static constants.keyName.CATEGORY_NAME;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
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

    ActivityResultLauncher<Intent> launcherSelectAddress = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == 2) {
                    Intent intent = result.getData();
                    if (intent != null) {
                        String address = intent.getStringExtra("address");
                        if (address != null) {
                            binding.txtSetAddress.setText(address);
                        }
                    }
                }
            });

    private void setupEvent() {
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
//                String newAddress = "TP. Hồ Chí Minh\nQuận 12, Phường Trung Mỹ Tây";
//                binding.txtSetAddress.setText(newAddress);

                Intent intent = new Intent(CreateAddressActivity.this, SelectAddressActivity.class);
                launcherSelectAddress.launch(intent);
            }
        });
        binding.txtDetailLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    private void initUI() {
        getWindow().setStatusBarColor(Color.parseColor("#F04D7F"));
        Objects.requireNonNull(getSupportActionBar()).hide();
    }


}