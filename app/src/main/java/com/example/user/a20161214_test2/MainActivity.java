package com.example.user.a20161214_test2;

import android.location.Location;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity
        implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener
{
    protected GoogleApiClient mGoogleApiClient;
    protected Location mLastLocation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();

    }

    @Override
    public void onConnected(Bundle bundle) {
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (mLastLocation != null)
        {
            Log.d("LOC", "" + mLastLocation.getLatitude() + "," + mLastLocation.getLongitude());
        }
        Location trainStation = new Location("MY");
        trainStation.setLatitude(24.953);
        trainStation.setLongitude(121.225);

        Float distance = mLastLocation.distanceTo(trainStation);
        Log.d("LOC", "中壢火車站距離:" + distance);

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    public void clickFood(View v)
    {
        Log.d("LOC", "Start Search Food");
        RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
        StringRequest request = new StringRequest(
                "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=" + mLastLocation.getLatitude() +  "," + mLastLocation.getLongitude() + "&radius=500&types=food&key=AIzaSyCycInR6Zqm9ZsspuN6734D5pBizxEPsM0"
                , new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject obj = new JSONObject(response);
                    JSONArray array = obj.getJSONArray("results");
                    for (int i=0; i < array.length(); i++)
                    {
                        JSONObject detail = array.getJSONObject(i);
                        String n = detail.getString("name");
                        Log.d("LOC", "附近食物:" + n);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }
        );
        queue.add(request);
        queue.start();
    }

}