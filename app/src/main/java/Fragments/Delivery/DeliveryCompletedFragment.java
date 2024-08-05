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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import Adapters.Invoices.DeliveryAdapter;
import Adapters.Invoices.InvoiceAdapter;
import api.invoiceApi;
import enums.OrderStatus;
import interfaces.GetCollectionCallback;
import models.CartItem;
import models.Invoice;
import models.Product;

public class DeliveryCompletedFragment extends Fragment {
    FragmentWithOnlyRecyclerviewBinding binding;

    @Nullable
    @Override

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentWithOnlyRecyclerviewBinding.inflate(getLayoutInflater());
        setupUI();
        return binding.getRoot();
    }

    private void setupUI() {
        invoiceApi invoiceApi = new invoiceApi();
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences(USER_INFO, MODE_PRIVATE);
        String userID = sharedPreferences.getString(USER_ID, null);

        if (userID == null) return;

        binding.progressBar.setVisibility(View.VISIBLE);
        binding.progressBar.getIndeterminateDrawable()
                .setColorFilter(Color.parseColor("#F04D7F"), PorterDuff.Mode.MULTIPLY);

        invoiceApi.getInvoicesByStatusApi(userID, OrderStatus.DELIVERED.getOrderStatusValue(),
                new GetCollectionCallback<Invoice>() {
                    @Override
                    public void onGetListSuccess(ArrayList<Invoice> invoiceList) {
                        binding.progressBar.setVisibility(View.GONE);
                        DeliveryAdapter invoiceAdapter = new DeliveryAdapter(requireActivity(), invoiceList);
                        binding.recyclerView.setAdapter(invoiceAdapter);
                        binding.recyclerView.setLayoutManager(new LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false));
                    }

                    @Override
                    public void onGetListFailure(String errorMessage) {
                        binding.progressBar.setVisibility(View.GONE);
                        Toast.makeText(getContext(), errorMessage, Toast.LENGTH_SHORT).show();
                    }
                });


    }
}