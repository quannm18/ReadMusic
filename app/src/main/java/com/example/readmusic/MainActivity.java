package com.example.readmusic;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.readmusic.Adapter.MusicAdapter;
import com.example.readmusic.Model.AudioModel;
import com.example.readmusic.Service.PlayingService;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity{
    private RecyclerView rcvMain;
    private Button btnSet;
    public static List<AudioModel> audioModelList;
    private MusicAdapter musicAdapter;

    private ImageView imgPlay;
    private ImageView imgPrevious;
    private ImageView imgNext;
    private ImageView imgPause;
    private TextView tvNameSong;

    private PlayingService playingService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        rcvMain = (RecyclerView) findViewById(R.id.rcvMain);

        imgPlay = (ImageView) findViewById(R.id.imgPlay);
        imgPrevious = (ImageView) findViewById(R.id.imgPrevious);
        imgNext = (ImageView) findViewById(R.id.imgNext);
        imgPause = (ImageView) findViewById(R.id.imgPause);
        tvNameSong = (TextView) findViewById(R.id.tvNameSong);

        //call check per
        checkper();
        //get List DATA
        audioModelList = new ArrayList<>();
        audioModelList = loadAudio(this);
        //setAdapter
        musicAdapter = new MusicAdapter(audioModelList);
        rcvMain.setAdapter(musicAdapter);
        rcvMain.setLayoutManager(new LinearLayoutManager(MainActivity.this));
        playingService = new PlayingService();
        imgPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playingService.stop();
            }
        });

        tvNameSong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isPermissionsGranted()){
                    Toast.makeText(v.getContext(), "Permission already granted", Toast.LENGTH_SHORT).show();
                }else {
                    takePermission();
                }
            }
        });
    }

    private void checkper() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                && ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    999);

            return;
        }
    }

    //load audio method
    private List<AudioModel> loadAudio(Context context){
        List<AudioModel> tempList = new ArrayList<>();
        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        String[] projection = {
                MediaStore.Audio.AudioColumns.TITLE,
                MediaStore.Audio.Media.ARTIST,
                MediaStore.Audio.Media._ID,
                MediaStore.Audio.AudioColumns.DATA,
                MediaStore.Audio.AudioColumns.DURATION,
                MediaStore.Audio.AudioColumns.ALBUM_ID
        };
        Cursor cursor = context.getContentResolver().query(uri,projection,null,null,null);
        if (cursor.getCount()>0){
            while (cursor.moveToNext()){
                long albumID = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.AudioColumns.ALBUM_ID));
                Uri imgPath = Uri.parse("content://media/external/audio/albumart");
                Uri imgParse = ContentUris.withAppendedId(imgPath,albumID);
                tempList.add(new AudioModel(
                        cursor.getString(cursor.getColumnIndex(MediaStore.Audio.AudioColumns.TITLE)),
                        cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST)),
                        cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media._ID)),
                        cursor.getString(cursor.getColumnIndex(MediaStore.Audio.AudioColumns.DATA)),
                        cursor.getString(cursor.getColumnIndex(MediaStore.Audio.AudioColumns.DURATION)),
                                imgParse.toString()
                ));
            }
            cursor.close();
        }
        return tempList;
    }
    private boolean isPermissionsGranted(){
        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.R){
            return Environment.isExternalStorageManager();
        }else {
            int readEnternalStoragePermission =
                    ContextCompat.checkSelfPermission(this,
                            Manifest.permission.READ_EXTERNAL_STORAGE);
            return readEnternalStoragePermission == PackageManager.PERMISSION_GRANTED;
        }
    }
    private void takePermission(){
        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.R){
            try {
                Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
                intent.addCategory("android.intent.category.DEFAULT");
                intent.setData(Uri.parse(String.format("package:%s",getApplicationContext().getPackageName())));
                startActivityForResult(intent,100);
            }catch (Exception e){
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
                startActivityForResult(intent,100);
                e.printStackTrace();
            }
        }else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},101);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode==RESULT_OK){
            if (requestCode==100){
                if (Build.VERSION.SDK_INT == Build.VERSION_CODES.R){
                    if (Environment.isExternalStorageManager()){
                        Toast.makeText(MainActivity.this, "Permission granted!", Toast.LENGTH_SHORT).show();
                    }else {
                        takePermission();
                    }
                }
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length>0){
            if (requestCode==101){
                boolean readExternalStorage = grantResults[0]==PackageManager.PERMISSION_GRANTED;
                if (readExternalStorage){
                    Toast.makeText(MainActivity.this, "Read permission is granted in android 10 and below", Toast.LENGTH_SHORT).show();
                }else {
                    takePermission();
                }
            }
        }
    }
}