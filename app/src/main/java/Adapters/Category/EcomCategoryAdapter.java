package Adapters.Category;
import static constants.keyName.CATEGORY_NAME;
import static constants.toastMessage.DEFAULT_REQUIRE;
import static utils.Cart.CartUtils.showToast;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.stores.R;
import com.example.stores.databinding.DialogUpdateEcomCategoryBinding;
import com.example.stores.databinding.ItemEcomCategoryBinding;
import com.zerobranch.layout.SwipeLayout;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import api.categoryApi;
import interfaces.GetCollectionCallback;
import interfaces.StatusCallback;
import models.Category;


public class EcomCategoryAdapter extends RecyclerView.Adapter<EcomCategoryAdapter.ViewHolder> {
    private final Context context;
    private final ArrayList<Category> list;
    private SwipeLayout g_CurrentSwipeLayout;

    public EcomCategoryAdapter(Context context, ArrayList<Category> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public EcomCategoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemEcomCategoryBinding binding = ItemEcomCategoryBinding.inflate(LayoutInflater.from(context), parent, false);
        return new ViewHolder(binding);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        ItemEcomCategoryBinding binding;

        public ViewHolder(ItemEcomCategoryBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull EcomCategoryAdapter.ViewHolder holder, int position) {
        Category category = list.get(holder.getBindingAdapterPosition());
        holder.binding.txtIndex.setText(String.valueOf(holder.getBindingAdapterPosition() + 1));
        holder.binding.txtCategoryName.setText(category.getCategoryName());


        holder.binding.swipeLayout.setOnActionsListener(new SwipeLayout.SwipeActionsListener() {
            @Override
            public void onOpen(int direction, boolean isContinuous) {
                if (g_CurrentSwipeLayout != null && g_CurrentSwipeLayout != holder.binding.swipeLayout) {
                    g_CurrentSwipeLayout.close(true);
                }
                g_CurrentSwipeLayout = holder.binding.swipeLayout;
            }

            @Override
            public void onClose() {
                if (g_CurrentSwipeLayout == holder.binding.swipeLayout) {
                    g_CurrentSwipeLayout = null;
                }
            }
        });

        holder.binding.btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupUpdateDialog(category);
            }
        });
    }



    private void popupUpdateDialog(Category category) {
        Dialog dialog = new Dialog(context, android.R.style.Theme_Material_Light_Dialog);
        DialogUpdateEcomCategoryBinding dialogBinding = DialogUpdateEcomCategoryBinding.inflate((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE));
        dialog.setContentView(dialogBinding.getRoot());


        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        Window window = dialog.getWindow();
        if (window != null) {
            layoutParams.copyFrom(window.getAttributes());
            layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
            layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
            layoutParams.horizontalMargin = (int) (100 * context.getResources().getDisplayMetrics().density); // 100dp
            window.setAttributes(layoutParams);
        }

        dialog.show();
        Animation slideUp = AnimationUtils.loadAnimation(context, R.anim.slide_in_from_right);
        dialogBinding.getRoot().startAnimation(slideUp);

        dialogBinding.txtCategoryName.setText(category.getCategoryName());
        dialogBinding.edtCategoryName.setText(category.getCategoryName());
        dialogBinding.btnClose.setOnClickListener(v -> dialog.dismiss());


        dialogBinding.btnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogBinding.progressBar.setVisibility(View.VISIBLE);
                categoryApi m_categoryApi = new categoryApi();
                String newCategoryName = Objects.requireNonNull(dialogBinding.edtCategoryName.getText()).toString().trim();
                if (!newCategoryName.isEmpty()){
                    Map<String, Object> updateData = new HashMap<>();
                    updateData.put(CATEGORY_NAME, newCategoryName);
                    m_categoryApi.updateCategory(updateData, category.getBaseID(), new StatusCallback() {
                        @Override
                        public void onSuccess(String successMessage) {
                            showToast(context, successMessage);
                            reloadList(dialogBinding.progressBar, dialog);
                        }

                        @Override
                        public void onFailure(String errorMessage) {
                            showToast(context, errorMessage);
                        }
                    });
                }else {
                    dialogBinding.edtCategoryName.setError(DEFAULT_REQUIRE);
                }

            }
        });
    }



    public void reloadList(ProgressBar progressBar, Dialog dialog){
        categoryApi m_categoryApi = new categoryApi();
        m_categoryApi.getAllCategoryApi(new GetCollectionCallback<Category>() {
            @Override
            public void onGetListSuccess(ArrayList<Category> categories) {
                list.clear();
                list.addAll(categories);
                notifyDataSetChanged();
                progressBar.setVisibility(View.GONE);
                dialog.dismiss();

            }

            @Override
            public void onGetListFailure(String errorMessage) {
                showToast(context, errorMessage);
                progressBar.setVisibility(View.GONE);
                dialog.dismiss();
            }
        });


    }


    @Override
    public int getItemCount() {
        return list.size();
    }
}