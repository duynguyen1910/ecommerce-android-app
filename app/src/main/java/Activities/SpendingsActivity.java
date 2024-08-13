package Activities;
import static constants.keyName.USER_ID;
import static constants.keyName.USER_INFO;
import static utils.Cart.CartUtils.showToast;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import com.example.stores.databinding.ActivitySpendingsBinding;
import java.util.ArrayList;
import java.util.Objects;
import api.invoiceApi;
import interfaces.GetCollectionCallback;
import interfaces.GetAggregate.GetManyAggregateCallback;
import utils.Chart.CustomValueMoneyFormatter;
import utils.Chart.DrawChartUtils;
import utils.TimeUtils;


public class SpendingsActivity extends AppCompatActivity {


    private String customerID;
    ActivitySpendingsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySpendingsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initUI();
        getCustomerID();
        setupEvents();


    }

    @Override
    protected void onResume() {
        super.onResume();
        getSpendingInHaftYear();
    }

    private void initUI() {
        getWindow().setStatusBarColor(Color.parseColor("#F04D7F"));
        Objects.requireNonNull(getSupportActionBar()).hide();

    }

    private void getSpendingInHaftYear() {
        binding.progressBar.setVisibility(View.VISIBLE);
        invoiceApi m_invoiceApi = new invoiceApi();
        int m_currentMonth = TimeUtils.getCurrentMonthValue();
        int m_currentYear = TimeUtils.getCurrentYearValue();

        binding.txtCurrentMonth.setText(TimeUtils.setMonthText(m_currentYear, m_currentMonth));

        m_invoiceApi.getSpendingsInHaftYearByCustomerID(customerID, m_currentYear, m_currentMonth, new GetCollectionCallback<Double>() {
            @Override
            public void onGetListSuccess(ArrayList<Double> listSpendings) {
                binding.progressBar.setVisibility(View.GONE);
                DrawChartUtils.drawSpendingBarChart(SpendingsActivity.this, listSpendings, m_currentMonth, binding.barChart);
            }

            @Override
            public void onGetListFailure(String errorMessage) {
                binding.progressBar.setVisibility(View.GONE);
                showToast(SpendingsActivity.this, errorMessage);

            }
        });

        m_invoiceApi.getCustomerMonthCountInvoice(customerID, m_currentYear, m_currentMonth, new GetManyAggregateCallback() {
            @Override
            public void onSuccess(double... aggregateResults) {
                float currentMonthSpending = (float) aggregateResults[0];
                int countMonthInvoice = (int) aggregateResults[1];

                binding.txtCurrentMonthSpending.setText(new CustomValueMoneyFormatter().getFormattedValue(currentMonthSpending));
                binding.txtCurrentMonthCountInvoice.setText(countMonthInvoice + " đơn");
            }

            @Override
            public void onFailure(String errorMessage) {
                Log.d("getCountOfInvoiceInAMonth", errorMessage);
            }
        });

    }
    private void setupEvents() {
        binding.btnBack.setOnClickListener(v -> finish());
    }

    private void getCustomerID() {
        SharedPreferences sharedPreferences = getSharedPreferences(USER_INFO, MODE_PRIVATE);
        customerID = sharedPreferences.getString(USER_ID, null);
    }


}