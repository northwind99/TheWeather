package com.weapp.evan.theweather.entity;


import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class City implements Parcelable {

    int id;
    String cityName;

    public City(String cityNmae) {
        this.cityName = cityNmae;
    }

    protected City(Parcel in) {
        id = in.readInt();
        cityName = in.readString();
    }

    public static final Creator<City> CREATOR = new Creator<City>() {
        @Override
        public City createFromParcel(Parcel in) {
            return new City(in);
        }

        @Override
        public City[] newArray(int size) {
            return new City[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityNmae) {
        this.cityName = cityNmae;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(cityName);
    }
}
