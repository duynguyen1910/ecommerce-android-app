package Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.example.stores.databinding.FragmentProfileBinding;

import Activities.CartActivity;
import Activities.InvoiceActivity;
import Activities.ActivateStoreActivity;
import Activities.SettingsActivity;
import Activities.StoreOwnerActivity;
import Activities.UpdateProfileActivity;

public class ProfileFragment extends Fragment {
    FragmentProfileBinding binding;
    @Nullable
    @Override

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentProfileBinding.inflate(getLayoutInflater());

        initUI();
        setupEvents();

        return binding.getRoot();
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
    }
}
