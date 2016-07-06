package com.weapp.evan.theweather;


//import android.app.ActionBar;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Geocoder;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.location.LocationSettingsStates;
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
import java.util.List;
import java.util.Locale;
import java.util.Map;
import android.widget.Toast;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;

public class MainActivity extends AppCompatActivity implements
        ConnectionCallbacks,
        OnConnectionFailedListener,
        com.google.android.gms.location.LocationListener,
        ResultCallback<LocationSettingsResult> {

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

        private static double myLat;
        private static double myLong;
        private static String town;
        private static String state;
        private static String country;
        private static String icon;
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

        /**
         * Tracks the status of the location updates request. Value changes when the user presses the
         * Start Updates and Stop Updates buttons.
         */
        protected Boolean mRequestingLocationUpdates;

        /**
         * Time when the location was updated represented as a String.
         */

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

                // Update values using data stored in the Bundle.
                updateValuesFromBundle(savedInstanceState);

                // Kick off the process of building the GoogleApiClient, LocationRequest, and
                // LocationSettingsRequest objects.
                buildGoogleApiClient();
                createLocationRequest();
                buildLocationSettingsRequest();
                checkLocationSettings();

                // startApplication();
        }

        /**
         * Updates fields based on data stored in the bundle.
         *
         * @param savedInstanceState The activity state saved in the Bundle.
         */
        private void updateValuesFromBundle(Bundle savedInstanceState) {
                if (savedInstanceState != null) {
                        // Update the value of mRequestingLocationUpdates from the Bundle, and make sure that
                        // the Start Updates and Stop Updates buttons are correctly enabled or disabled.
                        if (savedInstanceState.keySet().contains(KEY_REQUESTING_LOCATION_UPDATES)) {
                                mRequestingLocationUpdates = savedInstanceState.getBoolean(
                                        KEY_REQUESTING_LOCATION_UPDATES);
                        }

                        // Update the value of mCurrentLocation from the Bundle and update the UI to show the
                        // correct latitude and longitude.
                        if (savedInstanceState.keySet().contains(KEY_LOCATION)) {
                                // Since KEY_LOCATION was found in the Bundle, we can be sure that mCurrentLocation
                                // is not null.
                                mCurrentLocation = savedInstanceState.getParcelable(KEY_LOCATION);
                        }
                        updateUI();
                }
        }

        /**
         * Builds a GoogleApiClient. Uses the {@code #addApi} method to request the
         * LocationServices API.
         */
        protected synchronized void buildGoogleApiClient() {
                Log.i(TAG, "Building GoogleApiClient");
                mGoogleApiClient = new GoogleApiClient
                        .Builder(this)
                        .addConnectionCallbacks(this)
                        .addOnConnectionFailedListener(this)
                        .addApi(LocationServices.API)
                        .build();

            //    locationChecker(mGoogleApiClient, this);
        }

        /**
         * Sets up the location request. Android has two location request settings:
         * {@code ACCESS_COARSE_LOCATION} and {@code ACCESS_FINE_LOCATION}. These settings control
         * the accuracy of the current location. This sample uses ACCESS_FINE_LOCATION, as defined in
         * the AndroidManifest.xml.
         * <p/>
         * When the ACCESS_FINE_LOCATION setting is specified, combined with a fast update
         * interval (5 seconds), the Fused Location Provider API returns location updates that are
         * accurate to within a few feet.
         * <p/>
         * These settings are appropriate for mapping applications that show real-time location
         * updates.
         */
        protected void createLocationRequest() {
                mLocationRequest = new LocationRequest();

                // Sets the desired interval for active location updates. This interval is
                // inexact. You may not receive updates at all if no location sources are available, or
                // you may receive them slower than requested. You may also receive updates faster than
                // requested if other applications are requesting location at a faster interval.
                mLocationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);

                // Sets the fastest rate for active location updates. This interval is exact, and your
                // application will never receive updates faster than this value.
                mLocationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);

                mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        }

        /**
         * Uses a {@link com.google.android.gms.location.LocationSettingsRequest.Builder} to build
         * a {@link com.google.android.gms.location.LocationSettingsRequest} that is used for checking
         * if a device has the needed location settings.
         */
        protected void buildLocationSettingsRequest() {
                LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
                builder.addLocationRequest(mLocationRequest);
                builder.setAlwaysShow(true);
                mLocationSettingsRequest = builder.build();


        }

        /**
         * Check if the device's location settings are adequate for the app's needs using the
         * {@link com.google.android.gms.location.SettingsApi#checkLocationSettings(GoogleApiClient,
         * LocationSettingsRequest)} method, with the results provided through a {@code PendingResult}.
         */
        protected void checkLocationSettings() {
                PendingResult<LocationSettingsResult> result =
                        LocationServices.SettingsApi.checkLocationSettings(
                                mGoogleApiClient,
                                mLocationSettingsRequest
                        );
                result.setResultCallback(this);
        }

        /**
         * The callback invoked when
         * {@link com.google.android.gms.location.SettingsApi#checkLocationSettings(GoogleApiClient,
         * LocationSettingsRequest)} is called. Examines the
         * {@link com.google.android.gms.location.LocationSettingsResult} object and determines if
         * location settings are adequate. If they are not, begins the process of presenting a location
         * settings dialog to the user.
         */
        @Override
        public void onResult(LocationSettingsResult locationSettingsResult) {
                final Status status = locationSettingsResult.getStatus();
                switch (status.getStatusCode()) {
                        case LocationSettingsStatusCodes.SUCCESS:
                                Log.i(TAG, "All location settings are satisfied.");
                                if(mCurrentLocation == null)
                                           Toast.makeText(this,
                                                        "Please turn on Location permission for the app.",
                                                        Toast.LENGTH_LONG).show();
                                startLocationUpdates();
                                break;
                        case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                                Log.i(TAG, "Location settings are not satisfied. Show the user a dialog to" +
                                        "upgrade location settings ");

                                try {
                                        // Show the dialog by calling startResolutionForResult(), and check the result
                                        // in onActivityResult().
                                        status.startResolutionForResult(MainActivity.this, REQUEST_CHECK_SETTINGS);
                                } catch (IntentSender.SendIntentException e) {
                                        Log.i(TAG, "PendingIntent unable to execute request.");
                                }
                                break;
                        case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                                Log.i(TAG, "Location settings are inadequate, and cannot be fixed here. Dialog " +
                                        "not created.");
                                break;
                }
        }

        @Override
        protected void onActivityResult(int requestCode, int resultCode, Intent data) {
                switch (requestCode) {
                        // Check for the integer request code originally supplied to startResolutionForResult().
                        case REQUEST_CHECK_SETTINGS:
                                switch (resultCode) {
                                        case Activity.RESULT_OK:
                                                Log.i(TAG, "User agreed to make required location settings changes.");
                                                startLocationUpdates();
                                                break;
                                        case Activity.RESULT_CANCELED:
                                                Log.i(TAG, "User chose not to make required location settings changes.");
                                                break;
                                }
                                break;
                }
        }

        /**
         * Handles the Start Updates button and requests start of location updates. Does nothing if
         * updates have already been requested.
         */
        public void startUpdatesButtonHandler(View view) {
                checkLocationSettings();
        }

        /**
         * Requests location updates from the FusedLocationApi.
         */
        protected void startLocationUpdates() {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        return;
                }
                LocationServices.FusedLocationApi.requestLocationUpdates(
                        mGoogleApiClient,
                        mLocationRequest,
                        this
                ).setResultCallback(new ResultCallback<Status>() {
                        @Override
                        public void onResult(Status status) {
                                mRequestingLocationUpdates = true;
                                //  setButtonsEnabledState();
                        }
                });

        }

        /**
         * Updates all UI fields.
         */
        private void updateUI() {
                updateLocationUI();
        }

        /**
         * Sets the value of the UI fields for the location latitude, longitude and last update time.
         */
        private void updateLocationUI() {
                if (mCurrentLocation != null) {
                      //  cityName.setText(mCurrentLocation.getLatitude() + ", " + mCurrentLocation.getLongitude());
//                        forecastList.clear();
//                        startApplication();
                }
        }

        /**
         * Removes location updates from the FusedLocationApi.
         */
        protected void stopLocationUpdates() {
                // It is a good practice to remove location requests when the activity is in a paused or
                // stopped state. Doing so helps battery performance and is especially
                // recommended in applications that request frequent location updates.
                LocationServices.FusedLocationApi.removeLocationUpdates(
                        mGoogleApiClient,
                        this
                ).setResultCallback(new ResultCallback<Status>() {
                        @Override
                        public void onResult(Status status) {
                                mRequestingLocationUpdates = false;
                                //   setButtonsEnabledState();
                        }
                });
        }


        private void startApplication() {
                ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo info = cm.getActiveNetworkInfo();

                if (info != null) {
                        if (!info.isConnected()) {
                                Toast.makeText(this,
                                        "Please check your internet connection and try again.",
                                        Toast.LENGTH_LONG).show();

                        } else {
                                Intent intent = getIntent();
                                String location_name = intent.getStringExtra("location");
                                getCity();
                                String endPoint;
                                String endPoint_forecast;

                                if (location_name != null) {
                                        StringBuilder city_name = new StringBuilder();
                                        StringBuilder state_name = new StringBuilder();
                                        StringBuilder country_name = new StringBuilder();
                                        String strArray[] = location_name.split("");
                                        for (int i = 0; i < strArray.length; i++) {
                                                if (!strArray[i].equals(",")) {
                                                        city_name.append(strArray[i]);
                                                } else
                                                        break;
                                        }

                                        if(location_name.indexOf(",") >= 0) {
                                                for (int i = strArray.length - 1; i >= 0; i--) {
                                                        if (!strArray[i].equals(",")) {
                                                                country_name.append(strArray[i]);
                                                        } else
                                                                break;
                                                }
                                        }
                                        if(country_name != null) {
                                                endPoint = Helpers.BASE_URL + city_name + "," + country_name + Helpers.key;
                                                endPoint_forecast = Helpers.FORECAST_URL + city_name + "," + country_name + Helpers.FORECAST_LENGTH + Helpers.key;
                                        }else{
                                                endPoint = Helpers.BASE_URL + city_name  + Helpers.key;
                                                endPoint_forecast = Helpers.FORECAST_URL + city_name  + Helpers.FORECAST_LENGTH + Helpers.key;

                                        }
                                        cityName.setText(city_name);
                                        savePref(context, city_name.toString());
                                } else if (getPref(context) != null) {
                                        String locationName = getPref(context).replaceAll("\\s+", "");
                                        endPoint = Helpers.BASE_URL + locationName + Helpers.key;
                                        endPoint_forecast = Helpers.FORECAST_URL + locationName + Helpers.FORECAST_LENGTH + Helpers.key;
                                        cityName.setText(getPref(context));
                                } else {
                                        String townName;
                                        townName = town.replaceAll("\\s+", "");
                                        endPoint = Helpers.BASE_URL + townName + "," + country + Helpers.key;
                                        //  getCurrentWeather(endPoint);
                                        endPoint_forecast = Helpers.FORECAST_URL + townName + "," + country + Helpers.FORECAST_LENGTH + Helpers.key;
                                        cityName.setText(town);
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

        private void getMyLocation() {
                getCity();
                String endPoint;
                String endPoint_forecast;
                String townName;
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

        private void getCurrentWeather(String endpoint) {
                StringRequest jsonObjRequest = new StringRequest(Request.Method.GET,
                        endpoint, new Response.Listener<String>() {
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
                JSONObject weatherWindObject;
                long dt;
                long unixTime = System.currentTimeMillis() / 1000L;
                String description;
                float speed;
                int pressure_value;
                int humidity_value;

                try {
                        weatherMainObject = jsonObject.getJSONObject("main");
                        weatherWindObject = jsonObject.getJSONObject("wind");
                        JSONArray jsonArray = jsonObject.getJSONArray("weather");
                        JSONObject weatherObject = jsonArray.getJSONObject(0);
                        icon = weatherObject.getString("icon");
                        description = weatherObject.getString("description");
                        speed = weatherWindObject.getLong("speed");
                        //speed from m/s to km/h
                        speed = (speed * 3600) / 1000;
                        pressure_value = weatherMainObject.getInt("pressure");
                        humidity_value = weatherMainObject.getInt("humidity");

                        if (icon.equals("01d")) {
                                Picasso.with(context).load(R.drawable.sunny).into(iconView);
                        } else if (icon.equals("02d")) {
                                Picasso.with(context).load(R.drawable.dfewcloud).into(iconView);
                        } else if (icon.equals("03d")) {
                                Picasso.with(context).load(R.drawable.clouds).into(iconView);
                        } else if (icon.equals("04d")) {
                                Picasso.with(context).load(R.drawable.clouds).into(iconView);
                        } else if (icon.equals("09d")) {
                                Picasso.with(context).load(R.drawable.rain).into(iconView);
                        } else if (icon.equals("10d")) {
                                Picasso.with(context).load(R.drawable.rain).into(iconView);
                        } else if (icon.equals("11d")) {
                                Picasso.with(context).load(R.drawable.dstorm).into(iconView);
                        } else if (icon.equals("13d")) {
                                Picasso.with(context).load(R.drawable.dsnowing).into(iconView);
                        } else if (icon.equals("50d")) {
                                Picasso.with(context).load(R.drawable.dhaze).into(iconView);
                        } else if (icon.equals("01n")) {
                                Picasso.with(context).load(R.drawable.moon).into(iconView);
                        } else if (icon.equals("02n")) {
                                Picasso.with(context).load(R.drawable.nfewcloud).into(iconView);
                        } else if (icon.equals("03n")) {
                                Picasso.with(context).load(R.drawable.clouds).into(iconView);
                        } else if (icon.equals("04n")) {
                                Picasso.with(context).load(R.drawable.clouds).into(iconView);
                        } else if (icon.equals("09n")) {
                                Picasso.with(context).load(R.drawable.rain).into(iconView);
                        } else if (icon.equals("10n")) {
                                Picasso.with(context).load(R.drawable.rain).into(iconView);
                        } else if (icon.equals("11n")) {
                                Picasso.with(context).load(R.drawable.nstorm).into(iconView);
                        } else if (icon.equals("13n")) {
                                Picasso.with(context).load(R.drawable.nsnow).into(iconView);
                        } else if (icon.equals("50n")) {
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
                        maxDegree.setText(Math.round(temp_max) + "°");
                        minDegree.setText(Math.round(temp_min) + "°");
                        dt = jsonObject.getLong("dt");
                        updatedTime.setText(Math.round((unixTime - dt) * 0.0166667) + " mins ago");
                        desc.setText(description);
                        windSpeed.setText(speed + "km/h");
                        pressure.setText(Math.round(pressure_value / 10) + "kPa");
                        humidity.setText(humidity_value + "%");


                } catch (JSONException e) {
                        Toast.makeText(this, "data not found", Toast.LENGTH_LONG).show();
                }
        }

        private void getForecast(String endpoint_forecast) {
                StringRequest jsonObjRequest = new StringRequest(Request.Method.GET,
                        endpoint_forecast, new Response.Listener<String>() {
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
                                switch (day_of_week) {
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

        private void getCity() {
                Geocoder myLocation = new Geocoder(getApplicationContext(), Locale.getDefault());
                try {
                        List<android.location.Address> myList = myLocation.getFromLocation(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude(), 1);

                        if (myList != null && myList.size() > 0) {
                                android.location.Address address = myList.get(0);
                                //    String streetName = address.getAddressLine(0);
                                town = address.getLocality();
                                state = address.getAdminArea();
                                country = address.getCountryName();
                                if (town == null) {
                                        town = address.getSubLocality();
                                }
                               Log.v(TAG, "town: " + town + " state: " + state + " country: " + country);
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
        protected void onStart() {
                super.onStart();
                mGoogleApiClient.connect();
        }

        @Override
        protected void onResume() {
                super.onResume();
                // Within {@code onPause()}, we pause location updates, but leave the
                // connection to GoogleApiClient intact.  Here, we resume receiving
                // location updates if the user has requested them.
                if (mGoogleApiClient.isConnected() && mRequestingLocationUpdates) {
                        startLocationUpdates();
                }
        }

        @Override
        protected void onPause() {
                super.onPause();
                // Stop location updates to save battery, but don't disconnect the GoogleApiClient object.
                if (mGoogleApiClient.isConnected()) {
                        stopLocationUpdates();
                }
        }

        @Override
        protected void onStop() {
                super.onStop();
                mGoogleApiClient.disconnect();
        }

        /**
         * Runs when a GoogleApiClient object successfully connects.
         */
        @Override
        public void onConnected(@Nullable Bundle bundle) {
                Log.i(TAG, "Connected to GoogleApiClient");

                // If the initial location was never previously requested, we use
                // FusedLocationApi.getLastLocation() to get it. If it was previously requested, we store
                // its value in the Bundle and check for it in onCreate(). We
                // do not request it again unless the user specifically requests location updates by pressing
                // the Start Updates button.
                //
                // Because we cache the value of the initial location in the Bundle, it means that if the
                // user launches the activity,
                // moves to a new location, and then changes the device orientation, the original location
                // is displayed as the activity is re-created.
                if (mCurrentLocation == null) {
                        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                                // TODO: Consider calling
                                //    ActivityCompat#requestPermissions
                                // here to request the missing permissions, and then overriding
                                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                //                                          int[] grantResults)
                                // to handle the case where the user grants the permission. See the documentation
                                // for ActivityCompat#requestPermissions for more details.
                                return;
                        }
                        mCurrentLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
                        updateLocationUI();
                        startApplication();
                }
        }

        @Override
        public void onConnectionSuspended(int i) {
                Log.i(TAG, "Connection suspended");
        }

        /**
         * Callback that fires when the location changes.
         */
        @Override
        public void onLocationChanged(Location location) {
                mCurrentLocation = location;
              //  mLastUpdateTime = DateFormat.getTimeInstance().format(new Date());
                updateLocationUI();
//                Toast.makeText(this, getResources().getString(R.string.location_updated_message),
//                        Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                // Refer to the javadoc for ConnectionResult to see what error codes might be returned in
                // onConnectionFailed.
                Log.i(TAG, "Connection failed: ConnectionResult.getErrorCode() = " + connectionResult.getErrorCode());
        }

        /**
         * Stores activity data in the Bundle.
         */
        public void onSaveInstanceState(Bundle savedInstanceState) {
                savedInstanceState.putBoolean(KEY_REQUESTING_LOCATION_UPDATES, mRequestingLocationUpdates);
                savedInstanceState.putParcelable(KEY_LOCATION, mCurrentLocation);
                //  savedInstanceState.putString(KEY_LAST_UPDATED_TIME_STRING, mLastUpdateTime);
                super.onSaveInstanceState(savedInstanceState);
        }

        /**
         * Prompt user to enable GPS and Location Services
         * @param mGoogleApiClient
         * @param activity
         */
        public static void locationChecker(GoogleApiClient mGoogleApiClient, final Activity activity) {
                LocationRequest locationRequest = LocationRequest.create();
                locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
                locationRequest.setInterval(30 * 1000);
                locationRequest.setFastestInterval(5 * 1000);
                LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                        .addLocationRequest(locationRequest);
                builder.setAlwaysShow(true);
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
                                                                activity, 1000);
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
                });
        }
}


