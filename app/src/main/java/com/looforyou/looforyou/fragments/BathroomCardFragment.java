package com.looforyou.looforyou.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
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
import com.looforyou.looforyou.utilities.BookmarksUtil;

import java.io.Serializable;

import static com.looforyou.looforyou.utilities.Stars.getStarDrawableResource;

/**
 * This is a fragment for Bathroom Cards
 *
 * @author mingtau li
 */

public class BathroomCardFragment extends Fragment implements Serializable {
    /* Cardview for bathroom */
    private CardView cardView;
    /* transient Bathroom object so data is not mutated by Serialization */
    private transient Bathroom bathroom;
    /* Bookmarks Utility */
    private BookmarksUtil bookmarksUtil;
    /* ImageView for bathroom bookmark */
    private ImageView bookmark;

    /**
     * new instance of bathroom fragment
     *
     * @param position: current position
     * @param bathroom  bathroom object
     * @return instantiated bathroom Fragment
     */
    public static Fragment getInstance(int position, Bathroom bathroom) {
        BathroomCardFragment bathroomFragment = new BathroomCardFragment();

        /* pass arguments into bundle*/
        Bundle args = new Bundle();
        args.putInt("position", position);
        args.putSerializable("bathroom", bathroom);
        bathroomFragment.setArguments(args);

        return bathroomFragment;

    }

    /**
     * load image url into webview
     *
     * @param webview webview object
     * @param url     image url
     */
    public void loadWebviewFromURL(WebView webview, String url) {
        if (url == null || url.equals("")) {
            url = "no-image-uploaded.png";
        }

        /* load image into webview from url */
        String css = "width:100%;height:100%;overflow:hidden;background:url(" + url + ");background-size:cover;background-position:center center;";
        String html = "<html><body style=\"height:100%;width:100%;margin:0;padding:0;overflow:hidden;\"><div style=\"" + css + "\"></div></body></html>";
        webview.loadDataWithBaseURL("file:///android_asset/", html, "text/html", "utf-8", null);
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
        /* inflate cardview layout */
        final View view = inflater.inflate(R.layout.card_view, container, false);
        /* new bookmarksUtil object */
        bookmarksUtil = new BookmarksUtil(getContext());

        /* get bathroom object from bundle args */
        bathroom = (Bathroom) getArguments().getSerializable("bathroom");

        /* instance cardView */
        cardView = (CardView) view.findViewById(R.id.cardView);
        cardView.setMaxCardElevation(cardView.getCardElevation() * BathroomCardAdapter.MAX_ELEVATION_FACTOR);

        /* instantiate webview and load in bathroom image */
        WebView loadedImage = (WebView) view.findViewById(R.id.bathroom_webview);
        loadWebviewFromURL(loadedImage, bathroom.getImageURL());

        /* display progress bar until image is fully loaded into webview */
        final ProgressBar Pbar = (ProgressBar) view.findViewById(R.id.bathroom_progress);
        loadedImage.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int progress) {
                if (progress < 100 && Pbar.getVisibility() == ProgressBar.GONE) {
                    Pbar.setVisibility(ProgressBar.VISIBLE);
                }
                Pbar.setProgress(progress);
                if (progress == 100) {
                    Pbar.setVisibility(ProgressBar.GONE);
                }
            }
        });

        /* load title of bathroom */
        TextView title = (TextView) view.findViewById(R.id.title);
        title.setText(bathroom.getName());

        /* load description of bathroom */
        TextView description = (TextView) view.findViewById(R.id.bathroom_description);
        String desc1 = bathroom.getDescriptions().size() > 0 ? bathroom.getDescriptions().get(0) : "";
        String desc2 = bathroom.getDescriptions().size() > 1 ? bathroom.getDescriptions().get(1) : "";
        description.setText(desc1 + "\n" + desc2);

        /* load address of bathroom */
        TextView address = (TextView) view.findViewById(R.id.bathroom_address);
        address.setText(bathroom.getAddress());

        /* load bathroom rating */
        ImageView stars = (ImageView) view.findViewById(R.id.card_stars);
        int rating = (int) Math.round(bathroom.getRating());
        stars.setImageResource(getStarDrawableResource(rating));

        /* load primary amenities */
        ImageView accessible = (ImageView) view.findViewById(R.id.bathroom_accessible);
        if (bathroom.getAmenities().contains("accessible"))
            accessible.setImageResource(R.drawable.ic_accessibility_enabled_20);
        ImageView free = (ImageView) view.findViewById(R.id.bathroom_free);
        if (bathroom.getAmenities().contains("free"))
            free.setImageResource(R.drawable.ic_free_enabled_20);
        ImageView keyless = (ImageView) view.findViewById(R.id.bathroom_keyless);
        if (bathroom.getAmenities().contains("no key required"))
            keyless.setImageResource(R.drawable.ic_keyless_enabled_20);
        ImageView parking = (ImageView) view.findViewById(R.id.bathroom_parking);
        if (bathroom.getAmenities().contains("parking available"))
            parking.setImageResource(R.drawable.ic_parking_enabled_20);

        /* load bookmarks and set bookmark button accordingly */
        bookmark = (ImageView) view.findViewById(R.id.main_card_bookmark);
        try{
            if (bookmarksUtil.getBookmarkedBathroomIds().contains(bathroom.getId())) {
                bookmark.setImageResource(R.drawable.ic_bookmark_enabled_20);
            }
        }catch(Exception e){

        }

        /* set listener on bookmark button */
        bookmark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (bookmarksUtil.getBookmarkedBathroomIds().contains(bathroom.getId())) {
                    if (bookmarksUtil.unBookmarkBathroom(bathroom.getId())) {
                        bookmark.setImageResource(R.drawable.ic_bookmark_disabled_20);
                    }
                } else {
                    if (bookmarksUtil.bookmarkBathroom(bathroom.getId())) {
                        bookmark.setImageResource(R.drawable.ic_bookmark_enabled_20);
                        Toast.makeText(getActivity(), "Bookmarked " + bathroom.getName(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

        });

        return view;
    }

    /* getter for cardview
    * @return cardView bathroom cardView
    * */
    public CardView getCardView() {
        return cardView;
    }


}
