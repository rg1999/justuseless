package com.example.just_useless;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SigninActivity extends AppCompatActivity {

    private TextView name;
    private TextView pass;
    private Button signin;

    private FirebaseAuth mAuth;
    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        name = (TextView) findViewById(R.id.user_name_sign_in);
        pass = (TextView) findViewById(R.id.user_pass_signin);

        signin = (Button) findViewById(R.id.user_signin_btn);
        mAuth = FirebaseAuth.getInstance();
        mProgressDialog = new ProgressDialog(this);

        mProgressDialog.setTitle("Signing in");
        mProgressDialog.setMessage("checking");


        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                login();

            }
        });

    }

    private void login() {

        mProgressDialog.show();

        String email = name.getText().toString();
        String passs = pass.getText().toString();

        mAuth.signInWithEmailAndPassword(email,passs).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if(task.isSuccessful()){

                    String uid = FirebaseAuth.getInstance().getCurrentUser().getUid().toString();

                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("users").child(uid);

                    ref.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            if(dataSnapshot.child("group").getValue().toString().equals("user")){

                                Intent intent = new Intent(SigninActivity.this,UserLandingActivity.class);
                                mProgressDialog.dismiss();
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                                finish();


                            }
                            else if(dataSnapshot.child("group").getValue().toString().equals("ambulance")){
                                Intent intent = new Intent(SigninActivity.this,AmbulanceLandingActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                mProgressDialog.dismiss();
                                startActivity(intent);
                                finish();
                            }


                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                }
                else{
                    mProgressDialog.hide();
                }


            }
        });



    }
}
