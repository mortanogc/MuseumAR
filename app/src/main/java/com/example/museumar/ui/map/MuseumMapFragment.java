package com.example.museumar.ui.map;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.alexvasilkov.gestures.views.GestureImageView;
import com.example.museumar.R;

public class MuseumMapFragment extends Fragment {

    private GestureImageView mapImageView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_museum_map, container, false);
        mapImageView = view.findViewById(R.id.mapImageView);
        return view;
    }
}
