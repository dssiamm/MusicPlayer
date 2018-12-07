package com.example.dssiam.musicplayer;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.widget.RemoteViews;

public class NotificationActions {

    public static final String START_PLAY = "com.example.dssiam.musicplayer.START_PLAY";
    public static final String PAUSE_PLAY = "com.example.dssiam.musicplayer.PAUSE_PLAY";
    public static final String STOP_PLAY = "com.example.dssiam.musicplayer.STOP_PLAY";

    private static final int NOTIFICATION_ID = 111;

    public static void musicControl(Context context) {
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.music_controls_layout);

        NotificationCompat.Builder nc = new NotificationCompat.Builder(context);
        NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Intent notifyIntent = new Intent(context, MainActivity.class);

        notifyIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        nc.setContentIntent(pendingIntent);
        nc.setSmallIcon(R.drawable.ic_launcher);
        nc.setContent(remoteViews);

        setListeners(remoteViews, context);

        nm.notify(NOTIFICATION_ID, nc.build());
    }

    private static void setListeners(RemoteViews view, Context context){
        Intent play = new Intent(START_PLAY);
        Intent stop = new Intent(STOP_PLAY);
        Intent pause = new Intent(PAUSE_PLAY);

        PendingIntent pendingPlay = PendingIntent.getBroadcast(context, 0, play, PendingIntent.FLAG_UPDATE_CURRENT);
        view.setOnClickPendingIntent(R.id.btn_play, pendingPlay);

        PendingIntent pendingDelete = PendingIntent.getBroadcast(context, 0, pause, PendingIntent.FLAG_UPDATE_CURRENT);
        view.setOnClickPendingIntent(R.id.btn_pause, pendingDelete);

        PendingIntent pendingPause = PendingIntent.getBroadcast(context, 0, stop, PendingIntent.FLAG_UPDATE_CURRENT);
        view.setOnClickPendingIntent(R.id.btn_stop, pendingPause);
    }

}
