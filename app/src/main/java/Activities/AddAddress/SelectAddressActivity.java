package Activities.AddAddress;

import static constants.keyName.CATEGORY_ID;
import static constants.keyName.CATEGORY_NAME;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.stores.R;
import com.example.stores.databinding.ActivitySelectAddressBinding;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import models.Address.ApiAddressService;
import models.Address.Phuong;
import models.Address.PhuongXaResponse;
import models.Address.Quan;
import models.Address.QuanHuyenResponse;
import models.Address.Tinh;
import models.Address.TinhThanhResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SelectAddressActivity extends AppCompatActivity {

    ActivitySelectAddressBinding binding;
    ApiAddressService apiAddressService;
    Retrofit retrofit;
    Tinh selectedTinh;
    Quan selectedQuan;
    Phuong selectedPhuong;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySelectAddressBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initUI();
        setupEvents();

        retrofit = new Retrofit.Builder()
                .baseUrl("https://esgoo.net/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        apiAddressService = retrofit.create(ApiAddressService.class);
        loadTinhThanh();


    }


    private void loadTinhThanh() {
        apiAddressService.getTinhThanh().enqueue(new Callback<TinhThanhResponse>() {
            @Override
            public void onResponse(Call<TinhThanhResponse> call, Response<TinhThanhResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Tinh> tinhs = response.body().data;
                    // Chuyển đổi danh sách tỉnh thành thành danh sách tên tỉnh thành
                    List<String> tinhNames = new ArrayList<>();
                    for (Tinh tinh : tinhs) {
                        tinhNames.add(tinh.full_name);
                    }

                    // Cập nhật dữ liệu cho Spinner
                    ArrayAdapter<String> tinhAdapter = new ArrayAdapter<>(SelectAddressActivity.this,
                            android.R.layout.simple_spinner_item, tinhNames);
                    tinhAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    binding.spinnerTinh.setAdapter(tinhAdapter);
                    binding.spinnerTinh.setDropDownVerticalOffset(280);


                    // Đặt sự kiện cho Spinner tỉnh thành
                    binding.spinnerTinh.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            selectedTinh = tinhs.get(position);
                            loadQuanHuyen(selectedTinh.id);
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<TinhThanhResponse> call, Throwable t) {
                // Xử lý lỗi
            }
        });
    }

    private void loadQuanHuyen(String idTinh) {
        apiAddressService.getQuanHuyen(idTinh).enqueue(new Callback<QuanHuyenResponse>() {
            @Override
            public void onResponse(Call<QuanHuyenResponse> call, Response<QuanHuyenResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Quan> quans = response.body().data;
                    // Chuyển đổi danh sách quận huyện thành danh sách tên quận huyện
                    List<String> quanNames = new ArrayList<>();
                    for (Quan quan : quans) {
                        quanNames.add(quan.full_name);
                    }

                    // Cập nhật dữ liệu cho Spinner quận huyện
                    ArrayAdapter<String> quanAdapter = new ArrayAdapter<>(SelectAddressActivity.this,
                            android.R.layout.simple_spinner_item, quanNames);
                    quanAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    binding.spinnerQuan.setAdapter(quanAdapter);
                    binding.spinnerQuan.setDropDownVerticalOffset(150);
                    // Đặt sự kiện cho Spinner quận huyện
                    binding.spinnerQuan.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            // Xử lý khi chọn quận huyện
                            selectedQuan = quans.get(position);
                            loadPhuongXa(selectedQuan.id);
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<QuanHuyenResponse> call, Throwable t) {
                // Xử lý lỗi
            }
        });
    }

    private void loadPhuongXa(String idQuan) {
        apiAddressService.getPhuongXa(idQuan).enqueue(new Callback<PhuongXaResponse>() {
            @Override
            public void onResponse(Call<PhuongXaResponse> call, Response<PhuongXaResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Phuong> phuongs = response.body().data;
                    // Chuyển đổi danh sách phường xã thành danh sách tên phường xã
                    List<String> phuongNames = new ArrayList<>();
                    for (Phuong phuong : phuongs) {
                        phuongNames.add(phuong.full_name);
                    }

                    // Cập nhật dữ liệu cho Spinner phường xã
                    ArrayAdapter<String> phuongAdapter = new ArrayAdapter<>(SelectAddressActivity.this,
                            android.R.layout.simple_spinner_item, phuongNames);
                    phuongAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    binding.spinnerPhuong.setAdapter(phuongAdapter);
                    binding.spinnerPhuong.setDropDownVerticalOffset(280);

                    binding.spinnerPhuong.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            selectedPhuong = phuongs.get(position);
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<PhuongXaResponse> call, Throwable t) {
                // Xử lý lỗi
            }
        });
    }

    private void setupEvents() {
        binding.btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String address = selectedTinh.getFull_name() + "\n" + selectedQuan.getFull_name() + "\n" + selectedPhuong.getFull_name();
                Intent intent = new Intent();
                intent.putExtra("address", address);
                setResult(2, intent);

                finish();
            }
        });
        binding.imageBack.setOnClickListener(v -> finish());
    }

    private void initUI() {
        getWindow().setStatusBarColor(Color.parseColor("#F04D7F"));
        Objects.requireNonNull(getSupportActionBar()).hide();
    }


}