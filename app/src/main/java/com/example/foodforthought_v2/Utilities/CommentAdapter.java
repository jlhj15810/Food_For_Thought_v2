package com.example.foodforthought_v2.Utilities;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.example.foodforthought_v2.R;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentViewHolder> {

    private Context mContext;
    private List<Comment> mData;

    public CommentAdapter(Context mContext, List<Comment> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    public CommentAdapter() {
    }

    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View row = LayoutInflater.from(mContext).inflate(R.layout.row_comment,viewGroup,false);
        return new CommentViewHolder(row);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentViewHolder commentViewHolder, int i) {

        Glide.with(mContext).load(mData.get(i).getUimg()).into(commentViewHolder.user_image);
        commentViewHolder.tv_name.setText(mData.get(i).getUname());
        commentViewHolder.tv_content.setText(mData.get(i).getContent());
        commentViewHolder.tv_date.setText(timeStampToString((Long) mData.get(i).getTimestamp()));
        commentViewHolder.tv_rating_bar.setRating(mData.get(i).getRatings());

        //The date thingy


    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class CommentViewHolder extends RecyclerView.ViewHolder {


        ImageView user_image;
        TextView tv_name, tv_content, tv_date;
        RatingBar tv_rating_bar;



        public CommentViewHolder(View itemView) {
            super(itemView);

            user_image = itemView.findViewById(R.id.comment_user_img);
            tv_content = itemView.findViewById(R.id.comment_content);
            tv_name = itemView.findViewById(R.id.comment_username);
            tv_date = itemView.findViewById(R.id.comment_date);
            tv_rating_bar = itemView.findViewById(R.id.comment_ratingBar);



        }
    }



    private String timeStampToString(long time) {
        Calendar calendar = Calendar.getInstance(Locale.ENGLISH);
        calendar.setTimeInMillis(time);
        //Change the informat to the format i want.
        String date = DateFormat.format("hh:mm",calendar).toString();
        return date;
    }


}
