package com.example.museumar.ui.exhibits;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.example.museumar.R;
import com.example.museumar.model.Exhibit;

import java.util.List;
import java.util.stream.Collectors;

public class ExhibitsAdapter extends ArrayAdapter<Exhibit> {
    private Context context;
    private int resource;
    private List<Exhibit> filteredExhibits;

    public ExhibitsAdapter(Context context, int resource, List<Exhibit> objects, boolean showARCapableOnly) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;

        if (showARCapableOnly) {
            filteredExhibits = objects.stream()
                    .filter(Exhibit::isARCapable)
                    .collect(Collectors.toList());
        } else {
            filteredExhibits = objects;
        }
    }

    @Override
    public int getCount() {
        return filteredExhibits.size();
    }

    @Nullable
    @Override
    public Exhibit getItem(int position) {
        return filteredExhibits.get(position);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(resource, null);
        }

        ImageView imageView = convertView.findViewById(R.id.exhibit_image);
        TextView textView = convertView.findViewById(R.id.exhibit_name);

        Exhibit exhibit = getItem(position);

        if (exhibit != null) {
            Glide.with(context)
                    .load(exhibit.getImageUrl())
                    .placeholder(R.drawable.placeholder_image)
                    .into(imageView);
            textView.setText(exhibit.getName());
        }

        return convertView;
    }

    public Exhibit getExhibitByPosition(int position) {
        return filteredExhibits.get(position);
    }
}
