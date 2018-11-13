package com.example.blanca.coinz2;

import android.annotation.TargetApi;
import android.location.Location;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

// Map related imports
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.android.core.permissions.PermissionsListener;
import com.mapbox.android.core.permissions.PermissionsManager;

// Location related imports
import com.mapbox.android.core.location.LocationEngine;
import com.mapbox.android.core.location.LocationEngineListener;
import com.mapbox.android.core.location.LocationEnginePriority;
import com.mapbox.android.core.location.LocationEngineProvider;
import com.mapbox.mapboxsdk.plugins.locationlayer.LocationLayerPlugin;
import com.mapbox.mapboxsdk.plugins.locationlayer.modes.CameraMode;
import com.mapbox.mapboxsdk.plugins.locationlayer.modes.RenderMode;

// JSON related imports
import com.mapbox.geojson.Feature;
import com.mapbox.geojson.FeatureCollection;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback, LocationEngineListener, PermissionsListener{

    // Mapbox Variables =================//
    private static final String tag = "MapActivity";
    private MapView mapView;
    private MapboxMap map;

    // Location Variables ===============//
    private PermissionsManager permissionsManager;
    private LocationEngine locationEngine;
    private LocationLayerPlugin locationLayerPlugin;
    private Location originLocation;

    // Data Variables ===================//
    private String todayURL;
    private final String dataFile = "";
    private final String preferencesFile = "MyPrefsFile"; // for storing preferences
    private final String testFiil= "test.txt";
    private String coinData = "";

    // Public Access FeatureCollection ==//
    private MapCoinz mapCoinz;

    // Navigation Bar variables =========//
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private Toolbar toolbar;

    // Functional Variables =============//
    //private Player player;
    //private LocatDatabase locaDatabase;


    //private NavigationView navView;
    //private MenuItem toAccount;
    //private MenuItem toWallet;
    //private MenuItem toSettings;
    //private MenuItem toExit;

    // -------------- ON METHODS ---------------------------------------------//
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        //setSupportActionBar();

        // Load map instance from Mapbox
        Mapbox.getInstance(this, "pk.eyJ1IjoiYmxhbmNhZnMiLCJhIjoiY2puYnZydXB3MDR2cjNxc2ZkYzl5cWcwayJ9.o96HYUQpA58DXK3UxZKi3g");
        mapView = findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);

        // Setting up toolbar
        toolbar = (Toolbar) findViewById(R.id.nav_actionbar);
        setSupportActionBar(toolbar);

        // Setting up navigation bar
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @TargetApi(Build.VERSION_CODES.O)
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onStart() {
        super.onStart();
        mapView.onStart();

        // Set up json data ==================//
        setUpData();
        Log.d(tag, "[onStart] data set up ====================================");

        // Set the global  feature collection
        mapCoinz = new MapCoinz(FeatureCollection.fromJson(coinData));
        Log.d(tag, "[onStart] mapCoinz feature collection created");

        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(MapboxMap mapboxMap) {
                //  Adding coins to map using external class MapCoinz ==============//
                mapCoinz.addMarkers(map);

            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }
    // ---------------- Navigation Bar methods ------------------------------------//
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // ---------------- Data retrieval and set up -------------------------------- //

    // makes coindata the json string from todays file ==== //
    private void downloadData() {
        // Today's URL
        Log.d(tag,"[downloadData]: Downloading data =====================================");
        todayURL = Helpers.getTodaysURL();
        Log.d(tag, "[downloadData] todayURL = " + todayURL + "=========================");

        // Download the day's coins
        DownloadFileTask myTask = new DownloadFileTask();
        String result = null;

        // Send download request and use answer as coin data
        try {
            Log.d(tag, "[downloadData] mytask started with url: "+ todayURL + "================================");
            result = myTask.execute(todayURL).get();
            coinData = result;
            Log.d(tag, "[downloadData] mytask finished and coindata is live =========================================");
        } catch (InterruptedException e) {
            Log.d(tag, "[downloadData]: execution exception, coinData is null ==========================================");
            e.printStackTrace();
        } catch (ExecutionException e) {
            Log.d(tag, "[downloadData]: execution exception, coinData is null=========================================");
            e.printStackTrace();
        }
    }

    // if file never downloaded, download data, else read file and make coin data ==== //
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void setUpData() {
        // Get todays date on a file and output log
        Log.d(tag, "[setUpData]: setting up is starting !!!!!!!!=====================================================");
        File file = Helpers.getTodaysFile();
        Log.d(tag, "[setUpData]: " + file.getAbsolutePath() + " was returned. ===================================");

        // Check if todays data in memory already
        if (file.exists()) {
            try {
                // Get the json string from the file
                Log.d(tag, "[setUpData]: JSON file was found in memory  ==============================================");
                coinData = Helpers.readFile(file);
                Log.d(tag, "[setUpData]: JSON File was read ==================================================");

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

        }else{

            Log.d(tag, "[setUpData]: Todays file does not exist, proceeding with download ==================================================");
            downloadData();

            try {
                // Create a new file for today's coins
                file.createNewFile();
                Log.d(tag, "[setUpData]: New File was created ==================================================");
            } catch (IOException e) {
                Log.d(tag, "[setUpData]: New File could not be created ==================================================");
                e.printStackTrace();
            }

            // Write the data to file
            Helpers.writeToFile(file.getName(), " "+coinData );

            // Set the global feature collection
           // coinzCollection = new CoinzCollection(FeatureCollection.fromJson(coinData));
            Log.d(tag, "[setUpData]: coinData was updated ==================================================");
        }
    }

    @Override
    public void onConnected() {
        Log.d(tag, "[onConnected] requesting location updates ==================================================");
        locationEngine.requestLocationUpdates();
    }

    @Override
    public void onLocationChanged(Location location) {
        if (location == null) {
            Log.d(tag, "[onLocationChanged] location is null ==================================================");
        } else {
            Log.d(tag, "[onLocationChanged] location is not null ==================================================");
            originLocation = location;
            setCameraPosition(location);
        }

        /*Boolean pickedUp = player.searchForCoins(location);
            if(pickedUp){
                mapView.getMapAsync(new OnMapReadyCallback() {
                    @Override
                    public void onMapReady(MapboxMap mapboxMap) {
                        // Updata the status of map and player
                        player.setLocation(new LatLng(location.getLatitude(), location.getLongitude()));
                        coinzCollection.updateMapPlayerState(map, player); */
    }

    @Override
    public void onExplanationNeeded(List<String> permissionsToExplain) {
        Log.d(tag, "[onExplanationNeeded] Permissions: " + permissionsToExplain.toString() + "==================================================");
        //present toast or dialog
    }

    @Override
    public void onPermissionResult(boolean granted) {
        Log.d(tag, "[onPermissionResult] granted == " + granted + "==================================================");
        if (granted) {
            enableLocation();
        } else {
            // Open dialogue with user
        }
    }

    @Override
    public void onMapReady(MapboxMap mapboxMap) {
        if (mapboxMap == null) {
            Log.d(tag, "[onMapReady] mapBox is null ==================================================");
        } else {
            map = mapboxMap;
            // Set user interface options
            map.getUiSettings().setCompassEnabled(true);
            map.getUiSettings().setZoomControlsEnabled(true);

            // Make location information available
            enableLocation();
        }
    }

    private void enableLocation(){
        if (PermissionsManager.areLocationPermissionsGranted(this)) {
            Log.d(tag, "[enableLocation] Permissions are granted ==================================================");
            initializeLocationEngine();
            initializeLocationLayer();
        } else {
            Log.d(tag, "[enableLocation] Permissions are not granted) ==================================================");
            permissionsManager = new PermissionsManager(this);
            permissionsManager.requestLocationPermissions(this);
        }
    }

    @SuppressWarnings("MissingPermission")
    private void initializeLocationEngine() {
        locationEngine = new LocationEngineProvider(this).obtainBestLocationEngineAvailable();
        locationEngine.setInterval(5000); //at least every five seconds
        locationEngine.setFastestInterval(1000); //every second at most
        locationEngine.setPriority(LocationEnginePriority.HIGH_ACCURACY);
        locationEngine.activate();

        Location lastLocation = locationEngine.getLastLocation();
        if (lastLocation != null) {
            originLocation = lastLocation;
            setCameraPosition(lastLocation);
        } else {
            locationEngine.addLocationEngineListener(this);
        }
    }


    private void initializeLocationLayer() {
        if (mapView == null) {
            Log.d(tag, "[initializeLocationLayer] mapView is null ==================================================");
        } else {
            if (map == null) {
                Log.d(tag, "[initializeLocationLayer] map is null ==================================================");
            } else {
                locationLayerPlugin = new LocationLayerPlugin(mapView, map, locationEngine);
                locationLayerPlugin.setLocationLayerEnabled(true);
                locationLayerPlugin.setCameraMode(CameraMode.TRACKING);
                locationLayerPlugin.setRenderMode(RenderMode.NORMAL);
            }
        }
    }

    private void setCameraPosition(Location location) {
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        map.animateCamera(CameraUpdateFactory.newLatLng(latLng));
    }

}
