package Fragments.Invoice;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.stores.databinding.FragmentInvoiceAwaitConfirmationBinding;
import java.util.Map;
import Adapters.InvoiceAdapter;
import models.Invoice;

public class ConfirmationFragment extends Fragment {
    FragmentInvoiceAwaitConfirmationBinding binding;

    @Nullable
    @Override

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentInvoiceAwaitConfirmationBinding.inflate(getLayoutInflater());

        setupUI();

        return binding.getRoot();
    }

    private void setupUI() {


//        InvoiceAdapter invoiceAdapter = new InvoiceAdapter(requireActivity(), invoicesMap);
//        binding.recyclerView.setAdapter(invoiceAdapter);
//        binding.recyclerView.setLayoutManager(new LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false));
    }

}
