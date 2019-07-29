package com.example.foodforthought_v2.Utilities;

import android.widget.RatingBar;
import com.google.firebase.database.ServerValue;

//Used to store info about the user on firebase databse

public class Post {
    private String postKey;
    private String title;
    private String description;
    private String price;
    private String picture;

    private Object timeStamp;

    private int ratingBar_post;

    private String category;
    private String displayCategory;
    private String location;



    public Post(String title, String description, String price, String picture, int ratingBar_post, String category, String displayCategory, String location ) {
        this.title = title;
        this.description = description;
        this.price = price;
        this.picture = picture;
        this.timeStamp = ServerValue.TIMESTAMP;
        this.ratingBar_post = ratingBar_post;
        this.category = category;
        this.displayCategory = displayCategory;
        this.location = location;
    }

    public Post() {
        //Necessary empty constructor
    }


    public String getPostKey() {
        return postKey;
    }

    public void setPostKey(String postKey) {
        this.postKey = postKey;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public Object getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Object timeStamp) {
        this.timeStamp = timeStamp;
    }

    public int getRatingBar_post() {
        return ratingBar_post;
    }

    public void setRatingBar_post(int ratingBar_post) {
        this.ratingBar_post = ratingBar_post;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDisplayCategory() {
        return displayCategory;
    }

    public void setDisplayCategory(String displayCategory) {
        this.displayCategory = displayCategory;
    }
}
