package com.example.dictionaryappakash.dataSources.localSource

import androidx.lifecycle.LiveData
import com.example.dictionaryappakash.dataSources.wordsEntity

interface WordRepository {

    fun observeLocalWords(): LiveData<List<wordsEntity>>
}