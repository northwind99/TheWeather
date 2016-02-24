package com.weapp.evan.theweather;

import com.weapp.evan.theweather.model.Location;
import com.weapp.evan.theweather.model.Weather;
import com.weapp.evan.theweather.model.WeatherForecast;
import com.weapp.evan.theweather.model.DayForecast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Evan on 2016-01-31.
 */
public class JSONWeatherParser {

   // Location loc = new Location();

    public static Weather getWeather(String data) throws JSONException{
        JSONObject jObj = new JSONObject(data);
        Weather weather = new Weather();
        JSONObject mainObj = getObject("main", jObj);
        JSONObject windObj = getObject("wind",jObj);
        JSONArray jArrayObj = jObj.getJSONArray("weather");
        JSONObject JSONWeather = jArrayObj.getJSONObject(0);
        weather.currentCondition.setWeatherId(getInt("id",JSONWeather));
        weather.currentCondition.setCondition(getString("main", JSONWeather));
        weather.currentCondition.setIcon(getString("icon", JSONWeather));
        weather.currentCondition.setWind(getFloat("speed",windObj));
        weather.currentCondition.setPressure(getFloat("pressure",mainObj));
        weather.currentCondition.setHumidity(getFloat("humidity",mainObj));

        weather.temperature.setTemp(getFloat("temp", mainObj));
        weather.temperature.setTemp_min(getFloat("temp_min", mainObj));
        weather.temperature.setTemp_max(getFloat("temp_max",mainObj));
        weather.temperature.setDt(getDt("dt", jObj));
        return weather;
    }


    private static JSONObject getObject(String tagName, JSONObject jObj)  throws JSONException {
        JSONObject subObj = jObj.getJSONObject(tagName);
        return subObj;
    }

    private static float getFloat(String tagName, JSONObject jObj) throws JSONException {
        return (float) jObj.getDouble(tagName);
    }

    private  static  double getDt(String tagName, JSONObject jObj) throws JSONException {
        return (double) jObj.getDouble(tagName);
    }

    private static int  getInt(String tagName, JSONObject jObj) throws JSONException {
        return jObj.getInt(tagName);
    }

    private static String getString(String tagName, JSONObject jObj) throws JSONException {
        return jObj.getString(tagName);
    }

    public static WeatherForecast getForecastWeather(String data) throws JSONException{
        WeatherForecast forecast = new WeatherForecast();

        // We create out JSONObject from the data
        JSONObject jObj = new JSONObject(data);

        JSONArray jArr = jObj.getJSONArray("list"); // Here we have the forecast for every day

        // We traverse all the array and parse the data
        for (int i=0; i < jArr.length(); i++) {
            JSONObject jDayForecast = jArr.getJSONObject(i);

            // Now we have the json object so we can extract the data
            DayForecast df = new DayForecast();

            // We retrieve the timestamp (dt)
            df.timestamp = jDayForecast.getLong("dt");

            // Temp is an object
            JSONObject jTempObj = jDayForecast.getJSONObject("temp");

            df.forecastTemp.setMin((float) jTempObj.getDouble("day"));
            df.forecastTemp.setMax((float) jTempObj.getDouble("max"));

            df.forecastTemp.day = (float) jTempObj.getDouble("day");
            df.forecastTemp.min = (float) jTempObj.getDouble("min");
            df.forecastTemp.max = (float) jTempObj.getDouble("max");
            df.forecastTemp.night = (float) jTempObj.getDouble("night");
            df.forecastTemp.eve = (float) jTempObj.getDouble("eve");
            df.forecastTemp.morning = (float) jTempObj.getDouble("morn");

            JSONArray jArrayObj = jDayForecast.getJSONArray("weather");
            JSONObject jWeather = jArrayObj.getJSONObject(0);
            df.forecastCondition.icon = jWeather.getString("icon").toString();



            // Pressure & Humidity
            df.weather.currentCondition.setPressure((float) jDayForecast.getDouble("pressure"));
            df.weather.currentCondition.setHumidity((float) jDayForecast.getDouble("humidity"));

            // ...and now the weather
            JSONArray jWeatherArr = jDayForecast.getJSONArray("weather");
            JSONObject jWeatherObj = jWeatherArr.getJSONObject(0);
            df.weather.currentCondition.setWeatherId(getInt("id", jWeatherObj));
            df.weather.currentCondition.setDescr(getString("description", jWeatherObj));
            df.weather.currentCondition.setCondition(getString("main", jWeatherObj));
            df.weather.currentCondition.setIcon(getString("icon", jWeatherObj));

            forecast.addForecast(df);
        }

        return forecast;
    }

}
