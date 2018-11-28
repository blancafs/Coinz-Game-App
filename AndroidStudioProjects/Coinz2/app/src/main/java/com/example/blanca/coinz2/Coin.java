package com.example.blanca.coinz2;

import android.util.Log;

import com.mapbox.mapboxsdk.annotations.Marker;
import com.mapbox.mapboxsdk.annotations.MarkerViewOptions;
import com.mapbox.mapboxsdk.geometry.LatLng;

public class Coin {
    private final String tag = "Coin";
    private String id;
    private String value;
    private String currency;
    //private String markersymbol;
    //private String markercolor;
    private LatLng latLng;
    private Marker marker;
    private MarkerViewOptions markerViewOptions;

    public Coin(String id, String value, String currency) {
        this.id = id;
        this.value = value;
        this.currency = currency;
        //this.markersymbol = markersymbol;
        //this.markercolor = markercolor;
        this.latLng = latLng;
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

    /////////////
    // Getters //
    /////////////

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

    public String stringify() {
        String ans = id + "/" + value + "/" + currency;
        Log.d(tag, "[stringify] answer is " + ans + "============================");
        return ans;
    }

    public Boolean isEqualto(Coin coin) {
        return (this.id.equals(coin.getId()));
    }
}
