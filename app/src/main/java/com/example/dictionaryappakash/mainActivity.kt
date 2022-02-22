package com.example.dictionaryappakash

import android.media.AudioManager
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.SearchView
import android.widget.SearchView.OnCloseListener
import android.widget.Toast
import androidx.activity.viewModels
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModel
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.room.Room
import com.example.dictionaryappakash.SearchHelper.SearchDebouncingHelper
import com.example.dictionaryappakash.constantValues.DICTSearch
import com.example.dictionaryappakash.constantValues.EMPTY_STRING
import com.example.dictionaryappakash.constantValues.RESULTNotFound
import com.example.dictionaryappakash.dataSources.CentralRepository
import com.example.dictionaryappakash.dataSources.WordData
import com.example.dictionaryappakash.dataSources.localSource.*
import com.example.dictionaryappakash.dataSources.networkSource.NetworkRepository
import com.example.dictionaryappakash.viewModel.SharedViewModel
import kotlinx.android.synthetic.main.activity_main.*
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.URL
import java.util.*
import kotlin.concurrent.thread
import kotlin.math.log

class mainActivity : AppCompatActivity() {

    val TAG = "MainActivityLog"
    val sharedViewModel: SharedViewModel by viewModels()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)



        val repository  = CentralRepository(applicationContext, sharedViewModel)
        sharedViewModel.setCentralRepository(repository)

        svMainSearchView.setOnCloseListener(object : SearchView.OnCloseListener,
            androidx.appcompat.widget.SearchView.OnCloseListener {
            override fun onClose(): Boolean {
                Log.d(TAG, "onClose:ss ")
                return  true
            }

        })
        svMainSearchView.setOnQueryTextListener(
            SearchDebouncingHelper(
                this@mainActivity.lifecycle
            ){newText ->
                newText?.let{
                    if(it.isEmpty() || it == ""){
                        sharedViewModel.setWordData(WordData(""))
                        sharedViewModel.setScreenStatus(DICTSearch)
                        Log.d(TAG, "onCreate: Empty")
                    }else{
                        repository.searchForWord(it)
                        sharedViewModel.setScreenStatus(RESULTNotFound)
                        Log.d(TAG, "onCreate: "+it)

                    }
                }

            }
        )


        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
        setupActionBarWithNavController(navController)


    }

}


