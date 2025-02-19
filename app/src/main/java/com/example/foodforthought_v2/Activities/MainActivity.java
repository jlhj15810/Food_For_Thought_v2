package com.example.foodforthought_v2.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import com.example.foodforthought_v2.R;
import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;

//The page which holds the main page which "Finds some food"

public class MainActivity extends AppCompatActivity {

    private Button button;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button = (Button) findViewById(R.id.button123);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivity3();
            }
        });

    }


    public void openActivity3() {

        // To ensure that any users gets logged out.
        FirebaseAuth.getInstance().signOut();
        LoginManager.getInstance().logOut();
        Intent intent = new Intent(this, Real_Home_Main.class);
        startActivity(intent);
    }


}
