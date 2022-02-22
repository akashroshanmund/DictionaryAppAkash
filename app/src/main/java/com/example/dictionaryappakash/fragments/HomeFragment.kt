package com.example.dictionaryappakash.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.Observer
import androidx.navigation.fragment.NavHostFragment
import com.example.dictionaryappakash.R
import com.example.dictionaryappakash.constantValues
import com.example.dictionaryappakash.dataSources.CentralRepository
import com.example.dictionaryappakash.dataSources.WordData
import com.example.dictionaryappakash.databinding.FragmentHomeBinding
import com.example.dictionaryappakash.viewModel.SharedViewModel

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

        setObservatationActions()
    }

    private fun setObservatationActions(){


        getContext()?.let {
                sharedViewModel.getCentralRepository().observeLocalWords()
                ?.observe(viewLifecycleOwner, Observer {
                    if(it.size > 0) {
                        val wordData: WordData = sharedViewModel.getCentralRepository().getSearchedWordDataInstance(it)
                        wordData.word?.let { it1 -> sharedViewModel.setWordData(wordData) }
                        Log.d("checc", "setObservatationActions: "+wordData.word)
                    }
                })

        }
        sharedViewModel.wordData.observe(viewLifecycleOwner, Observer<WordData> {

            Log.d("checkk", "setObservatationActions: "+it)
            if(it.word.equals(constantValues.EMPTY_STRING)){
               NavHostFragment.findNavController(this).navigate(R.id.action_homeFragment_to_searchFragment)
            }
            else  if(it.word.equals(constantValues.DEFAULT_STRING)){
                NavHostFragment.findNavController(this).navigate(R.id.action_homeFragment_to_emptyFragment)
            }


        })


    }



    override fun onDestroy() {
        super.onDestroy()
    }
}