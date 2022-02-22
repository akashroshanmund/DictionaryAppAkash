package com.example.dictionaryappakash.fragments

import android.os.Bundle

import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.NavHostFragment
import com.example.dictionaryappakash.R
import com.example.dictionaryappakash.constantValues
import com.example.dictionaryappakash.databinding.FragmentEmptyBinding
import com.example.dictionaryappakash.viewModel.SharedViewModel
import kotlinx.android.synthetic.main.fragment_empty.*
import java.util.*

/**
 * A simple [Fragment] subclass.
 * Use the [EmptyFragment.newInstance] factory mehod to
 * create an instance of this fragment.
 */
class EmptyFragment : Fragment() {

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

        setObservatationActions()
    }


    /* observes for search status
    * status : result found -> go to home fragment
    * */
    private fun setObservatationActions() {
        sharedViewModel.screenStatus.observe(viewLifecycleOwner, Observer<String> {
            if (it == constantValues.RESULEFOUND) {
                NavHostFragment.findNavController(this)
                    .navigate(R.id.action_empty_Fragment_dest_to_home_Fragment_dest)
            }
        })
    }
}