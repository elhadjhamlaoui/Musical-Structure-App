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

public class song_list_activity extends AppCompatActivity implements View.OnClickListener {
    ImageView search, sync, cover, play, next;
    TextView song_title, song_artist;
    ListView listView;
    ArrayList<Song> list = new ArrayList<>();
    RelativeLayout panel;
    SeekBar seekBar;
    SharedPreferences.OnSharedPreferenceChangeListener spChanged;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.song_list);

        search = findViewById(R.id.imageView_search);
        sync = findViewById(R.id.imageView_sync);
        play = findViewById(R.id.play_pause);
        next = findViewById(R.id.next);
        cover = findViewById(R.id.song_cover);
        panel = findViewById(R.id.panel);
        seekBar = findViewById(R.id.seekBar);

        song_title = findViewById(R.id.song_title);
        song_artist = findViewById(R.id.song_artist);

        listView = findViewById(R.id.listView);

        list = ControlPanel.getAll_songs();

        Songs_Adapter adapter = new Songs_Adapter(this, list);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (ControlPanel.playing_list.equals("song_list")) {
                    // this activity is already using the control panel so we directly play the song
                    ControlPanel.play(i);
                } else {
                    // another activity is using the control panel so we need to update the list before playing
                    ControlPanel.setList(list);
                    ControlPanel.playing_list = "song_list";
                    ControlPanel.play(i);
                }
            }
        });

        play.setOnClickListener(this);
        next.setOnClickListener(this);
        panel.setOnClickListener(this);
        search.setOnClickListener(this);
        sync.setOnClickListener(this);

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
                Intent intent = new Intent(song_list_activity.this, now_playing_activity.class);
                startActivity(intent);
                break;
            case R.id.imageView_sync:
                // TODO : reload songs
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
}

