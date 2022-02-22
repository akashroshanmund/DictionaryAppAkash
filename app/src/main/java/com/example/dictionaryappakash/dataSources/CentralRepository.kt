package com.example.dictionaryappakash.dataSources

import android.content.Context
import com.example.dictionaryappakash.constantValues.CacheLimit

import com.example.dictionaryappakash.dataSources.localSource.LocalWordRepository
import com.example.dictionaryappakash.dataSources.networkSource.NetworkRepository
import com.example.dictionaryappakash.viewModel.SharedViewModel
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async

class CentralRepository(
    val context : Context,
    private val sharedViewModel: SharedViewModel,
    val networkRepo: NetworkRepository = NetworkRepository(),
    val  localWordRepository: LocalWordRepository = LocalWordRepository(context)
)

{
    /* return the latest searched data */
    fun getSearchedWordDataInstance(wordEntityLst : List<wordsEntity>) : WordData =
        localWordRepository.convertWordEntityToWordData(wordEntityLst[0])



    /* returns the list of strings  as synonyms */
    fun getSynonymsList(synonyms : String): List<String>{
        var res : List<String> = synonyms.split("_").map{
            it.trim()}
        return  res
        }

    /* makes request to search word from the api */

    //TODO
    fun makeRequestForWord(word: String){
        if(word == ""){
            return
        }
        sharedViewModel.centralRepository.value?.let {
            networkRepo.makeRequestForWord(word, it) }
    }

    /* updates the screen status live data */
    fun updateScreenStatus(status : String){
        sharedViewModel.setScreenStatus(status)
    }

    //TODo
    fun insertWordTodataBase(wordData :WordData){
        GlobalScope.async {

            val lst = localWordRepository.localDbRepoDao.getAllData()

            /* if database exceed the cachelimit, delet the oldest search */
            if(lst.size > CacheLimit && lst.isNotEmpty()){
                localWordRepository.localDbRepoDao.deleterWordData(lst[lst.size-1].Id)
            }

            for(item in lst){
                if(item.word == wordData.word){
                    localWordRepository.localDbRepoDao.deleterWordData(item.Id)
                   break
                }
            }

            /* add word to repository */
            localWordRepository.insertWordToDatabase(wordData)
        }

    }

    /* observer for database change */
    //todo
    fun observeLocalWords() = localWordRepository.observeLocalWords()

}