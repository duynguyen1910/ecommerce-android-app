package Adapters.Invoices;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.stores.databinding.ItemProductForInvoiceBinding;
import java.util.ArrayList;
import models.InvoiceDetail;
import utils.FormatHelper;

public class InvoiceDetailAdapter extends RecyclerView.Adapter<InvoiceDetailAdapter.ViewHolder> {
    private final Context context;
    private final ArrayList<InvoiceDetail> list;
    private int itemCount = 0;

    public InvoiceDetailAdapter(Context context, ArrayList<InvoiceDetail> list) {
        this.context = context;
        this.list = list;
    }

    public InvoiceDetailAdapter(Context context, ArrayList<InvoiceDetail> list, int itemCount) {
        this.context = context;
        this.list = list;
        this.itemCount = itemCount;
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
        InvoiceDetail detail = list.get(holder.getBindingAdapterPosition());

        holder.binding.txtProductTitle.setText(detail.getProductName());

        holder.binding.txtNewPrice.setText(FormatHelper.formatVND(detail.getNewPrice()));
        holder.binding.txtOldPrice.setText(FormatHelper.formatVND(detail.getOldPrice()));
        holder.binding.txtOldPrice.setPaintFlags(holder.binding.txtOldPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

        holder.binding.txtQuantity.setText("x" + detail.getQuantity());
//        Glide.with(context).load(product.getProductImages().get(0)).into(holder.binding.imageView);

    }

    @Override
    public int getItemCount() {
        return itemCount > 0 ? itemCount : list.size();
    }
}