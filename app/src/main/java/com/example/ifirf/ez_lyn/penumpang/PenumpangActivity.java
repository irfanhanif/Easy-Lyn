package com.example.ifirf.ez_lyn.penumpang;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.ifirf.ez_lyn.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class PenumpangActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private LatLng current_position;
    private ImageButton getCurrentLocation;
    private Marker your_marker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_penumpang);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

        getCurrentLocation = (ImageButton) findViewById(R.id.goto_current_btn);
        getCurrentLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMap.moveCamera(CameraUpdateFactory.newLatLng(current_position));
                mMap.moveCamera(CameraUpdateFactory.zoomTo(18));
                your_marker.showInfoWindow();
            }
        });

        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera

        current_position = new LatLng(-7.2797206, 112.7975585);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(current_position));
        mMap.moveCamera(CameraUpdateFactory.zoomTo(18));
        Bitmap you = this.customMarker(R.drawable.you, 80, 80);
        your_marker = mMap.addMarker(
                new MarkerOptions().position(current_position).icon((BitmapDescriptorFactory.fromBitmap(you))));
        your_marker.setTitle("Anda Disini!");
        your_marker.showInfoWindow();

        Bitmap bus_marker_icon = this.customMarker(R.drawable.bus_driver, 100, 100);
        LatLng bus_position = new LatLng(-7.2802124, 112.7979468);
        Marker bus_driver = mMap.addMarker(
                new MarkerOptions().position(bus_position)
                                    .icon(BitmapDescriptorFactory.fromBitmap(bus_marker_icon)));
        bus_driver.setTitle("Bus ITS 1");

        Bitmap bus_stop_icon = this.customMarker(R.drawable.bus_stop, 80, 80);
        LatLng bus_stop_position = new LatLng(-7.279282, 112.797815);
        Marker bus_stop = mMap.addMarker(
                new MarkerOptions().position(bus_stop_position)
                        .icon(BitmapDescriptorFactory.fromBitmap(bus_stop_icon)));
        bus_stop.setTitle("Bus Stop: Teknik Informatika ITS");

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                if (marker.getTitle().toString().toLowerCase().contains("stop")) {
                    Toast.makeText(PenumpangActivity.this, marker.getTitle().toString(), Toast.LENGTH_LONG).show();
                    return true;
                }
                else return false;
            }
        });
    }

    private Bitmap customMarker(int source, int height, int width){
        BitmapDrawable bitmapDrawable = (BitmapDrawable) getResources().getDrawable(source);
        Bitmap bitmap = bitmapDrawable.getBitmap();
        return Bitmap.createScaledBitmap(bitmap, height, width, false);
    }
}
