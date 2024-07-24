package Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.stores.R;
import com.example.stores.databinding.FragmentReviewBinding;

import java.util.ArrayList;

public class ReviewFragment extends Fragment {
    View view;
    FragmentReviewBinding binding;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentReviewBinding.inflate(getLayoutInflater());

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        initReviewList();
    }

//    private void initReviewList() {
//        FirebaseDatabase database = FirebaseDatabase.getInstance();
//        DatabaseReference reviewRef = database.getReference("Review");
//        ArrayList<ReviewDomain> reviewList = new ArrayList<>();
//        assert getArguments() != null;
//        Query query = reviewRef.orderByChild("ItemId").equalTo(4);
//        query.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                if (snapshot.exists()){
//                    for (DataSnapshot issue : snapshot.getChildren()){
//                        reviewList.add(issue.getValue(ReviewDomain.class));
//                    }
//
//                    if (reviewList.size() > 0){
//                        recyclerViewReview.setAdapter(new ReviewAdapter(getActivity(), reviewList));
//                        recyclerViewReview.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
//                    }
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });

}
