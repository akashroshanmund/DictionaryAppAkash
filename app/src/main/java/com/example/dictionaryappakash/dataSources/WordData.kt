package com.example.dictionaryappakash.dataSources

import com.example.dictionaryappakash.R



data class WordData (
    val word : String?,
    val phoneticText : String?,
    val phoneticAudioUri : String?,
    val origin : String?,
    val partOfSpeech: String?,
    val definition : String?,
    val synonyms : String?
)

