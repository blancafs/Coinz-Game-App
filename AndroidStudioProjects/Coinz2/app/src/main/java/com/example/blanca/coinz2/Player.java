package com.example.blanca.coinz2;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.lang.reflect.Array;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static com.mapbox.mapboxsdk.Mapbox.getApplicationContext;

public class Player {

    private final String tag = "Player";
    private String email;
    private int communityLevel;
    private int level;
    private ArrayList<String> friends;
    private ArrayList<Coin> walletCoinz;
    private ArrayList<Coin> bankedCoinz;
    private ArrayList<Coin> specialCoinz;
    private int totalSteps;
    private int totalSent;
    private int totalCoins;
    private double gold;

    public Player(Context context, String email) {
        this.email = email;
        this.walletCoinz = MySharedPreferences.getWalletCoins(context, email);
        this.bankedCoinz = MySharedPreferences.getBankedCoins(context, email);
        this.totalCoins = MySharedPreferences.getTotalCoins(context, email);
        this.totalSent = MySharedPreferences.getTotalSentCoins(context, email);
        this.specialCoinz = MySharedPreferences.getStoredSpecials(context, email);
        this.gold = MySharedPreferences.getGoldTotal(context, email);
        this.friends = MySharedPreferences.getFriends(context, email);
        updateCommunityLevel();
        updateLevel();
        refreshWallet();
    }

    //public void updateCommunityLevel() {
       // this.communityLevel = level;
   // }

    // you go up every ten coins
    private void updateLevel() {
        int l = Math.floorDiv(totalCoins,15);
        this.level = l;
    }

    private void updateCommunityLevel() {
        int l = totalSent/5;
        this.communityLevel = l;
    }

    private void refreshWallet() {
        SimpleDateFormat dformat = new SimpleDateFormat("dd.MM.yyyy");
        Date today = new Date();
        String todays = dformat.format(today);
        ArrayList<Coin> newwalletcoinz = new ArrayList<>();

        if (walletCoinz.isEmpty()) {
            return;
        }
        for (Coin coin: walletCoinz) {
            String date = coin.getDate();
            if ((todays.equals(date))) {
                newwalletcoinz.add(coin);
            } else {
                MySharedPreferences.removeWalletCoin(getApplicationContext(), coin);
            }
        }
        walletCoinz = newwalletcoinz;
    }

    public void addToWallet(Coin coin) {
        // so that when you pick up a coin it automatically adds to total coins picked up
        if (!(isInWallet(coin))) {
            walletCoinz.add(coin);
            totalCoins++;
            updateLevel();
            MySharedPreferences.addWalletCoin(getApplicationContext(), coin);
        } else {
            Log.d(tag, "[addToWallet] tried to add coin to wallet twice! ==========================");
        }
    }

    public Boolean addToBank(Coin coin, int where) {
        // so only 25 banked coins are allowed per day
        if (bankedCoinz.size()>=25) {
            return false;
        } else {
            // if less than 25, add coin
            // to banked coins and remove from wallet.
            double coingold = coin.getGold();
            gold += coingold;
            removeCoinFromCoinz(0,coin);
            if (isInSpecial(coin)) {
                removeCoinFromCoinz(2,coin);
            }
            bankedCoinz.add(coin);
            MySharedPreferences.addBankCoin(getApplicationContext(), coin, where);
            return true;
        }
    }

    public void addToSentCoins(Coin coin, int where) { // where is 0 for wallet, 2 for special coins
        if (where==0) {
            removeCoinFromCoinz(where,coin); //rem from wallet
            MySharedPreferences.removeWalletCoin(getApplicationContext(), coin);
        } else if (where==2) {
            removeCoinFromCoinz(where, coin); // rem from special coins
            MySharedPreferences.removeSpecialCoin(getApplicationContext(), coin);
        }
        MySharedPreferences.add2SentCoins(getApplicationContext(), coin, where);
    }

    public Boolean addToSpecialCoins(Coin coin) {
        if (specialCoinz.size() < level && !(isInSpecial(coin))) {
            walletCoinz.remove(coin);
            specialCoinz.add(coin);
            MySharedPreferences.addSpecialCoin(getApplicationContext(), coin);
            return true;
        } else {
            return false;
        }
    }

    public void removeCoinFromCoinz(int which, Coin coin) {
        // 0 -wallet, 1-bank, 2-special
        switch (which){
            case 0:
                for (Coin a: new ArrayList<>(walletCoinz)) {
                    if (a.isEqualto(coin)) {
                        walletCoinz.remove(a);
                        Log.d(tag, "[removeCoinFromCoinz] removing coin from wallecoinz ==============");
                    }
                }
            case 1:
                for (Coin a: new ArrayList<>(bankedCoinz)) {
                    if (a.isEqualto(coin)) {
                        bankedCoinz.remove(a);
                        Log.d(tag, "[removeCoinFromCoinz] removing coin from bankedcoinz ==============");
                    }
                }
            case 2:
                for (Coin a: new ArrayList<>(specialCoinz)) {
                    if (a.isEqualto(coin)) {
                        specialCoinz.remove(a);
                        Log.d(tag, "[removeCoinFromCoinz] removing coin from specialcoinz ==============");
                    }
                }
        }

    }

    public void sendCoin(Coin coin, String email) { // where is 0 if from wallet, 2 if from special coins
        int where = 2;
        if (isInWallet(coin)) {
            where = 0;
        }
        if (friends.contains(email)) {
            removeCoinFromCoinz(where,coin);
            addToSentCoins(coin,where);

        }
    }


    // PUBLIC CHECKERS //

    public boolean isInBank(Coin coin) {
        for (Coin a: bankedCoinz) {
            if (a.isEqualto(coin)) {
                return true;
            }
        }
        return false;
    }

    public boolean isInWallet(Coin coin) {
        for (Coin a: walletCoinz) {
            if (a.isEqualto(coin)) {
                return true;
            }
        }
        return false;
    }

    public boolean isInSpecial(Coin coin) {
        for (Coin a: specialCoinz) {
            if (a.isEqualto(coin)) {
                return true;
            }
        }
        return false;
    }


    // PUBLIC GETTERS //

    public int getLevel() {
        return level;
    }

    public String getEmail() { return email;}

    public double getGold() {
        return gold;
    }

    public int getCommunityLevel() {
        return communityLevel;
    }

    public ArrayList<Coin> getSpecialCoinz() {
        return specialCoinz;
    }

    public ArrayList<Coin> getWalletCoinz() {
        return walletCoinz;
    }

    public ArrayList<Coin> getBankedCoinz() { return bankedCoinz;
    }

    public int getTotalCoins() { return totalCoins; }

    public ArrayList<String> getFriends() {
        return friends;
    }

}
