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
    Context context = getApplicationContext();

    public Player(String email) {
        this.email = email;
        this.level = MySharedPreferences.getCurrentLevel(context);
        this.communityLevel = MySharedPreferences.getCommunityLevel(context);
        this.walletCoinz = MySharedPreferences.getWalletCoins(context);
        this.bankedCoinz = MySharedPreferences.getBankedCoins(context);
    }

    public void setCommunityLevel(Integer level) {
        this.communityLevel = level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public void addToWallet(Coin coin) {
        walletCoinz.add(coin);
    }

    public void addToBank(Coin coin) {
        bankedCoinz.add(coin);
    }

    public boolean isInBank(Coin coin, String email) {
        return bankedCoinz.contains(coin);
    }

    public boolean isInWallet(Coin coin, String email) {
        return walletCoinz.contains(coin);
    }
}
