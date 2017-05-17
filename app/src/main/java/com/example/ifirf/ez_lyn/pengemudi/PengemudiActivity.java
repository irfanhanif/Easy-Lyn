package com.example.ifirf.ez_lyn.pengemudi;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.location.LocationManager;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.ifirf.ez_lyn.Config;
import com.example.ifirf.ez_lyn.Loading;
import com.example.ifirf.ez_lyn.R;
import com.example.ifirf.ez_lyn.pengemudi.PengemudiActivity;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import android.speech.tts.TextToSpeech;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PengemudiActivity extends FragmentActivity
        implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    TextToSpeech t1;
    private GoogleMap mMap;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private Location mCurrentLocation;
    private boolean followingCamera;
    private boolean enteringApp;
    private boolean isGpsEnabled;
    private LocationManager locationManager;
    private List<Marker> halteMarkers;
    private String TAG = "PengemudiActivity";
    private Map<Marker, Map<String, String>> markerMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pengemudi);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

        mapFragment.getMapAsync(this);

        Loading loading = new Loading(PengemudiActivity.this);
        loading.displayLoading();

        halteMarkers = new ArrayList<>();
        markerMap = new HashMap<Marker, Map<String, String>>();

        locationManager = (LocationManager) PengemudiActivity.this.getSystemService(LOCATION_SERVICE);
        enteringApp = true;

        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }

        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(10000);
        mLocationRequest.setFastestInterval(5000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        android.location.LocationListener locListener = new android.location.LocationListener() {
            @Override
            public void onLocationChanged(Location location) {

            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {
                mGoogleApiClient.connect();
            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };

        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this,
                        Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locListener);

        displayAllHalte();

        loading.hideLoading();

        t1=new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR) {
                    t1.setLanguage(Locale.UK);
                }
            }
        });


    }

    private Marker findHalteNearby(){
        double min = 99999;
        Marker minMarker = null;
        double lat, lng, dist;
        for(int i=0; i<halteMarkers.size(); i++){
            lat = halteMarkers.get(i).getPosition().latitude;
            lng = halteMarkers.get(i).getPosition().longitude;
            dist = getDistance(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude(), lat, lng);
            if(dist<min){
                min = dist;
                minMarker = halteMarkers.get(i);
            }
        }
        return minMarker;
    }

    private double getDistance(double lat1, double lng1, double lat2, double lng2){
        double r = 6371000;
        double currLat1 = Math.toRadians(lat1);
        double currLat2 = Math.toRadians(lat2);
        double deltaLat = Math.toRadians(lat2-lat1);
        double deltaLng = Math.toRadians(lng2-lng1);

        double a = (Math.sin(deltaLat/2) * Math.sin(deltaLat/2)) +
                (Math.cos(currLat1) * Math.cos(currLat2) * Math.sin(deltaLng) * Math.sin(deltaLng));
        double c = Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        double d = r * c;

        Log.d("dist", String.valueOf(d));

        return d;
    }

    private void displayHalteIcon (String response){
        double lat, lng;
        String title;
        LatLng halteLocation;
        Marker halte;
        Bitmap bus_stop_icon;
        try {
            JSONArray jsonArray = new JSONArray(response);
            for(int i=0; i<jsonArray.length(); i++){
                JSONObject jsonObject = jsonArray.getJSONObject(i);

                lat = jsonObject.getDouble("lat_halte");
                lng = jsonObject.getDouble("lng_halte");
                title = jsonObject.getString("nama_halte");

                halteLocation = new LatLng(lat, lng);
                halte = mMap.addMarker(new MarkerOptions().position(halteLocation));
                halte.setTitle(title);
                halte.setSnippet("10 Penumpang");
                halte.setTag(jsonObject.getInt("kode_rute"));
                bus_stop_icon = this.customMarker(R.drawable.halte, 60, 60);
                halte.setIcon(BitmapDescriptorFactory.fromBitmap(bus_stop_icon));

                Map<String, String> markerInfo = new HashMap<String, String>();
                markerInfo.put("nama_halte", jsonObject.getString("nama_halte"));
                markerInfo.put("nama_rute", jsonObject.getString("nama_rute"));
                markerInfo.put("kode_halte", jsonObject.getString("kode_halte"));
                markerInfo.put("kode_rute", jsonObject.getString("kode_rute"));

                halteMarkers.add(halte);
                markerMap.put(halte, markerInfo);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void displayAllHalte(){
        StringRequest stringRequest = new StringRequest(Request.Method.GET, Config.address+"gethalte",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, response);
                        displayHalteIcon(response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Volley Error", error.toString());
            }
        }){
//            @Override
//            protected Map<String, String> getParams() throws AuthFailureError {
//                Map<String, String> params = new HashMap<String, String>();
//                params.put("key", Config.key);
//                params.put("longitude", longitude);
//                params.put("latitude", latitude);
//                params.put("token", token);
//                return params;
//            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(PengemudiActivity.this);
        requestQueue.add(stringRequest);
    }

    private void displaySettingsALert(){
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(PengemudiActivity.this);
        alertDialog.setTitle("GPS Tidak Aktif");
        alertDialog.setMessage("GPS tidak aktif. Apakah Anda ingin mengaktifkan GPS terlebih dahulu?");
        alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
                dialog.cancel();
            }
        });
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        alertDialog.show();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        mMap.getUiSettings().setMapToolbarEnabled(false);
        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(final Marker marker) {
                String toSpeak = "at, "+marker.getTitle()+", There are 10 passengers ahead";
                Toast.makeText(getApplicationContext(), toSpeak,Toast.LENGTH_SHORT).show();
                t1.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "ON RESUME");
        isGpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if(isGpsEnabled) mGoogleApiClient.connect();
        else displaySettingsALert();
    }

    public void onPause(){
        if(t1 !=null){
            t1.stop();
            t1.shutdown();
        }
        super.onPause();
    }

    @Override
    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    private Bitmap customMarker(int source, int height, int width) {
        BitmapDrawable bitmapDrawable = (BitmapDrawable) getResources().getDrawable(source);
        Bitmap bitmap = bitmapDrawable.getBitmap();
        return Bitmap.createScaledBitmap(bitmap, height, width, false);
    }

    /* Override from GoogleApiClient */
    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.d(TAG, "ON CONNECTED");
        if (mCurrentLocation == null) {
            if (ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            mCurrentLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
            updateUI();
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.d(TAG, "On connection suspended");
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d(TAG, "On connection failed");
    }

    /* Location Listener */
    @Override
    public void onLocationChanged(Location location) {
        mCurrentLocation = location;
        Log.d("mCurrentLocation", "Lat: " + String.valueOf(mCurrentLocation.getLatitude()) + ", Lng:" + String.valueOf(mCurrentLocation.getLongitude()));
//        Toast.makeText(PenumpangActivity.this,
//                "Lat: " + String.valueOf(mCurrentLocation.getLatitude()) + ", Lng:" + String.valueOf(mCurrentLocation.getLongitude()),
//                Toast.LENGTH_LONG).show();
        updateUI();
    }

    /* Custom Method */
    private void updateUI() {
        if (mCurrentLocation != null) {
            LatLng current_location = new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude());
            if(enteringApp){
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(current_location, 18));
                enteringApp = false;
            }
        }
    }
}
