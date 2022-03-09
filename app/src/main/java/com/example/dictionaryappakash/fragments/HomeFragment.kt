package com.example.dictionaryappakash.fragments


import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.example.dictionaryappakash.GridAdapter
import com.example.dictionaryappakash.R
import com.example.dictionaryappakash.constantValues
import com.example.dictionaryappakash.dataSources.WordData
import com.example.dictionaryappakash.dataSources.localSource.LocalWordRepository
import com.example.dictionaryappakash.dataSources.localSource.WordDao
import com.example.dictionaryappakash.databinding.FragmentHomeBinding
import com.example.dictionaryappakash.viewModel.SharedViewModel
import dagger.android.support.AndroidSupportInjection

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject


class HomeFragment : Fragment() {

    /* iniitalize variables */
    private var  homeFragmentBinding : FragmentHomeBinding ?= null
    private val sharedViewModel : SharedViewModel by activityViewModels()



    override fun onAttach(context: Context) {
        super.onAttach(context)
        AndroidSupportInjection.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val fragmentBinding = FragmentHomeBinding.inflate(inflater,container, false)
        homeFragmentBinding = fragmentBinding
        return fragmentBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        /* bind data with xml */
        homeFragmentBinding?.apply {
            lifecycleOwner = viewLifecycleOwner
            viewModel = sharedViewModel
        }
        homeFragmentBinding?.homeFragment = this

        Log.i("HomeFragment", "HomeFragmentCreated")
        setObservatationActions()
    }

    /* observes for change in local data base
    *
    * if there is any change in the database then triggers various UI operations
    * */
    private fun setObservatationActions(){

        /* set list view to show all recently searched words */
        var mListView = view?.findViewById<ListView>(R.id.lvRecentSearch)


        /* set grid view to show synonyms */
        val gridView = view?.findViewById<GridView>(R.id.gridView)

        getContext()?.let {
            /* Observe for local data base change */
                sharedViewModel.getCentralRepository().observeLocalWords()
                ?.observe(viewLifecycleOwner, Observer {
                    Log.i("HomeFragment", "change in database noticed")

                    if(it.size > 0) {

                        /* get data in WordData Format */
                        val lst = sharedViewModel.getCentralRepository().getWordDataList(it)

                        /* get last searched word */
                        val wordData: WordData = sharedViewModel.getCentralRepository().getSearchedWordDataInstance(it)

                        /* update live data */
                        wordData.word?.let { it1 -> sharedViewModel.setWordData(wordData) }
                        sharedViewModel.setScreenStatus(constantValues.RESULEFOUND)

                        /* max length of lst is 6 : cache limit */
                        var wordList: MutableList<String> = mutableListOf()

                        for (item in lst) {
                            item.word?.let { it1 -> wordList.add(it1) }
                        }

                        /* set lis adapter */
                        val listAdapter = view?.context?.let { it1 ->
                            ArrayAdapter(
                                it1,
                                android.R.layout.simple_list_item_1, wordList
                            )
                        }!!

                        mListView?.adapter = listAdapter

                        /* listen to click on recent words list */
                        mListView?.setOnItemClickListener { adapterView, view, i, l ->

                            /* update local database to listen back from observer, which will refresh the data */
                            GlobalScope.launch {
                                sharedViewModel.getCentralRepository().insertWordTodataBase(lst[i])
                            }
                            Log.i("HomeFragment", "clicked on cache word")
                        }

                        val mainAdapter = view?.context?.let { it1 ->
                            wordData?.synonyms?.let { it2 ->
                                sharedViewModel.getCentralRepository().getSynonymsList(
                                    it2
                                )
                            }?.let { it3 ->
                                GridAdapter(it1,
                                    it3
                                )
                            }
                        }
                        gridView?.adapter = mainAdapter
                    }
                    else {
                        /* if there is no data in the database, initial the data base with welcome word */
                        lifecycleScope.launch {
                        sharedViewModel.getCentralRepository().makeRequestForWord("welcome")
                    }
                        sharedViewModel.setWordData(WordData("Welcome"))
                    }

                })

            }

        /* set observer to look at search status
        * status : result not found go to empty fragment
        * */
        sharedViewModel.screenStatus.observe(viewLifecycleOwner, Observer<String> {

            if(it != constantValues.RESULEFOUND) {
                activity?.supportFragmentManager?.beginTransaction()?.replace(R.id.nav_host_fragment, EmptyFragment())?.addToBackStack("tr")?.commit()
            }
        })
    }

    /* call when the audio button pressed */
    fun playAudio(audioString: String){
        lifecycleScope.launch {
            sharedViewModel.getCentralRepository().playAudio(audioString)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        homeFragmentBinding = null
    }

}