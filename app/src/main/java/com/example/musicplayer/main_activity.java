package com.example.musicplayer;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class main_activity extends AppCompatActivity implements View.OnClickListener {

    RelativeLayout songs, favorites, playlists, recent, panel;
    ImageView play, settings, search, cover;
    TextView song_title, song_artist;
    SharedPreferences.OnSharedPreferenceChangeListener spChanged;
    ArrayList<Song> song_list = new ArrayList<>();
    ArrayList<Artist> artists = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new ControlPanel(this);

        song_title = findViewById(R.id.song_title);
        song_artist = findViewById(R.id.song_artist);

        songs = findViewById(R.id.songs_layout);
        favorites = findViewById(R.id.favorites_layout);
        playlists = findViewById(R.id.playlists_layout);
        recent = findViewById(R.id.recent_layout);
        panel = findViewById(R.id.panel);

        play = findViewById(R.id.play_pause);
        settings = findViewById(R.id.imageView_settings);
        search = findViewById(R.id.imageView_search);
        cover = findViewById(R.id.song_cover);

        songs.setOnClickListener(this);
        favorites.setOnClickListener(this);
        playlists.setOnClickListener(this);
        recent.setOnClickListener(this);
        play.setOnClickListener(this);
        settings.setOnClickListener(this);
        search.setOnClickListener(this);
        panel.setOnClickListener(this);

        spChanged = new SharedPreferences.OnSharedPreferenceChangeListener() {
            @Override
            public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
                                                  String key) {
                panel.setVisibility(View.VISIBLE);
                song_title.setText(sharedPreferences.getString("title", ""));
                song_artist.setText(sharedPreferences.getString("artist", ""));
                cover.setImageResource(sharedPreferences.getInt("cover", R.drawable.alizee));

                boolean isPlaying = sharedPreferences.getBoolean("play", false);
                if (isPlaying) {
                    play.setImageResource(R.drawable.pause_circle);
                } else {
                    play.setImageResource(R.drawable.play);
                }
                if (ControlPanel.list != null) {
                    panel.setVisibility(View.VISIBLE);
                } else {
                    panel.setVisibility(View.GONE);
                }
            }
        };

        SharedPreferences prefs = getSharedPreferences("control_panel", 0);
        prefs.edit().clear().apply();
        prefs.registerOnSharedPreferenceChangeListener(spChanged);

        // i've generated the information manually just to test , in production we should load information automatically from an online or offline source

        //generate artists

        artists.add(new Artist("Adele", R.drawable.adele));
        artists.add(new Artist("Alizee", R.drawable.alizee));
        artists.add(new Artist("Indila", R.drawable.indila));
        artists.add(new Artist("Taylor", R.drawable.taylor));
        artists.add(new Artist("One republic", R.drawable.onerepublic));
        artists.add(new Artist("Ramin djawadi", R.drawable.ramin));

        //generate songs

        song_list.add(new Song("Rolling in the deep", artists.get(0)));
        song_list.add(new Song("Skyfall", artists.get(0)));
        song_list.add(new Song("Je veux bien", artists.get(1)));
        song_list.add(new Song("Gourmandises", artists.get(1)));
        song_list.add(new Song("Dernière Danse", artists.get(2)));
        song_list.add(new Song("Tourner dans le vide", artists.get(2)));
        song_list.add(new Song("Delicate", artists.get(3)));
        song_list.add(new Song("Blank Space", artists.get(3)));
        song_list.add(new Song("Love Runs Out", artists.get(4)));
        song_list.add(new Song("Secrets", artists.get(4)));
        song_list.add(new Song("Westworld-Sweetwater", artists.get(5)));
        song_list.add(new Song("Westworld-Heart Shaped Box", artists.get(5)));

        //add some playlists

        song_list.get(0).addPlaylist("playlist1");
        song_list.get(0).addPlaylist("playlist2");
        song_list.get(1).addPlaylist("playlist2");
        song_list.get(2).addPlaylist("playlist2");
        song_list.get(3).addPlaylist("playlist2");
        song_list.get(4).addPlaylist("playlist1");

        //add some songs to favorites

        song_list.get(0).setFavorite(true);
        song_list.get(1).setFavorite(true);
        song_list.get(8).setFavorite(true);
        song_list.get(5).setFavorite(true);
        song_list.get(9).setFavorite(true);
        song_list.get(11).setFavorite(true);

        // here we count the number of songs for each artist
        for (Song s : song_list) {
            int index = getIndex(s.getArtist().getName());
            int count = artists.get(index).getCount();
            artists.get(index).setCount(count + 1);
        }

        ControlPanel.setArtists(artists);
        ControlPanel.setAll_songs(song_list);
    }

    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.songs_layout:
                intent = new Intent(main_activity.this, song_list_activity.class);
                startActivity(intent);

                break;

            case R.id.favorites_layout:
                intent = new Intent(main_activity.this, favorites_activity.class);
                startActivity(intent);

                break;

            case R.id.playlists_layout:
                intent = new Intent(main_activity.this, playlists_activity.class);
                startActivity(intent);

                break;

            case R.id.recent_layout:
                intent = new Intent(main_activity.this, artists_activity.class);
                startActivity(intent);

                break;

            case R.id.play_pause:
                if (ControlPanel.isPlaying()) {
                    play.setImageResource(R.drawable.play);
                    ControlPanel.pause();
                } else {
                    play.setImageResource(R.drawable.pause_circle);
                    ControlPanel.play(-1);
                }

                break;

            case R.id.panel:
                intent = new Intent(main_activity.this, now_playing_activity.class);
                startActivity(intent);

                break;

            case R.id.imageView_settings:
                // TODO : lauch settings activity
                break;

            case R.id.imageView_search:
                // TODO : lauch search activity
                break;

        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (ControlPanel.list != null) {
            panel.setVisibility(View.VISIBLE);
            song_title.setText(ControlPanel.getSong().getName());
            song_artist.setText(ControlPanel.getSong().getArtist().getName());
            cover.setImageResource(ControlPanel.getSong().getArtist().getCover());

            if (ControlPanel.isPlaying()) {
                play.setImageResource(R.drawable.pause_circle);
            } else {
                play.setImageResource(R.drawable.play);
            }
        } else {
            panel.setVisibility(View.GONE);
        }
    }

    // this function searches for an artist by name and returns the index
    private int getIndex(String name) {
        for (int i = 0; i < artists.size(); i++) {
            if (artists.get(i).getName().equals(name))
                return i;
        }
        return -1;
    }
}
