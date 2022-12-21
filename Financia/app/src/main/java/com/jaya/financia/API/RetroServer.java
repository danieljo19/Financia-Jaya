package com.jaya.financia.API;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetroServer {
    private static final String baseUrl = "https://financia-team.000webhostapp.com/Financia/";
    private static Retrofit retro;

    public static Retrofit konekRetrofit() {
        if(retro == null) {
            retro = new Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retro;
    }
}
