package com.example.blanca.coinz2;

import android.location.Location;
import android.location.LocationManager;
import android.util.Log;
import com.mapbox.geojson.Feature;
import com.mapbox.geojson.FeatureCollection;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.annotations.Icon;
import com.mapbox.mapboxsdk.annotations.MarkerViewOptions;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapboxMap;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static com.example.blanca.coinz2.MapActivity.player;
import static com.mapbox.mapboxsdk.Mapbox.getApplicationContext;

class MapCoinz {

    private static final String tag = "MapCoinz";

    private FeatureCollection featureCollection;
    private ArrayList<Coin> onmapcoins;

    public MapCoinz(FeatureCollection mapCoinz) {
        this.featureCollection = mapCoinz;
        Log.d(tag, "Coinz initialised correctly=========================");
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
        Log.d(tag, "[updateCoins] THE size of onmapcoins is " + onmapcoins.size());
        ArrayList<Coin> toremove = new ArrayList<>();

        boolean activeChallenge = isChallengeOn();

        // Check if the active challenge has expired, if the challenge was active
        if(activeChallenge){
            if(isChallengeExpired()){
                resetActiveChallenge();
                activeChallenge = false;
                Log.d(tag, "[updateCoins]: Challenge was reset because time had passed");
            }
        }

        boolean difficultMode = player.getCurrentMode();

        for (Coin a : onmapcoins) {
            Double dist = Helpers.dist(location, a.getLatLng());
            if ((dist <= 25)) {
                // If there is an active challenge check whether coin is pickable
                if(activeChallenge){
                    // If not disabled give it to player
                    if(!a.isDisabled()){
                        deleteCoin(mapboxMap, a);
                        toremove.add(a);
                        player.addToWallet(a);
                        Log.d(tag, "[updateCoins]: active challenge is on and coin was not disabled");
                    }else{
                        Log.d(tag, "[updateCoins]: active challenge is on and coin was disabled");
                    }
                }
                // If no active challenge but difficult mode
                else if(difficultMode){
                    // Give coin to player
                    deleteCoin(mapboxMap, a);
                    toremove.add(a);
                    player.addToWallet(a);

                    // Give added bonus to player
                    player.addBonusGold(a);

                    // Apply the active challenge with the current coin
                    applyActiveChallenge(mapboxMap,a);
                    Log.d(tag, "[updateCoins]: Difficulty mode was on and there was no active challenge so coin just applied a new active challenge");
                    break;
                }
                // If nothing is active then just give it to player
                else {
                    deleteCoin(mapboxMap, a);
                    toremove.add(a);
                    player.addToWallet(a);
                    Log.d(tag, "[updateCoins] marker removed and coin added to toremovelist, and addtowallet called ==========================");
                }
            }
        }
        toRemove(toremove);
        Log.d(tag, "[updateCoinz] is done");
    }

    private boolean isChallengeExpired() {
        String challenge = MySharedPreferences.getCurrChallenge(getApplicationContext(), player.getEmail());
        String strDate = challenge.split("/")[2];

        // Get current date and challenge data
        Date date = new Date();
        long now = date.getTime();
        long then = Long.parseLong(strDate);
        Log.d(tag, "[isChallengeExpired]: now is: "+ now);
        Log.d(tag, "[isChallengeExpired]: then is: "+ then);
        Log.d(tag, "[isChallengeExpired]: DIF IS :"+ String.valueOf(now-then));

        // If an hour has passed then return true, otherwise false (ms)
        return now - then > 3600000;
    }

    private void applyActiveChallenge(MapboxMap map, Coin a) {
        ArrayList<Coin> toremove = new ArrayList<>();
        // Get coin information
        String lat = String.valueOf(a.getLatLng().getLatitude());
        String lng = String.valueOf(a.getLatLng().getLongitude());

        // Get current date
        Date date = new Date();
        String strDate = String.valueOf(date.getTime());

        // Construct shared Pref string and save
        String answer = lat+ "/" +lng+ "/" +strDate;
        Log.d(tag, "[applyActiveChallenge] String to send to setcurrchallenge " + answer );
        MySharedPreferences.setCurrChallenge(getApplicationContext(), answer);

        // Make nearby coins disabled
        for (Coin coin : onmapcoins){
            // Get coin location from latlng
            Location coinLoc = new Location(LocationManager.GPS_PROVIDER);
            coinLoc.setLatitude(coin.getLatLng().getLatitude());
            coinLoc.setLongitude(coin.getLatLng().getLongitude());
            Double distance = Helpers.dist(coinLoc, a.getLatLng());
            if(distance <= 150){
                // Disable coin
                coin.setAsDisabled(true);

                // Delete marker from map
                deleteCoin(map, coin);
                toremove.add(coin);
                Log.d(tag, "[applyActiveChallenge] removing newly disabled coin");

                // Remake marker
                MarkerViewOptions marker = makeMarker(coin, Helpers.getDisabledIcon());
                coin.setMarkerViewOptions(marker);
                map.addMarker(marker);
                Log.d(tag, "[applyActiveChallenge] adding new disabled marker to map");
            }
        }
        toRemove(toremove);
    }

    private MarkerViewOptions makeMarker(Coin coin, Icon dicon) {
        String snippet = coin.getValue();
        String title = coin.getCurrency();
        Icon icon = dicon;
        LatLng latLng = coin.getLatLng();
        return new MarkerViewOptions().title(title).snippet(snippet).icon(icon).position(latLng);

    }

    // Reset challenge
    private void resetActiveChallenge() {
        // Remove active challenge
        MySharedPreferences.setCurrChallenge(getApplicationContext(), "");

        // Make all coins enabled
        for (Coin coin : onmapcoins){
            if(coin.isDisabled()){
                coin.setAsDisabled(false);

                // Resetting coin icon
                Icon icon = Helpers.getIcon(coin);

                // Implement marker =========================================//
                MarkerViewOptions markerViewOptions = coin.getMarkerViewOptions();
                markerViewOptions.icon(icon);
                coin.setMarkerViewOptions(markerViewOptions);
                Log.d(tag, "[resetActiveChallenge] coin marker reset!");
            }
        }
    }

    // Returns true if challenge is active
    private boolean isChallengeOn() {
        String challenge = MySharedPreferences.getCurrChallenge(getApplicationContext(), player.getEmail());
        if(challenge.length()<2){
            Log.d(tag, "[isChallengeOn]: current challenge is not active");
            return false;
        }else{
            Log.d(tag, "[isChallengeOn]: current challenge is active");
            return true;
        }
    }

    private void toRemove(ArrayList<Coin> list) {
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
