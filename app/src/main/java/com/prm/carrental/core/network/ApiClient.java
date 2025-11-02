package com.prm.carrental.core.network;

import androidx.annotation.NonNull;

import com.prm.carrental.core.session.SessionManager;

import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Configures Retrofit and OkHttp with JWT authentication header support.
 */
public class ApiClient {

    private static final String BASE_URL = "http://10.0.2.2:5000/api/"; // Android emulator mapping

    private final SessionManager sessionManager;
    private final Retrofit retrofit;

    public ApiClient(@NonNull SessionManager sessionManager) {
        this.sessionManager = sessionManager;
        this.retrofit = buildRetrofit();
    }

    private Retrofit buildRetrofit() {
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder()
            .readTimeout(30, TimeUnit.SECONDS)
            .connectTimeout(30, TimeUnit.SECONDS)
            .addInterceptor(createAuthInterceptor())
            .addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY));

        return new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(httpClient.build())
            .addConverterFactory(GsonConverterFactory.create())
            .build();
    }

    private Interceptor createAuthInterceptor() {
        return chain -> {
            Request original = chain.request();
            String token = sessionManager.getToken();
            if (token == null || token.isEmpty()) {
                return chain.proceed(original);
            }

            Request.Builder builder = original.newBuilder()
                .header("Authorization", "Bearer " + token);
            return chain.proceed(builder.build());
        };
    }

    public <T> T createService(Class<T> serviceClass) {
        return retrofit.create(serviceClass);
    }
}
