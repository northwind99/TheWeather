package com.weapp.evan.theweather;


//import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
        import android.location.Geocoder;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;

import android.text.InputType;
import android.util.Log;
import android.view.Menu;
        import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

        import com.weapp.evan.theweather.model.Weather;
import com.weapp.evan.theweather.model.WeatherForecast;

import org.json.JSONException;

        import java.text.DateFormat;
import java.util.Date;
        import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.support.v7.app.ActionBar;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {

    //A ProgressDialog object
    private ProgressDialog progressDialog;


    TextView updatedField;
    TextView currentTemperatureField;
    TextView inputCity;
    ImageView conditionIcons;
    TextView dayOneTitle;
    TextView dayOneMin;
    TextView dayOneMax;
    TextView currentMin;
    TextView currentMax;
    TextView currentWind;
    TextView currentCondition;
    TextView currentPressure;
    TextView currentHumidity;
    ImageView dayOneIcon;
    TextView dayTwoTitle;
    ImageView dayTwoIcon;
    TextView dayTwoMin;
    TextView dayTwoMax;
    TextView dayThreeTitle;
    ImageView dayThreeIcon;
    TextView dayThreeMin;
    TextView dayThreeMax;
    TextView dayFourTitle;
    ImageView dayFourIcon;
    TextView dayFourMin;
    TextView dayFourMax;


    protected Context context;

   // String city = "Toronto";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
       // getWindow().setBackgroundDrawableResource(R.drawable.bitmap);



        currentTemperatureField = (TextView) findViewById(R.id.current_temperature_field);
        updatedField = (TextView) findViewById(R.id.updateDateId);
        inputCity = (TextView) findViewById(R.id.cityNameId);
        conditionIcons = (ImageView) findViewById(R.id.conIcon);
        dayOneTitle = (TextView) findViewById(R.id.dayOneText);
        dayOneMin = (TextView) findViewById(R.id.dayOneMinID);
        dayOneMax = (TextView) findViewById(R.id.dayOneMaxID);
        currentMin = (TextView) findViewById(R.id.minValue);
        currentMax = (TextView) findViewById(R.id.maxValue);
        currentWind = (TextView) findViewById(R.id.windValue);
        currentCondition = (TextView) findViewById((R.id.conditionValue));
        currentPressure = (TextView) findViewById((R.id.pressureValue));
        currentHumidity = (TextView) findViewById((R.id.humidityValue));
        dayOneIcon = (ImageView) findViewById(R.id.dayOneIconID);
        dayTwoTitle = (TextView) findViewById(R.id.dayTwoText);
        dayTwoIcon = (ImageView) findViewById(R.id.dayTwoIconID);
        dayTwoMin =(TextView) findViewById(R.id.dayTwoMinID);
        dayTwoMax = (TextView) findViewById(R.id.dayTwoMaxID);
        dayThreeTitle = (TextView) findViewById(R.id.dayThreeText);
        dayThreeIcon = (ImageView) findViewById(R.id.dayThreeIconID);
        dayThreeMin = (TextView) findViewById(R.id.dayThreeMinID);
        dayThreeMax = (TextView) findViewById(R.id.dayThreeMaxID);
        dayFourTitle = (TextView) findViewById(R.id.dayFourText);
        dayFourIcon = (ImageView) findViewById(R.id.dayFourIconID);
        dayFourMin = (TextView) findViewById(R.id.dayFourMinID);
        dayFourMax = (TextView) findViewById(R.id.dayFourMaxID);


        ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setDisplayShowHomeEnabled(true);
        }


        getMyCurrentLocation();

        String cityAndCountry = inputCity.getText().toString();
       // String[] cityCountry = cityAndCountry.split(",");
      //  String city = cityCountry[0];
       // String country = cityCountry[1];

        renderWeatherData(cityAndCountry);


    }

    public  void renderWeatherData(String cityAndCountry){

        JSONForecastWeatherTask jsonForecastWeatherTask = new JSONForecastWeatherTask();
        jsonForecastWeatherTask.execute(new String[]{cityAndCountry});


    }

    public Bitmap conditionIcons(String iconValue){

        if("01d".equals(iconValue)){
            Bitmap bMap = BitmapFactory.decodeResource(getResources(), R.drawable.d01);
            return Bitmap.createScaledBitmap(bMap, 60, 60, true);
        }else if("01n".equals(iconValue)){
            Bitmap bMap = BitmapFactory.decodeResource(getResources(), R.drawable.n01);
            return Bitmap.createScaledBitmap(bMap, 60, 60, true);
        }else if("02d".equals(iconValue)){
            Bitmap bMap = BitmapFactory.decodeResource(getResources(), R.drawable.d02);
            return Bitmap.createScaledBitmap(bMap, 60, 60, true);
        }else if("02n".equals(iconValue)){
            Bitmap bMap = BitmapFactory.decodeResource(getResources(), R.drawable.n02);
            return Bitmap.createScaledBitmap(bMap, 60, 60, true);
        }else if("03d".equals(iconValue)){
            Bitmap bMap = BitmapFactory.decodeResource(getResources(), R.drawable.d03);
            return Bitmap.createScaledBitmap(bMap, 60, 60, true);
        }else if("03n".equals(iconValue)){
            Bitmap bMap = BitmapFactory.decodeResource(getResources(), R.drawable.n03);
            return Bitmap.createScaledBitmap(bMap, 60, 60, true);
        }else if("04d".equals(iconValue)){
            Bitmap bMap = BitmapFactory.decodeResource(getResources(), R.drawable.d04);
            return Bitmap.createScaledBitmap(bMap, 60, 60, true);
        }else if("04n".equals(iconValue)){
            Bitmap bMap = BitmapFactory.decodeResource(getResources(), R.drawable.n04);
            return Bitmap.createScaledBitmap(bMap, 60, 60, true);
        }else if("09d".equals(iconValue)){
            Bitmap bMap = BitmapFactory.decodeResource(getResources(), R.drawable.d09);
            return Bitmap.createScaledBitmap(bMap, 60, 60, true);
        }else if("09n".equals(iconValue)){
            Bitmap bMap = BitmapFactory.decodeResource(getResources(), R.drawable.n09);
            return Bitmap.createScaledBitmap(bMap, 60, 60, true);
        }else if("10d".equals(iconValue)){
            Bitmap bMap = BitmapFactory.decodeResource(getResources(), R.drawable.d10);
            return Bitmap.createScaledBitmap(bMap, 60, 60, true);
        }else if("10n".equals(iconValue)){
            Bitmap bMap = BitmapFactory.decodeResource(getResources(), R.drawable.n10);
            return Bitmap.createScaledBitmap(bMap, 60, 60, true);
        }else if("11d".equals(iconValue)){
            Bitmap bMap = BitmapFactory.decodeResource(getResources(), R.drawable.d11);
            return Bitmap.createScaledBitmap(bMap, 60, 60, true);
        }else if("11n".equals(iconValue)){
            Bitmap bMap = BitmapFactory.decodeResource(getResources(), R.drawable.n11);
            return Bitmap.createScaledBitmap(bMap, 60, 60, true);
        }else if("13d".equals(iconValue)){
            Bitmap bMap = BitmapFactory.decodeResource(getResources(), R.drawable.d13);
            return Bitmap.createScaledBitmap(bMap, 60, 60, true);
        }else if("13n".equals(iconValue)){
            Bitmap bMap = BitmapFactory.decodeResource(getResources(), R.drawable.n13);
            return Bitmap.createScaledBitmap(bMap, 60, 60, true);
        }else if("50d".equals(iconValue)){
            Bitmap bMap = BitmapFactory.decodeResource(getResources(), R.drawable.d50);
            return Bitmap.createScaledBitmap(bMap, 60, 60, true);
        }else if("50n".equals(iconValue)){
            Bitmap bMap = BitmapFactory.decodeResource(getResources(), R.drawable.n50);
            return Bitmap.createScaledBitmap(bMap, 60, 60, true);
        }else
            return null;

    }

    private class JSONWeatherTask extends AsyncTask<String, Void, Weather> {



        @Override
        protected Weather doInBackground(String... params) {

            Weather weather = new Weather();

            String data = (new WeatherHttpClient()).getWeatherData(params[0]);



            try {
                weather = JSONWeatherParser.getWeather(data);
                weather.iconData = ( (new WeatherHttpClient()).getImage(weather.currentCondition.getIcon()));


            } catch (JSONException e) {
                e.printStackTrace();
            }
            return weather;

        }


        //Update the progress
       // @Override
        protected void onProgressUpdate(Integer... values)
        {
            //set the current progress of the progress dialog
            progressDialog.setProgress(values[0]);
        }

        @Override
        protected void onPostExecute(Weather weather) {
            super.onPostExecute(weather);

            currentTemperatureField.setText("" + Math.round((weather.temperature.getTemp() - 273.15)) + "°");
            DateFormat df = DateFormat.getDateTimeInstance();

            String updatedOn = df.format(weather.temperature.getDt() *1000);
            updatedField.setText(updatedOn);
            Log.d("updateOn", updatedOn);

            currentMin.setText("" + (Math.round((weather.temperature.getTemp_min() - 273.15) - 4)) + "°");
            currentMax.setText("" + (Math.round((weather.temperature.getTemp_max() - 273.15) + 2)) + "°");
            currentWind.setText("" + weather.currentCondition.getWind() + "mps");
            currentCondition.setText("" + weather.currentCondition.getCondition());
            currentPressure.setText("" + weather.currentCondition.getPressure() + "hpa");
            currentHumidity.setText("" + weather.currentCondition.getHumidity() + "%");

            String iconValue = weather.currentCondition.getIcon().toString();

            if("01d".equals(iconValue)){
                Bitmap bMap = BitmapFactory.decodeResource(getResources(), R.drawable.d01);
                Bitmap bMapScaled = Bitmap.createScaledBitmap(bMap, 100, 100, true);
                conditionIcons.setImageBitmap(bMapScaled);
            }else if("01n".equals(iconValue)){
                Bitmap bMap = BitmapFactory.decodeResource(getResources(), R.drawable.n01);
                Bitmap bMapScaled = Bitmap.createScaledBitmap(bMap, 100, 100, true);
                conditionIcons.setImageBitmap(bMapScaled);
            }else if("02d".equals(iconValue)){
                Bitmap bMap = BitmapFactory.decodeResource(getResources(), R.drawable.d02);
                Bitmap bMapScaled = Bitmap.createScaledBitmap(bMap, 100, 100, true);
                conditionIcons.setImageBitmap(bMapScaled);
            }else if("02n".equals(iconValue)){
                Bitmap bMap = BitmapFactory.decodeResource(getResources(), R.drawable.n02);
                Bitmap bMapScaled = Bitmap.createScaledBitmap(bMap, 100, 100, true);
                conditionIcons.setImageBitmap(bMapScaled);
            }else if("03d".equals(iconValue)){
                Bitmap bMap = BitmapFactory.decodeResource(getResources(), R.drawable.d03);
                Bitmap bMapScaled = Bitmap.createScaledBitmap(bMap, 100, 100, true);
                conditionIcons.setImageBitmap(bMapScaled);
            }else if("03n".equals(iconValue)){
                Bitmap bMap = BitmapFactory.decodeResource(getResources(), R.drawable.n03);
                Bitmap bMapScaled = Bitmap.createScaledBitmap(bMap, 100, 100, true);
                conditionIcons.setImageBitmap(bMapScaled);
            }else if("04d".equals(iconValue)){
                Bitmap bMap = BitmapFactory.decodeResource(getResources(), R.drawable.d04);
                Bitmap bMapScaled = Bitmap.createScaledBitmap(bMap, 100, 100, true);
                conditionIcons.setImageBitmap(bMapScaled);
            }else if("04n".equals(iconValue)){
                Bitmap bMap = BitmapFactory.decodeResource(getResources(), R.drawable.n04);
                Bitmap bMapScaled = Bitmap.createScaledBitmap(bMap, 100, 100, true);
                conditionIcons.setImageBitmap(bMapScaled);
            }else if("09d".equals(iconValue)){
                Bitmap bMap = BitmapFactory.decodeResource(getResources(), R.drawable.d09);
                Bitmap bMapScaled = Bitmap.createScaledBitmap(bMap, 100, 100, true);
                conditionIcons.setImageBitmap(bMapScaled);
            }else if("09n".equals(iconValue)){
                Bitmap bMap = BitmapFactory.decodeResource(getResources(), R.drawable.n09);
                Bitmap bMapScaled = Bitmap.createScaledBitmap(bMap, 100, 100, true);
                conditionIcons.setImageBitmap(bMapScaled);
            }else if("10d".equals(iconValue)){
                Bitmap bMap = BitmapFactory.decodeResource(getResources(), R.drawable.d10);
                Bitmap bMapScaled = Bitmap.createScaledBitmap(bMap, 100, 100, true);
                conditionIcons.setImageBitmap(bMapScaled);
            }else if("10n".equals(iconValue)){
                Bitmap bMap = BitmapFactory.decodeResource(getResources(), R.drawable.n10);
                Bitmap bMapScaled = Bitmap.createScaledBitmap(bMap, 100, 100, true);
                conditionIcons.setImageBitmap(bMapScaled);
            }else if("11d".equals(iconValue)){
                Bitmap bMap = BitmapFactory.decodeResource(getResources(), R.drawable.d11);
                Bitmap bMapScaled = Bitmap.createScaledBitmap(bMap, 100, 100, true);
                conditionIcons.setImageBitmap(bMapScaled);
            }else if("11n".equals(iconValue)){
                Bitmap bMap = BitmapFactory.decodeResource(getResources(), R.drawable.n11);
                Bitmap bMapScaled = Bitmap.createScaledBitmap(bMap, 100, 100, true);
                conditionIcons.setImageBitmap(bMapScaled);
            }else if("13d".equals(iconValue)){
                Bitmap bMap = BitmapFactory.decodeResource(getResources(), R.drawable.d13);
                Bitmap bMapScaled = Bitmap.createScaledBitmap(bMap, 100, 100, true);
                conditionIcons.setImageBitmap(bMapScaled);
            }else if("13n".equals(iconValue)){
                Bitmap bMap = BitmapFactory.decodeResource(getResources(), R.drawable.n13);
                Bitmap bMapScaled = Bitmap.createScaledBitmap(bMap, 100, 100, true);
                conditionIcons.setImageBitmap(bMapScaled);
            }else if("50d".equals(iconValue)){
                Bitmap bMap = BitmapFactory.decodeResource(getResources(), R.drawable.d50);
                Bitmap bMapScaled = Bitmap.createScaledBitmap(bMap, 100, 100, true);
                conditionIcons.setImageBitmap(bMapScaled);
            }else if("50n".equals(iconValue)){
                Bitmap bMap = BitmapFactory.decodeResource(getResources(), R.drawable.n50);
                Bitmap bMapScaled = Bitmap.createScaledBitmap(bMap, 100, 100, true);
                conditionIcons.setImageBitmap(bMapScaled);
            }


            /*
            if (weather.iconData != null && weather.iconData.length > 0) {

               Bitmap img = BitmapFactory.decodeByteArray(weather.iconData, 0, weather.iconData.length);
               conditionIcons.setImageBitmap(Bitmap.createScaledBitmap(img, 170, 170, false));
           }

            */


        }

    }

    private class JSONForecastWeatherTask extends AsyncTask<String, Void, WeatherForecast> {

        //Before running code in separate thread
        @Override
        protected void onPreExecute()
        {
            progressDialog = ProgressDialog.show(MainActivity.this,"Loading",
                    "Loading View, please wait...", false, false);
        }

        @Override
        protected WeatherForecast doInBackground(String... params) {

            String data = ((new WeatherHttpClient()).getForecastWeatherData(params[0]));
            WeatherForecast forecast = new WeatherForecast();
            try {
                forecast = JSONWeatherParser.getForecastWeather(data);

              //  System.out.println("Weather [" + forecast + "]");
                // Let's retrieve the icon
                //weather.iconData = ( (new WeatherHttpClient()).getImage(weather.currentCondition.getIcon()));
                forecast.getForecast(0).weather.iconData = ( (new WeatherHttpClient()).getImage(forecast.getForecast(0).weather.currentCondition.getIcon()));
                forecast.getForecast(1).weather.iconData = ( (new WeatherHttpClient()).getImage(forecast.getForecast(1).weather.currentCondition.getIcon()));
                forecast.getForecast(2).weather.iconData = ( (new WeatherHttpClient()).getImage(forecast.getForecast(2).weather.currentCondition.getIcon()));
                forecast.getForecast(3).weather.iconData = ( (new WeatherHttpClient()).getImage(forecast.getForecast(3).weather.currentCondition.getIcon()));

              //  forecast.getForecast(0).weather.iconStringData = forecast.getForecast(0).weather.currentCondition.getIcon();


            } catch (JSONException e) {
                e.printStackTrace();
            }
            return forecast;


        }

        @Override
        protected void onPostExecute(WeatherForecast forecastWeather) {
            super.onPostExecute(forecastWeather);


            //day one forecast
            String date = (new Date(forecastWeather.getForecast(1).timestamp * 1000)).toString();
            String[] date1 = date.split(" ");
            String dayOneofWeek = date1[0].toUpperCase();
            dayOneTitle.setText("" + dayOneofWeek);

            String dayOneIconValue = forecastWeather.getForecast(1).weather.currentCondition.getIcon();
            dayOneIcon.setImageBitmap(conditionIcons(dayOneIconValue));

            /*
            if (forecastWeather.getForecast(0).weather.iconData != null && forecastWeather.getForecast(0).weather.iconData.length > 0) {
                    Bitmap forecastImg = BitmapFactory.decodeByteArray(forecastWeather.getForecast(0).weather.iconData, 0, forecastWeather.getForecast(0).weather.iconData.length);
                    dayOneIcon.setImageBitmap(Bitmap.createScaledBitmap(forecastImg, 60, 60, false));
            }
            */
            dayOneMin.setText("" + Math.round(forecastWeather.getForecast(1).forecastTemp.min) + "°");
            dayOneMax.setText("" + Math.round(forecastWeather.getForecast(1).forecastTemp.max) + "°");

            //day two forecast;
            String dayTwoofWeek = ((new Date(forecastWeather.getForecast(2).timestamp * 1000)).toString().split(" "))[0].toUpperCase();
            dayTwoTitle.setText("" + dayTwoofWeek);

            String dayTwoIconValue = forecastWeather.getForecast(2).weather.currentCondition.getIcon();
            dayTwoIcon.setImageBitmap(conditionIcons(dayTwoIconValue));

            /*
            if (forecastWeather.getForecast(1).weather.iconData != null && forecastWeather.getForecast(1).weather.iconData.length > 0) {
                Bitmap forecastImg = BitmapFactory.decodeByteArray(forecastWeather.getForecast(1).weather.iconData, 0, forecastWeather.getForecast(1).weather.iconData.length);
                dayTwoIcon.setImageBitmap(Bitmap.createScaledBitmap(forecastImg, 60, 60, false));
            }
            */
            dayTwoMin.setText("" + Math.round(forecastWeather.getForecast(2).forecastTemp.min) + "°");
            dayTwoMax.setText("" + Math.round(forecastWeather.getForecast(2).forecastTemp.max) + "°");

            //day three forecast
            String daythreeofWeek = ((new Date(forecastWeather.getForecast(3).timestamp * 1000)).toString().split(" "))[0].toUpperCase();
            dayThreeTitle.setText("" + daythreeofWeek);

            String dayThreeIconValue = forecastWeather.getForecast(3).weather.currentCondition.getIcon();
            dayThreeIcon.setImageBitmap(conditionIcons(dayThreeIconValue));

            /*
            if (forecastWeather.getForecast(2).weather.iconData != null && forecastWeather.getForecast(2).weather.iconData.length > 0) {
                Bitmap forecastImg = BitmapFactory.decodeByteArray(forecastWeather.getForecast(2).weather.iconData, 0, forecastWeather.getForecast(2).weather.iconData.length);
                dayThreeIcon.setImageBitmap(Bitmap.createScaledBitmap(forecastImg, 60, 60, false));
            }
            */
            dayThreeMin.setText("" + Math.round(forecastWeather.getForecast(3).forecastTemp.min) + "°");
            dayThreeMax.setText("" + Math.round(forecastWeather.getForecast(3).forecastTemp.max) + "°");

            //day Four forecast
            String dayFourofWeek = ((new Date(forecastWeather.getForecast(4).timestamp * 1000)).toString().split(" "))[0].toUpperCase();
            dayFourTitle.setText("" + dayFourofWeek);

            String dayFourIconValue = forecastWeather.getForecast(4).weather.currentCondition.getIcon();
            dayFourIcon.setImageBitmap(conditionIcons(dayFourIconValue));

            /*
            if (forecastWeather.getForecast(3).weather.iconData != null && forecastWeather.getForecast(3).weather.iconData.length > 0) {
                Bitmap forecastImg = BitmapFactory.decodeByteArray(forecastWeather.getForecast(3).weather.iconData, 0, forecastWeather.getForecast(3).weather.iconData.length);
                dayFourIcon.setImageBitmap(Bitmap.createScaledBitmap(forecastImg, 60, 60, false));
            }
            */
            dayFourMin.setText("" + Math.round(forecastWeather.getForecast(4).forecastTemp.min) + "°");
            dayFourMax.setText("" + Math.round(forecastWeather.getForecast(4).forecastTemp.max) + "°");

            String cityAndCountry = inputCity.getText().toString();
        //    String[] cityCountry = cityAndCountry.split(",");
         //   String city = cityCountry[0];
         //   String country = cityCountry[1];

            JSONWeatherTask task = new JSONWeatherTask();
            task.execute(new String[]{cityAndCountry});

            //close the progress dialog
            progressDialog.dismiss();

        }
    }


    /** Method to turn on GPS **/
    public void turnGPSOn(){
        try
        {

            String provider = Settings.Secure.getString(getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);


            if(!provider.contains("gps")){ //if gps is disabled
                final Intent poke = new Intent();
                poke.setClassName("com.android.settings", "com.android.settings.widget.SettingsAppWidgetProvider");
                poke.addCategory(Intent.CATEGORY_ALTERNATIVE);
                poke.setData(Uri.parse("3"));
                sendBroadcast(poke);
            }
        }
        catch (Exception e) {

        }
    }

    // Method to turn off the GPS
    public void turnGPSOff(){
        String provider = Settings.Secure.getString(getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);

        if(provider.contains("gps")){ //if gps is enabled
            final Intent poke = new Intent();
            poke.setClassName("com.android.settings", "com.android.settings.widget.SettingsAppWidgetProvider");
            poke.addCategory(Intent.CATEGORY_ALTERNATIVE);
            poke.setData(Uri.parse("3"));
            sendBroadcast(poke);
        }
    }

    // turning off the GPS if its in on state. to avoid the battery drain.
    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        turnGPSOff();
    }


    /**
     * Check the type of GPS Provider available at that instance and
     * collect the location informations
     *
     *
     */
    void getMyCurrentLocation() {


        LocationManager locManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        LocationListener locListener = new MyLocationListener();

        if ( Build.VERSION.SDK_INT >= 23 &&
                ContextCompat.checkSelfPermission( context, android.Manifest.permission.ACCESS_FINE_LOCATION ) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }


        try {
            gps_enabled = locManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception ex) {
        }
        try {
            network_enabled = locManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch (Exception ex) {
        }

        //don't start listeners if no provider is enabled
        //if(!gps_enabled && !network_enabled)
        //return false;

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

            MyLat = location.getLatitude();
            MyLong = location.getLongitude();


        } else {
            Location loc = getLastKnownLocation(this);
            if (loc != null) {

                MyLat = loc.getLatitude();
                MyLong = loc.getLongitude();


            }
        }
        locManager.removeUpdates(locListener); // removes the periodic updates from location listener to //avoid battery drainage. If you want to get location at the periodic intervals call this method using //pending intent.

        try {
// Getting address from found locations.
            Geocoder geocoder;

            List<Address> addresses;
            geocoder = new Geocoder(this, Locale.getDefault());
            addresses = geocoder.getFromLocation(MyLat, MyLong, 1);

            stateName = addresses.get(0).getAdminArea();
            cityName = addresses.get(0).getSubLocality();

            if(addresses.get(0).getSubLocality() != null){
                cityName = addresses.get(0).getSubLocality();
            }else if(addresses.get(0).getLocality() != null){
                cityName = addresses.get(0).getLocality();
            }else if(addresses.get(0).getSubAdminArea() != null){
                cityName = addresses.get(0).getSubAdminArea();
            }else{
                cityName ="Toronto";
            }

            countryName = addresses.get(0).getCountryName();


            // you can get more details other than this . like country code, state code, etc.

            inputCity.setText(cityName + "," + countryName );

            System.out.println(" StateName " + stateName);
            System.out.println(" CityName " + cityName);
            System.out.println(" CountryName " + countryName);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Location listener class. to get location.
    public class MyLocationListener implements LocationListener {
        public void onLocationChanged(Location location) {
            if (location != null) {
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

    private boolean gps_enabled=false;
    private boolean network_enabled=false;
    Location location;

    Double MyLat, MyLong;
    String cityName="";
    String stateName="";
    String countryName="";


// below method to get the last remembered location. because we don't get locations all the times .At some instances we are unable to get the location from GPS. so at that moment it will show us the last stored location.

    public static Location getLastKnownLocation(Context context)
    {
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
            //System.out.println("---------------------------------------------------------------------");
            if(!iterator.hasNext())
                break;
            String s = (String)iterator.next();

            if(i != false && !locationmanager.isProviderEnabled(s))
                continue;
            // System.out.println("provider ===> "+s);

            Location location1 = locationmanager.getLastKnownLocation(s);



            if(location1 == null)
                continue;
            if(location != null)
            {
                //System.out.println("location ===> "+location);
                //System.out.println("location1 ===> "+location);
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
            // System.out.println("location  out ===> "+location);
            //System.out.println("location1 out===> "+location);
            i = locationmanager.isProviderEnabled(s);
            // System.out.println("---------------------------------------------------------------------");
        } while(true);
        return location;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.change_cityId) {

            showInputDialog();

        }

        return super.onOptionsItemSelected(item);
    }

    public static boolean validateCity( String city){

        Pattern p = Pattern.compile("^[a-zA-Z]+(?:(?:\\s+|-)[a-zA-Z]+)*$");
        Pattern p2 = Pattern.compile("[a-z]+(,\\s*[a-z]+)*");
        Matcher m = p.matcher(city);
        Matcher m2 = p2.matcher(city);
        if (m.find()){
            return true;
        }else if(m2.find()){
            return true;
        }else
            return false;
    }


    private void showInputDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Enter City,Country");

        final EditText cityInput = new EditText(MainActivity.this);
        cityInput.setInputType(InputType.TYPE_CLASS_TEXT);
        cityInput.setHint("Toronto,Canada");
        builder.setView(cityInput);
        builder.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                SharedPreferences.Editor editor = getSharedPreferences("com.weapp.evan.theweather", MODE_PRIVATE).edit();


                CityPreference cityPreference = new CityPreference(MainActivity.this);
                cityPreference.setCity(cityInput.getText().toString());

//                String newCityandNewCountry = null;
//                String cityInputString2 = null;
//
//                String cityInputString = cityInput.getText().toString();
//
//                if (cityInputString.contains(",")) {
//                    String[] cityValue = cityInputString.split(",");
//                    String newCity = cityValue[0];
//                    //  String newCountry = cityValue[1].replaceAll("^\\s+", "");
//                    String newCountry = cityValue[1].trim();
//                    cityInputString2 = newCity + "," + newCountry;
//                    if (validateCity(cityInputString2) && !cityInputString2.isEmpty()) {
//                        cityPreference.setCity(cityInputString2);
//                        newCityandNewCountry = cityPreference.getCity().toString();
//                    }
//                } else if (validateCity(cityInputString)) {
//                    cityPreference.setCity(cityInputString);
//                    newCityandNewCountry = cityPreference.getCity().toString();
//                } else {
//                    newCityandNewCountry = "Toronto,Canada";
//                }
//
//
//                //new CityPreference(MainActivity.this).setCity(cityInput.getText().toString());
//
//                //re-render everything again
//                inputCity.setText(newCityandNewCountry);
//                renderWeatherData(newCityandNewCountry);
            }
        });
        builder.show();
    }


}


