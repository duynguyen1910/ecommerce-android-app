package Fragments.SearchProducts;
import static constants.toastMessage.INTERNET_ERROR;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import com.example.stores.R;
import com.example.stores.databinding.DialogSelectCategoryBinding;
import com.example.stores.databinding.FragmentSearchRelateBinding;

import java.util.ArrayList;
import java.util.Objects;

import Adapters.CategoryGridForDialogAdapter;
import Adapters.ProductAdapter;
import interfaces.CategoryDialogListener;
import interfaces.GetCollectionCallback;
import models.Category;
import models.Product;


public class SearchRelateFragment extends Fragment implements CategoryDialogListener {
    FragmentSearchRelateBinding binding;
    ArrayList<Category> allCategories;
    String transferedCategoryId, transferedCategoryName;
    ArrayList<Product> listProducts;
    int transferedselectedPosition = -1;
    String categoryName;
    ProductAdapter adapter;

    @Nullable
    @Override

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentSearchRelateBinding.inflate(getLayoutInflater());
        initUI();
        getBundle();
        setupEvents();

        return binding.getRoot();
    }

    @Override
    public void onResume() {
        getBundle();
        super.onResume();
    }

    private void getBundle() {

        getCategoriesRequest();
        Intent intent = requireActivity().getIntent();
        Bundle bundle = intent.getExtras();

        if (bundle != null) {
            //Bundle nhận từ nhiều nguồn khác nhau, có thể từ Home, từ chính fragment này hoặc từ chỗ khác....
            String categoryId = bundle.getString("categoryId");
            categoryName = bundle.getString("categoryName");
            binding.txtSelectedCategory.setText(categoryName);
            transferedselectedPosition = bundle.getInt("selectedPosition");
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
        }
    }

    private void initUI() {
        listProducts = new ArrayList<>();
        adapter = new ProductAdapter(requireActivity(), listProducts);
        binding.recyclerView.setLayoutManager(new GridLayoutManager(requireActivity(), 2));
        binding.recyclerView.setAdapter(adapter);
    }

    private void setupEvents() {
        binding.txtSelectCategory.setOnClickListener(v -> popUpCategoryDialog());
    }

    private void initProductsByCategorId(String categoryId) {
        binding.progressBar.setVisibility(View.VISIBLE);

        Product product = new Product();
        product.getAllProductByCategoryId(categoryId, new GetCollectionCallback<Product>() {
            @Override
            public void onGetDataSuccess(ArrayList<Product> products) {

                binding.txtSelectedCategory.setVisibility(View.VISIBLE);
                listProducts = new ArrayList<>(products);
                binding.progressBar.setVisibility(View.GONE);
                if (listProducts.isEmpty()) {
                    binding.layoutEmpty.setVisibility(View.VISIBLE);
                } else {

                    binding.layoutEmpty.setVisibility(View.GONE);

                }

                adapter.updateProducts(listProducts);
            }

            @Override
            public void onGetDataFailure(String errorMessage) {
                Toast.makeText(requireActivity(), errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void popUpCategoryDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        DialogSelectCategoryBinding categoryBinding = DialogSelectCategoryBinding.inflate(getLayoutInflater());
        builder.setView(categoryBinding.getRoot());
        AlertDialog dialog = builder.create();

        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawableResource(R.drawable.custom_edit_text_border);
        dialog.show();
        Window window = dialog.getWindow();
        if (window != null) {
            window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            window.setGravity(Gravity.BOTTOM);

            Animation slideUp = AnimationUtils.loadAnimation(requireActivity(), R.anim.slide_up);
            categoryBinding.getRoot().startAnimation(slideUp);
        }

        dialog.setCancelable(true);


        categoryBinding.progressBarCategory.setVisibility(View.VISIBLE);
        categoryBinding.progressBarCategory.setVisibility(View.GONE);
        CategoryGridForDialogAdapter categoryAdapter = new CategoryGridForDialogAdapter(requireActivity(), allCategories, SearchRelateFragment.this, transferedselectedPosition);
        categoryBinding.recyclerViewCategory.setLayoutManager(new GridLayoutManager(requireActivity(), 4, GridLayoutManager.VERTICAL, false));
        categoryBinding.recyclerViewCategory.setAdapter(categoryAdapter);

        categoryBinding.btnClose.setOnClickListener(v -> dialog.dismiss());

        categoryBinding.btnSubmit.setOnClickListener(v -> {

            if (transferedselectedPosition != -1 ){
                listProducts.clear();
                initProductsByCategorId(transferedCategoryId);
                binding.txtSelectedCategory.setText(transferedCategoryName);
                dialog.dismiss();
            }

        });
    }

    private void getCategoriesRequest() {
        Category category = new Category();
        category.getCategoryCollection(new GetCollectionCallback<Category>() {
            @Override
            public void onGetDataSuccess(ArrayList<Category> categories) {
                allCategories = new ArrayList<>(categories);
            }

            @Override
            public void onGetDataFailure(String errorMessage) {
                Toast.makeText(requireActivity(), INTERNET_ERROR, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initProductsByCategoryAndStoreId(String storeId, String categoryId) {
        binding.progressBar.setVisibility(View.VISIBLE);

        Product product = new Product();
        product.getAllProductByStoreIdAndCategoryId(storeId, categoryId, new GetCollectionCallback<Product>() {
            @Override
            public void onGetDataSuccess(ArrayList<Product> products) {

                binding.txtSelectedCategory.setVisibility(View.VISIBLE);
                listProducts = new ArrayList<>(products);
                if (!listProducts.isEmpty()) {
                    binding.progressBar.setVisibility(View.GONE);
                }
                adapter.updateProducts(listProducts);
            }

            @Override
            public void onGetDataFailure(String errorMessage) {
                Toast.makeText(requireActivity(), errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void transferCategory(int selectedPosition, String categoryId, String categoryName) {
        transferedselectedPosition = selectedPosition;
        transferedCategoryId = categoryId;
        transferedCategoryName = categoryName;
    }
}
