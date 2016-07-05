package com.weapp.evan.theweather;


//import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationSettingsStates;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.squareup.picasso.Picasso;
import com.weapp.evan.theweather.entity.Forecast;
import com.weapp.evan.theweather.http.ApplicationController;
import com.weapp.evan.theweather.http.Helpers;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import android.widget.Toast;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
        com.google.android.gms.location.LocationListener, ResultCallback<LocationSettingsResult> {

        TextView cityName;
        TextView updatedTime;
        TextView currentDegree;
        TextView minDegree;
        TextView maxDegree;
        ImageView iconView;
        TextView desc;
        TextView windSpeed;
        TextView humidity;
        TextView pressure;
        ImageView myLocationIcon;
        private static boolean gps_enabled=false;
        private static boolean network_enabled=false;
        static Location location;
        private static double myLat;
        private static double myLong;
        private static String town;
        private static String state;
        private static String country;
        private static String icon;
        static String townName;
        private static final String TAG = MainActivity.class.getName();
        static Context context;
        private RecyclerView mRecyclerView;
        private ForecastAdapter forecastAdapter;
        private static List<Forecast> forecastList;
        public static final String PREFS_NAME = "AOP_PREFS";
        public static final String PREFS_KEY = "AOP_PREFS_String";

        /**
         * Constant used in the location settings dialog.
         */
        protected static final int REQUEST_CHECK_SETTINGS = 0x1;

        /**
         * The desired interval for location updates. Inexact. Updates may be more or less frequent.
         */
        public static final long UPDATE_INTERVAL_IN_MILLISECONDS = 10000;

        /**
         * The fastest rate for active location updates. Exact. Updates will never be more frequent
         * than this value.
         */
        public static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS =
                UPDATE_INTERVAL_IN_MILLISECONDS / 2;

        // Keys for storing activity state in the Bundle.
        protected final static String KEY_REQUESTING_LOCATION_UPDATES = "requesting-location-updates";
        protected final static String KEY_LOCATION = "location";
        protected final static String KEY_LAST_UPDATED_TIME_STRING = "last-updated-time-string";

        /**
         * Provides the entry point to Google Play services.
         */
        protected GoogleApiClient mGoogleApiClient;

        /**
         * Stores parameters for requests to the FusedLocationProviderApi.
         */
        protected LocationRequest mLocationRequest;

        /**
         * Stores the types of location services the client is interested in using. Used for checking
         * settings to determine if the device has optimal location settings.
         */
        protected LocationSettingsRequest mLocationSettingsRequest;

        /**
         * Represents a geographical location.
         */
        protected Location mCurrentLocation;
        protected Boolean mRequestingLocationUpdates;

        @Override
        protected void onStart() {
                super.onStart();
                // Connect the client.
                mGoogleApiClient.connect();
        }

        @Override
        protected void onStop() {
                // Disconnecting the client invalidates it.
                mGoogleApiClient.disconnect();
                super.onStop();
        }


        @Override
        protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_main);
                cityName = (TextView) findViewById(R.id.cityName);
                updatedTime = (TextView) findViewById(R.id.lastUpdatedTime);
                currentDegree = (TextView) findViewById(R.id.current_degree);
                minDegree = (TextView) findViewById(R.id.min_degree);
                maxDegree = (TextView) findViewById(R.id.max_degree);
                iconView = (ImageView) findViewById(R.id.icon);
                desc = (TextView) findViewById(R.id.desc);
                windSpeed = (TextView) findViewById(R.id.wind_value);
                humidity = (TextView) findViewById(R.id.humidity_value);
                pressure = (TextView) findViewById(R.id.pressure_value);
                myLocationIcon = (ImageView) findViewById(R.id.my_location_id);
                forecastList = new ArrayList<>();
                context = this;
                mRecyclerView = (RecyclerView) findViewById(R.id.recycler_viewID);
                mRecyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.context));
                forecastAdapter = new ForecastAdapter(this, forecastList);
                mRecyclerView.setAdapter(forecastAdapter);
                mRequestingLocationUpdates = false;
                startApplication();
                Log.v(TAG, "test");
        }

        /**
         * Updates fields based on data stored in the bundle.
         *
         * @param savedInstanceState The activity state saved in the Bundle.
         */
//        private void updateValuesFromBundle(Bundle savedInstanceState) {
//                if (savedInstanceState != null) {
//                        // Update the value of mRequestingLocationUpdates from the Bundle, and make sure that
//                        // the Start Updates and Stop Updates buttons are correctly enabled or disabled.
//                        if (savedInstanceState.keySet().contains(KEY_REQUESTING_LOCATION_UPDATES)) {
//                                mRequestingLocationUpdates = savedInstanceState.getBoolean(
//                                        KEY_REQUESTING_LOCATION_UPDATES);
//                        }
//
//                        // Update the value of mCurrentLocation from the Bundle and update the UI to show the
//                        // correct latitude and longitude.
//                        if (savedInstanceState.keySet().contains(KEY_LOCATION)) {
//                                // Since KEY_LOCATION was found in the Bundle, we can be sure that mCurrentLocation
//                                // is not null.
//                                mCurrentLocation = savedInstanceState.getParcelable(KEY_LOCATION);
//                        }
//
////                        // Update the value of mLastUpdateTime from the Bundle and update the UI.
////                        if (savedInstanceState.keySet().contains(KEY_LAST_UPDATED_TIME_STRING)) {
////                                mLastUpdateTime = savedInstanceState.getString(KEY_LAST_UPDATED_TIME_STRING);
////                        }
////                        updateUI();
//                }
//        }

        private void startApplication(){
                ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo info = cm.getActiveNetworkInfo();

                if (info != null) {
                        if (!info.isConnected()) {
                                Toast.makeText(this,
                                        "Please check your internet connection and try again.",
                                        Toast.LENGTH_LONG).show();

                        } else{
                                Intent intent = getIntent();
                                String location_name = intent.getStringExtra("location");
                                getMyCurrentLocation();
                                getCity();
                                String endPoint;
                                String endPoint_forecast;

                                if(location_name != null){
                                        StringBuilder city_name = new StringBuilder();
                                        StringBuilder state_name = new StringBuilder();
                                        StringBuilder country_name = new StringBuilder();
                                        String strArray[] = location_name.split("");
                                        for(int i = 0; i < strArray.length; i++){
                                                if(!strArray[i].equals(",")){
                                                        city_name.append(strArray[i]);
                                                }else
                                                        break;
                                        }
                                        //  if(country_name.equals("")){
                                        endPoint = Helpers.BASE_URL + city_name + "," + Helpers.key;
                                        endPoint_forecast = Helpers.FORECAST_URL + city_name + "," + Helpers.FORECAST_LENGTH + Helpers.key;
                                        cityName.setText(city_name);
                                        savePref(context, city_name.toString());
//                                         }else{
//                                                 endPoint = Helpers.BASE_URL + city_name + "," + country_name + Helpers.key;
//                                                 endPoint_forecast = Helpers.FORECAST_URL + city_name + "," + country_name + Helpers.FORECAST_LENGTH + Helpers.key;
//                                                 cityName.setText(city_name + "," + state_name);
//                                                 savePref(context, city_name + "," + state_name);
//                                         }
                                }else if(getPref(context) != null){
                                        String locationName = getPref(context).replaceAll("\\s+", "");
                                        endPoint = Helpers.BASE_URL + locationName + Helpers.key;
                                        endPoint_forecast = Helpers.FORECAST_URL + locationName + Helpers.FORECAST_LENGTH + Helpers.key;
                                        cityName.setText(getPref(context));
                                }else{
                                        // String townName;
                                        townName = town.replaceAll("\\s+", "");
                                        endPoint = Helpers.BASE_URL + townName + "," + country + Helpers.key;
                                        //  getCurrentWeather(endPoint);
                                        endPoint_forecast = Helpers.FORECAST_URL + townName + "," + country + Helpers.FORECAST_LENGTH + Helpers.key;
                                        cityName.setText(town + ", " + state);
                                }
                                getCurrentWeather(endPoint);
                                if (forecastList.isEmpty())
                                        getForecast(endPoint_forecast);
                        }
                }

                myLocationIcon.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                                getMyLocation();
                        }
                });
        }

        private void settingRequest(){
                if (mGoogleApiClient == null) {
                        mGoogleApiClient = new GoogleApiClient.Builder(this)
                                .addApi(LocationServices.API)
                                .addConnectionCallbacks(this)
                                .addOnConnectionFailedListener(this).build();
                        mGoogleApiClient.connect();

                        LocationRequest locationRequest = LocationRequest.create();
                        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
                        locationRequest.setInterval(30 * 1000);
                        locationRequest.setFastestInterval(5 * 1000);
                        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                                .addLocationRequest(locationRequest);

                        //**************************
                        builder.setAlwaysShow(true); //this is the key ingredient
                        //**************************

                        PendingResult<LocationSettingsResult> result =
                                LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient, builder.build());
                        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
                                @Override
                                public void onResult(LocationSettingsResult result) {
                                        final Status status = result.getStatus();
                                        final LocationSettingsStates state = result.getLocationSettingsStates();
                                        switch (status.getStatusCode()) {
                                                case LocationSettingsStatusCodes.SUCCESS:
                                                        // All location settings are satisfied. The client can initialize location
                                                        // requests here.
                                                        break;
                                                case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                                                        // Location settings are not satisfied. But could be fixed by showing the user
                                                        // a dialog.
                                                        try {
                                                                // Show the dialog by calling startResolutionForResult(),
                                                                // and check the result in onActivityResult().
                                                                status.startResolutionForResult(
                                                                        MainActivity.this, REQUEST_CHECK_SETTINGS);
                                                        } catch (IntentSender.SendIntentException e) {
                                                                // Ignore the error.
                                                        }
                                                        break;
                                                case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                                                        // Location settings are not satisfied. However, we have no way to fix the
                                                        // settings so we won't show the dialog.
                                                        break;
                                        }
                                }
                        });             }
        }

//        @Override
//        protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//                switch (requestCode) {
//// Check for the integer request code originally supplied to startResolutionForResult().
//                        case REQUEST_CHECK_SETTINGS:
//                                switch (resultCode) {
//                                        case Activity.RESULT_OK:
//                                                startApplication();
//                                                break;
//                                        case Activity.RESULT_CANCELED:
//                                                settingRequest();//keep asking if imp or do whatever
//                                                break;
//                                }
//                                break;
//                }
//        }

        private void getMyLocation(){
                getMyCurrentLocation();
                getCity();
                String endPoint;
                String endPoint_forecast;
                townName = town.replaceAll("\\s+", "");
                endPoint = Helpers.BASE_URL + townName + "," + country + Helpers.key;
                getCurrentWeather(endPoint);
                endPoint_forecast = Helpers.FORECAST_URL + townName + "," + country + Helpers.FORECAST_LENGTH + Helpers.key;
                cityName.setText(town + ", " + state);
                savePref(context, town + ", " + state);
                getCurrentWeather(endPoint);
                forecastList.clear();
                getForecast(endPoint_forecast);
                Log.v(TAG, "latitude: " + myLat + ", longitude: " + myLong);
        }

        private void getCurrentWeather(String endpoint){
                StringRequest jsonObjRequest = new StringRequest(Request.Method.GET,
                        endpoint,new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                                Log.v(TAG, response + "volley response");
                                try {
                                        getCurrentWeatherData(new JSONObject(response));
                                } catch (JSONException e) {
                                        e.printStackTrace();
                                }
                        }
                }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                                VolleyLog.d("volley", "Error: " + error.getMessage());
                                error.printStackTrace();
                                Toast.makeText(getBaseContext(), "Oops! Something went wrong", Toast.LENGTH_LONG).show();
                        }
                }) {
                        @Override
                        public String getBodyContentType() {
                                return "application/x-www-form-urlencoded; charset=UTF-8";
                        }

                        @Override
                        public Map<String, String> getHeaders() throws AuthFailureError {
                                HashMap<String, String> headers = new HashMap<String, String>();
                                return headers;
                        }

                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                                Map<String, String> params = new HashMap<String, String>();
                                return params;
                        }

                };
                ApplicationController.getInstance().addToRequestQueue(jsonObjRequest);

        }

        private void getCurrentWeatherData(JSONObject jsonObject) {

                double temp;
                double temp_min;
                double temp_max;
                JSONObject weatherMainObject;
                JSONObject weatheWindObject;
                long dt;
                long unixTime = System.currentTimeMillis() / 1000L;
                String description;
                float speed;
                int pressure_value;
                int humidity_value;

                try {
                        weatherMainObject = jsonObject.getJSONObject("main");
                        weatheWindObject = jsonObject.getJSONObject("wind");
                        JSONArray jsonArray = jsonObject.getJSONArray("weather");
                        JSONObject weatherObject = jsonArray.getJSONObject(0);
                        icon = weatherObject.getString("icon");
                        description = weatherObject.getString("description");
                        speed = weatheWindObject.getLong("speed");
                        //speed from m/s to km/h
                        speed = (speed * 3600) /1000;
                        pressure_value = weatherMainObject.getInt("pressure");
                        humidity_value = weatherMainObject.getInt("humidity");

                        if(icon.equals("01d")) {
                                Picasso.with(context).load(R.drawable.sunny).into(iconView);
                        }else if(icon.equals("02d")) {
                                Picasso.with(context).load(R.drawable.dfewcloud).into(iconView);
                        }else if(icon.equals("03d")) {
                                Picasso.with(context).load(R.drawable.clouds).into(iconView);
                        }else if(icon.equals("04d")) {
                                Picasso.with(context).load(R.drawable.clouds).into(iconView);
                        }else if(icon.equals("09d")) {
                                Picasso.with(context).load(R.drawable.rain).into(iconView);
                        }else if(icon.equals("10d")) {
                                Picasso.with(context).load(R.drawable.rain).into(iconView);
                        }else if(icon.equals("11d")) {
                                Picasso.with(context).load(R.drawable.dstorm).into(iconView);
                        }else if(icon.equals("13d")) {
                                Picasso.with(context).load(R.drawable.dsnowing).into(iconView);
                        }else if(icon.equals("50d")) {
                                Picasso.with(context).load(R.drawable.dhaze).into(iconView);
                        }else if(icon.equals("01n")) {
                                Picasso.with(context).load(R.drawable.moon).into(iconView);
                        }else if(icon.equals("02n")) {
                                Picasso.with(context).load(R.drawable.nfewcloud).into(iconView);
                        }else if(icon.equals("03n")) {
                                Picasso.with(context).load(R.drawable.clouds).into(iconView);
                        }else if(icon.equals("04n")) {
                                Picasso.with(context).load(R.drawable.clouds).into(iconView);
                        }else if(icon.equals("09n")) {
                                Picasso.with(context).load(R.drawable.rain).into(iconView);
                        }else if(icon.equals("10n")) {
                                Picasso.with(context).load(R.drawable.rain).into(iconView);
                        }else if(icon.equals("11n")) {
                                Picasso.with(context).load(R.drawable.nstorm).into(iconView);
                        }else if(icon.equals("13n")) {
                                Picasso.with(context).load(R.drawable.nsnow).into(iconView);
                        }else if(icon.equals("50n")) {
                                Picasso.with(context).load(R.drawable.nhaze).into(iconView);
                        }

                        Log.v(TAG, weatherObject + "weather object value");
                        temp = weatherMainObject.getDouble("temp");
                        temp_min = weatherMainObject.getDouble("temp_min");
                        temp_max = weatherMainObject.getDouble("temp_max");
                        temp = temp - 273.15;
                        temp_min = temp_min - 273.15;
                        temp_max = temp_max - 273.15;
                        currentDegree.setText(Math.round(temp) + "°");
                        maxDegree.setText(Math.round(temp_max) + "°" );
                        minDegree.setText(Math.round(temp_min) + "°");
                        dt = jsonObject.getLong("dt");
                        updatedTime.setText(Math.round((unixTime - dt) * 0.0166667)  + " mins ago");
                        desc.setText(description);
                        windSpeed.setText(speed + "km/h");
                        pressure.setText(Math.round(pressure_value / 10) + "kPa");
                        humidity.setText(humidity_value + "%");


                } catch (JSONException e) {
                        Toast.makeText(this, "data not found", Toast.LENGTH_LONG).show();
                }
        }

        private void getForecast(String endpoint_forecast){
                StringRequest jsonObjRequest = new StringRequest(Request.Method.GET,
                        endpoint_forecast,new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                                Log.v(TAG, response + "forecast response");
                                try {
                                        getForecastData(new JSONObject(response));
                                } catch (JSONException e) {
                                        e.printStackTrace();
                                }
                        }
                }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                                VolleyLog.d("volley", "Error: " + error.getMessage());
                                error.printStackTrace();
                                Toast.makeText(getBaseContext(), "Oops! Something went wrong", Toast.LENGTH_LONG).show();
                        }
                }) {
                        @Override
                        public String getBodyContentType() {
                                return "application/x-www-form-urlencoded; charset=UTF-8";
                        }

                        @Override
                        public Map<String, String> getHeaders() throws AuthFailureError {
                                HashMap<String, String> headers = new HashMap<String, String>();
                                return headers;
                        }

                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                                Map<String, String> params = new HashMap<String, String>();
                                return params;
                        }

                };
                ApplicationController.getInstance().addToRequestQueue(jsonObjRequest);

        }

        private void getForecastData(JSONObject jsonObject) {
                Forecast forecast;
                long dt;
                String date_to_week = null;
                String icon_forecast;
                int max;
                int min;
                int day_of_week;
                JSONArray jsonArray = null;
                try {
                        jsonArray = jsonObject.getJSONArray("list");
                        for (int i = 1; i < jsonArray.length(); i++) {
                                JSONObject dataObject = jsonArray.getJSONObject(i);
                                JSONObject tempObject = dataObject.getJSONObject("temp");
                                JSONArray weatherArray = dataObject.getJSONArray("weather");
                                dt = dataObject.getLong("dt");
                                //start converting dt to day of week
                                long timestamp = dt * 1000l;
                                Calendar cal = Calendar.getInstance();
                                cal.setTimeInMillis(timestamp);
                                day_of_week = cal.get(Calendar.DAY_OF_WEEK);
                                switch (day_of_week){
                                        case 1:
                                                date_to_week = "Sunday";
                                                break;
                                        case 2:
                                                date_to_week = "Monday";
                                                break;
                                        case 3:
                                                date_to_week = "Tuesday";
                                                break;
                                        case 4:
                                                date_to_week = "Wednesday";
                                                break;
                                        case 5:
                                                date_to_week = "Thursday";
                                                break;
                                        case 6:
                                                date_to_week = "Friday";
                                                break;
                                        case 7:
                                                date_to_week = "Saturday";
                                                break;
                                }//end

                                max = (int) Math.round(tempObject.getDouble("max"));
                                min = (int) Math.round(tempObject.getDouble("min"));
                                icon_forecast = weatherArray.getJSONObject(0).getString("icon");

                                forecast = new Forecast(date_to_week, icon_forecast, max, min);
                                forecastList.add(forecast);
                        }
                        forecastAdapter.notifyDataSetChanged();
                } catch (JSONException e) {
                        e.printStackTrace();
                }


        }

        private void getCity(){
                Geocoder myLocation = new Geocoder(getApplicationContext(), Locale.getDefault());
                try {
                        List<Address> myList = myLocation.getFromLocation(myLat, myLong, 1);
                        String result;

                        if (myList != null && myList.size() > 0) {
                                Address address = myList.get(0);
                            //    String streetName = address.getAddressLine(0);
                                town = address.getLocality();
                                state = address.getAdminArea();
                                country = address.getCountryName();
                                if (town == null) {
                                        town = address.getSubLocality();
                                }
                                //show game detail address above map when user click on the map
                        }
                } catch (IOException e) {
                        Toast.makeText(this, "city not found", Toast.LENGTH_LONG).show();
                }
        }

        //save SharedPreference
        public void savePref(Context context, String text) {
                SharedPreferences settings;
                SharedPreferences.Editor editor;
                settings = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE); //1
                editor = settings.edit(); //2

                editor.putString(PREFS_KEY, text); //3
                editor.commit(); //4
        }

        //get SharedPreference
        public String getPref(Context context) {
                SharedPreferences settings;
                String text;
                settings = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE); //1
                text = settings.getString(PREFS_KEY, null); //2
                return text;
        }


        /**
         * Check the type of GPS Provider available at that instance and
         * collect the location information
         *
         * @Output Latitude and Longitude
         */
        public static void getMyCurrentLocation() {

                LocationManager locManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
                LocationListener locListener = new MyLocationListener();
                Log.wtf("Location", "Start");

                if ( Build.VERSION.SDK_INT >= 23 &&
                        ContextCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION ) != PackageManager.PERMISSION_GRANTED &&
                        ContextCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        return;
                }


                try {
                        gps_enabled = locManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
                } catch (Exception ex) {
                        ex.printStackTrace();
                }
                try {
                        network_enabled = locManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
                } catch (Exception ex) {
                        ex.printStackTrace();
                }

                if (gps_enabled) {
                        locManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locListener);
                }

                if (gps_enabled) {
                        location = locManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                }

                if (network_enabled && location == null) {
                        locManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locListener);
                }

                if (network_enabled && location == null) {
                        location = locManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                }

                if (location != null) {

                        myLat = location.getLatitude();
                        myLong = location.getLongitude();
                } else {
                        Location loc = getLastKnownLocation(context);
                        if (loc != null) {
                                myLat = loc.getLatitude();
                                myLong = loc.getLongitude();
                        }
                }
                locManager.removeUpdates((android.location.LocationListener) locListener);
        }

        @Override
        public void onConnected(@Nullable Bundle bundle) {

        }

        @Override
        public void onConnectionSuspended(int i) {

        }

        @Override
        public void onLocationChanged(Location location) {

        }



        @Override
        public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

        }

        @Override
        public void onResult(@NonNull LocationSettingsResult locationSettingsResult) {

        }

        // Location listener class. to get location.
        public static class MyLocationListener implements LocationListener {
                public void onLocationChanged(Location location) {
                        if (location != null) {
                                myLat = location.getLatitude();
                                myLong = location.getLongitude();
                        }
                }

                public void onProviderDisabled(String provider) {
                        // TODO Auto-generated method stub
                }

                public void onProviderEnabled(String provider) {
                        // TODO Auto-generated method stub
                }

                public void onStatusChanged(String provider, int status, Bundle extras) {
                        // TODO Auto-generated method stub
                }
        }

        public static Location getLastKnownLocation(Context context){
                Location location = null;
                LocationManager locationmanager = (LocationManager)context.getSystemService(context.LOCATION_SERVICE);

                if ( Build.VERSION.SDK_INT >= 23 &&
                        ContextCompat.checkSelfPermission( context, android.Manifest.permission.ACCESS_FINE_LOCATION ) != PackageManager.PERMISSION_GRANTED &&
                        ContextCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        //return;
                }

                List list = locationmanager.getAllProviders();
                boolean i = false;
                Iterator iterator = list.iterator();
                do
                {
                        if(!iterator.hasNext())
                                break;
                        String s = (String)iterator.next();

                        if(i != false && !locationmanager.isProviderEnabled(s))
                                continue;

                        Location location1 = locationmanager.getLastKnownLocation(s);

                        if(location1 == null)
                                continue;
                        if(location != null)
                        {
                                float f = location.getAccuracy();
                                float f1 = location1.getAccuracy();
                                if(f >= f1)
                                {
                                        long l = location1.getTime();
                                        long l1 = location.getTime();
                                        if(l - l1 <= 600000L)
                                                continue;
                                }
                        }
                        location = location1;
                        i = locationmanager.isProviderEnabled(s);
                } while(true);
                return location;
        }

        @Override
        public boolean onCreateOptionsMenu(Menu menu) {
                getMenuInflater().inflate(R.menu.menu_main, menu);
                return true;
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
                Intent i = new Intent(context, EnterLocationActivity.class);
                startActivity(i);
                finish();
                return super.onOptionsItemSelected(item);
        }

        @Override
        public void onBackPressed() {
                super.onBackPressed();
                finish();
        }

        @Override
        protected void onResume() {
                super.onResume();
        }
}


