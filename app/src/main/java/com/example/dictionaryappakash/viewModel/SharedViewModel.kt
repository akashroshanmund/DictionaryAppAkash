package com.example.dictionaryappakash.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.dictionaryappakash.dataSources.CentralRepository
import com.example.dictionaryappakash.dataSources.WordData



class SharedViewModel : ViewModel(){
    val TAG = "ViewModel"

    private val _wordData = MutableLiveData<WordData>()
    val wordData : LiveData<WordData> = _wordData

    private val _screenStatus = MutableLiveData<String>()
    val screenStatus : LiveData<String> =_screenStatus


    private val _centralRepository = MutableLiveData<CentralRepository>()
    val centralRepository : LiveData<CentralRepository> = _centralRepository


     fun setWordData(data : WordData){
            _wordData.value = data

    }

     fun setCentralRepository(repository: CentralRepository){
            _centralRepository.value = repository
    }


    fun getCentralRepository() : CentralRepository{
        return centralRepository.value!!
    }

    fun setScreenStatus(status : String){
        _screenStatus.value = status
    }

}