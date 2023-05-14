package com.example.museumar.ui.reviews;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.museumar.R;
import com.example.museumar.model.Review;
import com.example.museumar.model.ReviewCache;

import java.util.List;

public class ReviewListFragment extends Fragment {

    private RecyclerView reviewListView;
    private ReviewAdapter reviewAdapter;
    private TextView ratingTextView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_review_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        reviewListView = view.findViewById(R.id.rv_reviews);
        ratingTextView = view.findViewById(R.id.tv_average_rating);

        // Загрузка сохраненных отзывов
        ReviewCache.getInstance().loadReviews(requireContext());

        List<Review> reviews = ReviewCache.getInstance().getReviews();

        reviewAdapter = new ReviewAdapter(reviews, requireContext());

        reviewListView.setAdapter(reviewAdapter);
        reviewListView.setLayoutManager(new LinearLayoutManager(requireContext()));

        view.findViewById(R.id.fab_add_review).setOnClickListener(v ->
                Navigation.findNavController(requireView()).navigate(R.id.action_reviewListFragment_to_addReviewFragment));
    }

    @Override
    public void onResume() {
        super.onResume();
        float averageRating = ReviewCache.getInstance().getAverageRating();
        ratingTextView.setText(String.format("%.1f", averageRating));
        reviewAdapter.notifyDataSetChanged();
    }
}



//package com.example.museumar.ui.reviews;
//
//import android.os.Bundle;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.TextView;
//
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//import androidx.fragment.app.Fragment;
//import androidx.navigation.Navigation;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.example.museumar.R;
//import com.example.museumar.model.Review;
//import com.example.museumar.model.ReviewCache;
//
//import java.util.List;
//
//public class ReviewListFragment extends Fragment {
//
//    private RecyclerView reviewListView;
//    private ReviewAdapter reviewAdapter;
//    private TextView ratingTextView;
//
//    @Nullable
//    @Override
//    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        return inflater.inflate(R.layout.fragment_review_list, container, false);
//    }
//
//    @Override
//    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
//        super.onViewCreated(view, savedInstanceState);
//
//        reviewListView = view.findViewById(R.id.rv_reviews);
//        ratingTextView = view.findViewById(R.id.tv_average_rating);
//
//        // Загрузка сохраненных отзывов
//        ReviewCache.getInstance().loadReviews(requireContext());
//
//        List<Review> reviews = ReviewCache.getInstance().getReviews();
//
//        reviewAdapter = new ReviewAdapter(reviews, requireContext());
//
//        reviewListView.setAdapter(reviewAdapter);
//        reviewListView.setLayoutManager(new LinearLayoutManager(requireContext()));
//
//        view.findViewById(R.id.fab_add_review).setOnClickListener(v ->
//                Navigation.findNavController(requireView()).navigate(R.id.action_reviewListFragment_to_addReviewFragment));
//    }
//
//    @Override
//    public void onResume() {
//        super.onResume();
//        float averageRating = ReviewCache.getInstance().getAverageRating();
//        ratingTextView.setText(String.format("%.1f", averageRating));
//        reviewAdapter.notifyDataSetChanged();
//    }
//}
