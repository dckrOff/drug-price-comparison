package com.a1tech.drugprice.Activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.a1tech.drugprice.Model.Pharm;
import com.a1tech.drugprice.R;
import com.bumptech.glide.Glide;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import org.osmdroid.bonuspack.routing.OSRMRoadManager;
import org.osmdroid.bonuspack.routing.Road;
import org.osmdroid.bonuspack.routing.RoadManager;
import org.osmdroid.util.GeoPoint;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Objects;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class RouteActivity extends AppCompatActivity implements OnMapReadyCallback {

    private final String TAG = "RouteActivity";
    private final int ACCESS_LOCATION_REQUEST_CODE = 10001;
    private final String pattern = "###,###,###.###";
    private final DecimalFormat decimalFormat = new DecimalFormat(pattern);
    private ImageView ivDrugImage,mapType;
    private TextView tvPharmName, tvDrugPrice, tvPharmDistance;
    private String pharmaName, drugImage, drugPrice;
    private double pharmLat, pharmLon;
    private LatLng pharmLatLng, currentLatLng;
    private Road road;
    private Polyline polyline;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private LocationRequest locationRequest;
    private double lat, lon;
    private boolean mapChanged = false;
    private boolean distanceSet = false;
    private GoogleMap mMap;
    private Geocoder geocoder;
    private Marker userLocationMarker;
    private final LocationCallback locationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(@NonNull LocationResult locationResult) {
            super.onLocationResult(locationResult);
            setUserLocationMarker(Objects.requireNonNull(locationResult.getLastLocation()));
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route);

        init();
        initMap();
        getDataFromPrefs();
        setLocationUpdateInterval();
    }

    private void init() {
        ivDrugImage = findViewById(R.id.iv_drug_image_route);
        tvPharmName = findViewById(R.id.tv_pharma_name_route);
        tvDrugPrice = findViewById(R.id.tv_drug_price_route);
        tvPharmDistance = findViewById(R.id.tv_distance_route);
        mapType = findViewById(R.id.mapType);
        mapType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeMapType();
            }
        });
    }

    private void initMap() {
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        geocoder = new Geocoder(this);
    }

    @SuppressLint("SetTextI18n")
    private void getDataFromPrefs() {
        Bundle arguments = getIntent().getExtras();
        pharmaName = arguments.get("pharma_name").toString();
        drugImage = arguments.get("drug_image").toString();
        drugPrice = arguments.get("drug_price").toString();
        pharmLat = arguments.getDouble("lat", 0.0);
        pharmLon = arguments.getDouble("lon", 0.0);
        pharmLatLng = new LatLng(pharmLat, pharmLon);

        // formatter of number(1234567890)-- > (1 234 567 890)
        String formatPrice = decimalFormat.format(Double.valueOf(Integer.parseInt(drugPrice)));
        tvPharmName.setText(pharmaName);
        tvDrugPrice.setText(formatPrice + " сум");
        Glide.with(this).load(drugImage).into(ivDrugImage);
    }

    private void changeMapType() {
        if (!mapChanged) {
            mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            mapChanged = true;
//            changeMapType.setBackgroundResource(R.drawable.ic_satellite);
        } else {
            mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
            mapChanged = false;
//            changeMapType.setBackgroundResource(R.drawable.ic_default);
        }
    }

    private void setLocationUpdateInterval() {
        // set map update interval
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        locationRequest = LocationRequest.create();
        locationRequest.setInterval(500);
        locationRequest.setFastestInterval(500);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    private void calcMaxMinBound() {
        LatLngBounds.Builder builder = new LatLngBounds.Builder();

        //the include method will calculate the min and max bound.
        builder.include(currentLatLng);
        builder.include(pharmLatLng);

        LatLngBounds bounds = builder.build();
        int width = getResources().getDisplayMetrics().widthPixels;
        int height = getResources().getDisplayMetrics().heightPixels;
        int padding = (int) (width * 0.17); // offset from edges of the map 10% of screen
        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, width, height, padding);
        mMap.animateCamera(cu);

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
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(pharmLatLng);
        markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_marker));
        mMap.addMarker(markerOptions);
        route();
    }

    private void setUserLocationMarker(Location location) {
        currentLatLng = new LatLng(location.getLatitude(), location.getLongitude());
        lat = location.getLatitude();   // set current location
        lon = location.getLongitude();
        Log.e(TAG, String.valueOf(currentLatLng));

        if (userLocationMarker == null) {
            //Create a new marker
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(currentLatLng);
//            markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.greencar));
//            markerOptions.rotation(location.getBearing());
            markerOptions.anchor((float) 0.5, (float) 1);
            userLocationMarker = mMap.addMarker(markerOptions);
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 14));
        } else {
            //use the previously created marker
            userLocationMarker.setPosition(currentLatLng);
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 14));
//            userLocationMarker.setRotation(location.getBearing());
        }
        calcMaxMinBound();
        if (!distanceSet) {
            tvPharmDistance.setText(getDistance());
            distanceSet = true;
        }
        route();
    }

    public float calculateDistance(double startLat, double startLon, double endLat, double endLon) {
        float[] results = new float[1];
        Location.distanceBetween(startLat, startLon, endLat, endLon, results);

        return results[0];
    }

    @SuppressLint("DefaultLocale")
    public String getDistance() {
        float distance = calculateDistance(lat, lon, pharmLat, pharmLon);
        Log.e(TAG, "distance-> " + distance);
        String dis = null;
        if (distance > 999) {
            dis = String.format("%.2f", (distance / 1000)) + " км от вас";
            return dis;
        } else {
            dis = String.format("%.2f", distance) + " м от вас";
            return dis;
        }
    }

    private void route() {
        Log.e(TAG, "route->");
        final GeoPoint startPoint = new GeoPoint(lat, lon);
        final GeoPoint endPoint = new GeoPoint(pharmLat, pharmLon);

        if (endPoint.getLatitude() == 0d || endPoint.getLongitude() == 0d) return;
        if (startPoint.getLatitude() == 0d || startPoint.getLongitude() == 0d) return;

        AsyncTask.execute(() -> {
            Log.e(TAG, "route->1");
            RoadManager roadManager = new OSRMRoadManager(this);
            ArrayList<GeoPoint> wayPoints = new ArrayList<>();
            wayPoints.add(startPoint);
            wayPoints.add(endPoint);
            road = roadManager.getRoad(wayPoints);
            if (road.mStatus != Road.STATUS_OK) {
                runOnUiThread(this::showDialogRoutingTechnicalIssue);
                return;
            }
            drawRouteOnGMap();
        });
    }

    private void drawRouteOnGMap() {
        ArrayList<LatLng> list = new ArrayList();

        Log.e(TAG, "listSize " + list.size());
        for (int i = 0; i < road.mRouteHigh.size(); i++) {
            list.add(new LatLng(road.mRouteHigh.get(i).getLatitude(), road.mRouteHigh.get(i).getLongitude()));
        }

        PolylineOptions options = new PolylineOptions().width(6).color(Color.BLUE).geodesic(true);
        for (int z = 0; z < list.size(); z++) {
            LatLng point = new LatLng(list.get(z).latitude, list.get(z).longitude);
            options.add(point);
        }
        runOnUiThread(() -> {
//            mMap.clear();
            polyline = mMap.addPolyline(options);
        });
    }

    private void showDialogRoutingTechnicalIssue() {
        // 2. Confirmation message
        new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
                .setTitleText("Внимание!")
                .setContentText("Marshrut chizishda xatolik yuz berdi")
                .setConfirmText("Qayta urunish")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sDialog.dismissWithAnimation();
                        route();
                    }
                })
                .setCancelButton("OK", new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sDialog.dismissWithAnimation();
                    }
                })
                .show();
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