package com.weapp.evan.theweather;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Evan on 2016-01-31.
 */
public class WeatherHttpClient {

    private static final String OPEN_WEATHER_MAP_API =
            "http://api.openweathermap.org/data/2.5/weather?q=%s&appid=5229a831e5dad59d47e92e8f9260e606";
    private static final String WEATHER_IMG_ICON = "http://openweathermap.org/img/w/";

    private static final String BASE_FORECAST_URL =
            "http://api.openweathermap.org/data/2.5/forecast/daily?q=%s&appid=5229a831e5dad59d47e92e8f9260e606&units=metric";

    public String getWeatherData(String cityAndCountry) {
        HttpURLConnection con = null;
        InputStream is = null;

        try {
            URL url = new URL(String.format(OPEN_WEATHER_MAP_API, cityAndCountry));
            con = (HttpURLConnection)url.openConnection();

            // Let's read the response

            StringBuffer buffer = new StringBuffer();

            is = con.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String line = null;
            while ((line = br.readLine()) != null)
                buffer.append(line + "\r\n");

            is.close();
            con.disconnect();
            JSONObject data = new JSONObject(buffer.toString());

            // This value will be 404 if the request was not
            // successful
            if(data.getInt("cod") != 200){
                return null;
            }

            return buffer.toString();

        } catch (Throwable t) {
            t.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (Throwable t) {
            }
            try {
                con.disconnect();
            } catch (Throwable t) {
            }
        }

        return null;
    }


    public byte[] getImage(String code) {
        HttpURLConnection con = null ;
        InputStream is = null;
        try {
            con = (HttpURLConnection) ( new URL(WEATHER_IMG_ICON + code + ".png")).openConnection();
           // con.setRequestMethod("GET");
         //   con.setDoInput(true);
           // con.setDoOutput(true);
           // con.connect();

            // Let's read the response
            is = con.getInputStream();
            byte[] buffer = new byte[1024];
            ByteArrayOutputStream baos = new ByteArrayOutputStream();

            while ( is.read(buffer) != -1)
                baos.write(buffer);

            is.close();
            con.disconnect();
            return baos.toByteArray();
        }
        catch(Throwable t) {
            t.printStackTrace();
        }
        finally {
            try { is.close(); } catch(Throwable t) {}
            try { con.disconnect(); } catch(Throwable t) {}
        }

        return null;

    }

    public String getForecastWeatherData(String cityAndCountry){
        HttpURLConnection con = null;
        InputStream is = null;

        try {
            URL url = new URL(String.format(BASE_FORECAST_URL, cityAndCountry));
            con = (HttpURLConnection)url.openConnection();

            // Let's read the response

            StringBuffer buffer = new StringBuffer();

            is = con.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String line = null;
            while ((line = br.readLine()) != null)
                buffer.append(line + "\r\n");

            is.close();
            con.disconnect();
            JSONObject data = new JSONObject(buffer.toString());

            // This value will be 404 if the request was not
            // successful
            if(data.getInt("cod") != 200){
                return null;
            }

            return buffer.toString();

        } catch (Throwable t) {
            t.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (Throwable t) {
            }
            try {
                con.disconnect();
            } catch (Throwable t) {
            }
        }

        return null;
    }

}

