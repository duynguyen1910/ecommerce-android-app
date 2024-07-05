package Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.stores.R;
import com.example.stores.databinding.ItemCartBinding;
import com.google.android.material.checkbox.MaterialCheckBox;

import java.util.ArrayList;

import Interfaces.CartItemListener;
import Interfaces.ToTalFeeCallback;
import Models.CartItem;
import Models.Product;
import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {
    private final Context context;
    private final ArrayList<CartItem> list;
    private final ToTalFeeCallback totalFeeCallback;

    public CartAdapter(Context context, ArrayList<CartItem> list, ToTalFeeCallback totalFeeCallback) {
        this.context = context;
        this.list = list;
        this.totalFeeCallback = totalFeeCallback;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemCartBinding binding = ItemCartBinding.inflate(LayoutInflater.from(context), parent, false);
        return new ViewHolder(binding);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        ItemCartBinding binding;

        public ViewHolder(ItemCartBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        CartItem cartItem = list.get(holder.getBindingAdapterPosition());
        ArrayList<Product> listProducts = cartItem.getListProducts();

        holder.binding.txtStoreName.setText(cartItem.getStoreName());
        ProductsListAdapterForCartItem adapter = new ProductsListAdapterForCartItem(context, cartItem.getListProducts(), totalFeeCallback, new CartItemListener() {
            @Override
            public void updateCartItem() {
                int cartItemPosition = holder.getBindingAdapterPosition();
                list.remove(cartItemPosition);
                totalFeeCallback.totalFeeUpdate(getTotalFee());
                notifyItemRemoved(cartItemPosition);
            }

            @Override
            public void updateCheckboxAllStatus(boolean isChecked) {
                holder.binding.checkboxAll.setChecked(isChecked);
            }
        });

        holder.binding.recyclerViewProducts.setLayoutManager(new LinearLayoutManager(context));
        holder.binding.recyclerViewProducts.setAdapter(adapter);
        holder.binding.checkboxAll.addOnCheckedStateChangedListener(new MaterialCheckBox.OnCheckedStateChangedListener() {
            @Override
            public void onCheckedStateChangedListener(@NonNull MaterialCheckBox checkBox, int state) {
                if (state == MaterialCheckBox.STATE_CHECKED) {
                    // Nếu checkboxAll được check, check tất cả các sản phẩm
                    for (Product product : listProducts) {
                        product.setCheckedStatus(true);
                    }
                } else if (state == MaterialCheckBox.STATE_UNCHECKED) {
                    // Nếu checkboxAll được uncheck, uncheck tất cả các sản phẩm
                    for (Product product : listProducts) {
                        product.setCheckedStatus(false);
                    }
                }
                // Thông báo adapter cập nhật dữ liệu
                adapter.notifyDataSetChanged();
                // Cập nhật tổng phí
                totalFeeCallback.totalFeeUpdate(getTotalFee());
            }
        });



        ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int productPosition = viewHolder.getBindingAdapterPosition();
                cartItem.getListProducts().remove(productPosition);
                totalFeeCallback.totalFeeUpdate(getTotalFee());
                adapter.notifyItemRemoved(productPosition);
                if (cartItem.getListProducts().size() == 0) {
                    int cartItemPosition = holder.getBindingAdapterPosition();
                    list.remove(cartItemPosition);
                    notifyItemRemoved(cartItemPosition);
                    totalFeeCallback.totalFeeUpdate(getTotalFee());
                }

            }


            @Override
            public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                new RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                        .addSwipeLeftBackgroundColor(ContextCompat.getColor(context, R.color.primary_color))
                        .addActionIcon(R.drawable.ic_delete)
                        .setSwipeLeftLabelColor(ContextCompat.getColor(context, R.color.white))
                        .addSwipeLeftLabel("Delete")
                        .setIconHorizontalMargin(TypedValue.COMPLEX_UNIT_DIP, 16)
                        .addCornerRadius(TypedValue.COMPLEX_UNIT_DIP, 12)
                        .setSwipeLeftLabelTextSize(TypedValue.COMPLEX_UNIT_SP, 14)
                        .create()
                        .decorate();
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        };
        new ItemTouchHelper(simpleCallback).attachToRecyclerView(holder.binding.recyclerViewProducts);



    }

    private double getTotalFee() {
        double fee = 0;
        for (CartItem item : list) {
            for (Product product : item.getListProducts()) {
                if (product.getCheckedStatus()){
                    fee += product.getPrice() * product.getNumberInCart();
                }

            }
        }
        return fee;
    }


    @Override
    public int getItemCount() {
        return list.size();
    }
}
