package com.example.blanca.coinz2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.common.io.Resources;

import org.w3c.dom.Text;

import java.util.ArrayList;

import static com.mapbox.mapboxsdk.Mapbox.getApplicationContext;

public class CoinAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<Coin> coins;
    private ArrayList<String> values;
    private ArrayList<String> currs;
    private ArrayList<String> goldconversion;

    public CoinAdapter(Context context, ArrayList<Coin> coins) {
        values = new ArrayList<>();
        currs = new ArrayList<>();
        goldconversion = new ArrayList<>();

        for (Coin a: coins) {
            values.add(a.getValue());
            currs.add(a.getCurrency());
            goldconversion.add(String.valueOf(Helpers.goldTransform(a.getValue(), a.getCurrency())));
        }
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

        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (tvCurrency.getText()=="") {
                    values.set(position, coins.get(position).getValue());
                    currs.set(position, coins.get(position).getCurrency());

//                    tvValue.setText(values.get(position));
//                    tvCurrency.setText(currs.get(position));
                    notifyDataSetChanged();
                } else {
                    values.set(position, goldconversion.get(position) +"  in GOLD");
                    currs.set(position, "");
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
