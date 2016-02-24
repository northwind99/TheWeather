package com.weapp.evan.theweather.model;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Evan on 2016-02-11.
 */
public class DayForecast {

        private static SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        public Weather weather = new Weather();
        public ForecastTemp forecastTemp = new ForecastTemp();
        public ForecastCondition forecastCondition = new ForecastCondition();
        public long timestamp;


        public class ForecastTemp {
            public float day;
            public float min;
            public float max;
            public float night;
            public float eve;
            public float morning;

            public float getDay() {
                return day;
            }

            public void setDay(float day) {
                this.day = day;
            }

            public float getMin() {
                return min;
            }

            public void setMin(float min) {
                this.min = min;
            }

            public float getMax() {
                return max;
            }

            public void setMax(float max) {
                this.max = max;
            }

            public float getNight() {
                return night;
            }

            public void setNight(float night) {
                this.night = night;
            }

            public float getEve() {
                return eve;
            }

            public void setEve(float eve) {
                this.eve = eve;
            }

            public float getMorning() {
                return morning;
            }

            public void setMorning(float morning) {
                this.morning = morning;
            }
        }

        public String getStringDate() {
            return sdf.format(new Date(timestamp));
        }

        public class ForecastCondition{
            public String icon;

            public String getIcon() {
                return icon;
            }

            public void setIcon(String icon) {
                this.icon = icon;
            }
        }
}
