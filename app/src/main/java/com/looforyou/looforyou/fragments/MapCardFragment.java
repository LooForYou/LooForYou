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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.looforyou.looforyou.Models.Bathroom;
import com.looforyou.looforyou.Models.Review;
import com.looforyou.looforyou.R;
import com.looforyou.looforyou.adapters.BathroomCardAdapter;
import com.looforyou.looforyou.utilities.HttpGet;
import com.looforyou.looforyou.utilities.HttpUtils;
import com.looforyou.looforyou.utilities.ReviewDeserializer;
import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.ExecutionException;

import static com.looforyou.looforyou.Constants.GET_REVIEWS;
import static com.looforyou.looforyou.utilities.Stars.getStarDrawableResource;

/**
 * This is a fragment for Bathroom Cards on map
 *
 * @author mingtau li
 */

public class MapCardFragment extends Fragment implements Serializable {
    /* Cardview for bathroom */
    private CardView cardView;
    /* transient Bathroom object so data is not mutated by Serialization */
    private transient Bathroom bathroom;
    /* views for bathroom data display */
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

    /**
     * new instance of bathroom fragment
     *
     * @param position: current position
     * @param bathroom  bathroom object
     * @return instantiated bathroom Fragment
     */
    public static Fragment getInstance(int position, Bathroom bathroom) {
        MapCardFragment bathroomFragment = new MapCardFragment();
        Bundle args = new Bundle();
        args.putInt("position", position);
        args.putSerializable("bathroom", bathroom);

        bathroomFragment.setArguments(args);
        return bathroomFragment;

    }

    /**
     * OnCreate functions when inflating fragment
     *
     * @param container          ViewGroup container
     * @param savedInstanceState bundle savedInstance state
     * @retun View inflated View
     */
    @SuppressLint("DefaultLocale")
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        /* inflate card layout */
        View view = inflater.inflate(R.layout.map_card_view, container, false);

        /* get arguments from bundle */
        bathroom = (Bathroom) getArguments().getSerializable("bathroom");

        /* set up cardview */
        cardView = (CardView) view.findViewById(R.id.map_card_view);
        cardView.setMaxCardElevation(cardView.getCardElevation() * BathroomCardAdapter.MAX_ELEVATION_FACTOR);

        /* initaialize components */
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

        /* load image into bathroom card  */
        Picasso.get().load(bathroom.getImageURL()).fit().into(extraInfoImage);
        /* load name into bathroom card  */
        extraInfoBathroomName.setText(bathroom.getName());
        /* load address into bathroom card  */
        extraInfoAddress.setText(bathroom.getAddress());
        /* load rating into bathroom card  */
        int rating = (int) Math.round(bathroom.getRating());
        extraInfoStars.setImageResource(getStarDrawableResource(rating));
        int reviewNum = loadReviewsFromServer(bathroom).size();
        extraInfoReviewNumber.setText(reviewNum+(reviewNum == 1 ? " review" : " reviews"));

        /* load primary amenities images */
        if (bathroom.getAmenities().contains("accessible"))
            extraInfoAccessibility.setImageResource(R.drawable.ic_accessibility_enabled_20);
        if (bathroom.getAmenities().contains("free"))
            extraInfoFree.setImageResource(R.drawable.ic_free_enabled_20);
        if (bathroom.getAmenities().contains("no key required"))
            extraInfoKeyless.setImageResource(R.drawable.ic_keyless_enabled_20);
        if (bathroom.getAmenities().contains("parking available"))
            extraInfoParking.setImageResource(R.drawable.ic_parking_enabled_20);


        return view;
    }

    /* getter for cardview
    * @return cardView bathroom cardView
    * */
    public CardView getCardView() {
        return cardView;
    }

    /**
     * method to load reviews from server
     *
     * @param bathroom current bathroom
     * @return ArrayList<Review> list of reviews
     */
    public ArrayList<Review> loadReviewsFromServer(Bathroom bathroom) {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(Review.class, new ReviewDeserializer());
        Gson gson = gsonBuilder.create();
        String result = "";
        ArrayList<Review> reviews = new ArrayList<Review>();
        HttpGet getReviews = new HttpGet();
        URL reviewRequest = null;
        try {
            /* build url string for server call */
            reviewRequest = new URL(GET_REVIEWS + "?filter={\"where\":{\"bathroomId\": \"" + bathroom.getId() + "\"}}");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        /* convert url string to url-formatted URL */
        reviewRequest = HttpUtils.encodeQuery(reviewRequest);
        try {
            /* make get request for reviews via server call */
            result = getReviews.execute(reviewRequest.toString()).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        if (!result.equals("")) {
            reviews = new ArrayList<Review>(Arrays.asList(gson.fromJson(result, Review[].class)));
        }
        return reviews;
    }

}
