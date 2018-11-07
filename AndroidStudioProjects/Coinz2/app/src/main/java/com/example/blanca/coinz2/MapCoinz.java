package com.example.blanca.coinz2;

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

import static com.mapbox.mapboxsdk.Mapbox.getApplicationContext;

public class MapCoinz {

    private static final String tag = "MapCoinz";

    private FeatureCollection featureCollection;

    public MapCoinz(FeatureCollection mapCoinz) {
        this.featureCollection = mapCoinz;
    }


    public void addMarkers(MapboxMap mapboxMap) {
        Log.d(tag, "[addMarkers] started ====================================");

        for (Feature feature : featureCollection.features())  {
            // Snippet and title strings, individual to each feature =====//
            String snippet = "snippet";
            String title = "title";

            // Icon default ==============================================//
            IconFactory iconFactory = IconFactory.getInstance(getApplicationContext());
            Icon icon = iconFactory.fromResource(R.drawable.mapbox_marker_icon_default);

            // Position of marker =======================================//
            Point point = (Point) feature.geometry();
            LatLng latLng = new LatLng(point.latitude(), point.longitude());

            // Implement marker =========================================//
            MarkerViewOptions markerViewOptions= new MarkerViewOptions().title(title).snippet(snippet).icon(icon).position(latLng);
            mapboxMap.addMarker(markerViewOptions);
            Log.d(tag, "[addMarkers] Marker created successfully ===========================");
            Log.d(tag, "[addMarkers] position = " + latLng.toString() + " =====================================");
        }
    }

}
