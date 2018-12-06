package com.example.blanca.coinz2;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.google.common.io.Resources;

import org.w3c.dom.Text;

import java.util.ArrayList;

import static android.support.v4.content.ContextCompat.startActivity;
import static com.example.blanca.coinz2.MapActivity.player;
import static com.mapbox.mapboxsdk.Mapbox.getApplicationContext;

public class CoinAdapterBank extends BaseAdapter{

    private final String tag = "CoinAdapterBank";
    private Context context;
    private ArrayList<Coin> coins;
    private ArrayList<String> values;
    private ArrayList<String> currs;
    private ArrayList<String> goldconversion;
    private static Player player;

    public CoinAdapterBank(Context context, ArrayList<Coin> coins) {
        values = new ArrayList<>();
        currs = new ArrayList<>();
        goldconversion = new ArrayList<>();
        ArrayList<Double> convs = MapActivity.conversions;

        for (Coin a: coins) {
            values.add(a.getValue());
            currs.add(a.getCurrency());
            goldconversion.add(String.valueOf(Helpers.goldTransform(a.getValue(), a.getCurrency(), convs)));
        }

        player = MapActivity.player;

        this.context=context;
        this.coins=coins;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = View.inflate(context, R.layout.simplelist_item,null);
        TextView tvValue = v.findViewById(R.id.tvValue);
        TextView tvCurrency = v.findViewById(R.id.tvCurrency);

        // Populate data into template view using data object
        tvValue.setText(values.get(position));
        tvCurrency.setText(currs.get(position));

        v.setTag(coins.get(position).getId());

        Context wrapper = new ContextThemeWrapper(getApplicationContext(), R.style.MYSTYLE);

        v.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                PopupMenu popupMenu = new PopupMenu(wrapper, view, Gravity.RIGHT);
                // inflating popup
                popupMenu.getMenuInflater().inflate(R.menu.specialcoinmenu, popupMenu.getMenu());
                // registering popup with onmenuitemclicklistener
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        switch(menuItem.getItemId()) {
                            case R.id.nav_spaddtobank:
                                // addBankCoin checks no more than 25 done at a time
                                Boolean answer = player.addToBank(coins.get(position), 2);
                                if (answer==false) {
                                    // print toast saying too many banked coins
                                    LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE );
                                    View toastLayout = inflater.inflate(R.layout.toomanyinbanktoast, v.findViewById(R.id.llCustom));
                                    Toast toast = new Toast(getApplicationContext());
                                    toast.setDuration(Toast.LENGTH_SHORT);
                                    toast.setView(toastLayout);
                                    toast.show();
                                } else {
                                    // if coin is banked
                                    LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE );
                                    View toastLayout = inflater.inflate(R.layout.itembankedtoast, v.findViewById(R.id.llCustom));
                                    Toast toast = new Toast(getApplicationContext());
                                    toast.setDuration(Toast.LENGTH_SHORT);
                                    toast.setView(toastLayout);
                                    toast.show();

                                    BankActivity.goldval.setText(String.valueOf(Math.round(player.getGold())) + " GOLD");

                                    // delete coin from listview
                                    coins.remove(position);
                                    values.remove(position);
                                    currs.remove(position);
                                    goldconversion.remove(position);
                                    notifyDataSetChanged();
                                }
                                break;
                            case R.id.nav_spsendcoin:
                                Log.d(tag,"[onMenuItemClick] Button to send coin, and so community page, clicked.");
                                Coin chosen = coins.get(position);
                                CommunityActivity.setChosen(chosen);
                                Intent intent = new Intent(getApplicationContext(), CommunityActivity.class);
                                getApplicationContext().startActivity(intent);
                        }
                        return false;
                    }
                });
                popupMenu.show();
                return false;
            }
        });

        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (tvCurrency.getText()=="") {
                    // setting value and currency back to coin's value and currency
                    values.set(position, coins.get(position).getValue());
                    currs.set(position, coins.get(position).getCurrency());
                    notifyDataSetChanged();

                } else {
                    // shows each coins value in gold
                    values.set(position, goldconversion.get(position) +"  in GOLD");
                    currs.set(position, "");
                    // toast saying youre seeing value in gold
                    LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE );
                    View toastLayout = inflater.inflate(R.layout.coinconvmessage, v.findViewById(R.id.llCustom));
                    Toast toast = new Toast(getApplicationContext());
                    toast.setDuration(Toast.LENGTH_SHORT);
                    toast.setView(toastLayout);
                    toast.show();
                    notifyDataSetChanged();
                }
            }
        });
        // return completed view to render on screen
        return v;
    }

    @Override
    public int getCount() {
        return coins.size();
    }

    @Override
    public Coin getItem(int position) {
        return coins.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
}
