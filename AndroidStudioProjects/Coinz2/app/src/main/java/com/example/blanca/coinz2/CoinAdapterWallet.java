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

import java.util.ArrayList;

import static com.mapbox.mapboxsdk.Mapbox.getApplicationContext;

public class CoinAdapterWallet extends BaseAdapter{

    // Declaring variables used throughout use of adapter
    private final String tag = "CoinAdapterWallet";
    private Context context;
    private ArrayList<Coin> coins;
    private ArrayList<String> values;
    private ArrayList<String> currs;
    private ArrayList<String> goldconversion;
    private Player player;
    private TextView walletCoinNumber;

    public CoinAdapterWallet(Context context, ArrayList<Coin> coins) {
        values = new ArrayList<>();
        currs = new ArrayList<>();
        goldconversion = new ArrayList<>();
        ArrayList<Double> convs = MapActivity.conversions;

        for (Coin a: coins) {
            values.add(a.getValue());
            currs.add(a.getCurrency());
            goldconversion.add(String.valueOf(Helpers.goldTransform(a.getValue(), a.getCurrency(),convs)));
        }

        player = MapActivity.player;
        this.context=context;
        this.coins=coins;

        walletCoinNumber = WalletActivity.textView;
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

        v.setOnLongClickListener(view -> {
            PopupMenu popupMenu = new PopupMenu(wrapper, view, Gravity.RIGHT);
            // inflating popup
            popupMenu.getMenuInflater().inflate(R.menu.walletsmallmenu, popupMenu.getMenu());
            popupMenu.show();
            // registering popup with onmenuitemclicklistener
            popupMenu.setOnMenuItemClickListener(menuItem -> {
                switch(menuItem.getItemId()) {

                    case R.id.nav_sendcoin:
                        Log.d(tag,"[onMenuItemClick] Button to send coin, and so to community page, clicked.");
                        CommunityActivity.setChosen(coins.get(position));

                        // delete coin from listview
                        coins.remove(position);
                        values.remove(position);
                        currs.remove(position);
                        goldconversion.remove(position);
                        notifyDataSetChanged();

                        Log.d(tag,"[onMenuItemClick] setchosen methods of comm activity called ====");

                        Intent intent = new Intent(getApplicationContext(), CommunityActivity.class);
                        getApplicationContext().startActivity(intent);

                        break;

                    case R.id.nav_addtobank:
                        // addBankCoin checks no more than 25 done at a time
                        Boolean answer = player.addToBank(coins.get(position),0);
                        if (!answer) {
                            // print toast saying too many banked coins
                            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE );
                            assert inflater != null;
                            View toastLayout = inflater.inflate(R.layout.toomanyinbanktoast, v.findViewById(R.id.llCustom));
                            Toast toast = new Toast(getApplicationContext());
                            toast.setDuration(Toast.LENGTH_SHORT);
                            toast.setView(toastLayout);
                            toast.show();
                        } else {
                            // if coin is banked
                            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE );
                            assert inflater != null;
                            View toastLayout = inflater.inflate(R.layout.itembankedtoast, v.findViewById(R.id.llCustom));
                            Toast toast = new Toast(getApplicationContext());
                            toast.setDuration(Toast.LENGTH_SHORT);
                            toast.setView(toastLayout);
                            toast.show();

                            // delete coin from listview
                            coins.remove(position);
                            values.remove(position);
                            currs.remove(position);
                            goldconversion.remove(position);

                            // Refresh coin count
                            walletCoinNumber.setText(String.valueOf(player.getWalletCoinz().size()) + " coins");

                            // Notify
                            notifyDataSetChanged();
                        }
                        break;

                    case R.id.nav_storecoin:
                        Boolean ans = player.addToSpecialCoins(coins.get(position));
                        if (!ans) {
                            // can't add to specialcoins
                            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE );
                            View toastLayout = inflater.inflate(R.layout.limitspecialcoins, v.findViewById(R.id.llCustom));
                            Toast toast = new Toast(getApplicationContext());
                            toast.setDuration(Toast.LENGTH_SHORT);
                            toast.setView(toastLayout);
                            toast.show();
                        } else {
                            // if can add the special coin to storage
                            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE );
                            View toastLayout = inflater.inflate(R.layout.addedspecialcoin, v.findViewById(R.id.llCustom));
                            Toast toast = new Toast(getApplicationContext());
                            toast.setDuration(Toast.LENGTH_SHORT);
                            toast.setView(toastLayout);
                            toast.show();

                            // delete coin from listview
                            coins.remove(position);
                            values.remove(position);
                            currs.remove(position);
                            goldconversion.remove(position);

                            // Refresh coin count
                            walletCoinNumber.setText(String.valueOf(player.getWalletCoinz().size()) + " coins");

                            notifyDataSetChanged();
                        }
                        break;
                }
                return false;
            });

            return false;
        });

        v.setOnClickListener(view -> {
            // if the coin's display is only the first line, aka the gold conversion, we swith it back upon click
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
