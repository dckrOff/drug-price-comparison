package com.a1tech.drugprice.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.a1tech.drugprice.Adapter.DrugsAdapter;
import com.a1tech.drugprice.Adapter.PharmAdapter;
import com.a1tech.drugprice.Model.Drug;
import com.a1tech.drugprice.Model.Pharm;
import com.a1tech.drugprice.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

import java.util.ArrayList;
import java.util.Objects;

public class PharmsActivity extends AppCompatActivity {

    private final String TAG = "PharmActivity   ";
    private BottomSheetBehavior<ConstraintLayout> bottomSheetBehavior;
    private RecyclerView rvPharma;
    private ArrayList<Pharm> pharms = new ArrayList<Pharm>();
    private ConstraintLayout bottomSheet;

    private final int ACCESS_LOCATION_REQUEST_CODE = 10001;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private LocationRequest locationRequest;
    private double lat = 0.0, lon = 0.0;
    private LocationCallback locationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            super.onLocationResult(locationResult);
            setUserLocationMarker(Objects.requireNonNull(locationResult.getLastLocation()));
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drug);

        init();
        setList();
        setAdapter();
        setLocationUpdateInterval();

    }

    private void init() {
        rvPharma = findViewById(R.id.rv_pharma);
        bottomSheet = findViewById(R.id.bottomSheet);

        //#2 Initializing the BottomSheetBehavior
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);

        bottomSheetBehavior.setPeekHeight(250);
//        bottomSheetBehavior.setMaxHeight(1000);
        bottomSheetBehavior.setHideable(false);

        bottomSheetBehavior.addBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                newState = BottomSheetBehavior.STATE_COLLAPSED;
//                newState = BottomSheetBehavior.STATE_EXPANDED;
//                newState = BottomSheetBehavior.STATE_DRAGGING;
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });

    }

    private void setList() {
        pharms.add(new Pharm("Gulora MED PLUS 5+", "79000", "200м от вас", "https://firebasestorage.googleapis.com/v0/b/testproject-7ed45.appspot.com/o/Drugs%2FIngavirin-e1565687015128.jpg?alt=media&token=854fa891-08a5-4536-8ca7-9d996f9dbc62"));
        pharms.add(new Pharm("Gulora MED PLUS 5+", "79000", "200м от вас", "https://firebasestorage.googleapis.com/v0/b/testproject-7ed45.appspot.com/o/Drugs%2FIngavirin-e1565687015128.jpg?alt=media&token=854fa891-08a5-4536-8ca7-9d996f9dbc62"));
        pharms.add(new Pharm("Gulora MED PLUS 5+", "79000", "200м от вас", "https://firebasestorage.googleapis.com/v0/b/testproject-7ed45.appspot.com/o/Drugs%2FIngavirin-e1565687015128.jpg?alt=media&token=854fa891-08a5-4536-8ca7-9d996f9dbc62"));
        pharms.add(new Pharm("Gulora MED PLUS 5+", "79000", "200м от вас", "https://firebasestorage.googleapis.com/v0/b/testproject-7ed45.appspot.com/o/Drugs%2FIngavirin-e1565687015128.jpg?alt=media&token=854fa891-08a5-4536-8ca7-9d996f9dbc62"));
        pharms.add(new Pharm("Gulora MED PLUS 5+", "79000", "200м от вас", "https://firebasestorage.googleapis.com/v0/b/testproject-7ed45.appspot.com/o/Drugs%2FIngavirin-e1565687015128.jpg?alt=media&token=854fa891-08a5-4536-8ca7-9d996f9dbc62"));
        pharms.add(new Pharm("Gulora MED PLUS 5+", "79000", "200м от вас", "https://firebasestorage.googleapis.com/v0/b/testproject-7ed45.appspot.com/o/Drugs%2FIngavirin-e1565687015128.jpg?alt=media&token=854fa891-08a5-4536-8ca7-9d996f9dbc62"));
        pharms.add(new Pharm("Gulora MED PLUS 5+", "79000", "200м от вас", "https://firebasestorage.googleapis.com/v0/b/testproject-7ed45.appspot.com/o/Drugs%2FIngavirin-e1565687015128.jpg?alt=media&token=854fa891-08a5-4536-8ca7-9d996f9dbc62"));
        pharms.add(new Pharm("Gulora MED PLUS 5+", "79000", "200м от вас", "https://firebasestorage.googleapis.com/v0/b/testproject-7ed45.appspot.com/o/Drugs%2FIngavirin-e1565687015128.jpg?alt=media&token=854fa891-08a5-4536-8ca7-9d996f9dbc62"));
        pharms.add(new Pharm("Gulora MED PLUS 5+", "79000", "200м от вас", "https://firebasestorage.googleapis.com/v0/b/testproject-7ed45.appspot.com/o/Drugs%2FIngavirin-e1565687015128.jpg?alt=media&token=854fa891-08a5-4536-8ca7-9d996f9dbc62"));
    }

    private void setAdapter() {
        rvPharma.setLayoutManager(new GridLayoutManager(this, 1));
        PharmAdapter pharmAdapter = new PharmAdapter(getApplicationContext(), pharms);
        rvPharma.setAdapter(pharmAdapter); // set the Adapter to RecyclerView
    }

    private void setUserLocationMarker(Location location) {
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        lat = location.getLatitude();
        lon = location.getLongitude();
        Log.e(TAG, String.valueOf(latLng));
    }

    private void setLocationUpdateInterval() {
        // set map update interval
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        locationRequest = LocationRequest.create();
        locationRequest.setInterval(500);
        locationRequest.setFastestInterval(500);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }
}