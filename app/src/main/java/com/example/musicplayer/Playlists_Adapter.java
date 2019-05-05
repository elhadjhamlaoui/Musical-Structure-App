package com.example.musicplayer;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import java.util.ArrayList;

public class Playlists_Adapter extends ArrayAdapter {
    Playlists_Adapter(Context context, ArrayList<Playlist> objects) {
        super(context, 0, objects);
    }


    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        Playlist playlist = (Playlist) getItem(position);
        if (convertView == null)
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.playlists_item, parent, false);
        if (playlist != null) {
            viewHolder holder = new viewHolder(convertView);
            holder.playlist_name.setText(playlist.getName());
            holder.count.setText(Integer.toString(playlist.getCount()));
        }

        return convertView;
    }

    private class viewHolder {
        private TextView playlist_name, count;

        viewHolder(View v) {
            this.playlist_name = v.findViewById(R.id.song_title);
            this.count = v.findViewById(R.id.playlist_count);
        }

    }
}
