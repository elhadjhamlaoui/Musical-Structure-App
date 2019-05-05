package com.example.musicplayer;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import java.util.ArrayList;

public class playlists_activity extends AppCompatActivity implements View.OnClickListener {

    ImageView cover, play, next;
    TextView song_title, song_artist;
    ListView listView;
    ArrayList<Playlist> list = new ArrayList<>();
    RelativeLayout panel;
    SeekBar seekBar;
    SharedPreferences.OnSharedPreferenceChangeListener spChanged;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.playlists);

        play = findViewById(R.id.play_pause);
        next = findViewById(R.id.next);
        cover = findViewById(R.id.song_cover);
        panel = findViewById(R.id.panel);
        seekBar = findViewById(R.id.seekBar);

        song_title = findViewById(R.id.song_title);
        song_artist = findViewById(R.id.song_artist);

        listView = findViewById(R.id.listView);

        play.setOnClickListener(this);
        next.setOnClickListener(this);
        panel.setOnClickListener(this);

        ArrayList<String> names = new ArrayList<>();
        for (Song s : ControlPanel.getAll_songs()) {
            for (String s1 : s.getPlaylists()) {
                if (!names.contains(s1)) {
                    names.add(s1);
                    list.add(new Playlist(s1, 1));
                } else {
                    int index = getIndex(s1);
                    int count = list.get(index).getCount();
                    list.get(index).setCount(count + 1);
                }
            }
        }

        Playlists_Adapter adapter = new Playlists_Adapter(this, list);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(playlists_activity.this, playlist_activity.class);
                intent.putExtra("name", list.get(i).getName());
                intent.putExtra("class", "playlists");
                startActivity(intent);
            }
        });

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                ControlPanel.setProgress(i);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        spChanged = new SharedPreferences.OnSharedPreferenceChangeListener() {
            @Override
            public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
                                                  String key) {
                song_title.setText(sharedPreferences.getString("title", ""));
                song_artist.setText(sharedPreferences.getString("artist", ""));
                seekBar.setProgress(sharedPreferences.getInt("progress", 0));
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
        prefs.registerOnSharedPreferenceChangeListener(spChanged);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.play_pause:
                if (ControlPanel.isPlaying()) {
                    play.setImageResource(R.drawable.play);
                    ControlPanel.pause();
                } else {
                    play.setImageResource(R.drawable.pause_circle);
                    ControlPanel.play(-1);
                }
                break;
            case R.id.next:
                ControlPanel.next();
                break;
            case R.id.panel:
                Intent intent = new Intent(playlists_activity.this, now_playing_activity.class);
                startActivity(intent);
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
            seekBar.setProgress(ControlPanel.getSong().getProgress());
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

    // this function search for a playlist by name and returns the index
    private int getIndex(String name) {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getName().equals(name))
                return i;
        }
        return -1;
    }
}
