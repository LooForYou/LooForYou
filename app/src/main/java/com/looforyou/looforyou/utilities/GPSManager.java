package com.looforyou.looforyou.utilities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.looforyou.looforyou.activities.MapActivity;

import java.util.List;

/**
 * Created by ibreaker on 4/13/2018.
 */

public class GPSManager {

    final String LOCATION_TAG = "Location error";
    private Context context;
    private GoogleApiClient googleApiClient;

    public GPSManager(Context context) {
        this.context = context;
    }

    public void checkGPS(){
        final LocationManager manager = (LocationManager) ((Activity)context).getSystemService(Context.LOCATION_SERVICE);
        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER) && hasGPS(context)) {
            enableLocationServices();
        }
    }
    public boolean hasGPS(Context context) {
        final LocationManager manager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        if (manager == null)
            return false;
        final List<String> providers = manager.getAllProviders();
        if (providers == null)
            return false;
        return providers.contains(LocationManager.GPS_PROVIDER);
    }

    public void enableLocationServices() {
        if (googleApiClient == null) {
            googleApiClient = new GoogleApiClient.Builder(context).addApi(LocationServices.API)
                    .addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
                        @Override
                        public void onConnected(Bundle bundle) {
                        }
                        @Override
                        public void onConnectionSuspended(int i) {
                            googleApiClient.connect();
                        }
                    })
                    .addOnConnectionFailedListener(new GoogleApiClient.OnConnectionFailedListener() {
                        @Override
                        public void onConnectionFailed(ConnectionResult connectionResult) {
                            Log.v(LOCATION_TAG, String.valueOf(connectionResult.getErrorCode()));
                        }
                    }).build();
            googleApiClient.connect();

            LocationRequest locationRequest = LocationRequest.create();
            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            locationRequest.setInterval(30000);
            locationRequest.setFastestInterval(5000);
            LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);
            builder.setAlwaysShow(true);

            PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi.checkLocationSettings(googleApiClient, builder.build());
            result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
                @Override
                public void onResult(LocationSettingsResult result) {
                    final Status status = result.getStatus();
                    switch (status.getStatusCode()) {
                        case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                            try {
                                status.startResolutionForResult((Activity) context, 200);
                                ((Activity)context).finish();
                                Intent homeIntent = new Intent(context,((Activity)context).getClass());
                                context.startActivity(homeIntent);
                            } catch (IntentSender.SendIntentException e) {
                                Log.v(LOCATION_TAG,e.getLocalizedMessage());
                            }
                            break;
                    }
                }
            });
        }
    }
}
