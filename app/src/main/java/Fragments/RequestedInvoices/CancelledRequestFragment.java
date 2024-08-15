package Fragments.RequestedInvoices;
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


public class CancelledRequestFragment extends Fragment {
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
        InvoiceUtils.initRequestInvoiceByStatus(requireContext(), binding, OrderStatus.CANCELLED.getOrderStatusValue());
    }
}