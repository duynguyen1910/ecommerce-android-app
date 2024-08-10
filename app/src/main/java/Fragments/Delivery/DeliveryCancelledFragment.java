package Fragments.Delivery;

import static android.content.Context.MODE_PRIVATE;
import static constants.keyName.USER_ID;
import static constants.keyName.USER_INFO;

import android.content.SharedPreferences;
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
import com.example.stores.databinding.FragmentWithOnlyRecyclerviewBinding;

import java.util.ArrayList;

import Adapters.Invoices.DeliveryAdapter;
import api.invoiceApi;
import enums.OrderStatus;
import interfaces.GetCollectionCallback;
import interfaces.InAdapter.UpdateCountListener;
import models.Invoice;
import models.Product;
import utils.Invoice.InvoiceUtils;

public class DeliveryCancelledFragment extends Fragment {
    FragmentWithOnlyRecyclerviewBinding binding;
    ArrayList<Invoice> invoices = new ArrayList<>();

    @Nullable
    @Override

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentWithOnlyRecyclerviewBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }
    @Override
    public void onResume() {
        super.onResume();
        setupUI();
    }
    private void setupUI() {
        InvoiceUtils.initDeliveryInvoiceByStatus(requireContext(), binding, OrderStatus.CANCELLED.getOrderStatusValue());
    }
}
