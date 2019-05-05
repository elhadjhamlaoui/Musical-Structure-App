package com.example.musicplayer;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import java.util.ArrayList;


public class Songs_Adapter extends ArrayAdapter {
    Songs_Adapter(Context context, ArrayList<Song> objects) {
        super(context, 0, objects);
    }


    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        Song song = (Song) getItem(position);
        if (convertView == null)
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.song_list_item, parent, false);

        viewHolder holder = new viewHolder(convertView);
        holder.song_title.setText(song.getName());
        holder.song_artist.setText(song.getArtist().getName());

        return convertView;
    }

    private class viewHolder {
        private TextView song_title, song_artist;

        viewHolder(View v) {
            this.song_title = v.findViewById(R.id.song_title);
            this.song_artist = v.findViewById(R.id.song_artist);
        }
    }
}
