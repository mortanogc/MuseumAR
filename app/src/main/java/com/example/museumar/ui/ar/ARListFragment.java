// ARListFragment.java
package com.example.museumar.ui.ar;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.example.museumar.R;
import com.example.museumar.model.Exhibit;
import com.example.museumar.ui.exhibits.ExhibitsAdapter;
import com.example.museumar.ui.exhibits.ExhibitsViewModel;

import java.util.List;

public class ARListFragment extends Fragment {

    private ExhibitsViewModel exhibitsViewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        exhibitsViewModel = new ViewModelProvider(requireActivity()).get(ExhibitsViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ar_list, container, false);

        GridView gridView = view.findViewById(R.id.ar_exhibit_grid);

        exhibitsViewModel.getExhibitList().observe(getViewLifecycleOwner(), new Observer<List<Exhibit>>() {
            @Override
            public void onChanged(List<Exhibit> exhibits) {
                ExhibitsAdapter exhibitsAdapter = new ExhibitsAdapter(getContext(), R.layout.item_exhibit, exhibits, true);
                gridView.setAdapter(exhibitsAdapter);
            }
        });

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ExhibitsAdapter adapter = (ExhibitsAdapter) parent.getAdapter();
                Exhibit selectedExhibit = adapter.getExhibitByPosition(position);

                Bundle bundle = new Bundle();
                bundle.putString("exhibitId", selectedExhibit.getId());
                Navigation.findNavController(view).navigate(R.id.action_arListFragment_to_exhibitDetailsFragment, bundle);
            }
        });

        return view;
    }
}
