package com.example.dictionaryappakash.dataSources.localSource

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.Room
import com.example.dictionaryappakash.dataSources.WordData
import com.example.dictionaryappakash.dataSources.wordsEntity
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class LocalWordRepository internal constructor(
    val context : Context,
    private val ioDispatcher : CoroutineDispatcher = Dispatchers.IO,
    val localDbRepoDao: WordDao = Room.databaseBuilder(
        context,
        WordDataBase::class.java,"WordDataBase"
    ).build().wordDao()
) {

    //todo
    /* observe for dat base change */
    fun observeLocalWords(): LiveData<List<wordsEntity>>{
       return localDbRepoDao.observeLocalWords()
    }
    //todo
    suspend fun insertWordToDatabase(wordData : WordData){
        withContext(ioDispatcher) {
            val wordEntity = convertWordDataWordEntity(wordData)
            localDbRepoDao.insertWord(wordEntity)
        }
    }

    /* returns list of WordData from List of WordsEntity */
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