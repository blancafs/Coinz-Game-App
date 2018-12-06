package com.example.blanca.coinz2;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Map;

public class WalletActivity extends AppCompatActivity implements View.OnClickListener {

    private final String tag = "WalletActivity";
    private CoinAdapterWallet coinAdapterWallet;
    private ListView listView;
    private ArrayList<Coin> wcoins;

    static TextView textView;
    private Button mapbutton;
    private Button bankbutton;
    private static Player player;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet);

        player = MapActivity.player;

        wcoins = new ArrayList<>(player.getWalletCoinz());

        listView = findViewById(R.id.listview);

        coinAdapterWallet = new CoinAdapterWallet(getApplicationContext(), wcoins);

        listView.setAdapter(coinAdapterWallet);

        // setting textview to equal number of wcoins currently in wallet
        textView = findViewById(R.id.tvwalletno);
        textView.setText(Integer.toString(wcoins.size()) + " coins");

        // setting listeners on buttons on screen
        mapbutton = findViewById(R.id.btMap);
        mapbutton.setOnClickListener(this);
        bankbutton = findViewById(R.id.btWallet2Bank);
        bankbutton.setOnClickListener(this);

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
                Log.d(tag,"[onClick] Button to map has been clicked.");
                startActivity(new Intent(this,MapActivity.class));
                break;

            case R.id.btWallet2Bank:
                Log.d(tag,"[onClick] Button to bank page clicked. ");
                startActivity(new Intent(this,BankActivity.class));
                break;
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
