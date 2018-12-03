package com.example.blanca.coinz2;

import android.content.Context;
import android.content.SharedPreferences;

import java.lang.reflect.Array;
import java.util.ArrayList;

import static com.mapbox.mapboxsdk.Mapbox.getApplicationContext;

public class Player {

    private String email;
    private int communityLevel;
    private int level;
    private String[] friends;
    private ArrayList<Coin> walletCoinz;
    private ArrayList<Coin> bankedCoinz;
    private ArrayList<Coin> specialCoinz;
    private int totalSteps;
    private int totalCoins;
    private int specialsallowed;
    private double gold;

    public Player(Context context, String email) {
        this.email = email;
        this.communityLevel = MySharedPreferences.getCommunityLevel(context, email);
        this.walletCoinz = MySharedPreferences.getWalletCoins(context, email);
        this.bankedCoinz = MySharedPreferences.getBankedCoins(context, email);
        this.totalCoins = MySharedPreferences.getTotalCoins(context, email);
        this.specialCoinz = MySharedPreferences.getStoredSpecials(context, email);
        this.gold = MySharedPreferences.getGoldTotal(context, email);
        updateLevel();
        this.specialsallowed = level + specialCoinz.size();
    }

    //public void updateCommunityLevel() {
       // this.communityLevel = level;
   // }

    // you go up every ten coins
    private void updateLevel() {
        int l = Math.floorDiv(totalCoins,15);
        this.level = l;
    }

    public void addToWallet(Coin coin) {
        walletCoinz.add(coin);
        totalCoins++;
        updateLevel();
        MySharedPreferences.addWalletCoin(getApplicationContext(), coin);
        // so that when you pick up a coin it automatically adds to total coins picked up
    }

    public Boolean addToBank(Coin coin) {
        // so only 25 banked coins are allowed per day
        if (bankedCoinz.size()>=25) {
            return false;
        } else {
            // if less than 25, add coin to banked coins and remove from wallet.
            double coingold = coin.getGold();
            gold += coingold;
            walletCoinz.remove(coin);
            bankedCoinz.add(coin);
            MySharedPreferences.addBankCoin(getApplicationContext(), coin);
            return true;
        }
    }

    public Boolean addToSpecialCoins(Coin coin) {
        if (specialCoinz.size()<= specialsallowed) {
            specialCoinz.add(coin);
            walletCoinz.remove(coin);
            MySharedPreferences.addSpecialCoin(getApplicationContext(), coin);
            return true;
        } else {
            return false;
        }
    }

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

}
