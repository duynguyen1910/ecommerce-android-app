package Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.stores.R;
import com.example.stores.databinding.ItemMyProductBinding;
import com.example.stores.databinding.LayoutProductDetailBinding;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Objects;

import Activities.UpdateProductsActivity;
import models.Product;

public class MyProductsAdapter extends RecyclerView.Adapter<MyProductsAdapter.ViewHolder> {
    private final Context context;
    private final ArrayList<Product> list;

    public MyProductsAdapter(Context context, ArrayList<Product> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       ItemMyProductBinding binding = ItemMyProductBinding.inflate(LayoutInflater.from(context), parent, false);
        return new ViewHolder(binding);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

       ItemMyProductBinding binding;

        public ViewHolder(ItemMyProductBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }


    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Product product = list.get(holder.getBindingAdapterPosition());

        holder.binding.txtTitle.setText(product.getTitle());
        NumberFormat formatter = NumberFormat.getInstance(new Locale("vi", "VN"));
        String formattedOldPrice = formatter.format(product.getOldPrice());
        holder.binding.txtOldPrice.setText("đ" + formattedOldPrice);

        Glide.with(context).load(product.getPicUrl().get(0)).into(holder.binding.imageView);

        holder.binding.txtInStock.setText(String.valueOf(product.getInStock()));
        holder.binding.txtSold.setText(String.valueOf(product.getSold()));

        holder.binding.btnEdit.setOnClickListener(v -> {
            Intent intent = new Intent(context, UpdateProductsActivity.class);
            intent.putExtra("product", product);
            context.startActivity(intent);

        });


    }

    private void popUpProductDetailDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutProductDetailBinding detailBinding = LayoutProductDetailBinding.inflate((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE));
        builder.setView(detailBinding.getRoot());
        AlertDialog dialog = builder.create();

        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawableResource(R.drawable.custom_edit_text_border);
        dialog.show();

        // Set the dialog window attributes
        Window window = dialog.getWindow();
        if (window != null) {
            window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            window.setGravity(Gravity.BOTTOM);

        }

        detailBinding.imageCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        detailBinding.btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }


    @Override
    public int getItemCount() {
        return list.size();
    }
}
