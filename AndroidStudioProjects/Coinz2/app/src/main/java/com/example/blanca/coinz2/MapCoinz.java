package com.example.blanca.coinz2;

import android.location.Location;
import android.util.Log;

import com.mapbox.geojson.Feature;
import com.mapbox.geojson.FeatureCollection;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.annotations.Icon;
import com.mapbox.mapboxsdk.annotations.IconFactory;
import com.mapbox.mapboxsdk.annotations.Marker;
import com.mapbox.mapboxsdk.annotations.MarkerViewOptions;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapboxMap;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

import io.opencensus.internal.StringUtil;

import static com.example.blanca.coinz2.MapActivity.player;
import static com.mapbox.mapboxsdk.Mapbox.getApplicationContext;

public class MapCoinz {

    private static final String tag = "MapCoinz";

    private FeatureCollection featureCollection;
    private ArrayList<Coin> onmapcoins;

    public MapCoinz(FeatureCollection mapCoinz) {
        this.featureCollection = mapCoinz;
        Log.d(tag, "Coinz initialised correctly=========================");
    }

    public void setCurrencies() {
        String ans = featureCollection.toJson();
        Log.d(tag, "[setCurrencies] featurecollection to json gave " +ans +"==================");
    }

    public void addMarkers(MapboxMap mapboxMap) {
        Log.d(tag, "[addMarkers] started ====================================");
        onmapcoins = new ArrayList<>();
        Log.d(tag, "[addMarkers] bankedcoins has length " + player.getBankedCoinz().size() + " walletcoins has size " + player.getWalletCoinz().size());

        for (Feature feature : featureCollection.features()) {
            // Coin features ==============================================//
            String id = feature.getStringProperty("id");
            String value = feature.getStringProperty("value");
            String currency = feature.getStringProperty("currency");

            // Snippet and title strings, individual to each feature =====//
            String snippet = value;
            String title = currency;

            // Getting coordinates
            Point point = (Point) feature.geometry();
            LatLng latLng = new LatLng(point.latitude(), point.longitude());

            // Creating coin object
            Coin coin = new Coin(id, value, currency);
            coin.setLocation(latLng);

            // simple date format
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy");
            String date = simpleDateFormat.format(new Date());
            coin.setDate(date);

            // Coin icon
            Icon icon = Helpers.getIcon(coin);

            // Implement marker =========================================//
            MarkerViewOptions markerViewOptions = new MarkerViewOptions().title(title).snippet(snippet).icon(icon).position(latLng);
            coin.setMarkerViewOptions(markerViewOptions);

            if (!player.isInBank(coin) && !player.isInWallet(coin) && !player.isInSpecial(coin)) {
                onmapcoins.add(coin);
                mapboxMap.addMarker(markerViewOptions);
                Log.d(tag, "[addMarkers] coin added to mapcoins as not in wallet or bank, id " + coin.getId() + " ");
            }
            Log.d(tag, "[addMarkers] Marker created successfully ===========================");
        }
    }

    public void deleteCoin(MapboxMap mapboxMap, Coin coin) {
        mapboxMap.removeMarker(coin.getMarkerViewOptions().getMarker());
    }

    public void updateCoins(MapboxMap mapboxMap, Location location) {
        Log.d(tag, "[updateCoins] og size of onmapcoins is " + onmapcoins.size() + "=========");
        ArrayList<Coin> toremove = new ArrayList<>();
        for (Coin a : onmapcoins) {
            Double dist = Helpers.dist(location, a.getLatLng());
            if ((dist <= 25)) {
                // deletes coin from map
                deleteCoin(mapboxMap, a);
                // update list of coins on map
                toremove.add(a);
                player.addToWallet(a);
                Log.d(tag, "[updateCoins] marker removed and coin added to toremovelist, and addtowallet called ==========================");
                // adds to wallet of player and to coin total
            }
        }
        toRemove(toremove);
        Log.d(tag, "[updateCoinz] is done");
    }

    public void toRemove(ArrayList<Coin> list) {
        for (Coin a: list) {
            for (Coin c: new ArrayList<Coin>(onmapcoins)) {
                if (a.isEqualto(c)) {
                    onmapcoins.remove(a);
                    Log.d(tag, "[toRemove] coin " +a.getId()+ " removed from onmapcoins ==================");
                }
            }
        }
    }
}
