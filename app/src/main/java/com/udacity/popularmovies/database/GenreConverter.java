package com.udacity.popularmovies.database;

import android.arch.persistence.room.TypeConverter;

import java.util.ArrayList;
import java.util.List;

public class GenreConverter {
    @TypeConverter
    public List<Long> gettingListFromString(String genreIds) {
        List<Long> list = new ArrayList<>();

        String[] array = genreIds.split(",");

        for (String s : array) {
            if (!s.isEmpty()) {
                list.add(Long.parseLong(s));
            }
        }
        return list;
    }

    @TypeConverter
    public String writingStringFromList(List<Long> list) {
        StringBuilder genreIds = new StringBuilder();
        for (long i : list) {
            genreIds.append(",").append(i);
        }
        return genreIds.toString();
    }
}