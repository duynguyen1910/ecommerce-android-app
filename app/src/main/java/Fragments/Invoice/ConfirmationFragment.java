package Fragments.Invoice;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.stores.databinding.FragmentInvoiceAwaitConfirmationBinding;

import java.util.ArrayList;
import java.util.Map;

import Activities.CartActivity;
import Adapters.CartAdapter;
import Adapters.InvoiceAdapter;
import api.invoiceApi;
import interfaces.GetCollectionCallback;
import interfaces.ToTalFeeCallback;
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
        invoiceApi invoiceApi = new invoiceApi();


        binding.progressBar.setVisibility(View.VISIBLE);
        binding.progressBar.getIndeterminateDrawable()
                .setColorFilter(Color.parseColor("#F04D7F"), PorterDuff.Mode.MULTIPLY);
        invoiceApi.getAllInvoices(new GetCollectionCallback<Invoice>() {
            @Override
            public void onGetListSuccess(ArrayList<Invoice> cartList) {
                binding.progressBar.setVisibility(View.GONE);

                    InvoiceAdapter invoiceAdapter = new InvoiceAdapter(requireActivity(), cartList);
                    binding.recyclerView.setAdapter(invoiceAdapter);
                    binding.recyclerView.setLayoutManager(new LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false));

            }

            @Override
            public void onGetListFailure(String errorMessage) {
                binding.progressBar.setVisibility(View.GONE);
                Toast.makeText(getContext(), errorMessage, Toast.LENGTH_SHORT).show();
            }
        });


    }

}
