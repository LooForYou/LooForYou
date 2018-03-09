package com.looforyou.looforyou.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.looforyou.looforyou.R;
import com.looforyou.looforyou.adapters.BathroomCardAdapter;


public class BathroomCardFragment extends Fragment {

    private CardView cardView;
    private TextView title;

    public static Fragment getInstance(int position) {
        BathroomCardFragment bathroomFragment = new BathroomCardFragment();
        Bundle args = new Bundle();
        args.putInt("position", position);
        bathroomFragment.setArguments(args);
        return bathroomFragment;
    }

    @SuppressLint("DefaultLocale")
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.card_view, container, false);

        cardView = (CardView) view.findViewById(R.id.cardView);
        cardView.setMaxCardElevation(cardView.getCardElevation() * BathroomCardAdapter.MAX_ELEVATION_FACTOR);

        title = (TextView) view.findViewById(R.id.title);
        title.setText(String.format("Bathroom %d", getArguments().getInt("position")+1));

        final ImageView bookmark = (ImageView) view.findViewById(R.id.main_card_bookmark);
        bookmark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(), "Bookmarked Bathroom " + getArguments().getInt("position"), Toast.LENGTH_SHORT).show();
                bookmark.setImageResource(R.drawable.ic_bookmark_enabled_20); //delete me //test
            }

        });


        return view;
    }

    public CardView getCardView() {
        return cardView;
    }

    public void setTitle(String t){
        title.setText(t);
    }
}
