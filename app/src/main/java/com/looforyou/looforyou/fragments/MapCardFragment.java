package com.looforyou.looforyou.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.looforyou.looforyou.Models.Bathroom;
import com.looforyou.looforyou.R;
import com.looforyou.looforyou.adapters.BathroomCardAdapter;
import com.looforyou.looforyou.utilities.MetricConverter;
import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.text.DecimalFormat;

import static com.looforyou.looforyou.utilities.Stars.getStarDrawableResource;


public class MapCardFragment extends Fragment implements Serializable{

    private CardView cardView;
    private transient Bathroom bathroom;

    private LinearLayout extraInfo;
    private TextView extraInfoBathroomName;
    private ImageView extraInfoImage;
    private TextView extraInfoDistance;
    private TextView extraInfoAddress;
    private ImageView extraInfoStars;
    private TextView extraInfoReviewNumber;
    private ImageView extraInfoAccessibility;
    private ImageView extraInfoKeyless;
    private ImageView extraInfoFree;
    private ImageView extraInfoParking;

    public static Fragment getInstance(int position, Bathroom bathroom) {
        MapCardFragment bathroomFragment = new MapCardFragment();
        Bundle args = new Bundle();
        args.putInt("position", position);
        args.putSerializable("bathroom", bathroom);

        bathroomFragment.setArguments(args);
        return bathroomFragment;

    }

    @SuppressLint("DefaultLocale")
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.map_card_view, container, false);

        bathroom = (Bathroom) getArguments().getSerializable("bathroom");
        Log.v("bathroomcardfragment", "bathroom string: "+String.valueOf(bathroom.getName()));


        cardView = (CardView) view.findViewById(R.id.map_card_view);
        cardView.setMaxCardElevation(cardView.getCardElevation() * BathroomCardAdapter.MAX_ELEVATION_FACTOR);


        extraInfoBathroomName = (TextView) view.findViewById(R.id.map_extra_info_bathroom_name);
        extraInfoImage = (ImageView) view.findViewById(R.id.map_extra_info_image);
        extraInfoDistance = (TextView) view.findViewById(R.id.map_extra_info_distance);
        extraInfoAddress = (TextView) view.findViewById(R.id.map_extra_info_address);
        extraInfoStars = (ImageView) view.findViewById(R.id.map_extra_info_stars);
        extraInfoReviewNumber = (TextView) view.findViewById(R.id.map_extra_info_review_number);
        extraInfoAccessibility = (ImageView) view.findViewById(R.id.map_extra_info_accesssible);
        extraInfoKeyless = (ImageView) view.findViewById(R.id.map_extra_info_keyless);
        extraInfoFree = (ImageView) view.findViewById(R.id.map_extra_info_free);
        extraInfoParking = (ImageView) view.findViewById(R.id.map_extra_info_parking);


        Picasso.get().load(bathroom.getImageURL()).fit().into(extraInfoImage);
        extraInfoBathroomName.setText(bathroom.getName());
        extraInfoAddress.setText(bathroom.getAddress());
        int rating  = (int) Math.round(bathroom.getRating());
        extraInfoStars.setImageResource(getStarDrawableResource(rating));
//        extraInfoReviewNumber
        if(bathroom.getAmenities().contains("accessible")) extraInfoAccessibility.setImageResource(R.drawable.ic_accessibility_enabled_20);
        if(bathroom.getAmenities().contains("free")) extraInfoFree.setImageResource(R.drawable.ic_free_enabled_20);
        if(bathroom.getAmenities().contains("no key required")) extraInfoKeyless.setImageResource(R.drawable.ic_keyless_enabled_20);
        if(bathroom.getAmenities().contains("parking available")) extraInfoParking.setImageResource(R.drawable.ic_parking_enabled_20);



        return view;
    }

    public CardView getCardView() {
        return cardView;
    }


}
