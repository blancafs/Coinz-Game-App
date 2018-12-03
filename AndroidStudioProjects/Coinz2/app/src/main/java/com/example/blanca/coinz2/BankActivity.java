package com.example.blanca.coinz2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

import static com.example.blanca.coinz2.MapActivity.conversions;
import static com.example.blanca.coinz2.MapActivity.player;

public class BankActivity extends AppCompatActivity implements View.OnClickListener{

    private final String tag = "WalletActivity";
    private CoinAdapterBank coinAdapter;
    private ListView listView;
    private ArrayList<Coin> coins;

    private TextView goldval;
    private TextView textViewSHIL;
    private TextView textViewDOLR;
    private TextView textViewQUID;
    private TextView textViewPENY;
    private Button mapbutton;
    private Button walletbutton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bank);

        coins = new ArrayList<>(player.getSpecialCoinz());

        listView = findViewById(R.id.listviewBANK);

        coinAdapter = new CoinAdapterBank(getApplicationContext(), coins);

        listView.setAdapter(coinAdapter);

        // setting currency textviews to equal daily currency
        textViewSHIL = findViewById(R.id.textViewSHIL);
        textViewSHIL.setText("SHIL : " + String.valueOf(conversions.get(0)));

        textViewDOLR = findViewById(R.id.textViewDOLR);
        textViewDOLR.setText("DOLR : " + String.valueOf(conversions.get(1)));

        textViewQUID = findViewById(R.id.textViewQUID);
        textViewQUID.setText("QUID : " + String.valueOf(conversions.get(2)));

        textViewPENY = findViewById(R.id.textViewPENY);
        textViewPENY.setText("PENY : " + String.valueOf(conversions.get(3)));

        // setting buttons
        walletbutton = findViewById(R.id.btBank2Wallet);
        walletbutton.setOnClickListener(this);
        mapbutton = findViewById(R.id.btBank2Map);
        mapbutton.setOnClickListener(this);

        // setting to show total gold value
        goldval = findViewById(R.id.tvGold);
        goldval.setText(String.valueOf(Math.round(player.getGold())) + " GOLD");

    }
    @Override
    public void onClick(View view) {
        switch(view.getId()) {

            case R.id.btBank2Wallet:
                Log.d(tag,"[onClick] Button to wallet has been clicked.");
                startActivity(new Intent(this,WalletActivity.class));
                break;

            case R.id.btBank2Map:
                Log.d(tag,"[onClick] Button to map page clicked. ");
                startActivity(new Intent(this,MapActivity.class));
                break;
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
