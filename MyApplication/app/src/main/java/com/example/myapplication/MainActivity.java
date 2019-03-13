package com.example.myapplication;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import java.util.Timer;
import java.util.TimerTask;



import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;


public class MainActivity extends AppCompatActivity {
    Button buttonFoggy;
    Button buttonSunny;
    TextView mConditionTextView;

    FusedLocationProviderClient client;
    DatabaseReference newRef;
    DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    DatabaseReference mConditionRef = mRootRef.child("location");
    Users testUser = new Users("testName", "vuhu@tcd.ie");
    Users testUser2 = new Users("testName2", "vuhu@tcd.ie2");
    Users testUser3 = new Users("testName3", "vuhu@tcd.ie3");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        requestPermission();
        mConditionTextView = (TextView) findViewById((R.id.condition));
        buttonFoggy = (Button) findViewById(R.id.Foggy);
        buttonSunny = (Button) findViewById(R.id.sunny);


        client = LocationServices.getFusedLocationProviderClient(this);

        final long period = 4000;
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                sendLocation();
            }
        }, 0, period);


    }

    @Override
    protected void onStart() {
        super.onStart();

        mConditionRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });


        buttonSunny.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ActivityCompat.checkSelfPermission( MainActivity.this, ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    Log.d("test", "IN error");
                    return;
                }
                client.getLastLocation().addOnSuccessListener(MainActivity.this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        Log.d("test", "IN ON SUCCESS");
                        if (location != null){
                            Log.d("test", "IN LOCATION != NULL");
                            LongLat newLocation = new LongLat(location.getLongitude(), location.getLatitude());
                            Log.d("test", "" + location.getLongitude());
                            newRef = mConditionRef.push();
                            newRef.setValue(newLocation);
                        }
                    }
                });

            }
        });

        buttonFoggy.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                newRef = mConditionRef.push();
                newRef.setValue(testUser3);
            }
        });

    }


    private void requestPermission(){
        ActivityCompat.requestPermissions(this, new String[]{ACCESS_FINE_LOCATION}, 1);
    }

    private void sendLocation(){
        if (ActivityCompat.checkSelfPermission( MainActivity.this, ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.d("test", "IN error");
            return;
        }
        client.getLastLocation().addOnSuccessListener(MainActivity.this, new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                Log.d("test", "IN ON SUCCESS");
                if (location != null){
                    Log.d("test", "IN LOCATION != NULL");
                    LongLat newLocation = new LongLat(location.getLongitude(), location.getLatitude());
                    Log.d("test", "" + location.getLongitude());
                    newRef = mConditionRef.push();
                    newRef.setValue(newLocation);
                }
            }
        });
    }
}
