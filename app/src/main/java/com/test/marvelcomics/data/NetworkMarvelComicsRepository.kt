package com.test.marvelcomics.data

import com.test.marvelcomics.data.retrofit.MarvelComicsApi
import com.test.marvelcomics.data.retrofit.MarvelNetworkSecurity
import com.test.marvelcomics.domain.entity.api.Comic
import com.test.marvelcomics.domain.entity.api.Comics
import com.test.marvelcomics.domain.entity.api.Data
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NetworkMarvelComicsRepository(val api: MarvelComicsApi) {

    fun getMarvelComics(
        dataRange: String,
        offset: Int,
        callback: (List<Comic>?) -> Unit
    ) {
        val marvelNetworkSecurity = MarvelNetworkSecurity()
        api.getPublishedComics(
            nowDate = dataRange,
            formatType = "comic",
            orderBy = "-onsaleDate",
            offset = offset,
            timeStamp = marvelNetworkSecurity.timeStamp,
            apiKey = marvelNetworkSecurity.publicMarvelApiKey,
            hash = marvelNetworkSecurity.hashMd5ForMarvelRequest,
        ).enqueue(object : Callback<Data> {
            override fun onResponse(call: Call<Data>, response: Response<Data>) {
                response.body()?.let {
                    callback(it.dataComics.comicsList)
                }
            }

            override fun onFailure(call: Call<Data>, t: Throwable) {
                println(t.printStackTrace())
            }
        })
    }
}