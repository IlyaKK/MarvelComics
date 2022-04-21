package com.test.marvelcomics.data.retrofit

import com.test.marvelcomics.domain.entity.api.DataApi
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface MarvelComicsService {
    @GET("v1/public/comics")
    suspend fun getPublishedComics(
        @Query("dateRange") nowDate: String,
        @Query("formatType") formatType: String,
        @Query("orderBy") orderBy: String,
        @Query("offset") offset: Int,
        @Query("limit") limit: Int,
        @Query("ts") timeStamp: String,
        @Query("apikey") apiKey: String,
        @Query("hash") hash: String,
    ): DataApi

    companion object {
        private const val BASE_URL = "https://gateway.marvel.com/"

        fun create(): MarvelComicsService {
            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(MarvelComicsService::class.java)
        }
    }
}