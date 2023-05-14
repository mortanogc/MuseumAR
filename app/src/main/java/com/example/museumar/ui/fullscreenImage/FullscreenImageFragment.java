package com.example.museumar.ui.fullscreenImage;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;
import com.example.museumar.R;
import com.example.museumar.ui.reviews.FullscreenImageFragmentArgs;

public class FullscreenImageFragment extends Fragment {

    private SubsamplingScaleImageView fullscreenImage;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fullscreen_image, container, false);
        fullscreenImage = view.findViewById(R.id.fullscreen_image);
        FullscreenImageFragmentArgs args = FullscreenImageFragmentArgs.fromBundle(requireArguments());
        String imageUrl = args.getImageUrl();
        loadBase64Image(imageUrl);
        return view;
    }

    private void loadBase64Image(String base64) {
        RequestOptions requestOptions = new RequestOptions()
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true);

        Glide.with(this)
                .asBitmap()
                .apply(requestOptions)
                .load(base64ToBitmap(base64))
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        fullscreenImage.setImage(ImageSource.bitmap(resource));
                    }
                });
    }

    private Bitmap base64ToBitmap(String base64) {
        byte[] decodedString = android.util.Base64.decode(base64, android.util.Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
    }
}



//import android.os.Bundle;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//import androidx.fragment.app.Fragment;
//
//import com.davemorrissey.labs.subscaleview.ImageSource;
//import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;
//import com.example.museumar.R;
//import com.example.museumar.ui.reviews.FullscreenImageFragmentArgs;
//
//public class FullscreenImageFragment extends Fragment {
//
//    @Nullable
//    @Override
//    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        return inflater.inflate(R.layout.fragment_fullscreen_image, container, false);
//    }
//
//    @Override
//    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
//        super.onViewCreated(view, savedInstanceState);
//
//        SubsamplingScaleImageView fullscreenImage = view.findViewById(R.id.fullscreen_image);
//        String imageUrl = FullscreenImageFragmentArgs.fromBundle(requireArguments()).getImageUrl();
//
//        if (imageUrl != null) {
//            fullscreenImage.setImage(ImageSource.uri(imageUrl));
//        } else {
//            requireActivity().onBackPressed();
//        }
//    }
//}
