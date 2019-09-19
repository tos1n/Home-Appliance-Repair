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
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ServiceList extends AppCompatActivity {

    Button buttonAddService;
    TextView servicesTextView;
    Button buttonBack;

    FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_list);

        //servicesTextView = (TextView) findViewById(R.id.serviceTextView);
        buttonBack = (Button) findViewById(R.id.buttonBack);
        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Back(view);
            }
        });
        buttonAddService = (Button) findViewById(R.id.buttonAddService);
        buttonAddService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToAddService();
            }
        });

        database = FirebaseDatabase.getInstance();
        DatabaseReference dbref = FirebaseDatabase.getInstance().getReference();
        final DatabaseReference servicesRef = dbref.child("Service");

        servicesRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String servicesList = "";
                ListView lv = (ListView)findViewById(R.id.lv);
                for (DataSnapshot snapshot: dataSnapshot.getChildren()) {
                    String serviceName  = snapshot.child("serviceName").getValue().toString().trim();
                    String rateperhour  = snapshot.child("rate").getValue().toString();
                    servicesList += (serviceName + "($" + rateperhour + "/hour), ");
                }
                if(!servicesList.equals("")){
                    servicesList = "" + servicesList.substring(0, servicesList.length()-2);
                }
                // servicesTextView.setText(servicesList);

                String [] servicesListArray = servicesList.split(",");

                ArrayAdapter<String> adapter =  new ArrayAdapter<String>(ServiceList.this, android.R.layout.simple_list_item_1,servicesListArray);

                lv.setAdapter(adapter);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

        ListView lv = (ListView)findViewById(R.id.lv);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ListView lv = (ListView)findViewById(R.id.lv);
                Cursor cursor = (Cursor) lv.getItemAtPosition(position);
                Object selectedFromList = (lv.getItemAtPosition(position));
                selectedFromList = selectedFromList.toString();
                //until index of (-1 and from index of (+2 to index of /-1
                int theSlash = ((String) selectedFromList).indexOf('/');
                int theBracket = ((String) selectedFromList).indexOf('(');
                String serviceName = ((String) selectedFromList).substring(0, theBracket);
                String rate = ((String) selectedFromList).substring(theBracket+2,theSlash);
                Intent intent = new Intent(ServiceList.this, EditDeleteServiceActivity.class);
                intent.putExtra("serviceName",serviceName);
                intent.putExtra("rate", rate);
                startActivity(intent);
            }
        });
    }

    private void Back(View view){
        //takes the user to the sign up page
        Toast.makeText(getApplicationContext(), "Going back", Toast.LENGTH_LONG).show();
        Intent intent = new Intent(getApplicationContext(), WelcomeAdminActivity.class);
        startActivityForResult(intent, 0);
    }

    public void goToAddService(){
        Intent intent = new Intent(this, AddServiceActivity.class);
        startActivity(intent);
    }

    public void goToEditDeleteService(){
        Intent intent = new Intent(this, EditDeleteServiceActivity.class);
        intent.putExtra("serviceName", "Plumbing");
        intent.putExtra("rate", "60"); //placeholder rate
        startActivity(intent);
    }
}
