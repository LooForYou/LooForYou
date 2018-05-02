package com.looforyou.looforyou.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.looforyou.looforyou.R;
import com.looforyou.looforyou.utilities.HttpPost;
import com.looforyou.looforyou.utilities.HttpPut;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.util.List;
import java.util.concurrent.ExecutionException;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.looforyou.looforyou.Constants.DOWNVOTE;
import static com.looforyou.looforyou.Constants.GET_REVIEWS;
import static com.looforyou.looforyou.Constants.UPVOTE;
import static com.looforyou.looforyou.R.drawable.ic_helpful_icon_active;
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
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final ReviewsListItem reviewsListItem = reviewsListItems.get(position);
        holder.reviewer.setText(reviewsListItem.getReviewer());
        holder.content.setText(reviewsListItem.getContent());
        Picasso.get().load(reviewsListItem.getprofilePicture()).fit().into(holder.profileImage);
        holder.reviewPoints.setText(String.valueOf(reviewsListItem.getPoints())+" points");
        holder.rating.setImageResource(getStarDrawableResource(reviewsListItem.getRating()));
        final String id = reviewsListItem.getReview().getId();
        final SharedPreferences upvoteSharedPref = context.getSharedPreferences(context.getString(R.string.saved_upvote)+id,Context.MODE_PRIVATE);
        final SharedPreferences.Editor upvoteSharedPrefEditor = upvoteSharedPref.edit();
        final SharedPreferences downvoteSharedPref = context.getSharedPreferences(context.getString(R.string.saved_downvote)+id,Context.MODE_PRIVATE);
        final SharedPreferences.Editor downvoteSharedPrefEditor = downvoteSharedPref.edit();
        final String defaultValue = "";

        String upvoted = upvoteSharedPref.getString(context.getString(R.string.saved_upvote)+id, defaultValue);
        String downvoted = downvoteSharedPref.getString(context.getString(R.string.saved_downvote)+id, defaultValue);
        if(upvoted.equals(id)) {
            holder.upvote.setImageResource(R.drawable.ic_helpful_icon_active);
        }
        if(downvoted.equals(id)) {
            holder.downvote.setImageResource(R.drawable.ic_not_helpful_icon_active);
        }
        if(reviewsListItem.getReview().getLikes()>0){
            holder.reviewPoints.setTextColor(Color.parseColor("#FF5ACC77"));
        }else{
            holder.reviewPoints.setTextColor(Color.parseColor("#FFEA6455"));
        }

        holder.upvote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
                    HttpPut put = new HttpPut(new JSONObject());
                    String upvoted = upvoteSharedPref.getString(context.getString(R.string.saved_upvote)+id, defaultValue);
                    if(!upvoted.equals(id)) {
                        holder.upvote.setImageResource(R.drawable.ic_helpful_icon_active);
                        put.execute(GET_REVIEWS + id + UPVOTE);
                        upvoteSharedPrefEditor.putString(context.getString(R.string.saved_upvote)+id, reviewsListItem.getReview().getId());
                        upvoteSharedPrefEditor.commit();
                        reviewsListItem.getReview().setLikes(reviewsListItem.getReview().getLikes()+1);
                        holder.reviewPoints.setText(reviewsListItem.getReview().getLikes()+" points");
                        if(reviewsListItem.getReview().getLikes()>0){
                            holder.reviewPoints.setTextColor(Color.parseColor("#FF5ACC77"));
                        }else{
                            holder.reviewPoints.setTextColor(Color.parseColor("#FFEA6455"));
                        }

                        String downvoted = downvoteSharedPref.getString(context.getString(R.string.saved_downvote)+id, defaultValue);
                        if(downvoted.equals(id)) {
                            holder.downvote.setImageResource(R.drawable.ic_not_helpful_icon);
                            downvoteSharedPrefEditor.putString(context.getString(R.string.saved_downvote) + id, defaultValue);
                            downvoteSharedPrefEditor.commit();
                        }

                    }else {
                        holder.upvote.setImageResource(R.drawable.ic_helpful_icon);
                        put.execute(GET_REVIEWS + id + DOWNVOTE);
                        upvoteSharedPrefEditor.putString(context.getString(R.string.saved_upvote)+id, defaultValue);
                        upvoteSharedPrefEditor.commit();
                        reviewsListItem.getReview().setLikes(reviewsListItem.getReview().getLikes()-1);
                        holder.reviewPoints.setText(reviewsListItem.getReview().getLikes()+" points");
                        if(reviewsListItem.getReview().getLikes()>0){
                            holder.reviewPoints.setTextColor(Color.parseColor("#FF5ACC77"));
                        }else{
                            holder.reviewPoints.setTextColor(Color.parseColor("#FFEA6455"));
                        }
                    }

                }  catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        holder.downvote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.downvote.setImageResource(R.drawable.ic_not_helpful_icon_active);
                String id = reviewsListItem.getReview().getId();

                try {
                    HttpPut put = new HttpPut(new JSONObject());
                    String downvoted = downvoteSharedPref.getString(context.getString(R.string.saved_downvote)+id, defaultValue);
                    if(!downvoted.equals(id)) {
                        holder.downvote.setImageResource(R.drawable.ic_not_helpful_icon_active);
                        put.execute(GET_REVIEWS + id + DOWNVOTE);
                        downvoteSharedPrefEditor.putString(context.getString(R.string.saved_downvote)+id, reviewsListItem.getReview().getId());
                        downvoteSharedPrefEditor.commit();
                        reviewsListItem.getReview().setLikes(reviewsListItem.getReview().getLikes()-1);
                        holder.reviewPoints.setText(reviewsListItem.getReview().getLikes()+" points");

                        String upvoted = upvoteSharedPref.getString(context.getString(R.string.saved_upvote)+id, defaultValue);
                        if(upvoted.equals(id)) {
                            holder.upvote.setImageResource(R.drawable.ic_helpful_icon);
                            upvoteSharedPrefEditor.putString(context.getString(R.string.saved_upvote) + id, defaultValue);
                            upvoteSharedPrefEditor.commit();
                        }

                    }else {
                        holder.downvote.setImageResource(R.drawable.ic_not_helpful_icon);
                        put.execute(GET_REVIEWS + id + UPVOTE);
                        downvoteSharedPrefEditor.putString(context.getString(R.string.saved_downvote)+id, defaultValue);
                        downvoteSharedPrefEditor.commit();
                        reviewsListItem.getReview().setLikes(reviewsListItem.getReview().getLikes()+1);
                        holder.reviewPoints.setText(reviewsListItem.getReview().getLikes()+" points");
                    }
                }  catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
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
        public ImageView upvote;
        public ImageView downvote;

        public ViewHolder(View itemView) {
            super(itemView);

            reviewer = (TextView) itemView.findViewById(R.id.reviews_reviewer);
            content = (TextView) itemView.findViewById(R.id.reviews_content);
            profileImage = (CircleImageView) itemView.findViewById(R.id.reviews_profile_picture);
            reviewPoints = (TextView) itemView.findViewById(R.id.reviews_points);
            rating = (ImageView) itemView.findViewById(R.id.reviews_rating);
            upvote = (ImageView) itemView.findViewById(R.id.reviews_helpful);
            downvote = (ImageView) itemView.findViewById(R.id.reviews_not_helpful);
        }
    }
}
