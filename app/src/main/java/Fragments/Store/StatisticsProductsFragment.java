package Fragments.Store;

import static android.content.Context.MODE_PRIVATE;
import static constants.keyName.STORE_ID;
import static constants.keyName.USER_INFO;
import static constants.toastMessage.INTERNET_ERROR;
import static utils.Cart.CartUtils.showToast;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.stores.databinding.FragmentStatisticsProductsBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import Adapters.Statistics.ProductStatisticsAdapter;
import api.productApi;
import interfaces.GetAggregate.GetAggregateCallback;
import interfaces.GetCollectionCallback;
import models.Invoice;
import models.Product;


public class StatisticsProductsFragment extends Fragment {
    FragmentStatisticsProductsBinding binding;
    String g_sStoreID;

    @Nullable
    @Override

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentStatisticsProductsBinding.inflate(getLayoutInflater());
        getStoreID();
        return binding.getRoot();
    }


    @Override
    public void onResume() {
        super.onResume();
        getBestSeller();
        getHighesRevenue();
    }


    private void getStoreID() {
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences(USER_INFO, MODE_PRIVATE);
        g_sStoreID = sharedPreferences.getString(STORE_ID, null);
    }

    private void getBestSeller() {
        binding.progressBar.setVisibility(View.VISIBLE);
        productApi m_productApi = new productApi();

        m_productApi.getTopBestSellerByStoreID(g_sStoreID, 100, new GetCollectionCallback<Product>() {
            @Override
            public void onGetListSuccess(ArrayList<Product> bestSellers) {

                List<Task<Void>> tasks = new ArrayList<>();
                for (int i = 0; i < bestSellers.size(); i++) {
                    tasks.add(m_productApi.getProductRevenueTask(bestSellers.get(i)));
                }

                Tasks.whenAllSuccess(tasks)
                        .addOnSuccessListener(unused -> {
                            binding.progressBar.setVisibility(View.GONE);
                            ProductStatisticsAdapter adapter = new ProductStatisticsAdapter(requireActivity(), bestSellers);
                            binding.recyclerView.setAdapter(adapter);
                            binding.recyclerView.setLayoutManager(new LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false));

                        })
                        .addOnFailureListener(e -> {
                            showToast(requireActivity(), INTERNET_ERROR);
                        });
            }

            @Override
            public void onGetListFailure(String errorMessage) {
                showToast(requireActivity(), errorMessage);
            }
        });
    }

    private void getHighesRevenue() {
        productApi productApi = new productApi();
        productApi.getHighestRevenueByStoreID(g_sStoreID, 5, new GetCollectionCallback<Product>() {
            @Override
            public void onGetListSuccess(ArrayList<Product> highestRevenueList) {

            }

            @Override
            public void onGetListFailure(String errorMessage) {
                showToast(requireActivity(), errorMessage);
            }
        });
    }


}