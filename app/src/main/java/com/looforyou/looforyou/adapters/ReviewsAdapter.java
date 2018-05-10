package com.looforyou.looforyou.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import com.looforyou.looforyou.R;
import com.looforyou.looforyou.utilities.HttpDelete;
import com.looforyou.looforyou.utilities.HttpPost;
import com.looforyou.looforyou.utilities.HttpPut;
import com.looforyou.looforyou.utilities.HttpUtils;
import com.looforyou.looforyou.utilities.UserUtil;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.looforyou.looforyou.Constants.DOWNVOTE;
import static com.looforyou.looforyou.Constants.GET_REVIEWS;
import static com.looforyou.looforyou.Constants.TOKEN_QUERY;
import static com.looforyou.looforyou.Constants.UPDATE_REVIEW;
import static com.looforyou.looforyou.Constants.UPVOTE;
import static com.looforyou.looforyou.utilities.Stars.getStarDrawableResource;

/**
 * This is the adapter for displaying reviews
 *
 * @author mingtau li
 */

public class ReviewsAdapter extends RecyclerView.Adapter<ReviewsAdapter.ViewHolder> {
    /* list custom of Review List Items */
    private List<ReviewsListItem> reviewsListItems;
    /* activity context*/
    private Context context;
    /* global rating */
    private int ratingNum;

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
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        /* get current review list item*/
        final ReviewsListItem reviewsListItem = reviewsListItems.get(position);
        /* initialize rating */
        if(reviewsListItem.getReview().getReviewerId().equals(new UserUtil(context).getUserID())){
            ratingNum = reviewsListItem.getRating();
        }
        /* load reviewer name */
        holder.reviewer.setText(reviewsListItem.getReviewer());
        /* load review */
        holder.content.setText(reviewsListItem.getContent());
        holder.content.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        /* load reviewer profile picture */
        Picasso.get().load(reviewsListItem.getprofilePicture()).fit().into(holder.profileImage);
        /* load review points */
        holder.reviewPoints.setText(String.valueOf(reviewsListItem.getPoints()) + (reviewsListItem.getPoints() == 1 ? " point" : " points"));
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

        /* display number of days elapsed */
        holder.daysAgo.setText(reviewsListItem.getDaysAgo() + (reviewsListItem.getDaysAgo() == 1 ? "day ago" : " days ago"));

        /* expand/collapse viewswitcher when changing views */
        holder.editSwitcher.setMeasureAllChildren(false);
        UserUtil userUtil = new UserUtil(context);
        /* display custom options unique to a user */
        if (userUtil.isLoggedIn() && reviewsListItem.getReviewerId().equals(userUtil.getUserID())) {
            holder.deleteReview.setVisibility(View.VISIBLE);
            holder.reviewer.setText(reviewsListItem.getReviewer() + " (you)");
            holder.voteContainer.setVisibility(View.GONE);
            holder.editButton.setVisibility(View.VISIBLE);
            holder.editButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    holder.editSwitcher.showNext();
                    holder.ratingSwitcher.showNext();

                    if (holder.editButton.getText().equals("Edit Review")) {
                        holder.editButton.setText("Done Editing");
                        holder.editText.setText(holder.content.getText());
                        Toast.makeText(context,"rating: "+ratingNum, Toast.LENGTH_SHORT).show();
                        if(ratingNum == 1){
                            holder.rating.setImageResource(R.drawable.one_star_90_15);
                            setEditStars(holder,1);
                        }else if(ratingNum == 2){
                            holder.rating.setImageResource(R.drawable.two_stars_90_15);
                            setEditStars(holder,2);
                        }else if(ratingNum == 3){
                            holder.rating.setImageResource(R.drawable.three_stars_90_15);
                            setEditStars(holder,3);
                        }else if(ratingNum == 4){
                            holder.rating.setImageResource(R.drawable.four_stars_90_15);
                            setEditStars(holder,4);
                        }else if(ratingNum == 5){
                            holder.rating.setImageResource(R.drawable.five_stars_90_15);
                            setEditStars(holder,5);
                        }


                    } else {
                        holder.editButton.setText("Edit Review");
                        InputMethodManager imm = (InputMethodManager) context.getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

                        /* start edit content */
                        Map<String, String> updateData = new HashMap<String, String>();
                        updateData.put("content", holder.editText.getText().toString());
                        updateData.put("rating", String.valueOf(ratingNum));
                        HttpPost updateReview = new HttpPost(updateData);
                        String result = "";
                        URL query = null;
                        URL rawQuery = null;
                        String testString = UPDATE_REVIEW + "?where={\"id\":\"" + reviewsListItems.get(position).getReviewId() + "\"}" + TOKEN_QUERY + (new UserUtil(context).getUserToken());
                        try {
                            rawQuery = new URL(UPDATE_REVIEW + "?where={\"id\":\"" + reviewsListItems.get(position).getReviewId() + "\"}&" + TOKEN_QUERY + (new UserUtil(context).getUserToken()));
                        } catch (MalformedURLException e) {
                            e.printStackTrace();
                        }
                        try {
                            query = HttpUtils.encodeQuery(rawQuery);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        try {
                            result = updateReview.execute(query.toString()).get();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        } catch (ExecutionException e) {
                            e.printStackTrace();
                        }
                        if(!result.isEmpty()){
                            holder.content.setText(holder.editText.getText().toString());
                            reviewsListItem.getReview().setRating(ratingNum);
                            if(ratingNum == 1){
                                holder.rating.setImageResource(R.drawable.one_star_90_15);
                            }else if(ratingNum == 2) {
                                holder.rating.setImageResource(R.drawable.two_stars_90_15);
                            }else if(ratingNum == 3) {
                                holder.rating.setImageResource(R.drawable.three_stars_90_15);
                            }else if(ratingNum == 4) {
                                holder.rating.setImageResource(R.drawable.four_stars_90_15);
                            }else if(ratingNum == 5) {
                                holder.rating.setImageResource(R.drawable.five_stars_90_15);
                            }


                        }else {
                            Toast.makeText(context,"something has gone wrong. please try again later", Toast.LENGTH_SHORT).show();
                        }
                        /* end edit content */

                    }
                }
            });

        }

        holder.star1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setEditStars(holder,1);
                ratingNum = 1;
            }
        });

        holder.star2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setEditStars(holder,2);
                ratingNum = 2;
            }
        });

        holder.star3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setEditStars(holder,3);
                ratingNum = 3;
            }
        });

        holder.star4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setEditStars(holder,4);
                ratingNum = 4;
            }
        });

        holder.star5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setEditStars(holder,5);
                ratingNum = 5;
                Toast.makeText(context,"5: "+ratingNum, Toast.LENGTH_SHORT).show();
            }
        });

        /* set click listener for deleting a review */
        holder.deleteReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /* set up dialog for confirming review deletion */
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(view.getContext())
                        .setTitle("Confirm Deletion")
                        .setMessage("Are you sure you want to delete your review?");

                /* dismiss dialog if user cancels */
                alertDialog.setPositiveButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                /* delete reviews if user confirms */
                alertDialog.setNegativeButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        HttpDelete delete = new HttpDelete();
                        String query = GET_REVIEWS + reviewsListItem.getReview().getId() + TOKEN_QUERY + (new UserUtil(context).getUserToken());
                        try {
                            delete.execute(query);
                        } catch (Exception e) {
                            Toast.makeText(context, "oops, something went wrong with the request. Please try again later.", Toast.LENGTH_SHORT);
                        }
                        reviewsListItems.remove(position);
                        notifyItemRemoved(position);
                    }
                });

                /* create and  open dialog */
                AlertDialog dialog = alertDialog.create();
                dialog.show();

            }
        });

    }
    /**
     * helper method for changing image resources when editing stars
     * */
    public void setEditStars(ViewHolder holder, int newRating){
        if(newRating == 1){
            holder.star1.setImageResource(R.drawable.ic_star_expanded_full_15);
            holder.star2.setImageResource(R.drawable.ic_star_expanded_empty_15);
            holder.star3.setImageResource(R.drawable.ic_star_expanded_empty_15);
            holder.star4.setImageResource(R.drawable.ic_star_expanded_empty_15);
            holder.star5.setImageResource(R.drawable.ic_star_expanded_empty_15);
        }else if(newRating == 2){
            holder.star1.setImageResource(R.drawable.ic_star_expanded_full_15);
            holder.star2.setImageResource(R.drawable.ic_star_expanded_full_15);
            holder.star3.setImageResource(R.drawable.ic_star_expanded_empty_15);
            holder.star4.setImageResource(R.drawable.ic_star_expanded_empty_15);
            holder.star5.setImageResource(R.drawable.ic_star_expanded_empty_15);
        }else if(newRating == 3){
            holder.star1.setImageResource(R.drawable.ic_star_expanded_full_15);
            holder.star2.setImageResource(R.drawable.ic_star_expanded_full_15);
            holder.star3.setImageResource(R.drawable.ic_star_expanded_full_15);
            holder.star4.setImageResource(R.drawable.ic_star_expanded_empty_15);
            holder.star5.setImageResource(R.drawable.ic_star_expanded_empty_15);
        }else if(newRating == 4){
            holder.star1.setImageResource(R.drawable.ic_star_expanded_full_15);
            holder.star2.setImageResource(R.drawable.ic_star_expanded_full_15);
            holder.star3.setImageResource(R.drawable.ic_star_expanded_full_15);
            holder.star4.setImageResource(R.drawable.ic_star_expanded_full_15);
            holder.star5.setImageResource(R.drawable.ic_star_expanded_empty_15);
        }else if(newRating == 5){
            holder.star1.setImageResource(R.drawable.ic_star_expanded_full_15);
            holder.star2.setImageResource(R.drawable.ic_star_expanded_full_15);
            holder.star3.setImageResource(R.drawable.ic_star_expanded_full_15);
            holder.star4.setImageResource(R.drawable.ic_star_expanded_full_15);
            holder.star5.setImageResource(R.drawable.ic_star_expanded_full_15);
        }
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
        public TextView daysAgo;
        public TextView deleteReview;
        public LinearLayout voteContainer;
        public TextView editButton;
        public ViewSwitcher editSwitcher;
        public ViewSwitcher ratingSwitcher;
        public EditText editText;
        public ImageView star1;
        public ImageView star2;
        public ImageView star3;
        public ImageView star4;
        public ImageView star5;

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
            daysAgo = (TextView) itemView.findViewById(R.id.reviews_days_ago);
            deleteReview = (TextView) itemView.findViewById(R.id.reviews_delete_review);
            voteContainer = (LinearLayout) itemView.findViewById(R.id.reviews_vote_container);
            editButton = (TextView) itemView.findViewById(R.id.reviews_edit_button);
            editSwitcher = (ViewSwitcher) itemView.findViewById(R.id.reviews_content_switcher);
            ratingSwitcher = (ViewSwitcher) itemView.findViewById(R.id.reviews_rating_switcher);
            editText = (EditText) itemView.findViewById(R.id.reviews_edit);
            star1 = (ImageView) itemView.findViewById(R.id.reviews_star1);
            star2 = (ImageView) itemView.findViewById(R.id.reviews_star2);
            star3 = (ImageView) itemView.findViewById(R.id.reviews_star3);
            star4 = (ImageView) itemView.findViewById(R.id.reviews_star4);
            star5 = (ImageView) itemView.findViewById(R.id.reviews_star5);

        }
    }
}
