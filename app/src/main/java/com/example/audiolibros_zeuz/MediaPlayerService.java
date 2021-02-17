package com.example.audiolibros_zeuz;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.IBinder;
import android.os.PowerManager;
import android.util.Log;
import android.widget.MediaController;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.example.audiolibros_zeuz.Libro;

import java.io.IOException;

//Reproduccion en segundo plano con notificacion
import java.util.Random;
import android.app.Notification;
import android.app.PendingIntent;

public class MediaPlayerService extends Service implements  MediaPlayer.OnPreparedListener, MediaController.MediaPlayerControl {
    MediaPlayer mediaPlayer;
    private String songTitle="Titulo";
    private String songAutor="Autor";
    private static final int NOTIFY_ID=1;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        mp.start();
        Intent notIntent = new Intent(this, MainActivity.class);
        notIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendInt = PendingIntent.getActivity(this, 0,
                notIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Notification.Builder builder = new Notification.Builder(this);

        builder.setContentIntent(pendInt)
                .setSmallIcon(R.drawable.play)
                .setTicker(songTitle)
                .setOngoing(true)
                .setContentTitle(songAutor)
  .setContentText(songTitle);
        Notification not = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
            not = builder.build();
        }
        startForeground(NOTIFY_ID, not);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(getApplicationContext(),"MediaPlayerService",Toast.LENGTH_LONG).show();
        Libro libro=((Aplicacion)getApplicationContext()).getVectorLibros().elementAt(MainActivity.idLibroEnReproduccion);
        songTitle=libro.titulo;
        songAutor=libro.autor;
        Uri audio= Uri.parse(libro.urlAudio);
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setOnPreparedListener(this);
        try {
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.setDataSource(getApplicationContext(),audio);
            mediaPlayer.prepareAsync(); // prepare async to not block main thread
            mediaPlayer.seekTo(MainActivity.getIdLibroEnReproduccionProgreso);
            mediaPlayer.start();
        }catch (IOException q){
            Log.e("AudioLibros","ERROR: no se puede reproducir "+audio,q);

        }
        //return super.onStartCommand(intent, flags, startId);
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    @Override
    public void start() {
        mediaPlayer.start();
    }

    @Override
    public void pause() {
        mediaPlayer.pause();
    }

    @Override
    public int getDuration() {

        return mediaPlayer.getDuration();
    }

    @Override
    public int getCurrentPosition() {
        try{
            return mediaPlayer.getCurrentPosition();
        }catch (Exception e)
        {
            return 0;
        }

    }

    @Override
    public void seekTo(int pos) {
        mediaPlayer.seekTo(pos);

    }

    @Override
    public boolean isPlaying() {

        return mediaPlayer.isPlaying();
    }

    @Override
    public int getBufferPercentage() {
        return 0;
    }

    @Override
    public boolean canPause() {
        return true;
    }

    @Override
    public boolean canSeekBackward() {
        return true;
    }

    @Override
    public boolean canSeekForward() {
        return true;
    }

    @Override
    public int getAudioSessionId() {
        return 0;
    }


}
