package com.example.dictionaryappakash.dataSources

import com.example.dictionaryappakash.R



data class WordData (
    override val word : String? = "",
    override val phoneticText : String? = "",
    override val phoneticAudioUri : String?= "",
    override val origin : String? = "",
    override val partOfSpeech: String? = "",
    override val definition : String? = "",
    override val synonyms : String? = ""
) : wordDataInterface

