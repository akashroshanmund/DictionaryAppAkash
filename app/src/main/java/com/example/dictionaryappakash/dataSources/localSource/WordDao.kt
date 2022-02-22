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
     fun insertWord(wordEntity: wordsEntity)

    /**
     * return all the words
     * */
   @Query("SELECT * FROM wordsEntity ORDER BY Id DESC")
     fun getAllData() : List<wordsEntity>

    /**
     * delete a specific row
     * */
    @Query("DELETE FROM wordsEntity WHERE Id = :id")
    fun deleterWordData(id : Int)

    @Query("SELECT word FROM wordsEntity ORDER BY Id DESC LIMIT 5")
    fun getRecetnWords(): List<String>

    /**
     * get a specific word
     * */
    @Query("SELECT * FROM wordsEntity WHERE word = :word")
    fun  getWordData(word:String): wordsEntity
}