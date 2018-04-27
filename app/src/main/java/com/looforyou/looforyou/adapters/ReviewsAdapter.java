package com.looforyou.looforyou.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
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

        holder.upvote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context,"upvote", Toast.LENGTH_SHORT).show();
                holder.upvote.setImageResource(R.drawable.ic_helpful_icon_active);
                String id = reviewsListItem.getReview().getId();
                HttpPut put = new HttpPut(new JSONObject());
//                SharedPreferences sharedPref = ((Activity)context).getPreferences(Context.MODE_PRIVATE);
//                String defaultValue = context.getResources().getString(R.string.saved_upvote);
//                String upvoted = sharedPref.getString(context.getString(R.string.saved_upvote), defaultValue);
//                Log.v("sharedpreference defaul",defaultValue);
//                Log.v("sharedpreference u",upvoted);
                try {
                    put.execute(GET_REVIEWS+id+UPVOTE);
//                    SharedPreferences.Editor editor = sharedPref.edit();
//                    editor.putString(context.getString(R.string.saved_upvote), reviewsListItem.getReview().getId());
//                    editor.commit();
                }  catch (Exception e) {
                    e.printStackTrace();
                }
                reviewsListItem.getReview().setLikes(reviewsListItem.getReview().getLikes()+1);
                holder.reviewPoints.setText(reviewsListItem.getReview().getLikes()+" points");
            }
        });

        holder.downvote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context,"downvote", Toast.LENGTH_SHORT).show();
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
