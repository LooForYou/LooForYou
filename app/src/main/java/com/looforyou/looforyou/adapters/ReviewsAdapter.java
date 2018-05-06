package com.looforyou.looforyou.adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.looforyou.looforyou.R;
import com.looforyou.looforyou.utilities.HttpPut;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.looforyou.looforyou.Constants.DOWNVOTE;
import static com.looforyou.looforyou.Constants.GET_REVIEWS;
import static com.looforyou.looforyou.Constants.UPVOTE;
import static com.looforyou.looforyou.utilities.Stars.getStarDrawableResource;

/**
 * This is the adapter for displaying reviews
 *
 * @author: mingtau li
 */

public class ReviewsAdapter extends RecyclerView.Adapter<ReviewsAdapter.ViewHolder> {
    /* list custom of Review List Items */
    private List<ReviewsListItem> reviewsListItems;
    /* activity context*/
    private Context context;

    /**
     * Constructor for Reviews Adapter
     *
     * @param reviewsListItems list of reviews list items
     * @param context          activity context
     */
    public ReviewsAdapter(List<ReviewsListItem> reviewsListItems, Context context) {
        this.reviewsListItems = reviewsListItems;
        this.context = context;
    }

    /**
     * Inflates reviews list items for
     *
     * @param parent   parent ViewGroup of acivity
     * @param viewType viewType id
     * @return viewHolder from inflated layout
     */
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.reviews_list_item, parent, false);
        return new ViewHolder(v);
    }

    /**
     * Viewholder on bind functionality
     *
     * @param holder   current ViewHolder
     * @param position current position
     */
    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        /* get current review list item*/
        final ReviewsListItem reviewsListItem = reviewsListItems.get(position);
        /* load reviewer name */
        holder.reviewer.setText(reviewsListItem.getReviewer());
        /* load review */
        holder.content.setText(reviewsListItem.getContent());
        /* load reviewer profile picture */
        Picasso.get().load(reviewsListItem.getprofilePicture()).fit().into(holder.profileImage);
        /* load review points */
        holder.reviewPoints.setText(String.valueOf(reviewsListItem.getPoints()) + " points");
        /* load review review rating */
        holder.rating.setImageResource(getStarDrawableResource(reviewsListItem.getRating()));
        /* initialize review id */
        final String id = reviewsListItem.getReview().getId();
        /* initialize shared preferences for upvotes */
        final SharedPreferences upvoteSharedPref = context.getSharedPreferences(context.getString(R.string.saved_upvote) + id, Context.MODE_PRIVATE);
        /* initialize editor for upvotes */
        final SharedPreferences.Editor upvoteSharedPrefEditor = upvoteSharedPref.edit();
        /* initialize shared preferences for downvotes */
        final SharedPreferences downvoteSharedPref = context.getSharedPreferences(context.getString(R.string.saved_downvote) + id, Context.MODE_PRIVATE);
        /* initialize editor for downvotes */
        final SharedPreferences.Editor downvoteSharedPrefEditor = downvoteSharedPref.edit();
        /* default value of preference */
        final String defaultValue = "";
        /* get upvote preference value */
        String upvoted = upvoteSharedPref.getString(context.getString(R.string.saved_upvote) + id, defaultValue);
        /* get downvote preference value */
        String downvoted = downvoteSharedPref.getString(context.getString(R.string.saved_downvote) + id, defaultValue);

        /* set image of upvote/downvote buttons appropriately */
        if (upvoted.equals(id)) {
            holder.upvote.setImageResource(R.drawable.ic_helpful_icon_active);
        }
        if (downvoted.equals(id)) {
            holder.downvote.setImageResource(R.drawable.ic_not_helpful_icon_active);
        }
        if (reviewsListItem.getReview().getLikes() > 0) {
            holder.reviewPoints.setTextColor(Color.parseColor("#FF5ACC77"));
        } else {
            holder.reviewPoints.setTextColor(Color.parseColor("#FFEA6455"));
        }

        /* bind clicklistener for upvote button */
        holder.upvote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
                    HttpPut put = new HttpPut(new JSONObject());
                    String upvoted = upvoteSharedPref.getString(context.getString(R.string.saved_upvote) + id, defaultValue);
                    if (!upvoted.equals(id)) {
                        holder.upvote.setImageResource(R.drawable.ic_helpful_icon_active);
                        /* increment review point using server call */
                        put.execute(GET_REVIEWS + id + UPVOTE);

                        /* update shared preferences*/
                        upvoteSharedPrefEditor.putString(context.getString(R.string.saved_upvote) + id, reviewsListItem.getReview().getId());
                        upvoteSharedPrefEditor.commit();

                        /* update likes amount */
                        reviewsListItem.getReview().setLikes(reviewsListItem.getReview().getLikes() + 1);
                        holder.reviewPoints.setText(reviewsListItem.getReview().getLikes() + " points");
                        if (reviewsListItem.getReview().getLikes() > 0) {
                            holder.reviewPoints.setTextColor(Color.parseColor("#FF5ACC77"));
                        } else {
                            holder.reviewPoints.setTextColor(Color.parseColor("#FFEA6455"));
                        }

                        /* update downvote value */
                        String downvoted = downvoteSharedPref.getString(context.getString(R.string.saved_downvote) + id, defaultValue);
                        if (downvoted.equals(id)) {
                            holder.downvote.setImageResource(R.drawable.ic_not_helpful_icon);
                            downvoteSharedPrefEditor.putString(context.getString(R.string.saved_downvote) + id, defaultValue);
                            downvoteSharedPrefEditor.commit();
                        }

                    } else {
                        holder.upvote.setImageResource(R.drawable.ic_helpful_icon);
                        /* decrement review point using server call */
                        put.execute(GET_REVIEWS + id + DOWNVOTE);

                        /* update shared preferences*/
                        upvoteSharedPrefEditor.putString(context.getString(R.string.saved_upvote) + id, defaultValue);
                        upvoteSharedPrefEditor.commit();

                        /* update likes amount */
                        reviewsListItem.getReview().setLikes(reviewsListItem.getReview().getLikes() - 1);
                        holder.reviewPoints.setText(reviewsListItem.getReview().getLikes() + " points");
                        if (reviewsListItem.getReview().getLikes() > 0) {
                            holder.reviewPoints.setTextColor(Color.parseColor("#FF5ACC77"));
                        } else {
                            holder.reviewPoints.setTextColor(Color.parseColor("#FFEA6455"));
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        /* bind clicklistener for downvote button */
        holder.downvote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.downvote.setImageResource(R.drawable.ic_not_helpful_icon_active);
                String id = reviewsListItem.getReview().getId();

                try {
                    HttpPut put = new HttpPut(new JSONObject());
                    String downvoted = downvoteSharedPref.getString(context.getString(R.string.saved_downvote) + id, defaultValue);
                    if (!downvoted.equals(id)) {
                        holder.downvote.setImageResource(R.drawable.ic_not_helpful_icon_active);
                        /* decrement review point using server call */
                        put.execute(GET_REVIEWS + id + DOWNVOTE);

                        /* update shared preferences*/
                        downvoteSharedPrefEditor.putString(context.getString(R.string.saved_downvote) + id, reviewsListItem.getReview().getId());
                        downvoteSharedPrefEditor.commit();

                        /* update likes amount */
                        reviewsListItem.getReview().setLikes(reviewsListItem.getReview().getLikes() - 1);
                        holder.reviewPoints.setText(reviewsListItem.getReview().getLikes() + " points");

                        /* update upvote value */
                        String upvoted = upvoteSharedPref.getString(context.getString(R.string.saved_upvote) + id, defaultValue);
                        if (upvoted.equals(id)) {
                            holder.upvote.setImageResource(R.drawable.ic_helpful_icon);
                            upvoteSharedPrefEditor.putString(context.getString(R.string.saved_upvote) + id, defaultValue);
                            upvoteSharedPrefEditor.commit();
                        }

                    } else {
                        holder.downvote.setImageResource(R.drawable.ic_not_helpful_icon);
                        /* increment review point using server call */
                        put.execute(GET_REVIEWS + id + UPVOTE);

                        /* update shared preferences*/
                        downvoteSharedPrefEditor.putString(context.getString(R.string.saved_downvote) + id, defaultValue);
                        downvoteSharedPrefEditor.commit();

                        /* update likes amount */
                        reviewsListItem.getReview().setLikes(reviewsListItem.getReview().getLikes() + 1);
                        holder.reviewPoints.setText(reviewsListItem.getReview().getLikes() + " points");
                        if (reviewsListItem.getReview().getLikes() > 0) {
                            holder.reviewPoints.setTextColor(Color.parseColor("#FF5ACC77"));
                        } else {
                            holder.reviewPoints.setTextColor(Color.parseColor("#FFEA6455"));
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * gets number of review list items
     *
     * @return int number of review list items
     */
    @Override
    public int getItemCount() {
        return reviewsListItems.size();
    }

    /**
     * ViewHolder class that uses RecyclerView
     */
    public class ViewHolder extends RecyclerView.ViewHolder {
        /* views for review display data */
        public TextView reviewer;
        public TextView content;
        public CircleImageView profileImage;
        public TextView reviewPoints;
        public ImageView rating;
        public ImageView upvote;
        public ImageView downvote;

        /**
         * Constructor for ViewHolder
         * Initializes all view items
         */
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
