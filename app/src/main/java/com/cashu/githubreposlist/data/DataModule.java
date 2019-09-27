package com.cashu.githubreposlist.data;

import android.app.Application;

import androidx.room.Room;

import com.cashu.githubreposlist.data.local.GithubRepoDao;
import com.cashu.githubreposlist.data.local.GithubRepoDb;
import com.cashu.githubreposlist.data.remote.GithubRepoApi;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.Cache;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public abstract class DataModule {

    private static final int DISK_CACHE_SIZE = 50 * 1024 * 1024;
    private static final String BASE_URL = "https://api.github.com";

    @Provides
    @Singleton
    static Gson provideGson() {
        return new GsonBuilder()
                .create();
    }

    @Provides
    @Singleton
    static HttpUrl provideBaseUrl() {
        return HttpUrl.parse(BASE_URL);
    }

    @Provides
    @Singleton
    static OkHttpClient provideOkHttpClient(Application app) {

        File cacheDir = new File(app.getCacheDir(), "http");
        Cache cache = new Cache(cacheDir, DISK_CACHE_SIZE);

        return new OkHttpClient.Builder()
                .cache(cache)
                .build();
    }


    @Provides
    @Singleton
    static Retrofit provideRetrofit(HttpUrl baseUrl,
                                    OkHttpClient okHttpClient,
                                    Gson gson) {

        return new Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }

    @Provides
    @Singleton
    static GithubRepoApi provideGithubApi(Retrofit retrofit) {
        return retrofit.create(GithubRepoApi.class);
    }

    @Singleton
    @Provides
    static GithubRepoDb provideDb(Application app) {
        return Room
                .databaseBuilder(app, GithubRepoDb.class, "github.db")
                .fallbackToDestructiveMigration()
                .build();
    }

    @Singleton
    @Provides
    static GithubRepoDao provideRepoDao(GithubRepoDb githubRepoDb) {
        return githubRepoDb.repoDao();
    }
}
