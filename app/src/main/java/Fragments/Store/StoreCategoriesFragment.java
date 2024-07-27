package Fragments.Store;

import static constants.keyName.STORE_ID;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.stores.databinding.FragmentStoreCategoriesBinding;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import Adapters.CategoryAdapterForSearch;
import interfaces.GetCollectionCallback;
import models.Category;
import models.Product;

public class StoreCategoriesFragment extends Fragment {
    FragmentStoreCategoriesBinding binding;
    @Nullable
    @Override

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentStoreCategoriesBinding.inflate(getLayoutInflater());
        initCategories();
        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        initCategories();
    }

    private void initCategories() {
        binding.progressBar.setVisibility(View.VISIBLE);

        Intent intent = requireActivity().getIntent();
        if (intent != null) {
            String storeId = intent.getStringExtra(STORE_ID);
            Product product = new Product();
            product.getProductsByStoreId(storeId, new GetCollectionCallback<Product>() {
                @Override
                public void onGetListSuccess(ArrayList<Product> products) {
                    binding.progressBar.setVisibility(View.GONE);
                    Map<String, Category> categoryMap = new HashMap<>();
                    for (Product item : products){
                        String key = item.getCategoryID();
                        Category category = new Category(item.getCategoryID(), item.getCategoryName());
                        if (!categoryMap.containsKey(key)){
                            categoryMap.put(key, category);
                        }
                    }

                    ArrayList<Category> categories = new ArrayList<>(categoryMap.values());


                    CategoryAdapterForSearch adapter = new CategoryAdapterForSearch(requireActivity(), categories, storeId);
                    binding.recyclerView.setLayoutManager(new LinearLayoutManager(requireActivity()));
                    binding.recyclerView.setAdapter(adapter);
                }

                @Override
                public void onGetListFailure(String errorMessage) {
                    binding.progressBar.setVisibility(View.GONE);
                    Toast.makeText(requireActivity(), "Lỗi: " + errorMessage, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

}
