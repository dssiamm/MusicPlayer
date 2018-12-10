package com.example.dssiam.musicplayer;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.widget.RemoteViews;

public class NotificationActions {

    private static NotificationManager nm;
    public static boolean isAlreadyCreated = false;
    public static final String START_PLAY = "com.example.dssiam.musicplayer.START_PLAY";
    public static final String STOP_PLAY = "com.example.dssiam.musicplayer.STOP_PLAY";

    private static final int NOTIFICATION_ID = 111;

    public static void musicControl(Context context) {
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.music_controls_layout);

        NotificationCompat.Builder nc = new NotificationCompat.Builder(context);
        nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Intent notifyIntent = new Intent(context, MainActivity.class);

        notifyIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        nc.setContentIntent(pendingIntent);
        nc.setSmallIcon(R.drawable.ic_launcher);
        nc.setContent(remoteViews);

        setListeners(remoteViews, context);

        nm.notify(NOTIFICATION_ID, nc.build());

        isAlreadyCreated = true;
    }

    private static void setListeners(RemoteViews view, Context context){
        Intent play = new Intent(START_PLAY);
        Intent stop = new Intent(STOP_PLAY);

        PendingIntent pendingPlay = PendingIntent.getBroadcast(context, 0, play, PendingIntent.FLAG_UPDATE_CURRENT);
        view.setOnClickPendingIntent(R.id.btn_play, pendingPlay);

        PendingIntent pendingStop = PendingIntent.getBroadcast(context, 0, stop, PendingIntent.FLAG_UPDATE_CURRENT);
        view.setOnClickPendingIntent(R.id.btn_stop, pendingStop);
    }

    public static void deleteNotification() {
        if (nm != null) {
            nm.cancel(NOTIFICATION_ID);
        }
    }
}
