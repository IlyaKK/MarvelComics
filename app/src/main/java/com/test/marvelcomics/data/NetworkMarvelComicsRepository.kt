package com.test.marvelcomics.data

import com.test.marvelcomics.data.retrofit.MarvelComicsApi
import com.test.marvelcomics.data.retrofit.MarvelNetworkSecurity
import com.test.marvelcomics.domain.entity.api.ComicApi
import com.test.marvelcomics.domain.entity.api.DataApi
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NetworkMarvelComicsRepository(val api: MarvelComicsApi) {

    fun getMarvelComics(
        dataRange: String,
        offset: Int,
        callback: (List<ComicApi>?) -> Unit
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
        ).enqueue(object : Callback<DataApi> {
            override fun onResponse(call: Call<DataApi>, response: Response<DataApi>) {
                response.body()?.let {
                    callback(it.dataComicsApi.comicsList)
                }
            }

            override fun onFailure(call: Call<DataApi>, t: Throwable) {
                println(t.printStackTrace())
            }
        })
    }
}