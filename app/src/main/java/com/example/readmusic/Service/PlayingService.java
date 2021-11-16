package com.example.readmusic.Service;

import static com.example.readmusic.MainActivity.audioModelList;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;

import com.example.readmusic.Model.AudioModel;

import java.util.List;

public class PlayingService extends Service {
    private MediaPlayer mediaPlayer;
    private List<AudioModel> list;
    private Uri uri;
    public PlayingService() {
    }
    IBinder mBinder = new MyBinder();
    public class MyBinder extends Binder{
        PlayingService getService(){
            return PlayingService.this;
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        this.list = audioModelList;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public void start(){
        mediaPlayer.start();
    }
    public boolean isPlaying(){
        return mediaPlayer.isPlaying();
    }
    public void stop(){
        mediaPlayer.stop();
    }
    public void release(){
        mediaPlayer.release();
    }
    public int getDuration(){
        return mediaPlayer.getDuration();
    }
    public void seekTo(int pos){
        mediaPlayer.seekTo(pos);
    }
    public void createMediaPlayer(Context context,Uri uri){
        mediaPlayer = MediaPlayer.create(context,uri);
    }
}