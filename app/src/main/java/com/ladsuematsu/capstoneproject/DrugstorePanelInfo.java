package com.ladsuematsu.capstoneproject;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.location.places.Place;
import com.ladsuematsu.capstoneproject.core.entity.PlaceEntry;

public class DrugstorePanelInfo extends InfoPanelBottomSheetDialog {
    private static final String PLACE_DATA_ARG = "place_data_arg";
    private PlaceEntry place;

    public static DrugstorePanelInfo newInstance(PlaceEntry placeEntry) {
        DrugstorePanelInfo fragment = new DrugstorePanelInfo();

        Bundle args = new Bundle();
        args.putParcelable(PLACE_DATA_ARG, placeEntry);

        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        place = getArguments().getParcelable(PLACE_DATA_ARG);
    }

    @Override
    protected void inflateContent(ViewGroup content, LayoutInflater inflater) {
        View rootView = inflater.inflate(R.layout.place_details, content);

        TextView placeName = rootView.findViewById(R.id.name);
        TextView placeAddress = rootView.findViewById(R.id.address);

        placeName.setText(place.getName());
        placeAddress.setText(place.getAddress());
    }

    @Override
    protected void setPanelTitle(TextView title) { }
}
