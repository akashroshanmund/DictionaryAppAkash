package com.example.dictionaryappakash.dataSources.networkSource

import android.util.Log
import com.example.dictionaryappakash.constantValues
import com.example.dictionaryappakash.dataSources.WordData

import com.example.dictionaryappakash.dataSources.localSource.NetworkData
import com.example.dictionaryappakash.dataSources.localSource.NetworkServiceBuilder
import okhttp3.ResponseBody
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class NetworkRepository {
    val TAG = "DataFetchRequest"

    /* make a request to fetch search data from the given api */
    fun makeRequestForWord(searchWord : String) {

        var createService = NetworkServiceBuilder.createService(NetworkData::class.java)
        val makeRequest = createService.fetchWord(searchWord)

        makeRequest.enqueue(object : Callback<ResponseBody> {

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.d(TAG, "onFailure: "+t.message)
            }

            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                Log.d(TAG, "onResponse: "+response.isSuccessful)

                /* data received successfully */
                if(response.isSuccessful){
                    val jsonObject = JSONArray(response.body()?.string())
                    setWordData(jsonObject)
                }

            }

        })
    }

    fun setWordData( jsonArray: JSONArray)
    {
        val jsonObject = jsonArray.getJSONObject(0)
        with(jsonObject){
        WordData(
            try{getString(constantValues?.JSONString_Word)}catch (e: Exception){null},

            try{getJSONArray(constantValues?.JSONArray_PHONETICS)?.getJSONObject(0)
                ?.getString(constantValues?.JSONString_PHONETICSText)}catch (e: Exception){null},

            try{getJSONArray(constantValues.JSONArray_PHONETICS)?.getJSONObject(0)
                ?.getString(constantValues.JSONString_PHONETICSAudio)}catch (e: Exception){null},

            try{getString(constantValues.JSONString_Origin)}catch (e: Exception){null},

            try{getJSONArray(constantValues.JSONArray_MEANINGS)?.getJSONObject(0)
                ?.getString(constantValues.JSONString_MEANINGSPartOfSpeech)}catch (e: Exception){null},

            try{getJSONArray(constantValues.JSONArray_MEANINGS)?.getJSONObject(0)
                ?.getJSONArray(constantValues.JSONArray_DEFINITIONS)?.getJSONObject(0)
                ?.getString(constantValues.JSONString_DEFINITIONSDefinition)}catch (e: Exception){null},

            try{getJSONArray(constantValues.JSONArray_MEANINGS)?.getJSONObject(0)
                ?.getJSONArray(constantValues.JSONArray_DEFINITIONS)?.getJSONObject(0)
                ?.getString(constantValues.JSONList_DEFINITIONSSynonyms)}catch (e: Exception){null},
        )
            }

    }

}