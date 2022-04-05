package com.test.marvelcomics.data

import com.test.marvelcomics.data.retrofit.MarvelComicsApi
import com.test.marvelcomics.domain.entity.Comics
import com.test.marvelcomics.domain.entity.Date
import com.test.marvelcomics.domain.repo.MarvelComicsRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class NetworkMarvelComicsRepositoryImpl : MarvelComicsRepository {
    private val retrofit = Retrofit.Builder()
        .baseUrl("https://gateway.marvel.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    var api: MarvelComicsApi = retrofit.create(MarvelComicsApi::class.java)

    override fun getPublishedMarvelComics(
        nowData: String,
        callback: (Comics) -> Unit
    ) {
        val marvelNetworkSecurity = MarvelNetworkSecurity()
        api.getPublishedComics(
            nowDate = nowData,
            formatType = "comic",
            orderBy = "-onsaleDate",
            timeStamp = marvelNetworkSecurity.timeStamp,
            apiKey = marvelNetworkSecurity.publicMarvelApiKey,
            hash = marvelNetworkSecurity.hashMd5ForMarvelRequest,
        ).enqueue(object : Callback<Date> {
            override fun onResponse(call: Call<Date>, response: Response<Date>) {
                response.body()?.let { callback(it.data) }
            }

            override fun onFailure(call: Call<Date>, t: Throwable) {
                println(t.printStackTrace())
            }
        })
    }
}