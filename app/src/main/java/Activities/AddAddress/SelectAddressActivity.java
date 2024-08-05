package Activities.AddAddress;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.stores.databinding.ActivitySelectAddressBinding;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import interfaces.ApiAddressService;

import models.AddressesResponse;
import models.Addresses;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SelectAddressActivity extends AppCompatActivity {
    ActivitySelectAddressBinding binding;
    private FusedLocationProviderClient fusedLocationClient;
    private final static int REQUEST_CODE = 100;
    ApiAddressService apiAddressService;
    Retrofit retrofit;
    Addresses provinceSelected;
    Addresses districtSelected;
    Addresses wardSelected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySelectAddressBinding.inflate(getLayoutInflater());
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        setContentView(binding.getRoot());

        initUI();
        setupEvents();

        retrofit = new Retrofit.Builder()
                .baseUrl("https://esgoo.net/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        apiAddressService = retrofit.create(ApiAddressService.class);

        onLoadProvinceList();
    }

    private void getLocation() {
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this,
                        Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            askPermission();
        } else {
            fusedLocationClient.getLastLocation()
                    .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            if(location != null) {
                                Geocoder geocoder = new Geocoder(SelectAddressActivity.this, Locale.getDefault());
                                List<Address>  addresses = null;
                                try {
                                    addresses = geocoder.getFromLocation(
                                            location.getLatitude(), location.getLongitude(), 1);

                                    Log.d("TAG", "getLatitude: " + addresses.get(0).getLatitude());
                                    Log.d("TAG", "getLongitude: " + addresses.get(0).getLongitude());
                                    Log.d("TAG", "Address: " + addresses.get(0).getAddressLine(0));
                                    Log.d("TAG", "getLocality: " + addresses.get(0).getLocality());
                                    Log.d("TAG", "getCountryName: " + addresses.get(0).getCountryName());
                                } catch (IOException e) {
                                    throw new RuntimeException(e);
                                }
                            }
                        }
                    });
        }
    }

    private void askPermission() {
        ActivityCompat.requestPermissions(SelectAddressActivity.this,
                new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLocation();
            } else {
                Toast.makeText(this, "Required Permission", Toast.LENGTH_SHORT).show();
            }
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void onLoadProvinceList() {
        apiAddressService.getTinhThanh().enqueue(new Callback<AddressesResponse>() {
            @Override
            public void onResponse(Call<AddressesResponse> call, Response<AddressesResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Addresses> provinceList = response.body().data;

                    ArrayAdapter<Addresses> tinhAdapter = new ArrayAdapter<>(SelectAddressActivity.this,
                            android.R.layout.simple_spinner_item, provinceList);
                    tinhAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    binding.provinceSpinner.setAdapter(tinhAdapter);
                    binding.provinceSpinner.setDropDownVerticalOffset(280);

                    binding.provinceSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            provinceSelected = provinceList.get(position);
                            onLoadDistrictList(provinceSelected.getID());
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<AddressesResponse> call, Throwable t) {
                Toast.makeText(SelectAddressActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void onLoadDistrictList(String provinceID) {
        apiAddressService.getQuanHuyen(provinceID).enqueue(new Callback<AddressesResponse>() {
            @Override
            public void onResponse(Call<AddressesResponse> call, Response<AddressesResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Addresses> districtList = response.body().data;

                    ArrayAdapter<Addresses> quanAdapter = new ArrayAdapter<>(SelectAddressActivity.this,
                            android.R.layout.simple_spinner_item, districtList);
                    quanAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    binding.districtSpinner.setAdapter(quanAdapter);
                    binding.districtSpinner.setDropDownVerticalOffset(150);

                    binding.districtSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            districtSelected = districtList.get(position);
                            onLoadWardList(districtSelected.getID());
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<AddressesResponse> call, Throwable t) {
                Toast.makeText(SelectAddressActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void onLoadWardList(String districtID) {
        apiAddressService.getPhuongXa(districtID).enqueue(new Callback<AddressesResponse>() {
            @Override
            public void onResponse(Call<AddressesResponse> call, Response<AddressesResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Addresses> wardList = response.body().data;

                    ArrayAdapter<Addresses> phuongAdapter = new ArrayAdapter<>(SelectAddressActivity.this,
                            android.R.layout.simple_spinner_item, wardList);
                    phuongAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    binding.wardSpinner.setAdapter(phuongAdapter);
                    binding.wardSpinner.setDropDownVerticalOffset(280);

                    binding.wardSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            wardSelected = wardList.get(position);
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<AddressesResponse> call, Throwable t) {
                Toast.makeText(SelectAddressActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void setupEvents() {
        binding.btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("provinceSelected", provinceSelected);
                intent.putExtra("districtSelected", districtSelected);
                intent.putExtra("wardSelected", wardSelected);

                setResult(2, intent);
                finish();
            }
        });
        binding.imageBack.setOnClickListener(v -> finish());

        binding.getLocationBtn.setOnClickListener(v -> {
            getLocation();
        });
    }

    private void initUI() {
        getWindow().setStatusBarColor(Color.parseColor("#F04D7F"));
        Objects.requireNonNull(getSupportActionBar()).hide();
    }


}