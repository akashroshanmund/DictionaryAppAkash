package com.example.dictionaryappakash.dataSources.networkSource

import WordRoot
import android.content.Context
import android.media.AudioManager
import android.media.MediaPlayer
import android.net.*
import android.os.Build
import android.util.Log
import com.example.dictionaryappakash.constantValues
import com.example.dictionaryappakash.dataSources.CentralRepository
import com.example.dictionaryappakash.dataSources.WordData
import com.example.dictionaryappakash.dataSources.localSource.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import org.json.JSONArray
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject
import kotlin.math.log


class NetworkRepository @Inject constructor(){

    val TAG = "DataFetchRequest"


    /* make a request to fetch search data from the given api */
    suspend fun makeRequestForWord(searchWord : String, centralRepository: CentralRepository) {

        /* get instances */
        var createService = NetworkServiceBuilder.createService(NetworkData::class.java)
        val makeRequest = createService.fetchWord(searchWord)

        /* fetch word in background */
        makeRequest.enqueue(object : Callback<List<WordRoot>> {

            override fun onFailure(call: Call<List<WordRoot>>, t: Throwable) {

            }

            override fun onResponse(call: Call<List<WordRoot>>, response: Response<List<WordRoot>>) {

                Log.i(TAG, "onResponse: ${response.isSuccessful}")
                    /* data received successfully */
                    if (response.isSuccessful) {

                        /* get json data in form of WordData object */
                        val wordData = response.body()?.get(0)!!.getWordData()



                        /* insert data to local database and update screen status */
                        GlobalScope.launch(Dispatchers.IO) {
                            centralRepository.insertWordTodataBase(wordData)
                        }
                        if (centralRepository.getScreenStatus() != constantValues.RESULEFOUND) {
                            centralRepository.updateWordData(wordData)
                            centralRepository.updateScreenStatus(constantValues.RESULEFOUND)
                        }

                    } else {
                        /* if response is false set result not found */
                        centralRepository.updateScreenStatus(constantValues.RESULTNotFound)
                    }

            }
        })
    }






}