package com.ladsuematsu.capstoneproject;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.location.places.Place;
import com.ladsuematsu.capstoneproject.core.data.persistence.WidgetDataProvider;
import com.ladsuematsu.capstoneproject.core.di.component.AppComponent;
import com.ladsuematsu.capstoneproject.core.entity.PlaceEntry;
import com.ladsuematsu.capstoneproject.newplace.activity.NewPlaceActivity;
import com.ladsuematsu.capstoneproject.newplace.detail.PlaceDetailsActivity;
import com.ladsuematsu.capstoneproject.newplace.mvp.NewPlaceMvp;
import com.ladsuematsu.capstoneproject.widget.PlaceWidgetUpdateReceiver;

public class DrugstorePanelInfo extends InfoPanelBottomSheetDialog {
    private static final String PLACE_DATA_ARG = "place_data_arg";
    private PlaceEntry place;


    private final View.OnClickListener onDetailsClickCListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Context context = getContext();

            Intent activityIntent = new Intent(context, PlaceDetailsActivity.class)
                                            .putExtra(NewPlaceMvp.EXTRA_PLACE_KEY, place.getUid());
            startActivity(activityIntent);

            dismiss();
        }
    };

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
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // Update data and refresh app widget state when the dialog is instantiated
        if (savedInstanceState == null) {
            Intent widgetReceiverIntent = new Intent(PlaceWidgetUpdateReceiver.DEFAULT_ACTION)
                    .putExtra(PlaceWidgetUpdateReceiver.EXTRA_PLACE_INFO, place);
            LocalBroadcastManager.getInstance(getContext()).sendBroadcast(widgetReceiverIntent);
        }
    }

    @Override
    protected void inflateContent(ViewGroup content, LayoutInflater inflater) {
        View rootView = inflater.inflate(R.layout.place_details, content);

        TextView placeName = rootView.findViewById(R.id.name);
        TextView placeAddress = rootView.findViewById(R.id.address);
        TextView phoneNumber = rootView.findViewById(R.id.telephone);
        TextView placeDetails = rootView.findViewById(R.id.place_details);

        placeName.setText(place.getName());
        placeAddress.setText(place.getAddress());
        phoneNumber.setText(place.getPhoneNumber());
        placeDetails.setOnClickListener(onDetailsClickCListener);

    }

    @Override
    protected void setPanelTitle(TextView title) { }
}
