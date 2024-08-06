package Adapters.BuyProduct;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.stores.R;
import com.example.stores.databinding.ItemVariantForBuyingBinding;
import java.util.ArrayList;

import interfaces.InAdapter.CategoryDialogListener;
import interfaces.InAdapter.ItemSelectionListener;
import models.Variant;
import utils.DecorateUtils;


public class VariantGridAdapter extends RecyclerView.Adapter<VariantGridAdapter.ViewHolder> {
    private final Context context;
    ArrayList<Variant> list;
    private int selectedPosition = -1;
    private final ItemSelectionListener<Variant> listener;
    String tag = "variant";

    public VariantGridAdapter(Context context, ArrayList<Variant> list, int selectedPosition, ItemSelectionListener<Variant> listener) {
        this.context = context;
        this.list = list;
        this.listener = listener;
        this.selectedPosition = selectedPosition;
    }

    @NonNull
    @Override
    public VariantGridAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemVariantForBuyingBinding binding = ItemVariantForBuyingBinding.inflate(LayoutInflater.from(context), parent, false);
        return new ViewHolder(binding);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        ItemVariantForBuyingBinding binding;
        public ViewHolder(ItemVariantForBuyingBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull VariantGridAdapter.ViewHolder holder, int position) {
        Variant variant = list.get(holder.getBindingAdapterPosition());
        if(variant.getVariantImageUrl() != null) {
            Glide.with(context).load(variant.getVariantImageUrl()).centerCrop().into(holder.binding.variantImage);
        }
        holder.binding.txtVariantName.setText(variant.getVariantName());


        if (selectedPosition == holder.getBindingAdapterPosition()) {
            DecorateUtils.decorateBorderSelectedView(holder.binding.layout);
        } else {
            holder.binding.layout.setBackgroundResource(R.drawable.custom_border_unselected);
        }
        holder.itemView.setOnClickListener(v -> {
            int previousSelectedPosition = selectedPosition;
            selectedPosition = holder.getBindingAdapterPosition();
            // Cập nhật background cho item trước đó và item hiện tại
            notifyItemChanged(previousSelectedPosition);
            notifyItemChanged(selectedPosition);
            listener.transferInfo(selectedPosition, variant);
            Log.d(tag, selectedPosition + variant.getVariantName());
            Log.d(tag, "\n-------------------");
        });

    }



    @Override
    public int getItemCount() {
        return list.size();
    }
}
