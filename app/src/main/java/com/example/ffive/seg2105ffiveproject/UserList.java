package com.example.ffive.seg2105ffiveproject;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UserList extends AppCompatActivity {

    TextView homeOwnersTextView;
    TextView serviceProvidersTextView;
    Button buttonBack;

    FirebaseDatabase database;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list);
        buttonBack = (Button) findViewById(R.id.buttonBack);
        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Back(view);
            }
        });
        serviceProvidersTextView = (TextView) findViewById(R.id.serviceProvidersTextView);
        homeOwnersTextView = (TextView) findViewById(R.id.homeOwnersTextView);

        database = FirebaseDatabase.getInstance();
        DatabaseReference dbref = FirebaseDatabase.getInstance().getReference();
        final DatabaseReference homeOwnersRef = dbref.child("Account").child("HomeOwner");
        final DatabaseReference serviceProvidersRef = dbref.child("Account").child("ServiceProvider");

        homeOwnersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String homeOwnersList = "";
                for (DataSnapshot snapshot: dataSnapshot.getChildren()) {
                    homeOwnersList += ( snapshot.child("username").getValue().toString() + ", ");
                }
                if(!homeOwnersList.equals("")){
                    homeOwnersList = "Home Owners: " + homeOwnersList.substring(0, homeOwnersList.length()-2);
                }

                homeOwnersTextView.setText(homeOwnersList);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

        serviceProvidersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String serviceProvidersList = "";
                for (DataSnapshot snapshot: dataSnapshot.getChildren()) {
                    serviceProvidersList += (snapshot.child("username").getValue().toString() + ", ");
                }
                if(!serviceProvidersList.equals("")){
                    serviceProvidersList = "Service Providers: " + serviceProvidersList.substring(0, serviceProvidersList.length()-2);
                }
                   serviceProvidersTextView.setText(serviceProvidersList);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }
    private void Back(View view){
        //takes the user to the sign up page
        Toast.makeText(getApplicationContext(), "Going back", Toast.LENGTH_LONG).show();
        Intent intent = new Intent(getApplicationContext(), WelcomeAdminActivity.class);
        startActivityForResult(intent, 0);
    }
}
