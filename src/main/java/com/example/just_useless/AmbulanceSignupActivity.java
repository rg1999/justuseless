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

public class AmbulanceSignupActivity extends AppCompatActivity {

    private TextView Name;
    private TextView Email;
    private TextView PhoneNumber;
    private TextView Pass;
    private TextView ConfPass;
    private Button signUp;
    private TextView ambNo;
    private ProgressDialog mProgressDialog;

    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ambulance_signup);

        mProgressDialog = new ProgressDialog(this);

        mProgressDialog.setTitle("Registering");
        mProgressDialog.setMessage("Saving Data");


        //Database

        mDatabase = FirebaseDatabase.getInstance().getReference().child("users");

        ambNo = (TextView) findViewById(R.id.ambulance_no);

        Name = (TextView) findViewById(R.id.ambulance_name_signup);
        Email = (TextView) findViewById(R.id.ambulance_email_signup);
        PhoneNumber = (TextView) findViewById(R.id.ambulance_phone_number);
        Pass = (TextView) findViewById(R.id.ambulance_signup_pass1);
        ConfPass = (TextView) findViewById(R.id.ambulance_signup_pass2);

        mAuth = FirebaseAuth.getInstance();

        signUp = (Button) findViewById(R.id.ambulance_signup_btn);

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                register();

            }
        });


    }

    private void register() {




        mProgressDialog.show();

        final String AmbulanceNo = ambNo.getText().toString();
        final String AmbulanceuserNameR = Name.getText().toString();
        final String AmbulanceuserEmailR = Email.getText().toString();
        final String AmbulanceuserPhoneNumberR = PhoneNumber.getText().toString();
        final String AmbulanceuserPassR = Pass.getText().toString();
        final String AmbulanceuserConfPassR = ConfPass.getText().toString();

        if(!TextUtils.isEmpty(AmbulanceuserNameR) && !TextUtils.isEmpty(AmbulanceuserEmailR) && !TextUtils.isEmpty(AmbulanceuserPhoneNumberR)
                && !TextUtils.isEmpty(AmbulanceuserPassR) &&!TextUtils.isEmpty(AmbulanceuserConfPassR)){
            if(AmbulanceuserConfPassR.equals(AmbulanceuserPassR)){
                mAuth.createUserWithEmailAndPassword(AmbulanceuserEmailR,AmbulanceuserConfPassR).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid().toString();

                        mDatabase = mDatabase.child(uid);

                        mDatabase.child("name").setValue(AmbulanceuserNameR);
                        mDatabase.child("email").setValue(AmbulanceuserEmailR);
                        mDatabase.child("phone").setValue(AmbulanceuserPhoneNumberR);
                        mDatabase.child("pass").setValue(AmbulanceuserPassR);
                        mDatabase.child("group").setValue("ambulance");
                        mDatabase.child("optionalno").setValue("0000000000");
                        mDatabase.child("permanentaddress").setValue("India");
                        mDatabase.child("ambulanceNo").setValue(AmbulanceNo);

                        DatabaseReference md = FirebaseDatabase.getInstance().getReference().child("ambulanceNo").child(AmbulanceNo);
                        md.setValue(uid);

                        Intent intent = new Intent(AmbulanceSignupActivity.this,AmbulanceLandingActivity.class);
                        mProgressDialog.dismiss();
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
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
