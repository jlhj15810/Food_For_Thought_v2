package com.example.foodforthought_v2.Fragments;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.Rating;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.bumptech.glide.Glide;
import com.example.foodforthought_v2.Utilities.Comment;
import com.example.foodforthought_v2.Utilities.Post;
import com.example.foodforthought_v2.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import static android.app.Activity.RESULT_OK;

//To add new Restaurant. For admins Use

public class Add_New_Restaurant extends Fragment {

    ImageView popupUserImage, popupPostImage, popupAddButton;
    TextView popupTitle, popupDescription, popupPriceRange;
    ProgressBar popupProgressBar;
    Dialog popAddPost;
    FirebaseUser currentUser;
    FirebaseAuth mAuth;
    static int PReqCode = 2;
    static int REQUESTCODE = 2;
    private Uri pickedImguri = null;
    Comment comment;

    private RatingBar ratingBar;


    public Add_New_Restaurant() {
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        popAddPost = new Dialog(getContext());
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        initPopup(view);
        setupPopUpImageClick();
    }


    //Initialise my popup icons
    private void initPopup(View views) {

        popupUserImage = views.findViewById(R.id.popup_user_Image);
        popupPostImage = views.findViewById(R.id.popup_image);
        popupTitle = views.findViewById(R.id.popup_title);
        popupPriceRange = views.findViewById(R.id.popup_priceRange);
        popupDescription = views.findViewById(R.id.popup_description);
        popupAddButton = views.findViewById(R.id.popup_add);
        popupProgressBar = views.findViewById(R.id.popup_progressBar);
        ratingBar = views.findViewById(R.id.row_post_ratingBar);

        //Add post click listener

        popupAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupAddButton.setVisibility(View.INVISIBLE);
                popupProgressBar.setVisibility(View.VISIBLE);

                //We need to test all input fields (Title and description) and post image

                if(!popupTitle.getText().toString().isEmpty() && !popupDescription.toString().isEmpty()
                        && pickedImguri != null) {

                    //Everything is okay, no empty fields
                    //TODO Create the restaurant object and add it into Firebase database
                    //We need to upload the post Image
                    //Access firebase storage

                    StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("restaurant_images");
                    final StorageReference imageFilePath = storageReference.child(pickedImguri.getLastPathSegment());
                    imageFilePath.putFile(pickedImguri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            imageFilePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    String imageDownloadLink = uri.toString();
                                    //Once we get the download link, we create the new "Post" Object

                                    Post post = new Post(popupTitle.getText().toString(),
                                            popupDescription.getText().toString(),
                                            popupPriceRange.getText().toString(),
                                            imageDownloadLink,
                                            (int)comment.getRatings(),
                                            //Default category and location, change if necessary to do so
                                            "Hello",
                                            "Hello Hello",
                                            "Bye"

                                    );



                                    //Add the post to firebase database

                                    addPost(post);


                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    // Something goes wrong while uploading the picture
                                    // I Love you Claire Yeo.

                                    showMessage(e.getMessage());
                                    popupProgressBar.setVisibility(View.INVISIBLE);
                                    popupAddButton.setVisibility(View.VISIBLE);
                                }
                            });
                        }
                    });





                } else {
                    //The guy fucked it up

                    showMessage("Please verify all input fields :)");
                    popupAddButton.setVisibility(View.VISIBLE);
                    popupProgressBar.setVisibility(View.INVISIBLE);

                }


            }
        });


        mAuth.addAuthStateListener(new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                // if logged in, remove from nav bar items
                if (firebaseAuth.getCurrentUser() != null) {

                    popupPostImage.setImageResource(R.drawable.square_background_grey);
                    Glide.with(getActivity()).load(mAuth.getCurrentUser().getPhotoUrl()).into(popupUserImage);

                } else {
                    popupPostImage.setImageResource(R.drawable.square_background_grey);
                }
            }
        });
    }

    private void addPost(Post post) {

        FirebaseDatabase database = FirebaseDatabase.getInstance();

        //To change the path ID in firebase look at me :)
        //Change to restaurants later
        DatabaseReference myReference = database.getReference("Restaurants").push();

        //Get post unique ID and update post key
        String key = myReference.getKey();
        post.setPostKey(key);

        //Add post data to firebase database

        myReference.setValue(post).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                showMessage("Post Added successfully :)");
                popupProgressBar.setVisibility(View.INVISIBLE);
                popupAddButton.setVisibility(View.VISIBLE);
                updateUI();
            }
        });

    }

    private void showMessage(String you_messed_it_up) {

        Toast.makeText(getContext(),you_messed_it_up,Toast.LENGTH_LONG).show();
    }

    //To go back to home fragment
    private void updateUI() {

        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_container, new Home());
        ft.commit();
    }


    //Basically to click the image
    private void setupPopUpImageClick() {
        popupPostImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Here when image click, we need to open the gallery to get the picture
                //Before we open the gallery, we need to check if app has access to the gallery
                //If not we can see his *hahahahaha*

                if (Build.VERSION.SDK_INT >= 22) {

                    checkAndRequestForPermission();

                    // Toast.makeText(getContext(),"Im bigger", Toast.LENGTH_SHORT).show();
                } else {
                    //Toast.makeText(getContext(),"Im smaller", Toast.LENGTH_SHORT).show();
                    openGallery();
                }

            }
        });
    }


    //To Open the gallery
    private void openGallery() {
        //Open the gallery and wait for the user to pick his photo

        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, REQUESTCODE);


    }

    //To Ask for Permission
    private void checkAndRequestForPermission() {

        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE)) {
                Toast.makeText(getActivity().getApplicationContext(), "Please accept for required permission", Toast.LENGTH_SHORT).show();
            } else {
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        PReqCode);
            }
        } else {
            openGallery();
        }

    }


    //When user picked the right image
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (resultCode == RESULT_OK && requestCode == REQUESTCODE) {

            // the user has successfully picked his/her photo already
            // We need to save and set the code reference


            pickedImguri = data.getData();
            popupPostImage.setImageURI(pickedImguri);


        }


    }



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.add_new_restaurant, container, false);
    }
}
