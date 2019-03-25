package com.example.just_useless;

public class list {

    String latitude;
    String longitude;
    String needer;

    public list(){

    }

    public list(String latitude, String longitude, String needer) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.needer = needer;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getNeeder() {
        return needer;
    }

    public void setNeeder(String needer) {
        this.needer = needer;
    }
}
