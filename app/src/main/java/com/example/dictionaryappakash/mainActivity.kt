package com.example.dictionaryappakash

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.dictionaryappakash.SearchHelper.SearchDebouncingHelper
import com.example.dictionaryappakash.constantValues.DICTSearch
import com.example.dictionaryappakash.dataSources.CentralRepository
import com.example.dictionaryappakash.dataSources.localSource.*
import com.example.dictionaryappakash.viewModel.SharedViewModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.view.*
import java.util.*


class mainActivity : AppCompatActivity() {

    /*variable initialization */
    val TAG = "MainActivityLog"
    val sharedViewModel: SharedViewModel by viewModels()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

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
                        repository.makeRequestForWord(it)
                    }
                }

            }
        )


        /* initialize navigation controller */
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
        setupActionBarWithNavController(navController)

    }

}


