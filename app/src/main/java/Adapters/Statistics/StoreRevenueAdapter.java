package Adapters.Statistics;
import static constants.keyName.STORE_ID;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.stores.databinding.ItemStoreRevenueBinding;

import java.util.ArrayList;

import Activities.BuyProduct.ProductDetailActivity;
import Activities.StoreSetup.ViewMyStoreActivity;
import models.Store;
import utils.Chart.CustomValueMoneyFormatter;

public class StoreRevenueAdapter extends RecyclerView.Adapter<StoreRevenueAdapter.ViewHolder> {
    private final Context context;
    private final ArrayList<Store> list;

    public StoreRevenueAdapter(Context context, ArrayList<Store> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemStoreRevenueBinding binding = ItemStoreRevenueBinding.inflate(LayoutInflater.from(context), parent, false);
        return new ViewHolder(binding);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        ItemStoreRevenueBinding binding;

        public ViewHolder(ItemStoreRevenueBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }


    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Store store = list.get(holder.getBindingAdapterPosition());
        holder.binding.txtIndex.setText(String.valueOf(holder.getBindingAdapterPosition() + 1));
        holder.binding.txtStoreName.setText(store.getStoreName());

        holder.binding.txtProductRevenue.setText(new CustomValueMoneyFormatter().getFormattedValue((float) store.getStoreRevenue()));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ViewMyStoreActivity.class);
                intent.putExtra(STORE_ID, store.getBaseID());
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
