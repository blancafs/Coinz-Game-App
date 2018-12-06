package com.example.blanca.coinz2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class AccountActivity extends AppCompatActivity implements View.OnClickListener {

    private String tag = "AccountActivity";
    private Toolbar toolbar;
    private static String email;
    private static Integer noStepsWalked;
    private static Integer noCoinsPicked;
    private static Integer noCoinsSent;
    private static Integer playerLevel;
    private static Integer commLevel;

    // Assigning Ids from Login page to vars //
    static TextView noSteps;
    static TextView noCoinsPi;
    static TextView noCoinsSen;
    static TextView plalevel;
    static TextView comlevel;
    static TextView myaccount;
    private Button account2wallet;
    private Button account2map;

    private static Player player;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        player = MapActivity.player;

        Log.d(tag, "[onCreate] data " + noCoinsPicked +" " + playerLevel+ " " +commLevel+"===============");

        // initiating textviews
        noSteps = findViewById(R.id.tvNoSteps);
        noCoinsPi = findViewById(R.id.tvNoCoinsPicked);
        noCoinsSen = findViewById(R.id.tvNoCoinsSent);
        plalevel = findViewById(R.id.tvPlayerLevel);
        comlevel = findViewById(R.id.tvComLevel);
        myaccount = findViewById(R.id.tvMyAccount);
        myaccount.setText(player.getEmail());
        account2wallet = findViewById(R.id.btaccount2wallet);
        account2wallet.setOnClickListener(this);
        account2map = findViewById(R.id.btaccount2map);
        account2map.setOnClickListener(this);

        //noSteps.setText(noStepsWalked);
        noCoinsPi.setText(Integer.toString(player.getTotalCoins()));
        plalevel.setText(Integer.toString(player.getLevel()));
        comlevel.setText(Integer.toString(player.getCommunityLevel()));
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btaccount2wallet:
                Log.d(tag,"[onClick] Button to wallet has been clicked.");
                startActivity(new Intent(this,WalletActivity.class));
                break;
            case R.id.btaccount2map:
                Log.d(tag,"[onClick] Button to map page clicked.");
                startActivity(new Intent(this,MapActivity.class));
                break;
        }
    }
}