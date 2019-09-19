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

public class EditDeleteServiceActivity extends AppCompatActivity {

    Button buttonSave;
    Button buttonDelete;
    EditText editTextServiceName;
    EditText editTextRate;
    String oldServiceName;
    String oldRate;

    FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_delete_service);

        Bundle extras = getIntent().getExtras();
        String serviceName = extras.getString("serviceName").trim();
        String rate = extras.getString("rate");
//        Toast.makeText(getApplicationContext(), "logging out"+ rate, Toast.LENGTH_LONG).show();

        editTextServiceName = (EditText) findViewById(R.id.editTextServiceName2);
        editTextRate = (EditText) findViewById(R.id.editTextRate2);
        buttonSave = (Button) findViewById(R.id.buttonSave);
        buttonDelete = (Button) findViewById(R.id.buttonDelete);

        editTextServiceName.setText(serviceName);
        editTextRate.setText(rate);

        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                save();
            }
        });

        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                delete();
            }
        });
    }

    public void save(){
        Bundle extras = getIntent().getExtras();
        String serviceName = extras.getString("serviceName").trim();
        double rate = extras.getDouble("rate");


        database = FirebaseDatabase.getInstance();
        DatabaseReference dbref = FirebaseDatabase.getInstance().getReference();
        final DatabaseReference serviceNRef = dbref.child("Service").child(serviceName);
        serviceNRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String newServiceName = editTextServiceName.getText().toString().trim();
                String newRateString = editTextRate.getText().toString().trim();
                if(newServiceName.equals("") || newRateString.equals("")){
                    Toast.makeText(getApplicationContext(), "Please enter a service name and rate per hour", Toast.LENGTH_LONG).show();
                }
                else if (newServiceName.matches("[0-9]+")) {
                    Toast.makeText(getApplicationContext(), "The service name may not be only numbers", Toast.LENGTH_LONG).show();
                }
                else if(!isNumeric(newRateString)){
                    Toast.makeText(getApplicationContext(), "Please enter only a number for the rate", Toast.LENGTH_LONG).show();
                }
                else{
                    database = FirebaseDatabase.getInstance();
                    DatabaseReference dbref = FirebaseDatabase.getInstance().getReference();
                    final DatabaseReference serviceNRef = dbref.child("Service").child(newServiceName);
                    serviceNRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            Bundle extras = getIntent().getExtras();
                            String serviceName = extras.getString("serviceName").trim();
                            String newServiceName = editTextServiceName.getText().toString().trim();
                            if(dataSnapshot.exists() && !serviceName.equals(newServiceName)){
                                Toast.makeText(getApplicationContext(), "This service already exists", Toast.LENGTH_LONG).show();
                            }
                            else{
//                                String newServiceName = editTextServiceName.getText().toString().trim();
                                String newRateString = editTextRate.getText().toString().trim();
                                double newRate = Double.parseDouble(newRateString);
                                Service updatedService = new Service(newServiceName, newRate);
                                database = FirebaseDatabase.getInstance();
                                DatabaseReference dbref = FirebaseDatabase.getInstance().getReference();
                                serviceName = serviceName.substring(0,serviceName.length());
                                DatabaseReference ServiceNRef = dbref.child("Service");
                                dbref.child("Service").child(serviceName).removeValue();
                                dbref.child("Service").child(newServiceName).setValue(updatedService);
                                endUpdate();
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    public void delete(){
        Bundle extras = getIntent().getExtras();
        String serviceName = extras.getString("serviceName");
        double rate = extras.getDouble("rate");

        database = FirebaseDatabase.getInstance();
        DatabaseReference dbref = FirebaseDatabase.getInstance().getReference();
        final DatabaseReference serviceNRef = dbref.child("Service").child(serviceName);
        serviceNRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                database = FirebaseDatabase.getInstance();
                DatabaseReference dbref = FirebaseDatabase.getInstance().getReference();
                Bundle extras = getIntent().getExtras();
                String serviceName = extras.getString("serviceName");
                serviceName = serviceName.trim();
                DatabaseReference ServiceNRef = dbref.child("Service");
                dbref.child("Service").child(serviceName).removeValue();
                endUpdate();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    public boolean isNumeric(String str) {
        try {
            double d = Double.parseDouble(str);
        }
        catch(NumberFormatException nfe) {
            return false;
        }
        return true;
    }

    public void endUpdate(){
        Intent intent = new Intent(this, ServiceList.class);
        startActivity(intent);
    }
}
