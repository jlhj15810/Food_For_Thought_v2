package com.example.foodforthought_v2.LoginPage;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.foodforthought_v2.Fragments.Home;
import com.example.foodforthought_v2.Fragments.UserProfile_Fragment;
import com.example.foodforthought_v2.R;
import com.facebook.*;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.*;
import org.json.JSONException;
import org.json.JSONObject;

import javax.security.auth.callback.Callback;
import java.util.Arrays;
import java.util.List;


public class LoginFragment extends Fragment {

    private EditText userMail, userPassword;
    private Button button_login;
    private ProgressBar login_progress;
    private FirebaseAuth mAuth;
    CallbackManager callbackManager;
    List<AuthUI.IdpConfig> providers;
    public static final int MY_REQUEST_CODE = 7117;
    private LoginButton loginButton;

    FirebaseUser currentUser2;
    FirebaseAuth mAuth2;

    public LoginFragment() {
        // Required empty public constructor
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode,resultCode,data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Login");

        userMail = view.findViewById(R.id.login_email);
        userPassword = view.findViewById(R.id.login_password);
        button_login = view.findViewById(R.id.login_button);
        login_progress = view.findViewById(R.id.login_progressBar);



        //Initialize the facebook button
        loginButton = view.findViewById(R.id.facebook_login_button);
        loginButton.setReadPermissions(Arrays.asList("public_profile", "email"));
        callbackManager = CallbackManager.Factory.create();

        loginButton.setFragment(this);

        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                handleFacebookToken(loginResult.getAccessToken());
            }

            //If the user cancel the event
            @Override
            public void onCancel() {
                showMessage("User cancelled it");
            }

            @Override
            public void onError(FacebookException error) {
                showMessage(error.getMessage());
            }
        });


        mAuth = FirebaseAuth.getInstance();


        login_progress.setVisibility(View.INVISIBLE);


        //We need one button for facebook login here!! After login with fb then updateUI


        //This is for normal login
        button_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login_progress.setVisibility(View.VISIBLE);
                button_login.setVisibility(View.INVISIBLE);

                final String mail = userMail.getText().toString();
                final String password = userPassword.getText().toString();

                if (mail.isEmpty() || password.isEmpty()) {
                    showMessage("Please Verify All Fields");
                } else {
                    signIn(mail, password);
                }
            }
        });


    }


    private void handleFacebookToken(AccessToken accessToken) {
        AuthCredential credential = FacebookAuthProvider.getCredential(accessToken.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            updateUI();
                        }
                        //If not successful
                        else {
                            showMessage("Could not register to firebase");
                        }
                    }
                });
    }


    private void signIn(String mail, String password) {

        mAuth.signInWithEmailAndPassword(mail, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    login_progress.setVisibility(View.INVISIBLE);
                    button_login.setVisibility(View.VISIBLE);
                    updateUI();
                } else {
                    //Means dude cannot login
                    showMessage(task.getException().getMessage());
                    //To remove the login buttons and the progress thingy
                    login_progress.setVisibility(View.INVISIBLE);
                    button_login.setVisibility(View.VISIBLE);
                }
            }
        });

    }

    private void updateUI() {
        mAuth2 = FirebaseAuth.getInstance();
        currentUser2 = mAuth.getCurrentUser();

        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_container, new Home());
        ft.commit();
        showMessage("Welcome " + currentUser2.getDisplayName() + " :)");
        updateNavHeader();
    }


    public void updateNavHeader() {

        NavigationView navigationView = getActivity().findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        TextView navUserName = headerView.findViewById(R.id.navigation_bar_profile_name);
        TextView navEmail = headerView.findViewById(R.id.navigation_bar_email_address);
        ImageView navDisplayPicture = headerView.findViewById(R.id.navigation_bar_pic);

        navEmail.setText(mAuth.getCurrentUser().getEmail());
        navUserName.setText(mAuth.getCurrentUser().getDisplayName());

        //Using Glide to load user image
        Glide.with(getActivity()).load(mAuth.getCurrentUser().getPhotoUrl()).into(navDisplayPicture);
    }

    private void showMessage(String please_verify_all_fields) {

        Toast.makeText(getActivity().getApplicationContext(), please_verify_all_fields, Toast.LENGTH_LONG).show();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false);
    }


}


