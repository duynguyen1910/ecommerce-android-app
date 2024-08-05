package Fragments.RequestedInvoices;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.example.stores.databinding.FragmentWithOnlyRecyclerviewBinding;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import Adapters.Invoices.RequestInvoiceAdapter;
import models.CartItem;
import models.Invoice;
import models.Product;

public class CancelledFragment extends Fragment {
    FragmentWithOnlyRecyclerviewBinding binding;
    ArrayList<Invoice> invoices = new ArrayList<>();

    @Nullable
    @Override

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentWithOnlyRecyclerviewBinding.inflate(getLayoutInflater());
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
        RequestInvoiceAdapter adapter = new RequestInvoiceAdapter(requireActivity(), invoices);
        binding.recyclerView.setAdapter(adapter);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false));
    }

}
