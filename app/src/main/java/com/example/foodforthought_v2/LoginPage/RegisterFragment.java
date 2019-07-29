

package com.example.foodforthought_v2.LoginPage;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.bumptech.glide.Glide;
import com.example.foodforthought_v2.Fragments.Home;
import com.example.foodforthought_v2.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import static android.app.Activity.RESULT_OK;

public class RegisterFragment extends Fragment {

    ImageView userPhoto;
    static int PReqCode = 1;
    static int REQUESTCODE = 1;
    Uri pickedImguri;
    NavigationView navigationView;

    private EditText userEmail, userPassword, userPassword2, userName;
    private ProgressBar loadingProgress;
    private Button registerButton;

    private FirebaseAuth mAuth;


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Registration");

        //User's Email
        userEmail = view.findViewById(R.id.login_email);
        userPassword = view.findViewById(R.id.login_password);
        userPassword2 = view.findViewById(R.id.comfirm_password);
        userName = view.findViewById(R.id.name);
        loadingProgress = view.findViewById(R.id.login_progressBar);
        registerButton = view.findViewById(R.id.register);
        userPhoto = view.findViewById(R.id.login_image);

        loadingProgress.setVisibility(View.INVISIBLE);


        //For firebase stuff
        mAuth = FirebaseAuth.getInstance();


        //For when you press register
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerButton.setVisibility(View.INVISIBLE);
                loadingProgress.setVisibility(View.VISIBLE);
                final String email = userEmail.getText().toString();
                final String password = userPassword.getText().toString();
                final String password2 = userPassword2.getText().toString();
                final String name = userName.getText().toString();

                if (email.isEmpty() || name.isEmpty() || password.isEmpty() || !password2.equals(password2)) {

                    //If user never input correctly or something goes wrong and we need to display an error
                    showMessage("Please Verify all fields");
                    loadingProgress.setVisibility(View.INVISIBLE);
                    registerButton.setVisibility(View.VISIBLE);
                } else if (pickedImguri == null){
                    showMessage("Please click on the imaage to upload a profile picture");
                    loadingProgress.setVisibility(View.INVISIBLE);
                    registerButton.setVisibility(View.VISIBLE);
                }else {
                    //The guy is a good boy and never fuck it up
                    //So we create the account for him :)

                    CreateUserAccount(email, name, password);
                }

            }
        });


        //When you click the user photo
        userPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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

    private void CreateUserAccount(String email, final String name, String password) {

        //This will create a new user account for our user

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            //User account created successfully
                            showMessage("Account created");

                            //After successful creation, update profile pict and name
                            updateUserInfo(name, pickedImguri, mAuth.getCurrentUser());

                        } else {

                            //User account not created successfully
                            showMessage("Account creation failed :(" + task.getException().getMessage());
                            registerButton.setVisibility(View.VISIBLE);
                            loadingProgress.setVisibility(View.INVISIBLE);

                        }
                    }
                });


    }

    //We use storage to store the dudes photos
    //Authetentication to store the dudes username details


    //Now we need to store the users commentsssssss

    //To create the username and his photo
    private void updateUserInfo(final String name, Uri pickedImguri, final FirebaseUser currentUser) {

        //Need to load photo to firebase storage and get the URI

        StorageReference mStorage = FirebaseStorage.getInstance().getReference().child("users_photos");
        final StorageReference imageFilePath = mStorage.child(pickedImguri.getLastPathSegment());
        imageFilePath.putFile(pickedImguri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(final UploadTask.TaskSnapshot taskSnapshot) {

                //Image uploaded succeeded
                //Now can get image uri :)

                imageFilePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {

                        //uri contains user image uri (uri being the thingy)

                        UserProfileChangeRequest profileUpdate = new UserProfileChangeRequest.Builder()
                                .setDisplayName(name)
                                .setPhotoUri(uri)
                                .build();

                        currentUser.updateProfile(profileUpdate)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {

                                            //User information is updated sucessfully,
                                            showMessage("Registration Complete!");
                                            updateUI();
                                        }


                                    }
                                });
                    }
                });
            }
        });

    }

    private void updateUI() {

        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_container, new Home());
        ft.commit();
        updateNavHeader();

    }


    //Just used to give a toast to tell the dipshit he messed up
    private void showMessage(String please_verify_all_fields) {
        Toast.makeText(getActivity().getApplicationContext(), please_verify_all_fields, Toast.LENGTH_LONG).show();
    }

    private void openGallery() {
        //Open the gallery and wait for the user to pick his photo

        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, REQUESTCODE);


    }

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


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (resultCode == RESULT_OK && requestCode == REQUESTCODE) {

            // the user has successfully picked his/her photo already
            // We need to save and set the code reference


            pickedImguri = data.getData();
            userPhoto.setImageURI(pickedImguri);

        }


    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_register, container, false);
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


}

