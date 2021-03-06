package com.looforyou.looforyou.fragments;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.looforyou.looforyou.Models.Bathroom;
import com.looforyou.looforyou.Models.Review;
import com.looforyou.looforyou.Models.Reviewer;
import com.looforyou.looforyou.R;
import com.looforyou.looforyou.activities.ProfileActivity;
import com.looforyou.looforyou.adapters.ReviewsAdapter;
import com.looforyou.looforyou.adapters.ReviewsListItem;
import com.looforyou.looforyou.utilities.BookmarksUtil;
import com.looforyou.looforyou.utilities.HttpGet;
import com.looforyou.looforyou.utilities.HttpPost;
import com.looforyou.looforyou.utilities.HttpUtils;
import com.looforyou.looforyou.utilities.MetricConverter;
import com.looforyou.looforyou.utilities.ReviewDeserializer;
import com.looforyou.looforyou.utilities.ReviewerDeserializer;
import com.looforyou.looforyou.utilities.UserUtil;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import static com.looforyou.looforyou.Constants.GET_BATHROOMS;
import static com.looforyou.looforyou.Constants.GET_REVIEWS;
import static com.looforyou.looforyou.Constants.REVIEWS_LIST;
import static com.looforyou.looforyou.Constants.TOKEN_QUERY;
import static com.looforyou.looforyou.utilities.Stars.getStarDrawableResource;

/**
 * This is a fragment for Bathroom Data
 *
 * @author mingtau li
 */

public class BathroomViewFragment extends Fragment {
    /* boilerplace params */
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    /* Views for Bathroom data display */
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private List<ReviewsListItem> reviewsListItems;
    private BookmarksUtil bookmarksUtil;
    private ImageButton bathroomBookmarkButton;
    private TextView bathroomBookmarkText;
    private Dialog newReviewDialog;
    private int userRating;
    private Bathroom bathroom;
    private View view;
    private LinearLayout newReviewContainer;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private boolean hasReview;

    private OnFragmentInteractionListener mListener;

    public BathroomViewFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BathroomViewFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static BathroomViewFragment newInstance(String param1, String param2) {
        BathroomViewFragment fragment = new BathroomViewFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        /* instantiate bookmarksUtil object */
        bookmarksUtil = new BookmarksUtil(getContext());
         /* initialize new dialog */
        newReviewDialog = new Dialog(getContext());
        newReviewDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        userRating = 1;
        hasReview = false;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        /* Inflate the layout for this fragment */
        view = inflater.inflate(R.layout.fragment_bathroom_view, container, false);
        /* instantiate bathroom fragment button */
        Button button = (Button) view.findViewById(R.id.bathroom_fragment_button);

        newReviewContainer = (LinearLayout) view.findViewById(R.id.bathroom_fragment_review_container);

        /* get arguments from bundle */
        bathroom = getArguments().getParcelable("current bathroom");
        final Location location = getArguments().getParcelable("current location");

        /* bind clicklistener to bathroom fragment button */
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "bathroom fragment clicked ", Toast.LENGTH_SHORT).show();
            }
        });

        /* load bathroom name */
        TextView bathroomName = view.findViewById(R.id.bathroom_fragment_name);
        bathroomName.setText(bathroom.getName().toUpperCase());

        /* load bathroom address */
        TextView bathroomAddress = view.findViewById(R.id.bathroom_fragment_address);
        bathroomAddress.setText(bathroom.getAddress());

        /* load distance from bathroom to specified location */
        TextView bathroomDistance = view.findViewById(R.id.bathroom_fragment_distance);
        try {
            DecimalFormat df = new DecimalFormat("0.00");
            double dist = MetricConverter.distanceBetweenInMiles(new LatLng(location.getLatitude(), location.getLongitude()), bathroom.getLatLng());
            bathroomDistance.setText(df.format(dist) + " mi");
        } catch (Exception e) {
            e.printStackTrace();
        }

        /* load hours of operation*/
        TextView bathroomHours = view.findViewById(R.id.bathroom_fragment_operation_time);
        bathroomHours.setText(getHoursString(bathroom));

        /* load bathroom description */
        TextView bathroomDescription = view.findViewById(R.id.bathroom_fragment_description);
        bathroomDescription.setText(getStringDescription(bathroom));

        /* final Bathroom object for inner function reference */
        final Bathroom clickedBathroom = bathroom;

        /* bind clicklistener to bathroom directions button to open navigation */
        ImageButton bathroomDirectionsButton = (ImageButton) view.findViewById(R.id.bathroom_fragment_directions_button);
        bathroomDirectionsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onDirectionsButtonClick(clickedBathroom);
            }
        });

        /* bind clicklistener to bathroom directions label to open navigation */
        TextView bathroomDirectionsText = view.findViewById(R.id.bathroom_fragment_directions_text);
        bathroomDirectionsText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onDirectionsButtonClick(clickedBathroom);
            }
        });

        /* bind clicklistener to bathroom directions button to open navigation */
        bathroomBookmarkButton = (ImageButton) view.findViewById(R.id.bathroom_fragment_toggle_bookmark_button);
        try {
            if (bookmarksUtil.getBookmarkedBathroomIds().contains(bathroom.getId())) {
                if (bookmarksUtil.bookmarkBathroom(bathroom.getId())) {
                    bathroomBookmarkButton.setImageResource(R.drawable.ic_bookmark_button_enabled_45);
                    //TODO update bookmark on cardview in Main Activity (do not let it affect MapActivity)
                    // ((MainActivity)getActivity()).refreshBookmarkData();
                }
            } else {
                if (bookmarksUtil.unBookmarkBathroom(bathroom.getId())) {
                    bathroomBookmarkButton.setImageResource(R.drawable.ic_bookmark_button_disabled_45);
                }
            }
        } catch (Exception e) {
        }
        /* bind clicklistener to bookmark button in order to bookmark bathroom */
        bathroomBookmarkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBookmarkButtonClick(clickedBathroom);
            }
        });

        /* bind clicklistener to bookmark label in order to bookmark bathroom */
        bathroomBookmarkText = view.findViewById(R.id.bathroom_fragment_toggle_bookmark_text);
        bathroomBookmarkText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBookmarkButtonClick(clickedBathroom);
            }
        });

        /* load bathroom rating */
        ImageView bathroomStars = view.findViewById(R.id.bathroom_fragment_stars);
        int rating = (int) Math.round(bathroom.getRating());
        bathroomStars.setImageResource(getStarDrawableResource(rating));

        /* count number of reviews */
        TextView bathroomNumReviews = view.findViewById(R.id.bathroom_fragment_num_reviews);

        /* load reviews */
        loadReviews(view, bathroom);

        /* load number of reviews */
        if (reviewsListItems != null) {
            bathroomNumReviews.setText(reviewsListItems.size() + (reviewsListItems.size() == 1 ? " review" : " reviews"));
        }

        /* load images of primary amenities */
        ImageView bathroomAccessible = (ImageView) view.findViewById(R.id.bathroom_fragment_accessibility_icon);
        TextView bathroomAccessibleText = (TextView) view.findViewById(R.id.bathroom_fragment_accessibility_text);
        if (bathroom.getAmenities().contains("accessible")) {
            bathroomAccessible.setImageResource(R.drawable.ic_accessibility_enabled_20);
            bathroomAccessibleText.setTextColor(getResources().getColor(R.color.colorPrimary));
        }
        ImageView bathroomFree = (ImageView) view.findViewById(R.id.bathroom_fragment_free_icon);
        TextView bathroomFreeText = (TextView) view.findViewById(R.id.bathroom_fragment_free_text);
        if (bathroom.getAmenities().contains("free")) {
            bathroomFree.setImageResource(R.drawable.ic_free_enabled_20);
            bathroomFreeText.setTextColor(getResources().getColor(R.color.colorPrimary));
        }
        ImageView bathroomKeyless = (ImageView) view.findViewById(R.id.bathroom_fragment_keyless_icon);
        TextView bathroomKeylessText = (TextView) view.findViewById(R.id.bathroom_fragment_keyless_text);
        if (bathroom.getAmenities().contains("no key required")) {
            bathroomKeyless.setImageResource(R.drawable.ic_keyless_enabled_20);
            bathroomKeylessText.setTextColor(getResources().getColor(R.color.colorPrimary));
        }
        ImageView bathroomParking = (ImageView) view.findViewById(R.id.bathroom_fragment_parking_icon);
        TextView bathroomParkingText = (TextView) view.findViewById(R.id.bathroom_fragment_parking_text);
        if (bathroom.getAmenities().contains("parking available")) {
            bathroomParking.setImageResource(R.drawable.ic_parking_enabled_20);
            bathroomParkingText.setTextColor(getResources().getColor(R.color.colorPrimary));
        }

        /* load all amenities*/
        TextView bathroomAmenities = view.findViewById(R.id.bathroom_fragment_amenities);
        bathroomAmenities.setText(getStringAmenities(bathroom));

        /* load maintenance times*/
        TextView maintenanceString = view.findViewById(R.id.bathroom_fragment_maintenance);
        maintenanceString.setText(getMaintenanceHoursString(bathroom));

        /* load image url of bathroom into webvview */
        WebView loadedImage = (WebView) view.findViewById(R.id.bathroom_fragment_webview);
        loadWebviewFromURL(loadedImage, bathroom.getImageURL());
        final ProgressBar Pbar = (ProgressBar) view.findViewById(R.id.bathroom_fragment_progress);
        loadedImage.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView webview, int progress) {
                if (progress < 100 && Pbar.getVisibility() == ProgressBar.GONE) {
                    Pbar.setVisibility(ProgressBar.VISIBLE);
                }
                Pbar.setProgress(progress);
                if (progress == 100) {
                    Pbar.setVisibility(ProgressBar.GONE);
                }
            }
        });


        newReviewContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                if (!new UserUtil(getContext()).isLoggedIn()) {
                    newReviewDialog.dismiss();
                    Intent notLoggedInIntent = new Intent(getContext(), ProfileActivity.class);
                    getContext().startActivity(notLoggedInIntent);
                } else {
                    newReviewDialog.setContentView(R.layout.dialog_new_review);
                    newReviewDialog.show();

                    final ImageView star1 = (ImageView) newReviewDialog.findViewById(R.id.star_1);
                    final ImageView star2 = (ImageView) newReviewDialog.findViewById(R.id.star_2);
                    final ImageView star3 = (ImageView) newReviewDialog.findViewById(R.id.star_3);
                    final ImageView star4 = (ImageView) newReviewDialog.findViewById(R.id.star_4);
                    final ImageView star5 = (ImageView) newReviewDialog.findViewById(R.id.star_5);

                    star1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            star1.setImageResource(R.drawable.ic_star_expanded_full_15);
                            star2.setImageResource(R.drawable.ic_star_expanded_empty_15);
                            star3.setImageResource(R.drawable.ic_star_expanded_empty_15);
                            star4.setImageResource(R.drawable.ic_star_expanded_empty_15);
                            star5.setImageResource(R.drawable.ic_star_expanded_empty_15);
                            userRating = 1;
                        }
                    });

                    star2.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            star1.setImageResource(R.drawable.ic_star_expanded_full_15);
                            star2.setImageResource(R.drawable.ic_star_expanded_full_15);
                            star3.setImageResource(R.drawable.ic_star_expanded_empty_15);
                            star4.setImageResource(R.drawable.ic_star_expanded_empty_15);
                            star5.setImageResource(R.drawable.ic_star_expanded_empty_15);
                            userRating = 2;
                        }
                    });

                    star3.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            star1.setImageResource(R.drawable.ic_star_expanded_full_15);
                            star2.setImageResource(R.drawable.ic_star_expanded_full_15);
                            star3.setImageResource(R.drawable.ic_star_expanded_full_15);
                            star4.setImageResource(R.drawable.ic_star_expanded_empty_15);
                            star5.setImageResource(R.drawable.ic_star_expanded_empty_15);
                            userRating = 3;
                        }
                    });

                    star4.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            star1.setImageResource(R.drawable.ic_star_expanded_full_15);
                            star2.setImageResource(R.drawable.ic_star_expanded_full_15);
                            star3.setImageResource(R.drawable.ic_star_expanded_full_15);
                            star4.setImageResource(R.drawable.ic_star_expanded_full_15);
                            star5.setImageResource(R.drawable.ic_star_expanded_empty_15);
                            userRating = 4;
                        }
                    });

                    star5.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            star1.setImageResource(R.drawable.ic_star_expanded_full_15);
                            star2.setImageResource(R.drawable.ic_star_expanded_full_15);
                            star3.setImageResource(R.drawable.ic_star_expanded_full_15);
                            star4.setImageResource(R.drawable.ic_star_expanded_full_15);
                            star5.setImageResource(R.drawable.ic_star_expanded_full_15);
                            userRating = 5;
                        }
                    });

                    Button submit = (Button) newReviewDialog.findViewById(R.id.submit);
                    final Button cancel = (Button) newReviewDialog.findViewById(R.id.cancel);
                    final EditText reviewContent = (EditText) newReviewDialog.findViewById(R.id.review_content);

                    submit.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Date timeNow = Calendar.getInstance().getTime();
                            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"); //pattern for date parsing from string to time
                            String nowString = "";
                            UserUtil userUtil = new UserUtil(getContext());
                            if (reviewContent.getText().toString().trim().isEmpty()) {
                                Toast.makeText(getContext(), "Please say something about this bathroom first", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            try {
                                nowString = dateFormat.format(timeNow).toString();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            Map<String, String> reviewData = new HashMap<String, String>();
                            reviewData.put("rating", String.valueOf(userRating));
                            reviewData.put("content", reviewContent.getText().toString());
                            reviewData.put("time_created", nowString);
                            reviewData.put("time_updated", nowString);
                            reviewData.put("likes", "0");
                            reviewData.put("bathroomId", bathroom.getId());
                            reviewData.put("accountId", userUtil.getUserID());
                            String reviewPostResult = "";
                            HttpPost reviewPost = new HttpPost(reviewData);
                            try {
                                reviewPostResult = reviewPost.execute(GET_REVIEWS + TOKEN_QUERY + userUtil.getUserToken()).get();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            } catch (ExecutionException e) {
                                e.printStackTrace();
                            }

                            if (reviewPostResult.isEmpty()) {
                                Toast.makeText(getContext(), "Something has gone wrong", Toast.LENGTH_SHORT).show();

                            } else {
                                Toast.makeText(getContext(), "Thank you for submitting your review", Toast.LENGTH_SHORT).show();
                                newReviewContainer.setVisibility(View.GONE);
                            /* refresh list reviews list view */
                                loadReviews(view, bathroom);
                                newReviewDialog.dismiss();
                            }
                        }
                    });

                    cancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            newReviewDialog.dismiss();
                        }
                    });
                }
            }

        });

        return view;
    }

    /**
     * load reviews from server
     *
     * @param v        parent View
     * @param bathroom current Bathroom
     */
    public void loadReviews(View v, Bathroom bathroom) {
        /* define recycler view*/
        recyclerView = (RecyclerView) v.findViewById(R.id.review_recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        reviewsListItems = new ArrayList<>();

        /* load list of review and reviews from server */
        ArrayList<Reviewer> reviewers = loadReviewersFromServer(bathroom);
        ArrayList<Review> reviews = loadReviewsFromServer(bathroom);
        UserUtil userUtil = new UserUtil(getContext());
        String currentUserID = userUtil.getUserID();
        /* match reviewer to reviews */
        for (Review review : reviews) {
            review.setReviewerInfo(reviewers);
            if(review.getReviewerId().equals(userUtil.getUserID())){
                hasReview = true;
            }
            ReviewsListItem reviewsListItem = new ReviewsListItem(
                    review,
                    review.getReviewerId(),
                    review.getReviewerUserName(),
                    review.getContent(),
                    review.getReviewerImageUrl(),
                    review.getLikes(),
                    review.getRating(),
                    review.getTimeCreated(),
                    review.getTimeUpdated());
            if (!currentUserID.isEmpty() && review.getReviewerId().equals(currentUserID)) {
                    /* put current user's review on top */
                reviewsListItems.add(0, reviewsListItem);
            } else {
                reviewsListItems.add(reviewsListItem);
            }
        }

        /*  load review list items to list */
        if (reviews.size() > 0) {
            TextView noReviews = (TextView) v.findViewById(R.id.no_reviews);
            noReviews.setVisibility(View.GONE);
            if(hasReview){
                newReviewContainer.setVisibility(View.GONE);
            }
            ReviewsListItem userItem = null;
            if (userUtil.isLoggedIn() && hasReview) {
                userItem = reviewsListItems.remove(0);

            }
            /* sort reviews list by popularity using custom comparator */
            Collections.sort(reviewsListItems, new Comparator<ReviewsListItem>() {
                @Override
                public int compare(ReviewsListItem t1, ReviewsListItem t2) {
                    if (t1.getPoints() < t2.getPoints()) {
                        return 1;
                    } else if (t1.getPoints() > t2.getPoints()) {
                        return -1;
                    }
                    return 0;
                }
            });

            if (userUtil.isLoggedIn() && hasReview) {
                reviewsListItems.add(0,userItem);
            }
        }

        /* set adapter for reviews list items */
        adapter = new ReviewsAdapter(reviewsListItems, getActivity().getApplicationContext());
        recyclerView.setAdapter(adapter);
        int maxScrollHeight = 360;

        /* resize review scroll container for reviews */
        LinearLayout sv = (LinearLayout) v.findViewById(R.id.review_scroll_container);
        sv.measure(0, 0);
        if (MetricConverter.pxToDp(getActivity().getApplicationContext(), sv.getMeasuredHeight()) > maxScrollHeight) {
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) MetricConverter.dpToPx(getActivity().getApplicationContext(), maxScrollHeight));
            sv.setLayoutParams(lp);
        }
    }

    /**
     * method to load reviewers from server
     *
     * @param bathroom current bathroom
     * @return ArrayList<Reviewer> list of reviewers
     */
    public ArrayList<Reviewer> loadReviewersFromServer(Bathroom bathroom) {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(Reviewer.class, new ReviewerDeserializer());
        Gson gson = gsonBuilder.create();
        String result = "";
        HttpGet getReviewers = new HttpGet();
        try {
            /* make get request for reviewers via server call */
            result = getReviewers.execute(GET_BATHROOMS + bathroom.getId() + REVIEWS_LIST).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        ArrayList<Reviewer> reviewers = new ArrayList<Reviewer>();
        if (!result.equals("")) {
            reviewers = new ArrayList<Reviewer>(Arrays.asList(gson.fromJson(result, Reviewer[].class)));
        }
        return reviewers;
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
            /* make get request for reviews via server call */
            reviewRequest = new URL(GET_REVIEWS + "?filter={\"where\":{\"bathroomId\": \"" + bathroom.getId() + "\"}}");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        reviewRequest = HttpUtils.encodeQuery(reviewRequest);
        try {
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

    /**
     * load image url into webview
     *
     * @param webview webview object
     * @param url     string url of image
     */
    public void loadWebviewFromURL(WebView webview, String url) {
        if (url == null || url.equals("")) {
            url = "no-image-uploaded.png";
        }
        String css = "width:100%;height:100%;overflow:hidden;background:url(" + url + ");background-size:cover;background-position:center center;";
        String html = "<html><body style=\"height:100%;width:100%;margin:0;padding:0;overflow:hidden;\"><div style=\"" + css + "\"></div></body></html>";
        webview.loadDataWithBaseURL("file:///android_asset/", html, "text/html", "utf-8", null);
    }

    /**
     * handler for directions button for navigation
     *
     * @param bathroom current bathroom
     */
    public void onDirectionsButtonClick(Bathroom bathroom) {
        Uri markerUri = Uri.parse("https://www.google.com/maps/dir/?api=1&destination=" + bathroom.getLatLng().latitude + "," + bathroom.getLatLng().longitude);
        Intent directionsIntent = new Intent(Intent.ACTION_VIEW, markerUri);
        /* use google maps app */
        directionsIntent.setPackage("com.google.android.apps.maps");
        if (directionsIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivity(directionsIntent);
        } else {
            Toast.makeText(getActivity(), "Unable to route", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * handler for bookmark button bookmarking bathrooms
     *
     * @param bathroom current bathroom
     **/
    public void onBookmarkButtonClick(Bathroom bathroom) {
        if (bookmarksUtil.getBookmarkedBathroomIds().contains(bathroom.getId())) {
            bookmarksUtil.unBookmarkBathroom(bathroom.getId());
            bathroomBookmarkButton.setImageResource(R.drawable.ic_bookmark_button_disabled_45);
        } else {
            bookmarksUtil.bookmarkBathroom(bathroom.getId());
            bathroomBookmarkButton.setImageResource(R.drawable.ic_bookmark_button_enabled_45);
        }
    }

    /**
     * getter for bathroom amenities
     *
     * @param bathroom current bathroom
     * @return String formatted amenities string
     */
    public String getStringAmenities(Bathroom bathroom) {
        ArrayList<String> amenitiesList = bathroom.getAmenities();
        Collections.sort(amenitiesList);
        String formattedAmenities = amenitiesList.toString().replaceAll(", ", "\n• ");
        formattedAmenities = "• " + formattedAmenities.substring(1, formattedAmenities.length() - 1);
        return formattedAmenities;
    }

    /**
     * getter for bathroom description
     *
     * @param bathroom current bathroom
     * @return String formatted bathroom description
     */
    public String getStringDescription(Bathroom bathroom) {
        String desc1 = bathroom.getDescriptions().size() > 0 ? bathroom.getDescriptions().get(0) : "";
        String desc2 = bathroom.getDescriptions().size() > 1 ? bathroom.getDescriptions().get(1) : "";
        return desc1 + "\n" + desc2;
    }

    /**
     * getter for bathroom amenities
     *
     * @param bathroom current bathroom
     * @return String formatted anemities string
     */
    public String getAmenitiesString(Bathroom bathroom) {
        ArrayList<String> amenitiesList = bathroom.getAmenities();
        Collections.sort(amenitiesList);
        String formattedAmenities = amenitiesList.toString().replaceAll(", ", "\n• ");
        formattedAmenities = "• " + formattedAmenities.substring(1, formattedAmenities.length() - 1);
        return formattedAmenities;
    }

    /**
     * getter for bathroom maintenance hours
     *
     * @param bathroom current bathroom
     * @return formatted maintenance hours string
     */
    public String getMaintenanceHoursString(Bathroom bathroom) {
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a", Locale.ENGLISH);
        String mStart = null;
        Date maintenanceStartTime = bathroom.getMaintenanceStart();
        if (maintenanceStartTime != null) {
            try {
                mStart = sdf.format(maintenanceStartTime);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        Date maintenanceEndTime = bathroom.getMaintenanceEnd();
        String mEnd = null;
        if (maintenanceEndTime != null) {
            try {
                mEnd = sdf.format(maintenanceEndTime);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        String maintenanceDays = bathroom.getMaintenanceDays().toString();
        if (maintenanceStartTime == null || maintenanceEndTime == null) {
            return "Unknown";
        } else if (mStart.equals(mEnd)) {
            return "24HR";
        } else {
            return mStart + " to " + mEnd + "\n" + maintenanceDays;
        }
    }

    /**
     * getter for bathroom hours
     *
     * @param bathroom current bathroom
     * @return formatted operation hours string
     */
    public String getHoursString(Bathroom bathroom) {
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a", Locale.ENGLISH);
        Date startTime = bathroom.getStartTime();
        String start = null;
        if (startTime != null) {
            try {
                start = sdf.format(startTime);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        Date endTime = bathroom.getEndTime();
        String end = null;
        if (endTime != null) {
            try {
                end = sdf.format(endTime);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (startTime == null || endTime == null) {
            return "Unknown";
        } else if (start.equals(end)) {
            return "24HR";
        } else {
            return start + " to " + end;
        }
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
