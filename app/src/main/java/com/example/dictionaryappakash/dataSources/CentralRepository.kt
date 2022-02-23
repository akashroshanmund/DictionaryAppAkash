package com.example.dictionaryappakash.dataSources

import android.content.Context
import android.widget.Toast
import androidx.core.content.contentValuesOf
import com.example.dictionaryappakash.constantValues
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
) : Repository

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
    override fun makeRequestForWord(word: String){
        if(word == "" ){
            return
        }else if(!isConnectedToInternet(context)){
            Toast.makeText(context,"No Internet Connection", Toast.LENGTH_LONG).show()
            updateScreenStatus(constantValues.RESULEFOUND)
        }

        sharedViewModel.centralRepository.value?.let {
            networkRepo.makeRequestForWord(word, it) }
    }

    /* updates the screen status live data */
    fun updateScreenStatus(status : String){
        sharedViewModel.setScreenStatus(status)
    }
    fun updateWordData(data : WordData){
        sharedViewModel.setWordData(data)
    }


    fun getScreenStatus() = sharedViewModel.screenStatus.value


    //TODo
    override fun insertWordTodataBase(wordData :WordData){
        GlobalScope.async {

            val lst = localWordRepository.localDbRepoDao.getAllData()

            /* if database exceed the cachelimit, delet the oldest search */
            if(lst.size > CacheLimit && lst.isNotEmpty()){
                deleterWordData(lst[lst.size-1].Id)
            }

            for(item in lst){
                if(item.word == wordData.word){
                    deleterWordData(item.Id)
                   break
                }
            }

            /* add word to repository */
            localWordRepository.insertWordToDatabase(wordData)
        }

    }

    override fun isConnectedToInternet(context: Context) = networkRepo.isConnectedToInternet(context)

    override fun deleterWordData(id : Int){
        localWordRepository.localDbRepoDao.deleterWordData(id)
    }


    /* observer for database change */
    //todo
    override fun observeLocalWords() = localWordRepository.observeLocalWords()

}