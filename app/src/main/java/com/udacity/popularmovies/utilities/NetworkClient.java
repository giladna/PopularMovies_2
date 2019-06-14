package com.udacity.popularmovies.utilities;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.udacity.popularmovies.utilities.NetworkUtils.MOVIES_DB_BASE_URL;

public class NetworkClient {

    public static Retrofit retrofit;

    public static Retrofit getRetrofitClient() {

        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(MOVIES_DB_BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

}