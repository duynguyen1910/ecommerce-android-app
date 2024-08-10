package Fragments.Invoice;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.example.stores.databinding.FragmentWithOnlyRecyclerviewBinding;
import enums.OrderStatus;
import utils.Invoice.InvoiceUtils;

public class CancelledFragment extends Fragment {
    FragmentWithOnlyRecyclerviewBinding binding;
    @Nullable
    @Override

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentWithOnlyRecyclerviewBinding.inflate(getLayoutInflater());
        setupUI();
        return binding.getRoot();
    }

    private void setupUI() {
        InvoiceUtils.initCustomerInvoiceByStatus(requireContext(), binding, OrderStatus.CANCELLED.getOrderStatusValue());
    }
}