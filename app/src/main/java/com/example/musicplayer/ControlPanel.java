package com.example.musicplayer;

import android.content.Context;
import android.content.SharedPreferences;
import java.util.ArrayList;

public class ControlPanel {
    private static boolean playing; // a boolean to know if a song is playing or not
    private static ArrayList<Song> all_songs; // a list of all songs in the device/app
    private static ArrayList<Artist> artists; // a list of all artists in the device/app
    static ArrayList<Song> list; // a list of the concerned song list ex : favorites , recent ...
    private static int index; // an index to represent the current song
    static String playing_list = ""; // a string to represents the current list which the song is coming from ex :  "song_list' , 'favorites' , 'recent'...
    private static SharedPreferences.Editor editor;

    ControlPanel(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("control_panel", 0);
        editor = sharedPreferences.edit();
        editor.apply();
    }

    static boolean isPlaying() {
        return playing;
    }

    private static void setPlaying(boolean playing) {
        ControlPanel.playing = playing;
    }

    public static int getProgress() {
        return list.get(index).getProgress();
    }

    static void setProgress(int progress) {
        list.get(index).setProgress(progress);
        editor.putInt("progress", progress);
        editor.apply();
    }

    public static ArrayList<Song> getList() {
        return list;
    }

    public static void setList(ArrayList<Song> list) {
        ControlPanel.list = list;
    }

    static Song getSong() {
        return list.get(index);
    }

    public static void play(int i) {
        // this method either plays a new song with given index != -1 , or it plays ( resume ) the current song when i==-1
        if (i == -1) {
            //todo : play ( resume ) the current song
        } else {
            index = i;
            //todo : stop playing the selected song
        }
        setPlaying(true);
        editor.putString("title", getSong().getName());
        editor.putString("artist", getSong().getArtist().getName());
        editor.putInt("progress", getSong().getProgress());
        editor.putInt("cover", getSong().getArtist().getCover());
        editor.putBoolean("play", isPlaying());
        editor.apply();
    }

    public static void pause() {
        setPlaying(false);
        editor.putBoolean("play", isPlaying());
        editor.apply();
        // todo : pause the song
    }

    public static void next() {
        getSong().setProgress(0);
        index++;
        if (index == list.size())
            index = 0;
        play(index);
    }

    public static void previous() {
        getSong().setProgress(0);
        index--;
        if (index == -1)
            index = list.size() - 1;
        play(index);
    }

    static ArrayList<Song> getAll_songs() {
        return all_songs;
    }

    static void setAll_songs(ArrayList<Song> all_songs) {
        ControlPanel.all_songs = all_songs;
    }

    public static ArrayList<Artist> getArtists() {
        return artists;
    }

    public static void setArtists(ArrayList<Artist> artists) {
        ControlPanel.artists = artists;
    }
}
