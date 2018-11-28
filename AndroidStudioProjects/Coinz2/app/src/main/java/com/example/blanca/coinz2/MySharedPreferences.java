package com.example.blanca.coinz2;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.google.common.base.Joiner;
import com.mapbox.mapboxsdk.geometry.LatLng;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ResourceBundle;

import static com.example.blanca.coinz2.MapActivity.player;
import static com.mapbox.mapboxsdk.Mapbox.getApplicationContext;

public class MySharedPreferences {
    private static final String tag = "MySharedPreference";
    static final String CURR_USER_NAME = "username";
    static final String CURR_MODE = "current_mode";
    static final String CURR_TIME = "current_time";
    static final String CURR_RADIUS = "current_radius";
   // static final String CURR_LEVEL = "current_level";
    static final String COMMUNITY_LEVEL = "community_level";
    static final String BANKED_COINS = "banked_coins";
    static final String WALLET_COINS = "wallet_coins";
    static final String TOTAL_COINS = "total_coins";
    static final String TOTAL_STEPS = "total_steps";

    static SharedPreferences getSharedPreferences(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    /////////////
    // Setters //
    /////////////

    // Sets current username ========================= //
    public static void setUserName(Context context, String email) {
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString(CURR_USER_NAME, email);
        editor.apply();
        Log.d(tag, "[setUserName]: current username " + email + " saved in MyPreferences.");
    }

    // Sets current 'mode' - whether hard (True) or not (False) ====== //
    public static void setCurrMode(Context context, Boolean mode) {
        String email = getUserName(context);
        String playermodes = getSharedPreferences(context).getString(CURR_MODE, "0");
        // Formatting how the string will be saved in preferences //
        String result = email + ":" + mode.toString(); // format email:level, email:level ...
        String answer = "";

        // if not empty, simply add to existing string, unless username already there with a value in which case we replace the value and the substring
        if (!(playermodes.length()<2)) {
            String[] levels = playermodes.split(",");
            String[] diflevels = levels.clone();
            for (int i=0; i<levels.length; i++) {
                String s = levels[i];
                // if already a string with that username, replace with string with new level
                if (s.contains(email)) {
                    Log.d(tag, "[setCurrMode] string s in playermodes has occurence of username ! ===================");
                    diflevels[i] = result;
                    Log.d(tag, "[setCurrMode] string res " + result + " replaced old string===================");
                } else {
                    diflevels[i] = s;
                }
            }
            answer = Joiner.on(",").join(diflevels);
            Log.d(tag, "[setCurrMode]: current playermodes not empty, " + answer + " added to saved string.");
        } else {
            answer = result;
        }
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString(CURR_MODE, answer);
        editor.apply();
        Log.d(tag, "[setCurrMode]: current playermodes " + answer + " saved in preferences=============");
    }

//    // Set current level ====================================//
//    public static void setCurrLevel(Context context, Integer level) {
//        String email = getUserName(context);
//        String playerlevels = getSharedPreferences(context).getString(CURR_LEVEL, "0");
//        // Formatting how the string will be saved in preferences //
//        String result = email + ":" + level.toString(); // format email:level, email:level ...
//        String answer = "";
//
//        // if not empty, simply add to existing string, unless username already there with a value in which case we replace the value and the substring
//        if (!(playerlevels.length()<2)) {
//            String[] levels = playerlevels.split(",");
//            String[] diflevels = levels;
//            for (Integer i=0; i<levels.length; i++) {
//                String s = levels[i];
//                // if already a string with that username, replace with string with new level
//                if (s.contains(email)) {
//                    Log.d(tag, "[setCurrLevel] string s in playerlevels has occurence of username ! ===================");
//                    diflevels[i] = result;
//                    Log.d(tag, "[setCurrLevel] string res " + result + " replaced old string===================");
//                } else {
//                    diflevels[i] = s;
//                }
//            }
//            answer = Joiner.on(",").join(diflevels);
//            Log.d(tag, "[setCurrLevel]: current playerlevels not empty, " + answer + " added to saved string.");
//        } else {
//            answer = result;
//        }
//        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
//        editor.putString(CURR_LEVEL, answer);
//        editor.apply();
//        Log.d(tag, "[setCurrLevel]: current player levels " + answer + " saved in preferences");
//    }

    // Set community level ===================================//
    public static void setCommunityLevel(Context context, Integer level) {
        String email = getUserName(context);
        String commslevels = getSharedPreferences(context).getString(COMMUNITY_LEVEL, "0");
        String result = email + ":" + level.toString(); // format email:level, email:level ...
        String answer = "";

        // if not empty, simply add to existing string, unless username already there with a value in which case we replace the value and the substring
        if (!(commslevels.length()<2)) {
            String[] levels = commslevels.split(",");
            String[] diflevels = levels;
            for (Integer i=0; i<levels.length; i++) {
                String s = levels[i];
                // if already a string with that username, replace with string with new level
                if (s.contains(email)) {
                    Log.d(tag, "[setCommunityLevel] string s in commslevels has occurence of username ! ===================");
                    diflevels[i] = result;
                    Log.d(tag, "[setCommunityLevel] string res " + result + " replaced old string===================");
                } else {
                    diflevels[i] = s;
                }
            }
            answer = Joiner.on(",").join(diflevels);
            Log.d(tag, "[setCommunityLevel]: current community level not empty, " + answer + " added to saved string.");
        } else {
            answer = result;
        }
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString(COMMUNITY_LEVEL, answer);
        editor.apply();
        Log.d(tag, "[setCommunityLevel]: current community level " + answer + " saved in preferences");
    }

    /////////////
    // Adders //
    /////////////

    private static void add2CoinTotal(Context context) {
        String email = getUserName(context);
        String totalcoins = getSharedPreferences(context).getString(TOTAL_COINS, "0");
        String answer = "";
        // if string not empty
        if (!(totalcoins.length() < 2)) {
            String[] coins = totalcoins.split(",");
            for (Integer i = 0; i < coins.length; i++) {
                // when we get to string holding level for that player
                String s = coins[i];
                if (s.contains(email)) {
                    Log.d(tag, "[add2CoinTotal] string s in totalcoins has occurence of username ! ===================");
                    String ans = email;
                    Integer level = Integer.parseInt(s.split(":")[1]);
                    level += 1;
                    ans = ans + ":" + level;
                    coins[i] = ans;
                    Log.d(tag, "[add2cointotal] ans " + ans + " replaced old string s " + s);
                } else {
                    // otherwise leave string the same
                    coins[i] = s;
                }
            }
            answer = Joiner.on(",").join(coins);
            Log.d(tag, "[add2CoinTotal]: current totalcoins not empty, " + answer + " added to saved string.");
        } else { // if it is empty
            answer = email + ":" + String.valueOf(1);
        }
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString(TOTAL_COINS, answer);
        editor.apply();
        Log.d(tag, "[add2CoinTotal]: final coin string " + answer + " saved in preferences");
    }

    public static void addBankCoin(Context context, Coin coin) {
        String email = getUserName(context);
        String bankedcoins = getSharedPreferences(context).getString(BANKED_COINS, "0");
        String result = email + ":" + coin.stringify(); // format of coin stringify id + "-" + value + "-" + currency;
        Integer counter = 0;

        // checking if saved preferences string is empty
        if (!(bankedcoins.length()<2)) {
            // if not empty, split preference string
            String[] coins = bankedcoins.split(",");
            for (String s:coins) {
                if (s.contains(coin.getId()) && s.contains(email)) {
                    counter++;
                    // if coin already in saved string, counter will not be 0
                }
            }
            Log.d(tag, "[addBankCoin] picked up coin had a total of " + counter + " occurrences in saved string");
            if (counter==0) {
                // if not already in saved preferences for that user, add formatted string to preferences string
                result = bankedcoins + "," + result;
                Log.d(tag, "[addBankCoin]: coin string " + result + " added to non empty banked coins list.");
            } else {
                // if that coin is already saved for that user, keep string as is
                result = bankedcoins;
                Log.d(tag, "[addBankCoin] coin was already in saved banked coins string in preferences =========");
            }
        }
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString(BANKED_COINS, result);
        editor.apply();
        Log.d(tag, "[addBankCoin]: coin string " + result + " saved in preferences");
    }

    // adds string email:coin-details, email:coin-details, for each coin in wallet
    public static void addWalletCoin(Context context, Coin coin) {
        String email = getUserName(context);
        String walletcoins = getSharedPreferences(context).getString(WALLET_COINS, "0");
        String result = email + ":" + coin.stringify(); // format of coin stringify id + "-" + value + "-" + currency;
        int counter = 0;

        if (!(walletcoins.length()<2)) {
            Log.d(tag, "[addWalletCoin] walletcoins in preferences not empty! ===================");
            // as not empty, we split the preference string
            String[] coins = walletcoins.split(",");
            for (String s:coins) {
                if (s.contains(coin.getId()) && s.contains(email)) {
                    counter++;
                    // if coin already in saved string, counter will not be 0
                }
            }
            Log.d(tag, "[addWalletCoin] picked up coin had a total of " + counter + " occurrences in saved string");
            if (counter==0) {
                // if not already in saved preferences for that user, add formatted string to preferences string
                result = walletcoins + "," + result;

                Log.d(tag, "[addWalletCoin]: coin string " + result + " added to non empty wallet coins list.");
            } else {
                // if that coin is already saved for that user, keep string as is
                result = walletcoins;
                Log.d(tag, "[addWalletCoin] coin was already in saved wallet coins string in preferences =========");
            }
        }
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        add2CoinTotal(context);
        editor.putString(WALLET_COINS, result);
        editor.apply();
        Log.d(tag, "[addWalletCoin]: coin string " + result + " saved in preferences");
    }

    /////////////
    // Getters //
    /////////////

    // Get email of current user =====================//
    public static String getUserName(Context context) {
        String ans = getSharedPreferences(context).getString(CURR_USER_NAME, "0");
        return ans;
    }

    // Get total coins for player ====================//
    public static Integer getTotalCoins(Context context, String email) {
        String totals = getSharedPreferences(context).getString(TOTAL_COINS, "0");
        if (totals.length()<2) {
            return 0;
        }
        // have to check for the name and get the value with it
        String[] total = totals.split(","); // as we have formatted the string as "email:mode", "email:mode" ...
        for (String s:total) {
            if (s.contains(email)) {
                Integer actotal = Integer.parseInt(s.split(":")[1]);
                return actotal;
            }
        } // if no string matches name, return 0
        return 0;
    }

    // Get current mode ==============================// where false=standard mode and true=hard mode
    public static Boolean getCurrentMode(Context context, String email) {
        String modes = getSharedPreferences(context).getString(CURR_MODE, "0");
        if (modes.length()<2) {
            return Boolean.FALSE;
        }
        String[] mode = modes.split(","); // as we have formatted the string as "email:mode", "email:mode" ...
        for (String s:mode) {
            if (s.contains(email)) {
                Boolean acMode = Boolean.parseBoolean(s.split(":")[1]);
                return acMode;
            }
        }
        return Boolean.FALSE;
    }

//    // Get current time ==============================//
//    public static Integer getCurrentTime(Context context, String email) {
//        String modes = getSharedPreferences(context).getString(CURR_MODE, "0");
//        if (modes.length()<2) {
//            return 0;
//        }
//        String[] mode = modes.split(","); // as we have formatted the string as "email:mode", "email:mode" ...
//        for (String s:mode) {
//            if (s.contains(CURR_USER_NAME)) {
//                Boolean acMode = Boolean.parseBoolean(s.split(":")[1]);
//                if (acMode=Boolean.TRUE) {
//                    return 1; // for one hour!
//                }
//                return 0;
//            }
//        }
//        return 0; // unless mode is true, as in hard, we return 0 - if it is on hard mode, we return 1 as the one hour coins can stay inactive
//    }
//
//    // Get current radius ============================//
//    public static Integer getCurrentRadius(Context context) {
//        String modes = getSharedPreferences(context).getString(CURR_MODE, "0");
//        if (modes.length()<2) {
//            return 0;
//        }
//        String[] mode = modes.split(","); // as we have formatted the string as "email:mode", "email:mode" ...
//        for (String s:mode) {
//            if (s.contains(CURR_USER_NAME)) {
//                Boolean acMode = Boolean.parseBoolean(s.split(":")[1]);
//                if (acMode==Boolean.TRUE) {
//                    return 150; // for 150 meters!
//                }
//                return 0;
//            }
//        }
//        return 0; // unless mode is true, as in hard, we return 0 - if it is on hard mode, we return 150 as the radius for inactive coins
//    }

//    // Get user level ================================//
//    public static Integer getCurrentLevel(Context context , String email) {
//        String level = getSharedPreferences(context).getString(CURR_LEVEL, "0");
//        if (level.length()< 2) {
//            return 0;
//        }
//        String[] levels = level.split(",");
//        for (String s:levels) {
//            if (s.contains(email)) {
//                Integer actlevel = Integer.parseInt(s.split(":")[1]);
//                return actlevel;
//            }
//        }
//        return 0;
//    }

    // Get community level ============================//
    public static Integer getCommunityLevel(Context context, String email) {
        String level = getSharedPreferences(context).getString(COMMUNITY_LEVEL, "0");
        if (level.length()< 2) {
            return 0;
        }
        String[] levels = level.split(",");
        for (String s:levels) {
            if (s.contains(email)) {
                Integer actlevel = Integer.parseInt(s.split(":")[1]);
                return actlevel;
            }
        }
        return 0;
    }

    // Get wallet coins ==============================//
    public static ArrayList<Coin> getWalletCoins(Context context, String email) {
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
            if (s.contains(email)) {
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

    public static ArrayList<Coin> getBankedCoins(Context context, String email) {
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
            if (s.contains(email)) {
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
