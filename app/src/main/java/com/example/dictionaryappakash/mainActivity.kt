package com.example.dictionaryappakash

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.addCallback
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.dictionaryappakash.Dagger.DictApplication
import com.example.dictionaryappakash.SearchHelper.SearchDebouncingHelper
import com.example.dictionaryappakash.constantValues.DICTSearch
import com.example.dictionaryappakash.dataSources.CentralRepository
import com.example.dictionaryappakash.dataSources.localSource.*
import com.example.dictionaryappakash.fragments.EmptyFragment
import com.example.dictionaryappakash.fragments.HomeFragment
import com.example.dictionaryappakash.viewModel.SharedViewModel
import dagger.android.support.AndroidSupportInjection

import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject


class mainActivity : AppCompatActivity() {

    /*variable initialization */
    val TAG = "MainActivityLog"
    val sharedViewModel: SharedViewModel by viewModels()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        Log.i(TAG, "onCreate: "+ "MainActivity Created")

        /* set the repository to livedata so, can be used centrally */
        val repository  = CentralRepository(applicationContext, sharedViewModel)
        sharedViewModel.setCentralRepository(repository)



        /*set searchView Listener to listen to the user input
        *
        * search debouncing has been implemented with a delay of 600 milli seconds
        *  */
        svMainSearchView?.setQuery(" ",false)
        svMainSearchView.setOnQueryTextListener(
            SearchDebouncingHelper(
                this@mainActivity.lifecycle
            ){newText ->
                newText?.let{
                    /*set the search status in live data to switch between the fragments */
                    if(it.isEmpty()){
                        sharedViewModel.setScreenStatus(DICTSearch)
                    }else{
                        /* request to search in the api */
                        lifecycleScope.launch {
                            repository.makeRequestForWord(it)
                        }
                        Log.i(TAG, "OnSearch: "+ "Searching operation Starts")
                    }
                }

            }
        )


        supportFragmentManager.beginTransaction().replace(R.id.nav_host_fragment, HomeFragment()).addToBackStack("Home").commit()

    }

    override fun onBackPressed() {
        val fragment =
            this.supportFragmentManager.findFragmentById(R.id.nav_host_fragment)
        if(fragment is EmptyFragment){
            supportFragmentManager.popBackStack()
             svMainSearchView?.setQuery(sharedViewModel.wordData.value?.word,true)
            supportFragmentManager.beginTransaction().replace(R.id.nav_host_fragment, HomeFragment()).addToBackStack("Home").commit()
            Log.i(TAG, "OnBackPress: "+ "Remove Empty Fragment")
        }else if(fragment is HomeFragment){
            supportFragmentManager.popBackStack()
            Log.i(TAG, "OnBackPress: "+ "Application Closed")
            finish()
        }
    }
}

interface IOnBackPressed {
    fun onBackPressed(): Boolean
}


