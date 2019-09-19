package com.example.ffive.seg2105ffiveproject;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class WelcomeActivity extends AppCompatActivity {

    TextView welcomeTextView;
    TextView accountsTextView;
    TextView homeOwnersTextView;
    TextView serviceProvidersTextView;
    Button buttonLogOut;
    FirebaseDatabase database;

    private void logOut(View view){
        //takes the user to the sign up page
        Intent intent = new Intent(getApplicationContext(), LogInActivity.class);
        startActivityForResult(intent, 0);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        buttonLogOut = (Button) findViewById(R.id.buttonLogOut);
        buttonLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logOut(view);
            }
        });

        welcomeTextView = (TextView)findViewById(R.id.welcomeTextView);


        Bundle extras = getIntent().getExtras();
        String role = extras.getString("role");
        String username = extras.getString("username");


        database = FirebaseDatabase.getInstance();
        DatabaseReference dbref = FirebaseDatabase.getInstance().getReference();
        final DatabaseReference usernameRef = dbref.child(username);
        final DatabaseReference theRef = dbref.child("Account").child(role).child(username);

        theRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String rolePrint = "";
                welcomeTextView = (TextView)findViewById(R.id.welcomeTextView);
                if(dataSnapshot.child("role").getValue().equals("ServiceProvider")){
                    rolePrint = " Service Provider";
                }
                else if(dataSnapshot.child("role").getValue().equals("HomeOwner")){
                    rolePrint = " Home Owner";
                }
                welcomeTextView.setText("Welcome " + dataSnapshot.child("username").getValue() + "! You are logged in as a" + rolePrint + ".");
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }
}
