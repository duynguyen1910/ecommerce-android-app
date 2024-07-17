package Fragments.StoreSettings;

import static android.content.Context.MODE_PRIVATE;
import static constants.keyName.PHONE_NUMBER;
import static constants.keyName.USER_INFO;
import static constants.toastMessage.DEFAULT_REQUIRE;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.stores.R;
import com.example.stores.databinding.FragmentStoreInfoBinding;

import java.util.Objects;

import Activities.CreateAddressActivity;

public class StoreInfoFragment extends Fragment {
    FragmentStoreInfoBinding binding;
    private SharedPreferences sharedPreferences;

    @Nullable
    @Override

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentStoreInfoBinding.inflate(getLayoutInflater());
//        initUI();
        setupEvent();

        return binding.getRoot();
    }

//    private void initUI(){
//        // lấy các thông tin của user bao gồm phoneNumber, Address, Email
//
//        sharedPreferences = requireContext().getSharedPreferences(USER_INFO, MODE_PRIVATE);
//        String phoneNumber = sharedPreferences.getString(PHONE_NUMBER, null);
//        if (phoneNumber != null){
//            binding.edtPhoneNumber.setText(phoneNumber);
//        }
//    }
    private void setupEvent() {

        binding.layoutCreateAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(requireActivity(), CreateAddressActivity.class);
                startActivity(intent);
            }
        });

    }

    public String getStoreName() {
        String storeName = Objects.requireNonNull(binding.edtStoreName.getText()).toString().trim();
        if (storeName.equals("")) {
            binding.edtStoreName.setError(DEFAULT_REQUIRE);
            return null;
        }
        return storeName;
    }


    public String getStoreAddress() {
        // Chờ khi nào lấy được địa chỉ của customer sẽ triển khai thêm
        // Hiện tại lấy mặc định store Address là R.string.address1
        String storeAddress = getResources().getString(R.string.address1);
        if (storeAddress.equals("")){
            return null;
        }
        return storeAddress;
    }
}
