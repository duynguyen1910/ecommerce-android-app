package Adapters;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.stores.databinding.ItemPopularBrandBinding;
import java.util.ArrayList;
import models.OfficialBrand;

public class PopularBrandAdapter extends RecyclerView.Adapter<PopularBrandAdapter.ViewHolder> {
    private Context context;
    private ArrayList<OfficialBrand> list;

    public PopularBrandAdapter(Context context, ArrayList<OfficialBrand> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public PopularBrandAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
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
    public void onBindViewHolder(@NonNull PopularBrandAdapter.ViewHolder holder, int position) {

        OfficialBrand categoryDomain = list.get(holder.getBindingAdapterPosition());
        holder.binding.title.setText(categoryDomain.getTitle());
        Glide.with(context).load(categoryDomain.getPicUrl()).into(holder.binding.image);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
