package Fragments.Invoice;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.example.stores.databinding.FragmentInvoiceCompletedBinding;

public class DeliveredFragment extends Fragment {
    FragmentInvoiceCompletedBinding binding;
    @Nullable
    @Override

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentInvoiceCompletedBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }
}
