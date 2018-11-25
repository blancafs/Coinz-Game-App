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


import java.util.ArrayList;

import io.opencensus.internal.StringUtil;

import static com.example.blanca.coinz2.MapActivity.player;
import static com.mapbox.mapboxsdk.Mapbox.getApplicationContext;

public class MapCoinz {

    private static final String tag = "MapCoinz";

    private static FeatureCollection featureCollection;
    private static ArrayList<Coin> onmapcoins;
    public static ArrayList<Coin> bankedcoins;
    //= MySharedPreferences.getBankedCoins(getApplicationContext());
    public static ArrayList<Coin> walletcoins;
    //= MySharedPreferences.getWalletCoins(getApplicationContext());

    public MapCoinz(FeatureCollection mapCoinz) {
        featureCollection = mapCoinz;
        Log.d(tag, "Coinz initialised correctly=========================");
    }

    public static void addMarkers(MapboxMap mapboxMap) {
        Log.d(tag, "[addMarkers] started ====================================");
        onmapcoins = new ArrayList<>();
        Log.d(tag, "[addMarkers] bankedcoins has length " + bankedcoins.size() + " walletcoins has size " + walletcoins.size());

        for (Feature feature : featureCollection.features()) {
            // Coin features ==============================================//
            String id = feature.getStringProperty("id");
            String value = feature.getStringProperty("value");
            String currency = feature.getStringProperty("currency");
            String markersymbol = feature.getStringProperty("marker-symbol");
            String markercolor = feature.getStringProperty("marker-color");

            // Snippet and title strings, individual to each feature =====//
            String snippet = value;
            String title = currency;

            // Getting coordinates
            Point point = (Point) feature.geometry();
            LatLng latLng = new LatLng(point.latitude(), point.longitude());

            // Creating coin object
            Coin coin = new Coin(id, value, currency);
            coin.setLocation(latLng);

            // Coin icon
            Icon icon = Helpers.getIcon(coin);

            // Implement marker =========================================//
            MarkerViewOptions markerViewOptions = new MarkerViewOptions().title(title).snippet(snippet).icon(icon).position(latLng);
            coin.setMarkerViewOptions(markerViewOptions);

           //  Finally adding coin to arraylist - only if not in bank or wallet!
            if (MySharedPreferences.getBankedCoins(getApplicationContext()).isEmpty() && MySharedPreferences.getWalletCoins(getApplicationContext()).isEmpty()) {
                mapboxMap.addMarker(markerViewOptions);
                onmapcoins.add(coin);
                Log.d(tag, " added coin when bankedcoins and walletcoins empty! ==========================================");
            }
            // to be able to compare ids, so as to check whether they have already picked up the coin, and only show those markers they have not!
            ArrayList<String> bnwcoins_ids = new ArrayList<>();
            for (Coin c : bankedcoins) {
                bnwcoins_ids.add(c.getId());
            }
            for (Coin c : walletcoins) {
                bnwcoins_ids.add(c.getId());
            }
            //  if coin not in banked coins AND not in wallet coins, add it
            if (!bnwcoins_ids.contains(coin.getId())) {
                mapboxMap.addMarker(markerViewOptions);
                onmapcoins.add(coin);
                Log.d(tag, "[addMarkers] coin added to mapcoins as not in wallet or bank, id " + coin.getId() + " ");
                Log.d(tag, "[addMarkers] Marker created successfully ===========================");
                Log.d(tag, "[addMarkers] position = " + latLng.toString() + " =====================================");
            }
        }
    }

    public static void deleteCoin(MapboxMap mapboxMap, Coin coin) {
        mapboxMap.removeMarker(coin.getMarkerViewOptions().getMarker());
    }

    public static void updateCoins(MapboxMap mapboxMap, Location location) {
        for (Coin a : new ArrayList<Coin>(onmapcoins)) {
            Double dist = Helpers.dist(location, a.getLatLng());
            if (dist <= 25) {
                deleteCoin(mapboxMap, a);
                onmapcoins.remove(a);
                Log.d(tag, "[updateCoins] added wallet coin to players wallet");
                player.addToWallet(a);
                walletcoins.add(a);
                MySharedPreferences.addWalletCoin(getApplicationContext(), a);
                Log.d(tag, "[updateCoins] added wallet coin to preferences");
            }
        }
    }
}
