package com.example.blanca.coinz2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class CommunityActivity extends AppCompatActivity implements View.OnClickListener {

    private final String tag = "CommunityActivity";
    private static Coin chosen;
    private static Player player;
    private ArrayList<String> friends;
    private FriendAdapter friendAdapter;

    // Layout variables
    private TextView chosenCoin;
    private ListView commLv;
    private Button ctowallet;
    private Button ctomap;
    private Button choseCoin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_community);

        player = MapActivity.player;
        friends = player.getFriends();

        // establish layout variables
        ctowallet = findViewById(R.id.btcommtobank);
        ctowallet.setOnClickListener(this);
        ctomap = findViewById(R.id.btcommtoMap);
        ctomap.setOnClickListener(this);
        chosenCoin = findViewById(R.id.btchoseCoin);
        chosenCoin.setOnClickListener(this);
        //if it has not been set yet, make it empty
        chosenCoin = findViewById(R.id.tvChosenCoin);
        String ccoin = chosen.getValue() + " , " + chosen.getCurrency();

        // if no coin chosen replace empty string by message
        if (ccoin.equals("")) {
            chosenCoin.setText("No coin chosen!");
        } else {
            chosenCoin.setText(ccoin);
        }

        // listview
        commLv = findViewById(R.id.listviewComm);

        friendAdapter = new FriendAdapter(getApplicationContext(), friends, chosen);

        commLv.setAdapter(friendAdapter);


    }

    @Override
    public void onClick(View view) {
        switch(view.getId()) {

            case R.id.btcommtobank:
                Log.d(tag,"[onClick] Button to bank has been clicked.");
                startActivity(new Intent(this,BankActivity.class));
                break;

            case R.id.btcommtoMap:
                Log.d(tag,"[onClick] Button to map page clicked. ");
                startActivity(new Intent(this,MapActivity.class));
                break;

            case R.id.btchoseCoin:
                Log.d(tag,"[onClick] Button to wallet, choose coin, page clicked. ");
                startActivity(new Intent(this,WalletActivity.class));
                break;
        }
    }

    public static void setChosen(Coin a) {
        chosen =a;
    }
    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
