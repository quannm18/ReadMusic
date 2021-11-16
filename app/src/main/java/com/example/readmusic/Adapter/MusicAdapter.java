package com.example.readmusic.Adapter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.readmusic.Model.AudioModel;
import com.example.readmusic.R;

import java.util.List;

public class MusicAdapter extends RecyclerView.Adapter<MusicAdapter.MusicHolder> {
    private List<AudioModel> stringList;

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
        holder.tvRow.setText(audioModel.getName());
        holder.tvRowArtist.setText(audioModel.getArtist());
        Glide.with(holder.itemView.getContext())
                .load(Uri.parse(audioModel.getImgPath()))
                .centerCrop()
                .into(holder.imgRow);
        holder.tvRow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayer= MediaPlayer.create(holder.itemView.getContext(), Uri.parse(audioModel.getPath()));
                mediaPlayer.start();
            }
        });

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
