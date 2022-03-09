package com.example.dictionaryappakash.dataSources

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.example.dictionaryappakash.AppHelperFunction
import com.example.dictionaryappakash.constantValues
import com.example.dictionaryappakash.constantValues.CacheLimit

import com.example.dictionaryappakash.dataSources.localSource.LocalWordRepository
import com.example.dictionaryappakash.dataSources.networkSource.NetworkRepository
import com.example.dictionaryappakash.viewModel.SharedViewModel

class CentralRepository  (
    val context : Context,
    private val sharedViewModel: SharedViewModel,
    val networkRepo: NetworkRepository = NetworkRepository(),
    val  localWordRepository: LocalWordRepository = LocalWordRepository(context)
) : Repository

{
    /* makes request to search word from the api */
    override suspend fun makeRequestForWord(word: String){
        if(word == "" ){
            return
        }else if(!isConnectedToInternet(context)){   /* notify if network is not connected */
            Toast.makeText(context,"No Internet Connection", Toast.LENGTH_LONG).show()
            updateScreenStatus(constantValues.RESULEFOUND)
            Log.i("MakeWordRequest", "makeRequestForWord: "+ "No Internet Connection")
        }else {

            sharedViewModel.centralRepository.value?.let {
                networkRepo.makeRequestForWord(word, it)
            }
            Log.i("MakeWordRequest", "makeRequestForWord: "+ "Word Search Begins")
        }
    }


    override suspend fun insertWordTodataBase(wordData :WordData){

            val lst = localWordRepository.localDbRepoDao.getAllData()

            /* if database exceed the cachelimit, delet the oldest search */
            if(lst.size > CacheLimit && lst.isNotEmpty()){
                deleterWordData(lst[lst.size-1].Id)
                Log.i("DataBaseOperation", "insertWordTodataBase: "+ "more than cache limit")
            }

            for(item in lst){
                if(item.word == wordData.word){
                    deleterWordData(item.Id)
                   break
                }
            }

            /* add word to repository */
            localWordRepository.insertWordToDatabase(wordData)
             Log.i("DataBaseOperation", "insertWordTodataBase: "+ "Word Added To database")

    }

    override fun isConnectedToInternet(context: Context) = AppHelperFunction.isConnectedToInternet(context)

    override suspend fun deleterWordData(id : Int){
        localWordRepository.localDbRepoDao.deleterWordData(id)
    }


    /* observer for database change */
    override fun observeLocalWords() = localWordRepository.observeLocalWords()

    /* play audio */
    override suspend fun playAudio(audioUri:String){

        if(!isConnectedToInternet(context)){   /* notify if network is not connected */
            Toast.makeText(context,"No Internet Connection", Toast.LENGTH_LONG).show()
            Log.i("AudioOperation", "playAudio: "+ "No Internet Conncetion")
        }else{
            AppHelperFunction.playAudio(audioUri)
            Log.i("AudioOperation", "playAudio: "+ "Audio Play Triggered")
        }

    }



    /* return the latest searched data */
    fun getSearchedWordDataInstance(wordEntityLst : List<wordsEntity>) : WordData =
        wordsEntity.convertWordEntityToWordData(wordEntityLst[0])

    /* returns list of WordData from List of WordsEntity */
    fun getWordDataList(lst : List<wordsEntity>): List<WordData>{
        var datalst : MutableList<WordData> = mutableListOf()
        for (item in lst)
            datalst.add(wordsEntity.convertWordEntityToWordData(item))
        return datalst
    }

    /* returns the list of strings  as synonyms */
    fun getSynonymsList(synonyms : String): List<String> =  (synonyms.split("_").map{ it.trim()}).take(4)

    /* updates the screen status live data */
    fun updateScreenStatus(status : String) = sharedViewModel.setScreenStatus(status)

    fun updateWordData(data : WordData) = sharedViewModel.setWordData(data)

    fun getScreenStatus() = sharedViewModel.screenStatus.value


}