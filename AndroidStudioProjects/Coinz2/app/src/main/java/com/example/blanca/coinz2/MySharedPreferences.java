package com.example.blanca.coinz2;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.mapbox.mapboxsdk.geometry.LatLng;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class MySharedPreferences {
    private static final String tag = "MySharedPreference";
    static String CURR_USER_NAME = "username";
    static final String CURR_MODE = "current_mode";
    static final String CURR_TIME = "current_time";
    static final String CURR_RADIUS = "current_radius";
    static final String CURR_LEVEL = "current_level";
    static final String COMMUNITY_LEVEL = "community_level";
    static final String BANKED_COINS = "banked_coins";
    static final String WALLET_COINS = "wallet_coins";


    static SharedPreferences getSharedPreferences(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    /////////////
    // Setters //
    /////////////

    // Sets current username ========================= //
    public static void setUserName(String email) {
        CURR_USER_NAME = email;
        Log.d(tag, "[setUserName]: current username " + CURR_USER_NAME + " saved in MyPreferences.");
    }

    // Sets current 'mode' - whether hard (True) or not (False) ====== //
    public static void setCurrMode(Context context, Boolean mode) {
        String modes = getSharedPreferences(context).getString(CURR_MODE, "0");
        String result = CURR_USER_NAME + ":" + mode.toString();
        if (!(modes.length()<2)) {
            result = modes + "," + result;
            Log.d(tag, "[setCurrMode]: current modes was not empty, " + result + " added to saved string.");
        }
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString(CURR_MODE, result);
        editor.apply();
        Log.d(tag, "[setCurrMode]: current mode " + result + " saved in preferences");
    }

    // Set current level ====================================//
    public static void setCurrLevel(Context context, Integer level) {
        String levels = getSharedPreferences(context).getString(CURR_LEVEL, "0");
        // Formatting how the string will be saved in preferences //
        String result = CURR_USER_NAME + ":" + level.toString(); // format email:level, email:level ...
        if (!(levels.length()<2)) {
            result = levels + "," + result;
            Log.d(tag, "[setCurrLevel]: current level was not empty, " + level + " added to saved string in preferences.");
        }
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString(CURR_LEVEL, result);
        editor.apply();
        Log.d(tag, "[setCurrLevel]: current level " + level + " added to saved string in preferences.");
    }

    // Set community level ===================================//
    public static void setCommunityLevel(Context context, Integer level) {
        String commslevels = getSharedPreferences(context).getString(COMMUNITY_LEVEL, "0");
        String result = CURR_USER_NAME + ":" + level.toString();
        if (!(commslevels.length()<2)) {
            result = commslevels + "," + result;
            Log.d(tag, "[setCommunityLevel]: current community level not empty, " + result + " added to saved string.");
        }
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString(COMMUNITY_LEVEL, result);
        editor.apply();
        Log.d(tag, "[setCommunityLevel]: current community level " + result + " saved in preferences");
    }

    /////////////
    // Adders //
    /////////////

    public static void addBankCoin(Context context, Coin coin) {
        String bankedcoins = getSharedPreferences(context).getString(BANKED_COINS, "0");
        String result = CURR_USER_NAME + ":" + coin.stringify(); // format of coin stringify id + "-" + value + "-" + currency;
        if (!(bankedcoins.length()<2)) {
            result = bankedcoins + "," + result;
            Log.d(tag, "[addBankCoin]: coin string " + result + " added to non empty banked coins list.");
        }
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString(BANKED_COINS, result);
        editor.apply();
        Log.d(tag, "[addBankCoin]: coin string " + result + " saved in preferences");
    }

    // adds string email:coin-details, email:coin-details, for each coin in wallet
    public static void addWalletCoin(Context context, Coin coin) {
        String walletcoins = getSharedPreferences(context).getString(WALLET_COINS, "0");
        String result = CURR_USER_NAME + ":" + coin.stringify(); // format of coin stringify id + "-" + value + "-" + currency;
        if (!(walletcoins.length()<2)) {
            result = walletcoins + "," + result;
            Log.d(tag, "[addWalletCoin]: coin string " + result + " added to non empty wallet coins list.");
        }
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString(WALLET_COINS, result);
        editor.apply();
        Log.d(tag, "[addWalletCoin]: coin string " + result + " saved in wallet coins in preferences");
    }

    /////////////
    // Getters //
    /////////////

    // Get email of current user =====================//
    public static String getUserName() {
        return CURR_USER_NAME;
    }

    // Get current mode ==============================// where false=standard mode and true=hard mode
    public static Boolean getCurrentMode(Context context) {
        String modes = getSharedPreferences(context).getString(CURR_MODE, "0");
        if (modes.length()<2) {
            return Boolean.FALSE;
        }
        String[] mode = modes.split(","); // as we have formatted the string as "email:mode", "email:mode" ...
        for (String s:mode) {
            if (s.contains(CURR_USER_NAME)) {
                Boolean acMode = Boolean.parseBoolean(s.split(":")[1]);
                return acMode;
            }
        }
        return Boolean.FALSE;
    }

    // Get current time ==============================//
    public static Integer getCurrentTime(Context context) {
        String modes = getSharedPreferences(context).getString(CURR_MODE, "0");
        if (modes.length()<2) {
            return 0;
        }
        String[] mode = modes.split(","); // as we have formatted the string as "email:mode", "email:mode" ...
        for (String s:mode) {
            if (s.contains(CURR_USER_NAME)) {
                Boolean acMode = Boolean.parseBoolean(s.split(":")[1]);
                if (acMode=Boolean.TRUE) {
                    return 1; // for one hour!
                }
                return 0;
            }
        }
        return 0; // unless mode is true, as in hard, we return 0 - if it is on hard mode, we return 1 as the one hour coins can stay inactive
    }

    // Get current radius ============================//
    public static Integer getCurrentRadius(Context context) {
        String modes = getSharedPreferences(context).getString(CURR_MODE, "0");
        if (modes.length()<2) {
            return 0;
        }
        String[] mode = modes.split(","); // as we have formatted the string as "email:mode", "email:mode" ...
        for (String s:mode) {
            if (s.contains(CURR_USER_NAME)) {
                Boolean acMode = Boolean.parseBoolean(s.split(":")[1]);
                if (acMode==Boolean.TRUE) {
                    return 150; // for 150 meters!
                }
                return 0;
            }
        }
        return 0; // unless mode is true, as in hard, we return 0 - if it is on hard mode, we return 150 as the radius for inactive coins
    }

    // Get user level ================================//
    public static Integer getCurrentLevel(Context context) {
        String level = getSharedPreferences(context).getString(CURR_LEVEL, "0");
        if (level.length()< 2) {
            return 0;
        }
        String[] levels = level.split(",");
        for (String s:levels) {
            if (s.contains(CURR_USER_NAME)) {
                Integer actlevel = Integer.parseInt(s.split(":")[1]);
                return actlevel;
            }
        }
        return 0;
    }

    // Get community level ============================//
    public static Integer getCommunityLevel(Context context) {
        String level = getSharedPreferences(context).getString(COMMUNITY_LEVEL, "0");
        if (level.length()< 2) {
            return 0;
        }
        String[] levels = level.split(",");
        for (String s:levels) {
            if (s.contains(CURR_USER_NAME)) {
                Integer actlevel = Integer.parseInt(s.split(":")[1]);
                return actlevel;
            }
        }
        return 0;
    }

    // Get wallet coins ==============================//
    public static ArrayList<Coin> getWalletCoins(Context context) {
        String coins = getSharedPreferences(context).getString(WALLET_COINS, "0");
        ArrayList<Coin> empty = new ArrayList<Coin>();
        if (coins.length()<2) {
            Log.d(tag, "[getWalletCoins] no coins in wallet coins string in preferences.");
            return empty; // if nothing in string
        }
        // make string array with all contents of walletcoins
        String[] walletcoins = coins.split(",");
        ArrayList<Coin> finalcoins = new ArrayList<Coin>();

        for (String s:walletcoins) {
            // check for given email, if present keep string and get coin, then add it to arraylist
            if (s.contains(CURR_USER_NAME)) {
                String coinstring = s.split(":")[1];
                String[] features = coinstring.split("/"); // coin string format email:id-value-currency
                Coin walcoin = new Coin(features[0],features[1], features[2]); // Coin(String id, String value, String currency)
                Log.d(tag, "[getWalletCoins] stringify of walcoin gives " + walcoin.stringify() + "==========================");
                finalcoins.add(walcoin);
                Log.d(tag, "[getWalletCoins] coin retrieved from wallet memory, added to walletcoins arraylist.");
            }
        }
        return finalcoins;
    }

    public static ArrayList<Coin> getBankedCoins(Context context) {
        String coins = getSharedPreferences(context).getString(BANKED_COINS, "0");
        ArrayList<Coin> empty = new ArrayList<Coin>();
        if (coins.length()<2) {
            Log.d(tag, "[getBankedCoins] no coins in banked coins string in preferences.");
            return empty;
        }
        // make string array with all contents of walletcoins
        String[] bankedcoins = coins.split(",");
        ArrayList<Coin> finalcoins = new ArrayList<Coin>();

        for (String s:bankedcoins) {
            // check for given email, if present keep string and get coin, then add it to arraylist
            if (s.contains(CURR_USER_NAME)) {
                String coinstring = s.split(":")[1];
                String[] features = coinstring.split("/"); // coin string format email:id-value-currency
                Coin walcoin = new Coin(features[0], features[1], features[2]); // Coin(String id, String value, String currency)
                Log.d(tag, "[getBankedCoins] walcoin created, walcoin stringify " + walcoin.stringify() + " =========================");
                finalcoins.add(walcoin);
                Log.d(tag, "[getBankedCoins] coin " + features[0] + "retrieved from wallet memory, added to bankedcoins arraylist.");
            }
        }
        return finalcoins;
    }
}
