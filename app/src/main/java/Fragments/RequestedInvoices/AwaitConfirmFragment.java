package Fragments.RequestedInvoices;
import static android.content.Context.MODE_PRIVATE;
import static constants.keyName.STORE_ID;
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
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.stores.R;
import com.example.stores.databinding.FragmentRequestInvoiceAwaitConfirmBinding;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import Adapters.RequestInvoiceAdapter;
import api.invoiceApi;
import enums.OrderStatus;
import interfaces.GetCollectionCallback;
import models.CartItem;
import models.Invoice;
import models.Product;

public class AwaitConfirmFragment extends Fragment {
    FragmentRequestInvoiceAwaitConfirmBinding binding;

    @Nullable
    @Override

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentRequestInvoiceAwaitConfirmBinding.inflate(getLayoutInflater());

        initInvoicesRequest();

        return binding.getRoot();
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

        invoiceApi.getInvoiceByStoreIDApi(storeID, OrderStatus.PENDING_CONFIRMATION.getOrderStatusValue(),
                new GetCollectionCallback<Invoice>() {
            @Override
            public void onGetListSuccess(ArrayList<Invoice> requestInvoiceList) {
                binding.progressBar.setVisibility(View.GONE);

                RequestInvoiceAdapter adapter = new RequestInvoiceAdapter(requireActivity(),
                        requestInvoiceList);
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
