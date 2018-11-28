package com.example.blanca.coinz2;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import org.w3c.dom.Text;

import static com.example.blanca.coinz2.MapActivity.player;

public class AccountActivity extends AppCompatActivity {

    private String tag = "AccountActivity";
    private Toolbar toolbar;
    private static String email;
    private static Integer noStepsWalked;
    private static Integer noCoinsPicked;
    private static Integer noCoinsSent;
    private static Integer playerLevel;
    private static Integer commLevel;

    // Assigning Ids from Login page to vars //
    private TextView noSteps;
    private TextView noCoinsPi;
    private TextView noCoinsSen;
    private TextView plalevel;
    private TextView comlevel;

    private static Player player;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
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

        //noSteps.setText(noStepsWalked);
        noCoinsPi.setText(Integer.toString(player.getTotalCoins()));
        plalevel.setText(Integer.toString(player.getLevel()));
        comlevel.setText(Integer.toString(player.getCommunityLevel()));
    }

    public static Integer getNoStepsWalked() {
        return noStepsWalked;
    }

    public static Integer getNoCoinsPicked() {
        return noCoinsPicked;
    }

    public static Integer getNoCoinsSent() {
        return noCoinsSent;
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}