package Fragments.Delivery;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.example.stores.databinding.FragmentWithOnlyRecyclerviewBinding;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import Adapters.Invoices.RequestInvoiceAdapter;
import models.CartItem;
import models.Invoice;
import models.Product;

public class DeliveryCancelledFragment extends Fragment {
    FragmentWithOnlyRecyclerviewBinding binding;
    ArrayList<Invoice> invoices = new ArrayList<>();

    @Nullable
    @Override

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentWithOnlyRecyclerviewBinding.inflate(getLayoutInflater());
        fakeInvoices();
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
        binding.progressBar.setVisibility(View.GONE);
        RequestInvoiceAdapter adapter = new RequestInvoiceAdapter(requireActivity(), invoices);
        binding.recyclerView.setAdapter(adapter);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false));
    }

    private void fakeInvoices() {

        CartItem cartItem = new CartItem();
        ArrayList<Product> listProducts = new ArrayList<>();
        Product product1 = new Product(
                "Trang phục Custom Luffy GEAR 4 Arc Dressrosa cực cháy",
                "Trang phục Custom Luffy GEAR 4 Arc Dressrosa cực cháy",
                359000,
                359000,
                130,
                "o4qWt9OUVo6UE3i6dizL",
                1);
        Product product2 = new Product(
                "Trang phục Custom Zoro Arc Wanno phiên bản giới hạn",
                "Trang phục Custom Zoro Arc Wanno phiên bản giới hạn",
                399000,
                399000,
                110,
                "o4qWt9OUVo6UE3i6dizL",
                2);
        listProducts.add(product1);
        listProducts.add(product2);
        cartItem.setStoreName("One Piece Clothes");
        cartItem.setListProducts(listProducts);


        String deliveryAddress = "Ngoc Dai | 0968191001\nQuận 12, Hồ Chí Minh";
        String createdDate = generateTime();
        String paidDate = "";
        String giveToDeliveryDate = "";
        String completedDate = "";
        String note = "";
        int paymentMethod = 0; // 0: Thanh toán khi nhận hàng
        int orderStatus = 2; // 2: Đã hủy
        String customerID = "wGtZaW1XiQjnZDGz702F";
        String customerName = "Ngoc Dai";
//
//        Invoice newInvoice = new Invoice(
//                deliveryAddress,
//                createdDate,
//                paidDate,
//                giveToDeliveryDate,
//                completedDate,
//                getTotalForCartItem(cartItem),
//                note,
//                paymentMethod,
//                orderStatus,
//                cartItem,
//                customerID,
//                customerName);
//        newInvoice.setInvoiceID(generateInvoiceId(15));
//
//        invoices.add(newInvoice);
    }

    private double getTotalForCartItem(CartItem cartItem) {
        double fee = 0;

        for (Product product : cartItem.getListProducts()) {
            if (product.getCheckedStatus()) {
                fee += (product.getOldPrice() * product.getNumberInCart());
            }

        }

        return fee;
    }

    private String generateTime() {
        Calendar calendar = Calendar.getInstance();
        Date currentDate = calendar.getTime();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd, HH:mm");
        String invoiceId = dateFormat.format(currentDate);
        return invoiceId;
    }

    private String generateInvoiceId(int random) {
        Calendar calendar = Calendar.getInstance();
        Date currentDate = calendar.getTime();
        // Định dạng chuỗi ID cho hóa đơn
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        String invoiceId = dateFormat.format(currentDate) + random;
        return invoiceId;
    }
}
