package Fragments.BottomNavigation;

import static android.content.Context.MODE_PRIVATE;
import static constants.keyName.FULLNAME;
import static constants.keyName.PASSWORD;
import static constants.keyName.PHONE_NUMBER;
import static constants.keyName.USER_ID;
import static constants.keyName.USER_INFO;
import static constants.keyName.USER_ROLE;
import static constants.toastMessage.LOGIN_SUCCESSFULLY;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.stores.R;
import com.example.stores.databinding.FragmentProfileBinding;

import Activities.CartActivity;
import Activities.InvoiceActivity;
import Activities.ActivateStoreActivity;
import Activities.LoginActivity;
import Activities.RegisterActivity;
import Activities.SettingsActivity;
import Activities.StoreOwnerActivity;
import Activities.UpdateProfileActivity;
import enums.UserRole;
import models.User;

public class ProfileFragment extends Fragment {
    private SharedPreferences sharedPreferences;
    private FragmentProfileBinding binding;

    private ActivityResultLauncher<Intent> myLauncher;
    @Nullable
    @Override

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentProfileBinding.inflate(getLayoutInflater());

        initUI();
        setupEvents();
        getUserInfo();
        return binding.getRoot();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        myLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            Intent intent = result.getData();
                            if (intent != null && intent.getBooleanExtra(LOGIN_SUCCESSFULLY, false)) {
                                getUserInfo();
                            }
                        }
                    }
                });
    }


    private void getUserInfo() {
        sharedPreferences = requireContext().getSharedPreferences(USER_INFO, MODE_PRIVATE);

        String userId = sharedPreferences.getString(USER_ID, null);
        String phoneNumber = sharedPreferences.getString(PHONE_NUMBER, null);
        String fullname = sharedPreferences.getString(FULLNAME, null);
        int roleValue = sharedPreferences.getInt(USER_ROLE, -1);

        if(userId != null) {
            binding.loggedLayoutLn.setVisibility(View.VISIBLE);
            binding.defaultLayoutRl.setVisibility(View.GONE);
        } else {
            binding.loggedLayoutLn.setVisibility(View.GONE);
            binding.defaultLayoutRl.setVisibility(View.VISIBLE);
        }

//        if(UserRole.CUSTOMER_ROLE == UserRole.fromInt(roleValue)) {
//            binding.txtStoreName.setVisibility(View.GONE);
//        }
//
//        if(UserRole.STORE_OWNER_ROLE == UserRole.fromInt(roleValue)) {
//            binding.txtStoreName.setText("Chủ hàng");
//        }

        binding.txtFullname.setText(fullname);
        binding.txtPhoneNumber.setText(phoneNumber);
    }

    private void initUI(){
       // Nếu đã tạo store thì đổi text của txtStore
//        binding.txtStore.setText("Store của tôi");

    }

    private void setupEvents(){
        binding.imvSettings.setOnClickListener(v -> {
            Intent intent = new Intent(requireActivity(), SettingsActivity.class);
            startActivity(intent);

        });

        binding.iconCart.setOnClickListener(v -> {
            Intent intent = new Intent(requireActivity(), CartActivity.class);
            startActivity(intent);
        });

        binding.layoutUpdateProfile.setOnClickListener(v -> {
            //0
            Intent intent = new Intent(requireActivity(), UpdateProfileActivity.class);

            startActivity(intent);
        });
        binding.layoutInvoiceAwaitConfirm.setOnClickListener(v -> {
            //1
            Intent intent = new Intent(requireActivity(), InvoiceActivity.class);

            intent.putExtra("invoiceStatus", 0);
            startActivity(intent);
        });
        binding.layoutInvoiceAwaitDelivery.setOnClickListener(v -> {
            Intent intent = new Intent(requireActivity(), InvoiceActivity.class);
            intent.putExtra("invoiceStatus", 1);
            startActivity(intent);
        });
        binding.layoutInvoiceAwaitPickup.setOnClickListener(v -> {
            //2
            Intent intent = new Intent(requireActivity(), InvoiceActivity.class);
            intent.putExtra("invoiceStatus", 2);
            startActivity(intent);
        });


        binding.txtStore.setOnClickListener(v -> {
            // Nếu chưa tạo store
            Intent intent = new Intent(requireActivity(), ActivateStoreActivity.class);
            startActivity(intent);

            // Nếu đã tạo store thì vào thẳng Store Owner Activity

//                Intent intent = new Intent(requireActivity(), StoreOwnerActivity.class);
//                startActivity(intent);
        });

        binding.loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                myLauncher.launch(intent);
            }
        });

        binding.registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(requireActivity(), RegisterActivity.class));
            }
        });

        binding.layoutLogoutLn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sharedPreferences = requireContext().getSharedPreferences(USER_INFO, MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.clear();
                editor.apply();

                Intent intent = new Intent(getActivity(), LoginActivity.class);
                myLauncher.launch(intent);
            }
        });
    }

}
