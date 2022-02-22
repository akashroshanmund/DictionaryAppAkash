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
     * return all the tasks
     * */
    @Query("SELECT * FROM wordsEntity ORDER BY Id DESC")
    fun observeLocalWords(): LiveData<List<wordsEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
     fun insertWord(wordEntity: wordsEntity)


   @Query("SELECT * FROM wordsEntity ORDER BY Id DESC")
     fun getAllData() : List<wordsEntity>

    @Query("DELETE FROM wordsEntity WHERE Id = :id")
    fun deleterWordData(id : Int)

    @Query("SELECT word FROM wordsEntity ORDER BY Id DESC LIMIT 5")
    fun getRecetnWords(): List<String>

    @Query("SELECT * FROM wordsEntity WHERE word = :word")
    fun  getWordData(word:String): wordsEntity
}