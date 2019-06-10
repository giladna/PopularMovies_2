package com.udacity.popularmovies;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.udacity.popularmovies.databinding.ReviewItemBinding;
import com.udacity.popularmovies.model.ReviewMetadata;

import java.util.ArrayList;
import java.util.List;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewAdapterViewHolder> {

    private final Activity mActivity;
    private List<ReviewMetadata> mList;

    public ReviewAdapter(Activity activitu) {
        this.mActivity = activitu;
    }

    @NonNull
    @Override
    public ReviewAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(mActivity);
        ReviewItemBinding binding = ReviewItemBinding.inflate(layoutInflater, parent, false);

        return new ReviewAdapterViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewAdapterViewHolder holder, int position) {
        ReviewMetadata review = mList.get(position);
        holder.bind(review);
    }

    @Override
    public int getItemCount() {
        if (mList == null) {
            return 0;
        }
        return mList.size();
    }

    public void addReviewsList(List<ReviewMetadata> reviewsList) {
        mList = reviewsList;
        notifyDataSetChanged();
    }

    public ArrayList<ReviewMetadata> getList() {
        return (ArrayList<ReviewMetadata>) mList;
    }

    public class ReviewAdapterViewHolder extends RecyclerView.ViewHolder {
        ReviewItemBinding binding;

        ReviewAdapterViewHolder(ReviewItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(ReviewMetadata reviewMetadata) {
            binding.setReviewMetadata(reviewMetadata);
            binding.setPresenter((DetailActivity) mActivity);
        }
    }
}