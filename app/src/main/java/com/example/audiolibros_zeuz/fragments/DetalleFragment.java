package com.example.audiolibros_zeuz.fragments;

import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceManager;

import com.example.audiolibros_zeuz.Aplicacion;
import com.example.audiolibros_zeuz.Libro;
import com.example.audiolibros_zeuz.MainActivity;
import com.example.audiolibros_zeuz.PreferenciasActivity;
import com.example.audiolibros_zeuz.R;

import java.io.IOException;

public class DetalleFragment extends Fragment implements

        View.OnTouchListener, MediaPlayer.OnPreparedListener, MediaController.MediaPlayerControl {
    public  static String ARG_ID_LIBRO="id_libro";
    MediaPlayer mediaPlayer;
    MediaController mediaController;


    @Override
    public void onPrepared(MediaPlayer mp) {
        Log.d("AudioLibros","Entramos en onPRepared de mediaPlayer");
        SharedPreferences preferencias= PreferenceManager.getDefaultSharedPreferences(getActivity());
        if(preferencias.getBoolean("pref_autorerproducir",true)){
        mediaPlayer.start();
        }

        mediaController.setMediaPlayer(this);
        mediaController.setAnchorView(getView().findViewById(R.id.fragment_detalle));
        mediaController.setEnabled(true);
        mediaController.show();
    }



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View vista=inflater.inflate(R.layout.fragment_detalle,container,false);
        Bundle args=getArguments();
        if(args!=null){
            int position=args.getInt(ARG_ID_LIBRO);
            ponInfoLibro(position,vista);

        }else{
            ponInfoLibro(0,vista);
        }
        return vista;
    }
    private void ponInfoLibro(int id,View vista ){
        Libro libro=((Aplicacion)getActivity().getApplication()).getVectorLibros().elementAt(id);
        MainActivity.idLibroEnReproduccion=id;
        ((TextView)vista.findViewById(R.id.titulo)).setText(libro.titulo);
        ((TextView)vista.findViewById(R.id.autor)).setText(libro.autor);
        ((ImageView)vista.findViewById(R.id.portada)).setImageResource(libro.recursoImagen);
        vista.setOnTouchListener(this);
        if(mediaPlayer!=null){
            mediaPlayer.release();

        }
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setOnPreparedListener(this);
        mediaController= new MediaController(getActivity());
        Uri audio= Uri.parse(libro.urlAudio);
        try {
            mediaPlayer.setDataSource(getActivity(),audio);
            mediaPlayer.prepareAsync();

        }catch (IOException q){
            Log.e("AudioLibros","ERROR: no se puede reproducir "+audio,q);

        }
    }
    public void ponInfoLibro(int id){
            ponInfoLibro(id,getView());
    }


    @Override
    public boolean onTouch(View v, MotionEvent event) {
        mediaController.show();
        return false;
    }

    @Override
    public void onStop() {
        mediaController.hide();
        MainActivity.getIdLibroEnReproduccionProgreso=mediaPlayer.getCurrentPosition();
        try{
            mediaPlayer.stop();
            mediaPlayer.release();

        }catch (Exception e){
            Log.d("AudioLibros","Error en media player.stop()");

        }

        super.onStop();
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
