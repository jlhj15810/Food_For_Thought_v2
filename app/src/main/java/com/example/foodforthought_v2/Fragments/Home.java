package com.example.foodforthought_v2.Fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.example.foodforthought_v2.Utilities.Post;
import com.example.foodforthought_v2.Utilities.PostAdapter;
import com.example.foodforthought_v2.R;
import com.google.firebase.database.*;

import java.util.ArrayList;
import java.util.List;

//Our Home Page, the page which shows a lot of our things

public class Home extends Fragment {

    RecyclerView postRecyclerView;
    PostAdapter postAdapter;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    List<Post> postList;
    SearchView searchView;
    ImageView filter_button_dropdowns;


    static String[] food_choices = {"Halal", "Vegetarian", "No Beef", "$", "$$", "$$$", "Original"};
    AlertDialog alertDialog;
    List<String> selectedItemList = new ArrayList<>();

    boolean[] checked_food = new boolean[food_choices.length];

    TextView textView;
    String a = "";




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        Bundle bundle = this.getArguments();
        if (bundle != null) {
            Toast.makeText(getContext(), bundle.getString("edttext"), Toast.LENGTH_SHORT).show();
            a = bundle.getString("edttext");


        }


    }

    public Home() {
        // Required empty public constructor
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Home");


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        // return inflater.inflate(R.layout.row_post_item, container, false); (Original)

        final View fragmentView = inflater.inflate(R.layout.fragment_home, container, false);
        postRecyclerView = fragmentView.findViewById(R.id.postRV);
        postRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        firebaseDatabase = FirebaseDatabase.getInstance();
        //Change back to Restaurants later
        databaseReference = firebaseDatabase.getReference("Restaurant2");
        searchView = fragmentView.findViewById(R.id.searchView);

        textView = fragmentView.findViewById(R.id.home_textview);

        //For the dialog instead of the navigation bar
        filter_button_dropdowns = fragmentView.findViewById(R.id.filter_button_dropdown);

        filter_button_dropdowns.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.show();
            }
        });


        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Select your food");
        builder.setItems(food_choices, null);

        builder.setNegativeButton("Cancel", null);


        builder.setMultiChoiceItems(food_choices, checked_food, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which, boolean isChecked) {

                if(isChecked) {
                    selectedItemList.add(food_choices[which]);
                } else {
                    selectedItemList.remove(food_choices[which]);
                }

            }
        })
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        List<Post> testing_posts = new ArrayList<>();

                        if(selectedItemList.size() == 0) {
                            search(" ");
                        }

                        else {

                            if (selectedItemList.get(0) == "Original") {
                                search(" ");
                            }

                            if (selectedItemList.size() == 1) {
                                search(selectedItemList.get(0));
                            }

                            //Means the jackass selected more than one field,
                            else {



                                    for (Post post : postList) {

                                        for (String s : selectedItemList) {
                                            if (post.getCategory().toLowerCase().contains(s.toLowerCase()) || (post.getPrice().equals(s))) {
                                                testing_posts.add(post);
                                            }
                                        }
                                    }



                                List<Post> removed_duplicates = new ArrayList<>();

                                for (Post p : testing_posts) {
                                    if (!(removed_duplicates.contains(p))) {
                                        removed_duplicates.add(p);
                                    }
                                }


                                PostAdapter postAdapter = new PostAdapter(getActivity(), removed_duplicates);
                                postRecyclerView.setAdapter(postAdapter);

//
//                            for(Post post : postList) {
//                                if(post.getCategory().toLowerCase().contains())
//                            }
                            }
                        }
//                        List<Post> testing_posts = new ArrayList<>();
//
//                        if(selectedItemList.size() > 1) {
//
//                        }
//
//
//                        for (Post post : postList) {
//
//                            //If our description contains the search field we are interested in,
//
//                            if (post.getDescription().toLowerCase().contains(s.toLowerCase()) || post.getCategory().toLowerCase().contains(s.toLowerCase())
//                                    || post.getPrice().equals(s)) {
//                                list.add(post);
//                            }
//
//                        }
//
//
//                        for(String s :selectedItemList) {
//                            search(s);
//                        }

                      //  search(selectedItemList.get(0));

                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        alertDialog= builder.create();

        return fragmentView;

    }


    @Override
    public void onStart() {
        super.onStart();


        if (databaseReference != null) {

            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    if (dataSnapshot.exists()) {
                        postList = new ArrayList<>();
                        for (DataSnapshot postsnap : dataSnapshot.getChildren()) {
                            Post postss = postsnap.getValue(Post.class);
                            postList.add(postss);

                        }

                        postAdapter = new PostAdapter(getActivity(), postList);
                        postRecyclerView.setAdapter(postAdapter);
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(getActivity(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();

                }
            });
        }
        if (searchView != null) {
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String s) {
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String s) {

                    search(s);

                    return true;
                }
            });
        }


    }



    public void search(String s) {
        List<Post> list = new ArrayList<>();
        for (Post post : postList) {

            //If our description contains the search field we are interested in,

            if (post.getDescription().toLowerCase().contains(s.toLowerCase()) || post.getCategory().toLowerCase().contains(s.toLowerCase())
                    || post.getPrice().equals(s)) {
                list.add(post);
            }

        }

        PostAdapter postAdapter = new PostAdapter(getActivity(), list);
        postRecyclerView.setAdapter(postAdapter);

    }


}
