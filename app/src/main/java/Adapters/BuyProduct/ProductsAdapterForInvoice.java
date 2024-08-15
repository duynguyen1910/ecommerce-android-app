package Adapters.BuyProduct;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.stores.databinding.ItemProductForInvoiceBinding;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

import models.Product;
import models.Variant;
import utils.FormatHelper;

public class ProductsAdapterForInvoice extends RecyclerView.Adapter<ProductsAdapterForInvoice.ViewHolder> {
    private final Context context;
    private final ArrayList<Variant> list;

    public ProductsAdapterForInvoice(Context context, ArrayList<Variant> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemProductForInvoiceBinding binding = ItemProductForInvoiceBinding.inflate(LayoutInflater.from(context), parent, false);
        return new ViewHolder(binding);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        ItemProductForInvoiceBinding binding;

        public ViewHolder(ItemProductForInvoiceBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Variant variant = list.get(holder.getBindingAdapterPosition());
        if(variant.getVariantImageUrl() != null) {
            Glide.with(context).load(variant.getVariantImageUrl()).into(holder.binding.imageView);
        }
        holder.binding.txtProductName.setText(variant.getProductName());
        if (variant.getVariantName() != null){
            holder.binding.txtVariantName.setText(variant.getVariantName());
        }else {
            holder.binding.txtVariantName.setVisibility(View.GONE);
        }
        holder.binding.txtNewPrice.setText(FormatHelper.formatVND(variant.getNewPrice()));
        holder.binding.txtOldPrice.setText(FormatHelper.formatVND(variant.getOldPrice()));
        holder.binding.txtOldPrice.setPaintFlags(holder.binding.txtOldPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        holder.binding.txtQuantity.setText("x" + variant.getNumberInCart());

    }
    @Override
    public int getItemCount() {
        return list.size();
    }
}
