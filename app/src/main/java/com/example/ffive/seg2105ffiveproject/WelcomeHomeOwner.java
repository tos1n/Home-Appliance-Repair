package com.example.ffive.seg2105ffiveproject;

import android.app.Dialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class WelcomeHomeOwner extends AppCompatActivity {

    TextView welcomeHomeTextView;
    EditText editTextSearchBar;
    Button buttonHomeLogOut;
    Button buttonSearch;
    FirebaseDatabase database;
    Spinner dropdownSearchBy;

    ArrayList<String> services;
    ArrayList<String> timeslots;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_home_owner);

        Bundle extras = getIntent().getExtras();
        String username;
        if (extras != null) {
             username = extras.getString("username");

        } else {
            username = "owner";
        }

        dropdownSearchBy = findViewById(R.id.dropdownSearchBy);
        buttonHomeLogOut = (Button) findViewById(R.id.buttonLogOutHome);
        buttonSearch = (Button) findViewById(R.id.buttonSearch);
        editTextSearchBar = (EditText) findViewById(R.id.editTextSearchBar);
        buttonSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                search();
            }
        });
        buttonHomeLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logOut(view);
            }
        });

        List<String> items = new ArrayList<String>();
        items.add("Service type");
        items.add("Time availability (eg. 09:30 to 15:45 Monday)");
        items.add("Rating from 1-5 (eg. 4.5)");
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        dropdownSearchBy.setAdapter(adapter);


        // Booking Functionality:
        ListView tmpLV = (ListView) findViewById(R.id.lvSearch);
        tmpLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                createBooking(parent, position);
            }
        });

//        service = new ArrayList<>();
//        timeslots = new ArrayList<>();
//        final String serviceProvider = String.valueOf(parent.getItemAtPosition(position));

        database = FirebaseDatabase.getInstance();
        DatabaseReference dbref = FirebaseDatabase.getInstance().getReference();
        final DatabaseReference usernameRef = dbref.child(username);
        final DatabaseReference theRef = dbref.child("Account").child("HomeOwner").child(username);
//        final DatabaseReference svProviderRef = dbref.child(serviceProvider).child("services");
//        final DatabaseReference timeslotsRef = dbref.child(serviceProvider).child("availabilities");

//         svProviderRef.addListenerForSingleValueEvent(new ValueEventListener() {
//             @Override
//             public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                 for (DataSnapshot childSnapshot: dataSnapshot.getChildren()) {

//                 }

//             }
//             @Override
//             public void onCancelled(@NonNull DatabaseError databaseError) {
//             }
//         });

        theRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String rolePrint = "";
                welcomeHomeTextView = (TextView)findViewById(R.id.welcomeHomeTextView);
                rolePrint = " Home Owner";
                welcomeHomeTextView.setText("Welcome " + dataSnapshot.child("username").getValue() + "! You are logged in as a" + rolePrint + ".");
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    private void search(){
        String text = dropdownSearchBy.getSelectedItem().toString();
        String searchInput = editTextSearchBar.getText().toString().trim();
        if(text.equals("Service type")){
            searchServiceType(searchInput);
        }
        else if(text.equals("Time availability (eg. 09:30 to 15:45 Monday)")){
            searchTime(searchInput);
        }
        else if(text.equals("Rating from 1-5 (eg. 4.5)")){
            searchRating(searchInput);
        }
    }

    private void searchServiceType(String serviceType){
        Toast.makeText(getApplicationContext(), serviceType, Toast.LENGTH_LONG).show();
        if(serviceType.equals("")){
            Toast.makeText(getApplicationContext(), "Please enter a service type to search", Toast.LENGTH_LONG).show();
        }
        else if (isNumeric(serviceType)) {
            Toast.makeText(getApplicationContext(), "The service may not be only numbers", Toast.LENGTH_LONG).show();
        }
        else{
            Toast.makeText(getApplicationContext(), "Searching", Toast.LENGTH_LONG).show();
            database = FirebaseDatabase.getInstance();
            DatabaseReference dbref = FirebaseDatabase.getInstance().getReference();
            final DatabaseReference serviceNRef = dbref.child("Service").child(serviceType);
            final DatabaseReference serviceProvidersRef = dbref.child("Account").child("ServiceProvider");
            serviceNRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(!dataSnapshot.exists()){
                        ListView lv = (ListView)findViewById(R.id.lvSearch);
                        String serviceProvidersList = "";
                        String [] servicesListArray = serviceProvidersList.split(",");
                        ArrayAdapter<String> adapter =  new ArrayAdapter<String>(WelcomeHomeOwner.this, android.R.layout.simple_list_item_1,servicesListArray);
                        lv.setAdapter(adapter);
                        Toast.makeText(getApplicationContext(), "Service does not exist", Toast.LENGTH_LONG).show();
                    }
                    else{
                        database = FirebaseDatabase.getInstance();
                        serviceProvidersRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                ListView lv = (ListView)findViewById(R.id.lvSearch);
                                String serviceProvidersList = "";
                                String searchInput = editTextSearchBar.getText().toString().trim();
                                boolean found = false;
                                for (DataSnapshot snapshot: dataSnapshot.getChildren()) {
                                    if(snapshot.hasChild("services")){
                                        if(snapshot.child("services").getValue().toString().contains(searchInput)){
                                            serviceProvidersList += (snapshot.child("username").getValue().toString() + ", ");
                                            found = true;
                                        }
                                    }
                                }
                                if(found == false){
                                    Toast.makeText(getApplicationContext(), "No results found" , Toast.LENGTH_LONG).show();
                                }
                                String [] servicesListArray = serviceProvidersList.split(",");

                                ArrayAdapter<String> adapter =  new ArrayAdapter<String>(WelcomeHomeOwner.this, android.R.layout.simple_list_item_1,servicesListArray);

                                lv.setAdapter(adapter);
                                if(!serviceProvidersList.equals("")){
                                    serviceProvidersList = "Service Providers: " + serviceProvidersList.substring(0, serviceProvidersList.length()-2);
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
    }

    private void searchTime(String time){
        String[] days = {"Monday","Tuesday","Wednesday","Thursday", "Friday", "Saturday", "Sunday"};

        if(!(time.length() >= 14) || !time.substring(5,9).equals(" to ") || !(time.charAt(2)==':') || !(time.charAt(11)==':')){
            Toast.makeText(getApplicationContext(), "Please follow the format in the example", Toast.LENGTH_LONG).show();
        }
        else if(!isValidTime(time) || Double.parseDouble(time.substring(0,2)) > Double.parseDouble(time.substring(9,11))){
            Toast.makeText(getApplicationContext(), "Please enter valid times", Toast.LENGTH_LONG).show();
        }
        else if(Double.parseDouble(time.substring(0,2)) == Double.parseDouble(time.substring(9,11)) && Double.parseDouble(time.substring(3,5)) > Double.parseDouble(time.substring(12,14))){
            Toast.makeText(getApplicationContext(), "Please enter valid times", Toast.LENGTH_LONG).show();
        }
        else if(time.length() < 15 || !(Arrays.asList(days).contains(time.substring(15, time.length())))){
            Toast.makeText(getApplicationContext(), "Please specify a day of the week", Toast.LENGTH_LONG).show();
        }
        else{
            //search for appropriate service providers
            Toast.makeText(getApplicationContext(), "Searching", Toast.LENGTH_LONG).show();
            database = FirebaseDatabase.getInstance();
            DatabaseReference dbref = FirebaseDatabase.getInstance().getReference();
            final DatabaseReference serviceProvidersRef = dbref.child("Account").child("ServiceProvider");
            serviceProvidersRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    ListView lv = (ListView)findViewById(R.id.lvSearch);
                    String serviceProvidersList = "";
                    String searchInput = editTextSearchBar.getText().toString().trim();
                    String day = searchInput.substring(15,searchInput.length());
                    String availability;
                    boolean found = false;
                    for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                        availability = snapshot.child("availabilities").child(day).getValue().toString();
                        if(availability.equals("Not Available")){
                            continue;
                        }
                        else if(Double.parseDouble(searchInput.substring(0,2)) < Double.parseDouble(availability.substring(0,2)) || Double.parseDouble(searchInput.substring(0,2)) > Double.parseDouble(availability.substring(9,11))){
                            continue;
                        }
                        else if(Double.parseDouble(searchInput.substring(0,2)) == Double.parseDouble(availability.substring(0,2)) && Double.parseDouble(searchInput.substring(3,5)) < Double.parseDouble(availability.substring(3,5))){
                            continue;
                        }
                        else if(Double.parseDouble(searchInput.substring(9,11)) == Double.parseDouble(availability.substring(9,11)) && Double.parseDouble(searchInput.substring(12,14)) > Double.parseDouble(availability.substring(12,14))){
                            continue;
                        }
                        else{
                            serviceProvidersList += (snapshot.child("username").getValue().toString() + ", ");
                            found = true;
                        }
                    }
                    if(found == false){
                        Toast.makeText(getApplicationContext(), "No results found" , Toast.LENGTH_LONG).show();
                    }
                    String [] servicesListArray = serviceProvidersList.split(",");

                    ArrayAdapter<String> adapter =  new ArrayAdapter<String>(WelcomeHomeOwner.this, android.R.layout.simple_list_item_1,servicesListArray);

                    lv.setAdapter(adapter);
                    if(!serviceProvidersList.equals("")){
                        serviceProvidersList = "Service Providers: " + serviceProvidersList.substring(0, serviceProvidersList.length()-2);
                        Toast.makeText(getApplicationContext(), "No results found" , Toast.LENGTH_LONG).show();
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }
            });
        }
    }

    private void searchRating(String rating){
        if(!isNumeric(rating)){
            Toast.makeText(getApplicationContext(), "Please enter only a number for the rating", Toast.LENGTH_LONG).show();
        }
        else if(Double.parseDouble(rating) > 5 || Double.parseDouble(rating) < 1){
            Toast.makeText(getApplicationContext(), "Ratings can only be from 1 and 5", Toast.LENGTH_LONG).show();
        }
        else{
            //search for appropriate service providers
            Toast.makeText(getApplicationContext(), "Searching", Toast.LENGTH_LONG).show();
            database = FirebaseDatabase.getInstance();
            DatabaseReference dbref = FirebaseDatabase.getInstance().getReference();
            final DatabaseReference serviceProvidersRef = dbref.child("Account").child("ServiceProvider");
            serviceProvidersRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    ListView lv = (ListView)findViewById(R.id.lvSearch);
                    String serviceProvidersList = "";
                    String searchInput = editTextSearchBar.getText().toString().trim();
                    String availability;
                    double searchNum = Double.parseDouble(searchInput);
                    boolean found = false;
                    for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                        if(snapshot.hasChild("overallRating")){
                            double num = Double.parseDouble(snapshot.child("overallRating").getValue().toString());
//                            if(snapshot.child("overallRating").getValue().toString().equals(searchInput)){
//                                serviceProvidersList += (snapshot.child("username").getValue().toString() + ", ");
//                            }
                            if(num >= searchNum){
                                serviceProvidersList += (snapshot.child("username").getValue().toString() + ", ");
                                found = true;
                            }
                        }
                    }
                    if(found == false){
                        Toast.makeText(getApplicationContext(), "No results found" , Toast.LENGTH_LONG).show();
                    }
                    String [] servicesListArray = serviceProvidersList.split(",");

                    ArrayAdapter<String> adapter =  new ArrayAdapter<String>(WelcomeHomeOwner.this, android.R.layout.simple_list_item_1,servicesListArray);

                    lv.setAdapter(adapter);
                    if(!serviceProvidersList.equals("")){
                        serviceProvidersList = "Service Providers: " + serviceProvidersList.substring(0, serviceProvidersList.length()-2);
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }
            });
        }
    }

    private void logOut(View view){
        //takes the user to the sign up page
        Intent intent = new Intent(getApplicationContext(), LogInActivity.class);
        startActivityForResult(intent, 0);
    }

    private boolean isNumeric(String str) {
        try {
            double d = Double.parseDouble(str);
        }
        catch(NumberFormatException nfe) {
            return false;
        }
        return true;
    }

    private boolean isValidTime(String time){
        if(!(isNumeric(time.substring(0,2))) || !(isNumeric(time.substring(3,5))) || !(isNumeric(time.substring(9,11)))|| !(isNumeric(time.substring(12,14)))){
            return false;
        }
        else if(Double.parseDouble(time.substring(0,2))>23 || Double.parseDouble(time.substring(3,5))>59 || Double.parseDouble(time.substring(9,11))>23 || Double.parseDouble(time.substring(12,14))>59){
            return false;
        }
        return true;
    }


    // Booking Functionality

    // Generates and controls a dialog box through which a user can book and rate a service provider
    private void createBooking(AdapterView<?> parent, int position) {

        // Obtain data from Firebase
        services = new ArrayList<>();
            services.add("Choose Service");

        timeslots = new ArrayList<>();
            timeslots.add("Choose Timeslot");

        final String serviceProvider = String.valueOf(parent.getItemAtPosition(position));

        database = FirebaseDatabase.getInstance();
         DatabaseReference dbref = FirebaseDatabase.getInstance().getReference();
         final DatabaseReference svProviderRef = dbref.child("Account").child("ServiceProvider").child(serviceProvider).child("services");
         final DatabaseReference timeslotsRef = dbref.child("Account").child("ServiceProvider").child(serviceProvider).child("availabilities");

        svProviderRef.addListenerForSingleValueEvent(new ValueEventListener() {
             @Override
             public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                 for (DataSnapshot childSnapshot: dataSnapshot.getChildren()) {
                     String value = childSnapshot.getValue().toString();
                     services.add(value);
                 }
             }
             @Override
             public void onCancelled(@NonNull DatabaseError databaseError) {
             }
         });

        timeslotsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot childSnapshot: dataSnapshot.getChildren()) {
                    String value = childSnapshot.getValue().toString();
                    if (!value.equals("Not Available")) {
                        timeslots.add(value + " " + childSnapshot.getKey());
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

        // Generate booking dialog
        final Dialog d = new Dialog(WelcomeHomeOwner.this);
        d.setTitle("Booking");
        d.setContentView(R.layout.activity_book_service_provider);
        d.show();

        // Retrieve dialog elements
        Spinner spnServices = d.findViewById(R.id.spnServices);
        Spinner spnAvailabilities = d.findViewById(R.id.spnAvailabilities);
        Button btnBook = d.findViewById(R.id.btnBook);
        Button btnCancel = d.findViewById(R.id.btnCancel);

        //  Populate dialog content
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, services);
        spnServices.setAdapter(adapter);
        ArrayAdapter<String> adapter2 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, timeslots);
        spnAvailabilities.setAdapter(adapter2);

        // Deal with booking operation
        btnBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Create confirmation message
                String message = "You have successfully booked " + serviceProvider + "!";
                d.cancel();
                Toast.makeText(getApplicationContext(), message , Toast.LENGTH_LONG).show();
                createRating(serviceProvider);
            }
        });

        // Deal with cancel operation
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                d.cancel();
            }
        });

    }

    private void createRating(String sp) {

        final String serviceProvider = sp;

        // Generate Rating dialog
        final Dialog d = new Dialog(WelcomeHomeOwner.this);
        d.setTitle("Rating");
        d.setContentView(R.layout.activity_rate_service_provider);
        d.show();

        // Retrieve dialog elements
        EditText edtComments = d.findViewById(R.id.edtComments);
        RatingBar ratingBar = d.findViewById(R.id.ratingBar);
        Button btnRate = d.findViewById(R.id.btnRate);
        Button btnNoRate = d.findViewById(R.id.btnNoRate);

        // Deal with rating operation
        btnRate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Create confirmation message
                String message = "You have rated " + serviceProvider + "!";
                d.cancel();
                Toast.makeText(getApplicationContext(), message , Toast.LENGTH_LONG).show();
            }
        });

        // Deal with cancel operation
        btnNoRate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                d.cancel();
            }
        });


    }
}
