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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.stores.R;
import com.example.stores.databinding.FragmentWithOnlyRecyclerviewBinding;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import Adapters.Invoices.RequestInvoiceAdapter;
import api.invoiceApi;
import enums.OrderStatus;
import interfaces.GetCollectionCallback;
import interfaces.InAdapter.UpdateCountListener;
import models.CartItem;
import models.Invoice;
import models.Product;

public class CancelledFragment extends Fragment {
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
        invoiceApi invoiceApi = new invoiceApi();
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences(USER_INFO, MODE_PRIVATE);
        String userID = sharedPreferences.getString(USER_ID, null);
        String storeID = sharedPreferences.getString(STORE_ID, null);

        if(userID == null || storeID == null) return;

        binding.progressBar.setVisibility(View.VISIBLE);
        binding.progressBar.getIndeterminateDrawable()
                .setColorFilter(ContextCompat.getColor(getContext(), R.color.primary_color),
                        PorterDuff.Mode.MULTIPLY);

        invoiceApi.getInvoiceByStoreIDApi(storeID, OrderStatus.CANCELLED.getOrderStatusValue(),
                new GetCollectionCallback<Invoice>() {
                    @Override
                    public void onGetListSuccess(ArrayList<Invoice> requestInvoiceList) {
                        binding.progressBar.setVisibility(View.GONE);

                        RequestInvoiceAdapter adapter = new RequestInvoiceAdapter(requireActivity(),
                                requestInvoiceList, (UpdateCountListener) requireActivity());
                        binding.recyclerView.setAdapter(adapter);
                        binding.recyclerView.setLayoutManager(new LinearLayoutManager(requireActivity(),
                                LinearLayoutManager.VERTICAL, false));
                    }

                    @Override
                    public void onGetListFailure(String errorMessage) {
                        Toast.makeText(getContext(), errorMessage, Toast.LENGTH_SHORT).show();
                        binding.progressBar.setVisibility(View.GONE);
                    }
                });
    }
}
