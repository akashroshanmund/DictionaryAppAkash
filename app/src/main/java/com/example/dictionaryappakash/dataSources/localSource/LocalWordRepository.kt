package com.example.dictionaryappakash.dataSources.localSource

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import androidx.room.Room
import com.example.dictionaryappakash.dataSources.WordData
import com.example.dictionaryappakash.dataSources.wordsEntity
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.concurrent.thread

class LocalWordRepository internal constructor(
    val context : Context,
    private val ioDispatcher : CoroutineDispatcher = Dispatchers.IO,
    val localDbRepoDao: WordDao = Room.databaseBuilder(
        context,
        WordDataBase::class.java,"WordDataBase"
    ).build().wordDao()
) {


    fun observeLocalWords(): LiveData<List<wordsEntity>>{
       return localDbRepoDao.observeLocalWords()
    }

    suspend fun insertWordToDatabase(wordData : WordData){
        withContext(ioDispatcher){
            val wordEntity = convertWordDataWordEntity(wordData)

            localDbRepoDao.insertWord(wordEntity)
        }


    }

    fun getLatestData():List<WordData> {

       return getWordDataList(localDbRepoDao.getAllData())
    }

    fun getWordData(word:String): WordData{

        return convertWordEntityToWordData(localDbRepoDao.getWordData(word))
    }

    fun getRecentWords() : List<String>{
        return localDbRepoDao.getRecetnWords()
    }

     fun getWordDataList(lst : List<wordsEntity>): List<WordData>{
        var datalst : MutableList<WordData> = mutableListOf()
        for (item in lst)
            datalst.add(convertWordEntityToWordData(item))

        return datalst
    }

     fun convertWordEntityToWordData(entity : wordsEntity):WordData =
        WordData(
            entity.word,
            entity.phoneticText,
            entity.phoneticAudioUri,
            entity.origin,
            entity.partOfSpeech,
            entity.definition,
            entity.synonyms
        )
    private fun convertWordDataWordEntity(entity : WordData):wordsEntity =
        wordsEntity(
            0,
            entity.word?:"",
            entity.phoneticText?:"",
            entity.phoneticAudioUri?:"",
            entity.origin?:"",
            entity.partOfSpeech?:"",
            entity.definition?:"",
            entity.synonyms?:""
        )

}