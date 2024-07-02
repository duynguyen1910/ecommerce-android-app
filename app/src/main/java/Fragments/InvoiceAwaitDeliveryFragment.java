package Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.example.stores.databinding.FragmentInvoiceAwaitDeliveryBinding;

public class InvoiceAwaitDeliveryFragment extends Fragment {
    FragmentInvoiceAwaitDeliveryBinding binding;
    @Nullable
    @Override

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentInvoiceAwaitDeliveryBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }
}
