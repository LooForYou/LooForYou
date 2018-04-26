package com.looforyou.looforyou.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.looforyou.looforyou.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.looforyou.looforyou.utilities.Stars.getStarDrawableResource;

/**
 * Created by ibreaker on 4/9/2018.
 */

public class ReviewsAdapter extends RecyclerView.Adapter<ReviewsAdapter.ViewHolder>{
    private List<ReviewsListItem> reviewsListItems;
    private Context context;

    public ReviewsAdapter(List<ReviewsListItem> reviewsListItems, Context context) {
        this.reviewsListItems = reviewsListItems;
        this.context = context;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.reviews_list_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ReviewsListItem reviewsListItem = reviewsListItems.get(position);
        holder.reviewer.setText(reviewsListItem.getReviewer());
        holder.content.setText(reviewsListItem.getContent());
        Picasso.get().load(reviewsListItem.getprofilePicture()).fit().into(holder.profileImage);
        holder.reviewPoints.setText(String.valueOf(reviewsListItem.getPoints())+" points");
        holder.rating.setImageResource(getStarDrawableResource(reviewsListItem.getRating()));

    }

    @Override
    public int getItemCount() {
        return reviewsListItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView reviewer;
        public TextView content;
        public CircleImageView profileImage;
        public TextView reviewPoints;
        public ImageView rating;

        public ViewHolder(View itemView) {
            super(itemView);

            reviewer = (TextView) itemView.findViewById(R.id.reviews_reviewer);
            content = (TextView) itemView.findViewById(R.id.reviews_content);
            profileImage = (CircleImageView) itemView.findViewById(R.id.reviews_profile_picture);
            reviewPoints = (TextView) itemView.findViewById(R.id.reviews_points);
            rating = (ImageView) itemView.findViewById(R.id.reviews_rating);
        }
    }
}
