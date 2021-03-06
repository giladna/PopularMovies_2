package com.udacity.popularmovies;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.udacity.popularmovies.database.AppDatabase;
import com.udacity.popularmovies.model.MovieMetadata;
import com.udacity.popularmovies.preferences.AppFilterPreferences;
import com.udacity.popularmovies.utilities.AppExecutors;
import com.udacity.popularmovies.utilities.NetworkUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;

public class MovieImageGridAdapter extends RecyclerView.Adapter<MovieImageGridAdapter.MovieItemViewHolder> {

    private List<MovieMetadata> mMovieMetadataList;

    private Context mContext;
    private AppDatabase mDatabase;
    private Executor diskExecutor;
    private Executor networkExecutor;

    private final MovieImageGridOnClickHandler mClickHandler;
    private LayoutInflater mInflater;

    public MovieImageGridAdapter(Context context, MovieImageGridOnClickHandler clickHandler) {
        mContext  = context;
        mClickHandler = clickHandler;
        mInflater = LayoutInflater.from(context);
        this.mDatabase = AppDatabase.getDatabaseInstance(mContext);
        this.diskExecutor = AppExecutors.getInstance().diskIO();
        this.networkExecutor = AppExecutors.getInstance().networkIO();
    }

    public void setMoviesData(List<MovieMetadata> moviesData) {
        mMovieMetadataList = moviesData;
        notifyDataSetChanged();
    }

    public void clearList() {
        if (mMovieMetadataList == null) {
            mMovieMetadataList = new ArrayList<>();
        } else {
            int itemCount = mMovieMetadataList.size();
            mMovieMetadataList.clear();
            notifyItemRangeRemoved(0, itemCount);
        }
    }

    public void addMovies(List<MovieMetadata> moviesList) {
        int positionStart = mMovieMetadataList.size();
        mMovieMetadataList.clear();

        mMovieMetadataList.addAll(moviesList);
        notifyItemRangeInserted(positionStart, moviesList.size() - positionStart);
    }

    public interface MovieImageGridOnClickHandler {
        void onClick(MovieMetadata movieMetadata);
    }

    @NonNull
    @Override
    public MovieImageGridAdapter.MovieItemViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
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
        ImageView mMovieFavoritesView;
        TextView mMovieTitle;
        boolean isInFavorites;


        public MovieItemViewHolder(@NonNull View itemView) {
            super(itemView);
            mMovieImageView = itemView.findViewById(R.id.movie_image_view);
            mMovieFavoritesView = itemView.findViewById(R.id.favorite_image_view);
            mMovieFavoritesView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onClickFavorite(v);
                }
            });
            mMovieTitle = itemView.findViewById(R.id.title_text_view);

            //mMovieImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            //mMovieImageView.setAdjustViewBounds(true);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mClickHandler.onClick(mMovieMetadataList.get(getAdapterPosition()));
        }

        public void onClickFavorite(View view) {
            String snackBarText;
            int position = getAdapterPosition();
            final MovieMetadata movie = mMovieMetadataList.get(position);

            if (isInFavorites) {
                diskExecutor.execute(new Runnable() {
                    @Override
                    public void run() {
                        mDatabase.movieMetadataDao().delete(movie);
                    }
                });
                isInFavorites = false;
                mMovieFavoritesView.setImageResource(R.drawable.ic_star_border_white_24px);
                snackBarText = mContext.getString(R.string.remove_from_favorites, movie.getOriginalTitle());

                if (NetworkUtils.FAVORITES.equals(AppFilterPreferences.getFilter(mContext))) {
                    mMovieMetadataList.remove(position);
                    notifyItemRemoved(position);
                }

            } else {
                diskExecutor.execute(new Runnable() {
                    @Override
                    public void run() {
                        mDatabase.movieMetadataDao().insert(movie);
                    }
                });
                isInFavorites = true;
                mMovieFavoritesView.setImageResource(R.drawable.ic_star_white_24px);
                snackBarText = mContext.getString(R.string.added_to_favorites, movie.getOriginalTitle());
            }
            Snackbar.make(view, snackBarText, Snackbar.LENGTH_SHORT).show();
        }

        public void bind(final int position) {
            if (mMovieMetadataList != null && !mMovieMetadataList.isEmpty()) {
                String movieTitle = mMovieMetadataList.get(position).getTitle();
                mMovieTitle.setText(movieTitle);
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

                diskExecutor.execute(new Runnable() {
                    @Override
                    public void run() {
                        Long currentMovieId = mMovieMetadataList.get(position).getId();
                        final MovieMetadata movie = mDatabase.movieMetadataDao().getMovieById(currentMovieId);

                        Handler handler = new Handler(Looper.getMainLooper());
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                if (movie != null) {
                                    mMovieFavoritesView.setImageResource(R.drawable.ic_star_white_24px);
                                    isInFavorites = true;
                                    mMovieMetadataList.get(position).setInFavorites(true);
                                } else {
                                    mMovieFavoritesView.setImageResource(R.drawable.ic_star_border_white_24px);
                                    isInFavorites = false;
                                    mMovieMetadataList.get(position).setInFavorites(false);
                                }
                            }
                        });
                    }
                });
            }
        }
    }
}
