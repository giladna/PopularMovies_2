package com.udacity.popularmovies;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;
import com.udacity.popularmovies.model.MovieMetadata;

import java.util.List;

public class MovieImageGridAdapter extends RecyclerView.Adapter<MovieImageGridAdapter.MovieItemViewHolder> {

    private List<MovieMetadata> mMovieMetadataList;

    private Context mContext;
    private final MovieImageGridOnClickHandler mClickHandler;
    private LayoutInflater mInflater;

    public MovieImageGridAdapter(Context context, MovieImageGridOnClickHandler clickHandler) {
        mContext  = context;
        mClickHandler = clickHandler;
        mInflater = LayoutInflater.from(context);
    }

    public void setMoviesData(List<MovieMetadata> moviesData) {
        mMovieMetadataList = moviesData;
        notifyDataSetChanged();
    }

    public interface MovieImageGridOnClickHandler {
        void onClick(MovieMetadata movieMetadata);
    }

    @NonNull
    @Override
    public MovieImageGridAdapter.MovieItemViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        Context viewContext = viewGroup.getContext();
        int layoutIdForGridItem = R.layout.movie_grid_item;

        boolean shouldAttachToParentImmediately = false;
        View view = mInflater.inflate(layoutIdForGridItem, viewGroup, shouldAttachToParentImmediately);
        return new MovieItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieImageGridAdapter.MovieItemViewHolder movieItemViewHolder, final int position) {
        movieItemViewHolder.bind(position);

    }

    @Override
    public int getItemCount() {
        if (mMovieMetadataList == null) {
            return 0;
        }
        return mMovieMetadataList.size();
    }

    public class MovieItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView mMovieImageView;


        public MovieItemViewHolder(@NonNull View itemView) {
            super(itemView);
            mMovieImageView = itemView.findViewById(R.id.movie_image_view);
            //mMovieImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            //mMovieImageView.setAdjustViewBounds(true);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mClickHandler.onClick(mMovieMetadataList.get(getAdapterPosition()));
        }

        public void bind(int position) {
            if (mMovieMetadataList != null && !mMovieMetadataList.isEmpty()) {
                 String imagePath = mMovieMetadataList.get(position).getPosterFullPath();

//                Picasso.with(mContext)
//                        .load(imagePath)
//                        .placeholder(R.drawable.placeholder)
//                        .error(R.drawable.placeholder)
//                        .into(mMovieImageView);
                Glide.with(mContext)
                        .load(imagePath)
                        .error(R.drawable.placeholder)
                        .placeholder(R.drawable.placeholder)
                        .into(mMovieImageView);
            }

        }
    }
}
