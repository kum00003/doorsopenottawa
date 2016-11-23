package com.algonquincolleg.kum00003.doorsopenottawa;

import android.app.Activity;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Locale;
/**
 * Created by Sercan on 11/22/16.
 * Created by David on 11/22/16.
 */

public class DetailActivity extends FragmentActivity implements OnMapReadyCallback {
        private GoogleMap mMap;
        private Geocoder mGeocoder;
        private String addressText;

protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        mGeocoder = new Geocoder(this, Locale.CANADA);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
         SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
            .findFragmentById(R.id.map);
         mapFragment.getMapAsync(this);


            TextView buildingname = (TextView) findViewById(R.id.buildingName);
            TextView description = (TextView) findViewById(R.id.description);
            TextView address = (TextView) findViewById(R.id.address);
            TextView date = (TextView) findViewById(R.id.Hours);
            Button btnClose = (Button) findViewById(R.id.btnClose);


            Intent i = getIntent();
            // Receiving the Data
            String name = i.getStringExtra("name");
            String descriptiontext = i.getStringExtra("description");
            addressText = i.getStringExtra("address");
            String dateText = i.getStringExtra("date");

            // Displaying Received data
            buildingname.setText(name);
            description.setText(descriptiontext);
            address.setText(addressText);
            date.setText(dateText);

            btnClose.setOnClickListener(new View.OnClickListener() {

                public void onClick(View arg0) {
                    //Closing SecondScreen Activity
                    finish();
                }
            });



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

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        pin(addressText);

    }
    private void pin( String locationName ) {
        try {
            Address address = mGeocoder.getFromLocationName(locationName, 1).get(0);
            LatLng ll = new LatLng( address.getLatitude(), address.getLongitude() );
            mMap.addMarker( new MarkerOptions().position(ll).title(locationName) );
            mMap.moveCamera( CameraUpdateFactory.newLatLngZoom(ll,14f) );
            Toast.makeText(this, "Pinned: " + locationName, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(this, "Not found: " + locationName, Toast.LENGTH_SHORT).show();
        }
    }


}