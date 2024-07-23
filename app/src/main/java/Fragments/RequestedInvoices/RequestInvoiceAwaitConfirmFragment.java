package Fragments.RequestedInvoices;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.example.stores.databinding.FragmentRequestInvoiceAwaitConfirmBinding;

public class RequestInvoiceAwaitConfirmFragment extends Fragment {
    FragmentRequestInvoiceAwaitConfirmBinding binding;
    @Nullable
    @Override

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentRequestInvoiceAwaitConfirmBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }
}
