package com.example.just_useless;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

public class UserLandingActivity extends AppCompatActivity {

    private Button map;
    private Button nearestAmbulance;
    private Button emergency;
    private Button profile;
    private Button logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_landing);

        map = (Button) findViewById(R.id.map_btn);
        nearestAmbulance = (Button) findViewById(R.id.nearest_btn);
        emergency = (Button) findViewById(R.id.user_emergency_btn);
        profile = (Button) findViewById(R.id.user_profile_btn);
        logout = (Button) findViewById(R.id.user_logout_btn);



        map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(UserLandingActivity.this,UserMapsActivity.class);
                startActivity(intent);

            }
        });
        nearestAmbulance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(UserLandingActivity.this,SendRequestActivity.class);
                startActivity(intent);

            }
        });
        emergency.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



            }
        });
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(UserLandingActivity.this,UserProfileActivity.class);
                startActivity(intent);

            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(UserLandingActivity.this,MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        });

    }
}
