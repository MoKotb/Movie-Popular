package com.kotb.popularmovies.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.kotb.popularmovies.Model.Movie;
import com.kotb.popularmovies.Model.Video;
import com.kotb.popularmovies.R;

import java.util.ArrayList;

public class VideosAdapter extends RecyclerView.Adapter<VideosAdapter.ViewHolder> {

    private ArrayList<Video> videoArrayList;
    private final Context context1;
    private final VideosAdapter.onClickItem listener;

    public VideosAdapter(Context context, onClickItem listener) {
        this.context1 = context;
        this.listener = listener;
    }

    public interface onClickItem {
        void onClick(Video video);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.video_item, parent, false);
        return new VideosAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.videoText.setText("Trailer " + (1 + position));
    }

    @Override
    public int getItemCount() {
        if (videoArrayList == null) {
            return 0;
        }
        return videoArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public final ImageView videoImage;
        public final TextView videoText;

        public ViewHolder(View itemView) {
            super(itemView);
            videoImage = itemView.findViewById(R.id.videoItemImage);
            videoText = itemView.findViewById(R.id.videoItemText);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            listener.onClick(videoArrayList.get(getAdapterPosition()));
        }
    }

    public void setVideosList(ArrayList<Video> videos) {
        this.videoArrayList = videos;
        notifyDataSetChanged();
    }
}
