package com.example.blanca.coinz2;

import android.util.Log;

import com.mapbox.mapboxsdk.annotations.Marker;
import com.mapbox.mapboxsdk.annotations.MarkerViewOptions;
import com.mapbox.mapboxsdk.geometry.LatLng;

import java.util.Date;

public class Coin {
    private final String tag = "Coin";
    private String id;
    private String value;
    private String currency;
    //private String markersymbol;
    //private String markercolor;
    private LatLng latLng;
    private Marker marker;
    private String date;
    private MarkerViewOptions markerViewOptions;

    public Coin(String id, String value, String currency) {
        this.id = id;
        this.value = value;
        this.currency = currency;
        //this.markersymbol = markersymbol;
        //this.markercolor = markercolor;
    }

    /////////////
    // Setters //
    /////////////

    public void setMarkerViewOptions(MarkerViewOptions markerViewOptions) {
        this.markerViewOptions = markerViewOptions;
    }

    public void setLocation(LatLng a) {
        this.latLng = a;
    }

    public void setDate(String date) {
        this.date=date;
    }

    /////////////
    // Getters //
    /////////////

    public String getDate() {
        return date;
    }

    public  double getGold() {
        double ans = Double.parseDouble(value);
        double convrate = 0;
        switch (currency) {
            case "SHIL":
                convrate = MapActivity.conversions.get(0);
            case "DOLR":
                convrate = MapActivity.conversions.get(1);
            case "QUID":
                convrate = MapActivity.conversions.get(2);
            case "PENY":
                convrate = MapActivity.conversions.get(3);
        }
        return ans*convrate;
    }

    public String getId() {
        return id;
    }

    public String getValue() {
        return value;
    }

    public String getCurrency() {
        return currency;
    }

    public LatLng getLatLng() {
        return latLng;
    }

    public MarkerViewOptions getMarkerViewOptions() {
        return markerViewOptions;
    }

    ///////////////
    // Functions //
    ///////////////

    @Override
    public String toString() {
        String ans = value + "   " + currency;
        return ans;
    }

    public String stringify() {
        String ans = id + "/" + value + "/" + currency + "/" + date;
        return ans;
    }

    public Boolean isEqualto(Coin coin) {
        return (this.id.equals(coin.getId()));
    }
}
