package com.example.blanca.coinz2;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class FriendAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<String> friends;
    private Coin chosen;
    private TextView friend;
    private Player player;

    public FriendAdapter(Context context, ArrayList<String> friends, Coin chosen) {
        this.context=context;
        this.friends=friends;
        this.chosen = chosen;
        player = MapActivity.player;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = View.inflate(context, R.layout.friendlist_item, null);
        TextView friend = v.findViewById(R.id.tvNamecomm);

        // Populate data into view with object
        friend.setText(friends.get(position));
        v.setTag(friends.get(position));

        v.setOnLongClickListener(new View.OnLongClickListener(){
            @Override
            public boolean onLongClick(View view) {
                player.sendCoin(chosen, friends.get(position));

                return true;
            }
        });
       return v;
    }

    @Override
    public int getCount() {
        return friends.size();
    }

    @Override
    public String getItem(int position) {
        return friends.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

}
