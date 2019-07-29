package com.example.foodforthought_v2.Activities;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.*;
import com.bumptech.glide.Glide;
import com.example.foodforthought_v2.R;
import com.example.foodforthought_v2.Utilities.Comment;
import com.example.foodforthought_v2.Utilities.CommentAdapter;
import com.facebook.CallbackManager;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.*;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class PostDetailActivity extends AppCompatActivity {

    ImageView imagePost, imageCurrentUser;
    TextView textPostDescription, textPrice, textPostTitle;
    TextView textCategory;
    EditText editTextComment;
    Button buttonAddComment;
    String PostKey, postLocation;
    RatingBar ratingBar;


    ImageView share_to_facebook_picture;
    CallbackManager callbackManager;
    ShareDialog shareDialog;

    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;

    FirebaseDatabase firebaseDatabase;

    RecyclerView RvComment;
    CommentAdapter commentAdapter;
    List<Comment> commentList;
    List<Comment> reverseCommentList;

    ImageView open_to_google_maps_button;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_detail);

        //To set the status bar to transparent
//        Window w = getWindow();
//        w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
//        getSupportActionBar().hide();


        // Initialise the view



        RvComment = findViewById(R.id.rv_comment);
        imagePost = findViewById(R.id.post_detail_img);
        imageCurrentUser = findViewById(R.id.post_detail_currentuser_img);

        textPostTitle = findViewById(R.id.post_detail_title);
        textPostDescription = findViewById(R.id.post_detail_desc);

        //Later refractor the date time thing
        textPrice = findViewById(R.id.post_detail_date_name);

        editTextComment = findViewById(R.id.post_detail_comment);
        buttonAddComment = findViewById(R.id.post_detail_add_comment_btn);

        ratingBar = findViewById(R.id.post_detail_ratingBar);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();

        textCategory = findViewById(R.id.category);


        //Initialise the facebook share button
        share_to_facebook_picture = findViewById(R.id.share_to_facebook);
        callbackManager = CallbackManager.Factory.create();
        shareDialog = new ShareDialog(this);

        //Initialise the google maps button
        open_to_google_maps_button = findViewById(R.id.open_in_google_maps);


        share_to_facebook();
        open_in_google_maps();

        add_comment();


        //Now we need to bind all the data into those views
        // Need to get the post data from firebase and into this activity.


        String postImage = getIntent().getExtras().getString("picture");
        Glide.with(this).load(postImage).into(imagePost);

        String postTitle = getIntent().getExtras().getString("title");
        textPostTitle.setText(postTitle);

        String postPrice = getIntent().getExtras().getString("price");
        textPrice.setText("Price: " + postPrice);

        String postDescription = getIntent().getExtras().getString("description");
        textPostDescription.setText(postDescription);

        String postCategory = getIntent().getExtras().getString("category");
        if (postCategory.equals(" ")){
            textCategory.setText("Category: No Restrictions");
        } else {
            textCategory.setText("Category: " + postCategory);
        }

        postLocation = getIntent().getExtras().getString("location");


        //Set comment users image
        Glide.with(this).load(firebaseUser.getPhotoUrl()).into(imageCurrentUser);

        // To get the secure post ID
        PostKey = getIntent().getExtras().getString("postKey");

        // String date = timeStampToString(getIntent().getExtras().getLong("postDate"));
        //  textPostDateName.setText(date);


        //Initialise the recyclerview comment
        initRvComment();


    }

    private void open_in_google_maps() {
        open_to_google_maps_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Uri gmmIntentUri = Uri.parse("geo:1.2966,103.7764?q=" + postLocation);
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                startActivity(mapIntent);

            }
        });

    }


    private void share_to_facebook() {
        share_to_facebook_picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShareLinkContent linkContent = new ShareLinkContent.Builder()
                        .setQuote("Sharing " + textPostTitle.getText().toString() + " Restaurant")
                        //Maybe can put the uri from my place to the location of where i wanna go? Very Abstract, wont work 100% of the time
                        //Must think of a better way to do this instead
                        .setContentUrl(Uri.parse("https://www.google.com/maps/search/?api=1&query=" + postLocation))
                        .build();

                if (ShareDialog.canShow(ShareLinkContent.class)) {
                    shareDialog.show(linkContent);
                }
            }
        });
    }


    private void add_comment() {
        // Add Comment button click listener

        buttonAddComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                buttonAddComment.setVisibility(View.INVISIBLE);
                DatabaseReference commentReference = firebaseDatabase.getReference("Comment").child(PostKey).push();
                String comment_content = editTextComment.getText().toString();
                String uid = firebaseUser.getUid();
                String uname = firebaseUser.getDisplayName();
                String uimage = firebaseUser.getPhotoUrl().toString();
                float ratings = ratingBar.getRating();


                Comment comment = new Comment(comment_content,uid,uimage,uname, ratings);

                commentReference.setValue(comment).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        showMessage("Comment added");
                        editTextComment.setText("");
                        buttonAddComment.setVisibility(View.VISIBLE);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        showMessage("Failed to add comment " + e.getMessage());
                    }
                });


            }
        });

    }


    private void initRvComment() {

        RvComment.setLayoutManager(new LinearLayoutManager(this));

        DatabaseReference commentRef = firebaseDatabase.getReference("Comment").child(PostKey);
        commentRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                commentList = new ArrayList<>();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                    Comment commentss = snapshot.getValue(Comment.class);
                    commentList.add(commentss);

                }
                Collections.reverse(commentList);

                commentAdapter = new CommentAdapter(getApplicationContext(), commentList);
                RvComment.setAdapter(commentAdapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    private void showMessage(String s) {

        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }


    //To convert time stamp to a string. we dont need this for now.
    private String timeStampToString(long time) {
        Calendar calendar = Calendar.getInstance(Locale.ENGLISH);
        calendar.setTimeInMillis(time);
        //Change the informat to the format i want.
        String date = DateFormat.format("dd-MM-yyyy", calendar).toString();
        return date;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        //  uri = data.getData();

        super.onActivityResult(requestCode, resultCode, data);
    }
}
