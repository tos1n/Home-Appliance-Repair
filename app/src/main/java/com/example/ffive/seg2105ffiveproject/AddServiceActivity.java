package com.example.ffive.seg2105ffiveproject;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.math.BigDecimal;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

//import org.apache.commons.lang.math.NumberUtils;
//import static com.google.android.gms.common.util.NumberUtils.*;

public class AddServiceActivity extends AppCompatActivity {

    Button buttonAddService;
    EditText editTextServiceName;
    EditText editTextRate;
    Button buttonBack;

    FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_service);

        editTextServiceName = (EditText) findViewById(R.id.editTextServiceName);
        editTextRate = (EditText) findViewById(R.id.editTextRate);
        buttonAddService = (Button) findViewById(R.id.buttonAdd);
        buttonBack = (Button) findViewById(R.id.buttonBackSAdmin);

        buttonAddService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addService();
            }
        });
        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                back();
            }
        });
    }

    public void addService(){
        String serviceName = editTextServiceName.getText().toString().trim();
        String rate = editTextRate.getText().toString().trim();

        database = FirebaseDatabase.getInstance();

        if(serviceName.equals("") || rate.equals("")){
            Toast.makeText(getApplicationContext(), "Please enter a service name and rate per hour", Toast.LENGTH_LONG).show();
        }
        else if (serviceName.matches("[0-9]+")) {
            Toast.makeText(getApplicationContext(), "The service name may not be only numbers", Toast.LENGTH_LONG).show();
        }
        else if(!isNumeric(rate)){
            Toast.makeText(getApplicationContext(), "Please enter only a number for the rate", Toast.LENGTH_LONG).show();
        }
        else {
            database = FirebaseDatabase.getInstance();
            DatabaseReference dbref = FirebaseDatabase.getInstance().getReference();
            final DatabaseReference serviceNRef = dbref.child("Service").child(serviceName);
            serviceNRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.exists()){
                        Toast.makeText(getApplicationContext(), "This service already exists", Toast.LENGTH_LONG).show();
                    }
                    else{
                        Service newService;
                        database = FirebaseDatabase.getInstance();
                        DatabaseReference dbref = FirebaseDatabase.getInstance().getReference();
                        String serviceName = editTextServiceName.getText().toString().trim();
                        double rate = Double.parseDouble(editTextRate.getText().toString().trim());
                        newService = new Service(serviceName, rate);
                        DatabaseReference ServiceNRef = dbref.child("Service");
                        dbref.child("Service").child(serviceName).setValue(newService);
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
//            endAdd();
            Toast.makeText(getApplicationContext(), "Service has been added", Toast.LENGTH_LONG).show();
            finish();
            startActivity(getIntent());
        }
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

    public void endAdd(){
        Intent intent = new Intent(this, ServiceList.class);
        startActivity(intent);
    }

    public void back(){
        Bundle extras = getIntent().getExtras();
        Intent intent = new Intent(getApplicationContext(), ServiceList.class);
        startActivityForResult(intent, 0);
    }
}
