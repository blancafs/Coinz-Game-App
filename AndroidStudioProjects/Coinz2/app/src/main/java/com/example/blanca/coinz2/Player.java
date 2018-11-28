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
    private int totalSteps;
    private int totalCoins;

    public Player(Context context, String email) {
        this.email = email;
        this.communityLevel = MySharedPreferences.getCommunityLevel(context, email);
        this.walletCoinz = MySharedPreferences.getWalletCoins(context, email);
        this.bankedCoinz = MySharedPreferences.getBankedCoins(context, email);
        this.totalCoins = MySharedPreferences.getTotalCoins(context, email);
        updateLevel();
    }

    public void setCommunityLevel(Integer level) {
        this.communityLevel = level;
    }

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

    public void addToBank(Coin coin) {
        bankedCoinz.add(coin);
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

    public int getCommunityLevel() {
        return communityLevel;
    }

    public ArrayList<Coin> getWalletCoinz() {
        return walletCoinz;
    }

    public ArrayList<Coin> getBankedCoinz() { return bankedCoinz;
    }
    public int getTotalCoins() { return totalCoins; }

}
