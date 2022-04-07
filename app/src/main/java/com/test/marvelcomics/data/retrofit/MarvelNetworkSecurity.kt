package com.test.marvelcomics.data.retrofit

import com.test.marvelcomics.BuildConfig
import java.math.BigInteger
import java.security.MessageDigest

class MarvelNetworkSecurity {
    var timeStamp: String

    init {
        val intRange = 0..3000
        timeStamp = intRange.random().toString()
    }

    private val privateMarvelApiKey: String = BuildConfig.MARVEL_PRIVATE_API_KEY

    val publicMarvelApiKey: String = BuildConfig.MARVEL_PUBLIC_API_KEY

    val hashMd5ForMarvelRequest: String
        get() {
            val secureString: String = timeStamp + privateMarvelApiKey + publicMarvelApiKey
            val crypt = MessageDigest.getInstance("MD5")
            crypt.update(secureString.toByteArray())
            return BigInteger(1, crypt.digest()).toString(16)
        }
}