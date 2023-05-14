package com.example.museumar.ui.exhibits;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.example.museumar.R;
import com.example.museumar.model.Exhibit;
import com.example.museumar.ui.ar.ARActivity;

public class ExhibitDetailsFragment extends Fragment {

    private ExhibitsViewModel exhibitsViewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        exhibitsViewModel = new ViewModelProvider(requireActivity()).get(ExhibitsViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_exhibit_details, container, false);

        ImageView imageView = view.findViewById(R.id.exhibit_image);
        TextView nameTextView = view.findViewById(R.id.exhibit_name);
        TextView descriptionTextView = view.findViewById(R.id.exhibit_description);

        String exhibitId = getArguments().getString("exhibitId");

        Exhibit selectedExhibit = findExhibitById(exhibitId);
        if (selectedExhibit != null) {
            nameTextView.setText(selectedExhibit.getName());
            descriptionTextView.setText(selectedExhibit.getDescription());
            Glide.with(getContext()).load(selectedExhibit.getImageUrl()).into(imageView);

            Button arButton = view.findViewById(R.id.ar_button);

            if (selectedExhibit.isARCapable()) {
                arButton.setVisibility(View.VISIBLE);
                arButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity(), ARActivity.class);
                        intent.putExtra("exhibitId", selectedExhibit.getId());
                        startActivity(intent);
                    }
                });
            } else {
                arButton.setVisibility(View.GONE);
            }

        }

        return view;
    }

    private Exhibit findExhibitById(String id) {
        for (Exhibit exhibit : exhibitsViewModel.getExhibitList().getValue()) {
            if (exhibit.getId().equals(id)) {
                return exhibit;
            }
        }
        return null;
    }
}
