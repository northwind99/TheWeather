package com.weapp.evan.theweather.model;

/**
 * Created by Evan on 2016-01-31.
 */
public class Weather {

    public Temperature temperature = new Temperature();
    public CurrentCondition currentCondition = new CurrentCondition();

   // public String iconStringData;

    public byte[] iconData;

    public  class Temperature {
        private float temp;
        private double dt;
        private float temp_min;
        private float temp_max;

        public float getTemp_min() {
            return temp_min;
        }

        public void setTemp_min(float temp_min) {
            this.temp_min = temp_min;
        }

        public float getTemp_max() {
            return temp_max;
        }

        public void setTemp_max(float temp_max) {
            this.temp_max = temp_max;
        }

        public double getDt() {
            return dt;
        }

        public void setDt(double dt) {
            this.dt = dt;
        }

        public float getTemp() {
            return temp;
        }
        public void setTemp(float temp) {
            this.temp = temp;
        }
    }

    public class CurrentCondition {
        private int weatherId;
        private String condition;
        private String descr;
        private String icon;
        private float wind;

        private float pressure;
        private float humidity;

        public int getWeatherId() {

            return weatherId;
        }
        public void setWeatherId(int weatherId) {

            this.weatherId = weatherId;
        }
        public String getCondition() {

            return condition;
        }
        public void setCondition(String condition) {

            this.condition = condition;
        }
        public String getDescr() {

            return descr;
        }
        public void setDescr(String descr) {

            this.descr = descr;
        }
        public String getIcon() {

            return icon;
        }
        public void setIcon(String icon) {

            this.icon = icon;
        }
        public float getPressure() {

            return pressure;
        }
        public void setPressure(float pressure) {

            this.pressure = pressure;
        }
        public float getHumidity() {

            return humidity;
        }
        public void setHumidity(float humidity) {

            this.humidity = humidity;
        }

        public float getWind() {
            return wind;
        }

        public void setWind(float wind) {
            this.wind = wind;
        }
    }
}
