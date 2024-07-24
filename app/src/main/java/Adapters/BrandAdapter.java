package Adapters;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.stores.databinding.ItemPopularBrandBinding;
import java.util.ArrayList;
import models.Brand;

public class BrandAdapter extends RecyclerView.Adapter<BrandAdapter.ViewHolder> {
    private Context context;
    private ArrayList<Brand> list;

    public BrandAdapter(Context context, ArrayList<Brand> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public BrandAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemPopularBrandBinding binding = ItemPopularBrandBinding.inflate(LayoutInflater.from(context), parent, false);
        return new ViewHolder(binding);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        ItemPopularBrandBinding binding;
        public ViewHolder(ItemPopularBrandBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull BrandAdapter.ViewHolder holder, int position) {

        Brand categoryDomain = list.get(holder.getBindingAdapterPosition());
        holder.binding.title.setText(categoryDomain.getBrandName());
        Glide.with(context).load(categoryDomain.getPicUrl()).into(holder.binding.image);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
