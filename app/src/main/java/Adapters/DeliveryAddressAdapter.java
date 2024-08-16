package Adapters;

import static android.content.Context.MODE_PRIVATE;
import static constants.keyName.CATEGORY_ID;
import static constants.keyName.CATEGORY_NAME;
import static constants.keyName.DEFAULT_ADDRESS_ID;
import static constants.keyName.FULLNAME;
import static constants.keyName.PHONE_NUMBER;
import static constants.keyName.USER_ID;
import static constants.keyName.USER_INFO;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.stores.databinding.ItemUserAddressBinding;

import java.util.ArrayList;
import models.UserAddress;

public class DeliveryAddressAdapter extends RecyclerView.Adapter<DeliveryAddressAdapter.ViewHolder> {
    private final Context context;
    private final ArrayList<UserAddress> list;
    private String selectedRadio = "";

    public DeliveryAddressAdapter(Context context, ArrayList<UserAddress> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public DeliveryAddressAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemUserAddressBinding binding = ItemUserAddressBinding.inflate(
                LayoutInflater.from(context), parent, false);
        return new ViewHolder(binding);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ItemUserAddressBinding binding;
        public ViewHolder(ItemUserAddressBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull DeliveryAddressAdapter.ViewHolder holder, int position) {
        UserAddress userAddress = list.get(holder.getBindingAdapterPosition());
        SharedPreferences sharedPreferences = context.getSharedPreferences(USER_INFO, MODE_PRIVATE);

        String fullname = sharedPreferences.getString(FULLNAME, null);
        String phoneNumber = sharedPreferences.getString(PHONE_NUMBER, null);
        String defaultAddressID = sharedPreferences.getString(DEFAULT_ADDRESS_ID, null);
        String address = userAddress.getWardName() + ", " +  userAddress.getDistrictName() + ", "
                + userAddress.getProvinceName();

        holder.binding.txtFullname.setText(fullname);
        holder.binding.txtPhoneNumber.setText(phoneNumber);
        holder.binding.txtDetailedAddress.setText(userAddress.getDetailedAddress());
        holder.binding.txtAddress.setText(address);

        if (selectedRadio == "") {
            selectedRadio = defaultAddressID;
        }

        holder.binding.radioButton.setChecked(userAddress.getAddressID().equals(selectedRadio));

        holder.binding.radioButton.setOnClickListener(v -> {
            selectedRadio = userAddress.getAddressID();
            notifyDataSetChanged();
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public String getSelectedRadio() {
        return selectedRadio;
    }
}