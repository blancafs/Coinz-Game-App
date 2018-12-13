package com.example.blanca.coinz2;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import static com.mapbox.mapboxsdk.Mapbox.getApplicationContext;

public class FriendAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<String> ffriends;
    private Coin chosen;
    private Player player;

    public FriendAdapter(Context context, ArrayList<String> friends, Coin chosen) {
        this.context = context;
        ffriends = new ArrayList<>();
        for (String s :friends) {
            if (s.contains("@")) {
                ffriends.add(s);
            }
        }
        this.chosen = chosen;
        player = MapActivity.player;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        @SuppressLint("ViewHolder") View v = View.inflate(context, R.layout.friendlist_item, null);
        TextView friend = v.findViewById(R.id.tvNamecomm);

        // Populate data into view with object
        friend.setText(ffriends.get(position));
        v.setTag(ffriends.get(position));


        v.setOnLongClickListener(new View.OnLongClickListener(){
            @Override
            public boolean onLongClick(View view) {
                player.sendCoin(chosen, ffriends.get(position));
                // print toast saying too many banked coins
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE );
                View toastLayout = inflater.inflate(R.layout.sentcointoast, v.findViewById(R.id.llCustom));
                Toast toast = new Toast(getApplicationContext());
                toast.setDuration(Toast.LENGTH_SHORT);
                toast.setView(toastLayout);
                toast.show();

                CommunityActivity.setChosen(null);
                CommunityActivity.setChosenText("Choose new coin!");
                return true;
            }
        });
       return v;
    }

    @Override
    public int getCount() {
        return ffriends.size();
    }

    @Override
    public String getItem(int position) {
        return ffriends.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

}
