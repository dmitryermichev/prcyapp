package ru.prcy.app.api;

import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by dmitry on 26.10.17.
 */

public interface ApiService {

    @GET("base/{domain}")
    Call<JsonObject> baseAnalize(@Path("domain") String domain, @Query("key") String key);

    @GET("advanced/{domain}")
    Call<JsonObject> advancedAnalize(@Path("domain") String domain, @Query("key") String key);

    @GET("status/advanced/{domain}")
    Call<JsonObject> advancedAnalizeStatus(@Path("domain") String domain, @Query("key") String key);

    @GET("status/base/{domain}")
    Call<JsonObject> baseAnalizeStatus(@Path("domain") String domain, @Query("key") String key);

    @POST("update/base/{domain}")
    Call<JsonObject> updateBaseAnalize(@Path("domain") String domain, @Query("key") String key);

    @POST("update/advanced/{domain}")
    Call<JsonObject> updateAdvancedAnalize(@Path("domain") String domain, @Query("key") String key);

}