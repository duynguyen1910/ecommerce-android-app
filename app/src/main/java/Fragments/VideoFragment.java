package Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.stores.databinding.FragmentProfileBinding;
import com.example.stores.databinding.FragmentVideoBinding;

public class VideoFragment extends Fragment {
    FragmentVideoBinding binding;
    @Nullable
    @Override

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentVideoBinding.inflate(getLayoutInflater());

        return binding.getRoot();
    }
}
