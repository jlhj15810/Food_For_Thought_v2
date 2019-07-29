package com.example.foodforthought_v2.Fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.example.foodforthought_v2.MultipleChoiceDialogFragment;
import com.example.foodforthought_v2.Utilities.Post;
import com.example.foodforthought_v2.Utilities.PostAdapter;
import com.example.foodforthought_v2.R;
import com.google.firebase.database.*;

import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

//Our Home Page, the page which shows a lot of our things

public class Home extends Fragment {

    RecyclerView postRecyclerView;
    PostAdapter postAdapter;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    List<Post> postList;
    SearchView searchView;
    ImageView filter_button_dropdowns;

    static String[] food_choices = {"Halal", "Vegetarian", "No Beef", "$", "$$", "$$$", "Random Food Choice"};
    AlertDialog alertDialog;
    List<String> selectedItemList = new ArrayList<>();

    boolean[] checked_food = new boolean[food_choices.length];

    TextView textView;
    String a = "";

    boolean isRandom;



    //Can delete if you want
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
                        List<Post> testing_posts2 = new ArrayList<>();
                        List<Post> testing_posts_final = new ArrayList<>();

                        for (String item : selectedItemList){
                            if (item.equals("Random Food Choice")){
                                System.out.println("RFC ENGAGED");
                                isRandom = true;
                                break;
                            }
                        }

                        //So resets the recyclerview
                        if(selectedItemList.size() == 0) {
                            System.out.println("HERE 1");
                            displayAllRestaurant();
                        }

                        //The user chose something
                        else {
                            //If the user choses one option only
                            //If the user selects "Original", can delete if you want
                            //Resets the recyclerview
                            if (selectedItemList.size() == 1) {
                                if (selectedItemList.get(0) == "Random Food Choice") {
                                    System.out.println("HERE 2");
                                    getRandomChoice();
                                } else {
                                    System.out.println("HERE 3");
                                    search(selectedItemList.get(0));
                                }
                            }

                            //Means the jackass selected more than one field,
                            else {
                                System.out.println("HERE 4");

                                String category = "Halal Vegetarian No Beef Random Food Choice";
                                boolean isCatHasWords = false;
                                boolean isCatMoney = false;
                                for (String cat : selectedItemList) {
                                    if (category.contains(cat)){
                                        isCatHasWords = true;
                                    } else {
                                        isCatMoney = true;
                                    }
                                }

                                if (isCatHasWords && isCatMoney && !isRandom) {
                                    System.out.println("HERE 5");

                                    for (Post post : postList) {
                                        for (String s : selectedItemList) {
                                            if (post.getCategory().toLowerCase().contains(s.toLowerCase())) {
                                                testing_posts.add(post);
                                            }
                                        }
                                    }

                                    for (Post post : testing_posts) {
                                        for (String s : selectedItemList) {
                                            if (post.getPrice().equals(s)) {
                                                testing_posts2.add(post);
                                            }
                                        }
                                    }
                                } else if (isCatHasWords && !isCatMoney){
                                    System.out.println("HERE 6");

                                    for (Post post : postList) {
                                        for (String s : selectedItemList) {
                                            if (post.getCategory().toLowerCase().contains(s.toLowerCase())) {
                                                testing_posts2.add(post);
                                            }
                                        }
                                    }


                                }else {
                                    System.out.println("HERE 7");

                                    for (Post post : postList) {
                                        for (String s : selectedItemList) {
                                            if (post.getPrice().equals(s)) {
                                                testing_posts2.add(post);
                                            }
                                        }
                                    }
                                }


                                if (isRandom){
                                    getRandomChoice(testing_posts2);
                                    isRandom = false;
                                }





//                                for (String s : selectedItemList) {
//
//                                    //Consists of the restaurant you see on the mainpage
//                                    for (Post post : postList) {
//                                        if (post.getCategory().toLowerCase().contains(s.toLowerCase()) || post.getPrice().equals(s)) {
//                                            testing_posts.add(post);
//                                        }
//                                    }
//                                }

                                //List of post which contains all the non-duplicates
                                List<Post> removed_duplicates = new ArrayList<>();
                                System.out.println("2 TESTING POST 2 SIZE: " + testing_posts2.size());
                                for (Post p : testing_posts2) {
                                    System.out.println("FOR LOOP ENGAGED");
                                    if (!removed_duplicates.contains(p)) {
                                        removed_duplicates.add(p);
                                    }
                                }
                                if (removed_duplicates.size() == 0) {
                                    Toast.makeText(getContext(), "No Search Result Found", Toast.LENGTH_LONG).show();
                                }
                                //Show the results
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
        alertDialog = builder.create();

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

        //For the searchbar,
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

        //Postlist contains all the restaurants you see in the main page
        for (Post post : postList) {

            //If our description contains the search field we are interested in,

            if (post.getDescription().toLowerCase().contains(s.toLowerCase()) || post.getCategory().toLowerCase().contains(s.toLowerCase())
                    || post.getPrice().equals(s)) {
                list.add(post);
            }

        }

        if (list.size() == 0) {
            Toast.makeText(getContext(), "No Search Result Found", Toast.LENGTH_LONG).show();
        }
        //Set the recycler view to show the results of the search.
        PostAdapter postAdapter = new PostAdapter(getActivity(), list);
        postRecyclerView.setAdapter(postAdapter);

    }

    public void displayAllRestaurant () {
        List<Post> list = new ArrayList<>();

        //Postlist contains all the restaurants in the database
        for (Post post : postList) {
                list.add(post);
            }
        //Set the recycler view to show the results of the search.
        PostAdapter postAdapter = new PostAdapter(getActivity(), list);
        postRecyclerView.setAdapter(postAdapter);

    }

    public void getRandomChoice (){
        System.out.println("GET RANDOM CHOICE 1");
        List <Post> list = new ArrayList<>();
        Random rand = new Random();
        int randomElementPost = rand.nextInt(postList.size());
        list.add(postList.get(randomElementPost));
        PostAdapter postAdapter = new PostAdapter(getActivity(), list);
        postRecyclerView.setAdapter(postAdapter);

    }

    public void getRandomChoice (List <Post> testing_posts2){
        System.out.println("GET RANDOM CHOICE 2");
        System.out.println("TESTING POST 2 SIZE ERROR : " + testing_posts2.size());
        List <Post> list = new ArrayList<>();
        Random rand = new Random();
        int randomElementPost = rand.nextInt(testing_posts2.size());
        Post randPost = testing_posts2.get(randomElementPost);
        testing_posts2.clear();
        testing_posts2.add(randPost);
        System.out.println("1 TESTING POST 2 SIZE: " + testing_posts2.size());
    }
}

//
//    Post randomElementPost;
//                                for (String item : selectedItemList) {
//                                        if (item.equals("Random Food Choice")) {
//                                        Random rand = new Random();
//                                        System.out.println("TEST: " + testing_posts2.size());
//                                        int randomElement = rand.nextInt(testing_posts2.size());
//                                        randomElementPost = testing_posts2.get(randomElement);
//                                        testing_posts2.clear();
//                                        testing_posts2.add(randomElementPost);
//                                        }
//                                        }