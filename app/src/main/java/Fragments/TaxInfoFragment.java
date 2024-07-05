package Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.stores.databinding.FragmentTaxInfoBinding;

import Activities.RegisteredBusinessAddressActivity;

public class TaxInfoFragment extends Fragment {
    FragmentTaxInfoBinding binding;
    @Nullable
    @Override

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentTaxInfoBinding.inflate(getLayoutInflater());
        binding.layoutSettingBusinessAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(requireActivity(), RegisteredBusinessAddressActivity.class);
                startActivity(intent);
            }
        });
        return binding.getRoot();
    }
}
