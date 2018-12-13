package com.example.blanca.coinz2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import java.util.ArrayList;

public class CommunityActivity extends AppCompatActivity implements View.OnClickListener {

    private static Coin chosen;

    // Layout variables
    private static TextView chosenCoin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_community);

        Player player = MapActivity.player;
        ArrayList<String> friends = player.getFriends();

        // establish layout variables
        TextView commlevel = findViewById(R.id.tvCOMMLEVEL);
        commlevel.setText(String.valueOf(player.getCommunityLevel()));
        Button ctowallet = findViewById(R.id.btcommtowallet);
        ctowallet.setOnClickListener(this);
        Button ctomap = findViewById(R.id.btcommtoMap);
        ctomap.setOnClickListener(this);

        // CHOSEN COIN SET UP - if it has not been set yet, make it empty
        chosenCoin = findViewById(R.id.tvChosenCoin);
        // if no coin chosen replace empty string by message
        if (chosen==null) {
            chosenCoin.setText("No coin chosen!");
        } else {
            chosenCoin.setText(chosen.getValue() + " , " + chosen.getCurrency());
        }

        // listview setup
        ListView commLv = findViewById(R.id.listviewComm);
        FriendAdapter friendAdapter = new FriendAdapter(getApplicationContext(), friends, chosen);
        commLv.setAdapter(friendAdapter);
    }

    @Override
    public void onClick(View view) {
        String tag = "CommunityActivity";
        switch(view.getId()) {

            case R.id.btcommtowallet:
                Log.d(tag,"[onClick] Button to wallet has been clicked from comm.");
                startActivity(new Intent(this,WalletActivity.class));
                break;

            case R.id.btcommtoMap:
                Log.d(tag,"[onClick] Button to map page clicked from comm. ");
                startActivity(new Intent(this,MapActivity.class));
                break;
        }
    }

    public static void setChosen(Coin a) {
        chosen =a;
    }

    public static void setChosenText(String a) {
        chosenCoin.setText(a);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
