package com.sealsproject.locationapp;

import androidx.fragment.app.FragmentActivity;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.sealsproject.locationapp.databinding.ActivityMapsBinding;

import java.io.IOException;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMapLongClickListener, GoogleMap.OnMarkerDragListener {

    private static final String TAG = "MainActivity";
    private GoogleMap mMap;
    private ActivityMapsBinding binding;
    Geocoder geocoder;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        geocoder = new Geocoder(this);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.setOnMapLongClickListener(this);

        mMap.setOnMarkerDragListener(this);
        //-------------------------------------------------------------------------------------------------------------------------
        //mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);

        // Add a marker in Sydney and move the camera
        //LatLng sydney = new LatLng(-34, 151);
        //mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        //mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

//        LatLng latLng = new LatLng(59.2887,17.9075);
//        MarkerOptions markerOptions = new MarkerOptions()
//                                            .position(latLng)
//                                            .title("Stockholm")
//                                            .snippet("Seals");
//
//        mMap.addMarker(markerOptions);
//        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 18);
//        mMap.animateCamera(cameraUpdate);

        //-------------------------------------------------------------------------------------------------------------------------

        try {
            List<Address> addresses = geocoder.getFromLocationName("Stockholm", 1);
            if(addresses.size() > 0) {
                Address address = addresses.get(0);
                //Log.d(TAG, "onMapReady: "+ address.toString());
                LatLng london = new LatLng(address.getLatitude(), address.getLongitude());
                MarkerOptions markerOptions = new MarkerOptions()
                        .position(london)
                        .title(address.getLocality())
                        .snippet("Seals");
                mMap.addMarker(markerOptions);

                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(london, 18));
            } else{
                //Log.d(TAG, "Notice: \n\n\n\n\nLocation does not Exists in the address, Code" + addresses.size() +" Negative\n\n\n\n\n\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


    }



    @Override
    public void onMapLongClick(LatLng latLng) {
        Log.d(TAG, "{\n\n\nLong pressed" + latLng.toString());
        try {
            List<Address> addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
            if(addresses.size()>0){
                Address address = addresses.get(0);
                String streetAddress = address.getAddressLine(0);
                mMap.addMarker(new MarkerOptions().position(latLng).position(latLng).title(streetAddress)).setDraggable(true);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        //mMap.addMarker(new MarkerOptions().position(latLng));

    }



    @Override
    public void onMarkerDragStart(Marker marker) {
        Log.d(TAG, "onMarkerDragStart \n");
    }



    @Override
    public void onMarkerDrag(Marker marker) {
        Log.d(TAG, "onMarkerDrag \n");
    }



    @Override
    public void onMarkerDragEnd(Marker marker) {
        Log.d(TAG, "onMarkerDragEnd \n");
        LatLng latLng = marker.getPosition();
        try {
            List<Address> addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
            if(addresses.size()>0){
                Address address = addresses.get(0);
                String streetAddress = address.getAddressLine(0);
                marker.setTitle(streetAddress);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}