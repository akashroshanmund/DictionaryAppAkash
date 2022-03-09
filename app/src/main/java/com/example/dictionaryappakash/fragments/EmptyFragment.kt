package com.example.dictionaryappakash.fragments

import android.os.Bundle
import android.util.Log

import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.activity.addCallback
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.example.dictionaryappakash.IOnBackPressed
import com.example.dictionaryappakash.R
import com.example.dictionaryappakash.constantValues
import com.example.dictionaryappakash.databinding.FragmentEmptyBinding
import com.example.dictionaryappakash.viewModel.SharedViewModel



class EmptyFragment : Fragment(), IOnBackPressed {

    /* variable initialization */
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
        Log.i("EmptyFragment", "EmptyFragmentCreated")

       setObservatationActions()

    }


    /* observes for search status
    * status : result found -> go to home fragment
    * */
    private fun setObservatationActions() {

        sharedViewModel.screenStatus.observe(viewLifecycleOwner, Observer<String> {
            Log.i("EmptyFragment", "screenStatusObserve: $it")
            if (it == constantValues.RESULEFOUND) {
                activity?.supportFragmentManager?.beginTransaction()?.replace(R.id.nav_host_fragment, HomeFragment())?.addToBackStack("empty")?.commit()


            }
        })

    }

    override fun onDestroyView() {
        super.onDestroyView()
        emptyFragmentBinding = null
    }

    override fun onBackPressed(): Boolean {
        onDestroyView()
        return true
    }




}