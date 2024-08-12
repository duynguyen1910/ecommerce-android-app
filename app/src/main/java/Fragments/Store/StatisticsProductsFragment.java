package Fragments.Store;
import static android.content.Context.MODE_PRIVATE;
import static constants.keyName.STORE_ID;
import static constants.keyName.USER_INFO;
import static constants.toastMessage.INTERNET_ERROR;
import static utils.Cart.CartUtils.showToast;
import android.app.Dialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.example.stores.R;
import com.example.stores.databinding.DialogSortStatisticProductBinding;
import com.example.stores.databinding.FragmentStatisticsProductsBinding;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import java.util.ArrayList;
import java.util.List;
import Adapters.Statistics.ProductStatisticsAdapter;
import api.productApi;
import interfaces.GetCollectionCallback;
import models.Product;



public class StatisticsProductsFragment extends Fragment {
    private FragmentStatisticsProductsBinding binding;
    private String g_sStoreID;
    private ArrayList<Product> g_bestSellers;
    private ProductStatisticsAdapter g_adapter;
    private int g_sortChoice = 0;


    @Nullable
    @Override

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentStatisticsProductsBinding.inflate(getLayoutInflater());
        getStoreID();
        setupEvents();
        return binding.getRoot();
    }


    @Override
    public void onResume() {
        super.onResume();
        getBestSeller();
    }


    private void getStoreID() {
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences(USER_INFO, MODE_PRIVATE);
        g_sStoreID = sharedPreferences.getString(STORE_ID, null);
    }

    private void resetSortChoice() {
        binding.txtSortChoice.setText("Số lượng bán");
        g_sortChoice = 0;
    }

    private void getBestSeller() {
        resetSortChoice();
        binding.progressBar.setVisibility(View.VISIBLE);
        productApi m_productApi = new productApi();

        m_productApi.getTopBestSellerByStoreID(g_sStoreID, 100, new GetCollectionCallback<Product>() {
            @Override
            public void onGetListSuccess(ArrayList<Product> bestSellers) {
                g_bestSellers = new ArrayList<>(bestSellers);
                List<Task<Void>> tasks = new ArrayList<>();
                for (int i = 0; i < bestSellers.size(); i++) {
                    tasks.add(m_productApi.getProductRevenueTask(bestSellers.get(i)));
                }

                Tasks.whenAllSuccess(tasks)
                        .addOnSuccessListener(unused -> {
                            binding.progressBar.setVisibility(View.GONE);
                            g_adapter = new ProductStatisticsAdapter(requireActivity(), g_bestSellers);
                            binding.recyclerView.setAdapter(g_adapter);
                            binding.recyclerView.setLayoutManager(new LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false));

                        })
                        .addOnFailureListener(e -> {
                            showToast(requireActivity(), INTERNET_ERROR);
                        });
            }

            @Override
            public void onGetListFailure(String errorMessage) {
                showToast(requireActivity(), errorMessage);
            }
        });
    }

    private void setupEvents() {


        binding.txtSort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popUpSortDialog();
            }
        });


    }

    private void popUpSortDialog() {
        Dialog dialog = new Dialog(requireActivity(), android.R.style.Theme_Material_Light_Dialog);
        DialogSortStatisticProductBinding dialogBinding = DialogSortStatisticProductBinding.inflate(getLayoutInflater());
        dialog.setContentView(dialogBinding.getRoot());


        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        Window window = dialog.getWindow();
        if (window != null) {
            layoutParams.copyFrom(window.getAttributes());
            layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
            layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
            layoutParams.horizontalMargin = (int) (100 * getResources().getDisplayMetrics().density); // 100dp
            window.setAttributes(layoutParams);
        }

        dialog.show();
        Animation slideUp = AnimationUtils.loadAnimation(requireActivity(), R.anim.slide_in_from_right);
        dialogBinding.getRoot().startAnimation(slideUp);

        if (g_sortChoice == 0) {
            dialogBinding.rdoSortBySold.setChecked(true);
            binding.txtSortChoice.setText("Số lượng bán");
        } else {
            dialogBinding.rdoSortByRevenue.setChecked(true);
            binding.txtSortChoice.setText("Doanh thu sản phẩm");
        }

        dialogBinding.rdoGroup.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.rdoSortBySold) {
                g_sortChoice = 0;
            } else if (checkedId == R.id.rdoSortByRevenue) {
                g_sortChoice = 1;
            }
        });


        dialogBinding.btnClose.setOnClickListener(v -> dialog.dismiss());


        dialogBinding.btnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                sortProduct(g_bestSellers, g_sortChoice, g_adapter);
                binding.txtSortChoice.setText((g_sortChoice == 0) ? "Số lượng bán" : "Doanh thu sản phẩm");
                dialog.dismiss();
            }
        });
    }

    private void sortProduct(ArrayList<Product> products, int sortChoice, ProductStatisticsAdapter adapter) {



        if (sortChoice == 1) {
            products.sort((product1, product2) -> {
                double revenue1 = product1.getProductRevenue();
                double revenue2 = product2.getProductRevenue();
                return Double.compare(revenue2, revenue1);
            });
        } else {
            products.sort((product1, product2) -> {
                double revenue1 = product1.getSold();
                double revenue2 = product2.getSold();
                return Double.compare(revenue2, revenue1);
            });
        }
        Handler handler = new Handler();
        binding.progressBar.setVisibility(View.VISIBLE);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                adapter.notifyDataSetChanged();
                binding.progressBar.setVisibility(View.GONE);
            }
        }, 500);

    }


}

