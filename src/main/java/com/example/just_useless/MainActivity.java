package com.example.just_useless;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    private Button userSignUp;
    private Button userSignIn;
    private Button ambulanceSignUp;
    private Button ambulanceSignIn;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        userSignIn= (Button) findViewById(R.id.user_signin_btn);
        userSignUp = (Button) findViewById(R.id.user_sign_up_btn);
        //ambulanceSignIn = (Button) findViewById(R.id.ambulance_sign_in_btn);
        ambulanceSignUp = (Button) findViewById(R.id.ambulance_sign_up_btn);

        //firebase

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("users");

        userSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,SignupActivity.class);
                startActivity(intent);
            }
        });
        userSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,SigninActivity.class);
                startActivity(intent);
            }
        });
        ambulanceSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,AmbulanceSignupActivity.class);
                startActivity(intent);
            }
        });
//        ambulanceSignIn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(MainActivity.this,AmbulanceSigninActivity.class);
//                startActivity(intent);
//            }
//        });



    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser currentUser = mAuth.getCurrentUser();

        if(currentUser !=null){

            String UID = currentUser.getUid().toString();

            mDatabase = mDatabase.child(UID).child("group");
            mDatabase.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    String check = dataSnapshot.getValue().toString();

                    if(check.equals("user")){
                        Intent intent = new Intent(MainActivity.this,UserLandingActivity.class);
                        startActivity(intent);
                        finish();
                    }
                    if(check.equals("ambulance")){
                        Intent intent = new Intent(MainActivity.this,AmbulanceLandingActivity.class);
                        startActivity(intent);
                        finish();
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }



    }
}
