package com.udacity.popularmovies.database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.udacity.popularmovies.model.MovieMetadata;

import java.util.List;

@Dao
public interface MovieMetadataDao {
    @Query("SELECT * FROM movie")
    LiveData<List<MovieMetadata>> getAll();


    @Query("SELECT * FROM movie ORDER BY title")
    List<MovieMetadata> selectAll();

    @Query("SELECT * FROM movie WHERE id = :id")
    MovieMetadata getMovieById(Long id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(MovieMetadata movie);

    @Delete
    void delete(MovieMetadata movie);
}