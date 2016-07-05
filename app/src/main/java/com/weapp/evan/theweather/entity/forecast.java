package com.weapp.evan.theweather.entity;

/**
 * Created by Evan on 2016-06-29.
 */
public class Forecast {
    private String date;
    private String icon;
    private int temp_max;
    private int temp_min;

    public Forecast(String date, String icon, int temp_max, int temp_min) {
        this.date = date;
        this.icon = icon;
        this.temp_max = temp_max;
        this.temp_min = temp_min;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public int getTemp_max() {
        return temp_max;
    }

    public void setTemp_max(int temp_max) {
        this.temp_max = temp_max;
    }

    public int getTemp_min() {
        return temp_min;
    }

    public void setTemp_min(int temp_min) {
        this.temp_min = temp_min;
    }
}
