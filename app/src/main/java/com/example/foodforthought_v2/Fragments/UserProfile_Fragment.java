package com.example.foodforthought_v2.Fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.example.foodforthought_v2.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


//When you click Fragment

public class UserProfile_Fragment extends Fragment {

    FirebaseUser currentUser;
    FirebaseAuth mAuth;
    TextView profile_name, profile_email_address;
    ImageView profile_profile_picture;


    //This is for later on to implement number of reviews
    TextView profile_reviews;

    public UserProfile_Fragment() {
        // Required empty public constructor
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("User Profile");
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        profile_name = view.findViewById(R.id.profile_name);
        profile_email_address = view.findViewById(R.id.profile_email);
        profile_profile_picture = view.findViewById(R.id.profile_image);

        mAuth.addAuthStateListener(new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                // if logged in, remove from nav bar items
                if (firebaseAuth.getCurrentUser() != null) {


                    profile_name.setText("Welcome " + mAuth.getCurrentUser().getDisplayName());
                    profile_email_address.setText(mAuth.getCurrentUser().getEmail());
                    Glide.with(getActivity()).load(mAuth.getCurrentUser().getPhotoUrl()).into(profile_profile_picture);

                } else {
                    // show items in nav bar
                   profile_name.setText("Welcome Guest");
                   profile_email_address.setText("Guest@hotmail.com");
                }
            }
        });

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_user_profile, container, false);
    }
}
