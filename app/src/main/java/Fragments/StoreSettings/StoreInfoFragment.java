package Fragments.StoreSettings;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.stores.databinding.FragmentStoreInfoBinding;

import Activities.CreateAddressActivity;

public class StoreInfoFragment extends Fragment {
    FragmentStoreInfoBinding binding;

    @Nullable
    @Override

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentStoreInfoBinding.inflate(getLayoutInflater());
        setupEvent();

        return binding.getRoot();
    }

    private void setupEvent(){

        binding.layoutCreateAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(requireActivity(), CreateAddressActivity.class);
                startActivity(intent);
            }
        });

    }
}
