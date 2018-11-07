package com.example.blanca.coinz2;

import android.content.SharedPreferences;

public class Player {

    private String name;
    private String email;
    private int friendlyLevel;
    private int expLevel;
    private String[] friends;
    private SharedPreferences settings;

    public Player(String email, SharedPreferences settings) {
        this.email = email;
        this.settings = settings;

    }
}
