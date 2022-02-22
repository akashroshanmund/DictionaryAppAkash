package com.example.dictionaryappakash.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.NavHostFragment
import com.example.dictionaryappakash.R
import com.example.dictionaryappakash.constantValues
import com.example.dictionaryappakash.dataSources.CentralRepository
import com.example.dictionaryappakash.dataSources.WordData
import com.example.dictionaryappakash.databinding.FragmentEmptyBinding
import com.example.dictionaryappakash.databinding.FragmentHomeBinding
import com.example.dictionaryappakash.viewModel.SharedViewModel

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [EmptyFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class EmptyFragment : Fragment() {
    val sharedViewModel : SharedViewModel by activityViewModels()
    private var  emptyFragmentBinding : FragmentEmptyBinding ?= null



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val fragmentBinding = FragmentEmptyBinding.inflate(inflater,container, false)
        emptyFragmentBinding = fragmentBinding

        return fragmentBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        emptyFragmentBinding?.apply {
            lifecycleOwner = viewLifecycleOwner
            viewModel = sharedViewModel
        }

        setObservatationActions()
    }


    private fun setObservatationActions(){

            sharedViewModel.wordData.observe(viewLifecycleOwner, Observer {
                if(it.word.equals(constantValues.EMPTY_STRING)){
                    NavHostFragment.findNavController(this).navigate(R.id.action_empty_Fragment_dest_to_search_Fragment_dest)
                }

            })






    }

}