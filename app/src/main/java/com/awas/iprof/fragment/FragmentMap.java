package com.awas.iprof.fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.awas.iprof.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Objects;

public class FragmentMap extends Fragment implements OnMapReadyCallback, LocationListener {

    // Tag to filter class in LOG
    private static String TAG = FragmentMap.class.getName();

    // Variabiles for Map
    private MapView mapView;
    private LatLng position;
    private LocationManager locationManager;
    private Location location;
    private Marker marker;
    private GoogleMap map;

    // Test
    private LatLng temp;

    // Unique Code for the Authorization (Internal in APP)
    private final int REQUEST_CODE = 101;

    // Class Constructor
    public FragmentMap() {
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    // Constructor onCreateView of Fragment with Layout
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_map, container, false);
    }

    // Call when screen is just created
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mapView = view.findViewById(R.id.maps);
        mapView.onCreate(savedInstanceState);

        // Map focused on user
        MapsInitializer.initialize(Objects.requireNonNull(Objects.requireNonNull(getActivity())).getApplicationContext());

        // Map get in Async Method which call itself
        mapView.getMapAsync(this);
    }

    // When Map is Ready can make operation (ex. Permission)
    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.map = googleMap;

        if (ActivityCompat.checkSelfPermission(Objects.requireNonNull(getContext()), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            setupMap(this.map);
        }else{
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        // super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_CODE:
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    Log.d(TAG, "Permission OK");
                    setupMap(this.map);
                }else{
                    Log.d(TAG, "Permission Denied");
                }
                return;
            default:
                return;
        }
    }

    @SuppressLint("MissingPermission")
    public void setupMap(GoogleMap map){

        mapView.onResume();

        map.setMyLocationEnabled(true);

        // Map Controls
        map.getUiSettings().setZoomControlsEnabled(true);
        map.getUiSettings().setZoomGesturesEnabled(true);
        map.getUiSettings().setCompassEnabled(true);
        map.getUiSettings().setMyLocationButtonEnabled(true);
        map.getUiSettings().setRotateGesturesEnabled(true);

        temp = new LatLng(44, 11);

        MarkerOptions options = new MarkerOptions().position(temp).title("Temp Position").snippet("Descrizione");
        marker = map.addMarker(options);

        CameraPosition cameraPosition = new CameraPosition.Builder().target(temp).zoom(12).build();
        map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

        locationManager = (LocationManager) Objects.requireNonNull(getActivity()).getSystemService(Context.LOCATION_SERVICE);

        location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

        if(location != null){
            onLocationChanged(location);
        }

        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 2000, 0, this);
    }

    @Override
    public void onLocationChanged(Location location) {
        if(this.map != null){

            double latitude = location.getLatitude();
            double longitude = location.getLongitude();

            position = new LatLng(latitude, longitude);

            if(marker != null){
                marker.remove();
            }

            // New Marker of your position
            MarkerOptions options = new MarkerOptions();
            options.position(position);
            options.title("You");

            marker = this.map.addMarker(options);
            this.map.moveCamera(CameraUpdateFactory.newLatLng(position));
            this.map.animateCamera(CameraUpdateFactory.newLatLngZoom(position, 12.0f));
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    public void onStart() {
        super.onStart();

        if(mapView != null){
            mapView.onStart();
        }
    }

    @Override
    public void onResume() {
        mapView.onResume();
        super.onResume();
    }

    @Override
    public void onDestroy() {
        mapView.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onPause() {

        if(mapView != null){
            mapView.onPause();
        }

        super.onPause();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }
}
