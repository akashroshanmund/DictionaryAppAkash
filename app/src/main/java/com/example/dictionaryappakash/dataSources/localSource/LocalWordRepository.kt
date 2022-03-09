package com.example.dictionaryappakash.dataSources.localSource

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.room.Room
import com.example.dictionaryappakash.dataSources.WordData
import com.example.dictionaryappakash.dataSources.WordDataInterface
import com.example.dictionaryappakash.dataSources.wordsEntity
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class  LocalWordRepository (
    val context : Context,
    var localDbRepoDao: WordDao = Room.databaseBuilder(
        context,
        WordDataBase::class.java,"WordDataBase"
    ).build().wordDao()
) {

    /* observe for dat base change */
    fun observeLocalWords(): LiveData<List<wordsEntity>>{
       return localDbRepoDao.observeLocalWords()
    }

    suspend fun insertWordToDatabase(wordData : WordData){
            val wordEntity = WordData.convertWordDataToWordEntity(wordData)
            localDbRepoDao.insertWord(wordEntity)
        Log.i("Database Operation", "insertWordToDatabase: Word inserted ")
    }

    suspend fun deleterWordData(id : Int) = localDbRepoDao.deleterWordData(id)

    suspend  fun getAllData() = localDbRepoDao.getAllData()


}