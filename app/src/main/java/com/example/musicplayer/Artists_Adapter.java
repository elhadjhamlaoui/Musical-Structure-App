package com.example.musicplayer;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;


public class Artists_Adapter extends ArrayAdapter {
    Artists_Adapter(Context context, ArrayList<Artist> objects) {
        super(context, 0, objects);
    }


    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        Artist artist = (Artist) getItem(position);
        if (convertView == null)
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.artist_list_item, parent, false);

        viewHolder holder = new viewHolder(convertView);
        holder.name.setText(artist.getName());
        holder.count.setText(Integer.toString(artist.getCount()));
        holder.cover.setImageResource(artist.getCover());

        return convertView;
    }

    private class viewHolder {
        private TextView name, count;
        private ImageView cover;

        viewHolder(View v) {
            this.name = v.findViewById(R.id.name);
            this.count = v.findViewById(R.id.count);
            this.cover = v.findViewById(R.id.song_cover);
        }

    }
}
