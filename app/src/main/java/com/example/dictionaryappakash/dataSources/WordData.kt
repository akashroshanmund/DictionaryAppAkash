package com.example.dictionaryappakash.dataSources

/* data class to use through the program and to fetch data for UI */
data class WordData (
    override val word : String? = "",
    override val phoneticText : String? = "",
    override val phoneticAudioUri : String?= "",
    override val origin : String? = "",
    override val partOfSpeech: String? = "",
    override val definition : String? = "",
    override val synonyms : String? = ""
) : WordDataInterface

