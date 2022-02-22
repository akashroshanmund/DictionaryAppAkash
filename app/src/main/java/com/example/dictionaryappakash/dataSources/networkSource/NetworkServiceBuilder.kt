package com.example.dictionaryappakash.dataSources.localSource


import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


/* set up retrofit to fetch data from api */
object NetworkServiceBuilder {
    private val httpClient = OkHttpClient.Builder().build()

    private val serviceHolder =
        Retrofit
            .Builder()
            .baseUrl("https://api.dictionaryapi.dev/api/v2/entries/en/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(httpClient)
            .build()
    fun<T> createService(service: Class<T>) : T{
        return serviceHolder.create(service)
    }
}