package Fragments.Store;
import static android.content.Context.MODE_PRIVATE;
import static constants.keyName.STORE_ID;
import static constants.keyName.USER_INFO;
import android.content.SharedPreferences;
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
import java.util.ArrayList;
import Adapters.MyProductsAdapter;
import interfaces.GetCollectionCallback;
import models.Product;

public class ProductsInStockFragment extends Fragment {
    FragmentWithOnlyRecyclerviewBinding binding;

    @Nullable
    @Override

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentWithOnlyRecyclerviewBinding.inflate(getLayoutInflater());
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
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences(USER_INFO, MODE_PRIVATE);
        String storeId = sharedPreferences.getString(STORE_ID, null);
        Product product = new Product();
        product.getProductsInStockByStoreId(storeId, new GetCollectionCallback<Product>() {
            @Override
            public void onGetListSuccess(ArrayList<Product> products) {
                binding.progressBar.setVisibility(View.GONE);
                MyProductsAdapter adapter = new MyProductsAdapter(requireActivity(), products);
                binding.recyclerView.setAdapter(adapter);
                binding.recyclerView.setLayoutManager(new LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false));
            }

            @Override
            public void onGetListFailure(String errorMessage) {
                Toast.makeText(requireActivity(), errorMessage, Toast.LENGTH_SHORT).show();

            }
        });
    }
}