package com.example.just_useless;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UserProfileActivity extends AppCompatActivity {

    private TextView name;
    private TextView email;
    private TextView concno;
    private TextView optconcno;
    private TextView permanent;
    private Button update;

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private String uid;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        mAuth = FirebaseAuth.getInstance();
        uid = mAuth.getCurrentUser().getUid().toString();

        mDatabase = FirebaseDatabase.getInstance().getReference().child("users").child(uid);

        name = (TextView) findViewById(R.id.oneuser_name);
        email = (TextView) findViewById(R.id.oneuser_email);
        concno = (TextView) findViewById(R.id.oneuser_contact_no);
        optconcno = (TextView) findViewById(R.id.oneuser_opt);
        permanent = (TextView) findViewById(R.id.oneuser_permanent);
        update = (Button) findViewById(R.id.user_update_btn);

        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                String q = dataSnapshot.child("name").getValue().toString();
                String q1 = dataSnapshot.child("email").getValue().toString();
                String q2 = dataSnapshot.child("phone").getValue().toString();
                String q3 = dataSnapshot.child("optionalno").getValue().toString();
                String q4 = dataSnapshot.child("permanentaddress").getValue().toString();

                name.setText(q);
                email.setText(q1);
                concno.setText(q2);
                optconcno.setText(q3);
                permanent.setText(q4);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mDatabase.child("name").setValue(name.getText().toString());
                mDatabase.child("email").setValue(email.getText().toString());
                mDatabase.child("phone").setValue(concno.getText().toString());
                mDatabase.child("optionalno").setValue(optconcno.getText().toString());
                mDatabase.child("permanentaddress").setValue(permanent.getText().toString());

            }
        });



    }
}
