package com.example.karaokebuddies;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class TrackAdapter extends RecyclerView.Adapter<TrackAdapter.ViewHolder> {

    private List<Track> tracks;

    private int selectedTrack = -1;

    private List<Track> selectedTracks;


    public TrackAdapter(List<Track> tracks) {
        this.tracks = tracks;
        this.selectedTracks = new ArrayList<>();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View trackView = inflater.inflate(R.layout.select_music_adapter, parent, false);
        ViewHolder viewHolder = new ViewHolder(trackView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Track track = tracks.get(position);

        holder.textView_title.setText(track.getTitle());
        holder.textView_artist.setText(track.getArtist());
        Picasso.get().load(track.getImageURL()).into(holder.imageButton_song);

        holder.imageButton_song.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = track.getTrackURL();
                Intent intent = new Intent(v.getContext(), record.class);
                Bundle bundle = new Bundle();
                bundle.putString("url", url);
                intent.putExtras(bundle);
                v.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return tracks.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView textView_title;
        TextView textView_artist;
        String song_url;
        ImageButton imageButton_song;

        public ViewHolder (View itemView) {
            super(itemView);
            textView_title = itemView.findViewById(R.id.textView_songTitle);
            textView_artist = itemView.findViewById(R.id.textView_songArtist);

            imageButton_song = itemView.findViewById(R.id.imageButton_song);
            imageButton_song.setOnClickListener(this);
        }



        @Override
        public void onClick(View v) {
            int selected = getAdapterPosition();
            Track selectedTrack = tracks.get(selected);

            if (selectedTracks.contains(selectedTrack)) {
                selectedTracks.remove(selectedTrack);
            } else {
                selectedTracks.add(selectedTrack);
            }
            notifyDataSetChanged();
        }
    }
}

