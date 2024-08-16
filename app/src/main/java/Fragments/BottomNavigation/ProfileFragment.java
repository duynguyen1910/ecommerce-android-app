package Fragments.BottomNavigation;

import static android.content.Context.MODE_PRIVATE;
import static constants.keyName.FULLNAME;
import static constants.keyName.PHONE_NUMBER;
import static constants.keyName.STORE_ID;
import static constants.keyName.USER_ID;
import static constants.keyName.USER_INFO;
import static constants.keyName.USER_ROLE;

import static utils.Cart.CartUtils.updateQuantityInCart;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.stores.R;
import com.example.stores.databinding.FragmentProfileBinding;

import Activities.Admin.AdminActivity;
import Activities.BuyProduct.CartActivity;
import Activities.Invoices.DeliveryActivity;
import Activities.Invoices.InvoiceActivity;
import Activities.StoreSetup.ActivateStoreActivity;
import Activities.LoginActivity;
import Activities.RegisterActivity;
import Activities.ProfileSetup.SettingsActivity;
import Activities.StoreSetup.StoreOwnerActivity;
import Activities.ProfileSetup.UpdateProfileActivity;
import enums.UserRole;
import interfaces.ImageCallback;
import models.User;

public class ProfileFragment extends Fragment {
    private SharedPreferences sharedPreferences;
    private FragmentProfileBinding binding;
    private User user;
    private String storeID;


    @Nullable
    @Override

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentProfileBinding.inflate(getLayoutInflater());
        initUI();
        setupEvents();
        return binding.getRoot();
    }


    @Override
    public void onResume() {
        super.onResume();
        getUserInfo();
    }

    private void getUserInfo() {
        sharedPreferences = requireContext().getSharedPreferences(USER_INFO, MODE_PRIVATE);

        String userID = sharedPreferences.getString(USER_ID, null);
        String phoneNumber = sharedPreferences.getString(PHONE_NUMBER, null);
        String fullname = sharedPreferences.getString(FULLNAME, null);
        storeID = sharedPreferences.getString(STORE_ID, null);
        int roleValue = sharedPreferences.getInt(USER_ROLE, -1);

        binding.txtFullname.setText(fullname);
        binding.txtPhoneNumber.setText(phoneNumber);
        if (userID != null) {

            binding.txtStore.setText(storeID == null ? "Tạo Store" : "Store của bạn");
            binding.txtRole.setText(UserRole.fromInt(roleValue).getLabelRole());

            setupUIByRole(roleValue);

            binding.loggedLayoutLn.setVisibility(View.VISIBLE);
            binding.defaultLayoutRl.setVisibility(View.GONE);

            user.getUserImageUrl(userID, new ImageCallback() {
                @Override
                public void getImageSuccess(String downloadUri) {
                    Glide.with(getContext()).load(downloadUri).into(binding.imvAvatar);
                }

                @Override
                public void getImageFailure(String errorMessage) {
                    Toast.makeText(getContext(), errorMessage, Toast.LENGTH_SHORT).show();
                }
            });

            return;
        }

        binding.loggedLayoutLn.setVisibility(View.GONE);
        binding.defaultLayoutRl.setVisibility(View.VISIBLE);
    }

    private void setupUIByRole(int roleValue) {
        UserRole userRole = UserRole.fromInt(roleValue);
        // reset trạng thái ban đầu
        binding.layoutLogistics.setVisibility(View.VISIBLE);
        binding.layoutAdmin.setVisibility(View.VISIBLE);
        binding.layoutInvoices.setVisibility(View.VISIBLE);
        binding.layoutActivateStore.setVisibility(View.VISIBLE);
        binding.iconCart.setVisibility(View.VISIBLE);

        // set layout by role
        switch (userRole) {
            case CUSTOMER_ROLE:
            case STORE_OWNER_ROLE:
                binding.layoutLogistics.setVisibility(View.GONE);
                binding.layoutAdmin.setVisibility(View.GONE);
                break;

            case SHIPPER_ROLE:
                binding.layoutInvoices.setVisibility(View.GONE);
                binding.layoutActivateStore.setVisibility(View.GONE);
                binding.iconCart.setVisibility(View.GONE);
                binding.layoutAdmin.setVisibility(View.GONE);

                break;
            case ADMIN_ROLE:
                binding.layoutLogistics.setVisibility(View.GONE);
                binding.layoutInvoices.setVisibility(View.GONE);
                binding.layoutActivateStore.setVisibility(View.GONE);
                break;
        }
    }


    private void initUI() {
        user = new User();
        updateQuantityInCart(binding.txtQuantityInCart);
    }

    private void setupEvents() {
        binding.imvSettings.setOnClickListener(v -> {
            Intent intent = new Intent(requireActivity(), SettingsActivity.class);
            startActivity(intent);

        });
        binding.layoutLogistics.setOnClickListener(v -> {
            Intent intent = new Intent(requireActivity(), DeliveryActivity.class);
            startActivity(intent);
        });
        binding.layoutAdmin.setOnClickListener(v -> {
            Intent intent = new Intent(requireActivity(), AdminActivity.class);
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

            intent.putExtra("tabSelected", 0);
            startActivity(intent);
        });

        binding.layoutInvoiceAwaitDelivery.setOnClickListener(v -> {
            Intent intent = new Intent(requireActivity(), InvoiceActivity.class);
            intent.putExtra("tabSelected", 2);
            startActivity(intent);
        });

        binding.layoutInvoiceAwaitPickup.setOnClickListener(v -> {
            //2
            Intent intent = new Intent(requireActivity(), InvoiceActivity.class);
            intent.putExtra("tabSelected", 1);
            startActivity(intent);
        });


        binding.layoutActivateStore.setOnClickListener(v -> {
            // Nếu đã tạo store thì vào thẳng Store Owner Activity
            if (storeID != null) {
                Intent intent = new Intent(requireActivity(), StoreOwnerActivity.class);
                intent.putExtra(STORE_ID, storeID);
                startActivity(intent);
            } else {
                Intent intent = new Intent(requireActivity(), ActivateStoreActivity.class);
                startActivity(intent);
            }
        });

        binding.loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), LoginActivity.class));
            }
        });

        binding.registerBtn.setOnClickListener(v -> {
            startActivity(new Intent(getActivity(), RegisterActivity.class));
        });

        binding.layoutLogoutLn.setOnClickListener(v -> {
            sharedPreferences = requireContext().getSharedPreferences(USER_INFO, MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.clear();
            editor.apply();

            Glide.with(getContext()).load(R.drawable.ic_account_black).into(binding.imvAvatar);


            startActivity(new Intent(getActivity(), LoginActivity.class));
        });
    }
}