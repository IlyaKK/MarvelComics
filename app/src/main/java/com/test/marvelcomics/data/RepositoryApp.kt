package com.test.marvelcomics.data

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.test.marvelcomics.data.database.*
import com.test.marvelcomics.data.retrofit.MarvelComicsApi
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private const val DB_PATH = "comics.db"

class RepositoryApp : Application() {
    private val comicsDb by lazy {
        Room.databaseBuilder(
            this,
            ComicsDatabase::class.java,
            DB_PATH
        ).build()
    }

    private val comicDao: ComicDao by lazy {
        comicsDb.comicDao()
    }

    private val writerDao: WriterDao by lazy {
        comicsDb.writerDao()
    }

    private val painterDao: PainterDao by lazy {
        comicsDb.painterDao()
    }

    private val comicWriterJoinDao: ComicWriterJoinDao by lazy {
        comicsDb.comicWriterJoinDao()
    }

    private val comicPainterJoinDao: ComicPainterJoinDao by lazy {
        comicsDb.comicPainterJoinDao()
    }

    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl("https://gateway.marvel.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    private val api: MarvelComicsApi = retrofit.create(MarvelComicsApi::class.java)

    val comicDatabaseRepo: DataBaseComicsRepository by lazy {
        DataBaseComicsRepository(
            comicDao,
            writerDao,
            painterDao,
            comicWriterJoinDao,
            comicPainterJoinDao
        )
    }
    val comicNetworkRepo: NetworkMarvelComicsRepository by lazy { NetworkMarvelComicsRepository(api) }
}

val Context.repositoryApp
    get() = applicationContext as RepositoryApp


