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
import Adapters.Invoices.InvoiceAdapter;
import api.invoiceApi;
import enums.OrderStatus;
import interfaces.GetCollectionCallback;
import interfaces.InAdapter.UpdateCountListener;
import models.Invoice;
import utils.Invoice.InvoiceUtils;

public class DeliveryBeingTransportedFragment extends Fragment {
    FragmentWithOnlyRecyclerviewBinding binding;

    @Nullable
    @Override

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentWithOnlyRecyclerviewBinding.inflate(getLayoutInflater());
        setupUI();
        return binding.getRoot();
    }

    private void setupUI() {
        InvoiceUtils.initDeliveryInvoiceByStatus(requireContext(), binding, OrderStatus.IN_TRANSIT.getOrderStatusValue());
    }
}
