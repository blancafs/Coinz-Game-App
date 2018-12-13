package com.example.blanca.coinz2;
import com.mapbox.mapboxsdk.annotations.Marker;
import com.mapbox.mapboxsdk.annotations.MarkerViewOptions;
import com.mapbox.mapboxsdk.geometry.LatLng;


public class Coin {
    private String id;
    private String value;
    private String currency;
    private LatLng latLng;
    private Marker marker;
    private String date;
    private MarkerViewOptions markerViewOptions;
    private Boolean disabled;

    public Coin(String id, String value, String currency) {
        this.id = id;
        this.value = value;
        this.currency = currency;
        this.disabled = false;
    }

    /////////////
    // Setters //
    /////////////

    void setMarkerViewOptions(MarkerViewOptions markerViewOptions) {
        this.markerViewOptions = markerViewOptions;
    }

    void setLocation(LatLng a) {
        this.latLng = a;
    }

    public void setDate(String date) {
        this.date=date;
    }

    void setAsDisabled(Boolean disabled){
        this.disabled = disabled;
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

    String getCurrency() {
        return currency;
    }

    LatLng getLatLng() {
        return latLng;
    }

    MarkerViewOptions getMarkerViewOptions() {
        return markerViewOptions;
    }

    Boolean isDisabled(){
        return disabled;
    }

    ///////////////
    // Functions //
    ///////////////

    @Override
    public String toString() {
        String ans = value + "   " + currency;
        return ans;
    }

    String stringify() {
        String ans = id + "/" + value + "/" + currency + "/" + date;
        return ans;
    }

    Boolean isEqualto(Coin coin) {
        return (this.id.equals(coin.getId()));
    }
}
