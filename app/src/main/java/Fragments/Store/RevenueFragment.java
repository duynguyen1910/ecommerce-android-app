package Fragments.Store;
import static android.content.Context.MODE_PRIVATE;
import static constants.keyName.STORE_ID;
import static constants.keyName.USER_INFO;
import static utils.Cart.CartUtils.showToast;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.example.stores.databinding.FragmentRevenueBinding;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicReference;
import api.invoiceApi;
import interfaces.GetAggregate.GetManyAggregateCallback;
import interfaces.GetCollectionCallback;
import utils.Chart.DrawChartUtils;
import utils.FormatHelper;
import utils.TimeUtils;

public class RevenueFragment extends Fragment {
    FragmentRevenueBinding binding;
    String g_sStoreID;

    @Nullable
    @Override

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentRevenueBinding.inflate(getLayoutInflater());
        getStoreID();
        return binding.getRoot();
    }


    @Override
    public void onResume() {
        super.onResume();
        getRevenueForAllMonths();
    }

    private void getRevenueForAllMonths(){
        binding.progressBar.setVisibility(View.VISIBLE);
        invoiceApi m_invoiceApi = new invoiceApi();
        int m_currentMonth = TimeUtils.getCurrentMonthValue();
        int m_currentYear = TimeUtils.getCurrentYearValue();
        binding.txtCurrentYear.setText(String.valueOf(m_currentYear));

        m_invoiceApi.getRevenueForAllMonthsByStoreID(g_sStoreID, m_currentYear, new GetCollectionCallback<Double>() {
            @Override
            public void onGetListSuccess(ArrayList<Double> listRevenues) {
                binding.progressBar.setVisibility(View.GONE);
                AtomicReference<Double> yearRevenue = new AtomicReference<>((double) 0);
                listRevenues.forEach(item -> {
                    yearRevenue.updateAndGet(v -> v + item);
                });

                binding.txtRevenueInYear.setText(FormatHelper.formatVND(yearRevenue.get()));
                DrawChartUtils.drawRevenuesBarChart(requireActivity(), listRevenues,m_currentMonth, binding.barChart);
                DrawChartUtils.drawRevenuesLineChart(requireActivity(), listRevenues, binding.lineChart);
            }

            @Override
            public void onGetListFailure(String errorMessage) {
                binding.progressBar.setVisibility(View.GONE);
                showToast(requireActivity(), errorMessage);

            }
        });

        m_invoiceApi.getStoreMonthStatistics(g_sStoreID, m_currentYear, m_currentMonth, new GetManyAggregateCallback() {
            @Override
            public void onSuccess(double... aggregateResults) {
                double currentMonthRevenue = aggregateResults[0];
                int countMonthInvoice = (int) aggregateResults[1];

                binding.txtCurrentMonth.setText(TimeUtils.setMonthText(m_currentYear, m_currentMonth));
                binding.txtCurrentMonthRevenue.setText(FormatHelper.formatVND(currentMonthRevenue));
                binding.txtCurrentMonthCountInvoice.setText(countMonthInvoice + " đơn");
            }

            @Override
            public void onFailure(String errorMessage) {
                Log.d("getCountOfInvoiceInAMonth", errorMessage);
            }
        });
    }
    private void getStoreID() {
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences(USER_INFO, MODE_PRIVATE);
        g_sStoreID = sharedPreferences.getString(STORE_ID, null);
    }



}