package com.example.museumar.ui.reviews;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.museumar.R;
import com.example.museumar.model.Review;
import com.example.museumar.model.ReviewCache;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;

public class AddReviewFragment extends Fragment {

    private static final int REQUEST_CODE_PICK_IMAGE = 1;
    private static final int REQUEST_CODE_PERMISSION_READ_EXTERNAL_STORAGE = 2;

    private EditText editTextName;
    private EditText editTextReviewTitle;
    private EditText editTextReview;
    private RatingBar ratingBar;
    private Button buttonChoosePhoto;
    private Button buttonSubmitReview;
    private LinearLayout imagesContainer;

    private ArrayList<Bitmap> selectedImages = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_review, container, false);

        editTextName = view.findViewById(R.id.edit_text_name);
        editTextReviewTitle = view.findViewById(R.id.edit_text_review_title);
        editTextReview = view.findViewById(R.id.edit_text_review);
        ratingBar = view.findViewById(R.id.rating_bar);
        buttonChoosePhoto = view.findViewById(R.id.button_choose_photo);
        buttonSubmitReview = view.findViewById(R.id.button_submit_review);
        imagesContainer = view.findViewById(R.id.images_container);

        buttonChoosePhoto.setOnClickListener(v -> chooseImageFromGallery());
        buttonSubmitReview.setOnClickListener(v -> submitReview());

        return view;
    }

    private void chooseImageFromGallery() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.setType("image/*");
            startActivityForResult(intent, REQUEST_CODE_PICK_IMAGE);
        } else {
            if (ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_CODE_PERMISSION_READ_EXTERNAL_STORAGE);
            } else {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, REQUEST_CODE_PICK_IMAGE);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CODE_PERMISSION_READ_EXTERNAL_STORAGE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                chooseImageFromGallery();
            } else {
                Toast.makeText(requireActivity(), "Permission to access gallery is required to choose an image", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == REQUEST_CODE_PICK_IMAGE && resultCode == requireActivity().RESULT_OK && data != null) {
            Uri imageUri = data.getData();
            try {
                InputStream imageStream = requireActivity().getContentResolver().openInputStream(imageUri);
                Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                selectedImages.add(selectedImage);
                ImageView imageView = new ImageView(requireActivity());
                imageView.setImageBitmap(selectedImage);
                imageView.setLayoutParams(new LinearLayout.LayoutParams(200, 200));
                imageView.setAdjustViewBounds(true);
                imageView.setPadding(10, 10, 10, 10);
                imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
                imagesContainer.addView(imageView);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Toast.makeText(requireActivity(), "Error loading image", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void submitReview() {
        String name = editTextName.getText().toString();
        String reviewTitle = editTextReviewTitle.getText().toString();
        String reviewText = editTextReview.getText().toString();
        float rating = ratingBar.getRating();

        if (name.isEmpty() || reviewTitle.isEmpty() || reviewText.isEmpty()) {
            Toast.makeText(requireActivity(), "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        Review review = new Review(name, rating, reviewTitle, reviewText, selectedImages);
        ReviewCache.getInstance().addReview(review);
        ReviewCache.getInstance().saveReviews(requireContext());
        Toast.makeText(requireActivity(), "Review submitted successfully", Toast.LENGTH_SHORT).show();

        editTextName.setText("");
        editTextReviewTitle.setText("");
        editTextReview.setText("");
        ratingBar.setRating(0);
        imagesContainer.removeAllViews();
        selectedImages.clear();

        Navigation.findNavController(requireView()).navigate(R.id.action_addReviewFragment_to_reviewListFragment);
    }
}





//package com.example.museumar.ui.reviews;
//
//import android.Manifest;
//import android.content.Intent;
//import android.content.pm.PackageManager;
//import android.graphics.Bitmap;
//import android.graphics.BitmapFactory;
//import android.net.Uri;
//import android.os.Bundle;
//import android.provider.MediaStore;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.RatingBar;
//import android.widget.Toast;
//
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//import androidx.core.app.ActivityCompat;
//import androidx.core.content.ContextCompat;
//import androidx.fragment.app.Fragment;
//import androidx.navigation.Navigation;
//
//import com.example.museumar.R;
//import com.example.museumar.model.Review;
//import com.example.museumar.model.ReviewCache;
//
//import java.io.FileNotFoundException;
//import java.io.InputStream;
//import java.util.ArrayList;
//
//public class AddReviewFragment extends Fragment {
//
//    private static final int REQUEST_CODE_PICK_IMAGE = 1;
//    private static final int REQUEST_CODE_PERMISSION_READ_EXTERNAL_STORAGE = 2;
//
//    private EditText editTextName;
//    private EditText editTextReviewTitle;
//    private EditText editTextReview;
//    private RatingBar ratingBar;
//    private Button buttonChoosePhoto;
//    private Button buttonSubmitReview;
//    private LinearLayout imagesContainer;
//
//    private ArrayList<Bitmap> selectedImages = new ArrayList<>();
//
//    @Nullable
//    @Override
//    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        View view = inflater.inflate(R.layout.fragment_add_review, container, false);
//
//        editTextName = view.findViewById(R.id.edit_text_name);
//        editTextReviewTitle = view.findViewById(R.id.edit_text_review_title);
//        editTextReview = view.findViewById(R.id.edit_text_review);
//        ratingBar = view.findViewById(R.id.rating_bar);
//        buttonChoosePhoto = view.findViewById(R.id.button_choose_photo);
//        buttonSubmitReview = view.findViewById(R.id.button_submit_review);
//        imagesContainer = view.findViewById(R.id.images_container);
//
//        buttonChoosePhoto.setOnClickListener(v -> chooseImageFromGallery());
//        buttonSubmitReview.setOnClickListener(v -> submitReview());
//
//        return view;
//    }
//
//    private void chooseImageFromGallery() {
//        if (ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
//            ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_CODE_PERMISSION_READ_EXTERNAL_STORAGE);
//        } else {
//            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//            startActivityForResult(intent, REQUEST_CODE_PICK_IMAGE);
//        }
//    }
//
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        if (requestCode == REQUEST_CODE_PERMISSION_READ_EXTERNAL_STORAGE) {
//            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                chooseImageFromGallery();
//            } else {
//                Toast.makeText(requireActivity(), "Permission to access gallery is required to choose an image", Toast.LENGTH_SHORT).show();
//            }
//        }
//    }
//
//    @Override
//    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        if (requestCode == REQUEST_CODE_PICK_IMAGE && resultCode == requireActivity().RESULT_OK && data != null) {
//            Uri imageUri = data.getData();
//            try {
//                InputStream imageStream = requireActivity().getContentResolver().openInputStream(imageUri);
//                Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
//                selectedImages.add(selectedImage);
//                ImageView imageView = new ImageView(requireActivity());
//                imageView.setImageBitmap(selectedImage);
//                imageView.setLayoutParams(new LinearLayout.LayoutParams(200, 200));
//                imageView.setAdjustViewBounds(true);
//                imageView.setPadding(10, 10, 10, 10);
//                imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
//                imagesContainer.addView(imageView);
//            } catch (FileNotFoundException e) {
//                e.printStackTrace();
//                Toast.makeText(requireActivity(), "Error loading image", Toast.LENGTH_SHORT).show();
//            }
//        }
//    }
//
//    private void submitReview() {
//        String name = editTextName.getText().toString();
//        String reviewTitle = editTextReviewTitle.getText().toString();
//        String reviewText = editTextReview.getText().toString();
//        float rating = ratingBar.getRating();
//
//        if (name.isEmpty() || reviewTitle.isEmpty() || reviewText.isEmpty()) {
//            Toast.makeText(requireActivity(), "Please fill in all fields", Toast.LENGTH_SHORT).show();
//            return;
//        }
//
//        Review review = new Review(name, rating, reviewTitle, reviewText, selectedImages);
//        ReviewCache.getInstance().addReview(review);
//        ReviewCache.getInstance().saveReviews(requireContext());
//        Toast.makeText(requireActivity(), "Review submitted successfully", Toast.LENGTH_SHORT).show();
//
//
//        editTextName.setText("");
//        editTextReviewTitle.setText("");
//        editTextReview.setText("");
//        ratingBar.setRating(0);
//        imagesContainer.removeAllViews();
//        selectedImages.clear();
//
//        Navigation.findNavController(requireView()).navigate(R.id.action_addReviewFragment_to_reviewListFragment);
//    }
//}
//
//
//
//
////package com.example.museumar.ui.reviews;
////
////import android.Manifest;
////import android.content.Intent;
////import android.content.SharedPreferences;
////import android.content.pm.PackageManager;
////import android.graphics.Bitmap;
////import android.graphics.BitmapFactory;
////import android.net.Uri;
////import android.os.Bundle;
////import android.provider.MediaStore;
////import android.view.LayoutInflater;
////import android.view.View;
////import android.view.ViewGroup;
////import android.widget.Button;
////import android.widget.EditText;
////import android.widget.ImageView;
////import android.widget.LinearLayout;
////import android.widget.RatingBar;
////import android.widget.Toast;
////
////import androidx.annotation.NonNull;
////import androidx.annotation.Nullable;
////import androidx.core.app.ActivityCompat;
////import androidx.core.content.ContextCompat;
////import androidx.fragment.app.Fragment;
////
////import com.example.museumar.R;
////import com.example.museumar.model.Review;
////import com.google.gson.Gson;
////import com.google.gson.reflect.TypeToken;
////
////import java.io.FileNotFoundException;
////import java.io.InputStream;
////import java.lang.reflect.Type;
////import java.util.ArrayList;
////
////public class AddReviewFragment extends Fragment {
////
////    private static final int REQUEST_CODE_PICK_IMAGE = 1;
////    private static final int REQUEST_CODE_PERMISSION_READ_EXTERNAL_STORAGE = 2;
////
////    private EditText editTextName;
////    private EditText editTextReviewTitle;
////    private EditText editTextReview;
////    private RatingBar ratingBar;
////    private Button buttonChoosePhoto;
////    private Button buttonSubmitReview;
////    private LinearLayout imagesContainer;
////
////    private ArrayList<Bitmap> selectedImages = new ArrayList<>();
////
////    @Nullable
////    @Override
////    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
////        View view = inflater.inflate(R.layout.fragment_add_review, container, false);
////
////        editTextName = view.findViewById(R.id.edit_text_name);
////        editTextReviewTitle = view.findViewById(R.id.edit_text_review_title);
////        editTextReview = view.findViewById(R.id.edit_text_review);
////        ratingBar = view.findViewById(R.id.rating_bar);
////        buttonChoosePhoto = view.findViewById(R.id.button_choose_photo);
////        buttonSubmitReview = view.findViewById(R.id.button_submit_review);
////        imagesContainer = view.findViewById(R.id.images_container);
////
////        buttonChoosePhoto.setOnClickListener(v -> chooseImageFromGallery());
////        buttonSubmitReview.setOnClickListener(v -> submitReview());
////
////        return view;
////    }
////
////    private void chooseImageFromGallery() {
////        if (ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
////            ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_CODE_PERMISSION_READ_EXTERNAL_STORAGE);
////        } else {
////            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
////            startActivityForResult(intent, REQUEST_CODE_PICK_IMAGE);
////        }
////    }
////
////    @Override
////    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
////        if (requestCode == REQUEST_CODE_PERMISSION_READ_EXTERNAL_STORAGE) {
////            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
////                chooseImageFromGallery();
////            } else {
////                Toast.makeText(requireActivity(), "Permission to access gallery is required to choose an image", Toast.LENGTH_SHORT).show();
////            }
////        }
////    }
////
////    @Override
////    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
////        if (requestCode == REQUEST_CODE_PICK_IMAGE && resultCode == requireActivity().RESULT_OK && data != null) {
////            Uri imageUri = data.getData();
////            try {
////                InputStream imageStream = requireActivity().getContentResolver().openInputStream(imageUri);
////                Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
////                selectedImages.add(selectedImage);
////                ImageView imageView = new ImageView(requireActivity());
////                imageView.setImageBitmap(selectedImage);
////                imageView.setLayoutParams(new LinearLayout.LayoutParams(200, 200));
////                imageView.setAdjustViewBounds(true);
////                imageView.setPadding(10, 10, 10, 10);
////                imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
////                imagesContainer.addView(imageView);
////            } catch (FileNotFoundException e) {
////                e.printStackTrace();
////                Toast.makeText(requireActivity(), "Error loading image", Toast.LENGTH_SHORT).show();
////            }
////        }
////    }
////
////    private void submitReview() {
////        String name = editTextName.getText().toString();
////        String reviewTitle = editTextReviewTitle.getText().toString();
////        String reviewText = editTextReview.getText().toString();
////        float rating = ratingBar.getRating();
////
////        if (name.isEmpty() || reviewTitle.isEmpty() || reviewText.isEmpty()) {
////            Toast.makeText(requireActivity(), "Please fill in all fields", Toast.LENGTH_SHORT).show();
////            return;
////        }
////
////        Review review = new Review(name, rating, reviewTitle, reviewText, selectedImages);
////        saveReview(review);
////        Toast.makeText(requireActivity(), "Review submitted successfully", Toast.LENGTH_SHORT).show();
////
////        editTextName.setText("");
////        editTextReviewTitle.setText("");
////        editTextReview.setText("");
////        ratingBar.setRating(0);
////        imagesContainer.removeAllViews();
////        selectedImages.clear();
////    }
////
////    private void saveReview(Review review) {
////        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("reviews", 0);
////        String reviewsJson = sharedPreferences.getString("reviews_list", null);
////
////        ArrayList<Review> reviews;
////        if (reviewsJson == null) {
////            reviews = new ArrayList<>();
////        } else {
////            Gson gson = new Gson();
////            Type type = new TypeToken<ArrayList<Review>>(){}.getType();
////            reviews = gson.fromJson(reviewsJson, type);
////        }
////
////        reviews.add(review);
////
////        SharedPreferences.Editor editor = sharedPreferences.edit();
////        Gson gson = new Gson();
////        String newReviewsJson = gson.toJson(reviews);
////        editor.putString("reviews_list", newReviewsJson);
////        editor.apply();
////    }
////}
////
////
//////package com.example.museumar.ui.reviews;
//////
//////import android.content.Intent;
//////import android.graphics.Bitmap;
//////import android.os.Bundle;
//////import android.provider.MediaStore;
//////import android.view.LayoutInflater;
//////import android.view.View;
//////import android.view.ViewGroup;
//////import android.widget.Button;
//////import android.widget.EditText;
//////import android.widget.ImageView;
//////import android.widget.LinearLayout;
//////import android.widget.RatingBar;
//////
//////import androidx.annotation.NonNull;
//////import androidx.annotation.Nullable;
//////import androidx.fragment.app.Fragment;
//////
//////import com.example.museumar.R;
//////
//////import java.util.ArrayList;
//////
//////public class AddReviewFragment extends Fragment {
//////
//////    private EditText editTextName;
//////    private EditText editTextReviewTitle;
//////    private EditText editTextReview;
//////    private RatingBar ratingBar;
//////    private Button buttonChoosePhoto;
//////    private Button buttonSubmitReview;
//////    private LinearLayout imagesContainer;
//////    private ArrayList<Bitmap> selectedImages = new ArrayList<>();
//////    private static final int REQUEST_IMAGE_CAPTURE = 1;
//////
//////    @Nullable
//////    @Override
//////    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//////        View view = inflater.inflate(R.layout.fragment_add_review, container, false);
//////
//////        editTextName = view.findViewById(R.id.edit_text_name);
//////        editTextReviewTitle = view.findViewById(R.id.edit_text_review_title);
//////        editTextReview = view.findViewById(R.id.edit_text_review);
//////        ratingBar = view.findViewById(R.id.rating_bar);
//////        buttonChoosePhoto = view.findViewById(R.id.button_choose_photo);
//////        buttonSubmitReview = view.findViewById(R.id.button_submit_review);
//////        imagesContainer = view.findViewById(R.id.images_container);
//////
//////        buttonChoosePhoto.setOnClickListener(new View.OnClickListener() {
//////            @Override
//////            public void onClick(View v) {
//////                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//////                if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
//////                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
//////                }
//////            }
//////        });
//////
//////        buttonSubmitReview.setOnClickListener(new View.OnClickListener() {
//////            @Override
//////            public void onClick(View v) {
//////                // Здесь вы можете собрать информацию из полей ввода и отправить данные на сервер или обработать их локально.
//////            }
//////        });
//////
//////        return view;
//////    }
//////
//////    @Override
//////    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//////        super.onActivityResult(requestCode, resultCode, data);
//////
//////        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == getActivity().RESULT_OK) {
//////            Bundle extras = data.getExtras();
//////            Bitmap imageBitmap = (Bitmap) extras.get("data");
//////            selectedImages.add(imageBitmap);
//////            updateImagesContainer();
//////        }
//////    }
//////
//////    private void updateImagesContainer() {
//////        imagesContainer.removeAllViews();
//////        for (Bitmap bitmap : selectedImages) {
//////            ImageView imageView = new ImageView(getContext());
//////            imageView.setImageBitmap(bitmap);
//////            imageView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
//////            imageView.setAdjustViewBounds(true);
//////            imageView.setPadding(4, 4, 4, 4);
//////            imagesContainer.addView(imageView);
//////        }
//////    }
//////}
//////
//////
//////
////////package com.example.museumar.ui.reviews;
////////
////////import android.Manifest;
////////import android.content.Intent;
////////import android.content.pm.PackageManager;
////////import android.graphics.Bitmap;
////////import android.graphics.ImageDecoder;
////////import android.graphics.drawable.BitmapDrawable;
////////import android.graphics.drawable.Drawable;
////////import android.net.Uri;
////////import android.os.Build;
////////import android.os.Bundle;
////////import android.provider.MediaStore;
////////import android.view.LayoutInflater;
////////import android.view.View;
////////import android.view.ViewGroup;
////////import android.widget.Button;
////////import android.widget.EditText;
////////import android.widget.ImageView;
////////import android.widget.LinearLayout;
////////import android.widget.RatingBar;
////////import android.widget.Toast;
////////
////////import androidx.annotation.NonNull;
////////import androidx.annotation.Nullable;
////////import androidx.core.content.ContextCompat;
////////import androidx.fragment.app.Fragment;
////////
////////import com.example.museumar.R;
////////import com.example.museumar.model.Review;
////////import com.example.museumar.model.ReviewCache;
////////
////////import java.io.IOException;
////////import java.util.ArrayList;
////////
////////public class AddReviewFragment extends Fragment {
////////
////////    private static final int PICK_IMAGE_REQUEST = 1;
////////    private static final int PERMISSION_REQUEST_CODE = 2;
////////
////////    private EditText editTextName;
////////    private EditText editTextReviewTitle;
////////    private EditText editTextReview;
////////    private RatingBar ratingBar;
////////    private LinearLayout imagesContainer;
////////    private Button buttonChoosePhoto;
////////    private Button buttonSubmitReview;
////////    private ArrayList<Bitmap> images;
////////
////////    @Nullable
////////    @Override
////////    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
////////        View view = inflater.inflate(R.layout.fragment_add_review, container, false);
////////
////////        editTextName = view.findViewById(R.id.edit_text_name);
////////        editTextReviewTitle = view.findViewById(R.id.edit_text_review_title);
////////        editTextReview = view.findViewById(R.id.edit_text_review);
////////        ratingBar = view.findViewById(R.id.rating_bar);
////////        imagesContainer = view.findViewById(R.id.images_container);
////////        buttonChoosePhoto = view.findViewById(R.id.button_choose_photo);
////////        buttonSubmitReview = view.findViewById(R.id.button_submit_review);
////////        images = new ArrayList<>();
////////
////////        buttonChoosePhoto.setOnClickListener(new View.OnClickListener() {
////////            @Override
////////            public void onClick(View view) {
////////                checkPermissionAndOpenGallery();
////////            }
////////        });
////////
////////        buttonSubmitReview.setOnClickListener(new View.OnClickListener() {
////////            @Override
////////            public void onClick(View view) {
////////                submitReview();
////////            }
////////        });
////////
////////        return view;
////////    }
////////
////////    private void checkPermissionAndOpenGallery() {
////////        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
////////            if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
////////                openGallery();
////////            } else {
////////                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
////////            }
////////        } else {
////////            openGallery();
////////        }
////////    }
////////
////////    private void openGallery() {
////////        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
////////        intent.setType("image/*");
////////        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
////////        startActivityForResult(intent, PICK_IMAGE_REQUEST);
////////    }
////////
////////    private void submitReview() {
////////        String name = editTextName.getText().toString().trim();
////////        String reviewTitle = editTextReviewTitle.getText().toString().trim();
////////        String reviewText = editTextReview.getText().toString().trim();
////////        float rating = ratingBar.getRating();
////////
////////        if (name.isEmpty()) {
////////            Toast.makeText(getContext(), "Пожалуйста, введите ваше имя", Toast.LENGTH_SHORT).show();
////////        } else if (reviewTitle.isEmpty()) {
////////            Toast.makeText(getContext(), "Пожалуйста, введите заголовок отзыва", Toast.LENGTH_SHORT).show();
////////        } else if (rating == 0) {
////////                Toast.makeText(getContext(), "Пожалуйста, укажите вашу оценку", Toast.LENGTH_SHORT).show();
////////} else if (images.isEmpty()) {
////////            Toast.makeText(getContext(), "Пожалуйста, выберите хотя бы одно изображение", Toast.LENGTH_SHORT).show();
////////        } else {
////////            saveReview(name, reviewTitle, reviewText, rating, images);
////////            Toast.makeText(getContext(), "Отзыв успешно отправлен", Toast.LENGTH_SHORT).show();
////////            getActivity().onBackPressed();
////////        }
////////    }
////////
////////    private void saveReview(String name, String reviewTitle, String reviewText, float rating, ArrayList<Bitmap> images) {
////////        Review review = new Review(name, rating, reviewTitle, reviewText, images);
////////        ReviewCache.getInstance().addReview(review);
////////        ReviewCache.getInstance().saveReviews(getContext());
////////    }
////////
////////    @Override
////////    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
////////        super.onActivityResult(requestCode, resultCode, data);
////////        if (requestCode == PICK_IMAGE_REQUEST && resultCode == getActivity().RESULT_OK && data != null) {
////////            if (data.getClipData() != null) { // Если выбрано несколько изображений
////////                for (int i = 0; i < data.getClipData().getItemCount(); i++) {
////////                    Uri imageUri = data.getClipData().getItemAt(i).getUri();
////////
////////                    try {
////////                        Bitmap bitmap;
////////                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.P) {
////////                            ImageDecoder.Source source = ImageDecoder.createSource(getActivity().getContentResolver(), imageUri);
////////                            bitmap = ImageDecoder.decodeBitmap(source);
////////                        } else {
////////                            bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), imageUri);
////////                        }
////////                        images.add(bitmap);
////////                        ImageView imageView = new ImageView(getContext());
////////                        imageView.setImageBitmap(bitmap);
////////                        imagesContainer.addView(imageView);
////////                    } catch (IOException e) {
////////                        e.printStackTrace();
////////                    }
////////                }
////////            } else if (data.getData() != null) { // Если выбрано одно изображение
////////                Uri imageUri = data.getData();
////////
////////                try {
////////                    Bitmap bitmap;
////////                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.P) {
////////                        ImageDecoder.Source source = ImageDecoder.createSource(getActivity().getContentResolver(), imageUri);
////////                        bitmap = ImageDecoder.decodeBitmap(source);
////////                    } else {
////////                        bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), imageUri);
////////                    }
////////                    images.add(bitmap);
////////                    ImageView imageView = new ImageView(getContext());
////////                    imageView.setImageBitmap(bitmap);
////////                    imagesContainer.addView(imageView);
////////                } catch (IOException e) {
////////                    e.printStackTrace();
////////                }
////////            }
////////        }
////////    }
////////
////////    @Override
////////    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
////////        if (requestCode == PERMISSION_REQUEST_CODE) {
////////            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
////////                openGallery();
////////            } else {
////////                Toast.makeText(getContext(), "Разрешение не предоставлено", Toast.LENGTH_SHORT).show();
////////            }
////////        }
////////    }
////////}
////////
////////
////////
//////////package com.example.museumar.ui.reviews;
//////////
//////////import android.Manifest;
//////////import android.content.Intent;
//////////import android.content.pm.PackageManager;
//////////import android.graphics.Bitmap;
//////////import android.graphics.ImageDecoder;
//////////import android.graphics.drawable.BitmapDrawable;
//////////import android.graphics.drawable.Drawable;
//////////import android.net.Uri;
//////////import android.os.Build;
//////////import android.os.Bundle;
//////////import android.provider.MediaStore;
//////////import android.view.LayoutInflater;
//////////import android.view.View;
//////////import android.view.ViewGroup;
//////////import android.widget.Button;
//////////import android.widget.EditText;
//////////import android.widget.ImageView;
//////////import android.widget.RatingBar;
//////////import android.widget.Toast;
//////////
//////////import androidx.annotation.NonNull;
//////////import androidx.annotation.Nullable;
//////////import androidx.core.content.ContextCompat;
//////////import androidx.fragment.app.Fragment;
//////////
//////////import com.example.museumar.R;
//////////import com.example.museumar.model.Review;
//////////import com.example.museumar.model.ReviewCache;
//////////
//////////import java.io.IOException;
//////////
//////////public class AddReviewFragment extends Fragment {
//////////
//////////    private static final int PICK_IMAGE_REQUEST = 1;
//////////    private static final int PERMISSION_REQUEST_CODE = 2;
//////////
//////////    private EditText editTextName;
//////////    private EditText editTextReviewTitle;
//////////    private EditText editTextReview;
//////////    private RatingBar ratingBar;
//////////    private ImageView reviewImage;
//////////    private Button buttonChoosePhoto;
//////////    private Button buttonSubmitReview;
//////////
//////////    @Nullable
//////////    @Override
//////////    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//////////        View view = inflater.inflate(R.layout.fragment_add_review, container, false);
//////////
//////////        editTextName = view.findViewById(R.id.edit_text_name);
//////////        editTextReviewTitle = view.findViewById(R.id.edit_text_review_title);
//////////        editTextReview = view.findViewById(R.id.edit_text_review);
//////////        ratingBar = view.findViewById(R.id.rating_bar);
//////////        reviewImage = view.findViewById(R.id.image_view_photo);
//////////        buttonChoosePhoto = view.findViewById(R.id.button_choose_photo);
//////////        buttonSubmitReview = view.findViewById(R.id.button_submit_review);
//////////
//////////        buttonChoosePhoto.setOnClickListener(new View.OnClickListener() {
//////////            @Override
//////////            public void onClick(View view) {
//////////                checkPermissionAndOpenGallery();
//////////            }
//////////        });
//////////
//////////        buttonSubmitReview.setOnClickListener(new View.OnClickListener() {
//////////            @Override
//////////            public void onClick(View view) {
//////////                submitReview();
//////////            }
//////////        });
//////////
//////////        return view;
//////////    }
//////////
//////////    private void checkPermissionAndOpenGallery() {
//////////        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//////////            if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
//////////                openGallery();
//////////            } else {
//////////                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
//////////            }
//////////        } else {
//////////            openGallery();
//////////        }
//////////    }
//////////
//////////    private void openGallery() {
//////////        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
//////////        intent.setType("image/*");
//////////        startActivityForResult(intent, PICK_IMAGE_REQUEST);
//////////    }
//////////
//////////
//////////    private void submitReview() {
//////////        String name = editTextName.getText().toString().trim();
//////////        String reviewTitle = editTextReviewTitle.getText().toString().trim();
//////////        String reviewText = editTextReview.getText().toString().trim();
//////////        float rating = ratingBar.getRating();
//////////        Drawable drawable = reviewImage.getDrawable();
//////////
//////////        if (name.isEmpty()) {
//////////            Toast.makeText(getContext(), "Пожалуйста, введите ваше имя", Toast.LENGTH_SHORT).show();
//////////        } else if (reviewTitle.isEmpty()) {
//////////            Toast.makeText(getContext(), "Пожалуйста, введите заголовок отзыва", Toast.LENGTH_SHORT).show();
//////////        } else if (rating == 0) {
//////////            Toast.makeText(getContext(), "Пожалуйста, укажите вашу оценку", Toast.LENGTH_SHORT).show();
//////////        } else if (drawable == null) {
//////////            Toast.makeText(getContext(), "Пожалуйста, выберите хотя бы одно изображение", Toast.LENGTH_SHORT).show();
//////////        } else {
//////////            Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
//////////            saveReview(name, reviewTitle, reviewText, rating, bitmap);
//////////            Toast.makeText(getContext(), "Отзыв успешно отправлен", Toast.LENGTH_SHORT).show();
//////////            getActivity().onBackPressed();
//////////        }
//////////    }
//////////
//////////    private void saveReview(String name, String reviewTitle, String reviewText, float rating, Bitmap bitmap) {
//////////        Review review = new Review(name, rating, reviewTitle, reviewText, bitmap);
//////////        ReviewCache.getInstance().addReview(review);
//////////        ReviewCache.getInstance().saveReviews(getContext());
//////////    }
//////////
//////////    @Override
//////////    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//////////        super.onActivityResult(requestCode, resultCode, data);
//////////
//////////        if (requestCode == PICK_IMAGE_REQUEST && resultCode == getActivity().RESULT_OK && data != null && data.getData() != null) {
//////////            Uri imageUri = data.getData();
//////////
//////////            try {
//////////                Bitmap bitmap;
//////////                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.P) {
//////////                    ImageDecoder.Source source = ImageDecoder.createSource(getActivity().getContentResolver(), imageUri);
//////////                    bitmap = ImageDecoder.decodeBitmap(source);
//////////                } else {
//////////                    bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), imageUri);
//////////                }
//////////                reviewImage.setImageBitmap(bitmap);
//////////            } catch (IOException e) {
//////////                e.printStackTrace();
//////////            }
//////////        }
//////////    }
//////////
//////////    @Override
//////////    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//////////        if (requestCode == PERMISSION_REQUEST_CODE) {
//////////            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//////////                openGallery();
//////////            } else {
//////////                Toast.makeText(getContext(), "Разрешение не предоставлено", Toast.LENGTH_SHORT).show();
//////////            }
//////////        }
//////////    }
//////////}
//////////
//////////
//////////
//////////
////////////package com.example.museumar.ui.reviews;
////////////
////////////import android.Manifest;
////////////import android.content.Intent;
////////////import android.content.pm.PackageManager;
////////////import android.graphics.Bitmap;
////////////import android.graphics.drawable.BitmapDrawable;
////////////import android.graphics.drawable.Drawable;
////////////import android.net.Uri;
////////////import android.os.Bundle;
////////////import android.provider.MediaStore;
////////////import android.view.LayoutInflater;
////////////import android.view.View;
////////////import android.view.ViewGroup;
////////////import android.widget.Button;
////////////import android.widget.EditText;
////////////import android.widget.ImageView;
////////////import android.widget.RatingBar;
////////////import android.widget.Toast;
////////////
////////////import androidx.annotation.NonNull;
////////////import androidx.annotation.Nullable;
////////////import androidx.core.content.ContextCompat;
////////////import androidx.fragment.app.Fragment;
////////////
////////////import com.example.museumar.R;
////////////import com.example.museumar.model.Review;
////////////import com.example.museumar.model.ReviewCache;
////////////
////////////import java.io.IOException;
////////////
////////////public class AddReviewFragment extends Fragment {
////////////
////////////    private static final int PICK_IMAGE_REQUEST = 1;
////////////    private static final int PERMISSION_REQUEST_CODE = 2;
////////////
////////////    private EditText editTextName;
////////////    private EditText editTextReviewTitle;
////////////    private EditText editTextReview;
////////////    private RatingBar ratingBar;
////////////    private ImageView reviewImage;
////////////    private Button buttonChoosePhoto;
////////////    private Button buttonSubmitReview;
////////////
////////////    @Nullable
////////////    @Override
////////////    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
////////////        View view = inflater.inflate(R.layout.fragment_add_review, container, false);
////////////
////////////        editTextName = view.findViewById(R.id.edit_text_name);
////////////        editTextReviewTitle = view.findViewById(R.id.edit_text_review_title);
////////////        editTextReview = view.findViewById(R.id.edit_text_review);
////////////        ratingBar = view.findViewById(R.id.rating_bar);
////////////        reviewImage = view.findViewById(R.id.image_view_photo);
////////////        buttonChoosePhoto = view.findViewById(R.id.button_choose_photo);
////////////        buttonSubmitReview = view.findViewById(R.id.button_submit_review);
////////////
////////////        buttonChoosePhoto.setOnClickListener(new View.OnClickListener() {
////////////            @Override
////////////            public void onClick(View view) {
////////////                checkPermissionAndOpenGallery();
////////////            }
////////////        });
////////////
////////////        buttonSubmitReview.setOnClickListener(new View.OnClickListener() {
////////////            @Override
////////////            public void onClick(View view) {
////////////                submitReview();
////////////            }
////////////        });
////////////
////////////        return view;
////////////    }
////////////
////////////    private void checkPermissionAndOpenGallery() {
////////////        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
////////////            openGallery();
////////////        } else {
////////////            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
////////////        }
////////////    }
////////////
////////////    private void openGallery() {
////////////        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
////////////        startActivityForResult(intent, PICK_IMAGE_REQUEST);
////////////    }
////////////
////////////
////////////    private void submitReview() {
////////////        String name = editTextName.getText().toString().trim();
////////////        String reviewTitle = editTextReviewTitle.getText().toString().trim();
////////////        String reviewText = editTextReview.getText().toString().trim();
////////////        float rating = ratingBar.getRating();
////////////        Drawable drawable = reviewImage.getDrawable();
////////////
////////////        if (name.isEmpty()) {
////////////            Toast.makeText(getContext(), "Пожалуйста, введите ваше имя", Toast.LENGTH_SHORT).show();
////////////        } else if (reviewTitle.isEmpty()) {
////////////            Toast.makeText(getContext(), "Пожалуйста, введите заголовок отзыва", Toast.LENGTH_SHORT).show();
////////////        } else if (rating == 0) {
////////////            Toast.makeText(getContext(), "Пожалуйста, укажите вашу оценку", Toast.LENGTH_SHORT).show();
////////////        } else if (drawable == null) {
////////////            Toast.makeText(getContext(), "Пожалуйста, выберите хотя бы одно изображение", Toast.LENGTH_SHORT).show();
////////////        } else {
////////////            Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
////////////            saveReview(name, reviewTitle, reviewText, rating, bitmap);
////////////            Toast.makeText(getContext(), "Отзыв успешно отправлен", Toast.LENGTH_SHORT).show();
////////////            getActivity().onBackPressed();
////////////        }
////////////    }
////////////
////////////    private void saveReview(String name, String reviewTitle, String reviewText, float rating, Bitmap bitmap) {
////////////        Review review = new Review(name, rating, reviewTitle, reviewText, bitmap);
////////////        ReviewCache.getInstance().addReview(review);
////////////        ReviewCache.getInstance().saveReviews(getContext());
////////////    }
////////////
////////////
////////////    @Override
////////////    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
////////////        super.onActivityResult(requestCode, resultCode, data);
////////////
////////////        if (requestCode == PICK_IMAGE_REQUEST && resultCode == getActivity().RESULT_OK && data != null && data.getData() != null) {
////////////            Uri imageUri = data.getData();
////////////
////////////            try {
////////////                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), imageUri);
////////////                reviewImage.setImageBitmap(bitmap);
////////////            } catch (IOException e) {
////////////                e.printStackTrace();
////////////            }
////////////        }
////////////    }
////////////
////////////    @Override
////////////    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
////////////        if (requestCode == PERMISSION_REQUEST_CODE) {
////////////            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
////////////                openGallery();
////////////            } else {
////////////                Toast.makeText(getContext(), "Разрешение не предоставлено", Toast.LENGTH_SHORT).show();
////////////            }
////////////        }
////////////    }
////////////}
////////////
////////////
////////////
//////////////package com.example.museumar.ui.reviews;
//////////////
//////////////import android.Manifest;
//////////////import android.content.Intent;
//////////////import android.content.pm.PackageManager;
//////////////import android.graphics.Bitmap;
//////////////import android.graphics.drawable.Drawable;
//////////////import android.net.Uri;
//////////////import android.os.Bundle;
//////////////import android.provider.MediaStore;
//////////////import android.view.LayoutInflater;
//////////////import android.view.View;
//////////////import android.view.ViewGroup;
//////////////import android.widget.Button;
//////////////import android.widget.EditText;
//////////////import android.widget.ImageView;
//////////////import android.widget.RatingBar;
//////////////import android.widget.Toast;
//////////////
//////////////import androidx.annotation.NonNull;
//////////////import androidx.annotation.Nullable;
//////////////import androidx.core.content.ContextCompat;
//////////////import androidx.fragment.app.Fragment;
//////////////
//////////////import com.example.museumar.R;
//////////////
//////////////import java.io.IOException;
//////////////
//////////////public class AddReviewFragment extends Fragment {
//////////////
//////////////    private static final int PICK_IMAGE_REQUEST = 1;
//////////////    private static final int PERMISSION_REQUEST_CODE = 2;
//////////////
//////////////    private EditText editTextName;
//////////////    private EditText editTextReviewTitle;
//////////////    private EditText editTextReview;
//////////////    private RatingBar ratingBar;
//////////////    private ImageView reviewImage;
//////////////    private Button buttonChoosePhoto;
//////////////    private Button buttonSubmitReview;
//////////////
//////////////    @Nullable
//////////////    @Override
//////////////    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//////////////        View view = inflater.inflate(R.layout.fragment_add_review, container, false);
//////////////
//////////////        editTextName = view.findViewById(R.id.edit_text_name);
//////////////        editTextReviewTitle = view.findViewById(R.id.edit_text_review_title);
//////////////        editTextReview = view.findViewById(R.id.edit_text_review);
//////////////        ratingBar = view.findViewById(R.id.rating_bar);
//////////////        reviewImage = view.findViewById(R.id.image_view_photo);
//////////////        buttonChoosePhoto = view.findViewById(R.id.button_choose_photo);
//////////////        buttonSubmitReview = view.findViewById(R.id.button_submit_review);
//////////////
//////////////        buttonChoosePhoto.setOnClickListener(new View.OnClickListener() {
//////////////            @Override
//////////////            public void onClick(View view) {
//////////////                checkPermissionAndOpenGallery();
//////////////            }
//////////////        });
//////////////
//////////////        buttonSubmitReview.setOnClickListener(new View.OnClickListener() {
//////////////            @Override
//////////////            public void onClick(View view) {
//////////////                submitReview();
//////////////            }
//////////////        });
//////////////
//////////////        return view;
//////////////    }
//////////////
//////////////    private void checkPermissionAndOpenGallery() {
//////////////        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
//////////////            openGallery();
//////////////        } else {
//////////////            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
//////////////        }
//////////////    }
//////////////
//////////////    private void openGallery() {
//////////////        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//////////////        startActivityForResult(intent, PICK_IMAGE_REQUEST);
//////////////    }
//////////////
//////////////    private void submitReview() {
//////////////        String name = editTextName.getText().toString().trim();
//////////////        String reviewTitle = editTextReviewTitle.getText().toString().trim();
//////////////        String reviewText = editTextReview.getText().toString().trim();
//////////////        float rating = ratingBar.getRating();
//////////////        Drawable drawable = reviewImage.getDrawable();
//////////////
//////////////        if (name.isEmpty()) {
//////////////            Toast.makeText(getContext(), "Пожалуйста, введите ваше имя", Toast.LENGTH_SHORT).show();
//////////////        } else if (reviewTitle.isEmpty()) {
//////////////            Toast.makeText(getContext(), "Пожалуйста, введите заголовок отзыва", Toast.LENGTH_SHORT).show();
//////////////        } else if (rating == 0) {
//////////////            Toast.makeText(getContext(), "Пожалуйста, укажите вашу оценку", Toast.LENGTH_SHORT).show();
//////////////        } else if (drawable == null) {
//////////////            Toast.makeText(getContext(), "Пожалуйста, выберите хотя бы одно изображение", Toast.LENGTH_SHORT).show();
//////////////        } else {
//////////////            // Здесь добавьте ваш код для отправки отзыва
//////////////            Toast.makeText(getContext(), "Отзыв успешно отправлен", Toast.LENGTH_SHORT).show();
//////////////        }
//////////////    }
//////////////
//////////////    @Override
//////////////    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//////////////        super.onActivityResult(requestCode, resultCode, data);
//////////////
//////////////        if (requestCode == PICK_IMAGE_REQUEST && resultCode == getActivity().RESULT_OK && data != null && data.getData() != null) {
//////////////            Uri imageUri = data.getData();
//////////////
//////////////            try {
//////////////                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), imageUri);
//////////////                reviewImage.setImageBitmap(bitmap);
//////////////            } catch (IOException e) {
//////////////                e.printStackTrace();
//////////////            }
//////////////        }
//////////////    }
//////////////
//////////////    @Override
//////////////    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//////////////        if (requestCode == PERMISSION_REQUEST_CODE) {
//////////////            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//////////////                openGallery();
//////////////            } else {
//////////////                Toast.makeText(getContext(), "Разрешение не предоставлено", Toast.LENGTH_SHORT).show();
//////////////            }
//////////////        }
//////////////    }
//////////////}
//////////////
//////////////
//////////////
//////////////
//////////////
//////////////
////////////////package com.example.museumar.ui.reviews;
////////////////
////////////////import android.app.Activity;
////////////////import android.content.Intent;
////////////////import android.graphics.Bitmap;
////////////////import android.graphics.drawable.BitmapDrawable;
////////////////import android.os.Bundle;
////////////////import android.provider.MediaStore;
////////////////import android.view.LayoutInflater;
////////////////import android.view.View;
////////////////import android.view.ViewGroup;
////////////////import android.widget.Button;
////////////////import android.widget.EditText;
////////////////import android.widget.ImageView;
////////////////import android.widget.RatingBar;
////////////////import android.widget.Toast;
////////////////
////////////////import androidx.annotation.NonNull;
////////////////import androidx.annotation.Nullable;
////////////////import androidx.fragment.app.Fragment;
////////////////import androidx.navigation.Navigation;
////////////////
////////////////import com.example.museumar.R;
////////////////import com.example.museumar.model.Review;
////////////////import com.example.museumar.model.ReviewCache;
////////////////
////////////////import java.io.IOException;
////////////////
////////////////public class AddReviewFragment extends Fragment {
////////////////
////////////////    private static final int PICK_IMAGE_REQUEST = 1;
////////////////    private EditText reviewTitleInput;
////////////////    private RatingBar reviewRating;
////////////////    private EditText reviewContentInput;
////////////////    private ImageView reviewImageView;
////////////////
////////////////    @Nullable
////////////////    @Override
////////////////    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
////////////////        return inflater.inflate(R.layout.fragment_add_review, container, false);
////////////////    }
////////////////
////////////////    @Override
////////////////    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
////////////////        super.onViewCreated(view, savedInstanceState);
////////////////
////////////////        reviewTitleInput = view.findViewById(R.id.edit_text_name);
////////////////        reviewRating = view.findViewById(R.id.rating_bar);
////////////////        reviewContentInput = view.findViewById(R.id.edit_text_review);
////////////////        reviewImageView = view.findViewById(R.id.image_view_photo);
////////////////
////////////////        Button reviewImageButton = view.findViewById(R.id.button_choose_photo);
////////////////        reviewImageButton.setOnClickListener(v -> openGallery());
////////////////
////////////////        Button submitReviewButton = view.findViewById(R.id.button_submit_review);
////////////////        submitReviewButton.setOnClickListener(v -> submitReview());
////////////////    }
////////////////
////////////////    private void openGallery() {
////////////////        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
////////////////        startActivityForResult(intent, PICK_IMAGE_REQUEST);
////////////////    }
////////////////
////////////////    @Override
////////////////    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
////////////////        super.onActivityResult(requestCode, resultCode, data);
////////////////
////////////////        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
////////////////            try {
////////////////                Bitmap bitmap = MediaStore.Images.Media.getBitmap(requireActivity().getContentResolver(), data.getData());
////////////////                reviewImageView.setImageBitmap(bitmap);
////////////////            } catch (IOException e) {
////////////////                e.printStackTrace();
////////////////            }
////////////////        }
////////////////    }
////////////////
////////////////    private void submitReview() {
////////////////        String authorName = reviewTitleInput.getText().toString().trim();
////////////////        float rating = reviewRating.getRating();
////////////////        String title = reviewTitleInput.getText().toString().trim();
////////////////        String reviewContent = reviewContentInput.getText().toString().trim();
////////////////
////////////////        if (authorName.isEmpty() || title.isEmpty() || reviewContent.isEmpty()) {
////////////////            Toast.makeText(requireContext(), "Заголовок, текст отзыва и имя автора обязательны", Toast.LENGTH_SHORT).show();
////////////////            return;
////////////////        }
////////////////
////////////////        if (rating < 1) {
////////////////            Toast.makeText(requireContext(), "Оценка должна быть больше или равна 1", Toast.LENGTH_SHORT).show();
////////////////            return;
////////////////        }
////////////////
////////////////        if (reviewImageView.getDrawable() == null) {
////////////////            Toast.makeText(requireContext(), "Выберите хотя бы одну картинку", Toast.LENGTH_SHORT).show();
////////////////            return;
////////////////        }
////////////////
////////////////        Bitmap reviewImage = ((BitmapDrawable) reviewImageView.getDrawable()).getBitmap();
////////////////
////////////////        // Сохранение отзыва в локальном хранилище
////////////////        Review review = new Review(authorName, rating, title, reviewContent, reviewImage);
////////////////        ReviewCache.getInstance().addReview(review);
////////////////        ReviewCache.getInstance().saveReviews(requireContext());
////////////////
////////////////        // Возврат к списку отзывов
////////////////        Navigation.findNavController(requireView()).navigate(R.id.action_addReviewFragment_to_reviewListFragment);
////////////////    }
////////////////
////////////////}
