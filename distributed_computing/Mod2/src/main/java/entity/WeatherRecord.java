package entity;

import java.util.Calendar;

public class WeatherRecord extends Entity {
    public enum Precipitation {
        NONE,
        RAIN,
        SNOW,
        UNDEFINED;

        public String toString() {
            return switch (this) {
                case NONE -> "none";
                case RAIN -> "rain";
                case SNOW -> "show";
                case UNDEFINED -> "undefined";
            };
        }

        public static Precipitation fromString(String str) {
            try {
                return Precipitation.valueOf(str.toUpperCase());
            } catch (IllegalArgumentException e) {
                return Precipitation.UNDEFINED;
            }
        }
    }

    private Calendar date;
    private int temperature;
    private Precipitation precipitation;
    private int regionID;

    public WeatherRecord(int id, Calendar date, int temperature, Precipitation precipitation, int regionID) {
        super(id);
        this.date = date;
        this.temperature = temperature;
        this.precipitation = precipitation;
        this.regionID = regionID;
    }

    public WeatherRecord(int id, Calendar date, int temperature, String precipitationString, int regionID) {
        super(id);
        this.date = date;
        this.temperature = temperature;
        this.precipitation = Precipitation.fromString(precipitationString);
        this.regionID = regionID;
    }

    public Calendar getDate() {
        return date;
    }

    public int getTemperature() {
        return temperature;
    }

    public Precipitation getPrecipitation() {
        return precipitation;
    }

    public int getRegionID() {
        return regionID;
    }

    public void setDate(Calendar date) {
        this.date = date;
    }

    public void setTemperature(int temperature) {
        this.temperature = temperature;
    }

    public void setPrecipitation(Precipitation precipitation) {
        this.precipitation = precipitation;
    }

    public void setPrecipitation(String precipitationStr) {
        this.precipitation = Precipitation.fromString(precipitationStr);
    }

    public void setRegionID(int regionID) {
        this.regionID = regionID;
    }
}
