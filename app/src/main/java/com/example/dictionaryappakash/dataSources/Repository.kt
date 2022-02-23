package com.example.dictionaryappakash.dataSources

import android.content.Context
import androidx.lifecycle.LiveData
import com.example.dictionaryappakash.dataSources.wordsEntity

interface Repository {

    fun observeLocalWords(): LiveData<List<wordsEntity>>

    fun makeRequestForWord(word :String)

    fun insertWordTodataBase(wordData :WordData)

    fun deleterWordData(id : Int)

    fun isConnectedToInternet(context : Context) : Boolean
}