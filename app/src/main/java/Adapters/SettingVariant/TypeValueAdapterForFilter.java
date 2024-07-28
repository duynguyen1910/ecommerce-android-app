package Adapters.SettingVariant;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.stores.databinding.ItemTypeValueForDialogBinding;
import java.util.ArrayList;
import interfaces.FilterListener;
import models.TypeValue;
import utils.DecorateUtils;

public class TypeValueAdapterForFilter extends RecyclerView.Adapter<TypeValueAdapterForFilter.ViewHolder> {
    private final Context context;
    private final ArrayList<TypeValue> list;
    private final FilterListener listener;
    private int selectedPosition;


    public TypeValueAdapterForFilter(Context context, ArrayList<TypeValue> list, int selectedPosition, FilterListener listener) {
        this.context = context;
        this.list = list;
        this.selectedPosition = selectedPosition;
        this.listener = listener;
    }

    @NonNull
    @Override
    public TypeValueAdapterForFilter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemTypeValueForDialogBinding binding = ItemTypeValueForDialogBinding.inflate(LayoutInflater.from(context), parent, false);
        return new ViewHolder(binding);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        ItemTypeValueForDialogBinding binding;
        public ViewHolder(ItemTypeValueForDialogBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull TypeValueAdapterForFilter.ViewHolder holder, int position) {
        TypeValue typeValue = list.get(holder.getBindingAdapterPosition());

        holder.binding.chkValue.setText(typeValue.getValue());

        if (selectedPosition == holder.getBindingAdapterPosition()) {
            DecorateUtils.decorateSelectedCompoundButton(context, holder.binding.chkValue);
        } else {
            DecorateUtils.decorateUnselectedCompoundButton(context, holder.binding.chkValue);
        }
        holder.binding.chkValue.setOnClickListener(v -> {
            int previousSelectedPosition = selectedPosition;
            selectedPosition = holder.getBindingAdapterPosition();
            // Cập nhật background cho item trước đó và item hiện tại
            notifyItemChanged(previousSelectedPosition);
            notifyItemChanged(selectedPosition);
            listener.transfer(selectedPosition, getFilterRange(typeValue.getValue()));
        });


    }

    public String[] getFilterRange(String string) {
        return string.replace("k", "").split("-");
    }
    public void resetSelectedPos(){
        int previousSelectedPosition = selectedPosition;
        selectedPosition = -1;
        notifyItemChanged(previousSelectedPosition);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
