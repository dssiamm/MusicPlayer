package com.example.dssiam.musicplayer;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private final String TAG = "MusicPlayer";
    AsyncTask musicSeekBarUpdater;
    private MediaPlayer mediaPlayer;
    private int songDuration;
    private Button btnPlayPause, btnStop;
    private SeekBar musicPlayBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        TextView textView = (TextView) findViewById(R.id.song_text);
        textView.setMovementMethod(new ScrollingMovementMethod());

        mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.scorpions_white_dove);
        mediaPlayer.seekTo(0);
        songDuration = mediaPlayer.getDuration();

        musicPlayBar = (SeekBar) findViewById(R.id.music_play_bar);
        musicPlayBar.setMax(songDuration);
        musicPlayBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(fromUser) {
                    mediaPlayer.seekTo(progress);
                    musicPlayBar.setProgress(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) { }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) { }
        });

        btnPlayPause = (Button) findViewById(R.id.btn_play);
        btnPlayPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playPause();
            }
        });

        btnStop = (Button) findViewById(R.id.btn_stop);
        btnStop.setVisibility(View.INVISIBLE);
        btnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stop();
            }
        });

        getMusicUpdater().execute();
    }

    private AsyncTask getMusicUpdater() {
        if (mediaPlayer != null) {
            musicSeekBarUpdater = new AsyncTask() {
                @Override
                protected Object doInBackground(Object[] objects) {
                    while (mediaPlayer != null) {
                        musicPlayBar.setProgress(mediaPlayer.getCurrentPosition());
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException ex) {
                            Log.e(TAG, ex.toString());
                        }
                    }
                    return null;
                }
            };
        }

        return musicSeekBarUpdater;
    }

    @Override
    protected void onPause() {
        if (!NotificationActions.isAlreadyCreated) {
            NotificationActions.musicControl(this);

            BroadcastReceiver broadcastReceiver = new BroadcastReceiver()
            {
                @Override
                public void onReceive(Context context, Intent intent)
                {
                    if (intent.getAction() != null) {
                        switch (intent.getAction()) {
                            case NotificationActions.START_PLAY:
                                playPause();
                                break;
                            case NotificationActions.STOP_PLAY:
                                stop();
                                break;
                            default:
                                Log.e(TAG, "Unknown intent param");
                                break;
                        }
                    }
                }
            };

            IntentFilter filter = new IntentFilter();
            filter.addAction(NotificationActions.START_PLAY);
            filter.addAction(NotificationActions.STOP_PLAY);

            getApplicationContext().registerReceiver(broadcastReceiver, filter);
        }

        super.onPause();
    }

    private void playPause() {
        if(mediaPlayer == null) {
            mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.scorpions_white_dove);
            getMusicUpdater().execute();
            btnStop.setVisibility(View.VISIBLE);
        }

        if (mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
            btnPlayPause.setBackgroundResource(R.drawable.ic_action_play);
        } else {
            mediaPlayer.start();
            btnPlayPause.setBackgroundResource(R.drawable.ic_action_pause);
            btnStop.setVisibility(View.VISIBLE);
        }
    }

    private void stop() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;

            musicPlayBar.setProgress(0);
            musicSeekBarUpdater.cancel(true);

            btnStop.setVisibility(View.INVISIBLE);
            btnPlayPause.setBackgroundResource(R.drawable.ic_action_play);
        }
    }

    @Override
    protected void onDestroy() {
        NotificationActions.deleteNotification();
        super.onDestroy();
    }
}
