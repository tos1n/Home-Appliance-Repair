package com.example.ffive.seg2105ffiveproject;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class SignUpActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    Button buttonSignUp1;
    EditText editTextUsername;
    EditText editTextPassword;

    String username;
    String role;

    FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        Spinner dropdownRole = findViewById(R.id.dropdownRole);
        String[] items = new String[]{"Role", "Home Owner", "Service Provider"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        dropdownRole.setAdapter(adapter);
        dropdownRole.setOnItemSelectedListener(this);

        editTextUsername = (EditText) findViewById(R.id.editTextUsername);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);
        buttonSignUp1 = (Button) findViewById(R.id.buttonSignUp);

        buttonSignUp1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signUp();
            }
        });
    }

    public void signUp(){
        String username = editTextUsername.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        database = FirebaseDatabase.getInstance();

        if(role.equals("Role") || username.equals("")|| password.equals("")){
            Toast.makeText(getApplicationContext(), "Please enter a username and password and select a role", Toast.LENGTH_LONG).show();
        }
        else if(username.equals("admin")){
            Toast.makeText(getApplicationContext(), "You may not create an account with username admin", Toast.LENGTH_LONG).show();
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
            final DatabaseReference serviceRef = dbref.child("Account").child("ServiceProvider").child(username);
            homeRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.exists()){
                        Toast.makeText(getApplicationContext(), "This username is taken", Toast.LENGTH_LONG).show();
                    }
                    else{
                        database = FirebaseDatabase.getInstance();
                        serviceRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if(dataSnapshot.exists()){
                                    Toast.makeText(getApplicationContext(), "This username is taken", Toast.LENGTH_LONG).show();
                                }
                                else{
                                    Account newAcc;
                                    database = FirebaseDatabase.getInstance();
                                    DatabaseReference dbref = FirebaseDatabase.getInstance().getReference();
                                    String username = editTextUsername.getText().toString().trim();
                                    String password = editTextPassword.getText().toString().trim();
                                    if(role.equals("Home Owner")){
                                        newAcc = new HomeOwner(username, password);
                                        DatabaseReference homeRef = dbref.child("Account").child("HomeOwner");
                                        dbref.child("Account").child("HomeOwner").child(username).setValue(newAcc);
                                    }
                                    else if(role.equals("Service Provider")){
                                        newAcc = new ServiceProvider(username, password);
                                        DatabaseReference homeRef = dbref.child("Account").child("ServiceProvider");
                                        dbref.child("Account").child("ServiceProvider").child(username).setValue(newAcc);
                                        dbref.child("Account").child("ServiceProvider").child(username).child("availabilities").setValue(((ServiceProvider) newAcc).availabilities);
                                    }
                                    logInPage();
                                }
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                            }
                        });
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {}
            });
        }
    }

    public void logInPage(){
        Intent intent = new Intent(this, LogInActivity.class);
        startActivity(intent);
    }

    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
        // An item was selected. You can retrieve the selected item using
        // parent.getItemAtPosition(pos)
        role = parent.getItemAtPosition(pos).toString();
    }

    public void onNothingSelected(AdapterView<?> parent) {}
}
