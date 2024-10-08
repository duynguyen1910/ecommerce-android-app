package Fragments.RequestedInvoices;
import static android.content.Context.MODE_PRIVATE;
import static constants.keyName.STORE_ID;
import static constants.keyName.USER_ID;
import static constants.keyName.USER_INFO;

import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.example.stores.R;
import com.example.stores.databinding.FragmentWithOnlyRecyclerviewBinding;
import java.util.ArrayList;
import Adapters.Invoices.RequestInvoiceAdapter;
import api.invoiceApi;
import enums.OrderStatus;
import interfaces.GetCollectionCallback;
import interfaces.InAdapter.UpdateCountListener;
import models.Invoice;
import utils.Invoice.InvoiceUtils;

public class ConfirmedFragment extends Fragment {
    FragmentWithOnlyRecyclerviewBinding binding;
    @Nullable
    @Override

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentWithOnlyRecyclerviewBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        initInvoicesRequest();
    }

    private void initInvoicesRequest() {
        InvoiceUtils.initRequestInvoiceByStatus(requireContext(), binding, OrderStatus.PENDING_SHIPMENT.getOrderStatusValue());
    }
}
