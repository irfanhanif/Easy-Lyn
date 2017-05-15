package com.example.ifirf.ez_lyn.pengemudi;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.support.v4.app.ActivityCompat;
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
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class PengemudiActivity extends FragmentActivity implements OnMapReadyCallback {
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

        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.getUiSettings().setMyLocationButtonEnabled(true);

        // Add a marker in Sydney and move the camera

        current_position = new LatLng(-7.2802124, 112.7979468);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(current_position));
        mMap.moveCamera(CameraUpdateFactory.zoomTo(18));
        Bitmap your_icon = this.customMarker(R.drawable.you, 50, 50);
        your_marker = mMap.addMarker(
                new MarkerOptions().position(current_position)
                        .icon(BitmapDescriptorFactory.fromBitmap(your_icon)));
        your_marker.setTitle("Bus ITS 1");

        Bitmap bus_stop_icon = this.customMarker(R.drawable.bus_stop, 80, 80);
        LatLng bus_stop_position = new LatLng(-7.279282, 112.797815);
        Marker bus_stop = mMap.addMarker(
                new MarkerOptions().position(bus_stop_position)
                        .icon(BitmapDescriptorFactory.fromBitmap(bus_stop_icon)));
        bus_stop.setTitle("Bus Stop: Teknik Informatika ITS [4 penumpang]");

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                if (marker.getTitle().toString().toLowerCase().contains("stop")) {
                    Toast.makeText(PengemudiActivity.this, marker.getTitle().toString(), Toast.LENGTH_LONG).show();
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
