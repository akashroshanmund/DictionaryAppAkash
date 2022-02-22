package com.example.dictionaryappakash.dataSources

import android.content.Context
import androidx.room.Room
import com.example.dictionaryappakash.dataSources.localSource.LocalWordRepository
import com.example.dictionaryappakash.dataSources.localSource.WordDao
import com.example.dictionaryappakash.dataSources.localSource.WordDataBase
import com.example.dictionaryappakash.dataSources.networkSource.NetworkRepository
import com.example.dictionaryappakash.viewModel.SharedViewModel
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlin.concurrent.thread

class CentralRepository(
    val context : Context,
    val sharedViewModel: SharedViewModel,
    val networkRepo: NetworkRepository = NetworkRepository(),
    val  localWordRepository: LocalWordRepository = LocalWordRepository(context)
)
{


    fun getSearchedWordDataInstance(wordEntityLst : List<wordsEntity>) : WordData =
        convertWordEntityToWordData(wordEntityLst[0])


    private fun convertWordEntityToWordData(entity : wordsEntity):WordData =
         WordData(
             entity.word,
             entity.phoneticText,
             entity.phoneticAudioUri,
             entity.origin,
             entity.partOfSpeech,
             entity.definition,
             entity.synonyms
         )



    fun searchForWord(word: String){
        if(word == ""){
            return
        }
        sharedViewModel.centralRepository.value?.let {
            networkRepo.makeRequestForWord(word, it) }
    }

    fun updateSharedViewModel(wordData : WordData){
        sharedViewModel.setWordData(wordData)
    }

    fun updateScreenStatus(status : String){
        sharedViewModel.setScreenStatus(status)
    }

    fun insertWordTodataBase(wordData :WordData){
        GlobalScope.async {
            localWordRepository.insertWordToDatabase(wordData)
        }

    }

    fun observeLocalWords() = localWordRepository.observeLocalWords()



}