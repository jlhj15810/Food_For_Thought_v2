package com.example.foodforthought_v2.Utilities;

import android.widget.RatingBar;
import com.google.firebase.database.ServerValue;

public class Comment {

    public Comment() {
    }

    //uid == user id
    private String content, uid, uimg, uname;
    private Object timestamp;
    private float ratings;

    public Comment(String content, String uid, String uimg, String uname, float ratings) {
        this.content = content;
        this.uid = uid;
        this.uimg = uimg;
        this.uname = uname;
        this.timestamp = ServerValue.TIMESTAMP;
        this.ratings = ratings;
    }

    public Comment(String content, String uid, String uimg, String uname, float ratings, Object timestamp) {
        this.content = content;
        this.uid = uid;
        this.uimg = uimg;
        this.uname = uname;
        this.ratings = ratings;
        this.timestamp = timestamp;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUimg() {
        return uimg;
    }

    public void setUimg(String uimg) {
        this.uimg = uimg;
    }

    public String getUname() {
        return uname;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }

    public Object getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Object timestamp) {
        this.timestamp = timestamp;
    }

    public float getRatings() {

        //This shit need to think again,

         if ((this.ratings >0 && this.ratings <0.5) || this.ratings <0) {
            return  0;
        } else if (this.ratings >=0.5 && this.ratings <1) {
            return (float) 0.5;
        } else if (this.ratings >=1 && this.ratings <1.5) {
             return (float) 1;
         } else if (this.ratings >=1.5 && this.ratings <2) {
             return (float) 1.5;
         } else if (this.ratings >=2 && this.ratings <2.5) {
             return (float) 2.0;
         } else if (this.ratings >=2.5 && this.ratings <3) {
             return (float) 2.5;
         } else if (this.ratings >=3 && this.ratings <3.5) {
             return (float) 3.0;
         } else if (this.ratings >=3.5 && this.ratings <4) {
             return (float) 3.5;
         } else if (this.ratings >=4 && this.ratings <4.5) {
             return (float) 4.0;
         } else if (this.ratings >=4.5 && this.ratings <5) {
             return (float) 4.5;
         } else {
             return (float) 5;
         }
    }

    public void setRatings(float ratings) {
        this.ratings = ratings;
    }
}
