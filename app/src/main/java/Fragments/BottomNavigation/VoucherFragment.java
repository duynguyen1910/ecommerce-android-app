package Fragments.BottomNavigation;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.stores.databinding.FragmentEmptyBinding;
import com.google.firebase.firestore.FirebaseFirestore;


public class VoucherFragment extends Fragment {
    FragmentEmptyBinding binding;
    @Nullable
    @Override

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentEmptyBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }
}
