package com.example.dictionaryappakash.dataSources

import android.content.Context
import android.content.Entity
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
        localWordRepository.convertWordEntityToWordData(wordEntityLst[0])


    fun getAllWordData(): List<WordData>{
        return  localWordRepository.getLatestData()
    }

    fun getRecentWords(): List<String>{
        return localWordRepository.getRecentWords()
    }





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

            val lst = localWordRepository.localDbRepoDao.getAllData()
            if(lst.size > 5 && lst.isNotEmpty()){
                localWordRepository.localDbRepoDao.deleterWordData(lst[lst.size-1].Id)
            }

            var isAvailable : Boolean = false
            for(item in lst){
                if(item.word == wordData.word){
                   isAvailable = true
                   break
                }
            }

            if(!isAvailable)
                localWordRepository.insertWordToDatabase(wordData)
        }

    }

    fun getWordData(word:String):WordData{
        return localWordRepository.getWordData(word)
    }

    fun observeLocalWords() = localWordRepository.observeLocalWords()



}