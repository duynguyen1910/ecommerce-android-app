package Adapters;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.stores.databinding.ItemProductBinding;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

import Models.Product;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {
    private Context context;
    ArrayList<Product> list;

    public ProductAdapter(Context context, ArrayList<Product> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ProductAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemProductBinding binding = ItemProductBinding.inflate(LayoutInflater.from(context), parent, false);
        return new ViewHolder(binding);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        ItemProductBinding binding;
        public ViewHolder(ItemProductBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ProductAdapter.ViewHolder holder, int position) {

        Product product = list.get(holder.getBindingAdapterPosition());
        holder.binding.txtTitle.setText(product.getTitle());

        // Create a NumberFormat instance for the desired locale
        NumberFormat formatter = NumberFormat.getInstance(new Locale("vi", "VN"));

// Format the price
        String formattedPrice = formatter.format(product.getPrice());

// Set the formatted price text
        holder.binding.txtPrice.setText("đ" + formattedPrice);

        holder.binding.txtSold.setText("Đã bán " + product.getSold());

        holder.binding.txtRating.setText("(" + product.getRating() + ")");
        holder.binding.ratingBar.setRating((float) product.getRating());
        Glide.with(context).load(product.getPicUrl().get(0)).into(holder.binding.imageView);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(context, DetailActivity.class);
//                intent.putExtra("object", product);
//                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
