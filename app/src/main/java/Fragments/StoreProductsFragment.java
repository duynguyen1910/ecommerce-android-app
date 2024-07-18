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
        initProducts1();

        return binding.getRoot();
    }
    private void initProducts1() {
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
    private void initProducts() {


//        binding.progressBarProducts.setVisibility(View.VISIBLE);
        ArrayList<Product> list = new ArrayList<>();
//        ArrayList<String> picUrls1 = new ArrayList<>();
//        picUrls1.add("https://down-vn.img.susercontent.com/file/sg-11134201-7rbmn-lqld5fts53pj7e");
//        picUrls1.add("https://down-vn.img.susercontent.com/file/sg-11134201-7rbl4-lqld5gi75d2e60");
//        picUrls1.add("https://down-vn.img.susercontent.com/file/sg-11134201-7rbmc-lqld5e0asxgg61");
//        picUrls1.add("https://down-vn.img.susercontent.com/file/sg-11134201-7rbk4-lqlpjd5622bp5d");
//
//        ArrayList<String> picUrls2 = new ArrayList<>();
//        picUrls2.add("https://down-vn.img.susercontent.com/file/sg-11134201-7rccv-ls294ot5axpz02");
//        picUrls2.add("https://down-vn.img.susercontent.com/file/sg-11134201-7rcdn-ls294pp208mn48");
//        picUrls2.add("https://down-vn.img.susercontent.com/file/sg-11134201-7rcdp-ls294qoujut336");
//        picUrls2.add("https://down-vn.img.susercontent.com/file/sg-11134201-7rcev-ls294uaz9fcv33");
//
//
//        ArrayList<String> picUrls3 = new ArrayList<>();
//        picUrls3.add("https://down-vn.img.susercontent.com/file/e55e62939961126e8ef3b54061f0307d");
//        picUrls3.add("https://down-vn.img.susercontent.com/file/91d4a1fe3c410262bf67de9ac30e837d");
//        picUrls3.add("https://down-vn.img.susercontent.com/file/94ef82139c4ba8bfe56df151ff783e36");
//        picUrls3.add("https://down-vn.img.susercontent.com/file/4444ceee25d81cdc39228cdaec86c89e");
//
//        ArrayList<String> picUrls4 = new ArrayList<>();
//        picUrls4.add("https://down-vn.img.susercontent.com/file/sg-11134201-7rcck-lsaqdevfanrq8f");
//        picUrls4.add("https://down-vn.img.susercontent.com/file/sg-11134201-7rce5-lsaqdhlxbx002b");
//        picUrls4.add("https://down-vn.img.susercontent.com/file/sg-11134201-7rcen-lsaqdfgsfeeqa7");
//        picUrls4.add("https://down-vn.img.susercontent.com/file/sg-11134201-7rcfb-lsaqdfxpqmpo1c");
//
////        Product(String title, String description, ArrayList<String> picUrl, double price, double rating, int sold)
//        list.add(new Product("Lovito Đầm chữ A phối ren hoa đơn giản dành cho nữ LNA38057", getResources().getResourceName(R.string.product_desc1).toString(), picUrls1, 149000, 298000, 4.9, 200, 50, "2"));
//        list.add(new Product("Lovito Đầm trễ vai ngọc trai trơn đơn giản dành cho nữ L76AD154", getResources().getResourceName(R.string.product_desc1).toString(), picUrls2, 119000, 228000, 4.8, 179, 48, "2"));
//        list.add(new Product("Đồng Hồ Điện Tử Chống Nước Phong Cách Quân Đội SANDA 2023 Cho Nam", getResources().getResourceName(R.string.product_desc2).toString(), picUrls3, 189000, 350000, 5.0, 559, 46, "2"));
//        list.add(new Product("Huizumei Váy preppy nữ mùa hè cổ polo nhỏ chắp vá eo nâng cao và giảm béo váy ngắn", getResources().getResourceName(R.string.product_desc3).toString(), picUrls4, 129000, 235000, 4.7, 989, 45, "2"));
////
//        list.add(new Product("Lovito Đầm chữ A phối ren hoa đơn giản dành cho nữ LNA38057", getResources().getResourceName(R.string.product_desc1).toString(), picUrls1, 149000, 298000, 4.9, 200, 50, "2"));
//        list.add(new Product("Lovito Đầm trễ vai ngọc trai trơn đơn giản dành cho nữ L76AD154", getResources().getResourceName(R.string.product_desc1).toString(), picUrls2, 119000, 228000, 4.8, 179, 48, "2"));
//        list.add(new Product("Đồng Hồ Điện Tử Chống Nước Phong Cách Quân Đội SANDA 2023 Cho Nam", getResources().getResourceName(R.string.product_desc2).toString(), picUrls3, 189000, 350000, 5.0, 559, 46, "2"));
//        list.add(new Product("Huizumei Váy preppy nữ mùa hè cổ polo nhỏ chắp vá eo nâng cao và giảm béo váy ngắn", getResources().getResourceName(R.string.product_desc3).toString(), picUrls4, 129000, 235000, 4.7, 989, 45, "2"));
////

            binding.recyclerView.setLayoutManager(new GridLayoutManager(requireActivity(), 2));
            binding.recyclerView.setAdapter(new ProductAdapter(requireActivity(), list));



    }


}
