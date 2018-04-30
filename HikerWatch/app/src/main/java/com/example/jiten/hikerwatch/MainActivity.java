package com.example.jiten.hikerwatch;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    LocationManager locationManager ;
    LocationListener locationListener ;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(grantResults.length > 0 && grantResults[0]==PackageManager.PERMISSION_GRANTED ){
            if(ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION == PackageManager.PERMISSION_GRANTED)){
                locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

            }
        }
    }

    public void updateLocationInfo(Location location) {
        locationManager.requestLocationUpdates(locationManager.GPS_PROVIDER, 0, 0, locationListener);

        TextView latTextView = findViewById(R.id.latTextView);
        TextView longTextView = findViewById(R.id.longTextView);
        TextView accTextView = findViewById(R.id.accTextView);
        TextView altiTextView = findViewById(R.id.altiTextView);
        TextView  addTextView = findViewById(R.id.addressTextView);
        latTextView.setText("Latitude: " +location.getLatitude());
        longTextView.setText("Longitutde: " + location.getLongitude());
        accTextView.setText("Accuracy: " + location.getAccuracy());
        altiTextView.setText("Altitude: " + location.getAltitude());

        // for geetting the address
        Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
        try{
            String address = "Could not Find the place";
            List<Address> addressList = geocoder.getFromLocation(location.getLatitude(),location.getLongitude(),1);
            if(addressList!=null && addressList.size()>0){
                Log.i("Place Info: ",addressList.get(0).toString());
                address ="Address \n";
                if(addressList.get(0).getSubThoroughfare()!=null){
                    address += addressList.get(0).getSubThoroughfare();
                }
                if(addressList.get(0).getThoroughfare()!=null){
                    address += addressList.get(0).getThoroughfare();
                }
                if(addressList.get(0).getCountryName()!=null){
                    address += addressList.get(0).getCountryName();
                }
                if(addressList.get(0).getPostalCode()!=null){
                    address += addressList.get(0).getPostalCode();
                }
                addTextView.setText(address);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                Log.i("Location",location.toString());
                updateLocationInfo(location);
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
        };
        if(Build.VERSION.SDK_INT < 23){
            locationManager.requestLocationUpdates(locationManager.GPS_PROVIDER,0,0,locationListener);

        }else{
            if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION != PackageManager.PERMISSION_GRANTED)){

                ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);
            }else{
                locationManager.requestLocationUpdates(locationManager.GPS_PROVIDER,0,0,locationListener);
                Location location = locationManager.getLastKnownLocation(locationManager.GPS_PROVIDER);
                if(location!=null){
                    updateLocationInfo(location);
                }

            }
        }
    }
}
