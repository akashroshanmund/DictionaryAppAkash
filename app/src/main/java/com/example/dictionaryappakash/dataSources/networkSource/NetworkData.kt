package com.example.dictionaryappakash.dataSources.localSource


import WordRoot
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path


interface NetworkData {

    /* create request to return the details about the searched word */
    @GET("{word}")
    fun fetchWord(@Path("word") word : String) : Call<List<WordRoot>>
}