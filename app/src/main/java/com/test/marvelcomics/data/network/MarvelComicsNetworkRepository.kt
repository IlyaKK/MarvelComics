package com.test.marvelcomics.data.network

import android.content.Context
import android.content.Context.CONNECTIVITY_SERVICE
import android.net.ConnectivityManager
import android.net.NetworkInfo
import com.test.marvelcomics.R
import com.test.marvelcomics.domain.entity.api.DataApi
import java.io.IOException

class MarvelComicsNetworkRepository(
    private val context: Context,
    private val marvelComicsService: MarvelComicsService
) {
    suspend fun getPublishedComics(offset: Int, limit: Int, dataRange: String): DataApi {
        checkInternet()
        val marvelNetworkSecurity = MarvelNetworkSecurity()
        return marvelComicsService.getPublishedComics(
            nowDate = dataRange,
            formatType = "comic",
            format = "comic",
            noVariants = true,
            orderBy = "-onsaleDate",
            offset = offset,
            limit = limit,
            timeStamp = marvelNetworkSecurity.timeStamp,
            apiKey = marvelNetworkSecurity.publicMarvelApiKey,
            hash = marvelNetworkSecurity.hashMd5ForMarvelRequest
        )
    }

    private fun checkInternet() {
        val connection = context.getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo: NetworkInfo = connection.activeNetworkInfo
            ?: throw IOException(context.getString(R.string.no_internet_connection))
        if (!networkInfo.isConnected) {
            throw IOException(context.getString(R.string.no_internet_connection))
        }
    }
}