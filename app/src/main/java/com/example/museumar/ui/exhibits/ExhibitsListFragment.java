package com.example.museumar.ui.exhibits;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.example.museumar.R;
import com.example.museumar.model.Exhibit;

import java.util.List;

public class ExhibitsListFragment extends Fragment {

    private ExhibitsViewModel exhibitsViewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        exhibitsViewModel = new ViewModelProvider(requireActivity()).get(ExhibitsViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_exhibit_list, container, false);

        GridView gridView = view.findViewById(R.id.grid_view);

        exhibitsViewModel.getExhibitList().observe(getViewLifecycleOwner(), new Observer<List<Exhibit>>() {
            @Override
            public void onChanged(List<Exhibit> exhibits) {
                ExhibitsAdapter exhibitsAdapter = new ExhibitsAdapter(getContext(), R.layout.item_exhibit, exhibits, false); // Обратите внимание на передачу дополнительного параметра false
                gridView.setAdapter(exhibitsAdapter);
            }
        });

        gridView.setOnItemClickListener((parent, view1, position, id) -> {
            Exhibit selectedExhibit = exhibitsViewModel.getExhibitList().getValue().get(position);

            Bundle bundle = new Bundle();
            bundle.putString("exhibitId", selectedExhibit.getId());
            Navigation.findNavController(view).navigate(R.id.action_exhibitsListFragment_to_exhibitDetailsFragment, bundle);
        });

        return view;
    }
}
