package com.example.dictionaryappakash.dataSources.localSource

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.dictionaryappakash.dataSources.wordsEntity

@Dao
interface WordDao {

    /** Observe the local database words
     *
     * return all the words details
     * */
    @Query("SELECT * FROM wordsEntity ORDER BY Id DESC")
    fun observeLocalWords(): LiveData<List<wordsEntity>>


    /** Insert word details to database */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWord(wordEntity: wordsEntity)

    /**
     * return all the words
     * */
   @Query("SELECT * FROM wordsEntity ORDER BY Id DESC")
   suspend  fun getAllData() : List<wordsEntity>

    /**
     * delete a specific row
     * */
    @Query("DELETE FROM wordsEntity WHERE Id = :id")
    suspend fun deleterWordData(id : Int)


    /**
     * get a specific word
     * */
    @Query("SELECT * FROM wordsEntity WHERE word = :word")
    suspend fun  getWordData(word:String): wordsEntity
}