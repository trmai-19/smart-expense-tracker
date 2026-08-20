package com.smartexpense.android.data.remote;

import android.content.Context;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    // TODO: Update this to your local IP address for testing on physical device
    private static final String BASE_URL = "http://localhost:8080/";
    
    private static Retrofit retrofit = null;
    private static TokenManager tokenManager = null;

    public static void init(Context context) {
        if (tokenManager == null) {
            tokenManager = new TokenManager(context.getApplicationContext());
        }
    }

    public static TokenManager getTokenManager() {
        return tokenManager;
    }

    public static Retrofit getClient() {
        if (retrofit == null) {
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);

            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(logging)
                    .addInterceptor(new AuthInterceptor(tokenManager))
                    .build();

            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}

