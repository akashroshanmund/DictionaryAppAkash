package com.example.dictionaryappakash.fragments


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
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.example.dictionaryappakash.GridAdapter
import com.example.dictionaryappakash.R
import com.example.dictionaryappakash.constantValues
import com.example.dictionaryappakash.dataSources.WordData
import com.example.dictionaryappakash.databinding.FragmentHomeBinding
import com.example.dictionaryappakash.viewModel.SharedViewModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.view.*
import kotlinx.android.synthetic.main.fragment_home.*
import java.util.*


/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomeFragment : Fragment() {



    /* iniitalize variables */
    private var  homeFragmentBinding : FragmentHomeBinding ?= null
    private val sharedViewModel : SharedViewModel by activityViewModels()


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

        setObservatationActions()
    }

    /* observes for change in local data base
    *
    * if there is any change in the database then triggers various UI operations
    * */
    private fun setObservatationActions(){


        getContext()?.let {
            /* Observe for local data base change */
            Log.d("trigger", "setObservatationActions:check ")
                sharedViewModel.getCentralRepository().observeLocalWords()
                ?.observe(viewLifecycleOwner, Observer {


                    if(it.size > 0) {
                        /* get data in WordData Format */
                        val lst = sharedViewModel.getCentralRepository().localWordRepository.getWordDataList(it)

                        /* get last searched word */
                        val wordData: WordData = sharedViewModel.getCentralRepository().getSearchedWordDataInstance(it)

                        /* update live data */
                        wordData.word?.let { it1 -> sharedViewModel.setWordData(wordData) }
                        sharedViewModel.setScreenStatus(constantValues.RESULEFOUND)

                        /* set list view to show all recently searched words */
                        val listAdapter: ArrayAdapter<*>
                        var mListView = view?.findViewById<ListView>(R.id.lvRecentSearch)
                        var wordList: MutableList<String> = mutableListOf()

                        /* max length of lst is 6 : cache limit */
                        for (item in lst) {
                            item.word?.let { it1 -> wordList.add(it1) }
                        }

                        /* set lis adapter */
                        listAdapter = view?.context?.let { it1 ->
                            ArrayAdapter(
                                it1,
                                android.R.layout.simple_list_item_1, wordList
                            )
                        }!!

                        mListView?.adapter = listAdapter

                        /* listen to click on recent words list */
                        mListView?.setOnItemClickListener { adapterView, view, i, l ->

                            /* update local database to listen back from observer, which will refresh the data */
                            sharedViewModel.getCentralRepository().insertWordTodataBase(lst[i])
                        }

                        /* set grid view to show synonyms */
                        val gridView = view?.findViewById<GridView>(R.id.gridView)
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
                    else{
                        /* if there is no data in the database, initial the data base with welcome word */
                        sharedViewModel.getCentralRepository().makeRequestForWord("welcome")
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
            // findNavController().navigate(R.id.action_homeFragment_to_emptyFragment)

        })

//        sharedViewModel.backPressed.observe(viewLifecycleOwner, Observer<Int> {
//            if(it == 100){
//
//                findNavController().popBackStack()
//                sharedViewModel.setOnBackPress(50)
//            }
//        })
    }

    /* call when the audio button pressed */
    fun playAudio(audioString: String){
        sharedViewModel.getCentralRepository().networkRepo.playAudio(audioString)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        homeFragmentBinding = null
    }

}