package com.example.blanca.coinz2;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import static com.example.blanca.coinz2.MapActivity.player;
import static com.mapbox.mapboxsdk.Mapbox.getApplicationContext;

public class WalletActivity extends AppCompatActivity implements View.OnClickListener {

    private final String tag = "WalletActivity";
    private CoinAdapter coinAdapter;
    private ListView listView;
    private ArrayList<Coin> coins;

    private TextView textView;
    private Button mapbutton;
    private Button commbutton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet);

        coins = new ArrayList<>(player.getWalletCoinz());

        listView = findViewById(R.id.listview);

        coinAdapter = new CoinAdapter(getApplicationContext(), coins);

        listView.setAdapter(coinAdapter);

        // setting textview to equal number of coins currently in wallet
        textView = findViewById(R.id.tvwalletno);
        textView.setText(Integer.toString(coins.size()) + " coins");

        // setting listeners on buttons on screen
        mapbutton = findViewById(R.id.btMap);
        mapbutton.setOnClickListener(this);
        commbutton = findViewById(R.id.btComm);
        commbutton.setOnClickListener(this);

        // toast that shows how to work screen
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE );
        View toastLayout = inflater.inflate(R.layout.wallettoast, findViewById(R.id.llCustom2));
        Toast toast = new Toast(getApplicationContext());
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(toastLayout);
        toast.show();

        //toolbar = findViewById(R.id.nav_actionbar_wallet);
//        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()) {

            case R.id.btMap:
                Log.d(tag,"[onClick]Button to map has been clicked.");
                startActivity(new Intent(this,MapActivity.class));
                break;

            case R.id.btComm:
                Log.d(tag,"[onClick] Button to community page clicked. ");
                startActivity(new Intent(this,CommunityActivity.class));
                break;
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
