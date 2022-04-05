package com.test.marvelcomics.data.retrofit

import com.test.marvelcomics.domain.entity.Comic
import com.test.marvelcomics.domain.entity.Comics
import com.test.marvelcomics.domain.entity.Date
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

interface MarvelComicsApi {
    @GET("v1/public/comics")
    fun getPublishedComics(
        @Query("dateRange") nowDate: String,
        @Query("formatType") formatType: String,
        @Query("orderBy") orderBy: String,
        @Query("ts") timeStamp: String,
        @Query("apikey") apiKey: String,
        @Query("hash") hash: String,
    ): Call<Date>
}