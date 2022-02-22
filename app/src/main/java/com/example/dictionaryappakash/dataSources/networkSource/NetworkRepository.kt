package com.example.dictionaryappakash.dataSources.networkSource

import android.media.AudioManager
import android.media.MediaPlayer
import android.util.Log
import com.example.dictionaryappakash.constantValues
import com.example.dictionaryappakash.dataSources.CentralRepository
import com.example.dictionaryappakash.dataSources.WordData
import com.example.dictionaryappakash.dataSources.localSource.*
import okhttp3.ResponseBody
import org.json.JSONArray
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response



class NetworkRepository() {

    val TAG = "DataFetchRequest"

    /* make a request to fetch search data from the given api */
    fun makeRequestForWord(searchWord : String, centralRepository: CentralRepository) {

        /* get instances */
        var createService = NetworkServiceBuilder.createService(NetworkData::class.java)
        val makeRequest = createService.fetchWord(searchWord)

        /* fetch word in background */
        makeRequest.enqueue(object : Callback<ResponseBody> {

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.d(TAG, "onFailure: "+t.message)
            }

            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                Log.d(TAG, "onResponse: "+response.isSuccessful)

                /* data received successfully */
                if(response.isSuccessful){
                    val jsonObject = JSONArray(response.body()?.string())

                    /* get json dat in form of WordData object */
                    val wordData = setWordData(jsonObject)

                    /* insert data to local database and update screen status */
                    centralRepository.insertWordTodataBase(wordData)
                    centralRepository.updateScreenStatus(constantValues.RESULEFOUND)

                }else{
                    /* if response is false set result not found */
                    centralRepository.updateScreenStatus(constantValues.RESULTNotFound)
                }
            }
        })
    }

    private fun setWordData( jsonArray: JSONArray):WordData
    {

        val jsonObject = jsonArray.getJSONObject(0)
        var synonyms : MutableList<String> = mutableListOf()
        var synonymsStr  = ""

        /* check for synonyms, as it is not available for every json object*/
        with(jsonObject) {
            try {
                val jsonArray = getJSONArray(constantValues.JSONArray_MEANINGS)?.getJSONObject(0)
                    ?.getJSONArray(constantValues.JSONArray_DEFINITIONS)?.getJSONObject(0)
                    ?.getJSONArray(constantValues.JSONList_DEFINITIONSSynonyms)
                /* if found convert is to a string array list */
                for(i in 0 until jsonArray?.length()!!){
                    synonyms.add(jsonArray.getString(i))
                }
                synonymsStr = synonyms.joinToString("_")

            }catch (e : java.lang.Exception){
                Log.d(TAG, "setWordData: Synonym not found")
            }
        }

        /* extract data and return */
        return   with(jsonObject){
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
                synonymsStr
        )
            }
    }

    //todo
    fun playAudio(audioUri:String){
        Log.d(TAG, "playAudio: ")
        val mediaplayer : MediaPlayer = MediaPlayer()
        mediaplayer.setAudioStreamType(AudioManager.STREAM_MUSIC)
        try {
            mediaplayer.setDataSource(audioUri)
            mediaplayer.prepare()
            mediaplayer.start()
        }catch (e : Exception){

        }
    }

}