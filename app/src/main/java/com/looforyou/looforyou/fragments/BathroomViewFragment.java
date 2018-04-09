package com.looforyou.looforyou.fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.looforyou.looforyou.Models.Bathroom;
import com.looforyou.looforyou.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Locale;

import static com.looforyou.looforyou.utilities.Stars.getStarDrawableResource;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link BathroomViewFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link BathroomViewFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BathroomViewFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_bathroom_view, container, false);
        View view = inflater.inflate(R.layout.fragment_bathroom_view, container, false);
        Button button = (Button) view.findViewById(R.id.bathroom_fragment_button);
//        String menu = getArguments().getString("Menu");
        Bathroom bathroom = getArguments().getParcelable("current bathroom");
//        button.setText(menu);
          button.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View view) {
                  Toast.makeText(getContext(),"stuff", Toast.LENGTH_SHORT).show();
              }
          });

        TextView bathroomName = view.findViewById(R.id.bathroom_fragment_name);
        bathroomName.setText(bathroom.getName().toUpperCase());

        TextView bathroomAddress = view.findViewById(R.id.bathroom_fragment_address);
        bathroomAddress.setText(bathroom.getAddress());

        TextView bathroomDistance = view.findViewById(R.id.bathroom_fragment_distance);
        //TODO get distance

        TextView bathroomHours = view.findViewById(R.id.bathroom_fragment_operation_time);
        bathroomHours.setText(getHoursString(bathroom));

        TextView bathroomDescription = view.findViewById(R.id.bathroom_fragment_description);
        bathroomDescription.setText(getStringDescription(bathroom));

        final Bathroom clickedBathroom = bathroom;
        ImageButton bathroomDirectionsButton = view.findViewById(R.id.bathroom_fragment_directions_button);
        bathroomDirectionsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onDirectionsButtonClick(v,clickedBathroom);
            }
        });

        TextView bathroomDirectionsText = view.findViewById(R.id.bathroom_fragment_directions_text);
        bathroomDirectionsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onDirectionsButtonClick(v,clickedBathroom);
            }
        });
        ImageView bathroomStars = view.findViewById(R.id.bathroom_fragment_stars);
        int rating  = (int) Math.round(bathroom.getRating());
        bathroomStars.setImageResource(getStarDrawableResource(rating));

        TextView bathroomNumReviews = view.findViewById(R.id.bathroom_fragment_num_reviews);
        //TODO get number of reviews

        ImageView bathroomAccessible = (ImageView) view.findViewById(R.id.bathroom_fragment_accessibility_icon);
        TextView bathroomAccessibleText = (TextView) view.findViewById(R.id.bathroom_fragment_accessibility_text);
        if(bathroom.getAmenities().contains("accessible")) {
            bathroomAccessible.setImageResource(R.drawable.ic_accessibility_enabled_20);
            bathroomAccessibleText.setTextColor(getResources().getColor(R.color.colorPrimary));
        }
        ImageView bathroomFree = (ImageView) view.findViewById(R.id.bathroom_fragment_free_icon);
        TextView bathroomFreeText = (TextView) view.findViewById(R.id.bathroom_fragment_free_text);
        if(bathroom.getAmenities().contains("free")) {
            bathroomFree.setImageResource(R.drawable.ic_free_enabled_20);
            bathroomFreeText.setTextColor(getResources().getColor(R.color.colorPrimary));
        }
        ImageView bathroomKeyless = (ImageView) view.findViewById(R.id.bathroom_fragment_keyless_icon);
        TextView bathroomKeylessText = (TextView) view.findViewById(R.id.bathroom_fragment_keyless_text);
        if(bathroom.getAmenities().contains("no key required")) {
            bathroomKeyless.setImageResource(R.drawable.ic_keyless_enabled_20);
            bathroomKeylessText.setTextColor(getResources().getColor(R.color.colorPrimary));
        }
        ImageView bathroomParking = (ImageView) view.findViewById(R.id.bathroom_fragment_parking_icon);
        TextView bathroomParkingText = (TextView) view.findViewById(R.id.bathroom_fragment_parking_text);
        if(bathroom.getAmenities().contains("parking available")) {
            bathroomParking.setImageResource(R.drawable.ic_parking_enabled_20);
            bathroomParkingText.setTextColor(getResources().getColor(R.color.colorPrimary));
        }

        TextView bathroomAmenities = view.findViewById(R.id.bathroom_fragment_amenities);
        bathroomAmenities.setText(getStringAmenities(bathroom));

        TextView maintenanceString = view.findViewById(R.id.bathroom_fragment_maintenance);
        maintenanceString.setText(getMaintenanceHoursString(bathroom));

        WebView loadedImage = (WebView) view.findViewById(R.id.bathroom_fragment_webview);
        loadWebviewFromURL(loadedImage,bathroom.getImageURL());
        final ProgressBar Pbar = (ProgressBar) view.findViewById(R.id.bathroom_fragment_progress);
        loadedImage.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int progress)
            {
                if(progress < 100 && Pbar.getVisibility() == ProgressBar.GONE){
                    Pbar.setVisibility(ProgressBar.VISIBLE);
                }
                Pbar.setProgress(progress);
                if(progress == 100) {
                    Pbar.setVisibility(ProgressBar.GONE);
                }
            }
        });

        return view;
    }

    public void loadWebviewFromURL(WebView webview,String url){
        if(url == null || url.equals("")) {
            url = "no-image-uploaded.png";
        }
        String css = "width:100%;height:100%;overflow:hidden;background:url("+url+");background-size:cover;background-position:center center;";
        String html = "<html><body style=\"height:100%;width:100%;margin:0;padding:0;overflow:hidden;\"><div style=\"" + css + "\"></div></body></html>";
        webview.loadDataWithBaseURL("file:///android_asset/", html, "text/html", "utf-8", null);
    }

    public void onDirectionsButtonClick(View view, Bathroom bathroom){
            view.setVisibility(View.GONE);
            Uri markerUri = Uri.parse("https://www.google.com/maps/dir/?api=1&destination="+bathroom.getLatLng().latitude+","+bathroom.getLatLng().longitude);
            Intent directionsIntent = new Intent(Intent.ACTION_VIEW,markerUri);
            directionsIntent.setPackage("com.google.android.apps.maps");
            if(directionsIntent.resolveActivity(getActivity().getPackageManager()) != null){
                startActivity(directionsIntent);
            }else {
                Toast.makeText(getActivity(),"Unable to route", Toast.LENGTH_SHORT).show();
            }
        }

    public String getStringAmenities(Bathroom bathroom) {
        ArrayList<String> amenitiesList = bathroom.getAmenities();
        Collections.sort(amenitiesList);
        String formattedAmenities = amenitiesList.toString().replaceAll(", ", "\n• ");
        formattedAmenities = "• " + formattedAmenities.substring(1, formattedAmenities.length() - 1);
        return formattedAmenities;
    }
    public String getStringDescription(Bathroom bathroom){
        String desc1 = bathroom.getDescriptions().size() > 0 ? bathroom.getDescriptions().get(0) : "";
        String desc2 = bathroom.getDescriptions().size() > 1 ? bathroom.getDescriptions().get(1) : "";
        return desc1+"\n"+desc2;
    }
    public String getAmenitiesString(Bathroom bathroom) {
        ArrayList<String> amenitiesList = bathroom.getAmenities();
        Collections.sort(amenitiesList);
        String formattedAmenities = amenitiesList.toString().replaceAll(", ", "\n• ");
        formattedAmenities = "• " + formattedAmenities.substring(1, formattedAmenities.length() - 1);
        return formattedAmenities;
    }

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
        }else if (mStart.equals(mEnd)) {
            return "24HR";}
        else {
            return mStart + " to " + mEnd + "\n" + maintenanceDays;
        }
    }

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
