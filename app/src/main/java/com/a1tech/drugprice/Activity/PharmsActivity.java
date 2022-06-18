package com.a1tech.drugprice.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.util.Log;
import android.view.View;

import com.a1tech.drugprice.Adapter.PharmAdapter;
import com.a1tech.drugprice.Model.Pharm;
import com.a1tech.drugprice.R;
import com.a1tech.drugprice.Utils.MyComparator;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;

public class PharmsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private final String TAG = "PharmActivity   ";
    private final int ACCESS_LOCATION_REQUEST_CODE = 10001;
    private BottomSheetBehavior<ConstraintLayout> bottomSheetBehavior;
    private RecyclerView rvPharma;
    private ArrayList<Pharm> pharms = new ArrayList<Pharm>();
    private ConstraintLayout bottomSheet;
    private String drugImg, drugId;
    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private LocationRequest locationRequest;
    private double lat = 0, lon = 0;
    private GoogleMap mMap;
    private Geocoder geocoder;
    private Marker userLocationMarker;
    private boolean distanceSet = false;
    private Circle userLocationAccuracyCircle;
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
        setContentView(R.layout.activity_pharm);

        init();
        initMap();
//        setList();
//        setAdapter();
        setLocationUpdateInterval();
    }

    private void init() {
        database = FirebaseDatabase.getInstance();
//        myRef = database.getReference("drug_list");

        Bundle arguments = getIntent().getExtras();
        drugImg = arguments.get("drug_img").toString();
        drugId = arguments.get("drug_id").toString();
        myRef = database.getReference(drugId);


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
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                MarkerOptions markerOptions = new MarkerOptions();
                LatLng latLng;
                pharms.clear();
                for (DataSnapshot childDataSnapshot : dataSnapshot.getChildren()) {
//                    Log.e(TAG, "1) " + childDataSnapshot.getKey()); //displays the key for the node
//                    Log.e(TAG, "2) " + childDataSnapshot.child("img").getValue());   //gives the value for given keyname
                    pharms.add(new Pharm(childDataSnapshot.child("pharm_name").getValue().toString(), childDataSnapshot.child("price").getValue().toString(), drugImg, Double.parseDouble(childDataSnapshot.child("lat").getValue().toString()), Double.parseDouble(childDataSnapshot.child("lon").getValue().toString()), getDistance(Double.parseDouble(childDataSnapshot.child("lat").getValue().toString()), Double.parseDouble(childDataSnapshot.child("lon").getValue().toString()))));
                    latLng = new LatLng(Double.parseDouble(childDataSnapshot.child("lat").getValue().toString()), Double.parseDouble(childDataSnapshot.child("lon").getValue().toString()));
                    markerOptions.position(latLng);
                    mMap.addMarker(markerOptions).setTitle(childDataSnapshot.child("pharm_name").getValue().toString());
                }
                Collections.sort(pharms, new MyComparator());
                setAdapter();
                Log.e(TAG, "list size=> " + pharms.size());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Failed to read value
                Log.e(TAG, "Failed to read value.", error.toException());
            }
        });
    }

    private void setAdapter() {
        rvPharma.setLayoutManager(new GridLayoutManager(this, 1));
        PharmAdapter pharmAdapter = new PharmAdapter(getApplicationContext(), pharms);
        rvPharma.setAdapter(pharmAdapter); // set the Adapter to RecyclerView
    }

    @SuppressLint("DefaultLocale")
    public String getDistance(double pharmLat, double pharmLon) {
        float distance = calculateDistance(lat, lon, pharmLat, pharmLon);
        Log.e(TAG, "distance-> " + distance);
        String dis = null;
        if (distance > 999) {
            dis = String.format("%.2f", (distance / 1000)) + "км от вас";
            return dis;
        } else {
            dis = String.format("%.2f", distance) + "м от вас";
            return dis;
        }
    }

    public float calculateDistance(double startLat, double startLon, double endLat, double endLon) {
        float[] results = new float[1];
        Location.distanceBetween(startLat, startLon, endLat, endLon, results);

        return results[0];
    }

    private void initMap() {
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        geocoder = new Geocoder(this);
    }

    private void setLocationUpdateInterval() {
        // set map update interval
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        locationRequest = LocationRequest.create();
        locationRequest.setInterval(500);
        locationRequest.setFastestInterval(500);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        // mMap.setOnMapLongClickListener(this);
        // mMap.setOnMarkerDragListener(this);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            // enableUserLocation();
            // zoomToUserLocation();
        } else {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                //We can show user a dialog why this permission is necessary
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, ACCESS_LOCATION_REQUEST_CODE);
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, ACCESS_LOCATION_REQUEST_CODE);
            }
        }
    }

    private void setUserLocationMarker(Location location) {
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        lat = location.getLatitude();   // set current location
        lon = location.getLongitude();
        Log.e(TAG, String.valueOf(latLng));

        if (userLocationMarker == null) {
            //Create a new marker
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(latLng);
//            markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.greencar));
//            markerOptions.rotation(location.getBearing());
            markerOptions.anchor(0.5f, 1);
            userLocationMarker = mMap.addMarker(markerOptions);
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 13));
        } else {
            //use the previously created marker
            userLocationMarker.setPosition(latLng);
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 13));
//            userLocationMarker.setRotation(location.getBearing());
        }
        if (!distanceSet) {
            setList();
            distanceSet = true;
        }
    }

    private void startLocationUpdates() {
        checkPermission();
        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper());
    }

    private void stopLocationUpdates() {
        fusedLocationProviderClient.removeLocationUpdates(locationCallback);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            startLocationUpdates();
        } else {
            // you need to request permissions...
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        stopLocationUpdates();
//        onDestroy();
    }

    public void checkPermission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            //   TODO: Consider calling
            //   ActivityCompat#requestPermissions
            //   here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            //   to handle the case where the user grants the permission. See the documentation
            //   for ActivityCompat#requestPermissions for more details.
            return;
        }
    }

    private void enableUserLocation() {
        checkPermission();
        mMap.setMyLocationEnabled(true);
    }

    private void zoomToUserLocation() {
        checkPermission();
        Task<Location> locationTask = fusedLocationProviderClient.getLastLocation();
        locationTask.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 20));
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == ACCESS_LOCATION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                enableUserLocation();
                zoomToUserLocation();
            } else {
                //We can show a dialog that permission is not granted...
                final AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("Sizning GPS qurilmangiz o'chirilgan!")
                        .setCancelable(false)
                        .setPositiveButton("GPSni yoqish", new DialogInterface.OnClickListener() {
                            public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                                startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                            }
                        }).setNegativeButton("Bekor qilish", new DialogInterface.OnClickListener() {
                            public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                                dialog.cancel();
                            }
                        });
                final AlertDialog alert = builder.create();
                alert.show();
            }
        }
    }
}