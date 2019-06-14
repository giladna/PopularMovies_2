package com.udacity.popularmovies;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.squareup.picasso.Picasso;
import com.udacity.popularmovies.databinding.TrailerItemBinding;
import com.udacity.popularmovies.model.VideoMetadata;

import java.util.ArrayList;
import java.util.List;

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.VideoAdapterViewHolder> {

    private final Context mContext;
    private List<VideoMetadata> mList;

    public VideoAdapter(Context context) {
        this.mContext = context;
    }

    @NonNull
    @Override
    public VideoAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        TrailerItemBinding binding = TrailerItemBinding.inflate(layoutInflater, parent, false);
        return new VideoAdapterViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull VideoAdapterViewHolder holder, int position) {
        VideoMetadata videoMetadata = mList.get(position);
        holder.bind(videoMetadata);
    }

    @Override
    public int getItemCount() {
        if (mList == null) {
            return 0;
        }
        return mList.size();
    }

    public void addVideosList(List<VideoMetadata> videosList) {
        mList = videosList;
        notifyDataSetChanged();
    }

    public ArrayList<VideoMetadata> getList() {
        return (ArrayList<VideoMetadata>) mList;
    }

    public class VideoAdapterViewHolder extends RecyclerView.ViewHolder {
        TrailerItemBinding binding;

        VideoAdapterViewHolder(TrailerItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(VideoMetadata videoMetadata) {
            binding.setVideoMetadata(videoMetadata);
            binding.setPresenter(this);

            String photoUrl = String.format("https://img.youtube.com/vi/%s/0.jpg", videoMetadata.videoUrl);
            Picasso.with(mContext)
                    .load(photoUrl)
                    .placeholder(R.drawable.placeholder)
                    .error(R.drawable.error)
                    .into(binding.trailerIv);
        }

        public void onClickVideo(String videoUrl) {
            Intent appIntent = new Intent(Intent.ACTION_VIEW,
                    Uri.parse("vnd.youtube:" + videoUrl));

            Intent webIntent = new Intent(Intent.ACTION_VIEW,
                    Uri.parse("https://www.youtube.com/watch?v=" + videoUrl));
            try {
                mContext.startActivity(appIntent);
            } catch (ActivityNotFoundException ex) {
                mContext.startActivity(webIntent);
            }
        }
    }
}