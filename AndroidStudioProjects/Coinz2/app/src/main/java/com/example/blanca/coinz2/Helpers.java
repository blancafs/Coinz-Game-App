package com.example.blanca.coinz2;

import android.annotation.TargetApi;
import android.content.Context;
import android.location.Location;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;


import com.google.firebase.auth.FirebaseAuth;
import com.mapbox.mapboxsdk.annotations.Icon;
import com.mapbox.mapboxsdk.annotations.IconFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;


import static com.mapbox.mapboxsdk.Mapbox.getApplicationContext;

public class Helpers {
    // VARIABLES
    private static final String tag = "Helpers";

    // METHODS
    // NEEDS IMPLEMENTATION
    public static String getTodaysURL() {
        // Set today's URL form Json repo and get json file
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        Date date = new Date();
        String sdate = dateFormat.format(date);
        String ans = "https://homepages.inf.ed.ac.uk/stg/coinz/"+sdate+"/coinzmap.geojson";
        return ans;
    }

    public static File getTodaysFile() {
        Context context = getApplicationContext();
        File path = context.getFilesDir();
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        Date date = new Date();
        String name = dateFormat.format(date);
        String fileName = "JSON_" + name + ".txt";
        Log.d(tag, "[getTodaysFile]: " + fileName + " was returned");
        return new File(path, fileName);
    }

    //
    public static void writeToFile(String fileName, String data) {
        Context context = getApplicationContext();
        File path = context.getFilesDir();
        File file = new File(path, fileName);
        FileOutputStream stream = null;
        // Open a file stream
        try {
            if (!file.exists())
                file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            stream = new FileOutputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        // Try and write the string data
        try {
            stream.write(data.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                stream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @TargetApi(Build.VERSION_CODES.O)
    @RequiresApi(api = Build.VERSION_CODES.O)
    public static String readFile(File f) throws FileNotFoundException {
        Log.d(tag, "[readFile]: Trying to read" + f.getName());
        Scanner scanner = new Scanner(f);
        String text = scanner.useDelimiter("\\A").next();

        Log.d(tag, "[readFile]: Returning contents as string");
        return text;
    }

    public static Icon getIcon(Coin coin) {
        IconFactory iconFactory = IconFactory.getInstance(getApplicationContext());
        switch (coin.getCurrency()) {
            //return correct icon design depending on currency
            case ("PENY"):
                return iconFactory.fromResource(R.mipmap.penny_icon);
            case ("QUID"):
                return iconFactory.fromResource(R.mipmap.quid_icon);
            case ("SHIL"):
                return iconFactory.fromResource(R.mipmap.shilling_icon);
            case ("DOLR"):
                return iconFactory.fromResource(R.mipmap.dollar_icon);
        }
        return iconFactory.fromResource(R.drawable.mapbox_info_icon_default);
    }

    public static double dist(Location location, LatLng latlng) {
        double lat1 = location.getLatitude();
        double lng1 = location.getLongitude();
        double lat2 = latlng.getLatitude();
        double lng2 = latlng.getLongitude();
        double earthRadius = 6371000; //meters
        double dLat = Math.toRadians(lat2 - lat1);
        double dLng = Math.toRadians(lng2 - lng1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(dLng / 2) * Math.sin(dLng / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double dist = earthRadius * c;
        return dist;
    }

    public static ArrayList<Double> getCurrencies(String coindata) {

        ArrayList<Double> answer = new ArrayList<>(); // format is SHIL DOLR QUID PENY

        String[] s = coindata.split(","); // format is SHIL DOLR QUID PENY
        String shilcur = s[4].trim().split(":")[2].trim(); // s[4] = "rates": {"SHIL": cur, so we take the third string when split by :
        Log.d(tag, "[getCurrencies] shilcur is " +shilcur);
        answer.add(Double.parseDouble(shilcur));

        // apply same concept for all conversions
        String dolrcur =s[5].trim().split(":")[1].trim(); // s[5] = "DOLR": curr
        Log.d(tag, "[getCurrencies] dolrcur is " +dolrcur);
        answer.add(Double.parseDouble(dolrcur));

        String quidcur = s[6].trim().split(":")[1].trim(); // s[6] = "QUID : curr
        Log.d(tag, "[getCurrencies] quidcur is " +quidcur);
        answer.add(Double.parseDouble(quidcur));

        String penycur = s[7].trim().split(":")[1].trim().replace("}", "");
        Log.d(tag, "[getCurrencies] penycur is " +penycur);
        answer.add(Double.parseDouble(penycur));

        return answer;
    }

    public static double goldTransform(String value, String currency, ArrayList<Double> conversions) {
        double val = Double.parseDouble(value);
        double goldconv = 0;
        switch (currency) {
            case "SHIL":
                goldconv = conversions.get(0);
            case "DOLR":
                goldconv = conversions.get(1);
            case "QUID":
                goldconv = conversions.get(2);
            case "PENY":
                goldconv = conversions.get(3);
        }
        return val*goldconv;
    }

}

