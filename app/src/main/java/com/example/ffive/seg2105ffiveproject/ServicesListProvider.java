package com.example.ffive.seg2105ffiveproject;

import android.content.Intent;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ServicesListProvider extends AppCompatActivity {

    Button buttonBack;
    Button buttonAddService;

    FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_services_list_provider);

        buttonBack = (Button) findViewById(R.id.buttonServicesListBack);
        buttonAddService = (Button) findViewById(R.id.buttonAddProvService);
        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                back();
            }
        });
        buttonAddService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToAddService();
            }
        });

        updateServiceList();
    }

    private void back(){
        Bundle extras = getIntent().getExtras();
        String username = extras.getString("username");
        Intent intent = new Intent(getApplicationContext(), WelcomeServiceProvider.class);
        intent.putExtra("username",username);
        intent.putExtra("role", "ServiceProvider");
        startActivityForResult(intent, 0);
    }

    private void goToAddService(){
        Bundle extras = getIntent().getExtras();
        String username = extras.getString("username");
        Intent intent = new Intent(getApplicationContext(), AddOfferedService.class);
        intent.putExtra("username",username);
        startActivityForResult(intent, 0);
    }

    public void updateServiceList(){
        Bundle extras = getIntent().getExtras();
        String username = extras.getString("username");

        database = FirebaseDatabase.getInstance();
        DatabaseReference dbref = FirebaseDatabase.getInstance().getReference();
        final DatabaseReference servicesRef = dbref.child("Account").child("ServiceProvider").child(username);

        servicesRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<String> presentServices = new ArrayList<String>();
                presentServices = (ArrayList<String>) dataSnapshot.child("services").getValue();

                String servicesList = "";
                ListView lvProvider = (ListView)findViewById(R.id.lvProvider);

                if(presentServices!=null){
                    for(int i = 0; i < presentServices.size(); i++){
                        servicesList += presentServices.get(i) + ", ";
                    }
                    if(!servicesList.equals("")){
                        servicesList = "" + servicesList.substring(0, servicesList.length()-2);
                    }
                }
                else{
                    servicesList = "No services currently listed";
                }
                // servicesTextView.setText(servicesList);

                String [] servicesListArray = servicesList.split(",");
                ArrayAdapter<String> adapter =  new ArrayAdapter<String>(ServicesListProvider.this, android.R.layout.simple_list_item_1,servicesListArray);

                lvProvider.setAdapter(adapter);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

        final ListView lvProvider = (ListView)findViewById(R.id.lvProvider);
        lvProvider.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Bundle extras = getIntent().getExtras();
                String username = extras.getString("username");

                final int position2 = position;

                database = FirebaseDatabase.getInstance();

                DatabaseReference dbref = FirebaseDatabase.getInstance().getReference();
                final DatabaseReference servicesRef = dbref.child("Account").child("ServiceProvider").child(username);
                servicesRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Cursor cursor = (Cursor) lvProvider.getItemAtPosition(position2);
                        Object selectedFromList = (lvProvider.getItemAtPosition(position2));
                        String selectedFromListS = selectedFromList.toString().trim();

                        if(!selectedFromListS.trim().equals("No services currently listed")){
                            List<String> presentServices = new ArrayList<String>();
                            presentServices = (ArrayList<String>) dataSnapshot.child("services").getValue();

                            for(int i = 0; i < presentServices.size(); i++){
                                if(presentServices.get(i).equals(selectedFromListS)){
                                    presentServices.remove(i);
                                    break;
                                }
                            }
                            servicesRef.child("services").removeValue();
                            for(int i = 0; i < presentServices.size(); i++){
                                String index = Integer.toString(i);
                                servicesRef.child("services").child(index).setValue(presentServices.get(i));
                            }
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });

                finish();
                startActivity(getIntent());
//                Intent intent = new Intent(getApplicationContext(), ServicesListProvider.class);
//                intent.putExtra("username",username);
//                startActivityForResult(intent, 0);
            }
        });
    }

    public void deleteService(String serviceToDelete){

    }
}
