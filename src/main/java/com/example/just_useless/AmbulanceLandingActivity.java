package com.example.just_useless;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AmbulanceLandingActivity extends AppCompatActivity {

    private Button map;
    private Button logout;
    private Button showRequests;
    private Button profile;
    private Button availibility;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private String uid;

    private final String CHANNEL_ID = "personal_notification";
    private final int NOTIFICATION_ID = 001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ambulance_landing);

        map = (Button) findViewById(R.id.ambulance_map);
        logout = (Button) findViewById(R.id.ambulance_logout);
        showRequests = (Button) findViewById(R.id.show_requests_btn);
        profile = (Button) findViewById(R.id.ambulance_profile_btn);
        availibility = (Button) findViewById(R.id.availibility_btn);

        mDatabase = FirebaseDatabase.getInstance().getReference().child("ambulancelocation");
        mAuth = FirebaseAuth.getInstance();
        uid = mAuth.getCurrentUser().getUid().toString();

        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(dataSnapshot.child(uid).exists()){
                    availibility.setText("U r On Duty");
                }else{
                    availibility.setText("U r Off Duty");
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        showRequests.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(AmbulanceLandingActivity.this,ShowRequestActivity.class);
                startActivity(intent);

            }
        });

        availibility.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {

                String text = availibility.getText().toString();

                if(text.equals("U r On Duty")){

                    mDatabase.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            if(dataSnapshot.child(uid).exists()){
                               dataSnapshot.child(uid).getRef().removeValue();
                               availibility.setText("U r Off Duty");
                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });




                }
                else if(text.equals("U r Off Duty")){

                    availibility.setText("U r On Duty");

                    Intent intent = new Intent(AmbulanceLandingActivity.this,AmbulanceMapsActivity.class);
                    startActivity(intent);




                }



            }
        });



        map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(AmbulanceLandingActivity.this,AmbulanceMapsActivity.class);
                startActivity(intent);

            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mAuth.signOut();
                Intent intent = new Intent(AmbulanceLandingActivity.this,MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        });

    }



    @Override
    protected void onResume() {
        super.onResume();

        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.child(uid).exists()) {
                    availibility.setText("U r On Duty");
                } else {
                    availibility.setText("U r Off Duty");
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }
}
