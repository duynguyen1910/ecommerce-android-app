package Fragments;

import static android.content.Context.MODE_PRIVATE;
import static constants.keyName.STORE_ID;
import static constants.keyName.USER_INFO;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import com.example.stores.databinding.FragmentStoreProductsBinding;

import java.util.ArrayList;

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

    @Override
    public void onResume() {
        super.onResume();
        initProducts();
    }

    private void initProducts() {
        binding.progressBar.setVisibility(View.VISIBLE);

        Intent intent = requireActivity().getIntent();
        if (intent != null){
            String storeId = intent.getStringExtra("storeId");
            Product product = new Product();
            product.getProductsByStoreId(storeId, new GetCollectionCallback<Product>() {
                @Override
                public void onGetListSuccess(ArrayList<Product> products) {
                    binding.progressBar.setVisibility(View.GONE);
                    binding.recyclerView.setLayoutManager(new GridLayoutManager(requireActivity(), 2));
                    binding.recyclerView.setAdapter(new ProductAdapter(requireActivity(), products));
                }

                @Override
                public void onGetListFailure(String errorMessage) {

                }
            });
        }

    }



}
