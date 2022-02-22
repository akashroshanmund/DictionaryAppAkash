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

    @Query("SELECT * FROM wordsEntity ORDER BY Id DESC LIMIT 6")
    suspend fun getLatestData():List<wordsEntity>
}