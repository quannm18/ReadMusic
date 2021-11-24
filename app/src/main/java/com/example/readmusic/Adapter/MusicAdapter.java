package com.example.readmusic.Adapter;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.readmusic.Model.AudioModel;
import com.example.readmusic.R;
import com.example.readmusic.Service.PlayingService;

import java.io.File;
import java.util.List;

public class MusicAdapter extends RecyclerView.Adapter<MusicAdapter.MusicHolder> {
    private List<AudioModel> stringList;
    private PlayingService playingService;
    public MusicAdapter(List<AudioModel> stringList) {
        this.stringList = stringList;
    }

    @NonNull
    @Override
    public MusicHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row,parent,false);
        return new MusicHolder(view);
    }
    private MediaPlayer mediaPlayer;
    private Handler handler;
    @Override
    public void onBindViewHolder(@NonNull MusicHolder holder, int position) {
        final AudioModel audioModel = stringList.get(position);
        playingService = new PlayingService();
        holder.tvRow.setText(audioModel.getName());
        holder.tvRowArtist.setText(audioModel.getArtist());
        Glide.with(holder.itemView.getContext())
                .load(Uri.parse(audioModel.getImgPath()))
                .centerCrop()
                .into(holder.imgRow);
        holder.tvRow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playingService.createMediaPlayer(v.getContext(),Uri.parse(audioModel.getPath()));
                playingService.start();
            }
        });
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                playingService.stop();
                return false;
            }
        });
        holder.imgRow.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R
//                        && ActivityCompat.checkSelfPermission(v.getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
//                        != PackageManager.PERMISSION_GRANTED) {
//                    ActivityCompat.requestPermissions((Activity) holder.itemView.getContext(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 101);
//                } else {
                    //Delete below api 30

                    Uri uri = ContentUris.withAppendedId(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                            Long.parseLong(stringList.get(holder.getAdapterPosition()).getId()));
                    File file = new File(stringList.get(holder.getAdapterPosition()).getPath());
                    boolean deleted = file.delete();
                    if (deleted){
                        v.getContext().getContentResolver().delete(uri,
                                null,
                                null);
                        stringList.remove(audioModel);
                        notifyItemRemoved(holder.getAdapterPosition());
                        notifyItemRangeChanged(holder.getAdapterPosition(), stringList.size());
                        Toast.makeText(v.getContext(), "Delete ok!", Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(v.getContext(), "Can't delete!", Toast.LENGTH_SHORT).show();

                    }

                    //delete above api 29


//                }
                return false;
            }
        });

    }
    //query to find out id file to delete
    public long getFilePathToMediaID(String songPath, Context context) {
        long id = 0;
        ContentResolver cr = context.getContentResolver();

        Uri uri = MediaStore.Files.getContentUri("external");
        String selection = MediaStore.Audio.Media.DATA;
        String[] selectionArgs = {songPath};
        String[] projection = {MediaStore.Audio.Media._ID};
        String sortOrder = MediaStore.Audio.Media.TITLE + " ASC";

        Cursor cursor = cr.query(uri, projection, selection + "=?", selectionArgs, sortOrder);

        if (cursor != null) {
            while (cursor.moveToNext()) {
                int idIndex = cursor.getColumnIndex(MediaStore.Audio.Media._ID);
                id = Long.parseLong(cursor.getString(idIndex));
            }
        }
        return id;
    }

    @Override
    public int getItemCount() {
        return stringList.size();
    }


    public class MusicHolder extends RecyclerView.ViewHolder {
        private TextView tvRow;
        private TextView tvRowArtist;
        private ImageView imgRow;
        public MusicHolder(@NonNull View itemView) {
            super(itemView);
            tvRow = itemView.findViewById(R.id.tvRow);
            tvRowArtist = itemView.findViewById(R.id.tvRowArtist);
            imgRow = itemView.findViewById(R.id.imgRow);
        }
    }
}
