package com.example.assignment2;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

public class Foreservice extends Service
{
    int[] songs = {
            R.raw.music1, R.raw.music2, R.raw.dream
    };
    private MediaPlayer sound;

    @Nullable
    @Override
    public IBinder onBind(Intent intent)
    {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        playSongs(0);
        createNotificationChannel();
        Intent inten=new Intent(this,mainActivity.class);
        PendingIntent pendingIntent=PendingIntent.getActivity(this,0,inten,0);
        Notification notification=new NotificationCompat.Builder(this,"ChannelId1").setContentTitle("Assignment 2")
                .setContentText("Music Playing")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentIntent(pendingIntent)
                .build();
        startForeground(1,notification);

        return START_STICKY;

    }
    private void playSongs(final int next){
        if(next>=songs.length)
        {return;}
        sound = MediaPlayer.create(getApplicationContext(),songs[next]);
        sound.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                playSongs(next+1);
            }
        });
        sound.start();
    }

    private void createNotificationChannel()
    {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            NotificationChannel notificationChannel = new NotificationChannel("ChannelId1", "Foreground notification", NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(notificationChannel);
        }
    }

    public void onDestroy()
    {
        sound.stop();
        stopForeground(true);
        stopSelf();
        super.onDestroy();
    }
}
