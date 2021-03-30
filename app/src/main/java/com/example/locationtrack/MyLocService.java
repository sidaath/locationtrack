package com.example.locationtrack;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.Nullable;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;

public class MyLocService extends Service {
    double x,y; //for lat and longitude
    String name;
    FusedLocationProviderClient fusedLocationProviderClient;
    LocationCallback locationCallback;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        fusedLocationProviderClient = new FusedLocationProviderClient(this);
        //Log.d("name is", name);
        locationCallback = new LocationCallback(){
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                //what to do with location data
                Log.d("locs", "lat is "+locationResult.getLastLocation().getLatitude()+
                        "long is "+locationResult.getLastLocation().getLongitude());
                x = locationResult.getLastLocation().getLatitude();
                String latitude = Double.toString(x);
                y=locationResult.getLastLocation().getLongitude();
                String longitude=Double.toString(y);
                LocationEnterBackground bg = new LocationEnterBackground();
                bg.execute(name, latitude, longitude);
            }
        };
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        name = intent.getStringExtra("name");
        requestLocationData();
        return super.onStartCommand(intent, flags, startId);
    }


    public void requestLocationData(){
        LocationRequest request = new LocationRequest();
        request.setInterval(5000);
        request.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        fusedLocationProviderClient.requestLocationUpdates(request, locationCallback, Looper.myLooper());
    }
}
