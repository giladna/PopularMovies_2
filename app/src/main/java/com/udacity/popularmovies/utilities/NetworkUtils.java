/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.udacity.popularmovies.utilities;

import android.net.Uri;

import com.udacity.popularmovies.BuildConfig;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

/**
 * These utilities will be used to communicate with the network.
 */
public class NetworkUtils {

    //http://api.themoviedb.org/3/movie/popular?language=en-US&api_key=8778f67f11a5a1dcb6ee731a56892416
    //https://image.tmdb.org/t/p/w185/nUXCJMnAiwCpNPZuJH2n6h5hGtF.jpg

    public static final String NOW_PLAYING = "now_playing";
    public static final String POPULARITY = "popularity";
    public static final String VOTE_AVARAGE = "vote_average";

    final static String MOVIES_DB_BASE_URL =
            "https://api.themoviedb.org/3/";

    final static String POPULAR_MOVIES_DB_BASE_URL =
            MOVIES_DB_BASE_URL + "movie/popular";

    final static String TOP_RATED_MOVIES_DB_BASE_URL =
            MOVIES_DB_BASE_URL + "movie/top_rated";

    final static String NOW_PLAYING_MOVIES_DB_BASE_URL =
            MOVIES_DB_BASE_URL + "movie/now_playing";


    final static String API_KEY = "api_key";
    final static String PARAM_LANG ="language";
    final static String PARAM_PAGE ="page";
    final static String sortByPopularity = "popularity.desc";
    final static String sortByRating = "vote_average.desc";

    public static URL buildUrl(String moviesSortQuery, int page) {
        Uri builtUri;
        if (POPULARITY.equals(moviesSortQuery)) {
            builtUri = Uri.parse(POPULAR_MOVIES_DB_BASE_URL).buildUpon()
                    .appendQueryParameter(API_KEY, BuildConfig.MOVIE_DB_API_KEY)
                    .appendQueryParameter(PARAM_LANG, "en-US")
                    .appendQueryParameter(PARAM_PAGE, String.valueOf(page))
                    .build();
        } else {
            builtUri = Uri.parse(TOP_RATED_MOVIES_DB_BASE_URL).buildUpon()
                    .appendQueryParameter(API_KEY, BuildConfig.MOVIE_DB_API_KEY)
                    .appendQueryParameter(PARAM_LANG, "en-US")
                    .appendQueryParameter(PARAM_PAGE, String.valueOf(page))
                    .build();
        }

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }

    /**
     * This method returns the entire result from the HTTP response.
     *
     * @param url The URL to fetch the HTTP response from.
     * @return The contents of the HTTP response.
     * @throws IOException Related to network and stream reading
     */
    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }
}