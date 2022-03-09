package com.example.dictionaryappakash.Dagger

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.example.dictionaryappakash.dataSources.CentralRepository
import com.example.dictionaryappakash.dataSources.localSource.LocalWordRepository
import com.example.dictionaryappakash.dataSources.localSource.WordDao
import com.example.dictionaryappakash.dataSources.localSource.WordDataBase
import com.example.dictionaryappakash.dataSources.networkSource.NetworkRepository
import com.example.dictionaryappakash.mainActivity
import com.example.dictionaryappakash.viewModel.SharedViewModel
import dagger.Module
import dagger.Provides
import dagger.android.ContributesAndroidInjector
import javax.inject.Singleton


@Module
class DictModule(private  val application: DictApplication) {


    @Singleton
    @Provides
    fun provideLocalDataBase(): WordDao {
        return Room.databaseBuilder(
            application,
            WordDataBase::class.java, "WordDataBase"
        ).build().wordDao()
    }

}