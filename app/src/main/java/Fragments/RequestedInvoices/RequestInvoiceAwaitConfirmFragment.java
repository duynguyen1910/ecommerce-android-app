package Fragments.RequestedInvoices;

import static android.content.Context.MODE_PRIVATE;
import static constants.keyName.STORE_ID;
import static constants.keyName.USER_INFO;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.stores.databinding.FragmentRequestInvoiceAwaitConfirmBinding;

import java.util.ArrayList;

import Adapters.MyProductsAdapter;
import Adapters.RequestInvoiceAdapter;
import interfaces.GetCollectionCallback;
import models.Invoice;
import models.Product;

public class RequestInvoiceAwaitConfirmFragment extends Fragment {
    FragmentRequestInvoiceAwaitConfirmBinding binding;
    @Nullable
    @Override

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentRequestInvoiceAwaitConfirmBinding.inflate(getLayoutInflater());
        initProducts();
        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        initProducts();
    }

    private void initProducts() {
        binding.progressBar.setVisibility(View.VISIBLE);
        binding.progressBar.setVisibility(View.GONE);
        ArrayList<Invoice> invoices = new ArrayList<>();
        RequestInvoiceAdapter adapter = new RequestInvoiceAdapter(requireActivity(), invoices);
        binding.recyclerView.setAdapter(adapter);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false));
    }
}
