package com.example.ffive.seg2105ffiveproject;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.view.View;

import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LogInActivity extends AppCompatActivity {

    Button buttonLogIn;
    Button buttonSignUp;
    EditText editTextUsername;
    EditText editTextPassword;

    FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        editTextUsername = (EditText) findViewById(R.id.editTextUsername);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);
        buttonLogIn = (Button) findViewById(R.id.buttonLogIn);
        buttonSignUp = (Button) findViewById(R.id.buttonSignUp);
        createAdminAcc();

        //adding an onclicklistener to log in button
        buttonLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                verifyCreds();
            }
        });
        //adding an onclicklistener to sign up button
        buttonSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signUp(view);
            }
        });
    }

    private void verifyCreds(){//implement
        //if username is admin, checks if the password is admin and logs into admin account
        //otherwise checks if username is in firebase, if so checks if password is correct
        //then logs into welcome page or says username doesn't exist or wrong password
        String username = editTextUsername.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        if(username.equals("") || password.equals("")){
            Toast.makeText(getApplicationContext(), "Please enter a username and password", Toast.LENGTH_LONG).show();
        }
        else if(username.equals("admin")){
            if(password.equals("admin")){
                logInAdmin();
            }
            else{
                Toast.makeText(getApplicationContext(), "Incorrect password", Toast.LENGTH_LONG).show();
            }
        }
        else if (username.matches("[0-9]+")) {
            Toast.makeText(getApplicationContext(), "Your username may not be only numbers", Toast.LENGTH_LONG).show();
        }
        else if(username.length() < 3){
            Toast.makeText(getApplicationContext(), "Your username must contain at least 3 characters", Toast.LENGTH_LONG).show();
        }
        else{
            database = FirebaseDatabase.getInstance();
            DatabaseReference dbref = FirebaseDatabase.getInstance().getReference();
            final DatabaseReference homeRef = dbref.child("Account").child("HomeOwner").child(username);
            final DatabaseReference passHomeRef = homeRef.child("password");
            homeRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.exists()){
                        database = FirebaseDatabase.getInstance();
                        passHomeRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if(dataSnapshot.getValue().equals(editTextPassword.getText().toString().trim())){
                                    logIn(editTextUsername.getText().toString().trim(), "HomeOwner");
                                }
                                else{
                                    Toast.makeText(getApplicationContext(), "Incorrect password", Toast.LENGTH_LONG).show();
                                }
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                            }
                        });
                    }
                    else{
                        verifyCreds2();
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {}
            });
        }
    }

    public void verifyCreds2(){
        String username = editTextUsername.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        database = FirebaseDatabase.getInstance();
        DatabaseReference dbref = FirebaseDatabase.getInstance().getReference();
        final DatabaseReference serviceRef = dbref.child("Account").child("ServiceProvider").child(username);
        final DatabaseReference passServRef = serviceRef.child("password");

        serviceRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(dataSnapshot.exists()){
                    database = FirebaseDatabase.getInstance();
                    passServRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if(dataSnapshot.getValue().equals(editTextPassword.getText().toString().trim())){
                                logIn(editTextUsername.getText().toString().trim(), "ServiceProvider");
                            }
                            else{
                                Toast.makeText(getApplicationContext(), "Incorrect password", Toast.LENGTH_LONG).show();
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                        }
                    });
                }
                else{
                    Toast.makeText(getApplicationContext(), "Username does not exist", Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        });
    }

    public void logIn(String username, String role){
        if(role.equals("HomeOwner")){
            Intent intent = new Intent(this, WelcomeHomeOwner.class);
            intent.putExtra("username",username);
            intent.putExtra("role", role);
            startActivity(intent);
        }
        else{
            Toast.makeText(getApplicationContext(), "Service Loggin in", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(this, WelcomeServiceProvider.class);
            intent.putExtra("username",username);
            intent.putExtra("role", role);
            startActivity(intent);
        }
    }

    public void logInAdmin(){
        Intent intent = new Intent(this, WelcomeAdminActivity.class);
        startActivity(intent);
    }

    private void createAdminAcc(){
        database = FirebaseDatabase.getInstance();
        DatabaseReference dbref = FirebaseDatabase.getInstance().getReference();
        if(Admin.isExisting() == false){
            Account admin = new Admin();
            final DatabaseReference serviceRef = dbref.child("Account").child("Admin");
            serviceRef.setValue(admin);
        }
    }

    private void signUp(View view){
        //takes the user to the sign up page
        Intent intent = new Intent(getApplicationContext(), SignUpActivity.class);
        startActivityForResult(intent, 0);
    }
}
