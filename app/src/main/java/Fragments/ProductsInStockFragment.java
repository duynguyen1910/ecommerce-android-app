package Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;


import com.example.stores.R;
import com.example.stores.databinding.FragmentProductsInStockBinding;

import java.util.ArrayList;
import Adapters.MyProductsAdapter;
import Models.Product;

public class ProductsInStockFragment extends Fragment {
    FragmentProductsInStockBinding binding;
    ArrayList<Product> products;

    @Nullable
    @Override

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentProductsInStockBinding.inflate(getLayoutInflater());
        initProducts();
        initCartUI();
        return binding.getRoot();
    }


    private void initProducts() {
//        binding.progressBarProducts.setVisibility(View.VISIBLE);
        products = new ArrayList<>();
        ArrayList<String> picUrls1 = new ArrayList<>();
        picUrls1.add("https://down-vn.img.susercontent.com/file/sg-11134201-7rbmn-lqld5fts53pj7e");
        picUrls1.add("https://down-vn.img.susercontent.com/file/sg-11134201-7rbl4-lqld5gi75d2e60");
        picUrls1.add("https://down-vn.img.susercontent.com/file/sg-11134201-7rbmc-lqld5e0asxgg61");
        picUrls1.add("https://down-vn.img.susercontent.com/file/sg-11134201-7rbk4-lqlpjd5622bp5d");

        ArrayList<String> picUrls2 = new ArrayList<>();
        picUrls2.add("https://down-vn.img.susercontent.com/file/sg-11134201-7rccv-ls294ot5axpz02");
        picUrls2.add("https://down-vn.img.susercontent.com/file/sg-11134201-7rcdn-ls294pp208mn48");
        picUrls2.add("https://down-vn.img.susercontent.com/file/sg-11134201-7rcdp-ls294qoujut336");
        picUrls2.add("https://down-vn.img.susercontent.com/file/sg-11134201-7rcev-ls294uaz9fcv33");


        ArrayList<String> picUrls3 = new ArrayList<>();
        picUrls3.add("https://down-vn.img.susercontent.com/file/cn-11134301-7r98o-lozhxkvh2rbr86");
        picUrls3.add("https://down-vn.img.susercontent.com/file/cn-11134301-7r98o-lozhxkw11xlo75");
        picUrls3.add("https://down-vn.img.susercontent.com/file/cn-11134301-7r98o-lozhxkvh6z13cb");
        picUrls3.add("https://down-vn.img.susercontent.com/file/cn-11134301-7r98o-lozhxkvh5kgn8a");

        ArrayList<String> picUrls4 = new ArrayList<>();
        picUrls4.add("https://down-vn.img.susercontent.com/file/sg-11134201-7rcck-lsaqdevfanrq8f");
        picUrls4.add("https://down-vn.img.susercontent.com/file/sg-11134201-7rce5-lsaqdhlxbx002b");
        picUrls4.add("https://down-vn.img.susercontent.com/file/sg-11134201-7rcen-lsaqdfgsfeeqa7");
        picUrls4.add("https://down-vn.img.susercontent.com/file/sg-11134201-7rcfb-lsaqdfxpqmpo1c");

        products.add(new Product("Lovito Đầm chữ A phối ren hoa đơn giản dành cho nữ LNA38057", getResources().getResourceName(R.string.product_desc1), picUrls1, 298000, 200, 500, 73, 800, false, 1));
        products.add(new Product("Lovito Đầm trễ vai ngọc trai trơn đơn giản dành cho nữ L76AD154", getResources().getResourceName(R.string.product_desc1), picUrls2, 228000, 98, 456, 139, 506, false, 1));
        products.add(new Product("Đồng hồ điện tử Unisex không thông minh thời trang S8 phong cách mới", getResources().getResourceName(R.string.product_desc2), picUrls3, 70000, 150, 600, 29, 2340, false, 1));
        products.add(new Product("Huizumei Váy preppy nữ mùa hè cổ polo nhỏ chắp vá eo nâng cao và giảm béo váy ngắn", getResources().getResourceName(R.string.product_desc3), picUrls4, 235000, 45, 800, 69, 780, false, 1));

    }

    private void initCartUI() {

        MyProductsAdapter adapter = new MyProductsAdapter(requireActivity(), products);
        binding.recyclerView.setAdapter(adapter);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false));
    }
}
