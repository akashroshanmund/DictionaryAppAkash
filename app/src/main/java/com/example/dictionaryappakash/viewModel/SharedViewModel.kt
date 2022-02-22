package com.example.dictionaryappakash.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.dictionaryappakash.constantValues
import com.example.dictionaryappakash.dataSources.CentralRepository
import com.example.dictionaryappakash.dataSources.WordData
import kotlin.contracts.contract

class SharedViewModel : ViewModel(){
    val TAG = "ViewModel"

    private val _wordData = MutableLiveData<WordData>()
    val wordData : LiveData<WordData> = _wordData

    private val _screenStatus = MutableLiveData<String>()
    val screenStatus : LiveData<String> =_screenStatus

    private val _dataFetched = MutableLiveData<Boolean>()
    val dataFetched : LiveData<Boolean> = _dataFetched



    private val _internetConnection = MutableLiveData<Boolean>()
    val internetConnection : LiveData<Boolean> = _internetConnection

    private val _centralRepository = MutableLiveData<CentralRepository>()
    val centralRepository : LiveData<CentralRepository> = _centralRepository


     fun setWordData(data : WordData){

            _wordData.value = data
            Log.d(TAG, "setWordData: " + data.word)

    }

     fun setCentralRepository(repository: CentralRepository){
            _centralRepository.value = repository
    }

    fun setDataFetched(bool: Boolean){
        _dataFetched.value = bool
    }

    fun getCentralRepository() : CentralRepository{
        return centralRepository.value!!
    }

    fun setScreenStatus(status : String){
        _screenStatus.value = status
    }


}