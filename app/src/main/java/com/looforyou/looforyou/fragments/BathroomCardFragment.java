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
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.looforyou.looforyou.Models.Bathroom;
import com.looforyou.looforyou.R;
import com.looforyou.looforyou.adapters.BathroomCardAdapter;

import java.io.Serializable;

import static com.looforyou.looforyou.utilities.Stars.getStarDrawableResource;


public class BathroomCardFragment extends Fragment implements Serializable{

    private CardView cardView;
    private transient Bathroom bathroom;

    public static Fragment getInstance(int position, Bathroom bathroom) {
        BathroomCardFragment bathroomFragment = new BathroomCardFragment();
        Bundle args = new Bundle();
        args.putInt("position", position);
        args.putSerializable("bathroom", bathroom);

        bathroomFragment.setArguments(args);
        return bathroomFragment;

    }

    public void loadWebviewFromURL(WebView webview,String url){
        if(url == null || url.equals("")) {
            url = "no-image-uploaded.png";
        }
        String css = "width:100%;height:100%;overflow:hidden;background:url("+url+");background-size:cover;background-position:center center;";
        String html = "<html><body style=\"height:100%;width:100%;margin:0;padding:0;overflow:hidden;\"><div style=\"" + css + "\"></div></body></html>";
        webview.loadDataWithBaseURL("file:///android_asset/", html, "text/html", "utf-8", null);
    }

    @SuppressLint("DefaultLocale")
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.card_view, container, false);

        bathroom = (Bathroom) getArguments().getSerializable("bathroom");
        Log.v("bathroomcardfragment", "bathroom string: "+String.valueOf(bathroom.getName()));


        cardView = (CardView) view.findViewById(R.id.cardView);
        cardView.setMaxCardElevation(cardView.getCardElevation() * BathroomCardAdapter.MAX_ELEVATION_FACTOR);

        WebView loadedImage = (WebView) view.findViewById(R.id.bathroom_webview);
        loadWebviewFromURL(loadedImage,bathroom.getImageURL());
        final ProgressBar Pbar = (ProgressBar) view.findViewById(R.id.bathroom_progress);
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


        TextView title = (TextView) view.findViewById(R.id.title);
        title.setText(bathroom.getName());

        TextView description = (TextView) view.findViewById(R.id.bathroom_description);
        String desc1 = bathroom.getDescriptions().size() > 0 ? bathroom.getDescriptions().get(0) : "";
        String desc2 = bathroom.getDescriptions().size() > 1 ? bathroom.getDescriptions().get(1) : "";
        description.setText(desc1+"\n"+desc2);

        TextView address = (TextView) view.findViewById(R.id.bathroom_address);
        address.setText(bathroom.getAddress());

        ImageView stars = (ImageView) view.findViewById(R.id.card_stars);
        int rating  = (int) Math.round(bathroom.getRating());
        stars.setImageResource(getStarDrawableResource(rating));

        ImageView accessible = (ImageView) view.findViewById(R.id.bathroom_accessible);
        if(bathroom.getAmenities().contains("accessible")) accessible.setImageResource(R.drawable.ic_accessibility_enabled_20);
        ImageView free = (ImageView) view.findViewById(R.id.bathroom_free);
        if(bathroom.getAmenities().contains("free")) free.setImageResource(R.drawable.ic_free_enabled_20);
        ImageView keyless = (ImageView) view.findViewById(R.id.bathroom_keyless);
        if(bathroom.getAmenities().contains("no key required")) keyless.setImageResource(R.drawable.ic_keyless_enabled_20);
        ImageView parking = (ImageView) view.findViewById(R.id.bathroom_parking);
        if(bathroom.getAmenities().contains("parking available")) parking.setImageResource(R.drawable.ic_parking_enabled_20);


        final ImageView bookmark = (ImageView) view.findViewById(R.id.main_card_bookmark);
        bookmark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(bathroom.isBookmarked()){
                    bathroom.setBookmarked(false);
                    bookmark.setImageResource(R.drawable.ic_bookmark_disabled_20);
                }else{
                    bathroom.setBookmarked(true);
                    bookmark.setImageResource(R.drawable.ic_bookmark_enabled_20);
                    Toast.makeText(getActivity(), "Bookmarked " + bathroom.getName(), Toast.LENGTH_SHORT).show();
                }
            }

        });




        return view;
    }

    public CardView getCardView() {
        return cardView;
    }


}
