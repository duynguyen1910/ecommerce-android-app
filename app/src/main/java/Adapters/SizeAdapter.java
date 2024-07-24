package Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.stores.R;
import com.example.stores.databinding.ItemSizeBinding;

import java.util.ArrayList;

public class SizeAdapter extends RecyclerView.Adapter<SizeAdapter.ViewHolder> {
    private Context context;
    ArrayList<String> list;
    int selectedPosition = -1;
    int lastSelectedPosition = -1;

    public SizeAdapter(Context context, ArrayList<String> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemSizeBinding binding = ItemSizeBinding.inflate(LayoutInflater.from(context), parent, false);
        return new ViewHolder(binding);
    }
    public static class ViewHolder extends RecyclerView.ViewHolder{

        ItemSizeBinding binding;
        public ViewHolder(ItemSizeBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String itemSize = list.get(holder.getAdapterPosition());
        holder.binding.txtSize.setText(itemSize);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lastSelectedPosition = selectedPosition;
                selectedPosition = holder.getBindingAdapterPosition();
                notifyItemChanged(lastSelectedPosition);
                notifyItemChanged(selectedPosition);
            }
        });
        if (selectedPosition == holder.getBindingAdapterPosition()){
            holder.binding.layoutSize.setBackgroundResource(R.drawable.size_selected);
            holder.binding.txtSize.setTextColor(R.color.green);
        }else {
            holder.binding.layoutSize.setBackgroundResource(R.drawable.size_unselected);
            holder.binding.txtSize.setTextColor(R.color.black);
        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
