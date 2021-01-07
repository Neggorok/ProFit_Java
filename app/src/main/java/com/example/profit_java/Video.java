package com.example.profit_java;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Video extends AppCompatActivity {

    String tearVideoString;
    String videoId;
    int loggedInUserTear;
    String tearVideoID;

    RequestQueue queue;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);

        loggedInUserTear = PreferenceManager.getDefaultSharedPreferences(this).getInt("userTear", -1);
        tearVideoID = PreferenceManager.getDefaultSharedPreferences(this).getString("tearVideo", "-1");


        queue = Volley.newRequestQueue(this);

//        loadVideoID();


        YouTubePlayerView youTubePlayerView = findViewById(R.id.youtube_player_view);
        getLifecycle().addObserver(youTubePlayerView);

        youTubePlayerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {


            @Override
            public void onReady(@NonNull YouTubePlayer youTubePlayer) {
                // der youtube Link darf kein "=" enthalten, da es sonst zu einem Fehler kommt
                String videoId = tearVideoID;
                youTubePlayer.loadVideo(videoId, 0f);
            }
        });
    }

}