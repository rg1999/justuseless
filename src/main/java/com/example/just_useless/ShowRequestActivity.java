package com.example.just_useless;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.ValueEventListener;


public class ShowRequestActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_request);

        mAuth = FirebaseAuth.getInstance();

        uid = mAuth.getCurrentUser().getUid().toString();

        mDatabase = FirebaseDatabase.getInstance().getReference().child("req").child(uid).child("list");

        mRecyclerView = (RecyclerView) findViewById(R.id.req_list);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerOptions options =
                new FirebaseRecyclerOptions.Builder<list>()
                        .setQuery(mDatabase,list.class)
                        .build();

        FirebaseRecyclerAdapter<list,BloodBankViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<list,BloodBankViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final BloodBankViewHolder bloodBankViewHolder, final int i, @NonNull final list list) {

                String uuiidd = list.getNeeder().toString();

                DatabaseReference ee = FirebaseDatabase.getInstance().getReference().child("users").child(uuiidd);

                ee.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        String we = dataSnapshot.child("name").getValue().toString();
                        String wee = dataSnapshot.child("phone").getValue().toString();

                        bloodBankViewHolder.setName(we,wee);

                        //final String user_id = getRef(i).getKey();

                        bloodBankViewHolder.mView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                Intent userProfile = new Intent(ShowRequestActivity.this,LastMapActivity.class);
                                userProfile.putExtra("longi",list.getLatitude().toString());
                                userProfile.putExtra("lati",list.getLongitude().toString());
                                startActivity(userProfile);

                            }
                        });
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });



            }

            @NonNull
            @Override
            public BloodBankViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_user_req, parent, false);

                return new BloodBankViewHolder(view);


            }
        };



        firebaseRecyclerAdapter.startListening();

        mRecyclerView.setAdapter(firebaseRecyclerAdapter);


    }

    public static class BloodBankViewHolder extends RecyclerView.ViewHolder{

        View mView;

        public BloodBankViewHolder(@NonNull View itemView) {
            super(itemView);

            mView = itemView;

        }

        public void setName(String A,String a){

            TextView AA = (TextView) mView.findViewById(R.id.sigle_name);
            AA.setText(A);
            TextView aa = (TextView) mView.findViewById(R.id.single_phone);
            aa.setText(a);


        }

    }


}
