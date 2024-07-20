package Fragments.SearchProducts;

import static android.content.Context.MODE_PRIVATE;
import static constants.keyName.STORE_ID;
import static constants.keyName.USER_INFO;
import static constants.toastMessage.INTERNET_ERROR;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.stores.databinding.FragmentSearchRelateBinding;

import java.util.ArrayList;

import Adapters.MyProductsAdapter;
import Adapters.ProductAdapter;
import interfaces.GetCollectionCallback;
import models.Product;


public class SearchRelateFragment extends Fragment {
    FragmentSearchRelateBinding binding;
    @Nullable
    @Override

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentSearchRelateBinding.inflate(getLayoutInflater());
        getBundle();
//        initProducts();

        return binding.getRoot();
    }

    private void getBundle(){
        Intent intent = requireActivity().getIntent();
        Bundle bundle = intent.getExtras();

        if (bundle != null){
//            String categoryName = bundle.getString("categoryName");
            String categoryId = bundle.getString("categoryId");
            String storeId = bundle.getString("storeId");
            initProductsByCategoryAndStoreId(storeId, categoryId);

        }else {
            initProducts();
        }

    }
    private void initProductsByCategoryAndStoreId(String storeId,String categoryId) {
        binding.progressBar.setVisibility(View.VISIBLE);

        Product product = new Product();
        product.getProductsByStoreId(storeId, new GetCollectionCallback<Product>() {
            @Override
            public void onGetDataSuccess(ArrayList<Product> products) {
                ArrayList<Product> list = new ArrayList<>();
                for (Product pr : products){
                    if (pr.getCategoryId().equals(categoryId)){
                        list.add(pr);
                    }
                }
                binding.progressBar.setVisibility(View.GONE);
                binding.recyclerView.setLayoutManager(new GridLayoutManager(requireActivity(), 2));
                binding.recyclerView.setAdapter(new ProductAdapter(requireActivity(), list));
            }

            @Override
            public void onGetDataFailure(String errorMessage) {
                Toast.makeText(requireActivity(), errorMessage, Toast.LENGTH_SHORT).show();

            }
        });
    }
    private void initProducts() {
        binding.progressBar.setVisibility(View.VISIBLE);

        Product product = new Product();
        product.getAllProducts(new GetCollectionCallback<Product>() {
            @Override
            public void onGetDataSuccess(ArrayList<Product> products) {
                ArrayList<Product> productsInStock = new ArrayList<>();
                for (Product item : products){
                    if (item.getInStock() > 0){
                        productsInStock.add(item);
                    }
                }
                binding.progressBar.setVisibility(View.GONE);
                binding.recyclerView.setLayoutManager(new GridLayoutManager(requireActivity(), 2));
                binding.recyclerView.setAdapter(new ProductAdapter(requireActivity(), productsInStock));
            }

            @Override
            public void onGetDataFailure(String errorMessage) {
                Toast.makeText(requireActivity(), INTERNET_ERROR, Toast.LENGTH_SHORT).show();

            }
        });
    }
}
