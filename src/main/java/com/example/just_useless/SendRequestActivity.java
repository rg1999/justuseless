package com.example.just_useless;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SendRequestActivity extends AppCompatActivity {

    private Button srchbtn;
    private Button sendReq;
    private TextView name;
    private TextView email;
    private TextView conc;
    private TextView srchfld;
    private String uid;

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_request);

        srchbtn = (Button) findViewById(R.id.getinfo);
        sendReq = (Button) findViewById(R.id.send_pick);
        name = (TextView) findViewById(R.id.driver_name);
        email = (TextView) findViewById(R.id.driver_mail);
        conc = (TextView) findViewById(R.id.driver_no);
        srchfld = (TextView) findViewById(R.id.sechno);



        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("ambulanceNo");

        srchbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String a = srchfld.getText().toString();

                if(!TextUtils.isEmpty(a)){

                    mDatabase.child(a).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            uid = dataSnapshot.getValue().toString();

                            DatabaseReference mddd = FirebaseDatabase.getInstance().getReference().child("users").child(uid);

                            mddd.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                    String aaa = dataSnapshot.child("name").getValue().toString();
                                    String aaaa = dataSnapshot.child("phone").getValue().toString();
                                    String aaaaa = dataSnapshot.child("email").getValue().toString();

                                    name.setText(aaa);
                                    email.setText(aaaa);
                                    conc.setText(aaaaa);

                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {



                                }
                            });

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                }

            }
        });

        sendReq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final DatabaseReference mf = FirebaseDatabase.getInstance().getReference().child("req").child(uid).child("list").push();

                final String uuu = FirebaseAuth.getInstance().getCurrentUser().getUid().toString();

                final DatabaseReference mff = FirebaseDatabase.getInstance().getReference().child("userlocation").child(uuu).child("l");

                mff.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        String w = dataSnapshot.child("0").getValue().toString();
                        String e = dataSnapshot.child("1").getValue().toString();


                        mf.child("longitude").setValue(w);
                        mf.child("latitude").setValue(e);
                        mf.child("needer").setValue(uuu);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }
        });


    }
}
