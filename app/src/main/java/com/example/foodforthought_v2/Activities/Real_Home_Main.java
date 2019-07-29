package com.example.foodforthought_v2.Activities;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;
import com.example.foodforthought_v2.Fragments.Add_New_Restaurant;
import com.example.foodforthought_v2.Fragments.Home;
import com.example.foodforthought_v2.LoginPage.LoginFragment;
import com.example.foodforthought_v2.LoginPage.RegisterFragment;
import com.example.foodforthought_v2.R;
import com.example.foodforthought_v2.Fragments.UserProfile_Fragment;
import com.example.foodforthought_v2.Utilities.Post;
import com.example.foodforthought_v2.Utilities.PostAdapter;
import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.*;

import java.util.ArrayList;
import java.util.List;


//The backbone of our code which runs the navigation bar and all

public class Real_Home_Main extends AppCompatActivity implements
        NavigationView.OnNavigationItemSelectedListener {

    FirebaseUser currentUser;
    FirebaseAuth mAuth;
    Dialog popAddPost;



    private DrawerLayout drawerLayout;

    public interface Listener23 {
        void onInputASent(CharSequence input);
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //MUST BE REAL_MAIN_PAGE else cant even run.
        setContentView(R.layout.real_main_page);


        Toolbar toolbar = findViewById(R.id.toolbar);

        //Set our created toolbar as the action bar
        setSupportActionBar(toolbar);


        //initiate the Firebase

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        //Initialise the popup icon
        initPopup();



        drawerLayout = findViewById(R.id.drawer_layout);
        final NavigationView navigationView = findViewById(R.id.nav_view);
        final NavigationView slider = findViewById(R.id.filter_menu);
        MenuItem halal = slider.getMenu().findItem(R.id.filter_menu_halal);
        final CompoundButton switca = (CompoundButton) MenuItemCompat.getActionView(halal);



        switca.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean checky = switca.isChecked();

                //Means that the checbox is checked,
                if(checky) {
                    CharSequence input = switca.getText();

                    Bundle bundle = new Bundle();
                    bundle.putString("edttext", "halal");
                    Home home = new Home();
                    home.setArguments(bundle);
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, home).commit();
                    //Sent to whoever is implementing this interface, which is Home.class

                }


                //   Toast.makeText(getApplicationContext(),switca.getText().toString(), Toast.LENGTH_SHORT).show();
            }
        });

        navigationView.setNavigationItemSelectedListener(this);


        mAuth.addAuthStateListener(new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                // if logged in, remove from nav bar items
                if (firebaseAuth.getCurrentUser() != null) {

                    Menu navMenuLogIn = navigationView.getMenu();


                    navMenuLogIn.findItem(R.id.nav_login).setVisible(false);
                    navMenuLogIn.findItem(R.id.nav_register).setVisible(false);
                    navMenuLogIn.findItem(R.id.nav_signout).setVisible(true);
                    navMenuLogIn.findItem(R.id.nav_add_new_location).setVisible(true);
                } else {
                    // show items in nav bar
                    Menu navMenuLogIn = navigationView.getMenu();
                    navMenuLogIn.findItem(R.id.nav_login).setVisible(true);
                    navMenuLogIn.findItem(R.id.nav_register).setVisible(true);
                    navMenuLogIn.findItem(R.id.nav_signout).setVisible(false);
                    navMenuLogIn.findItem(R.id.nav_add_new_location).setVisible(false);
                }
            }
        });


        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar
                , R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        drawerLayout.addDrawerListener(toggle);
        //To rotate the hamburger icon when you turn on
        toggle.syncState();

        //So when you start the app then like that, show the message fragment
        if (savedInstanceState == null) {

            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new Home()).commit();

        }


        navigationView.setCheckedItem(R.id.nav_home);

    }




    //Set up the popup to add our stuff in the database
    private void initPopup() {
        popAddPost = new Dialog(this);
        popAddPost.setContentView(R.layout.add_new_restaurant);
        popAddPost.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        popAddPost.getWindow().setLayout(Toolbar.LayoutParams.MATCH_PARENT, Toolbar.LayoutParams.WRAP_CONTENT);
        popAddPost.getWindow().getAttributes().gravity = Gravity.TOP;

    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        //AddtoBackStack so when you press back, it will not destroy the launched fragment

        switch (menuItem.getItemId()) {

            case R.id.nav_home:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new Home()).commit();

                break;

            case R.id.nav_register:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new RegisterFragment()).commit();

                break;


            case R.id.nav_login:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new LoginFragment()).commit();
                break;


            case R.id.nav_profile:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new UserProfile_Fragment()).commit();
                break;

            case R.id.nav_share:
                Toast.makeText(this, "Share", Toast.LENGTH_SHORT).show();
                break;

            case R.id.nav_send:
                Toast.makeText(this, "Send", Toast.LENGTH_SHORT).show();
                break;

            case R.id.nav_signout:

                FirebaseAuth.getInstance().signOut();
                LoginManager.getInstance().logOut();

                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new Home()).commit();
                updateNavHeader();
                break;

            case R.id.nav_add_new_location:


                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new Add_New_Restaurant()).commit();
                break;
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {

        //Close the navigation bar without closing the activity!
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            //If drawer is on the right side on the screen, then use "GravityCompat.END"
            drawerLayout.closeDrawer(GravityCompat.START);

        }
        //This will on default close the activity!
        else {
            super.onBackPressed();
        }
    }


    public void updateNavHeader() {

        NavigationView navigationView = findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        final String generic_nav_user_name = "Guest";
        final String generic_nav_email_address = "Guest@hotmail.com";
        TextView navUserName = headerView.findViewById(R.id.navigation_bar_profile_name);
        TextView navEmail = headerView.findViewById(R.id.navigation_bar_email_address);
        ImageView navDisplayPicture = headerView.findViewById(R.id.navigation_bar_pic);

        navEmail.setText(generic_nav_user_name);
        navUserName.setText(generic_nav_email_address);
        navDisplayPicture.setImageResource(R.drawable.loginhuman_nowords);

    }

    @Override
    public void onStart() {

        super.onStart();

    }



//    //To check if we need. Not important, good to have only
//    @Override
//    public void onAttachFragment(Fragment fragment){
//        super.onAttachFragment(fragment);
//
//        if(fragment instanceof  Listener23) {
//            listener2 = (Listener23) fragment;
//        } else {
//            throw new RuntimeException(fragment.toString()
//            + " must implement Listener");
//        }
//
//    }


}
