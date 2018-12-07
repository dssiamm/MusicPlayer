package com.example.dssiam.musicplayer;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        TextView textView = (TextView) findViewById(R.id.song_text);
        textView.setMovementMethod(new ScrollingMovementMethod());


        findViewById(R.id.btn_play).setOnClickListener(new View.OnClickListener()
        {
            @Override public void onClick(View v)
            {
                if (mediaPlayer == null) {
                    mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.scorpions_white_dove);
                    mediaPlayer.start();
                }

                if (!mediaPlayer.isPlaying()) {
                    mediaPlayer.start();
                }
            }
        });

        findViewById(R.id.btn_pause).setOnClickListener(new View.OnClickListener()
        {
            @Override public void onClick(View v)
            {
                if (mediaPlayer != null) {
                    if (mediaPlayer.isPlaying()) {
                        mediaPlayer.pause();
                    }
                }
            }
        });

        findViewById(R.id.btn_stop).setOnClickListener(new View.OnClickListener()
        {
            @Override public void onClick(View v)
            {
                if (mediaPlayer != null) {
                    mediaPlayer.stop();
                    mediaPlayer.release();
                    mediaPlayer = null;
                }
            }
        });
    }

    @Override
    protected void onPause() {
        NotificationActions.musicControl(this);

        BroadcastReceiver broadcastReceiver = new BroadcastReceiver()
        {
            @Override public void onReceive(Context context, Intent intent)
            {
                if (intent.getAction() != null) {
                    switch (intent.getAction()) {
                        case NotificationActions.START_PLAY:
                            findViewById(R.id.btn_play).performClick();
                            break;
                        case NotificationActions.PAUSE_PLAY:
                            findViewById(R.id.btn_pause).performClick();
                            break;
                        case NotificationActions.STOP_PLAY:
                            findViewById(R.id.btn_stop).performClick();
                            break;
                    }
                }
            }
        };

        IntentFilter filter = new IntentFilter();
        filter.addAction(NotificationActions.PAUSE_PLAY);
        filter.addAction(NotificationActions.START_PLAY);
        filter.addAction(NotificationActions.STOP_PLAY);

        getApplicationContext().registerReceiver(broadcastReceiver, filter);

        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
