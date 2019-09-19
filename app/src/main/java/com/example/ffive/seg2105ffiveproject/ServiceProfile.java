package com.example.ffive.seg2105ffiveproject;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ServiceProfile extends AppCompatActivity {

    Button buttonBack;
    Button buttonSave;
    EditText editTextAddress;
    EditText editTextPhone;
    EditText editTextDescription;
    EditText editTextCompany;
    RadioGroup radioGroupLicense;
    RadioButton radioButtonLicense;
    RadioButton radioButtonYes;
    RadioButton radioButtonNo;
    FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_profile);

        radioGroupLicense = (RadioGroup) findViewById(R.id.radioGroupLicense);
        radioButtonYes = (RadioButton) findViewById(R.id.radioButtonYes);
        radioButtonNo = (RadioButton) findViewById(R.id.radioButtonNo);
        editTextAddress = (EditText) findViewById(R.id.editTextServiceAddress);
        editTextPhone = (EditText) findViewById(R.id.editTextServicePhone);
        editTextDescription = (EditText) findViewById(R.id.editTextServiceDescription);
        editTextCompany = (EditText) findViewById(R.id.editTextCompanyName);
        buttonBack = (Button) findViewById(R.id.buttonServiceProfileBack);
        buttonSave = (Button) findViewById(R.id.buttonProfileSSave);
        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                save();
            }
        });
        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                back();
            }
        });

        Bundle extras = getIntent().getExtras();
        String username = extras.getString("username");

        database = FirebaseDatabase.getInstance();
        DatabaseReference dbref = FirebaseDatabase.getInstance().getReference();
        final DatabaseReference serviceUserRef = dbref.child("Account").child("ServiceProvider").child(username);
        serviceUserRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.child("address").exists()){
                    String address = dataSnapshot.child("address").getValue().toString();
                    editTextAddress.setText(address);
                }
                if(dataSnapshot.child("phoneNumber").exists()){
                    String phoneNumber = dataSnapshot.child("phoneNumber").getValue().toString();
                    editTextPhone.setText(phoneNumber);
                }
                if(dataSnapshot.child("companyName").exists()){
                    String companyName = dataSnapshot.child("companyName").getValue().toString();
                    editTextCompany.setText(companyName);
                }

                if(dataSnapshot.child("description").exists() ){
                    String description = dataSnapshot.child("description").getValue().toString();
                    if(!description.equals("")){
                        editTextDescription.setText(description);
                    }
                }
                if(dataSnapshot.child("licensed").exists()){
                    if(dataSnapshot.child("licensed").getValue().equals("Yes")){
                        radioButtonYes.setChecked(true);
                    }
                    else if(dataSnapshot.child("licensed").getValue().equals("No")){
                        radioButtonNo.setChecked(true);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void save(){
        Bundle extras = getIntent().getExtras();
        String username = extras.getString("username");

        String address = editTextAddress.getText().toString().trim();
        String phoneNumber = editTextPhone.getText().toString().trim();
        String companyName = editTextCompany.getText().toString().trim();
        String description = editTextDescription.getText().toString().trim();

        if(address.equals("") || phoneNumber.equals("") || companyName.equals("")){
            Toast.makeText(getApplicationContext(), "Address, phone number, and company name are mandatory", Toast.LENGTH_LONG).show();
        }
        else if (address.matches("[0-9]+")) {
            Toast.makeText(getApplicationContext(), "The address may not be only numbers", Toast.LENGTH_LONG).show();
        }
        else if(!isNumeric(phoneNumber)){
            Toast.makeText(getApplicationContext(), "The phone number must only consist of numbers", Toast.LENGTH_LONG).show();
        }
        else{
            database = FirebaseDatabase.getInstance();
            DatabaseReference dbref = FirebaseDatabase.getInstance().getReference();
            final DatabaseReference serviceUserRef = dbref.child("Account").child("ServiceProvider").child(username);
            serviceUserRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    String address = editTextAddress.getText().toString().trim();
                    String phoneNumber = editTextPhone.getText().toString().trim();
                    String companyName = editTextCompany.getText().toString().trim();
                    String description = editTextDescription.getText().toString().trim();
                    // get selected radio button from radioGroup
                    int selectedId = radioGroupLicense.getCheckedRadioButtonId();
                    // find the radiobutton by returned id
                    String radioButtonText;
                    if(selectedId != -1){
                        radioButtonLicense = (RadioButton) findViewById(selectedId);
                        radioButtonText = radioButtonLicense.getText().toString();
                    }
                    else{
                        radioButtonText = "";
                    }
                    serviceUserRef.child("address").setValue(address);
                    serviceUserRef.child("phoneNumber").setValue(phoneNumber);
                    serviceUserRef.child("companyName").setValue(companyName);
                    serviceUserRef.child("description").setValue(description);
                    serviceUserRef.child("licensed").setValue(radioButtonText);
                    Toast.makeText(getApplicationContext(), "Your profile has been updated", Toast.LENGTH_LONG).show();
//                    if(address.equals("") || phoneNumber.equals("") || companyName.equals("")){
//                        Toast.makeText(getApplicationContext(), "Address, phone number, and company name are mandatory", Toast.LENGTH_LONG).show();
//                    }
//                    else if (address.matches("[0-9]+")) {
//                        Toast.makeText(getApplicationContext(), "The address may not be only numbers", Toast.LENGTH_LONG).show();
//                    }
//                    else if(!isNumeric(phoneNumber)){
//                        Toast.makeText(getApplicationContext(), "The phone number must only consist of numbers", Toast.LENGTH_LONG).show();
//                    }
//                    else{
//                        database = FirebaseDatabase.getInstance();
//                        DatabaseReference dbref = FirebaseDatabase.getInstance().getReference();
//                        final DatabaseReference serviceNRef = dbref.child("Account").child(username);
//                        serviceNRef.addListenerForSingleValueEvent(new ValueEventListener() {
//                            @Override
//                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                                Bundle extras = getIntent().getExtras();
//                                String serviceName = extras.getString("serviceName").trim();
//                                String newServiceName = editTextServiceName.getText().toString().trim();
//                                Toast.makeText(getApplicationContext(), "T"+serviceName, Toast.LENGTH_LONG).show();
//                                if(dataSnapshot.exists() && !serviceName.equals(newServiceName)){
////                                Toast.makeText(getApplicationContext(), "This service already exists", Toast.LENGTH_LONG).show();
//                                }
//                                else{
////                                String newServiceName = editTextServiceName.getText().toString().trim();
//                                    String newRateString = editTextRate.getText().toString().trim();
//                                    double newRate = Double.parseDouble(newRateString);
//                                    Service updatedService = new Service(newServiceName, newRate);
//                                    database = FirebaseDatabase.getInstance();
//                                    DatabaseReference dbref = FirebaseDatabase.getInstance().getReference();
//                                    serviceName = serviceName.substring(1,serviceName.length());
//                                    DatabaseReference ServiceNRef = dbref.child("Service");
//                                    dbref.child("Service").child(serviceName).removeValue();
//                                    dbref.child("Service").child(newServiceName).setValue(updatedService);
//                                    endUpdate();
//                                }
//                            }
//                            @Override
//                            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                            }
//                        });
//                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }
            });
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

    private void back(){
        Bundle extras = getIntent().getExtras();
        String username = extras.getString("username");
        Intent intent = new Intent(getApplicationContext(), WelcomeServiceProvider.class);
        intent.putExtra("username",username);
        intent.putExtra("role", "ServiceProvider");
        startActivityForResult(intent, 0);
    }
}
