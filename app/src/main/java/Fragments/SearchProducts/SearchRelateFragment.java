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

    private void getBundle() {
        Intent intent = requireActivity().getIntent();
        Bundle bundle = intent.getExtras();

        if (bundle != null) {
            //Bundle nhận từ nhiều nguồn khác nhau, có thể từ Home, từ chính fragment này hoặc từ chỗ khác....
            String categoryId = bundle.getString("categoryId");
            String storeId = bundle.getString("storeId");

            if (storeId != null) {
                // Nhận storeId và categoroId từ StoreCategoriesFragments
                // Khởi tạo list products từ chính storeId và categoryId được yêu cầu
                initProductsByCategoryAndStoreId(storeId, categoryId);
            } else {

                // Nhận categoryId từ home hoặc chọn category trên chính fragment này
                // Khởi tạo list products bằng cách lấy toàn bộ Collection Products thỏa mãn categoryId
                initProductsByCategorId(categoryId);
            }

        } else {
            initProducts();
        }

    }

    private void initProductsByCategorId(String categoryId) {
        binding.progressBar.setVisibility(View.VISIBLE);

        Product product = new Product();
        product.getAllProductByCategoryId(categoryId, new GetCollectionCallback<Product>() {
            @Override
            public void onGetDataSuccess(ArrayList<Product> products) {
                binding.progressBar.setVisibility(View.GONE);
                if (products.isEmpty()) {
                    binding.layoutEmpty.setVisibility(View.VISIBLE);
                } else {
                    binding.layoutEmpty.setVisibility(View.GONE);

                    binding.recyclerView.setLayoutManager(new GridLayoutManager(requireActivity(), 2));
                    binding.recyclerView.setAdapter(new ProductAdapter(requireActivity(), products));
                }

            }

            @Override
            public void onGetDataFailure(String errorMessage) {
                Toast.makeText(requireActivity(), errorMessage, Toast.LENGTH_SHORT).show();

            }
        });
    }


    private void initProductsByCategoryAndStoreId(String storeId, String categoryId) {
        binding.progressBar.setVisibility(View.VISIBLE);

        Product product = new Product();
        product.getAllProductByStoreIdAndCategoryId(storeId, categoryId, new GetCollectionCallback<Product>() {
            @Override
            public void onGetDataSuccess(ArrayList<Product> products) {

                binding.progressBar.setVisibility(View.GONE);
                binding.recyclerView.setLayoutManager(new GridLayoutManager(requireActivity(), 2));
                binding.recyclerView.setAdapter(new ProductAdapter(requireActivity(), products));
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
                for (Product item : products) {
                    if (item.getInStock() > 0) {
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
