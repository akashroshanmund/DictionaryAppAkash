package com.example.dictionaryappakash.dataSources.localSource

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.dictionaryappakash.dataSources.wordsEntity

@Database(entities = [wordsEntity::class], version = 1, exportSchema = false)
abstract class WordDataBase : RoomDatabase(){
    abstract fun wordDao() : WordDao
}