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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignupActivity extends AppCompatActivity {

    private TextView userName;
    private TextView userEmail;
    private TextView userPhoneNumber;
    private TextView userPass;
    private TextView userConfPass;
    private Button signUp;
    private ProgressDialog mProgressDialog;

    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        mProgressDialog = new ProgressDialog(this);

        mProgressDialog.setTitle("Registering");
        mProgressDialog.setMessage("Saving Data");


        //Database

        mDatabase = FirebaseDatabase.getInstance().getReference().child("users");

        userName = (TextView) findViewById(R.id.user_name_signup);
        userEmail = (TextView) findViewById(R.id.user_email_signup);
        userPhoneNumber = (TextView) findViewById(R.id.user_phone_number);
        userPass = (TextView) findViewById(R.id.user_signup_pass1);
        userConfPass = (TextView) findViewById(R.id.user_signup_pass2);

        mAuth = FirebaseAuth.getInstance();

        signUp = (Button) findViewById(R.id.user_signup_btn);

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                register();

            }
        });


    }

    private void register() {




        mProgressDialog.show();

        final String userNameR = userName.getText().toString();
        final String userEmailR = userEmail.getText().toString();
        final String userPhoneNumberR = userPhoneNumber.getText().toString();
        final String userPassR = userPass.getText().toString();
        final String userConfPassR = userConfPass.getText().toString();

        if(!TextUtils.isEmpty(userNameR) && !TextUtils.isEmpty(userEmailR) && !TextUtils.isEmpty(userPhoneNumberR)
                && !TextUtils.isEmpty(userPassR) &&!TextUtils.isEmpty(userConfPassR)){
            if(userConfPassR.equals(userPassR)){
                mAuth.createUserWithEmailAndPassword(userEmailR,userConfPassR).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid().toString();

                        mDatabase = mDatabase.child(uid);

                        mDatabase.child("name").setValue(userNameR);
                        mDatabase.child("email").setValue(userEmailR);
                        mDatabase.child("phone").setValue(userPhoneNumberR);
                        mDatabase.child("pass").setValue(userPassR);
                        mDatabase.child("group").setValue("user");
                        mDatabase.child("optionalno").setValue("0000000000");
                        mDatabase.child("permanentaddress").setValue("India");

                        Intent intent = new Intent(SignupActivity.this,UserLandingActivity.class);

                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        mProgressDialog.dismiss();
                        startActivity(intent);
                        finish();

                    }
                });
            }
            else{
                mProgressDialog.hide();
                Toast.makeText(this,"Password Not Same",Toast.LENGTH_LONG).show();
            }

        }
        else{
            mProgressDialog.hide();
            Toast.makeText(this,"Field is Empty",Toast.LENGTH_LONG).show();
        }







    }
}
