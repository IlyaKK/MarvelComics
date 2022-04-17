package com.test.marvelcomics.data.retrofit

import com.test.marvelcomics.domain.entity.api.DataApi
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface MarvelComicsApi {
    @GET("v1/public/comics")
    fun getPublishedComics(
        @Query("dateRange") nowDate: String,
        @Query("formatType") formatType: String,
        @Query("orderBy") orderBy: String,
        @Query("offset") offset: Int,
        @Query("ts") timeStamp: String,
        @Query("apikey") apiKey: String,
        @Query("hash") hash: String,
    ): Call<DataApi>
}