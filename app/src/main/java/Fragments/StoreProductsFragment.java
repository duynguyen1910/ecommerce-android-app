package Fragments;

import static android.content.Context.MODE_PRIVATE;
import static constants.keyName.STORE_ID;
import static constants.keyName.USER_INFO;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.stores.R;
import com.example.stores.databinding.FragmentStoreProductsBinding;

import java.util.ArrayList;

import Adapters.MyProductsAdapter;
import Adapters.ProductAdapter;
import interfaces.GetCollectionCallback;
import models.Product;

public class StoreProductsFragment extends Fragment {
    FragmentStoreProductsBinding binding;

    @Nullable
    @Override

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentStoreProductsBinding.inflate(getLayoutInflater());
        initProducts();

        return binding.getRoot();
    }
    private void initProducts() {
        binding.progressBar.setVisibility(View.VISIBLE);
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences(USER_INFO, MODE_PRIVATE);
        String storeId = sharedPreferences.getString(STORE_ID, null);
        Product product = new Product();
        product.getProductsCollection(storeId, new GetCollectionCallback<Product>() {
            @Override
            public void onGetDataSuccess(ArrayList<Product> products) {
                binding.progressBar.setVisibility(View.GONE);
                binding.recyclerView.setLayoutManager(new GridLayoutManager(requireActivity(), 2));
                binding.recyclerView.setAdapter(new ProductAdapter(requireActivity(), products));
            }

            @Override
            public void onGetDataFailure(String errorMessage) {

            }
        });
    }



}
