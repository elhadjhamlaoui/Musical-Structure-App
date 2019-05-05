package com.example.musicplayer;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

public class now_playing_activity extends AppCompatActivity implements View.OnClickListener {
    ImageView play, next, previous, cover;
    TextView song_title, song_artist;
    SeekBar seekBar;
    SharedPreferences.OnSharedPreferenceChangeListener spChanged;


    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.now_playing);

        play = findViewById(R.id.play_pause);
        next = findViewById(R.id.next);
        previous = findViewById(R.id.previous);
        cover = findViewById(R.id.song_cover);
        song_title = findViewById(R.id.song_title);
        song_artist = findViewById(R.id.song_artist);
        seekBar = findViewById(R.id.seekBar);

        play.setOnClickListener(this);
        next.setOnClickListener(this);
        previous.setOnClickListener(this);

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
                    play.setImageResource(R.drawable.pause_circle_large);
                } else {
                    play.setImageResource(R.drawable.play_large);
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
                    ControlPanel.pause();
                    play.setImageResource(R.drawable.play_large);
                } else {
                    ControlPanel.play(-1);
                    play.setImageResource(R.drawable.pause_circle_large);
                }
                break;

            case R.id.next:
                ControlPanel.next();
                break;

            case R.id.previous:
                ControlPanel.previous();
                break;

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        song_title.setText(ControlPanel.getSong().getName());
        song_artist.setText(ControlPanel.getSong().getArtist().getName());
        seekBar.setProgress(ControlPanel.getSong().getProgress());
        cover.setImageResource(ControlPanel.getSong().getArtist().getCover());

        if (ControlPanel.isPlaying()) {
            play.setImageResource(R.drawable.pause_circle_large);
        } else {
            play.setImageResource(R.drawable.play_large);
        }

    }

}
