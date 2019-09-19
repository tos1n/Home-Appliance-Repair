package com.example.ffive.seg2105ffiveproject;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.*;

import java.util.*;

public class ProviderAvailabilities extends AppCompatActivity {

    TextView mondayAvText;
    TextView tuesdayAvText;
    TextView wednesdayAvText;
    TextView thursdayAvText;
    TextView fridayAvText;
    TextView saturdayAvText;
    TextView sundayAvText;
    Button buttonBack;
    Button buttonSave;
    EditText editTextFromTime;
    EditText editTextToTime;
    Spinner spinnerDays;
    CheckBox checkBoxAvailable;

    FirebaseDatabase database;
    DatabaseReference dbref;
    DatabaseReference serviceRef;

    private HashMap<String, String> availabilities;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // Generic Setup
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_provider_availbilities);

        // Initialise UI components
        mondayAvText = (TextView) findViewById(R.id.mondayAvText);
        tuesdayAvText = (TextView) findViewById(R.id.tuesdayAvText);
        wednesdayAvText = (TextView) findViewById(R.id.wednesdayAvText);
        thursdayAvText = (TextView) findViewById(R.id.thursdayAvText);
        fridayAvText = (TextView) findViewById(R.id.fridayAvText);
        saturdayAvText = (TextView) findViewById(R.id.saturdayAvText);
        sundayAvText = (TextView) findViewById(R.id.sundayAvText);

        buttonBack = (Button) findViewById(R.id.buttonAvailabilitiesBack);
        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                back();
            }
        });
        buttonSave = (Button) findViewById(R.id.btnSaveAvailabilities);
        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateAvailabilities();
            }
        });

        editTextFromTime = (EditText) findViewById(R.id.fromTime);
        editTextToTime = (EditText) findViewById(R.id.toTime);

        checkBoxAvailable = (CheckBox)  findViewById(R.id.isAvailable);

        spinnerDays = (Spinner) findViewById(R.id.daySelector);
        ArrayList<String> days = new ArrayList<String>(Arrays.asList("Monday", "Tuesday",
                "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"));
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_dropdown_item, days);
        spinnerDays.setAdapter(adapter);

        // Firebase setup
        Bundle extras = getIntent().getExtras();
        String username = extras.getString("username");

        database = FirebaseDatabase.getInstance();
        dbref = FirebaseDatabase.getInstance().getReference();
        serviceRef = dbref.child("Account").child("ServiceProvider").child(username);

        // Keeps availabilities in sync with any database side changes
        ValueEventListener avListener
                = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Get availabilities HashMap
                availabilities = (HashMap<String, String>) dataSnapshot.getValue();
                // Update availability UI
                mondayAvText.setText(availabilities.get("Monday"));
                tuesdayAvText.setText(availabilities.get("Tuesday"));
                wednesdayAvText.setText(availabilities.get("Wednesday"));
                thursdayAvText.setText(availabilities.get("Thursday"));
                fridayAvText.setText(availabilities.get("Friday"));
                saturdayAvText.setText(availabilities.get("Saturday"));
                sundayAvText.setText(availabilities.get("Sunday"));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        serviceRef.child("availabilities").addValueEventListener(avListener);
    }

    /**  Updates or adds a new availability for a service provider. */
    private void updateAvailabilities() {

        // Obtain availability info from UI
        String fromTime = editTextFromTime.getText().toString().trim();
        String toTime = editTextToTime.getText().toString().trim();
        String day = spinnerDays.getSelectedItem().toString();
        boolean checked = checkBoxAvailable.isChecked();


        // Verify if Not Available is checked
        if (checked) {
            // It is checked:
            // Set availability of that day to "Not Available"
            availabilities.put(day, "Not Available");
        } else {
            // It is not checked:
            // Check that inputted times are valid
            if (isValidTime(fromTime) && isValidTime(toTime)) {
                // Times are valid:
                // Set the availability of that day to the appropriate time
                availabilities.put(day, fromTime + " to " + toTime);
            } else {
                // Times are invalid:
                // Notify user
                Toast.makeText(getApplicationContext(), "Times must be in 24h format (hh:mm)", Toast.LENGTH_LONG).show();
                return;
            }
        }

        // Time inputs have been validated, update Firebase and notify user
        serviceRef.child("availabilities").setValue(availabilities);
        Toast.makeText(getApplicationContext(), "Availability has been updated", Toast.LENGTH_LONG).show();
    }

    /** Returns true if time is a valid 24h time (i.e hh:mm) and false otherwise. */
    private boolean isValidTime(String time) {
        return time.matches("^([01]\\d|2[0-3]):?([0-5]\\d)$");
    }

    /* Disables time EditTexts when Not Available checkbox is checked and enables them otherwise */
    public void onCheckboxClicked(View view) {

        // Get the checkboxes current value
        boolean checked = checkBoxAvailable.isChecked();

        // If it is checked, disable EditTexts
        if (checked) {
            editTextFromTime.setEnabled(false);
            editTextToTime.setEnabled(false);
        } else {
            // It is unchecked, enable EditTexts
            editTextFromTime.setEnabled(true);
            editTextToTime.setEnabled(true);
        }
    }

    /** Returns the current activity to the serviceProvider's welcome Page */
    private void back() {

        Bundle extras = getIntent().getExtras();
        String username = extras.getString("username");
        Intent intent = new Intent(getApplicationContext(), WelcomeServiceProvider.class);
        intent.putExtra("username",username);
        intent.putExtra("role", "ServiceProvider");
        startActivityForResult(intent, 0);
    }




}
