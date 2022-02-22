package com.example.dictionaryappakash

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView

class GridAdapter(
    private val context : Context,
    private val synonyms : List<String>
    ) : BaseAdapter() {

    private var inflater: LayoutInflater? = null
    private lateinit var tvSynonym: TextView

    override fun getCount(): Int {

        return synonyms.size
    }

    override fun getItem(p0: Int): Any? {
       return null

    }

    override fun getItemId(p0: Int): Long {

        return 0
    }

    override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {

        var cview = p1
        if (inflater == null) {
            inflater =
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        }
        if (cview == null) {
            cview = inflater!!.inflate(R.layout.grid_layout, null)
        }

        tvSynonym = cview?.findViewById(R.id.tvSynonym)!!

        tvSynonym.text = synonyms[p0]
        return cview

    }


}