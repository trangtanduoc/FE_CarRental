package com.prm.carrental.core.network;

import androidx.annotation.NonNull;

import com.prm.carrental.core.session.SessionManager;

import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

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

    private static final String BASE_URL = "https://10.0.2.2:7125/api/"; // Android emulator mapping

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
            .client(getUnsafeOkHttpClient().build())
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

    private OkHttpClient.Builder getUnsafeOkHttpClient() {
        try {

            final TrustManager[] trustAllCerts = new TrustManager[]{
                    new X509TrustManager() {
                        @Override
                        public void checkClientTrusted(X509Certificate[] chain, String authType) { }
                        @Override
                        public void checkServerTrusted(X509Certificate[] chain, String authType) { }
                        @Override
                        public X509Certificate[] getAcceptedIssuers() { return new X509Certificate[]{}; }
                    }
            };


            final SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, trustAllCerts, new SecureRandom());
            final javax.net.ssl.SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();


            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            builder.sslSocketFactory(sslSocketFactory, (X509TrustManager) trustAllCerts[0]);
            builder.hostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            });

            return builder;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    public <T> T createService(Class<T> serviceClass) {
        return retrofit.create(serviceClass);
    }
}
