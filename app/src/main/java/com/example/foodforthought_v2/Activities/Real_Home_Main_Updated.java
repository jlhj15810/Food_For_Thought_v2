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

public class Real_Home_Main_Updated extends AppCompatActivity{
    private Button logIn;
    private Button createAcc;

    private LoginFragment loginFragment;
    private RegisterFragment registerFragment;


    FirebaseUser currentUser;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //MUST BE REAL_MAIN_PAGE else cant even run.
        setContentView(R.layout.activity_main_updated);

        Toolbar toolbar = findViewById(R.id.toolbar);

        //Set our created toolbar as the action bar
        setSupportActionBar(toolbar);


        //initiate the Firebase

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();


        loginFragment = new LoginFragment();
        registerFragment = new RegisterFragment();

        logIn = (Button) findViewById(R.id.buttonLogIn);
        createAcc = (Button) findViewById(R.id.buttonCreateAnAccUpdated);

        logIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSupportFragmentManager().beginTransaction().replace(R.id.layout,loginFragment).commit();
            }
        });

        createAcc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSupportFragmentManager().beginTransaction().replace(R.id.layout,registerFragment).commit();
            }
        });

    }


    private void openLogInPage () {
        Intent intent = new Intent(this, LoginFragment.class);
        startActivity(intent);
    }

    private void openCreateAccPage () {
        Intent intent = new Intent(this, RegisterFragment.class);
        startActivity(intent);

    }



//
//        case R.id.nav_register:
//        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
//                new RegisterFragment()).commit();
//
//        break;
//
//
//        case R.id.nav_login:
//        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
//                new LoginFragment()).commit();
//        break;






    @Override
    public void onStart() {

        super.onStart();

    }
}
