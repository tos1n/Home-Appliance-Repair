package com.example.ffive.seg2105ffiveproject;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class WelcomeAdminActivity extends AppCompatActivity {

    TextView welcomeTextView;
    Button buttonServiceList;
    Button buttonUserList;
    Button buttonLogOutAdmin;

    FirebaseDatabase database;
    //ListView lv = (ListView)findViewById(R.id.lv);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_admin);

        welcomeTextView = (TextView)findViewById(R.id.welcomeTextView);
        buttonLogOutAdmin = (Button) findViewById(R.id.buttonLogOutAdmin);
        buttonServiceList = (Button) findViewById(R.id.buttonServiceList);
        buttonUserList = (Button) findViewById(R.id.buttonUserList);

        buttonServiceList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToServiceList();
            }
        });
        buttonLogOutAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logOut(view);
            }
        });
        buttonUserList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToUserList();
            }
        });

        database = FirebaseDatabase.getInstance();
        DatabaseReference dbref = FirebaseDatabase.getInstance().getReference();
        final DatabaseReference homeOwnersRef = dbref.child("Account").child("HomeOwner");
        final DatabaseReference serviceProvidersRef = dbref.child("Account").child("ServiceProvider");
        final DatabaseReference servicesRef = dbref.child("Service");

        welcomeTextView.setText("Welcome! You are logged in as the Administrator.");
    }


    public void goToServiceList(){
        Intent intent = new Intent(this, ServiceList.class);
        startActivity(intent);
    }

    public void goToUserList(){
        Intent intent = new Intent(this, UserList.class);
        startActivity(intent);
    }

    private void logOut(View view){
        //takes the user to the sign up page
        Toast.makeText(getApplicationContext(), "logging out", Toast.LENGTH_LONG).show();
        Intent intent = new Intent(getApplicationContext(), LogInActivity.class);
        startActivityForResult(intent, 0);
    }
}
