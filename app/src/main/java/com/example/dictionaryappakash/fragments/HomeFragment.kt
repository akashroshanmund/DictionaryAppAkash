package com.example.dictionaryappakash.fragments

import android.content.Context
import android.opengl.Visibility
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import com.example.dictionaryappakash.R
import com.example.dictionaryappakash.constantValues
import com.example.dictionaryappakash.dataSources.CentralRepository
import com.example.dictionaryappakash.dataSources.WordData
import com.example.dictionaryappakash.databinding.FragmentHomeBinding
import com.example.dictionaryappakash.viewModel.SharedViewModel
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.Dispatcher
import kotlin.concurrent.thread

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomeFragment : Fragment() {



    private var  homeFragmentBinding : FragmentHomeBinding ?= null
    private val sharedViewModel : SharedViewModel by activityViewModels()
    lateinit var lst : List<String>
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

     // Inflate the layout for this fragment
        val fragmentBinding = FragmentHomeBinding.inflate(inflater,container, false)
        homeFragmentBinding = fragmentBinding

        return fragmentBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)





        homeFragmentBinding?.apply {
            lifecycleOwner = viewLifecycleOwner
            viewModel = sharedViewModel

        }
        homeFragmentBinding?.homeFragment = this



        setRecentWords()
        setObservatationActions()
    }


    fun setRecentWords(){



    }
    private fun setObservatationActions(){


        getContext()?.let {
                sharedViewModel.getCentralRepository().observeLocalWords()
                ?.observe(viewLifecycleOwner, Observer {
                    if(it.size > 0) {
                        val lst = sharedViewModel.getCentralRepository().localWordRepository.getWordDataList(it)
                        val wordData: WordData = sharedViewModel.getCentralRepository().getSearchedWordDataInstance(it)
                        wordData.word?.let { it1 -> sharedViewModel.setWordData(wordData) }
                        sharedViewModel.setScreenStatus(constantValues.RESULEFOUND)
                        setRecentWords()
                        Log.d("checc", "setObservatationActions: "+wordData.word)



                        val arrayAdapter: ArrayAdapter<*>
                         var array : MutableList<String>   = mutableListOf()
                        for(item in lst){
                            item.word?.let { it1 -> array.add(it1) }
                        }
                        // access the listView from xml file
                        var mListView = view?.findViewById<ListView>(R.id.lvRecentSearch)
                        arrayAdapter = view?.context?.let { it1 ->
                            ArrayAdapter(
                                it1,
                                android.R.layout.simple_list_item_1, array
                            )
                        }!!
                        mListView?.adapter = arrayAdapter
                        mListView?.setOnItemClickListener { adapterView, view, i, l ->
                            sharedViewModel.setWordData(lst[i])
                        }
                    }
                })

            sharedViewModel.dataFetched.observe(viewLifecycleOwner, Observer {

                if(sharedViewModel?.dataFetched?.value == true) {

                    sharedViewModel.setDataFetched(false)
                }
            })
            }


//        sharedViewModel.wordData.observe(viewLifecycleOwner, Observer<WordData> {
//
//            Log.d("checkk", "setObservatationActions: "+it)
//            if(it.word.equals(constantValues.EMPTY_STRING)){
//               NavHostFragment.findNavController(this).navigate(R.id.action_homeFragment_to_searchFragment)
//            }
//            else  if(it.word.equals(constantValues.DEFAULT_STRING)){
//                NavHostFragment.findNavController(this).navigate(R.id.action_homeFragment_to_emptyFragment)
//            }
//
//
//        })

        sharedViewModel.screenStatus.observe(viewLifecycleOwner, Observer<String> {

            Log.d("checkk", "setObservatationActions: "+it)

            if(it != constantValues.RESULEFOUND)
             NavHostFragment.findNavController(this).navigate(R.id.action_homeFragment_to_emptyFragment)
        })


    }


    fun playAudio(audioString: String){
        Log.d("TAG", "playAudio: "+audioString)
        sharedViewModel.getCentralRepository().networkRepo.playAudio(audioString)
    }



    override fun onDestroy() {
        super.onDestroy()
    }
}