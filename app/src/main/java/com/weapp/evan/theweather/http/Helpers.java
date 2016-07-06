package com.weapp.evan.theweather.http;

/**
 * Created by Evan on 2016-06-27.
 */
public class Helpers {
    public static final String BASE_URL = "http://api.openweathermap.org/data/2.5/weather?q=";
    public static final String key = "&appid=5229a831e5dad59d47e92e8f9260e606";
    public static final String FORECAST_URL = "http://api.openweathermap.org/data/2.5/forecast/daily?q=";
    public static final String FORECAST_LENGTH = "&mode=json&units=metric&cnt=14";
    public Helpers(){}
}
