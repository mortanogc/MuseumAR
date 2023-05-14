package com.example.museumar.model;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class ReviewCache {
    private static final String REVIEW_CACHE_PREFS = "reviews_cache_prefs";
    private static final String REVIEW_CACHE_KEY = "reviews_cache_key";
    private static ReviewCache instance;
    private final List<Review> reviews;

    private ReviewCache() {
        reviews = new ArrayList<>();
    }

    public static ReviewCache getInstance() {
        if (instance == null) {
            instance = new ReviewCache();
        }
        return instance;
    }

    public List<Review> getReviews() {
        return reviews;
    }

    public void addReview(Review review) {
        reviews.add(review);
    }

    public float getAverageRating() {
        float sum = 0;
        for (Review review : reviews) {
            sum += review.getRating();
        }
        return reviews.size() > 0 ? sum / reviews.size() : 0;
    }

    public void saveReviews(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(REVIEW_CACHE_PREFS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(reviews);
        editor.putString(REVIEW_CACHE_KEY, json);
        editor.apply();
    }

    public void loadReviews(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(REVIEW_CACHE_PREFS, Context.MODE_PRIVATE);
        String json = sharedPreferences.getString(REVIEW_CACHE_KEY, "");
        if (!json.isEmpty()) {
            Gson gson = new Gson();
            Type type = new TypeToken<ArrayList<Review>>() {}.getType();
            List<Review> loadedReviews = gson.fromJson(json, type);
            reviews.clear();
            reviews.addAll(loadedReviews);
        }
    }
}
