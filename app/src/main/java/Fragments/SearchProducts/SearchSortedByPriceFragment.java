package Fragments.SearchProducts;
import static constants.keyName.CATEGORY_ID;
import static constants.keyName.CATEGORY_NAME;
import static constants.keyName.STORE_ID;
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
import com.example.stores.databinding.FragmentSearchSortedByPriceBinding;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import java.util.regex.Pattern;

import Adapters.CategoryGridForDialogAdapter;
import Adapters.ProductAdapter;
import interfaces.CategoryDialogListener;
import interfaces.GetCollectionCallback;
import models.Category;
import models.Product;


public class SearchSortedByPriceFragment extends Fragment implements CategoryDialogListener {
    FragmentSearchSortedByPriceBinding binding;
    ArrayList<Category> allCategories;
    String transferedCategoryId, transferedCategoryName;
    ArrayList<Product> listProducts;
    int transferedselectedPosition = -1;
    String categoryName;
    ProductAdapter adapter;

    @Nullable
    @Override

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentSearchSortedByPriceBinding.inflate(getLayoutInflater());
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
            String categoryId = bundle.getString(CATEGORY_ID);
            categoryName = bundle.getString(CATEGORY_NAME);
            binding.txtSelectedCategory.setText(categoryName);
            transferedselectedPosition = bundle.getInt("selectedPosition");
            String storeId = bundle.getString(STORE_ID);
            String stringQuery = bundle.getString("stringQuery");


            if (storeId != null) {
                // Nhận storeId và categoroId từ StoreCategoriesFragments
                // Khởi tạo list products từ chính storeId và categoryId được yêu cầu
                fetchProductsByCategoryAndStoreId(storeId, categoryId);
            } else {
                if (stringQuery != null){
                    // Nhận stringQuery từ Home hoặc từ Search Activity
                    fetchProductsByStringQuery(stringQuery);
                }else {
                    // Nhận categoryId từ home hoặc chọn category trên chính fragment này
                    // Khởi tạo list products bằng cách lấy toàn bộ Collection Products thỏa mãn categoryId
                    fetchProductsAscendingByCategoryId(categoryId);
                }


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

    private void fetchProductsAscendingByCategoryId(String categoryId) {
        setLoadingState(true);

        Product product = new Product();
        product.getAllProductAscendingByCategoryId(categoryId, new GetCollectionCallback<Product>() {
            @Override
            public void onGetDataSuccess(ArrayList<Product> products) {
                setLoadingState(false);
                binding.txtSelectedCategory.setVisibility(View.VISIBLE);
                listProducts = new ArrayList<>(products);

                if (listProducts.isEmpty()) {
                    binding.layoutEmpty.setVisibility(View.VISIBLE);
                } else {
                    binding.layoutEmpty.setVisibility(View.GONE);
                }

                adapter.updateProducts(listProducts);
            }

            @Override
            public void onGetDataFailure(String errorMessage) {
                showToast(errorMessage);
            }
        });
    }
    public void fetchProductsByStringQuery(String stringQuery) {
        setLoadingState(true);
        binding.txtSelectedCategory.setVisibility(View.GONE);

        Product product = new Product();
        product.getAllProducts(new GetCollectionCallback<Product>() {
            @Override
            public void onGetDataSuccess(ArrayList<Product> products) {

                setLoadingState(false);
                listProducts = new ArrayList<>();
                if (products.isEmpty()) {
                    binding.layoutEmpty.setVisibility(View.VISIBLE);

                } else {
                    binding.layoutEmpty.setVisibility(View.GONE);
                    for (Product item : products){
                        if (toNormalCase(item.getProductName()).contains(toNormalCase(stringQuery))){
                            listProducts.add(item);
                        }
                    }
                }

                adapter.updateProducts(listProducts);
            }

            @Override
            public void onGetDataFailure(String errorMessage) {
                showToast(errorMessage);
            }
        });
    }
    public String toNormalCase(String productName) {
        productName = Normalizer.normalize(productName.toLowerCase(), Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        productName = pattern.matcher(productName).replaceAll("");
        return productName;
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

        CategoryGridForDialogAdapter categoryAdapter = new CategoryGridForDialogAdapter(requireActivity(), allCategories, SearchSortedByPriceFragment.this, transferedselectedPosition);
        categoryBinding.recyclerViewCategory.setLayoutManager(new GridLayoutManager(requireActivity(), 3, GridLayoutManager.VERTICAL, false));
        categoryBinding.recyclerViewCategory.setAdapter(categoryAdapter);

        categoryBinding.btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.txtSelectedCategory.setVisibility(View.GONE);
                transferedselectedPosition = -1;
                dialog.dismiss();
            }
        });

        categoryBinding.btnSubmit.setOnClickListener(v -> {
            if (transferedselectedPosition != -1 ){
                listProducts.clear();
                fetchProductsAscendingByCategoryId(transferedCategoryId);
                binding.txtSelectedCategory.setText(transferedCategoryName);
                dialog.dismiss();
            }
        });
    }
    private void setLoadingState(boolean isLoading) {
        binding.progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
    }

    private void getCategoriesRequest() {
        allCategories = new ArrayList<>();
        Category category = new Category();
        category.getCategoryCollection(new GetCollectionCallback<Category>() {
            @Override
            public void onGetDataSuccess(ArrayList<Category> categories) {
                allCategories = new ArrayList<>(categories);
            }

            @Override
            public void onGetDataFailure(String errorMessage) {
                showToast(errorMessage);
            }
        });
    }

    private void fetchProductsByCategoryAndStoreId(String storeId, String categoryId) {
        setLoadingState(true);


        Product product = new Product();
        product.getAllProductByStoreIdAndCategoryId(storeId, categoryId, new GetCollectionCallback<Product>() {
            @Override
            public void onGetDataSuccess(ArrayList<Product> products) {
                setLoadingState(false);
                binding.txtSelectedCategory.setVisibility(View.VISIBLE);
                listProducts = new ArrayList<>(products);
                if (!listProducts.isEmpty()) {
                    setLoadingState(false);
                }
                adapter.updateProducts(listProducts);
            }

            @Override
            public void onGetDataFailure(String errorMessage) {
                showToast(errorMessage);
            }
        });
    }
    private void showToast(String message) {
        Toast.makeText(requireActivity(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void transferCategory(int selectedPosition, String categoryId, String categoryName) {
        transferedselectedPosition = selectedPosition;
        transferedCategoryId = categoryId;
        transferedCategoryName = categoryName;
    }
}
