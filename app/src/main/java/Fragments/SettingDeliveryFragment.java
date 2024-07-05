package Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.stores.databinding.FragmentSettingDeliveryBinding;

import Activities.SelectDeliveryMethodActivity;

public class SettingDeliveryFragment extends Fragment {
    FragmentSettingDeliveryBinding binding;
    @Nullable
    @Override

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentSettingDeliveryBinding.inflate(getLayoutInflater());
        binding.txtViewDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(requireActivity(), SelectDeliveryMethodActivity.class);
                startActivity(intent);
            }
        });
        return binding.getRoot();
    }
}
