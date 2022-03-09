package com.example.dictionaryappakash.dataSources

import android.content.Context
import androidx.lifecycle.LiveData
import com.example.dictionaryappakash.dataSources.wordsEntity

interface Repository {

    fun observeLocalWords(): LiveData<List<wordsEntity>>

    suspend fun makeRequestForWord(word :String)

    suspend fun insertWordTodataBase(wordData :WordData)

    suspend fun deleterWordData(id : Int)

    suspend fun playAudio(audioUri : String)

    fun isConnectedToInternet(context : Context) : Boolean
}