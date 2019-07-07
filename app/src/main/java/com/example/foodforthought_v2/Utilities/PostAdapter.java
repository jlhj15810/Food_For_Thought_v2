package com.example.foodforthought_v2.Utilities;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.example.foodforthought_v2.Activities.PostDetailActivity;
import com.example.foodforthought_v2.R;


import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.MyViewHolder> {

    Context mContext;
    List<Post> mData;

    public PostAdapter(Context mContext, List<Post> mData) {
        this.mContext = mContext;
        this.mData = mData;

    }

    public List<Post> getmData() {
        return mData;
    }

    public void setmData(List<Post> mData) {
        this.mData = mData;
    }

    //Testing 12345
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View row = LayoutInflater.from(mContext).inflate(R.layout.row_post_item,viewGroup,false);
        return new MyViewHolder(row);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {

        myViewHolder.tvTitle.setText(mData.get(i).getTitle());
        myViewHolder.tvDescription.setText(mData.get(i).getDescription());
        myViewHolder.tvPrice.setText(mData.get(i).getPrice());
        Glide.with(mContext).load(mData.get(i).getPicture()).into(myViewHolder.imagePost);

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tvTitle;
        ImageView imagePost;
        TextView tvDescription;
        TextView tvPrice;

        //Add the ratings thing, uncompleted yet.
        RatingBar tvRatingBar;


        public MyViewHolder(View itemView) {
            super(itemView);

            tvTitle = itemView.findViewById(R.id.row_post_title);
            imagePost = itemView.findViewById(R.id.row_post_img);
            tvDescription = itemView.findViewById(R.id.row_post_description);
            tvPrice = itemView.findViewById(R.id.row_post_price);
            tvRatingBar = itemView.findViewById(R.id.post_detail_ratingBar);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent postDetailActivity = new Intent(mContext, PostDetailActivity.class);
                    int position = getAdapterPosition();

                    postDetailActivity.putExtra("title", mData.get(position).getTitle());
                    postDetailActivity.putExtra("picture",mData.get(position).getPicture());
                    postDetailActivity.putExtra("price", mData.get(position).getPrice());
                    postDetailActivity.putExtra("description", mData.get(position).getDescription());
                    postDetailActivity.putExtra("postKey", mData.get(position).getPostKey());
                    postDetailActivity.putExtra("category", mData.get(position).getCategory());
                    postDetailActivity.putExtra("location", mData.get(position).getLocation());





                    //long timestamp = (long) mData.get(position).getTimeStamp();
                    //If we want the timestamp + uncomment the postdetailactivity line 84
                    //postDetailActivity.putExtra("postDate", timestamp);

                    mContext.startActivity(postDetailActivity);

                }
            });


        }
    }


}


