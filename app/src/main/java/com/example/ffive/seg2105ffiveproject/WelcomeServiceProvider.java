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

public class WelcomeServiceProvider extends AppCompatActivity {

    TextView welcomeServiceTextView;
    TextView accountsTextView;
    TextView serviceProvidersTextViewS;
    Button buttonServiceLogOut;
    Button buttonAvailabilities;
    Button buttonEditProfile;
    Button buttonGoToServices;
    FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_service_provider);

        Bundle extras = getIntent().getExtras();
        String role = extras.getString("role");
        String username = extras.getString("username");

        buttonServiceLogOut = (Button) findViewById(R.id.buttonLogOutService);
        buttonAvailabilities = (Button) findViewById(R.id.buttonAvailabilities);
        buttonEditProfile = (Button) findViewById(R.id.buttonEditProfile);
        buttonGoToServices = (Button) findViewById(R.id.buttonGoToServices);
        buttonServiceLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logOut(view);
            }
        });
        buttonAvailabilities.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAvailabilities();
            }
        });
        buttonEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editProfile();
            }
        });
        buttonGoToServices.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToServices();
            }
        });



        welcomeServiceTextView = (TextView)findViewById(R.id.welcomeServiceTextView);


        database = FirebaseDatabase.getInstance();
        DatabaseReference dbref = FirebaseDatabase.getInstance().getReference();
//        final DatabaseReference usernameRef = dbref.child(username);
        final DatabaseReference theRef = dbref.child("Account").child("ServiceProvider").child(username);

        theRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String rolePrint = "";
                welcomeServiceTextView = (TextView)findViewById(R.id.welcomeServiceTextView);
                rolePrint = " Service Provider";
                welcomeServiceTextView.setText("Welcome " + dataSnapshot.child("username").getValue() + "! You are logged in as a" + rolePrint + ".");
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    private void showAvailabilities(){
        Bundle extras = getIntent().getExtras();
        String username = extras.getString("username");
        Intent intent = new Intent(getApplicationContext(), ProviderAvailabilities.class);
        intent.putExtra("username",username);
        startActivityForResult(intent, 0);
    }

    private void editProfile(){
        Bundle extras = getIntent().getExtras();
        String username = extras.getString("username");
        Intent intent = new Intent(getApplicationContext(), ServiceProfile.class);
        intent.putExtra("username",username);
        startActivityForResult(intent, 0);
    }

    private void goToServices(){
        Bundle extras = getIntent().getExtras();
        String username = extras.getString("username");
        Intent intent = new Intent(getApplicationContext(), ServicesListProvider.class);
        intent.putExtra("username",username);
        startActivityForResult(intent, 0);
    }

    private void logOut(View view){
        //takes the user to the sign up page
        Intent intent = new Intent(getApplicationContext(), LogInActivity.class);
        startActivityForResult(intent, 0);
    }
}
