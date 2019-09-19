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

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class AddOfferedService extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    Button buttonAddService;
    Button buttonBack;
    EditText editTextServiceName;

    FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_offered_service);

        Bundle extras = getIntent().getExtras();
        String username = extras.getString("username");

        Spinner dropdownService = findViewById(R.id.dropdownService);
        List<String> items = new ArrayList<String>();

        database = FirebaseDatabase.getInstance();
        DatabaseReference dbref = FirebaseDatabase.getInstance().getReference();
        final DatabaseReference servicesRef = dbref.child("Service");
        servicesRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Spinner dropdownService = findViewById(R.id.dropdownService);
                List<String> items = new ArrayList<String>();
                for (DataSnapshot snapshot: dataSnapshot.getChildren()) {
                    String serviceName  = snapshot.child("serviceName").getValue().toString().trim();
                    items.add(serviceName);
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<>(AddOfferedService.this, android.R.layout.simple_spinner_dropdown_item, items);
                dropdownService.setAdapter(adapter);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
        buttonAddService = (Button) findViewById(R.id.buttonAddP);
        buttonBack = (Button) findViewById(R.id.buttonBackOff);
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


    private void addService(){
        Bundle extras = getIntent().getExtras();
        String username = extras.getString("username");

        database = FirebaseDatabase.getInstance();

        Spinner dropdownService = findViewById(R.id.dropdownService);
        String text = dropdownService.getSelectedItem().toString();
//        Toast.makeText(getApplicationContext(), text, Toast.LENGTH_LONG).show();

        DatabaseReference dbref = FirebaseDatabase.getInstance().getReference();
        final DatabaseReference servicesRef = dbref.child("Account").child("ServiceProvider").child(username);
        servicesRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<String> presentServices = new ArrayList<String>();
                presentServices = (ArrayList<String>) dataSnapshot.child("services").getValue();

                Spinner dropdownService = findViewById(R.id.dropdownService);
                String newService = dropdownService.getSelectedItem().toString();
                if(presentServices != null && presentServices.contains(newService)){
                    Toast.makeText(getApplicationContext(), "You already have this service listed", Toast.LENGTH_LONG).show();
                }
                else{
                    if(presentServices == null){
                        presentServices = new ArrayList<String>();
                    }
                    for(int i = 0; i < presentServices.size(); i++){
                        String index = Integer.toString(i);
                        servicesRef.child("services").child(index).setValue(presentServices.get(i));
                    }
                    String index = Integer.toString(presentServices.size());
                    servicesRef.child("services").child(index).setValue(newService);
                }
                Toast.makeText(getApplicationContext(), "Service has been added", Toast.LENGTH_LONG).show();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
        // An item was selected. You can retrieve the selected item using
        // parent.getItemAtPosition(pos)
//        role = parent.getItemAtPosition(pos).toString();
    }

    public void onNothingSelected(AdapterView<?> parent) {}

    private void back(){
        Bundle extras = getIntent().getExtras();
        String username = extras.getString("username");
        Intent intent = new Intent(getApplicationContext(), ServicesListProvider.class);
        intent.putExtra("username",username);
        startActivityForResult(intent, 0);
    }
}
